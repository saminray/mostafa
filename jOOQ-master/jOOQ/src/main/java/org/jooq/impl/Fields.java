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

import static org.jooq.impl.Utils.indexOrFail;

import java.util.Collection;

import org.jooq.Clause;
import org.jooq.Context;
import org.jooq.DataType;
import org.jooq.Field;
import org.jooq.Record;
import org.jooq.RecordType;

/**
 * A simple wrapper for <code>Field[]</code>, providing some useful lookup
 * methods
 *
 * @author Lukas Eder
 */
class Fields<R extends Record> extends AbstractQueryPart implements RecordType<R> {

    private static final long serialVersionUID = -6911012275707591576L;
    Field<?>[]                fields;

    Fields(Field<?>... fields) {
        this.fields = fields;
    }

    Fields(Collection<? extends Field<?>> fields) {
        this.fields = fields.toArray(new Field[fields.size()]);
    }

    @Override
    public final int size() {
        return fields.length;
    }

    @Override
    @SuppressWarnings("unchecked")
    public final <T> Field<T> field(Field<T> field) {
        if (field == null) {
            return null;
        }

        // [#1802] Try finding an exact match (e.g. exact matching qualified name)
        for (Field<?> f : fields) {
            if (f.equals(field)) {
                return (Field<T>) f;
            }
        }

        // In case no exact match was found, return the first field with matching name
        String name = field.getName();
        for (Field<?> f1 : fields) {
            if (f1.getName().equals(name)) {
                return (Field<T>) f1;
            }
        }

        return null;
    }

    @Override
    public final Field<?> field(String name) {
        if (name == null) {
            return null;
        }

        for (Field<?> f : fields) {
            if (f.getName().equals(name)) {
                return f;
            }
        }

        return null;
    }

    @Override
    public final Field<?> field(int index) {
        if (index >= 0 && index < fields.length) {
            return fields[index];
        }

        return null;
    }

    @Override
    public final Field<?>[] fields() {
        return fields;
    }

    @Override
    public final Field<?>[] fields(Field<?>... f) {
        Field<?>[] result = new Field[f.length];

        for (int i = 0; i < result.length; i++) {
            result[i] = field(f[i]);
        }

        return result;
    }

    @Override
    public final Field<?>[] fields(String... f) {
        Field<?>[] result = new Field[f.length];

        for (int i = 0; i < result.length; i++) {
            result[i] = field(f[i]);
        }

        return result;
    }

    @Override
    public final Field<?>[] fields(int... f) {
        Field<?>[] result = new Field[f.length];

        for (int i = 0; i < result.length; i++) {
            result[i] = field(f[i]);
        }

        return result;
    }

    @Override
    public final int indexOf(Field<?> field) {

        // Get an exact match, or a field with a similar name
        Field<?> compareWith = field(field);

        if (compareWith != null) {
            int size = fields.length;

            for (int i = 0; i < size; i++) {
                if (fields[i].equals(compareWith))
                    return i;
            }
        }

        return -1;
    }

    @Override
    public final int indexOf(String fieldName) {
        return indexOf(field(fieldName));
    }

    @Override
    public final Class<?>[] types() {
        int size = fields.length;
        Class<?>[] result = new Class[size];

        for (int i = 0; i < size; i++) {
            result[i] = field(i).getType();
        }

        return result;
    }

    @Override
    public final Class<?> type(int fieldIndex) {
        return fieldIndex >= 0 && fieldIndex < size() ? field(fieldIndex).getType() : null;
    }

    @Override
    public final Class<?> type(String fieldName) {
        return type(indexOrFail(this, fieldName));
    }

    @Override
    public final DataType<?>[] dataTypes() {
        int size = fields.length;
        DataType<?>[] result = new DataType[size];

        for (int i = 0; i < size; i++) {
            result[i] = field(i).getDataType();
        }

        return result;
    }

    @Override
    public final DataType<?> dataType(int fieldIndex) {
        return fieldIndex >= 0 && fieldIndex < size() ? field(fieldIndex).getDataType() : null;
    }

    @Override
    public final DataType<?> dataType(String fieldName) {
        return dataType(indexOrFail(this, fieldName));
    }

    final int[] indexesOf(Field<?>... f) {
        int[] result = new int[f.length];

        for (int i = 0; i < f.length; i++) {
            result[i] = indexOrFail(this, f[i]);
        }

        return result;
    }

    final int[] indexesOf(String... fieldNames) {
        int[] result = new int[fieldNames.length];

        for (int i = 0; i < fieldNames.length; i++) {
            result[i] = indexOrFail(this, fieldNames[i]);
        }

        return result;
    }


    @Override
    public final void accept(Context<?> ctx) {
        new QueryPartList<Field<?>>(fields).accept(ctx);
    }

    @Override
    public final Clause[] clauses(Context<?> ctx) {
        return null;
    }

    // -------------------------------------------------------------------------
    // XXX: List-like API
    // -------------------------------------------------------------------------

    final void add(Field<?> f) {
        Field<?>[] result = new Field[fields.length + 1];

        System.arraycopy(fields, 0, result, 0, fields.length);
        result[fields.length] = f;

        fields = result;
    }
}
