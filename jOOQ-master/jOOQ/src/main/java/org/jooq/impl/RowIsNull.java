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
import static org.jooq.Clause.CONDITION;
import static org.jooq.Clause.CONDITION_IS_NOT_NULL;
import static org.jooq.Clause.CONDITION_IS_NULL;
// ...
import static org.jooq.SQLDialect.CUBRID;
// ...
import static org.jooq.SQLDialect.DERBY;
import static org.jooq.SQLDialect.FIREBIRD;
import static org.jooq.SQLDialect.H2;
import static org.jooq.SQLDialect.HSQLDB;
// ...
import static org.jooq.SQLDialect.MARIADB;
import static org.jooq.SQLDialect.MYSQL;
// ...
import static org.jooq.SQLDialect.SQLITE;
// ...
// ...

import java.util.ArrayList;
import java.util.List;

import org.jooq.Clause;
import org.jooq.Condition;
import org.jooq.Configuration;
import org.jooq.Context;
import org.jooq.Field;
import org.jooq.Operator;
import org.jooq.QueryPartInternal;
import org.jooq.Row;

/**
 * @author Lukas Eder
 */
class RowIsNull extends AbstractCondition {

    /**
     * Generated UID
     */
    private static final long     serialVersionUID = -1806139685201770706L;
    private static final Clause[] CLAUSES_NULL     = { CONDITION, CONDITION_IS_NULL };
    private static final Clause[] CLAUSES_NOT_NULL = { CONDITION, CONDITION_IS_NOT_NULL };

    private final Row             row;
    private final boolean         isNull;

    RowIsNull(Row row, boolean isNull) {
        this.row = row;
        this.isNull = isNull;
    }

    @Override
    public final void accept(Context<?> ctx) {
        delegate(ctx.configuration()).accept(ctx);
    }

    @Override
    public final Clause[] clauses(Context<?> ctx) {
        return delegate(ctx.configuration()).clauses(ctx);
    }

    private final QueryPartInternal delegate(Configuration configuration) {

        // CUBRID 9.0.0 and HSQLDB have buggy implementations of the NULL predicate.
        // Let's wait for them to be fixed
        if (asList(CUBRID, DERBY, FIREBIRD, H2, HSQLDB, MARIADB, MYSQL, SQLITE).contains(configuration.dialect().family())) {
            List<Condition> conditions = new ArrayList<Condition>();

            for (Field<?> field : row.fields()) {
                conditions.add(isNull ? field.isNull() : field.isNotNull());
            }

            Condition result = new CombinedCondition(Operator.AND, conditions);
            return (QueryPartInternal) result;
        }
        else {
            return new Native();
        }
    }

    private class Native extends AbstractCondition {

        /**
         * Generated UID
         */
        private static final long serialVersionUID = -2977241780111574353L;

        @Override
        public final void accept(Context<?> ctx) {
            ctx.visit(row)
               .sql(" ")
               .keyword(isNull ? "is null" : "is not null");
        }

        @Override
        public final Clause[] clauses(Context<?> ctx) {
            return isNull ? CLAUSES_NULL : CLAUSES_NOT_NULL;
        }
    }
}