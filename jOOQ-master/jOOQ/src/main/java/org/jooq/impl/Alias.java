/**
 * Copyright (c) 2009-2014, Data Geekery GmbH (http://www.datageekery.com)
 * All rights reserved.
 *
 * This work is dual-licensed
 * - under the Apache Software License 2.0 (the "ASL")
 * - under the jOOQ License and Maintenance Agreement (the "jOOQ License")
 * =============================================================================
 * You may choose which license applies to you:
 *
 * - If you're using this work with Open Source databases, you may choose
 *   either ASL or jOOQ License.
 * - If you're using this work with at least one commercial database, you must
 *   choose jOOQ License
 *
 * For more information, please visit http://www.jooq.org/licenses
 *
 * Apache Software License 2.0:
 * -----------------------------------------------------------------------------
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *  http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 * jOOQ License and Maintenance Agreement:
 * -----------------------------------------------------------------------------
 * Data Geekery grants the Customer the non-exclusive, timely limited and
 * non-transferable license to install and use the Software under the terms of
 * the jOOQ License and Maintenance Agreement.
 *
 * This library is distributed with a LIMITED WARRANTY. See the jOOQ License
 * and Maintenance Agreement for more details: http://www.jooq.org/licensing
 */

package org.jooq.impl;

import static java.util.Arrays.asList;
import static org.jooq.Clause.FIELD;
import static org.jooq.Clause.FIELD_ALIAS;
import static org.jooq.Clause.FIELD_REFERENCE;
import static org.jooq.Clause.TABLE;
import static org.jooq.Clause.TABLE_ALIAS;
import static org.jooq.Clause.TABLE_REFERENCE;
// ...
import static org.jooq.SQLDialect.CUBRID;
import static org.jooq.SQLDialect.DERBY;
import static org.jooq.SQLDialect.FIREBIRD;
import static org.jooq.SQLDialect.H2;
import static org.jooq.SQLDialect.HSQLDB;
// ...
import static org.jooq.SQLDialect.MARIADB;
import static org.jooq.SQLDialect.MYSQL;
// ...
import static org.jooq.SQLDialect.POSTGRES;
import static org.jooq.SQLDialect.SQLITE;
// ...
// ...
import static org.jooq.impl.DSL.falseCondition;
import static org.jooq.impl.DSL.field;
import static org.jooq.impl.DSL.select;
import static org.jooq.impl.Utils.list;

import org.jooq.Clause;
import org.jooq.Context;
import org.jooq.QueryPart;
import org.jooq.Record;
import org.jooq.SQLDialect;
import org.jooq.Select;
import org.jooq.Table;

/**
 * @author Lukas Eder
 */
class Alias<Q extends QueryPart> extends AbstractQueryPart {

    private static final long     serialVersionUID        = -2456848365524191614L;
    private static final Clause[] CLAUSES_TABLE_REFERENCE = { TABLE, TABLE_REFERENCE };
    private static final Clause[] CLAUSES_TABLE_ALIAS     = { TABLE, TABLE_ALIAS };
    private static final Clause[] CLAUSES_FIELD_REFERENCE = { FIELD, FIELD_REFERENCE };
    private static final Clause[] CLAUSES_FIELD_ALIAS     = { FIELD, FIELD_ALIAS };

    private final Q               wrapped;
    private final String          alias;
    private final String[]        fieldAliases;
    private final boolean         wrapInParentheses;

    Alias(Q wrapped, String alias) {
        this(wrapped, alias, null, false);
    }

    Alias(Q wrapped, String alias, boolean wrapInParentheses) {
        this(wrapped, alias, null, wrapInParentheses);
    }

    Alias(Q wrapped, String alias, String[] fieldAliases) {
        this(wrapped, alias, fieldAliases, false);
    }

    Alias(Q wrapped, String alias, String[] fieldAliases, boolean wrapInParentheses) {
        this.wrapped = wrapped;
        this.alias = alias;
        this.fieldAliases = fieldAliases;
        this.wrapInParentheses = wrapInParentheses;
    }

    final Q wrapped() {
        return wrapped;
    }

    @Override
    public final void accept(Context<?> context) {
        if (context.declareFields() || context.declareTables()) {
            SQLDialect dialect = context.configuration().dialect();
            boolean simulateDerivedColumnList = false;

            // [#454] [#1801] Some databases don't allow "derived column names" in
            // "simple class specifications", or "common table expression references".
            // Hence, wrap the table reference in a subselect
            if (fieldAliases != null
                    && asList(CUBRID, FIREBIRD).contains(dialect.family())
                    && (wrapped instanceof TableImpl || wrapped instanceof CommonTableExpressionImpl)) {

                @SuppressWarnings("unchecked")
                Select<Record> select =
                    select(list(field("*"))).from(((Table<?>) wrapped).as(alias));

                context.sql("(").formatIndentStart().formatNewLine()
                       .visit(select).formatIndentEnd().formatNewLine()
                       .sql(")");
            }

            // [#1801] Some databases do not support "derived column names".
            // They can be simulated by concatenating a dummy SELECT with no
            // results using UNION ALL
            else if (fieldAliases != null && asList(H2, MARIADB, MYSQL, SQLITE).contains(dialect.family())) {
                simulateDerivedColumnList = true;

                SelectFieldList fields = new SelectFieldList();
                for (String fieldAlias : fieldAliases) {

                    switch (dialect.family()) {

                        /* [pro] xx
                        xxxx xxxxxxx x
                            xx xxxxxxxxxxx xx xxxxxx xxxxxxx xxxx xxxxx xxxxxxxxx xxxxx xx x xxxxxxx xxxxxxxxx
                            xx xx xxx xxxxx xxxxxxxxx xxxxxxx xx xxxxx
                            xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx
                            xxxxxx
                        x
                        xx [/pro] */

                        default: {
                            fields.add(field("null").as(fieldAlias));
                            break;
                        }
                    }
                }

                Select<Record> select = select(fields).where(falseCondition())
                .unionAll(

                    // [#3156] Do not SELECT * from derived tables to prevent ambiguously defined columns
                    // in those derived tables
                      wrapped instanceof Select
                    ? (Select<?>) wrapped
                    : wrapped instanceof SelectQueryAsTable
                    ? ((SelectQueryAsTable<?>) wrapped).query()
                    : select(field("*")).from(((Table<?>) wrapped).as(alias))

                );

                context.sql("(").formatIndentStart().formatNewLine()
                       .visit(select).formatIndentEnd().formatNewLine()
                       .sql(")");
            }

            // The default behaviour
            else {
                toSQLWrapped(context);
            }

            // [#291] some aliases cause trouble, if they are not explicitly marked using "as"
            toSQLAs(context);

            context.sql(" ");
            context.literal(alias);

            // [#1801] Add field aliases to the table alias, if applicable
            if (fieldAliases != null && !simulateDerivedColumnList) {
                toSQLDerivedColumnList(context);
            }

            else {
                // [#756] If the aliased object is an anonymous table (usually an
                // unnested array), then field names must be part of the alias
                // declaration. For example:
                //
                // SELECT t.column_value FROM UNNEST(ARRAY[1, 2]) AS t(column_value)

                // TODO: Is this still needed?
                switch (dialect) {
                    case HSQLDB:
                    case POSTGRES: {
                        // The javac compiler doesn't like casting of generics
                        Object o = wrapped;

                        if (context.declareTables() && o instanceof ArrayTable) {
                            ArrayTable table = (ArrayTable) o;

                            context.sql("(");
                            Utils.fieldNames(context, table.fields());
                            context.sql(")");
                        }

                        break;
                    }
                }
            }
        }
        else {
            context.literal(alias);
        }
    }

    static void toSQLAs(Context<?> context) {
        if (asList(DERBY, HSQLDB, MARIADB, MYSQL, POSTGRES).contains(context.configuration().dialect())) {
            context.sql(" ").keyword("as");
        }
    }

    private void toSQLWrapped(Context<?> context) {
        context.sql(wrapInParentheses ? "(" : "")
               .visit(wrapped)
               .sql(wrapInParentheses ? ")" : "");
    }

    private void toSQLDerivedColumnList(Context<?> context) {
        String separator = "";

        context.sql("(");

        for (int i = 0; i < fieldAliases.length; i++) {
            context.sql(separator);
            context.literal(fieldAliases[i]);

            separator = ", ";
        }

        context.sql(")");
    }

    @Override
    public final Clause[] clauses(Context<?> ctx) {
        if (ctx.declareFields() || ctx.declareTables()) {
            if (wrapped instanceof Table)
                return CLAUSES_TABLE_ALIAS;
            else
                return CLAUSES_FIELD_ALIAS;
        }
        else {
            if (wrapped instanceof Table)
                return CLAUSES_TABLE_REFERENCE;
            else
                return CLAUSES_FIELD_REFERENCE;
        }
    }

    @Override
    public final boolean declaresFields() {
        return true;
    }

    @Override
    public final boolean declaresTables() {
        return true;
    }
}
