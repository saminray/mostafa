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
package org.jooq.util.firebird;

import static org.jooq.util.firebird.rdb.Tables.RDB$GENERATORS;
import static org.jooq.util.firebird.rdb.Tables.RDB$INDEX_SEGMENTS;
import static org.jooq.util.firebird.rdb.Tables.RDB$REF_CONSTRAINTS;
import static org.jooq.util.firebird.rdb.Tables.RDB$RELATIONS;
import static org.jooq.util.firebird.rdb.Tables.RDB$RELATION_CONSTRAINTS;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.jooq.DSLContext;
import org.jooq.Record;
import org.jooq.Record3;
import org.jooq.Result;
import org.jooq.SQLDialect;
import org.jooq.impl.DSL;
import org.jooq.util.AbstractDatabase;
import org.jooq.util.ArrayDefinition;
import org.jooq.util.ColumnDefinition;
import org.jooq.util.DataTypeDefinition;
import org.jooq.util.DefaultDataTypeDefinition;
import org.jooq.util.DefaultRelations;
import org.jooq.util.DefaultSequenceDefinition;
import org.jooq.util.EnumDefinition;
import org.jooq.util.PackageDefinition;
import org.jooq.util.RoutineDefinition;
import org.jooq.util.SchemaDefinition;
import org.jooq.util.SequenceDefinition;
import org.jooq.util.TableDefinition;
import org.jooq.util.UDTDefinition;
import org.jooq.util.firebird.rdb.tables.Rdb$indexSegments;
import org.jooq.util.firebird.rdb.tables.Rdb$refConstraints;
import org.jooq.util.firebird.rdb.tables.Rdb$relationConstraints;
import org.jooq.util.jaxb.Schema;

/**
 * @author Sugiharto Lim - Initial contribution
 */
public class FirebirdDatabase extends AbstractDatabase {

    public FirebirdDatabase() {

        // Firebird doesn't know schemata
        Schema schema = new Schema();
        schema.setInputSchema("");
        schema.setOutputSchema("");

        List<Schema> schemata = new ArrayList<Schema>();
        schemata.add(schema);

        setConfiguredSchemata(schemata);
    }

    @Override
    protected void loadPrimaryKeys(DefaultRelations r) throws SQLException {
        for (Record record : fetchKeys("PRIMARY KEY")) {
            String tableName = record.getValue(RDB$RELATION_CONSTRAINTS.RDB$RELATION_NAME.trim());
            String fieldName = record.getValue(RDB$INDEX_SEGMENTS.RDB$FIELD_NAME.trim());
            String key = record.getValue(RDB$RELATION_CONSTRAINTS.RDB$CONSTRAINT_NAME.trim());

            TableDefinition td = getTable(this.getSchemata().get(0), tableName);
            if (td != null) {
                ColumnDefinition cd = td.getColumn(fieldName);
                r.addPrimaryKey(key, cd);
            }
        }
    }

    @Override
    protected void loadUniqueKeys(DefaultRelations r) throws SQLException {
        for (Record record : fetchKeys("UNIQUE")) {
            String tableName = record.getValue(RDB$RELATION_CONSTRAINTS.RDB$RELATION_NAME.trim());
            String fieldName = record.getValue(RDB$INDEX_SEGMENTS.RDB$FIELD_NAME.trim());
            String key = record.getValue(RDB$RELATION_CONSTRAINTS.RDB$CONSTRAINT_NAME.trim());

            TableDefinition td = getTable(this.getSchemata().get(0), tableName);
            if (td != null) {
                ColumnDefinition cd = td.getColumn(fieldName);
                r.addUniqueKey(key, cd);
            }
        }
    }

    private Result<Record3<String, String, String>> fetchKeys(String constraintType) {
        return create()
            .select(
                RDB$RELATION_CONSTRAINTS.RDB$CONSTRAINT_NAME.trim(),
                RDB$RELATION_CONSTRAINTS.RDB$RELATION_NAME.trim(),
                RDB$INDEX_SEGMENTS.RDB$FIELD_NAME.trim())
            .from(RDB$RELATION_CONSTRAINTS)
            .join(RDB$INDEX_SEGMENTS)
            .on(RDB$INDEX_SEGMENTS.RDB$INDEX_NAME.eq(RDB$RELATION_CONSTRAINTS.RDB$INDEX_NAME))
            .where(RDB$RELATION_CONSTRAINTS.RDB$CONSTRAINT_TYPE.eq(constraintType))
            .orderBy(
                RDB$RELATION_CONSTRAINTS.RDB$CONSTRAINT_NAME.asc(),
                RDB$INDEX_SEGMENTS.RDB$FIELD_POSITION.asc())
            .fetch();
    }

    @Override
    protected void loadForeignKeys(DefaultRelations relations) throws SQLException {
        Rdb$relationConstraints pk = RDB$RELATION_CONSTRAINTS.as("pk");
        Rdb$relationConstraints fk = RDB$RELATION_CONSTRAINTS.as("fk");
        Rdb$refConstraints rc = RDB$REF_CONSTRAINTS.as("rc");
        Rdb$indexSegments isp = RDB$INDEX_SEGMENTS.as("isp");
        Rdb$indexSegments isf = RDB$INDEX_SEGMENTS.as("isf");

        for (Record record : create()
                .selectDistinct(
                    fk.RDB$CONSTRAINT_NAME.trim().as("fk"),
                    fk.RDB$RELATION_NAME.trim().as("fkTable"),
                    isf.RDB$FIELD_NAME.trim().as("fkField"),
                    pk.RDB$CONSTRAINT_NAME.trim().as("pk"),
                    pk.RDB$RELATION_NAME.trim().as("pkTable"))
                .from(fk)
                .join(rc).on(fk.RDB$CONSTRAINT_NAME.eq(rc.RDB$CONSTRAINT_NAME))
                .join(pk).on(pk.RDB$CONSTRAINT_NAME.eq(rc.RDB$CONST_NAME_UQ))
                .join(isp).on(isp.RDB$INDEX_NAME.eq(pk.RDB$INDEX_NAME))
                .join(isf).on(isf.RDB$INDEX_NAME.eq(fk.RDB$INDEX_NAME))
                .where(isp.RDB$FIELD_POSITION.eq(isf.RDB$FIELD_POSITION))
                .orderBy(
                    fk.RDB$CONSTRAINT_NAME.asc(),
                    isf.RDB$FIELD_POSITION.asc())
                .fetch()) {

            String pkName = record.getValue("pk", String.class);
            String pkTable = record.getValue("pkTable", String.class);

            String fkName = record.getValue("fk", String.class);
            String fkTable = record.getValue("fkTable", String.class);
            String fkField = record.getValue("fkField", String.class);

            TableDefinition tdReferencing = getTable(getSchemata().get(0), fkTable, true);
            TableDefinition tdReferenced = getTable(getSchemata().get(0), pkTable, true);

            if (tdReferenced != null && tdReferencing != null) {
                ColumnDefinition referencingColumn = tdReferencing.getColumn(fkField);
                relations.addForeignKey(fkName, pkName, referencingColumn, getSchemata().get(0));
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
        result.add(new SchemaDefinition(this, "", ""));
        return result;
    }

    @Override
    protected List<SequenceDefinition> getSequences0() throws SQLException {
        List<SequenceDefinition> result = new ArrayList<SequenceDefinition>();

        for (String sequenceName : create()
                .select(RDB$GENERATORS.RDB$GENERATOR_NAME.trim())
                .from(RDB$GENERATORS)
                .orderBy(1)
                .fetch(RDB$GENERATORS.RDB$GENERATOR_NAME.trim())) {

            SchemaDefinition schema = getSchemata().get(0);
            DataTypeDefinition type = new DefaultDataTypeDefinition(
                this, schema, FirebirdDataType.BIGINT.getTypeName()
            );

            result.add(new DefaultSequenceDefinition(schema, sequenceName, type ));
        }

        return result;
    }

    @Override
    protected List<TableDefinition> getTables0() throws SQLException {
        List<TableDefinition> result = new ArrayList<TableDefinition>();

        for (String tableName : create()
                .select(RDB$RELATIONS.RDB$RELATION_NAME.trim())
                .from(RDB$RELATIONS)
                .orderBy(1)
                .fetch(0, String.class)) {

            TableDefinition tableDef = new FirebirdTableDefinition(getSchemata().get(0), tableName, "");
            result.add(tableDef);
        }

        return result;
    }

    @Override
    protected List<RoutineDefinition> getRoutines0() throws SQLException {
        List<RoutineDefinition> result = new ArrayList<RoutineDefinition>();
        return result;
    }

    @Override
    protected List<PackageDefinition> getPackages0() throws SQLException {
        List<PackageDefinition> result = new ArrayList<PackageDefinition>();
        return result;
    }

    @Override
    protected List<EnumDefinition> getEnums0() throws SQLException {
        List<EnumDefinition> result = new ArrayList<EnumDefinition>();
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
    protected DSLContext create0() {
        return DSL.using(getConnection(), SQLDialect.FIREBIRD);
    }
}
