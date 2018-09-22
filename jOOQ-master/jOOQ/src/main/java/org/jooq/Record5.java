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
package org.jooq;

import javax.annotation.Generated;

/**
 * A model type for a records with degree <code>5</code>
 *
 * @see Row5
 * @author Lukas Eder
 */
@Generated("This class was generated using jOOQ-tools")
public interface Record5<T1, T2, T3, T4, T5> extends Record {

    // ------------------------------------------------------------------------
    // Row value expressions
    // ------------------------------------------------------------------------

    /**
     * Get this record's fields as a {@link Row5}.
     */
    @Override
    Row5<T1, T2, T3, T4, T5> fieldsRow();

    /**
     * Get this record's values as a {@link Row5}.
     */
    @Override
    Row5<T1, T2, T3, T4, T5> valuesRow();

    // ------------------------------------------------------------------------
    // Field accessors
    // ------------------------------------------------------------------------

    /**
     * Get the first field.
     */
    Field<T1> field1();

    /**
     * Get the second field.
     */
    Field<T2> field2();

    /**
     * Get the third field.
     */
    Field<T3> field3();

    /**
     * Get the fourth field.
     */
    Field<T4> field4();

    /**
     * Get the fifth field.
     */
    Field<T5> field5();

    // ------------------------------------------------------------------------
    // Value accessors
    // ------------------------------------------------------------------------

    /**
     * Get the first value.
     */
    T1 value1();

    /**
     * Get the second value.
     */
    T2 value2();

    /**
     * Get the third value.
     */
    T3 value3();

    /**
     * Get the fourth value.
     */
    T4 value4();

    /**
     * Get the fifth value.
     */
    T5 value5();

    /**
     * Set the first value.
     */
    Record5<T1, T2, T3, T4, T5> value1(T1 value);

    /**
     * Set the second value.
     */
    Record5<T1, T2, T3, T4, T5> value2(T2 value);

    /**
     * Set the third value.
     */
    Record5<T1, T2, T3, T4, T5> value3(T3 value);

    /**
     * Set the fourth value.
     */
    Record5<T1, T2, T3, T4, T5> value4(T4 value);

    /**
     * Set the fifth value.
     */
    Record5<T1, T2, T3, T4, T5> value5(T5 value);

    /**
     * Set all values.
     */
    Record5<T1, T2, T3, T4, T5> values(T1 t1, T2 t2, T3 t3, T4 t4, T5 t5);

}
