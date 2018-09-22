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
package org.jooq.test;

import static java.util.Arrays.asList;
import static org.junit.Assert.fail;
import static org.jooq.Clause.CONDITION;
import static org.jooq.Clause.CONDITION_AND;
import static org.jooq.Clause.CONDITION_BETWEEN;
import static org.jooq.Clause.CONDITION_BETWEEN_SYMMETRIC;
import static org.jooq.Clause.CONDITION_COMPARISON;
import static org.jooq.Clause.CONDITION_EXISTS;
import static org.jooq.Clause.CONDITION_IN;
import static org.jooq.Clause.CONDITION_IS_NOT_NULL;
import static org.jooq.Clause.CONDITION_IS_NULL;
import static org.jooq.Clause.CONDITION_NOT;
import static org.jooq.Clause.CONDITION_NOT_BETWEEN;
import static org.jooq.Clause.CONDITION_NOT_BETWEEN_SYMMETRIC;
import static org.jooq.Clause.CONDITION_NOT_EXISTS;
import static org.jooq.Clause.CONDITION_NOT_IN;
import static org.jooq.Clause.CONDITION_OR;
import static org.jooq.Clause.DELETE;
import static org.jooq.Clause.DELETE_DELETE;
import static org.jooq.Clause.DELETE_WHERE;
import static org.jooq.Clause.FIELD;
import static org.jooq.Clause.FIELD_ALIAS;
import static org.jooq.Clause.FIELD_REFERENCE;
import static org.jooq.Clause.FIELD_ROW;
import static org.jooq.Clause.FIELD_VALUE;
import static org.jooq.Clause.INSERT;
import static org.jooq.Clause.INSERT_INSERT_INTO;
import static org.jooq.Clause.INSERT_ON_DUPLICATE_KEY_UPDATE;
import static org.jooq.Clause.INSERT_ON_DUPLICATE_KEY_UPDATE_ASSIGNMENT;
import static org.jooq.Clause.INSERT_RETURNING;
import static org.jooq.Clause.INSERT_SELECT;
import static org.jooq.Clause.INSERT_VALUES;
import static org.jooq.Clause.MERGE;
import static org.jooq.Clause.MERGE_DELETE_WHERE;
import static org.jooq.Clause.MERGE_MERGE_INTO;
import static org.jooq.Clause.MERGE_ON;
import static org.jooq.Clause.MERGE_SET;
import static org.jooq.Clause.MERGE_SET_ASSIGNMENT;
import static org.jooq.Clause.MERGE_USING;
import static org.jooq.Clause.MERGE_VALUES;
import static org.jooq.Clause.MERGE_WHEN_MATCHED_THEN_UPDATE;
import static org.jooq.Clause.MERGE_WHEN_NOT_MATCHED_THEN_INSERT;
import static org.jooq.Clause.MERGE_WHERE;
import static org.jooq.Clause.SELECT;
import static org.jooq.Clause.SELECT_CONNECT_BY;
import static org.jooq.Clause.SELECT_FROM;
import static org.jooq.Clause.SELECT_GROUP_BY;
import static org.jooq.Clause.SELECT_HAVING;
import static org.jooq.Clause.SELECT_ORDER_BY;
import static org.jooq.Clause.SELECT_SELECT;
import static org.jooq.Clause.SELECT_START_WITH;
import static org.jooq.Clause.SELECT_UNION_ALL;
import static org.jooq.Clause.SELECT_WHERE;
import static org.jooq.Clause.SELECT_WINDOW;
import static org.jooq.Clause.TABLE;
import static org.jooq.Clause.TABLE_ALIAS;
import static org.jooq.Clause.TABLE_REFERENCE;
import static org.jooq.Clause.TABLE_VALUES;
import static org.jooq.Clause.TRUNCATE;
import static org.jooq.Clause.TRUNCATE_TRUNCATE;
import static org.jooq.Clause.UPDATE;
import static org.jooq.Clause.UPDATE_FROM;
import static org.jooq.Clause.UPDATE_RETURNING;
import static org.jooq.Clause.UPDATE_SET;
import static org.jooq.Clause.UPDATE_SET_ASSIGNMENT;
import static org.jooq.Clause.UPDATE_UPDATE;
import static org.jooq.Clause.UPDATE_WHERE;
import static org.jooq.SQLDialect.FIREBIRD;
import static org.jooq.SQLDialect.MYSQL;
import static org.jooq.SQLDialect.POSTGRES;
import static org.jooq.impl.DSL.exists;
import static org.jooq.impl.DSL.notExists;
import static org.jooq.impl.DSL.row;
import static org.jooq.impl.DSL.select;
import static org.jooq.impl.DSL.selectOne;
import static org.jooq.impl.DSL.val;
import static org.jooq.impl.DSL.values;
import static org.jooq.impl.DefaultVisitListenerProvider.providers;
import static org.jooq.test.data.Table1.FIELD_DATE1;
import static org.jooq.test.data.Table1.FIELD_ID1;
import static org.jooq.test.data.Table1.FIELD_NAME1;
import static org.jooq.test.data.Table1.TABLE1;
import static org.jooq.test.data.Table2.FIELD_ID2;
import static org.jooq.test.data.Table2.TABLE2;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.jooq.BindContext;
import org.jooq.Clause;
import org.jooq.DSLContext;
import org.jooq.QueryPart;
import org.jooq.RenderContext;
import org.jooq.VisitContext;
import org.jooq.VisitListener;
import org.jooq.impl.DSL;
import org.jooq.tools.jdbc.MockConnection;
import org.jooq.tools.jdbc.MockDataProvider;
import org.jooq.tools.jdbc.MockExecuteContext;
import org.jooq.tools.jdbc.MockResult;
import org.jooq.tools.jdbc.MockStatement;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

/**
 * Some common tests related to {@link VisitContext}
 *
 * @author Lukas Eder
 */
@SuppressWarnings("unchecked")
public class VisitContextTest extends AbstractTest {

    private DSLContext ctx;
    private SimpleListener listener;

    @Before
    public void setup() {
        listener = new SimpleListener();
        ctx = DSL.using(
            create.configuration().derive(providers(listener)));
    }

    @After
    public void teardown() {
        ctx = null;
        listener = null;
    }

    @Test
    public void test_table_VALUES() {

        // Postgres supports VALUES table constructors
        ctx.configuration().set(POSTGRES);
        assertEvents(asList(
            asList(TABLE),
            asList(TABLE, TABLE_ALIAS),
            asList(TABLE, TABLE_ALIAS, TABLE),
            asList(TABLE, TABLE_ALIAS, TABLE, TABLE_VALUES),
            asList(TABLE, TABLE_ALIAS, TABLE, TABLE_VALUES, FIELD_ROW),
            asList(TABLE, TABLE_ALIAS, TABLE, TABLE_VALUES, FIELD_ROW, FIELD),
            asList(TABLE, TABLE_ALIAS, TABLE, TABLE_VALUES, FIELD_ROW, FIELD, FIELD_VALUE),
            asList(TABLE, TABLE_ALIAS, TABLE, TABLE_VALUES, FIELD_ROW, FIELD),
            asList(TABLE, TABLE_ALIAS, TABLE, TABLE_VALUES, FIELD_ROW, FIELD, FIELD_VALUE),
            asList(TABLE, TABLE_ALIAS, TABLE, TABLE_VALUES, FIELD_ROW),
            asList(TABLE, TABLE_ALIAS, TABLE, TABLE_VALUES, FIELD_ROW, FIELD),
            asList(TABLE, TABLE_ALIAS, TABLE, TABLE_VALUES, FIELD_ROW, FIELD, FIELD_VALUE),
            asList(TABLE, TABLE_ALIAS, TABLE, TABLE_VALUES, FIELD_ROW, FIELD),
            asList(TABLE, TABLE_ALIAS, TABLE, TABLE_VALUES, FIELD_ROW, FIELD, FIELD_VALUE)
        ),
        values(row(1, "value"), row(2, "value")),
        r_ctx().declareTables(true),
        b_ctx().declareTables(true));
    }

    @Test
    public void test_tableAliasReference() {
        assertEvents(asList(
            asList(TABLE),
            asList(TABLE, TABLE_REFERENCE)
        ),
        TABLE1.as("x"));
    }

    @Test
    public void test_tableAliasDeclaration() {
        assertEvents(asList(
            asList(TABLE),
            asList(TABLE, TABLE_ALIAS),
            asList(TABLE, TABLE_ALIAS, TABLE),
            asList(TABLE, TABLE_ALIAS, TABLE, TABLE_REFERENCE)
        ),
        TABLE1.as("x"),
        r_ctx().declareTables(true),
        b_ctx().declareTables(true));
    }

    @Test
    public void test_fieldAliasReference() {
        assertEvents(asList(
            asList(FIELD),
            asList(FIELD, FIELD_REFERENCE)
        ),
        FIELD_ID1.as("x"));
    }

    @Test
    public void test_fieldAliasDeclaration() {
        assertEvents(asList(
            asList(FIELD),
            asList(FIELD, FIELD_ALIAS),
            asList(FIELD, FIELD_ALIAS, FIELD),
            asList(FIELD, FIELD_ALIAS, FIELD, FIELD_REFERENCE)
        ),
        FIELD_ID1.as("x"),
        r_ctx().declareFields(true),
        b_ctx().declareFields(true));
    }

    @Test
    public void test_INSERT_VALUES_simple() {
        assertEvents(asList(
            asList(INSERT),
            asList(INSERT, INSERT_INSERT_INTO),
            asList(INSERT, INSERT_INSERT_INTO, TABLE),
            asList(INSERT, INSERT_INSERT_INTO, TABLE, TABLE_REFERENCE),
            asList(INSERT, INSERT_INSERT_INTO, FIELD),
            asList(INSERT, INSERT_INSERT_INTO, FIELD, FIELD_REFERENCE),
            asList(INSERT, INSERT_INSERT_INTO, FIELD),
            asList(INSERT, INSERT_INSERT_INTO, FIELD, FIELD_REFERENCE),
            asList(INSERT, INSERT_INSERT_INTO, FIELD),
            asList(INSERT, INSERT_INSERT_INTO, FIELD, FIELD_REFERENCE),
            asList(INSERT, INSERT_VALUES),
            asList(INSERT, INSERT_VALUES, FIELD_ROW),
            asList(INSERT, INSERT_VALUES, FIELD_ROW, FIELD),
            asList(INSERT, INSERT_VALUES, FIELD_ROW, FIELD, FIELD_VALUE),
            asList(INSERT, INSERT_VALUES, FIELD_ROW, FIELD),
            asList(INSERT, INSERT_VALUES, FIELD_ROW, FIELD, FIELD_VALUE),
            asList(INSERT, INSERT_VALUES, FIELD_ROW, FIELD),
            asList(INSERT, INSERT_VALUES, FIELD_ROW, FIELD, FIELD_VALUE),
            asList(INSERT, INSERT_ON_DUPLICATE_KEY_UPDATE),
            asList(INSERT, INSERT_RETURNING)
        ),
        ctx.insertInto(TABLE1)
           .values(1, "value", null));
    }

    @Test
    public void test_INSERT_VALUES_RETURNING() {

        // Postgres is the only dialect to actually have a RETURNING clause.
        ctx.configuration().set(POSTGRES);
        assertEvents(asList(
            asList(INSERT),
            asList(INSERT, INSERT_INSERT_INTO),
            asList(INSERT, INSERT_INSERT_INTO, TABLE),
            asList(INSERT, INSERT_INSERT_INTO, TABLE, TABLE_REFERENCE),
            asList(INSERT, INSERT_INSERT_INTO, FIELD),
            asList(INSERT, INSERT_INSERT_INTO, FIELD, FIELD_REFERENCE),
            asList(INSERT, INSERT_INSERT_INTO, FIELD),
            asList(INSERT, INSERT_INSERT_INTO, FIELD, FIELD_REFERENCE),
            asList(INSERT, INSERT_INSERT_INTO, FIELD),
            asList(INSERT, INSERT_INSERT_INTO, FIELD, FIELD_REFERENCE),
            asList(INSERT, INSERT_VALUES),
            asList(INSERT, INSERT_VALUES, FIELD_ROW),
            asList(INSERT, INSERT_VALUES, FIELD_ROW, FIELD),
            asList(INSERT, INSERT_VALUES, FIELD_ROW, FIELD, FIELD_VALUE),
            asList(INSERT, INSERT_VALUES, FIELD_ROW, FIELD),
            asList(INSERT, INSERT_VALUES, FIELD_ROW, FIELD, FIELD_VALUE),
            asList(INSERT, INSERT_VALUES, FIELD_ROW, FIELD),
            asList(INSERT, INSERT_VALUES, FIELD_ROW, FIELD, FIELD_VALUE),
            asList(INSERT, INSERT_ON_DUPLICATE_KEY_UPDATE),
            asList(INSERT, INSERT_RETURNING),
            asList(INSERT, INSERT_RETURNING, FIELD),
            asList(INSERT, INSERT_RETURNING, FIELD, FIELD_REFERENCE),
            asList(INSERT, INSERT_RETURNING, FIELD),
            asList(INSERT, INSERT_RETURNING, FIELD, FIELD_REFERENCE)
        ),
        ctx.insertInto(TABLE1)
           .values(1, "value", null)
           .returning(FIELD_ID1, FIELD_NAME1));
    }

    @Test
    public void test_INSERT_VALUES_ON_DUPLICATE_KEY() {

        // Use MySQL to actually render ON DUPLICATE KEY UPDATE
        ctx.configuration().set(MYSQL);

        assertEvents(asList(
            asList(INSERT),
            asList(INSERT, INSERT_INSERT_INTO),
            asList(INSERT, INSERT_INSERT_INTO, TABLE),
            asList(INSERT, INSERT_INSERT_INTO, TABLE, TABLE_REFERENCE),
            asList(INSERT, INSERT_INSERT_INTO, FIELD),
            asList(INSERT, INSERT_INSERT_INTO, FIELD, FIELD_REFERENCE),
            asList(INSERT, INSERT_INSERT_INTO, FIELD),
            asList(INSERT, INSERT_INSERT_INTO, FIELD, FIELD_REFERENCE),
            asList(INSERT, INSERT_INSERT_INTO, FIELD),
            asList(INSERT, INSERT_INSERT_INTO, FIELD, FIELD_REFERENCE),
            asList(INSERT, INSERT_VALUES),
            asList(INSERT, INSERT_VALUES, FIELD_ROW),
            asList(INSERT, INSERT_VALUES, FIELD_ROW, FIELD),
            asList(INSERT, INSERT_VALUES, FIELD_ROW, FIELD, FIELD_VALUE),
            asList(INSERT, INSERT_VALUES, FIELD_ROW, FIELD),
            asList(INSERT, INSERT_VALUES, FIELD_ROW, FIELD, FIELD_VALUE),
            asList(INSERT, INSERT_VALUES, FIELD_ROW, FIELD),
            asList(INSERT, INSERT_VALUES, FIELD_ROW, FIELD, FIELD_VALUE),
            asList(INSERT, INSERT_ON_DUPLICATE_KEY_UPDATE),
            asList(INSERT, INSERT_ON_DUPLICATE_KEY_UPDATE, INSERT_ON_DUPLICATE_KEY_UPDATE_ASSIGNMENT),
            asList(INSERT, INSERT_ON_DUPLICATE_KEY_UPDATE, INSERT_ON_DUPLICATE_KEY_UPDATE_ASSIGNMENT, FIELD),
            asList(INSERT, INSERT_ON_DUPLICATE_KEY_UPDATE, INSERT_ON_DUPLICATE_KEY_UPDATE_ASSIGNMENT, FIELD, FIELD_REFERENCE),
            asList(INSERT, INSERT_ON_DUPLICATE_KEY_UPDATE, INSERT_ON_DUPLICATE_KEY_UPDATE_ASSIGNMENT, FIELD),
            asList(INSERT, INSERT_ON_DUPLICATE_KEY_UPDATE, INSERT_ON_DUPLICATE_KEY_UPDATE_ASSIGNMENT, FIELD, FIELD_VALUE),
            asList(INSERT, INSERT_ON_DUPLICATE_KEY_UPDATE, INSERT_ON_DUPLICATE_KEY_UPDATE_ASSIGNMENT),
            asList(INSERT, INSERT_ON_DUPLICATE_KEY_UPDATE, INSERT_ON_DUPLICATE_KEY_UPDATE_ASSIGNMENT, FIELD),
            asList(INSERT, INSERT_ON_DUPLICATE_KEY_UPDATE, INSERT_ON_DUPLICATE_KEY_UPDATE_ASSIGNMENT, FIELD, FIELD_REFERENCE),
            asList(INSERT, INSERT_ON_DUPLICATE_KEY_UPDATE, INSERT_ON_DUPLICATE_KEY_UPDATE_ASSIGNMENT, FIELD),
            asList(INSERT, INSERT_ON_DUPLICATE_KEY_UPDATE, INSERT_ON_DUPLICATE_KEY_UPDATE_ASSIGNMENT, FIELD, FIELD_REFERENCE),
            asList(INSERT, INSERT_RETURNING)
        ),
        ctx.insertInto(TABLE1)
           .values(1, "value", null)
           .onDuplicateKeyUpdate()
           .set(FIELD_NAME1, "value")
           .set(FIELD_DATE1, FIELD_DATE1));
    }

    @Test
    public void test_INSERT_VALUES_multiple() {

        // Postgres has a native implementation for multi-value inserts
        ctx.configuration().set(POSTGRES);

        assertEvents(asList(
            asList(INSERT),
            asList(INSERT, INSERT_INSERT_INTO),
            asList(INSERT, INSERT_INSERT_INTO, TABLE),
            asList(INSERT, INSERT_INSERT_INTO, TABLE, TABLE_REFERENCE),
            asList(INSERT, INSERT_INSERT_INTO, FIELD),
            asList(INSERT, INSERT_INSERT_INTO, FIELD, FIELD_REFERENCE),
            asList(INSERT, INSERT_INSERT_INTO, FIELD),
            asList(INSERT, INSERT_INSERT_INTO, FIELD, FIELD_REFERENCE),
            asList(INSERT, INSERT_INSERT_INTO, FIELD),
            asList(INSERT, INSERT_INSERT_INTO, FIELD, FIELD_REFERENCE),
            asList(INSERT, INSERT_VALUES),
            asList(INSERT, INSERT_VALUES, FIELD_ROW),
            asList(INSERT, INSERT_VALUES, FIELD_ROW, FIELD),
            asList(INSERT, INSERT_VALUES, FIELD_ROW, FIELD, FIELD_VALUE),
            asList(INSERT, INSERT_VALUES, FIELD_ROW, FIELD),
            asList(INSERT, INSERT_VALUES, FIELD_ROW, FIELD, FIELD_VALUE),
            asList(INSERT, INSERT_VALUES, FIELD_ROW, FIELD),
            asList(INSERT, INSERT_VALUES, FIELD_ROW, FIELD, FIELD_VALUE),
            asList(INSERT, INSERT_VALUES, FIELD_ROW),
            asList(INSERT, INSERT_VALUES, FIELD_ROW, FIELD),
            asList(INSERT, INSERT_VALUES, FIELD_ROW, FIELD, FIELD_VALUE),
            asList(INSERT, INSERT_VALUES, FIELD_ROW, FIELD),
            asList(INSERT, INSERT_VALUES, FIELD_ROW, FIELD, FIELD_VALUE),
            asList(INSERT, INSERT_VALUES, FIELD_ROW, FIELD),
            asList(INSERT, INSERT_VALUES, FIELD_ROW, FIELD, FIELD_VALUE),
            asList(INSERT, INSERT_ON_DUPLICATE_KEY_UPDATE),
            asList(INSERT, INSERT_RETURNING)
        ),
        ctx.insertInto(TABLE1)
           .values(1, "value", null)
           .values(2, "value", null));
    }

    @Test
    public void test_INSERT_VALUES_multiple_emulated() {

        // Firebird emulates multi-record inserts through INSERT .. SELECT
        ctx.configuration().set(FIREBIRD);

        assertEvents(asList(
            asList(INSERT),
            asList(INSERT, INSERT_INSERT_INTO),
            asList(INSERT, INSERT_INSERT_INTO, TABLE),
            asList(INSERT, INSERT_INSERT_INTO, TABLE, TABLE_REFERENCE),
            asList(INSERT, INSERT_INSERT_INTO, FIELD),
            asList(INSERT, INSERT_INSERT_INTO, FIELD, FIELD_REFERENCE),
            asList(INSERT, INSERT_INSERT_INTO, FIELD),
            asList(INSERT, INSERT_INSERT_INTO, FIELD, FIELD_REFERENCE),
            asList(INSERT, INSERT_INSERT_INTO, FIELD),
            asList(INSERT, INSERT_INSERT_INTO, FIELD, FIELD_REFERENCE),
            asList(INSERT, INSERT_SELECT),
            asList(INSERT, INSERT_SELECT, SELECT_UNION_ALL),
            asList(INSERT, INSERT_SELECT, SELECT_UNION_ALL, SELECT),
            asList(INSERT, INSERT_SELECT, SELECT_UNION_ALL, SELECT, SELECT_SELECT),
            asList(INSERT, INSERT_SELECT, SELECT_UNION_ALL, SELECT, SELECT_SELECT, FIELD),
            asList(INSERT, INSERT_SELECT, SELECT_UNION_ALL, SELECT, SELECT_SELECT, FIELD, FIELD_VALUE),
            asList(INSERT, INSERT_SELECT, SELECT_UNION_ALL, SELECT, SELECT_SELECT, FIELD),
            asList(INSERT, INSERT_SELECT, SELECT_UNION_ALL, SELECT, SELECT_SELECT, FIELD, FIELD_VALUE),
            asList(INSERT, INSERT_SELECT, SELECT_UNION_ALL, SELECT, SELECT_SELECT, FIELD),
            asList(INSERT, INSERT_SELECT, SELECT_UNION_ALL, SELECT, SELECT_SELECT, FIELD, FIELD_VALUE),
            asList(INSERT, INSERT_SELECT, SELECT_UNION_ALL, SELECT, SELECT_FROM),
            asList(INSERT, INSERT_SELECT, SELECT_UNION_ALL, SELECT, SELECT_FROM, TABLE),
            asList(INSERT, INSERT_SELECT, SELECT_UNION_ALL, SELECT, SELECT_WHERE),
            asList(INSERT, INSERT_SELECT, SELECT_UNION_ALL, SELECT, SELECT_START_WITH),
            asList(INSERT, INSERT_SELECT, SELECT_UNION_ALL, SELECT, SELECT_CONNECT_BY),
            asList(INSERT, INSERT_SELECT, SELECT_UNION_ALL, SELECT, SELECT_GROUP_BY),
            asList(INSERT, INSERT_SELECT, SELECT_UNION_ALL, SELECT, SELECT_HAVING),
            asList(INSERT, INSERT_SELECT, SELECT_UNION_ALL, SELECT, SELECT_WINDOW),
            asList(INSERT, INSERT_SELECT, SELECT_UNION_ALL, SELECT, SELECT_ORDER_BY),
            asList(INSERT, INSERT_SELECT, SELECT_UNION_ALL, SELECT),
            asList(INSERT, INSERT_SELECT, SELECT_UNION_ALL, SELECT, SELECT_SELECT),
            asList(INSERT, INSERT_SELECT, SELECT_UNION_ALL, SELECT, SELECT_SELECT, FIELD),
            asList(INSERT, INSERT_SELECT, SELECT_UNION_ALL, SELECT, SELECT_SELECT, FIELD, FIELD_VALUE),
            asList(INSERT, INSERT_SELECT, SELECT_UNION_ALL, SELECT, SELECT_SELECT, FIELD),
            asList(INSERT, INSERT_SELECT, SELECT_UNION_ALL, SELECT, SELECT_SELECT, FIELD, FIELD_VALUE),
            asList(INSERT, INSERT_SELECT, SELECT_UNION_ALL, SELECT, SELECT_SELECT, FIELD),
            asList(INSERT, INSERT_SELECT, SELECT_UNION_ALL, SELECT, SELECT_SELECT, FIELD, FIELD_VALUE),
            asList(INSERT, INSERT_SELECT, SELECT_UNION_ALL, SELECT, SELECT_FROM),
            asList(INSERT, INSERT_SELECT, SELECT_UNION_ALL, SELECT, SELECT_FROM, TABLE),
            asList(INSERT, INSERT_SELECT, SELECT_UNION_ALL, SELECT, SELECT_WHERE),
            asList(INSERT, INSERT_SELECT, SELECT_UNION_ALL, SELECT, SELECT_START_WITH),
            asList(INSERT, INSERT_SELECT, SELECT_UNION_ALL, SELECT, SELECT_CONNECT_BY),
            asList(INSERT, INSERT_SELECT, SELECT_UNION_ALL, SELECT, SELECT_GROUP_BY),
            asList(INSERT, INSERT_SELECT, SELECT_UNION_ALL, SELECT, SELECT_HAVING),
            asList(INSERT, INSERT_SELECT, SELECT_UNION_ALL, SELECT, SELECT_WINDOW),
            asList(INSERT, INSERT_SELECT, SELECT_UNION_ALL, SELECT, SELECT_ORDER_BY),
            asList(INSERT, INSERT_ON_DUPLICATE_KEY_UPDATE),
            asList(INSERT, INSERT_RETURNING)
        ),
        ctx.insertInto(TABLE1)
           .values(1, "value", null)
           .values(2, "value", null));
    }

    @Test
    public void test_INSERT_SELECT() {
        assertEvents(asList(
            asList(INSERT),
            asList(INSERT, INSERT_INSERT_INTO),
            asList(INSERT, INSERT_INSERT_INTO, TABLE),
            asList(INSERT, INSERT_INSERT_INTO, TABLE, TABLE_REFERENCE),
            asList(INSERT, INSERT_INSERT_INTO, FIELD),
            asList(INSERT, INSERT_INSERT_INTO, FIELD, FIELD_REFERENCE),
            asList(INSERT, INSERT_INSERT_INTO, FIELD),
            asList(INSERT, INSERT_INSERT_INTO, FIELD, FIELD_REFERENCE),
            asList(INSERT, INSERT_INSERT_INTO, FIELD),
            asList(INSERT, INSERT_INSERT_INTO, FIELD, FIELD_REFERENCE),
            asList(INSERT, INSERT_SELECT),
            asList(INSERT, INSERT_SELECT, SELECT),
            asList(INSERT, INSERT_SELECT, SELECT, SELECT_SELECT),
            asList(INSERT, INSERT_SELECT, SELECT, SELECT_SELECT, FIELD),
            asList(INSERT, INSERT_SELECT, SELECT, SELECT_SELECT, FIELD, FIELD_VALUE),
            asList(INSERT, INSERT_SELECT, SELECT, SELECT_SELECT, FIELD),
            asList(INSERT, INSERT_SELECT, SELECT, SELECT_SELECT, FIELD, FIELD_VALUE),
            asList(INSERT, INSERT_SELECT, SELECT, SELECT_SELECT, FIELD),
            asList(INSERT, INSERT_SELECT, SELECT, SELECT_SELECT, FIELD, FIELD_VALUE),
            asList(INSERT, INSERT_SELECT, SELECT, SELECT_FROM),
            asList(INSERT, INSERT_SELECT, SELECT, SELECT_FROM, TABLE),
            asList(INSERT, INSERT_SELECT, SELECT, SELECT_WHERE),
            asList(INSERT, INSERT_SELECT, SELECT, SELECT_START_WITH),
            asList(INSERT, INSERT_SELECT, SELECT, SELECT_CONNECT_BY),
            asList(INSERT, INSERT_SELECT, SELECT, SELECT_GROUP_BY),
            asList(INSERT, INSERT_SELECT, SELECT, SELECT_HAVING),
            asList(INSERT, INSERT_SELECT, SELECT, SELECT_WINDOW),
            asList(INSERT, INSERT_SELECT, SELECT, SELECT_ORDER_BY),
            asList(INSERT, INSERT_ON_DUPLICATE_KEY_UPDATE),
            asList(INSERT, INSERT_RETURNING)
        ),
        ctx.insertInto(TABLE1)
           .select(select(val(1), val("value"), val(null))));
    }

    @Test
    public void test_MERGE_simple() {
        assertEvents(asList(
            asList(MERGE),
            asList(MERGE, MERGE_MERGE_INTO),
            asList(MERGE, MERGE_MERGE_INTO, TABLE),
            asList(MERGE, MERGE_MERGE_INTO, TABLE, TABLE_REFERENCE),
            asList(MERGE, MERGE_USING),
            asList(MERGE, MERGE_USING, TABLE),
            asList(MERGE, MERGE_USING, TABLE, TABLE_REFERENCE),
            asList(MERGE, MERGE_ON),
            asList(MERGE, MERGE_ON, CONDITION),
            asList(MERGE, MERGE_ON, CONDITION, CONDITION_COMPARISON),
            asList(MERGE, MERGE_ON, CONDITION, CONDITION_COMPARISON, FIELD),
            asList(MERGE, MERGE_ON, CONDITION, CONDITION_COMPARISON, FIELD, FIELD_REFERENCE),
            asList(MERGE, MERGE_ON, CONDITION, CONDITION_COMPARISON, FIELD),
            asList(MERGE, MERGE_ON, CONDITION, CONDITION_COMPARISON, FIELD, FIELD_REFERENCE),
            asList(MERGE, MERGE_WHEN_MATCHED_THEN_UPDATE),
            asList(MERGE, MERGE_WHEN_MATCHED_THEN_UPDATE, MERGE_SET),
            asList(MERGE, MERGE_WHEN_MATCHED_THEN_UPDATE, MERGE_SET, MERGE_SET_ASSIGNMENT),
            asList(MERGE, MERGE_WHEN_MATCHED_THEN_UPDATE, MERGE_SET, MERGE_SET_ASSIGNMENT, FIELD),
            asList(MERGE, MERGE_WHEN_MATCHED_THEN_UPDATE, MERGE_SET, MERGE_SET_ASSIGNMENT, FIELD, FIELD_REFERENCE),
            asList(MERGE, MERGE_WHEN_MATCHED_THEN_UPDATE, MERGE_SET, MERGE_SET_ASSIGNMENT, FIELD),
            asList(MERGE, MERGE_WHEN_MATCHED_THEN_UPDATE, MERGE_SET, MERGE_SET_ASSIGNMENT, FIELD, FIELD_VALUE),
            asList(MERGE, MERGE_WHEN_MATCHED_THEN_UPDATE, MERGE_SET, MERGE_SET_ASSIGNMENT),
            asList(MERGE, MERGE_WHEN_MATCHED_THEN_UPDATE, MERGE_SET, MERGE_SET_ASSIGNMENT, FIELD),
            asList(MERGE, MERGE_WHEN_MATCHED_THEN_UPDATE, MERGE_SET, MERGE_SET_ASSIGNMENT, FIELD, FIELD_REFERENCE),
            asList(MERGE, MERGE_WHEN_MATCHED_THEN_UPDATE, MERGE_SET, MERGE_SET_ASSIGNMENT, FIELD),
            asList(MERGE, MERGE_WHEN_MATCHED_THEN_UPDATE, MERGE_SET, MERGE_SET_ASSIGNMENT, FIELD, FIELD_REFERENCE),
            asList(MERGE, MERGE_WHEN_MATCHED_THEN_UPDATE, MERGE_WHERE),
            asList(MERGE, MERGE_WHEN_MATCHED_THEN_UPDATE, MERGE_DELETE_WHERE),
            asList(MERGE, MERGE_WHEN_NOT_MATCHED_THEN_INSERT),
            asList(MERGE, MERGE_WHEN_NOT_MATCHED_THEN_INSERT, FIELD),
            asList(MERGE, MERGE_WHEN_NOT_MATCHED_THEN_INSERT, FIELD, FIELD_REFERENCE),
            asList(MERGE, MERGE_WHEN_NOT_MATCHED_THEN_INSERT, FIELD),
            asList(MERGE, MERGE_WHEN_NOT_MATCHED_THEN_INSERT, FIELD, FIELD_REFERENCE),
            asList(MERGE, MERGE_WHEN_NOT_MATCHED_THEN_INSERT, MERGE_VALUES),
            asList(MERGE, MERGE_WHEN_NOT_MATCHED_THEN_INSERT, MERGE_VALUES, FIELD_ROW),
            asList(MERGE, MERGE_WHEN_NOT_MATCHED_THEN_INSERT, MERGE_VALUES, FIELD_ROW, FIELD),
            asList(MERGE, MERGE_WHEN_NOT_MATCHED_THEN_INSERT, MERGE_VALUES, FIELD_ROW, FIELD, FIELD_VALUE),
            asList(MERGE, MERGE_WHEN_NOT_MATCHED_THEN_INSERT, MERGE_VALUES, FIELD_ROW, FIELD),
            asList(MERGE, MERGE_WHEN_NOT_MATCHED_THEN_INSERT, MERGE_VALUES, FIELD_ROW, FIELD, FIELD_VALUE),
            asList(MERGE, MERGE_WHEN_NOT_MATCHED_THEN_INSERT, MERGE_WHERE)
        ),
        ctx.mergeInto(TABLE1)
           .using(TABLE2)
           .on(FIELD_ID1.eq(FIELD_ID2))
           .whenMatchedThenUpdate()
           .set(FIELD_NAME1, "a")
           .set(FIELD_DATE1, FIELD_DATE1)
           .whenNotMatchedThenInsert(FIELD_ID1, FIELD_NAME1)
           .values(1, "a"));
    }

    @Test
    public void test_TRUNCATE_simple() {
        assertEvents(asList(
            asList(TRUNCATE),
            asList(TRUNCATE, TRUNCATE_TRUNCATE),
            asList(TRUNCATE, TRUNCATE_TRUNCATE, TABLE),
            asList(TRUNCATE, TRUNCATE_TRUNCATE, TABLE, TABLE_REFERENCE)
        ),
        ctx.truncate(TABLE1));
    }

    @Test
    public void test_DELETE_simple() {
        assertEvents(asList(
            asList(DELETE),
            asList(DELETE, DELETE_DELETE),
            asList(DELETE, DELETE_DELETE, TABLE),
            asList(DELETE, DELETE_DELETE, TABLE, TABLE_REFERENCE),
            asList(DELETE, DELETE_WHERE)
        ),
        ctx.delete(TABLE1));
    }

    @Test
    public void test_DELETE_WHERE() {
        QueryPart part =
        ctx.delete(TABLE1)
           .where(FIELD_ID1.eq(1));

        assertEvents(asList(
            asList(DELETE),
            asList(DELETE, DELETE_DELETE),
            asList(DELETE, DELETE_DELETE, TABLE),
            asList(DELETE, DELETE_DELETE, TABLE, TABLE_REFERENCE),
            asList(DELETE, DELETE_WHERE),
            asList(DELETE, DELETE_WHERE, CONDITION),
            asList(DELETE, DELETE_WHERE, CONDITION, CONDITION_COMPARISON),
            asList(DELETE, DELETE_WHERE, CONDITION, CONDITION_COMPARISON, FIELD),
            asList(DELETE, DELETE_WHERE, CONDITION, CONDITION_COMPARISON, FIELD, FIELD_REFERENCE),
            asList(DELETE, DELETE_WHERE, CONDITION, CONDITION_COMPARISON, FIELD),
            asList(DELETE, DELETE_WHERE, CONDITION, CONDITION_COMPARISON, FIELD, FIELD_VALUE)
        ), part);
    }

    @Test
    public void test_UPDATE_SET_simple() {
        assertEvents(asList(
            asList(UPDATE),
            asList(UPDATE, UPDATE_UPDATE),
            asList(UPDATE, UPDATE_UPDATE, TABLE),
            asList(UPDATE, UPDATE_UPDATE, TABLE, TABLE_REFERENCE),
            asList(UPDATE, UPDATE_SET),
            asList(UPDATE, UPDATE_SET, UPDATE_SET_ASSIGNMENT),
            asList(UPDATE, UPDATE_SET, UPDATE_SET_ASSIGNMENT, FIELD),
            asList(UPDATE, UPDATE_SET, UPDATE_SET_ASSIGNMENT, FIELD, FIELD_REFERENCE),
            asList(UPDATE, UPDATE_SET, UPDATE_SET_ASSIGNMENT, FIELD),
            asList(UPDATE, UPDATE_SET, UPDATE_SET_ASSIGNMENT, FIELD, FIELD_VALUE),
            asList(UPDATE, UPDATE_FROM),
            asList(UPDATE, UPDATE_WHERE),
            asList(UPDATE, UPDATE_RETURNING)
        ),
        ctx.update(TABLE1)
           .set(FIELD_NAME1, "value"));
    }

    @Test
    public void test_UPDATE_SET_twoValues() {
        assertEvents(asList(
            asList(UPDATE),
            asList(UPDATE, UPDATE_UPDATE),
            asList(UPDATE, UPDATE_UPDATE, TABLE),
            asList(UPDATE, UPDATE_UPDATE, TABLE, TABLE_REFERENCE),
            asList(UPDATE, UPDATE_SET),
            asList(UPDATE, UPDATE_SET, UPDATE_SET_ASSIGNMENT),
            asList(UPDATE, UPDATE_SET, UPDATE_SET_ASSIGNMENT, FIELD),
            asList(UPDATE, UPDATE_SET, UPDATE_SET_ASSIGNMENT, FIELD, FIELD_REFERENCE),
            asList(UPDATE, UPDATE_SET, UPDATE_SET_ASSIGNMENT, FIELD),
            asList(UPDATE, UPDATE_SET, UPDATE_SET_ASSIGNMENT, FIELD, FIELD_VALUE),
            asList(UPDATE, UPDATE_SET, UPDATE_SET_ASSIGNMENT),
            asList(UPDATE, UPDATE_SET, UPDATE_SET_ASSIGNMENT, FIELD),
            asList(UPDATE, UPDATE_SET, UPDATE_SET_ASSIGNMENT, FIELD, FIELD_REFERENCE),
            asList(UPDATE, UPDATE_SET, UPDATE_SET_ASSIGNMENT, FIELD),
            asList(UPDATE, UPDATE_SET, UPDATE_SET_ASSIGNMENT, FIELD, FIELD_REFERENCE),
            asList(UPDATE, UPDATE_FROM),
            asList(UPDATE, UPDATE_WHERE),
            asList(UPDATE, UPDATE_RETURNING)
        ),
        ctx.update(TABLE1)
           .set(FIELD_NAME1, "value")
           .set(FIELD_DATE1, FIELD_DATE1));
    }

    @Test
    public void test_UPDATE_SET_rowValueExpressions() {

        // Postgres is known to support this syntax particularly well
        ctx.configuration().set(POSTGRES);
        assertEvents(asList(
            asList(UPDATE),
            asList(UPDATE, UPDATE_UPDATE),
            asList(UPDATE, UPDATE_UPDATE, TABLE),
            asList(UPDATE, UPDATE_UPDATE, TABLE, TABLE_REFERENCE),
            asList(UPDATE, UPDATE_SET),
            asList(UPDATE, UPDATE_SET, UPDATE_SET_ASSIGNMENT),
            asList(UPDATE, UPDATE_SET, UPDATE_SET_ASSIGNMENT, FIELD_ROW),
            asList(UPDATE, UPDATE_SET, UPDATE_SET_ASSIGNMENT, FIELD_ROW, FIELD),
            asList(UPDATE, UPDATE_SET, UPDATE_SET_ASSIGNMENT, FIELD_ROW, FIELD, FIELD_REFERENCE),
            asList(UPDATE, UPDATE_SET, UPDATE_SET_ASSIGNMENT, FIELD_ROW, FIELD),
            asList(UPDATE, UPDATE_SET, UPDATE_SET_ASSIGNMENT, FIELD_ROW, FIELD, FIELD_REFERENCE),
            asList(UPDATE, UPDATE_SET, UPDATE_SET_ASSIGNMENT, FIELD_ROW),
            asList(UPDATE, UPDATE_SET, UPDATE_SET_ASSIGNMENT, FIELD_ROW, FIELD),
            asList(UPDATE, UPDATE_SET, UPDATE_SET_ASSIGNMENT, FIELD_ROW, FIELD, FIELD_VALUE),
            asList(UPDATE, UPDATE_SET, UPDATE_SET_ASSIGNMENT, FIELD_ROW, FIELD),
            asList(UPDATE, UPDATE_SET, UPDATE_SET_ASSIGNMENT, FIELD_ROW, FIELD, FIELD_REFERENCE),
            asList(UPDATE, UPDATE_FROM),
            asList(UPDATE, UPDATE_WHERE),
            asList(UPDATE, UPDATE_RETURNING)
        ),
        ctx.update(TABLE1)
           .set(row(FIELD_NAME1,  FIELD_DATE1),
                row(val("value"), FIELD_DATE1)));
    }

    @Test
    public void test_UPDATE_SET_WHERE() {
        assertEvents(asList(
            asList(UPDATE),
            asList(UPDATE, UPDATE_UPDATE),
            asList(UPDATE, UPDATE_UPDATE, TABLE),
            asList(UPDATE, UPDATE_UPDATE, TABLE, TABLE_REFERENCE),
            asList(UPDATE, UPDATE_SET),
            asList(UPDATE, UPDATE_SET, UPDATE_SET_ASSIGNMENT),
            asList(UPDATE, UPDATE_SET, UPDATE_SET_ASSIGNMENT, FIELD),
            asList(UPDATE, UPDATE_SET, UPDATE_SET_ASSIGNMENT, FIELD, FIELD_REFERENCE),
            asList(UPDATE, UPDATE_SET, UPDATE_SET_ASSIGNMENT, FIELD),
            asList(UPDATE, UPDATE_SET, UPDATE_SET_ASSIGNMENT, FIELD, FIELD_VALUE),
            asList(UPDATE, UPDATE_FROM),
            asList(UPDATE, UPDATE_WHERE),
            asList(UPDATE, UPDATE_WHERE, CONDITION),
            asList(UPDATE, UPDATE_WHERE, CONDITION, CONDITION_COMPARISON),
            asList(UPDATE, UPDATE_WHERE, CONDITION, CONDITION_COMPARISON, FIELD),
            asList(UPDATE, UPDATE_WHERE, CONDITION, CONDITION_COMPARISON, FIELD, FIELD_REFERENCE),
            asList(UPDATE, UPDATE_WHERE, CONDITION, CONDITION_COMPARISON, FIELD),
            asList(UPDATE, UPDATE_WHERE, CONDITION, CONDITION_COMPARISON, FIELD, FIELD_VALUE),
            asList(UPDATE, UPDATE_RETURNING)
        ),
        ctx.update(TABLE1)
           .set(FIELD_NAME1, "value")
           .where(FIELD_ID1.eq(1)));
    }

    @Test
    public void test_UPDATE_SET_RETURNING() {

        // Postgres is the only DB to support the RETURNING clause
        ctx.configuration().set(POSTGRES);
        assertEvents(asList(
            asList(UPDATE),
            asList(UPDATE, UPDATE_UPDATE),
            asList(UPDATE, UPDATE_UPDATE, TABLE),
            asList(UPDATE, UPDATE_UPDATE, TABLE, TABLE_REFERENCE),
            asList(UPDATE, UPDATE_SET),
            asList(UPDATE, UPDATE_SET, UPDATE_SET_ASSIGNMENT),
            asList(UPDATE, UPDATE_SET, UPDATE_SET_ASSIGNMENT, FIELD),
            asList(UPDATE, UPDATE_SET, UPDATE_SET_ASSIGNMENT, FIELD, FIELD_REFERENCE),
            asList(UPDATE, UPDATE_SET, UPDATE_SET_ASSIGNMENT, FIELD),
            asList(UPDATE, UPDATE_SET, UPDATE_SET_ASSIGNMENT, FIELD, FIELD_VALUE),
            asList(UPDATE, UPDATE_FROM),
            asList(UPDATE, UPDATE_WHERE),
            asList(UPDATE, UPDATE_RETURNING),
            asList(UPDATE, UPDATE_RETURNING, FIELD),
            asList(UPDATE, UPDATE_RETURNING, FIELD, FIELD_REFERENCE),
            asList(UPDATE, UPDATE_RETURNING, FIELD),
            asList(UPDATE, UPDATE_RETURNING, FIELD, FIELD_REFERENCE)
        ),
        ctx.update(TABLE1)
           .set(FIELD_NAME1, "value")
           .returning(FIELD_ID1, FIELD_NAME1));
    }

    @Test
    public void test_CONDITION_simple() {
        assertEvents(asList(
            asList(CONDITION),
            asList(CONDITION, CONDITION_COMPARISON),
            asList(CONDITION, CONDITION_COMPARISON, FIELD),
            asList(CONDITION, CONDITION_COMPARISON, FIELD, FIELD_REFERENCE),
            asList(CONDITION, CONDITION_COMPARISON, FIELD),
            asList(CONDITION, CONDITION_COMPARISON, FIELD, FIELD_VALUE)
        ),
        FIELD_ID1.eq(1));
    }

    @Test
    public void test_CONDITION_NOT() {
        assertEvents(asList(
            asList(CONDITION),
            asList(CONDITION, CONDITION_NOT),
            asList(CONDITION, CONDITION_NOT, CONDITION),
            asList(CONDITION, CONDITION_NOT, CONDITION, CONDITION_COMPARISON),
            asList(CONDITION, CONDITION_NOT, CONDITION, CONDITION_COMPARISON, FIELD),
            asList(CONDITION, CONDITION_NOT, CONDITION, CONDITION_COMPARISON, FIELD, FIELD_REFERENCE),
            asList(CONDITION, CONDITION_NOT, CONDITION, CONDITION_COMPARISON, FIELD),
            asList(CONDITION, CONDITION_NOT, CONDITION, CONDITION_COMPARISON, FIELD, FIELD_VALUE)
        ),
        FIELD_ID1.eq(1).not());
    }

    @Test
    public void test_CONDITION_AND() {
        assertEvents(asList(
            asList(CONDITION),
            asList(CONDITION, CONDITION_AND),
            asList(CONDITION, CONDITION_AND, CONDITION),
            asList(CONDITION, CONDITION_AND, CONDITION, CONDITION_COMPARISON),
            asList(CONDITION, CONDITION_AND, CONDITION, CONDITION_COMPARISON, FIELD),
            asList(CONDITION, CONDITION_AND, CONDITION, CONDITION_COMPARISON, FIELD, FIELD_REFERENCE),
            asList(CONDITION, CONDITION_AND, CONDITION, CONDITION_COMPARISON, FIELD),
            asList(CONDITION, CONDITION_AND, CONDITION, CONDITION_COMPARISON, FIELD, FIELD_VALUE),
            asList(CONDITION, CONDITION_AND, CONDITION),
            asList(CONDITION, CONDITION_AND, CONDITION, CONDITION_IS_NOT_NULL),
            asList(CONDITION, CONDITION_AND, CONDITION, CONDITION_IS_NOT_NULL, FIELD),
            asList(CONDITION, CONDITION_AND, CONDITION, CONDITION_IS_NOT_NULL, FIELD, FIELD_REFERENCE)
        ),
        FIELD_ID1.eq(1).and(FIELD_NAME1.isNotNull()));
    }

    @Test
    public void test_CONDITION_OR() {
        assertEvents(asList(
            asList(CONDITION),
            asList(CONDITION, CONDITION_OR),
            asList(CONDITION, CONDITION_OR, CONDITION),
            asList(CONDITION, CONDITION_OR, CONDITION, CONDITION_COMPARISON),
            asList(CONDITION, CONDITION_OR, CONDITION, CONDITION_COMPARISON, FIELD),
            asList(CONDITION, CONDITION_OR, CONDITION, CONDITION_COMPARISON, FIELD, FIELD_REFERENCE),
            asList(CONDITION, CONDITION_OR, CONDITION, CONDITION_COMPARISON, FIELD),
            asList(CONDITION, CONDITION_OR, CONDITION, CONDITION_COMPARISON, FIELD, FIELD_VALUE),
            asList(CONDITION, CONDITION_OR, CONDITION),
            asList(CONDITION, CONDITION_OR, CONDITION, CONDITION_IS_NULL),
            asList(CONDITION, CONDITION_OR, CONDITION, CONDITION_IS_NULL, FIELD),
            asList(CONDITION, CONDITION_OR, CONDITION, CONDITION_IS_NULL, FIELD, FIELD_REFERENCE)
        ),
        FIELD_ID1.eq(1).or(FIELD_NAME1.isNull()));
    }

    @Test
    public void test_CONDITION_NULL() {
        assertEvents(asList(
            asList(CONDITION),
            asList(CONDITION, CONDITION_IS_NULL),
            asList(CONDITION, CONDITION_IS_NULL, FIELD),
            asList(CONDITION, CONDITION_IS_NULL, FIELD, FIELD_REFERENCE)
        ),
        FIELD_ID1.isNull());
    }

    @Test
    public void test_CONDITION_NOT_NULL() {
        assertEvents(asList(
            asList(CONDITION),
            asList(CONDITION, CONDITION_IS_NOT_NULL),
            asList(CONDITION, CONDITION_IS_NOT_NULL, FIELD),
            asList(CONDITION, CONDITION_IS_NOT_NULL, FIELD, FIELD_REFERENCE)
        ),
        FIELD_ID1.isNotNull());
    }

    @Test
    public void test_CONDITION_IN() {
        assertEvents(asList(
            asList(CONDITION),
            asList(CONDITION, CONDITION_IN),
            asList(CONDITION, CONDITION_IN, FIELD),
            asList(CONDITION, CONDITION_IN, FIELD, FIELD_REFERENCE),
            asList(CONDITION, CONDITION_IN, FIELD),
            asList(CONDITION, CONDITION_IN, FIELD, FIELD_VALUE),
            asList(CONDITION, CONDITION_IN, FIELD),
            asList(CONDITION, CONDITION_IN, FIELD, FIELD_VALUE)
        ),
        FIELD_ID1.in(1, 2));
    }

    @Test
    public void test_CONDITION_NOT_IN() {
        assertEvents(asList(
            asList(CONDITION),
            asList(CONDITION, CONDITION_NOT_IN),
            asList(CONDITION, CONDITION_NOT_IN, FIELD),
            asList(CONDITION, CONDITION_NOT_IN, FIELD, FIELD_REFERENCE),
            asList(CONDITION, CONDITION_NOT_IN, FIELD),
            asList(CONDITION, CONDITION_NOT_IN, FIELD, FIELD_VALUE),
            asList(CONDITION, CONDITION_NOT_IN, FIELD),
            asList(CONDITION, CONDITION_NOT_IN, FIELD, FIELD_VALUE)
        ),
        FIELD_ID1.notIn(1, 2));
    }

    @Test
    public void test_CONDITION_EXISTS() {

        // Omit "dual" with Postgres
        ctx.configuration().set(POSTGRES);
        assertEvents(asList(
            asList(CONDITION),
            asList(CONDITION, CONDITION_EXISTS),
            asList(CONDITION, CONDITION_EXISTS, SELECT),
            asList(CONDITION, CONDITION_EXISTS, SELECT, SELECT_SELECT),
            asList(CONDITION, CONDITION_EXISTS, SELECT, SELECT_SELECT, FIELD),
            asList(CONDITION, CONDITION_EXISTS, SELECT, SELECT_SELECT, FIELD, FIELD_VALUE),
            asList(CONDITION, CONDITION_EXISTS, SELECT, SELECT_FROM),
            asList(CONDITION, CONDITION_EXISTS, SELECT, SELECT_WHERE),
            asList(CONDITION, CONDITION_EXISTS, SELECT, SELECT_START_WITH),
            asList(CONDITION, CONDITION_EXISTS, SELECT, SELECT_CONNECT_BY),
            asList(CONDITION, CONDITION_EXISTS, SELECT, SELECT_GROUP_BY),
            asList(CONDITION, CONDITION_EXISTS, SELECT, SELECT_HAVING),
            asList(CONDITION, CONDITION_EXISTS, SELECT, SELECT_WINDOW),
            asList(CONDITION, CONDITION_EXISTS, SELECT, SELECT_ORDER_BY)
        ),
        exists(selectOne()));
    }

    @Test
    public void test_CONDITION_NOT_EXISTS() {

        // Omit "dual" with Postgres
        ctx.configuration().set(POSTGRES);
        assertEvents(asList(
            asList(CONDITION),
            asList(CONDITION, CONDITION_NOT_EXISTS),
            asList(CONDITION, CONDITION_NOT_EXISTS, SELECT),
            asList(CONDITION, CONDITION_NOT_EXISTS, SELECT, SELECT_SELECT),
            asList(CONDITION, CONDITION_NOT_EXISTS, SELECT, SELECT_SELECT, FIELD),
            asList(CONDITION, CONDITION_NOT_EXISTS, SELECT, SELECT_SELECT, FIELD, FIELD_VALUE),
            asList(CONDITION, CONDITION_NOT_EXISTS, SELECT, SELECT_FROM),
            asList(CONDITION, CONDITION_NOT_EXISTS, SELECT, SELECT_WHERE),
            asList(CONDITION, CONDITION_NOT_EXISTS, SELECT, SELECT_START_WITH),
            asList(CONDITION, CONDITION_NOT_EXISTS, SELECT, SELECT_CONNECT_BY),
            asList(CONDITION, CONDITION_NOT_EXISTS, SELECT, SELECT_GROUP_BY),
            asList(CONDITION, CONDITION_NOT_EXISTS, SELECT, SELECT_HAVING),
            asList(CONDITION, CONDITION_NOT_EXISTS, SELECT, SELECT_WINDOW),
            asList(CONDITION, CONDITION_NOT_EXISTS, SELECT, SELECT_ORDER_BY)
        ),
        notExists(selectOne()));
    }

    @Test
    public void test_CONDITION_BETWEEN() {
        assertEvents(asList(
            asList(CONDITION),
            asList(CONDITION, CONDITION_BETWEEN),
            asList(CONDITION, CONDITION_BETWEEN, FIELD),
            asList(CONDITION, CONDITION_BETWEEN, FIELD, FIELD_REFERENCE),
            asList(CONDITION, CONDITION_BETWEEN, FIELD),
            asList(CONDITION, CONDITION_BETWEEN, FIELD, FIELD_VALUE),
            asList(CONDITION, CONDITION_BETWEEN, FIELD),
            asList(CONDITION, CONDITION_BETWEEN, FIELD, FIELD_VALUE)
        ),
        FIELD_ID1.between(1).and(2));
    }

    @Test
    public void test_CONDITION_NOT_BETWEEN() {
        assertEvents(asList(
            asList(CONDITION),
            asList(CONDITION, CONDITION_NOT_BETWEEN),
            asList(CONDITION, CONDITION_NOT_BETWEEN, FIELD),
            asList(CONDITION, CONDITION_NOT_BETWEEN, FIELD, FIELD_REFERENCE),
            asList(CONDITION, CONDITION_NOT_BETWEEN, FIELD),
            asList(CONDITION, CONDITION_NOT_BETWEEN, FIELD, FIELD_VALUE),
            asList(CONDITION, CONDITION_NOT_BETWEEN, FIELD),
            asList(CONDITION, CONDITION_NOT_BETWEEN, FIELD, FIELD_VALUE)
        ),
        FIELD_ID1.notBetween(1, 2));
    }

    @Test
    public void test_CONDITION_BETWEEN_SYMMETRIC() {

        // Use Postgres for its native SYMMETRIC support
        ctx.configuration().set(POSTGRES);
        assertEvents(asList(
            asList(CONDITION),
            asList(CONDITION, CONDITION_BETWEEN_SYMMETRIC),
            asList(CONDITION, CONDITION_BETWEEN_SYMMETRIC, FIELD),
            asList(CONDITION, CONDITION_BETWEEN_SYMMETRIC, FIELD, FIELD_REFERENCE),
            asList(CONDITION, CONDITION_BETWEEN_SYMMETRIC, FIELD),
            asList(CONDITION, CONDITION_BETWEEN_SYMMETRIC, FIELD, FIELD_VALUE),
            asList(CONDITION, CONDITION_BETWEEN_SYMMETRIC, FIELD),
            asList(CONDITION, CONDITION_BETWEEN_SYMMETRIC, FIELD, FIELD_VALUE)
        ),
        FIELD_ID1.betweenSymmetric(1).and(2));
    }

    @Test
    public void test_CONDITION_NOT_BETWEEN_SYMMETRIC() {

        // Use Postgres for its native SYMMETRIC support
        ctx.configuration().set(POSTGRES);
        assertEvents(asList(
            asList(CONDITION),
            asList(CONDITION, CONDITION_NOT_BETWEEN_SYMMETRIC),
            asList(CONDITION, CONDITION_NOT_BETWEEN_SYMMETRIC, FIELD),
            asList(CONDITION, CONDITION_NOT_BETWEEN_SYMMETRIC, FIELD, FIELD_REFERENCE),
            asList(CONDITION, CONDITION_NOT_BETWEEN_SYMMETRIC, FIELD),
            asList(CONDITION, CONDITION_NOT_BETWEEN_SYMMETRIC, FIELD, FIELD_VALUE),
            asList(CONDITION, CONDITION_NOT_BETWEEN_SYMMETRIC, FIELD),
            asList(CONDITION, CONDITION_NOT_BETWEEN_SYMMETRIC, FIELD, FIELD_VALUE)
        ),
        FIELD_ID1.notBetweenSymmetric(1, 2));
    }

    @Test
    public void test_CONDITION_NOT_BETWEEN_SYMMETRIC_rowValueExpressions() {

        // Use Postgres for its native SYMMETRIC support
        ctx.configuration().set(POSTGRES);
        assertEvents(asList(
            asList(CONDITION),
            asList(CONDITION, CONDITION_NOT_BETWEEN_SYMMETRIC),
            asList(CONDITION, CONDITION_NOT_BETWEEN_SYMMETRIC, FIELD_ROW),
            asList(CONDITION, CONDITION_NOT_BETWEEN_SYMMETRIC, FIELD_ROW, FIELD),
            asList(CONDITION, CONDITION_NOT_BETWEEN_SYMMETRIC, FIELD_ROW, FIELD, FIELD_REFERENCE),
            asList(CONDITION, CONDITION_NOT_BETWEEN_SYMMETRIC, FIELD_ROW, FIELD),
            asList(CONDITION, CONDITION_NOT_BETWEEN_SYMMETRIC, FIELD_ROW, FIELD, FIELD_REFERENCE),
            asList(CONDITION, CONDITION_NOT_BETWEEN_SYMMETRIC, FIELD_ROW),
            asList(CONDITION, CONDITION_NOT_BETWEEN_SYMMETRIC, FIELD_ROW, FIELD),
            asList(CONDITION, CONDITION_NOT_BETWEEN_SYMMETRIC, FIELD_ROW, FIELD, FIELD_VALUE),
            asList(CONDITION, CONDITION_NOT_BETWEEN_SYMMETRIC, FIELD_ROW, FIELD),
            asList(CONDITION, CONDITION_NOT_BETWEEN_SYMMETRIC, FIELD_ROW, FIELD, FIELD_VALUE),
            asList(CONDITION, CONDITION_NOT_BETWEEN_SYMMETRIC, FIELD_ROW),
            asList(CONDITION, CONDITION_NOT_BETWEEN_SYMMETRIC, FIELD_ROW, FIELD),
            asList(CONDITION, CONDITION_NOT_BETWEEN_SYMMETRIC, FIELD_ROW, FIELD, FIELD_VALUE),
            asList(CONDITION, CONDITION_NOT_BETWEEN_SYMMETRIC, FIELD_ROW, FIELD),
            asList(CONDITION, CONDITION_NOT_BETWEEN_SYMMETRIC, FIELD_ROW, FIELD, FIELD_VALUE)
        ),
        row(FIELD_ID1, FIELD_NAME1).notBetweenSymmetric(1, "a").and(2, "b"));
    }

    private RenderContext r_ctx() {
        return ctx.renderContext();
    }

    private BindContext b_ctx() {
        MockDataProvider p = new MockDataProvider() {
            @Override
            public MockResult[] execute(MockExecuteContext c) throws SQLException {
                return new MockResult[0];
            }
        };

        return ctx.bindContext(new MockStatement(new MockConnection(p), p));
    }

    private void assertEvents(List<List<Clause>> expected, QueryPart part) {
        assertEvents(expected, part, r_ctx(), b_ctx());
    }

    private void assertEvents(List<List<Clause>> expected, QueryPart part, RenderContext r_ctx, BindContext b_ctx) {
        listener.clauses.clear();
        r_ctx.visit(part);
        assertEvents0(expected);

        // TODO: Re-enable this once bind variable visiting is implemented
        if (false) {
            listener.clauses.clear();
            b_ctx.visit(part);
            assertEvents0(expected);
        }
    }

    private void assertEvents0(List<List<Clause>> expected) {

        // This assertion is a bit more verbose to be able to detect errors more easily
        for (int i = 0; i < expected.size() && i < listener.clauses.size(); i++) {
            assertEquals("Mismatch at position " + i + ":", expected.get(i), listener.clauses.get(i));
        }

        if (expected.size() != listener.clauses.size()) {
            fail("Size mismatch:\n\tExpected: " + expected + "\n\tActual: " + listener.clauses);
        }
    }


    private static class SimpleListener implements VisitListener {

        List<List<Clause>> clauses = new ArrayList<List<Clause>>();

        @Override
        public void clauseStart(VisitContext context) {
            clauses.add(asList(context.clauses()));
        }

        @Override
        public void clauseEnd(VisitContext context) {}

        @Override
        public void visitStart(VisitContext context) {}

        @Override
        public void visitEnd(VisitContext context) {}

    }
}
