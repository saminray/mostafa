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

import org.jooq.api.annotation.State;
import org.jooq.api.annotation.Transition;

/**
 * A model type for a row value expression.
 * <p>
 * Note: Not all databases support row value expressions, but many row value
 * expression operations can be simulated on all databases. See relevant row
 * value expression method Javadocs for details.
 *
 * @author Lukas Eder
 */
@State(terminal = true)
public interface Row extends QueryPart {

    /**
     * Get the degree of this row value expression.
     */
    int size();

    /**
     * Get a specific field from this row.
     * <p>
     * Usually, this will return the field itself. However, if this is a row
     * from an aliased table, the field will be aliased accordingly.
     *
     * @param <T> The generic field type
     * @param field The field to fetch
     * @return The field itself or an aliased field
     */
    <T> Field<T> field(Field<T> field);

    /**
     * Get a specific field from this row.
     *
     * @param fieldName The field to fetch
     * @return The field with the given name
     */
    Field<?> field(String fieldName);

    /**
     * Get a specific field from this row.
     *
     * @param fieldIndex The field's index of the field to fetch
     * @return The field with the given name
     */
    Field<?> field(int fieldIndex);

    /**
     * Get all fields from this row.
     *
     * @return All available fields
     */
    Field<?>[] fields();

    /**
     * Get all fields from this row, providing some fields.
     *
     * @return All available fields
     * @see #field(Field)
     */
    Field<?>[] fields(Field<?>... fields);

    /**
     * Get all fields from this row, providing some field names.
     *
     * @return All available fields
     * @see #field(String)
     */
    Field<?>[] fields(String... fieldNames);

    /**
     * Get all fields from this row, providing some field indexes.
     *
     * @return All available fields
     * @see #field(int)
     */
    Field<?>[] fields(int... fieldIndexes);

    /**
     * Get a field's index from this row.
     *
     * @param field The field to look for
     * @return The field's index or <code>-1</code> if the field is not
     *         contained in this <code>Row</code>
     */
    int indexOf(Field<?> field);

    /**
     * Get a field's index from this row.
     *
     * @param fieldName The field name to look for
     * @return The field's index or <code>-1</code> if the field is not
     *         contained in this <code>Row</code>
     */
    int indexOf(String fieldName);

    /**
     * Get an array of types for this row.
     * <p>
     * Entries in the resulting array correspond to {@link Field#getType()} for
     * the corresponding <code>Field</code> in {@link #fields()}
     */
    Class<?>[] types();

    /**
     * Get the type for a given field index.
     *
     * @param fieldIndex The field's index of the field's type to fetch
     * @return The field's type
     */
    Class<?> type(int fieldIndex);

    /**
     * Get the type for a given field name.
     *
     * @param fieldName The field's name of the field's type to fetch
     * @return The field's type
     */
    Class<?> type(String fieldName);

    /**
     * Get an array of data types for this row.
     * <p>
     * Entries in the resulting array correspond to {@link Field#getDataType()}
     * for the corresponding <code>Field</code> in {@link #fields()}
     */
    DataType<?>[] dataTypes();

    /**
     * Get the data type for a given field index.
     *
     * @param fieldIndex The field's index of the field's data type to fetch
     * @return The field's data type
     */
    DataType<?> dataType(int fieldIndex);

    /**
     * Get the data type for a given field name.
     *
     * @param fieldName The field's name of the field's data type to fetch
     * @return The field's data type
     */
    DataType<?> dataType(String fieldName);

    // ------------------------------------------------------------------------
    // [NOT] NULL predicates
    // ------------------------------------------------------------------------

    /**
     * Check if this row value expression contains only <code>NULL</code>
     * values.
     * <p>
     * Row NULL predicates can be simulated in those databases that do not
     * support such predicates natively: <code>(A, B) IS NULL</code> is
     * equivalent to <code>A IS NULL AND B IS NULL</code>
     */
    @Support
    @Transition(
        name = "IS NULL",
        to = "NullPredicate"
    )
    Condition isNull();

    /**
     * Check if this row value expression contains no <code>NULL</code> values.
     * <p>
     * Row NOT NULL predicates can be simulated in those databases that do not
     * support such predicates natively: <code>(A, B) IS NOT NULL</code> is
     * equivalent to <code>A IS NOT NULL AND B IS NOT NULL</code>
     * <p>
     * Note that the two following predicates are NOT equivalent:
     * <ul>
     * <li><code>(A, B) IS NOT NULL</code>, which is the same as
     * <code>(A IS NOT NULL) AND (B IS NOT NULL)</code></li>
     * <li><code>NOT((A, B) IS NULL)</code>, which is the same as
     * <code>(A IS NOT NULL) OR (B IS NOT NULL)</code></li>
     * </ul>
     */
    @Support
    @Transition(
        name = "IS NOT NULL",
        to = "NullPredicate"
    )
    Condition isNotNull();

}
