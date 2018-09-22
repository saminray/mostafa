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
package org.jooq.test._.testcases;

import static java.util.Arrays.asList;
// ...
import static org.jooq.SQLDialect.CUBRID;
// ...
import static org.jooq.SQLDialect.H2;
// ...
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.sql.Date;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import org.jooq.Catalog;
import org.jooq.Field;
import org.jooq.Meta;
import org.jooq.Record1;
import org.jooq.Record2;
import org.jooq.Record3;
import org.jooq.Record6;
import org.jooq.SQLDialect;
import org.jooq.Schema;
import org.jooq.Sequence;
import org.jooq.Table;
import org.jooq.TableRecord;
import org.jooq.UDT;
import org.jooq.UpdatableRecord;
import org.jooq.impl.SQLDataType;
import org.jooq.test.BaseTest;
import org.jooq.test.jOOQAbstractTest;

import org.junit.Test;

public class MetaDataTests<
    A    extends UpdatableRecord<A> & Record6<Integer, String, String, Date, Integer, ?>,
    AP,
    B    extends UpdatableRecord<B>,
    S    extends UpdatableRecord<S> & Record1<String>,
    B2S  extends UpdatableRecord<B2S> & Record3<String, Integer, Integer>,
    BS   extends UpdatableRecord<BS>,
    L    extends TableRecord<L> & Record2<String, String>,
    X    extends TableRecord<X>,
    DATE extends UpdatableRecord<DATE>,
    BOOL extends UpdatableRecord<BOOL>,
    D    extends UpdatableRecord<D>,
    T    extends UpdatableRecord<T>,
    U    extends TableRecord<U>,
    UU   extends UpdatableRecord<UU>,
    I    extends TableRecord<I>,
    IPK  extends UpdatableRecord<IPK>,
    T725 extends UpdatableRecord<T725>,
    T639 extends UpdatableRecord<T639>,
    T785 extends TableRecord<T785>,
    CASE extends UpdatableRecord<CASE>>
extends BaseTest<A, AP, B, S, B2S, BS, L, X, DATE, BOOL, D, T, U, UU, I, IPK, T725, T639, T785, CASE> {

    public MetaDataTests(jOOQAbstractTest<A, AP, B, S, B2S, BS, L, X, DATE, BOOL, D, T, U, UU, I, IPK, T725, T639, T785, CASE> delegate) {
        super(delegate);
    }

    public void testMetaFieldTypes() throws Exception {

        // [#3133] Check if DEFAULT information is correctly generated
        assertFalse(TBook_ID().getDataType().defaulted());

        if (!asList().contains(dialect().family())) {
            assertTrue(TBook_LANGUAGE_ID().getDataType().defaulted());
        }
    }

    public void testMetaModel() throws Exception {

        // Test correct source code generation for the meta model
        Schema schema = TAuthor().getSchema();
        if (schema != null) {
            int sequences = 0;

            if (cSequences() != null) {
                sequences++;

                // DB2 has an additional sequence for the T_TRIGGERS table
                if (/* [pro] xxxxxxxxxxxxxxxxxxxx xx xxx xx
                    xx [/pro] */dialect().family() == H2) {

                    sequences++;
                }

                // CUBRID generates sequences for AUTO_INCREMENT columns
                else if (dialect().family() == CUBRID) {
                    sequences += 3;
                }

                /* [pro] xx
                xx xxxxxx xxx xxxxxxxxxx xxxxxxxxx xxx xxxxxx
                xxxx xx xxxxxxxxxxxxxxxxxxx xx xxxxxxx x
                    xxxxxxxxx xx xx
                x
                xx [/pro] */
            }

            assertEquals(sequences, schema.getSequences().size());
            for (Table<?> table : schema.getTables()) {
                assertEquals(table, schema.getTable(table.getName()));
            }
            for (UDT<?> udt : schema.getUDTs()) {
                assertEquals(udt, schema.getUDT(udt.getName()));
            }
            for (Sequence<?> sequence : schema.getSequences()) {
                assertEquals(sequence, schema.getSequence(sequence.getName()));
            }

            // Some selective tables
            assertTrue(schema.getTables().contains(T639()));
            assertTrue(schema.getTables().contains(TAuthor()));
            assertTrue(schema.getTables().contains(TBook()));
            assertTrue(schema.getTables().contains(TBookStore()));
            assertTrue(schema.getTables().contains(TBookToBookStore()));

            // The additional T_DIRECTORY table for recursive queries
            if (TDirectory() != null) {
                schema.getTables().contains(TDirectory());
            }

            // The additional T_TRIGGERS table for INSERT .. RETURNING
            if (TTriggers() != null) {
                schema.getTables().contains(TTriggers());
            }

            // The additional T_UNSIGNED table
            if (TUnsigned() != null) {
                schema.getTables().contains(TUnsigned());
            }

            // The additional T_IDENTITY table
            if (TIdentity() != null) {
                schema.getTables().contains(TIdentity());
            }

            // The additional T_IDENTITY_PK table
            if (TIdentityPK() != null) {
                schema.getTables().contains(TIdentityPK());
            }

            if (cUAddressType() == null) {
                assertEquals(0, schema.getUDTs().size());
            }
            /* [pro] xx
            xx xxxxxx xxx xxxxxxxxx xxxxx xxx xxxx xxxxxxxxx xx xxxxxx
            xx xxxxxx xxx xxxxxx xxxxxxxxx xxxxx xxx
            xxxx xx xxxxxxxxxxxxxxxxxxx xx xxxxxxx x
                xxxxxxxxxxxxxxx xxxxxxxxxxxxxxxxxxxxxxxxx
            x
            xx [/pro] */
            else {
                assertEquals(3, schema.getUDTs().size());
            }
        }

        // Test correct source code generation for identity columns
        assertNull(TAuthor().getIdentity());
        assertNull(TBook().getIdentity());

        if (TIdentity() != null || TIdentityPK() != null) {
            if (TIdentity() != null) {
                assertEquals(TIdentity(), TIdentity().getIdentity().getTable());
                assertEquals(TIdentity_ID(), TIdentity().getIdentity().getField());
            }

            if (TIdentityPK() != null) {
                assertEquals(TIdentityPK(), TIdentityPK().getIdentity().getTable());
                assertEquals(TIdentityPK_ID(), TIdentityPK().getIdentity().getField());
            }
        }
        else {
            log.info("SKIPPING", "Identity tests");
        }

        // Test correct source code generation for relations
        assertNotNull(TAuthor().getPrimaryKey());
        assertNotNull(TAuthor().getKeys());
        assertTrue(TAuthor().getKeys().contains(TAuthor().getPrimaryKey()));
        assertEquals(1, TAuthor().getKeys().size());
        assertEquals(1, TAuthor().getPrimaryKey().getFields().size());
        assertEquals(TAuthor_ID(), TAuthor().getPrimaryKey().getFields().get(0));
        assertEquals(Record1.class, TAuthor().getRecordType().getMethod("key").getReturnType());
        assertTrue(TAuthor().getRecordType().getMethod("key").toGenericString().contains("org.jooq.Record1<java.lang.Integer>"));
        assertEquals(Record2.class, TBookToBookStore().getRecordType().getMethod("key").getReturnType());
        assertTrue(TBookToBookStore().getRecordType().getMethod("key").toGenericString().contains("org.jooq.Record2<java.lang.String, java.lang.Integer>"));

        if (supportsReferences()) {

            // Without aliasing
            assertEquals(0, TAuthor().getReferences().size());
            assertEquals(2, TAuthor().getPrimaryKey().getReferences().size());
            assertEquals(TBook(), TAuthor().getPrimaryKey().getReferences().get(0).getTable());
            assertEquals(TBook(), TAuthor().getPrimaryKey().getReferences().get(1).getTable());
            assertEquals(Arrays.asList(), TAuthor().getReferencesTo(TBook()));
            assertTrue(TBook().getReferences().containsAll(TAuthor().getReferencesFrom(TBook())));
            assertTrue(TBook().getReferences().containsAll(TBook().getReferencesFrom(TAuthor())));
            assertEquals(TBook().getReferencesTo(TAuthor()), TAuthor().getReferencesFrom(TBook()));

            // [#1460] With aliasing
            Table<A> a = TAuthor().as("a");
            Table<B> b = TBook().as("b");

            assertEquals(0, a.getReferences().size());
            assertEquals(Arrays.asList(), a.getReferencesTo(b));

            // This should work with both types of meta-models (static, non-static)
            assertEquals(TBook().getReferencesTo(TAuthor()), TBook().getReferencesTo(a));
            assertEquals(TBook().getReferencesTo(TAuthor()), b.getReferencesTo(a));
            assertEquals(TBook().getReferencesTo(TAuthor()), b.getReferencesTo(TAuthor()));

            // Only with a non-static meta model
            if (a.getPrimaryKey() != null && b.getPrimaryKey() != null) {
                assertEquals(2, a.getPrimaryKey().getReferences().size());
                assertEquals(TBook(), a.getPrimaryKey().getReferences().get(0).getTable());
                assertEquals(TBook(), a.getPrimaryKey().getReferences().get(1).getTable());
                assertTrue(b.getReferences().containsAll(a.getReferencesFrom(b)));
                assertTrue(b.getReferences().containsAll(b.getReferencesFrom(a)));
                assertEquals(b.getReferencesTo(a), a.getReferencesFrom(b));
                assertEquals(TBook().getReferencesTo(a), a.getReferencesFrom(b));
                assertEquals(b.getReferencesTo(a), TAuthor().getReferencesFrom(b));
            }
        }
        else {
            log.info("SKIPPING", "References tests");
        }

        // Some string data type tests
        // ---------------------------
        assertEquals(0, TAuthor_LAST_NAME().getDataType().precision());
        assertEquals(0, TAuthor_LAST_NAME().getDataType().scale());
        assertEquals(50, TAuthor_LAST_NAME().getDataType().length());

        // Some numeric data type tests
        // ----------------------------
        for (Field<?> field : T639().fields()) {
            if ("BYTE".equalsIgnoreCase(field.getName())) {
                assertEquals(Byte.class, field.getType());
                assertEquals(SQLDataType.TINYINT, field.getDataType());
                assertEquals(3, field.getDataType().precision());
                assertEquals(0, field.getDataType().scale());
                assertEquals(0, field.getDataType().length());
            }
            else if ("SHORT".equalsIgnoreCase(field.getName())) {
                assertEquals(Short.class, field.getType());
                assertEquals(SQLDataType.SMALLINT, field.getDataType());
                assertEquals(5, field.getDataType().precision());
                assertEquals(0, field.getDataType().scale());
                assertEquals(0, field.getDataType().length());
            }
            else if ("INTEGER".equalsIgnoreCase(field.getName())) {
                assertEquals(Integer.class, field.getType());
                assertEquals(SQLDataType.INTEGER, field.getDataType());
                assertEquals(10, field.getDataType().precision());
                assertEquals(0, field.getDataType().scale());
                assertEquals(0, field.getDataType().length());
            }
            else if ("LONG".equalsIgnoreCase(field.getName())) {
                assertEquals(Long.class, field.getType());
                assertEquals(SQLDataType.BIGINT, field.getDataType());
                assertEquals(19, field.getDataType().precision());
                assertEquals(0, field.getDataType().scale());
                assertEquals(0, field.getDataType().length());
            }
            else if ("BYTE_DECIMAL".equalsIgnoreCase(field.getName())) {
                assertEquals(Byte.class, field.getType());
                assertEquals(SQLDataType.TINYINT, field.getDataType());
                assertEquals(3, field.getDataType().precision());
                assertEquals(0, field.getDataType().scale());
                assertEquals(0, field.getDataType().length());
            }
            else if ("SHORT_DECIMAL".equalsIgnoreCase(field.getName())) {
                assertEquals(Short.class, field.getType());
                assertEquals(SQLDataType.SMALLINT, field.getDataType());
                assertEquals(5, field.getDataType().precision());
                assertEquals(0, field.getDataType().scale());
                assertEquals(0, field.getDataType().length());
            }
            else if ("INTEGER_DECIMAL".equalsIgnoreCase(field.getName())) {
                assertEquals(Integer.class, field.getType());
                assertEquals(SQLDataType.INTEGER, field.getDataType());
                assertEquals(10, field.getDataType().precision());
                assertEquals(0, field.getDataType().scale());
                assertEquals(0, field.getDataType().length());
            }
            else if ("LONG_DECIMAL".equalsIgnoreCase(field.getName())) {
                assertEquals(Long.class, field.getType());
                assertEquals(SQLDataType.BIGINT, field.getDataType());
                assertEquals(19, field.getDataType().precision());
                assertEquals(0, field.getDataType().scale());
                assertEquals(0, field.getDataType().length());
            }
            else if ("BIG_INTEGER".equalsIgnoreCase(field.getName())) {
                assertEquals(BigInteger.class, field.getType());
                assertEquals(SQLDataType.DECIMAL_INTEGER.getType(), field.getDataType().getType());
                assertTrue(field.getDataType().precision() > 0);
                assertEquals(0, field.getDataType().scale());
                assertEquals(0, field.getDataType().length());
            }

            // [#745] TODO: Unify distinction between NUMERIC and DECIMAL
            else if ("BIG_DECIMAL".equalsIgnoreCase(field.getName())
                    /* [pro] xx
                    xx xxxxxxxxxxxxxxxxxx xx xxxxxxxxxxxxxxxxx
                    xx xxxxxxxxxxxxxxxxxx xx xxxxxxxxxxxxxxxxxxxx
                    xx [/pro] */
                    && dialect().family() != SQLDialect.POSTGRES
                    && dialect().family() != SQLDialect.SQLITE) {

                assertEquals(BigDecimal.class, field.getType());
                assertEquals(SQLDataType.DECIMAL.getType(), field.getDataType().getType());
                assertTrue(field.getDataType().precision() > 0);
                assertEquals(5, field.getDataType().scale());
                assertEquals(0, field.getDataType().length());
            }
            else if ("BIG_DECIMAL".equalsIgnoreCase(field.getName())) {
                assertEquals(BigDecimal.class, field.getType());
                assertEquals(SQLDataType.NUMERIC.getType(), field.getDataType().getType());
                assertTrue(field.getDataType().precision() > 0);
                assertEquals(5, field.getDataType().scale());
                assertEquals(0, field.getDataType().length());
            }

            // [#746] TODO: Interestingly, HSQLDB and MySQL match REAL with DOUBLE.
            // There is no matching type for java.lang.Float...

            // [#456] TODO: Should floating point numbers have precision and scale?
            else if ("FLOAT".equalsIgnoreCase(field.getName())
                    && dialect() != SQLDialect.HSQLDB
                    && dialect() != SQLDialect.MARIADB
                    && dialect() != SQLDialect.MYSQL
                    /* [pro] xx
                    xx xxxxxxxxx xx xxxxxxxxxxxxxxxxx
                    xx [/pro] */
            ) {
                assertEquals(Float.class, field.getType());
                assertEquals(SQLDataType.REAL, field.getDataType());
                assertEquals(0, field.getDataType().length());
            }
            else if ("FLOAT".equalsIgnoreCase(field.getName())
                    && dialect() != SQLDialect.MARIADB
                    && dialect() != SQLDialect.MYSQL
                    /* [pro] xx
                    xx xxxxxxxxx xx xxxxxxxxxxxxxxxxx
                    xx [/pro] */
            ) {
                assertEquals(Double.class, field.getType());
                assertEquals(SQLDataType.DOUBLE, field.getDataType());
                assertEquals(0, field.getDataType().length());
            }
            else if ("FLOAT".equalsIgnoreCase(field.getName())) {
                assertEquals(Double.class, field.getType());
                assertEquals(SQLDataType.FLOAT, field.getDataType());
                assertEquals(0, field.getDataType().length());
            }

            // [#746] TODO: Fix this, too
            else if ("DOUBLE".equalsIgnoreCase(field.getName())
                    /* [pro] xx
                    xx xxxxxxxxxxxxxxxxxx xx xxxxxxxxxxxxxxxxxxxx
                    xx xxxxxxxxx xx xxxxxxxxxxxxxx
                    xx [/pro] */
            ) {

                assertEquals(Double.class, field.getType());
                assertEquals(SQLDataType.DOUBLE, field.getDataType());
                assertEquals(0, field.getDataType().length());
            }
            else if ("DOUBLE".equalsIgnoreCase(field.getName())) {
                assertEquals(Double.class, field.getType());
                assertEquals(SQLDataType.FLOAT, field.getDataType());
                assertEquals(0, field.getDataType().length());
            }
        }
    }

    public void testMetaData() throws Exception {
        Meta meta = create().meta();

        if (schema() != null) {

            // Catalog checks
            List<Catalog> metaCatalogs = meta.getCatalogs();
            List<Schema> metaSchemasFromCatalogs = new ArrayList<Schema>();

            for (Catalog metaCatalog : metaCatalogs) {
                metaSchemasFromCatalogs.addAll(metaCatalog.getSchemas());
            }

            assertTrue(metaSchemasFromCatalogs.contains(schema()));

            // The schema returned from meta should be equal to the
            // generated test schema
            List<Schema> metaSchemas = meta.getSchemas();
            assertTrue(metaSchemas.contains(schema()));
            assertEquals(metaSchemasFromCatalogs, metaSchemas);

            Schema metaSchema = metaSchemas.get(metaSchemas.indexOf(schema()));
            assertEquals(schema(), metaSchema);

            // The schema returned from meta should contain at least all the
            // generated test tables
            List<Table<?>> metaTables = metaSchema.getTables();
            assertTrue(metaTables.containsAll(schema().getTables()));
            assertTrue(metaTables.size() >= schema().getTables().size());

            metaTableChecks(metaTables);
        }

        // Some sample checks about tables returned from meta
        List<Table<?>> metaTables = meta.getTables();
        assertTrue(metaTables.contains(TAuthor()));
        assertTrue(metaTables.contains(TBook()));
        assertTrue(metaTables.contains(TBookStore()));
        assertTrue(metaTables.contains(TBookToBookStore()));
        assertTrue(metaTables.contains(VAuthor()));
        assertTrue(metaTables.contains(VBook()));
        assertTrue(metaTables.contains(VLibrary()));

        metaTableChecks(metaTables);
    }

    private void metaTableChecks(Collection<? extends Table<?>> metaTables) {
        for (Table<?> metaTable : metaTables) {

            // Check only the "TEST" schema, not "MULTI_SCHEMA" and others
            if (schema().equals(metaTable.getSchema())) {
                Table<?> generatedTable = schema().getTable(metaTable.getName());

                // Every table returned from meta should have a corresponding
                // table by name in the generated test tables
                if (generatedTable != null) {
                    assertNotNull(generatedTable);
                    assertEquals(metaTable, generatedTable);

                    // Check if fields match, as well
                    List<Field<?>> metaFields = asList(metaTable.fields());
                    assertTrue(metaFields.containsAll(asList(generatedTable.fields())));

                    // Check if relations are correctly loaded (and typed) as well
                    if (generatedTable.getPrimaryKey() != null) {
                        assertNotNull(metaTable.getPrimaryKey());
                        assertEquals(generatedTable, metaTable.getPrimaryKey().getTable());
                        assertEquals(generatedTable.getPrimaryKey().getFields(), metaTable.getPrimaryKey().getFields());
                    }

                    // Only truly updatable tables should be "Updatable"
                    else {
                        assertNull(metaTable.getPrimaryKey());
                    }
                }
            }
        }
    }
}
