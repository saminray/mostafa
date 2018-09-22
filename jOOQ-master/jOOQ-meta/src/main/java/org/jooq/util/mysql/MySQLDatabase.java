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

package org.jooq.util.mysql;

import static org.jooq.util.mysql.information_schema.Tables.COLUMNS;
import static org.jooq.util.mysql.information_schema.Tables.KEY_COLUMN_USAGE;
import static org.jooq.util.mysql.information_schema.Tables.REFERENTIAL_CONSTRAINTS;
import static org.jooq.util.mysql.information_schema.Tables.SCHEMATA;
import static org.jooq.util.mysql.information_schema.Tables.TABLES;
import static org.jooq.util.mysql.information_schema.Tables.TABLE_CONSTRAINTS;
import static org.jooq.util.mysql.mysql.tables.Proc.DB;
import static org.jooq.util.mysql.mysql.tables.Proc.PROC;

import java.io.StringReader;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.jooq.DSLContext;
import org.jooq.Field;
import org.jooq.Record;
import org.jooq.Record4;
import org.jooq.Record5;
import org.jooq.Record6;
import org.jooq.Result;
import org.jooq.SQLDialect;
import org.jooq.exception.DataAccessException;
import org.jooq.impl.DSL;
import org.jooq.tools.JooqLogger;
import org.jooq.tools.csv.CSVReader;
import org.jooq.util.AbstractDatabase;
import org.jooq.util.ArrayDefinition;
import org.jooq.util.ColumnDefinition;
import org.jooq.util.DefaultEnumDefinition;
import org.jooq.util.DefaultRelations;
import org.jooq.util.EnumDefinition;
import org.jooq.util.PackageDefinition;
import org.jooq.util.RoutineDefinition;
import org.jooq.util.SchemaDefinition;
import org.jooq.util.SequenceDefinition;
import org.jooq.util.TableDefinition;
import org.jooq.util.UDTDefinition;
import org.jooq.util.mysql.information_schema.tables.Columns;
import org.jooq.util.mysql.information_schema.tables.KeyColumnUsage;
import org.jooq.util.mysql.information_schema.tables.ReferentialConstraints;
import org.jooq.util.mysql.information_schema.tables.Schemata;
import org.jooq.util.mysql.information_schema.tables.TableConstraints;
import org.jooq.util.mysql.information_schema.tables.Tables;
import org.jooq.util.mysql.mysql.enums.ProcType;
import org.jooq.util.mysql.mysql.tables.Proc;

/**
 * @author Lukas Eder
 */
public class MySQLDatabase extends AbstractDatabase {

    private static final JooqLogger log = JooqLogger.getLogger(MySQLDatabase.class);

    @Override
    protected void loadPrimaryKeys(DefaultRelations relations) throws SQLException {
        for (Record record : fetchKeys("PRIMARY KEY")) {
            SchemaDefinition schema = getSchema(record.getValue(KeyColumnUsage.TABLE_SCHEMA));
            String constraintName = record.getValue(KeyColumnUsage.CONSTRAINT_NAME);
            String tableName = record.getValue(KeyColumnUsage.TABLE_NAME);
            String columnName = record.getValue(KeyColumnUsage.COLUMN_NAME);

            String key = getKeyName(tableName, constraintName);
            TableDefinition table = getTable(schema, tableName);

            if (table != null) {
                relations.addPrimaryKey(key, table.getColumn(columnName));
            }
        }
    }

    @Override
    protected void loadUniqueKeys(DefaultRelations relations) throws SQLException {
        for (Record record : fetchKeys("UNIQUE")) {
            SchemaDefinition schema = getSchema(record.getValue(KeyColumnUsage.TABLE_SCHEMA));
            String constraintName = record.getValue(KeyColumnUsage.CONSTRAINT_NAME);
            String tableName = record.getValue(KeyColumnUsage.TABLE_NAME);
            String columnName = record.getValue(KeyColumnUsage.COLUMN_NAME);

            String key = getKeyName(tableName, constraintName);
            TableDefinition table = getTable(schema, tableName);

            if (table != null) {
                relations.addUniqueKey(key, table.getColumn(columnName));
            }
        }
    }

    private String getKeyName(String tableName, String keyName) {
        return "KEY_" + tableName + "_" + keyName;
    }

    private Result<Record4<String, String, String, String>> fetchKeys(String constraintType) {
        return create().select(
                KeyColumnUsage.TABLE_SCHEMA,
                KeyColumnUsage.CONSTRAINT_NAME,
                KeyColumnUsage.TABLE_NAME,
                KeyColumnUsage.COLUMN_NAME)
            .from(KEY_COLUMN_USAGE)
            .join(TABLE_CONSTRAINTS)
            .on(KeyColumnUsage.TABLE_SCHEMA.equal(TableConstraints.TABLE_SCHEMA))
            .and(KeyColumnUsage.TABLE_NAME.equal(TableConstraints.TABLE_NAME))
            .and(KeyColumnUsage.CONSTRAINT_NAME.equal(TableConstraints.CONSTRAINT_NAME))
            .where(TableConstraints.CONSTRAINT_TYPE.equal(constraintType))
            .and(KeyColumnUsage.TABLE_SCHEMA.in(getInputSchemata()))
            .orderBy(
                KeyColumnUsage.TABLE_SCHEMA.asc(),
                KeyColumnUsage.TABLE_NAME.asc(),
                KeyColumnUsage.ORDINAL_POSITION.asc())
            .fetch();
    }

    @Override
    protected void loadForeignKeys(DefaultRelations relations) throws SQLException {
        for (Record record : create().select(
                    ReferentialConstraints.CONSTRAINT_SCHEMA,
                    ReferentialConstraints.CONSTRAINT_NAME,
                    ReferentialConstraints.TABLE_NAME,
                    ReferentialConstraints.REFERENCED_TABLE_NAME,
                    ReferentialConstraints.UNIQUE_CONSTRAINT_NAME,
                    ReferentialConstraints.UNIQUE_CONSTRAINT_SCHEMA,
                    KeyColumnUsage.COLUMN_NAME)
                .from(REFERENTIAL_CONSTRAINTS)
                .join(KEY_COLUMN_USAGE)
                .on(ReferentialConstraints.CONSTRAINT_SCHEMA.equal(KeyColumnUsage.CONSTRAINT_SCHEMA))
                .and(ReferentialConstraints.CONSTRAINT_NAME.equal(KeyColumnUsage.CONSTRAINT_NAME))
                .where(ReferentialConstraints.CONSTRAINT_SCHEMA.in(getInputSchemata()))
                .orderBy(
                    KeyColumnUsage.CONSTRAINT_SCHEMA.asc(),
                    KeyColumnUsage.CONSTRAINT_NAME.asc(),
                    KeyColumnUsage.ORDINAL_POSITION.asc())
                .fetch()) {

            SchemaDefinition foreignKeySchema = getSchema(record.getValue(ReferentialConstraints.CONSTRAINT_SCHEMA));
            SchemaDefinition uniqueKeySchema = getSchema(record.getValue(ReferentialConstraints.UNIQUE_CONSTRAINT_SCHEMA));

            String foreignKey = record.getValue(ReferentialConstraints.CONSTRAINT_NAME);
            String foreignKeyColumn = record.getValue(KeyColumnUsage.COLUMN_NAME);
            String foreignKeyTableName = record.getValue(ReferentialConstraints.TABLE_NAME);
            String referencedKey = record.getValue(ReferentialConstraints.UNIQUE_CONSTRAINT_NAME);
            String referencedTableName = record.getValue(ReferentialConstraints.REFERENCED_TABLE_NAME);

            TableDefinition foreignKeyTable = getTable(foreignKeySchema, foreignKeyTableName);

            if (foreignKeyTable != null) {
                ColumnDefinition column = foreignKeyTable.getColumn(foreignKeyColumn);

                String key = getKeyName(referencedTableName, referencedKey);
                relations.addForeignKey(foreignKey, key, column, uniqueKeySchema);
            }
        }
    }

    @Override
    protected void loadCheckConstraints(DefaultRelations r) throws SQLException {
        // Currently not supported
    }

    @Override
    protected List<SchemaDefinition> getSchemata0() throws SQLException {
        List<SchemaDefinition> result = new ArrayList<SchemaDefinition>();

        for (String name : create()
                .select(Schemata.SCHEMA_NAME)
                .from(SCHEMATA)
                .fetch(Schemata.SCHEMA_NAME)) {

            result.add(new SchemaDefinition(this, name, ""));
        }

        return result;
    }

    @Override
    protected List<SequenceDefinition> getSequences0() throws SQLException {
        List<SequenceDefinition> result = new ArrayList<SequenceDefinition>();
        return result;
    }

    @Override
    protected List<TableDefinition> getTables0() throws SQLException {
        List<TableDefinition> result = new ArrayList<TableDefinition>();

        for (Record record : create().select(
                Tables.TABLE_SCHEMA,
                Tables.TABLE_NAME,
                Tables.TABLE_COMMENT)
            .from(TABLES)
            .where(Tables.TABLE_SCHEMA.in(getInputSchemata()))
            .orderBy(
                Tables.TABLE_SCHEMA,
                Tables.TABLE_NAME)
            .fetch()) {

            SchemaDefinition schema = getSchema(record.getValue(Tables.TABLE_SCHEMA));
            String name = record.getValue(Tables.TABLE_NAME);
            String comment = record.getValue(Tables.TABLE_COMMENT);

            MySQLTableDefinition table = new MySQLTableDefinition(schema, name, comment);
            result.add(table);
        }

        return result;
    }

    @Override
    protected List<EnumDefinition> getEnums0() throws SQLException {
        List<EnumDefinition> result = new ArrayList<EnumDefinition>();

        Result<Record5<String, String, String, String, String>> records = create()
            .select(
                Columns.TABLE_SCHEMA,
                Columns.COLUMN_COMMENT,
                Columns.TABLE_NAME,
                Columns.COLUMN_NAME,
                Columns.COLUMN_TYPE)
            .from(COLUMNS)
            .where(
                Columns.COLUMN_TYPE.like("enum(%)").and(
                Columns.TABLE_SCHEMA.in(getInputSchemata())))
            .orderBy(
                Columns.TABLE_SCHEMA.asc(),
                Columns.TABLE_NAME.asc(),
                Columns.COLUMN_NAME.asc())
            .fetch();

        for (Record record : records) {
            SchemaDefinition schema = getSchema(record.getValue(Columns.TABLE_SCHEMA));

            String comment = record.getValue(Columns.COLUMN_COMMENT);
            String table = record.getValue(Columns.TABLE_NAME);
            String column = record.getValue(Columns.COLUMN_NAME);
            String name = table + "_" + column;
            String columnType = record.getValue(Columns.COLUMN_TYPE);

            // [#1237] Don't generate enum classes for columns in MySQL tables
            // that are excluded from code generation
            TableDefinition tableDefinition = getTable(schema, table);
            if (tableDefinition != null) {
                ColumnDefinition columnDefinition = tableDefinition.getColumn(column);

                if (columnDefinition != null) {

                	// [#1137] Avoid generating enum classes for enum types that
                	// are explicitly forced to another type
                    if (getConfiguredForcedType(columnDefinition) == null) {
                        DefaultEnumDefinition definition = new DefaultEnumDefinition(schema, name, comment);

                        CSVReader reader = new CSVReader(
                            new StringReader(columnType.replaceAll("(^enum\\()|(\\)$)", ""))
                           ,','  // Separator
                           ,'\'' // Quote character
                           ,true // Strict quotes
                        );

                        for (String string : reader.next()) {
                            definition.addLiteral(string);
                        }

                        result.add(definition);
                    }
                }
            }
        }

        return result;
    }

    @Override
    protected List<UDTDefinition> getUDTs0() throws SQLException {
        List<UDTDefinition> result = new ArrayList<UDTDefinition>();
        return result;
    }

    @Override
    protected List<ArrayDefinition> getArrays0() throws SQLException {
        List<ArrayDefinition> result = new ArrayList<ArrayDefinition>();
        return result;
    }

    @Override
    protected List<RoutineDefinition> getRoutines0() throws SQLException {
        List<RoutineDefinition> result = new ArrayList<RoutineDefinition>();

        try {
            create().fetchCount(PROC);
        }
        catch (DataAccessException e) {
            log.warn("Table unavailable", "The `mysql`.`proc` table is unavailable. Stored procedures cannot be loaded. Check if you have sufficient grants");
            return result;
        }

        Result<Record6<String, String, String, byte[], byte[], ProcType>> records =
        create().select(
                    Proc.DB,
                    Proc.NAME,
                    Proc.COMMENT,
                    Proc.PARAM_LIST,
                    Proc.RETURNS,
                    Proc.TYPE)
                .from(PROC)
                .where(DB.in(getInputSchemata()))
                .orderBy(
                    DB,
                    Proc.NAME)
                .fetch();

        Map<Record, Result<Record6<String, String, String, byte[], byte[], ProcType>>> groups =
            records.intoGroups(new Field[] { Proc.DB, Proc.NAME });

        // [#1908] This indirection is necessary as MySQL allows for overloading
        // procedures and functions with the same signature.
        for (Entry<Record, Result<Record6<String, String, String, byte[], byte[], ProcType>>> entry : groups.entrySet()) {
            Result<?> overloads = entry.getValue();

            for (int i = 0; i < overloads.size(); i++) {
                Record record = overloads.get(i);

                SchemaDefinition schema = getSchema(record.getValue(DB));
                String name = record.getValue(Proc.NAME);
                String comment = record.getValue(Proc.COMMENT);
                String params = new String(record.getValue(Proc.PARAM_LIST));
                String returns = new String(record.getValue(Proc.RETURNS));
                ProcType type = record.getValue(Proc.TYPE);

                if (overloads.size() > 1) {
                    result.add(new MySQLRoutineDefinition(schema, name, comment, params, returns, type, "_" + type.name()));
                }
                else {
                    result.add(new MySQLRoutineDefinition(schema, name, comment, params, returns, type, null));
                }
            }
        }

        return result;
    }

    @Override
    protected List<PackageDefinition> getPackages0() throws SQLException {
        List<PackageDefinition> result = new ArrayList<PackageDefinition>();
        return result;
    }

    @Override
    protected DSLContext create0() {
        return DSL.using(getConnection(), SQLDialect.MYSQL);
    }
}
