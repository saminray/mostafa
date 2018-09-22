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
import static org.jooq.SQLDialect.CUBRID;
// ...
import static org.jooq.impl.DSL.one;

import java.util.Arrays;
import java.util.Collection;

import org.jooq.Clause;
import org.jooq.Context;
import org.jooq.Field;
import org.jooq.SortField;
import org.jooq.WindowSpecificationFinalStep;
import org.jooq.WindowSpecificationOrderByStep;
import org.jooq.WindowSpecificationPartitionByStep;
import org.jooq.WindowSpecificationRowsAndStep;

/**
 * @author Lukas Eder
 */
class WindowSpecificationImpl extends AbstractQueryPart implements

    // Cascading interface implementations for window specification behaviour
    WindowSpecificationPartitionByStep,
    WindowSpecificationRowsAndStep
    {

    /**
     * Generated UID
     */
    private static final long             serialVersionUID = 2996016924769376361L;

    private final QueryPartList<Field<?>> partitionBy;
    private final SortFieldList           orderBy;
    private Integer                       rowsStart;
    private Integer                       rowsEnd;
    private boolean                       partitionByOne;

    WindowSpecificationImpl() {
        this.partitionBy = new QueryPartList<Field<?>>();
        this.orderBy = new SortFieldList();
    }

    @Override
    public final void accept(Context<?> ctx) {
        String glue = "";

        if (!partitionBy.isEmpty()) {

            // Ignore PARTITION BY 1 clause. These databases erroneously map the
            // 1 literal onto the column index
            if (partitionByOne && asList(CUBRID).contains(ctx.configuration().dialect())) {
            }
            else {
                ctx.sql(glue)
                       .keyword("partition by").sql(" ")
                       .visit(partitionBy);

                glue = " ";
            }
        }

        if (!orderBy.isEmpty()) {
            ctx.sql(glue)
                   .keyword("order by").sql(" ")
                   .visit(orderBy);

            glue = " ";
        }

        if (rowsStart != null) {
            ctx.sql(glue);
            ctx.keyword("rows").sql(" ");

            if (rowsEnd != null) {
                ctx.keyword("between").sql(" ");
                toSQLRows(ctx, rowsStart);

                ctx.sql(" ").keyword("and").sql(" ");
                toSQLRows(ctx, rowsEnd);
            }
            else {
                toSQLRows(ctx, rowsStart);
            }

            glue = " ";
        }
    }

    private final void toSQLRows(Context<?> ctx, Integer rows) {
        if (rows == Integer.MIN_VALUE) {
            ctx.keyword("unbounded preceding");
        }
        else if (rows == Integer.MAX_VALUE) {
            ctx.keyword("unbounded following");
        }
        else if (rows < 0) {
            ctx.sql(-rows);
            ctx.sql(" ").keyword("preceding");
        }
        else if (rows > 0) {
            ctx.sql(rows);
            ctx.sql(" ").keyword("following");
        }
        else {
            ctx.keyword("current row");
        }
    }

    @Override
    public final Clause[] clauses(Context<?> ctx) {
        return null;
    }

    @Override
    public final WindowSpecificationPartitionByStep partitionBy(Field<?>... fields) {
        return partitionBy(Arrays.asList(fields));
    }

    @Override
    public final WindowSpecificationPartitionByStep partitionBy(Collection<? extends Field<?>> fields) {
        partitionBy.addAll(fields);
        return this;
    }

    @Override
    public final WindowSpecificationOrderByStep partitionByOne() {
        partitionByOne = true;
        partitionBy.add(one());
        return null;
    }

    @Override
    public final WindowSpecificationOrderByStep orderBy(Field<?>... fields) {
        orderBy.addAll(fields);
        return this;
    }

    @Override
    public final WindowSpecificationOrderByStep orderBy(SortField<?>... fields) {
        return orderBy(Arrays.asList(fields));
    }

    @Override
    public final WindowSpecificationOrderByStep orderBy(Collection<? extends SortField<?>> fields) {
        orderBy.addAll(fields);
        return this;
    }

    @Override
    public final WindowSpecificationFinalStep rowsUnboundedPreceding() {
        rowsStart = Integer.MIN_VALUE;
        return this;
    }

    @Override
    public final WindowSpecificationFinalStep rowsPreceding(int number) {
        rowsStart = -number;
        return this;
    }

    @Override
    public final WindowSpecificationFinalStep rowsCurrentRow() {
        rowsStart = 0;
        return this;
    }

    @Override
    public final WindowSpecificationFinalStep rowsUnboundedFollowing() {
        rowsStart = Integer.MAX_VALUE;
        return this;
    }

    @Override
    public final WindowSpecificationFinalStep rowsFollowing(int number) {
        rowsStart = number;
        return this;
    }

    @Override
    public final WindowSpecificationRowsAndStep rowsBetweenUnboundedPreceding() {
        rowsUnboundedPreceding();
        return this;
    }

    @Override
    public final WindowSpecificationRowsAndStep rowsBetweenPreceding(int number) {
        rowsPreceding(number);
        return this;
    }

    @Override
    public final WindowSpecificationRowsAndStep rowsBetweenCurrentRow() {
        rowsCurrentRow();
        return this;
    }

    @Override
    public final WindowSpecificationRowsAndStep rowsBetweenUnboundedFollowing() {
        rowsUnboundedFollowing();
        return this;
    }

    @Override
    public final WindowSpecificationRowsAndStep rowsBetweenFollowing(int number) {
        rowsFollowing(number);
        return this;
    }

    @Override
    public final WindowSpecificationFinalStep andUnboundedPreceding() {
        rowsEnd = Integer.MIN_VALUE;
        return this;
    }

    @Override
    public final WindowSpecificationFinalStep andPreceding(int number) {
        rowsEnd = -number;
        return this;
    }

    @Override
    public final WindowSpecificationFinalStep andCurrentRow() {
        rowsEnd = 0;
        return this;
    }

    @Override
    public final WindowSpecificationFinalStep andUnboundedFollowing() {
        rowsEnd = Integer.MAX_VALUE;
        return this;
    }

    @Override
    public final WindowSpecificationFinalStep andFollowing(int number) {
        rowsEnd = number;
        return this;
    }
}
