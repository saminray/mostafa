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

import static org.jooq.impl.DSL.nvl2;
import static org.jooq.impl.DSL.one;
import static org.jooq.impl.DSL.zero;

import org.jooq.Clause;
import org.jooq.Context;
import org.jooq.Field;
import org.jooq.SortField;
import org.jooq.SortOrder;

class SortFieldImpl<T> extends AbstractQueryPart implements SortField<T> {

    /**
     * Generated UID
     */
    private static final long serialVersionUID = 1223739398544155873L;

    private final Field<T>    field;
    private final SortOrder   order;
    private boolean           nullsFirst;
    private boolean           nullsLast;

    SortFieldImpl(Field<T> field, SortOrder order) {
        this.field = field;
        this.order = order;
    }

    @Override
    public final String getName() {
        return field.getName();
    }

    @Override
    public final SortOrder getOrder() {
        return order;
    }

    final Field<T> getField() {
        return field;
    }

    final boolean getNullsFirst() {
        return nullsFirst;
    }

    final boolean getNullsLast() {
        return nullsLast;
    }

    @Override
    public final SortField<T> nullsFirst() {
        nullsFirst = true;
        nullsLast = false;
        return this;
    }

    @Override
    public final SortField<T> nullsLast() {
        nullsFirst = false;
        nullsLast = true;
        return this;
    }

    @Override
    public final void accept(Context<?> ctx) {
        if (nullsFirst || nullsLast) {
            switch (ctx.configuration().dialect().family()) {

                /* [pro] xx
                xx xxx xxxxxxxx xxxxx xxxxxxxxxx xxxx xx xxxx xxxxxxxx xxxxxxxxx
                xxxx xxxx

                xx xxxxx xxxxxxxxxx xxxxxxxx xxxxx xxxxxxx xxxx xxxxxx xx xxx
                xxxx xxxxxxx
                xxxx xxxx
                xxxx xxxxxxx
                xxxx xxxxxxxxxx
                xxxx xxxxxxx
                xx [/pro] */

                // These OSS dialects don't support this syntax at all
                case CUBRID:
                case MARIADB:
                case MYSQL:
                case SQLITE: {
                    Field<Integer> ifNull = nullsFirst ? zero() : one();
                    Field<Integer> ifNotNull = nullsFirst ? one() : zero();

                    ctx.visit(nvl2(field, ifNotNull, ifNull))
                       .sql(", ")
                       .visit(field)
                       .sql(" ")
                       .keyword(order.toSQL());

                    break;
                }

                // DERBY, H2, HSQLDB, ORACLE, POSTGRES
                default: {
                    ctx.visit(field)
                       .sql(" ")
                       .keyword(order.toSQL());

                    if (nullsFirst) {
                        ctx.sql(" ").keyword("nulls first");
                    }
                    else {
                        ctx.sql(" ").keyword("nulls last");
                    }

                    break;
                }
            }
        }
        else {
            ctx.visit(field)
               .sql(" ")
               .keyword(order.toSQL());
        }
    }

    @Override
    public final Clause[] clauses(Context<?> ctx) {
        return null;
    }
}
