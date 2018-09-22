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

import static org.jooq.impl.DSL.function;

import org.jooq.CaseConditionStep;
import org.jooq.Configuration;
import org.jooq.Field;

/**
 * @author Lukas Eder
 */
class Decode<T, Z> extends AbstractFunction<Z> {

    /**
     * Generated UID
     */
    private static final long serialVersionUID = -7273879239726265322L;

    private final Field<T>    field;
    private final Field<T>    search;
    private final Field<Z>    result;
    private final Field<?>[]  more;

    public Decode(Field<T> field, Field<T> search, Field<Z> result, Field<?>[] more) {
        super("decode", result.getDataType(), Utils.combine(field, search, result, more));

        this.field = field;
        this.search = search;
        this.result = result;
        this.more = more;
    }

    @SuppressWarnings("unchecked")
    @Override
    final Field<Z> getFunction0(Configuration configuration) {
        switch (configuration.dialect().family()) {

            /* [pro] xx
            xx xxxxxx xxxxxxxx xxx xxxx xxxxxxxx
            xxxx xxxxxxx x
                xxxxxx xxxxxxxxxxxxxxxxxx xxxxxxxxxxxxxx xxxxxxxxxxxxxxxx
            x

            xx [/pro] */
            // Other dialects simulate it with a CASE ... WHEN expression
            default: {
                CaseConditionStep<Z> when = DSL
                    .decode()
                    .when(field.isNotDistinctFrom(search), result);

                for (int i = 0; i < more.length; i += 2) {

                    // search/result pair
                    if (i + 1 < more.length) {
                        when = when.when(field.isNotDistinctFrom((Field<T>) more[i]), (Field<Z>) more[i + 1]);
                    }

                    // trailing default value
                    else {
                        return when.otherwise((Field<Z>) more[i]);
                    }
                }

                return when;
            }
        }
    }
}
