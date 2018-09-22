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

// ...
import static org.jooq.SQLDialect.CUBRID;
// ...
import static org.jooq.SQLDialect.DERBY;
import static org.jooq.SQLDialect.FIREBIRD;
import static org.jooq.SQLDialect.H2;
import static org.jooq.SQLDialect.HSQLDB;
// ...
import static org.jooq.SQLDialect.MARIADB;
import static org.jooq.SQLDialect.MYSQL;
// ...
import static org.jooq.SQLDialect.POSTGRES;
import static org.jooq.SQLDialect.SQLITE;
// ...
// ...

import java.util.Collection;
import java.util.Map;

/**
 * This type is used for the {@link Insert}'s alternative DSL API.
 * <p>
 * Example: <code><pre>
 * DSLContext create = DSL.using(configuration);
 *
 * create.insertInto(table)
 *       .set(field1, value1)
 *       .set(field2, value2)
 *       .newRecord()
 *       .set(field1, value3)
 *       .set(field2, value4)
 *       .onDuplicateKeyUpdate()
 *       .set(field1, value1)
 *       .set(field2, value2)
 *       .execute();
 * </pre></code>
 *
 * @author Lukas Eder
 */
public interface InsertSetStep<R extends Record> {

    /**
     * Set a value for a field in the <code>INSERT</code> statement.
     */
    @Support
    <T> InsertSetMoreStep<R> set(Field<T> field, T value);

    /**
     * Set a value for a field in the <code>INSERT</code> statement.
     */
    @Support
    <T> InsertSetMoreStep<R> set(Field<T> field, Field<T> value);

    /**
     * Set a value for a field in the <code>INSERT</code> statement.
     */
    @Support
    <T> InsertSetMoreStep<R> set(Field<T> field, Select<? extends Record1<T>> value);

    /**
     * Set values in the <code>INSERT</code> statement.
     * <p>
     * Values can either be of type <code>&lt;T&gt;</code> or
     * <code>Field&lt;T&gt;</code>. jOOQ will attempt to convert values to their
     * corresponding field's type.
     */
    @Support
    InsertSetMoreStep<R> set(Map<? extends Field<?>, ?> map);

    /**
     * Set values in the <code>INSERT</code> statement.
     * <p>
     * This is the same as calling {@link #set(Map)} with the argument record
     * treated as a <code>Map<Field<?>, Object></code>.
     *
     * @see #set(Map)
     */
    @Support
    InsertSetMoreStep<R> set(Record record);

    /**
     * Add values to the insert statement with implicit field names.
     */
    @Support
    InsertValuesStepN<R> values(Object... values);

    /**
     * Add values to the insert statement with implicit field names.
     */
    @Support
    InsertValuesStepN<R> values(Field<?>... values);

    /**
     * Add values to the insert statement with implicit field names.
     */
    @Support
    InsertValuesStepN<R> values(Collection<?> values);

    /**
     * Add an empty record with default values.
     */
    @Support({ CUBRID, DERBY, FIREBIRD, H2, HSQLDB, MARIADB, MYSQL, POSTGRES, SQLITE })
    InsertReturningStep<R> defaultValues();

    /**
     * Use a <code>SELECT</code> statement as the source of values for the
     * <code>INSERT</code> statement.
     * <p>
     * This variant of the <code>INSERT .. SELECT</code> statement does not
     * allow for specifying a subset of the fields inserted into. It will insert
     * into all fields of the table specified in the <code>INTO</code> clause.
     * Use {@link DSLContext#insertInto(Table, Field...)} or
     * {@link DSLContext#insertInto(Table, Collection)} instead, to
     * define a field set for insertion.
     */
    @Support
    Insert<R> select(Select<?> select);
}
