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

import java.util.ArrayList;
import java.util.List;

import org.jooq.CaseWhenStep;
import org.jooq.Clause;
import org.jooq.Configuration;
import org.jooq.Context;
import org.jooq.Field;
import org.jooq.QueryPart;

class CaseWhenStepImpl<V, T> extends AbstractFunction<T> implements CaseWhenStep<V, T> {

    /**
     * Generated UID
     */
    private static final long    serialVersionUID = -3817194006479624228L;

    private final Field<V>       value;
    private final List<Field<V>> compareValues;
    private final List<Field<T>> results;
    private Field<T>             otherwise;

    CaseWhenStepImpl(Field<V> value, Field<V> compareValue, Field<T> result) {
        super("case", result.getDataType());

        this.value = value;
        this.compareValues = new ArrayList<Field<V>>();
        this.results = new ArrayList<Field<T>>();

        when(compareValue, result);
    }

    @Override
    public final Field<T> otherwise(T result) {
        return otherwise(Utils.field(result));
    }

    @Override
    public final Field<T> otherwise(Field<T> result) {
        this.otherwise = result;

        return this;
    }

    @Override
    public final CaseWhenStep<V, T> when(V compareValue, T result) {
        return when(Utils.field(compareValue), Utils.field(result));
    }

    @Override
    public final CaseWhenStep<V, T> when(V compareValue, Field<T> result) {
        return when(Utils.field(compareValue), result);
    }

    @Override
    public final CaseWhenStep<V, T> when(Field<V> compareValue, T result) {
        return when(compareValue, Utils.field(result));
    }

    @Override
    public final CaseWhenStep<V, T> when(Field<V> compareValue, Field<T> result) {
        compareValues.add(compareValue);
        results.add(result);

        return this;
    }

    @Override
    final QueryPart getFunction0(Configuration configuration) {
        switch (configuration.dialect().family()) {
            /* [pro] xx
            xxxx xxxxxxx
                xxxxxx xxx xxxxxxxxx

            xx [/pro] */
            default:
                return new Native();
        }
    }

    private abstract class Base extends AbstractQueryPart {

        /**
         * Generated UID
         */
        private static final long serialVersionUID = 6146002888421945901L;

        @Override
        public final Clause[] clauses(Context<?> ctx) {
            return null;
        }
    }

    /* [pro] xx
    xxxxxxx xxxxx xxxxxx xxxxxxx xxxx x

        xxx
         x xxxxxxxxx xxx
         xx
        xxxxxxx xxxxxx xxxxx xxxx xxxxxxxxxxxxxxxx x xxxxxxxxxxxxxxxxxxxxxx

        xxxxxxxxx
        xxxxxx xxxxx xxxx xxxxxxxxxxxxxxxxx xxxx x
            xxxxxxxxxxxxxxxxxxxxx
               xxxxxxxxxx

            xxx xxxx x x xx x x xxxxxxxxxxxxxxxxxxxxx xxxx x
                xx xx x xx x
                    xxxxxxxxxx xxx
                x

                xxxxxxxxxxxxxxxx
                   xxxxxx x xx
                   xxxxxxxxxxxxxxxxxxxxxxxxxxxx
                   xxxxxxx xx
                   xxxxxxxxxxxxxxxxxxxxxxx
            x

            xx xxxxxxxxxx xx xxxxx x
                xxxxxxxxxx xx
                   xxxxxxxxxxxxxxxx
                   xxxxxxx xx
                   xxxxxxxxxxxxxxxxxx
            x

            xxxxxxxxxxxxx
        x
    x

    xx [/pro] */

    private class Native extends Base {

        /**
         * Generated UID
         */
        private static final long serialVersionUID = 7564667836130498156L;

        @Override
        public final void accept(Context<?> ctx) {
            ctx.formatIndentLockStart()
               .keyword("case");

            int size = compareValues.size();
            switch (ctx.configuration().dialect()) {

                // The DERBY dialect doesn't support the simple CASE clause
                case DERBY: {
                    ctx.formatIndentLockStart();

                    for (int i = 0; i < size; i++) {
                        if (i > 0) {
                            ctx.formatNewLine();
                        }

                        ctx.sql(" ").keyword("when").sql(" ");
                        ctx.visit(value.equal(compareValues.get(i)));
                        ctx.sql(" ").keyword("then").sql(" ");
                        ctx.visit(results.get(i));
                    }

                    break;
                }

                default: {
                    ctx.sql(" ")
                       .visit(value)
                       .formatIndentLockStart();

                    for (int i = 0; i < size; i++) {
                        if (i > 0) {
                            ctx.formatNewLine();
                        }

                        ctx.sql(" ").keyword("when").sql(" ");
                        ctx.visit(compareValues.get(i));
                        ctx.sql(" ").keyword("then").sql(" ");
                        ctx.visit(results.get(i));
                    }

                    break;
                }
            }

            if (otherwise != null) {
                ctx.formatNewLine()
                   .sql(" ").keyword("else").sql(" ").visit(otherwise);
            }

            ctx.formatIndentLockEnd();

            if (size > 1 || otherwise != null) {
                ctx.formatSeparator();
            }
            else {
                ctx.sql(" ");
            }

            ctx.keyword("end")
               .formatIndentLockEnd();
        }
    }
}
