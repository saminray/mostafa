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
import static org.jooq.Clause.CONDITION_COMPARISON;
import static org.jooq.Comparator.LIKE;
import static org.jooq.Comparator.LIKE_IGNORE_CASE;
import static org.jooq.Comparator.NOT_LIKE;
import static org.jooq.Comparator.NOT_LIKE_IGNORE_CASE;
// ...
// ...
import static org.jooq.SQLDialect.DERBY;
import static org.jooq.SQLDialect.POSTGRES;

import org.jooq.Clause;
import org.jooq.Comparator;
import org.jooq.Context;
import org.jooq.Field;
import org.jooq.SQLDialect;

/**
 * @author Lukas Eder
 */
class CompareCondition extends AbstractCondition {

    private static final long     serialVersionUID = -747240442279619486L;
    private static final Clause[] CLAUSES          = { CONDITION, CONDITION_COMPARISON };

    private final Field<?>        field1;
    private final Field<?>        field2;
    private final Comparator      comparator;
    private final Character       escape;

    CompareCondition(Field<?> field1, Field<?> field2, Comparator comparator) {
        this(field1, field2, comparator, null);
    }

    CompareCondition(Field<?> field1, Field<?> field2, Comparator comparator, Character escape) {
        this.field1 = field1;
        this.field2 = field2;
        this.comparator = comparator;
        this.escape = escape;
    }

    @Override
    public final void accept(Context<?> ctx) {
        SQLDialect family = ctx.configuration().dialect().family();
        Field<?> lhs = field1;
        Field<?> rhs = field2;
        Comparator op = comparator;

        // [#1159] Some dialects cannot auto-convert the LHS operand to a
        // VARCHAR when applying a LIKE predicate
        // [#293] TODO: This could apply to other operators, too
        if ((op == LIKE || op == NOT_LIKE)
                && field1.getType() != String.class
                && asList(DERBY, POSTGRES).contains(family)) {

            lhs = lhs.cast(String.class);
        }

        // [#1423] Only Postgres knows a true ILIKE operator. Other dialects
        // need to simulate this as LOWER(lhs) LIKE LOWER(rhs)
        else if ((op == LIKE_IGNORE_CASE || op == NOT_LIKE_IGNORE_CASE)
                && POSTGRES != family) {

            lhs = lhs.lower();
            rhs = rhs.lower();
            op = (op == LIKE_IGNORE_CASE ? LIKE : NOT_LIKE);
        }

        ctx.visit(lhs)
           .sql(" ");

        boolean castRhs = false;

        /* [pro] xx
        xx xxxxxxx xxxx xxxxx xxx xxxxx xxxxx xxxxxx xxxx xxxxxxx xxxx x
        xx xxxxxxxxxxxx xxxxxx xxxxxxxxxxx xx xxx xxxxxxxxxx xx xxxx xxxx xxxx
        xx xxxxxxxxxx xxxx
        xx xxxxxxx xx xxx xx xxx xxxxxxxxxx xxxxxxx
            xxxxxxx x xxxxx
        xx [/pro] */

                     ctx.keyword(op.toSQL()).sql(" ");
        if (castRhs) ctx.keyword("cast").sql("(");
                     ctx.visit(rhs);
        if (castRhs) ctx.sql(" ").keyword("as").sql(" ").keyword("varchar").sql("(4000))");

        if (escape != null) {
            ctx.sql(" ").keyword("escape").sql(" '")
               .sql(escape)
               .sql("'");
        }
    }

    @Override
    public final Clause[] clauses(Context<?> ctx) {
        return CLAUSES;
    }
}
