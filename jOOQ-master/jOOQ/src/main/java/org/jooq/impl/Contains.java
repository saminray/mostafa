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

import static org.jooq.Clause.CONDITION;
import static org.jooq.Clause.CONDITION_COMPARISON;
import static org.jooq.impl.DSL.inline;
import static org.jooq.impl.DSL.val;

import org.jooq.Clause;
import org.jooq.Condition;
import org.jooq.Configuration;
import org.jooq.Context;
import org.jooq.Field;

/**
 * Abstraction for various "contains" operations
 *
 * @author Lukas Eder
 */
class Contains<T> extends AbstractCondition {

    /**
     * Generated UID
     */
    private static final long     serialVersionUID = 6146303086487338550L;
    private static final Clause[] CLAUSES          = { CONDITION, CONDITION_COMPARISON };

    private final Field<T>        lhs;
    private final Field<T>        rhs;
    private final T               value;

    Contains(Field<T> field, T value) {
        this.lhs = field;
        this.rhs = null;
        this.value = value;
    }

    Contains(Field<T> field, Field<T> rhs) {
        this.lhs = field;
        this.rhs = rhs;
        this.value = null;
    }

    @Override
    public final void accept(Context<?> ctx) {
        ctx.visit(condition(ctx.configuration()));
    }

    @Override
    public final Clause[] clauses(Context<?> ctx) {
        return CLAUSES;
    }

    private final Condition condition(Configuration configuration) {

        // [#1107] Some dialects support "contains" operations for ARRAYs
        if (lhs.getDataType().isArray()) {
            return new PostgresArrayContains();
        }

        // "contains" operations on Strings
        else {
            Field<String> concat;

            if (rhs == null) {
                concat = DSL.concat(inline("%"), Utils.escapeForLike(value, configuration), inline("%"));
            }
            else {
                concat = DSL.concat(inline("%"), Utils.escapeForLike(rhs, configuration), inline("%"));
            }

            return lhs.like(concat, Utils.ESCAPE);
        }
    }

    /**
     * The Postgres array contains operator
     */
    private class PostgresArrayContains extends AbstractCondition {

        /**
         * Generated UID
         */
        private static final long serialVersionUID = 8083622843635168388L;

        @Override
        public final void accept(Context<?> ctx) {
            ctx.visit(lhs).sql(" @> ").visit(rhs());
        }

        @Override
        public final Clause[] clauses(Context<?> ctx) {
            return CLAUSES;
        }

        private final Field<T> rhs() {
            return (rhs == null) ? val(value, lhs) : rhs;
        }
    }
}
