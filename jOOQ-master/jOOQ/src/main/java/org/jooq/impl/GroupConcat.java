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

import static org.jooq.impl.DSL.inline;

import java.util.Arrays;
import java.util.Collection;

import org.jooq.AggregateFunction;
import org.jooq.Configuration;
import org.jooq.Field;
import org.jooq.GroupConcatOrderByStep;
import org.jooq.GroupConcatSeparatorStep;
import org.jooq.Name;
import org.jooq.SortField;
import org.jooq.WindowBeforeOverStep;
import org.jooq.WindowDefinition;
import org.jooq.WindowFinalStep;
import org.jooq.WindowPartitionByStep;
import org.jooq.WindowSpecification;

/**
 * @author Lukas Eder
 */
class GroupConcat extends AbstractFunction<String> implements GroupConcatOrderByStep {

    /**
     * Generated UID
     */
    private static final long   serialVersionUID = -6884415527559632960L;

    private final Field<?>      field;
    private final boolean       distinct;
    private final SortFieldList orderBy;
    private String              separator;

    GroupConcat(Field<?> field) {
        this(field, false);
    }

    GroupConcat(Field<?> field, boolean distinct) {
        super("group_concat", SQLDataType.VARCHAR);

        this.field = field;
        this.distinct = distinct;
        this.orderBy = new SortFieldList();
    }

    @Override
    final Field<String> getFunction0(Configuration configuration) {
        Function<String> result;

        if (separator == null) {
            result = new Function<String>(Term.LIST_AGG, distinct, SQLDataType.VARCHAR, field);
        }
        else {
            Field<String> literal = inline(separator);
            result = new Function<String>(Term.LIST_AGG, distinct, SQLDataType.VARCHAR, field, literal);
        }

        return result.withinGroupOrderBy(orderBy);
    }

    /* [pro] xx
    xxxxxxxxx
    xxxxxx xxxxx xxxxxxxxxxxxxxxxxxxxxxxxxxxx xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx xxxxxxx x
        xxxxx xxx xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx xxx xxxxxxxxx xx xxxxxxxxxxxx xxxxxxxxx xxxxxxxxxxx
    x

    xxxxxxxxx
    xxxxxx xxxxx xxxxxxxxxxxxxxxxxxxxxxxxxxxx xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx xxxxxxx x
        xxxxx xxx xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx xxx xxxxxxxxx xx xxxxxxxxxxxx xxxxxxxxx xxxxxxxxxxx
    x

    xxxxxxxxx
    xxxxxx xxxxx xxxxxxxxxxxxxxxxxxxxxxxxxxxx xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx xxxxxxx xxxxxxxxxxxxx xxxxxxx x
        xxxxx xxx xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx xxx xxxxxxxxx xx xxxxxxxxxxxx xxxxxxxxx xxxxxxxxxxx
    x

    xxxxxxxxx
    xxxxxx xxxxx xxxxxxxxxxxxxxxxxxxxxxxxxxxx xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx xxxxxxx x
        xxxxx xxx xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx xxx xxxxxxxxx xx xxxxxxxxxxxx xxxxxxxxx xxxxxxxxxxx
    x

    xxxxxxxxx
    xxxxxx xxxxx xxxxxxxxxxxxxxxxxxxxxxxxxxxx xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx xxxxxxx x
        xxxxx xxx xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx xxx xxxxxxxxx xx xxxxxxxxxxxx xxxxxxxxx xxxxxxxxxxx
    x

    xxxxxxxxx
    xxxxxx xxxxx xxxxxxxxxxxxxxxxxxxxxxxxxxxx xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx xxxxxxx xxxxxxxxxxxxx xxxxxxx x
        xxxxx xxx xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx xxx xxxxxxxxx xx xxxxxxxxxxxx xxxxxxxxx xxxxxxxxxxx
    x

    xx [/pro] */
    @Override
    public final WindowPartitionByStep<String> over() {
        throw new UnsupportedOperationException("OVER() not supported on GROUP_CONCAT aggregate function");
    }

    @Override
    public final WindowFinalStep<String> over(WindowSpecification specification) {
        throw new UnsupportedOperationException("OVER() not supported on GROUP_CONCAT aggregate function");
    }

    @Override
    public final WindowFinalStep<String> over(WindowDefinition definition) {
        throw new UnsupportedOperationException("OVER() not supported on GROUP_CONCAT aggregate function");
    }

    @Override
    public final WindowFinalStep<String> over(Name name) {
        throw new UnsupportedOperationException("OVER() not supported on GROUP_CONCAT aggregate function");
    }

    @Override
    public final WindowFinalStep<String> over(String name) {
        throw new UnsupportedOperationException("OVER() not supported on GROUP_CONCAT aggregate function");
    }

    @Override
    public final AggregateFunction<String> separator(String s) {
        this.separator = s;
        return this;
    }

    @Override
    public final GroupConcatSeparatorStep orderBy(Field<?>... fields) {
        orderBy.addAll(fields);
        return this;
    }

    @Override
    public final GroupConcatSeparatorStep orderBy(SortField<?>... fields) {
        orderBy.addAll(Arrays.asList(fields));
        return this;
    }

    @Override
    public final GroupConcatSeparatorStep orderBy(Collection<? extends SortField<?>> fields) {
        orderBy.addAll(fields);
        return this;
    }
}
