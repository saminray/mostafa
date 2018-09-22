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


import static org.jooq.util.h2.information_schema.tables.FunctionColumns.FUNCTION_COLUMNS;

import java.sql.SQLException;

import org.jooq.Record;
import org.jooq.tools.StringUtils;
import org.jooq.util.AbstractRoutineDefinition;
import org.jooq.util.DataTypeDefinition;
import org.jooq.util.DefaultDataTypeDefinition;
import org.jooq.util.DefaultParameterDefinition;
import org.jooq.util.InOutDefinition;
import org.jooq.util.ParameterDefinition;
import org.jooq.util.SchemaDefinition;
import org.jooq.util.h2.information_schema.tables.FunctionColumns;

/**
 * H2 implementation of {@link AbstractRoutineDefinition}
 *
 * @author Espen Stromsnes
 * @author Lukas Eder
 */
public class H2RoutineDefinition extends AbstractRoutineDefinition {

    public H2RoutineDefinition(SchemaDefinition schema, String name, String comment, String typeName, Number precision, Number scale) {
        super(schema, null, name, comment, null);

        if (!StringUtils.isBlank(typeName)) {
            DataTypeDefinition type = new DefaultDataTypeDefinition(
                getDatabase(),
                schema,
                typeName,
                precision,
                precision,
                scale,
                null,
                null
            );

            this.returnValue = new DefaultParameterDefinition(this, "RETURN_VALUE", -1, type);
        }
    }

    @Override
    protected void init0() throws SQLException {
        for (Record record : create()
                .select(
                    FunctionColumns.COLUMN_NAME,
                    FunctionColumns.TYPE_NAME,
                    FunctionColumns.PRECISION,
                    FunctionColumns.SCALE,
                    FunctionColumns.POS,
                    FunctionColumns.NULLABLE,
                    FunctionColumns.COLUMN_DEFAULT.nvl2(true, false).as("default"))
                .from(FUNCTION_COLUMNS)
                .where(FunctionColumns.ALIAS_SCHEMA.equal(getSchema().getName()))
                .and(FunctionColumns.ALIAS_NAME.equal(getName()))
                .orderBy(FunctionColumns.POS.asc()).fetch()) {

            String paramName = record.getValue(FunctionColumns.COLUMN_NAME);
            String typeName = record.getValue(FunctionColumns.TYPE_NAME);
            Integer precision = record.getValue(FunctionColumns.PRECISION);
            Short scale = record.getValue(FunctionColumns.SCALE);
            int position = record.getValue(FunctionColumns.POS);
            boolean nullable = record.getValue(FunctionColumns.NULLABLE, boolean.class);
            boolean defaulted = record.getValue("default", boolean.class);

            // VERY special case for H2 alias/function parameters. The first parameter
            // may be a java.sql.Connection object and in such cases it should NEVER be used.
            // It is only used internally by H2 to provide a connection to the current database.
            if (position == 0 && H2DataType.OTHER.getTypeName().equalsIgnoreCase(typeName)) {
                continue;
            }

            DataTypeDefinition type = new DefaultDataTypeDefinition(
                getDatabase(),
                getSchema(), typeName,
                precision,
                precision,
                scale,
                nullable,
                defaulted
            );

            ParameterDefinition parameter = new DefaultParameterDefinition(this, paramName, position, type);
            addParameter(InOutDefinition.IN, parameter);
        }
    }
}
