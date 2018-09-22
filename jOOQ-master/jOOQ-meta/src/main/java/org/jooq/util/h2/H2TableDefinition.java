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
package org.jooq.util.h2;

import static org.jooq.util.h2.information_schema.tables.Columns.COLUMNS;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.jooq.Record;
import org.jooq.util.AbstractTableDefinition;
import org.jooq.util.ColumnDefinition;
import org.jooq.util.DataTypeDefinition;
import org.jooq.util.DefaultColumnDefinition;
import org.jooq.util.DefaultDataTypeDefinition;
import org.jooq.util.SchemaDefinition;
import org.jooq.util.h2.information_schema.tables.Columns;

/**
 * H2 table definition
 *
 * @author Espen Stromsnes
 */

public class H2TableDefinition extends AbstractTableDefinition {

    public H2TableDefinition(SchemaDefinition schema, String name, String comment) {
        super(schema, name, comment);
    }

    @Override
    public List<ColumnDefinition> getElements0() throws SQLException {
        List<ColumnDefinition> result = new ArrayList<ColumnDefinition>();

        for (Record record : create().select(
                Columns.COLUMN_NAME,
                Columns.ORDINAL_POSITION,
                Columns.TYPE_NAME,
                Columns.CHARACTER_MAXIMUM_LENGTH,
                Columns.NUMERIC_PRECISION,
                Columns.NUMERIC_SCALE,
                Columns.IS_NULLABLE,
                Columns.COLUMN_DEFAULT.nvl2(true, false).as("default"),
                Columns.REMARKS,
                Columns.SEQUENCE_NAME)
            .from(COLUMNS)
            .where(Columns.TABLE_SCHEMA.equal(getSchema().getName()))
            .and(Columns.TABLE_NAME.equal(getName()))
            .orderBy(Columns.ORDINAL_POSITION)
            .fetch()) {

            DataTypeDefinition type = new DefaultDataTypeDefinition(
                getDatabase(),
                getSchema(),
                record.getValue(Columns.TYPE_NAME),
                record.getValue(Columns.CHARACTER_MAXIMUM_LENGTH),
                record.getValue(Columns.NUMERIC_PRECISION),
                record.getValue(Columns.NUMERIC_SCALE),
                record.getValue(Columns.IS_NULLABLE, boolean.class),
                record.getValue("default", boolean.class));

            ColumnDefinition column = new DefaultColumnDefinition(
            	getDatabase().getTable(getSchema(), getName()),
                record.getValue(Columns.COLUMN_NAME),
                record.getValue(Columns.ORDINAL_POSITION),
                type,
                null != record.getValue(Columns.SEQUENCE_NAME),
                record.getValue(Columns.REMARKS));

            result.add(column);
        }

        return result;
    }
}
