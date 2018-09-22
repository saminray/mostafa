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

import static org.jooq.impl.DefaultExecuteContext.localConfiguration;

import java.sql.SQLException;
import java.sql.SQLInput;
import java.sql.SQLOutput;

import org.jooq.Configuration;
import org.jooq.Field;
import org.jooq.Row;
import org.jooq.Schema;
import org.jooq.UDT;
import org.jooq.UDTRecord;

/**
 * A record implementation for a record originating from a single UDT
 * <p>
 * This type is for JOOQ INTERNAL USE only. Do not reference directly
 *
 * @author Lukas Eder
 */
public class UDTRecordImpl<R extends UDTRecord<R>> extends AbstractRecord implements UDTRecord<R> {

    /**
     * Generated UID
     */
    private static final long serialVersionUID = 5671315498175872799L;
    private final UDT<R>      udt;

    public UDTRecordImpl(UDT<R> udt) {
        super(udt.fields());

        this.udt = udt;
    }

    @Override
    public final UDT<R> getUDT() {
        return udt;
    }

    /*
     * Subclasses may override this method
     */
    @Override
    public Row fieldsRow() {
        return fields;
    }

    /*
     * Subclasses may override this method
     */
    @SuppressWarnings({ "unchecked", "rawtypes" })
    @Override
    public Row valuesRow() {
        return new RowImpl(Utils.fields(intoArray(), fields.fields.fields()));
    }

    @Override
    public final String getSQLTypeName() throws SQLException {
        StringBuilder sb = new StringBuilder();

        // [#1693] This needs to return the fully qualified SQL type name, in
        // case the connected user is not the owner of the UDT
        Configuration configuration = localConfiguration();
        if (configuration != null) {
            Schema schema = Utils.getMappedSchema(configuration, getUDT().getSchema());

            if (schema != null) {
                sb.append(schema.getName());
                sb.append(".");
            }
        }

        sb.append(getUDT().getName());
        return sb.toString();
    }

    @Override
    public final void readSQL(SQLInput stream, String typeName) throws SQLException {
        Configuration configuration = localConfiguration();

        for (Field<?> field : getUDT().fields()) {
            setValue(configuration, stream, field);
        }
    }

    private final <T> void setValue(Configuration configuration, SQLInput stream, Field<T> field) throws SQLException {
        setValue(field, Utils.getFromSQLInput(configuration, stream, field));
    }

    @Override
    public final void writeSQL(SQLOutput stream) throws SQLException {
        for (Field<?> field : getUDT().fields()) {
            setValue(stream, field);
        }
    }

    private final <T> void setValue(SQLOutput stream, Field<T> field) throws SQLException {
        Utils.writeToSQLOutput(stream, field, getValue(field));
    }

    @Override
    public String toString() {
        StringBuilder result = new StringBuilder();
        String separator = "";

        result.append(create().render(getUDT()));
        result.append("(");

        Object[] array = intoArray();

        // [#3046] array can be null if custom RecordMapperProviders (illegally) return null
        if (array != null) {
            for (Object o : array) {
                result.append(separator);
                result.append(o);

                separator = ", ";
            }
        }

        result.append(")");

        return result.toString();
    }
}
