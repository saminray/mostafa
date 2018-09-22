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
import static org.jooq.Clause.DELETE;
import static org.jooq.Clause.DELETE_DELETE;
import static org.jooq.Clause.DELETE_WHERE;
import static org.jooq.SQLDialect.MARIADB;
import static org.jooq.SQLDialect.MYSQL;

import java.util.Collection;

import org.jooq.Clause;
import org.jooq.Condition;
import org.jooq.Configuration;
import org.jooq.Context;
import org.jooq.DeleteQuery;
import org.jooq.Operator;
import org.jooq.Record;
import org.jooq.Table;

/**
 * @author Lukas Eder
 */
class DeleteQueryImpl<R extends Record> extends AbstractQuery implements DeleteQuery<R> {

    private static final long           serialVersionUID = -1943687511774150929L;
    private static final Clause[]       CLAUSES          = { DELETE };

    private final Table<R>              table;
    private final ConditionProviderImpl condition;

    DeleteQueryImpl(Configuration configuration, Table<R> table) {
        super(configuration);

        this.table = table;
        this.condition = new ConditionProviderImpl();
    }

    final Table<R> getFrom() {
        return table;
    }

    final Condition getWhere() {
        return condition.getWhere();
    }

    @Override
    public final void addConditions(Collection<? extends Condition> conditions) {
        condition.addConditions(conditions);
    }

    @Override
    public final void addConditions(Condition... conditions) {
        condition.addConditions(conditions);
    }

    @Override
    public final void addConditions(Operator operator, Condition... conditions) {
        condition.addConditions(operator, conditions);
    }

    @Override
    public final void addConditions(Operator operator, Collection<? extends Condition> conditions) {
        condition.addConditions(operator, conditions);
    }

    @Override
    public final void accept(Context<?> ctx) {
        boolean declare = ctx.declareTables();

        ctx.start(DELETE_DELETE)
           .keyword("delete").sql(" ");

        // [#2464] MySQL supports a peculiar multi-table DELETE syntax for aliased tables:
        // DELETE t1 FROM my_table AS t1
        if (asList(MARIADB, MYSQL).contains(ctx.configuration().dialect())) {

            // [#2579] TODO: Improve Table API to discover aliased tables more
            // reliably instead of resorting to instanceof:
            if (getFrom() instanceof TableAlias ||
               (getFrom() instanceof TableImpl && ((TableImpl<R>)getFrom()).getAliasedTable() != null)) {
                ctx.visit(getFrom())
                   .sql(" ");
            }
        }

        ctx.keyword("from").sql(" ")
           .declareTables(true)
           .visit(getFrom())
           .declareTables(declare)
           .end(DELETE_DELETE)
           .start(DELETE_WHERE);

        if (!(getWhere() instanceof TrueCondition)) {
            ctx.formatSeparator()
               .keyword("where").sql(" ")
               .visit(getWhere());
        }

        ctx.end(DELETE_WHERE);
    }

    @Override
    public final Clause[] clauses(Context<?> ctx) {
        return CLAUSES;
    }
}
