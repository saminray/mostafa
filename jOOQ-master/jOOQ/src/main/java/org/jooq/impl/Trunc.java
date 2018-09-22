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

import static java.math.BigDecimal.TEN;
import static org.jooq.impl.DSL.field;
import static org.jooq.impl.DSL.inline;
import static org.jooq.impl.DSL.one;
import static org.jooq.impl.DSL.zero;
import static org.jooq.impl.Utils.extractVal;

import java.math.BigDecimal;
import java.math.MathContext;

import org.jooq.Configuration;
import org.jooq.Field;

/**
 * @author Lukas Eder
 */
class Trunc<T> extends AbstractFunction<T> {

    /**
     * Generated UID
     */
    private static final long    serialVersionUID = 4291348230758816484L;

    private final Field<T>       field;
    private final Field<Integer> decimals;

    Trunc(Field<T> field, Field<Integer> decimals) {
        super("trunc", field.getDataType());

        this.field = field;
        this.decimals = decimals;
    }

    @Override
    final Field<T> getFunction0(Configuration configuration) {
        if (decimals != null) {
            return getNumericFunction(configuration);
        }
        else {
            return getDateTimeFunction(configuration);
        }
    }

    @SuppressWarnings("unused")
    private final Field<T> getDateTimeFunction(Configuration configuration) {
        return null;
    }

    private final Field<T> getNumericFunction(Configuration configuration) {
        switch (configuration.dialect().family()) {
            /* [pro] xx
            xxxx xxxxxxx
            xxxx xxxx
            xx [/pro] */
            case DERBY: {
                Field<BigDecimal> power;

                // [#1334] if possible, calculate the power in Java to prevent
                // inaccurate arithmetics in the Derby database
                Integer decimalsVal = extractVal(decimals);
                if (decimalsVal != null) {
                    power = inline(TEN.pow(decimalsVal, MathContext.DECIMAL128));
                }
                else {
                    power = DSL.power(inline(TEN), decimals);
                }

                return DSL.decode()
                    .when(field.sign().greaterOrEqual(zero()),
                          field.mul(power).floor().div(power))
                    .otherwise(
                          field.mul(power).ceil().div(power));
            }

            case H2:
            case MARIADB:
            case MYSQL:
                return field("{truncate}({0}, {1})", field.getDataType(), field, decimals);

            // Postgres TRUNC() only takes NUMERIC arguments, no
            // DOUBLE PRECISION ones
            case POSTGRES:
                return field("{trunc}({0}, {1})", SQLDataType.NUMERIC, field.cast(BigDecimal.class), decimals).cast(field.getDataType());

            /* [pro] xx
            xx xxx xxxxxxxx xxxxx xxxxxxxx xxx xx xxxx xx xxxxxxxxx
            xxxx xxxxxxxxxx
                xxxxxx xxxxxxxxxxxxxxxxxxx xxxx xxxxxx xxxxxxxxxxxxxxxxxxxx xxxxxx xxxxxxxxx xxxxxxx

            xxxx xxxxxxx
                xxxxxx xxxxxxxxxxxxxxxxxxxxxx xxxxxx xxxxxxxxxxxxxxxxxxxx xxxxxx xxxxxxxxxx

            xx xxxxxx xxxxx xxxx xxx xxxxx xxxxxxxxx xxx xx xxxxx xxxx xx xx xxx
            xx xxxxxxxxxxx xxxxxxxxx xx xxx xxxxxxxx xxxxxxxxx
            xxxx xxxx
            xxxx xxxxxxx
            xxxx xxxxxxx
            xx [/pro] */
            case CUBRID:
            case HSQLDB:
            default:
                return field("{trunc}({0}, {1})", field.getDataType(), field, decimals);
        }
    }
}
