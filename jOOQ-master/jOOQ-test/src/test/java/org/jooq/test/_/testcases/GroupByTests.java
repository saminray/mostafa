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
import static org.jooq.SQLDialect.MARIADB;
import static org.jooq.SQLDialect.MYSQL;
// ...
import static org.jooq.impl.DSL.count;
// ...
// ...
// ...
// ...
import static org.jooq.impl.DSL.one;
import static org.jooq.impl.DSL.rollup;
import static org.jooq.impl.DSL.selectOne;
import static org.junit.Assert.assertEquals;

import java.sql.Date;
import java.util.Arrays;

import org.jooq.Field;
import org.jooq.Record1;
import org.jooq.Record2;
import org.jooq.Record3;
import org.jooq.Record4;
import org.jooq.Record6;
import org.jooq.Result;
import org.jooq.SelectOrderByStep;
import org.jooq.TableRecord;
import org.jooq.UpdatableRecord;
import org.jooq.exception.DataAccessException;
import org.jooq.test.BaseTest;
import org.jooq.test.jOOQAbstractTest;

import org.junit.Test;

public class GroupByTests<
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

    public GroupByTests(jOOQAbstractTest<A, AP, B, S, B2S, BS, L, X, DATE, BOOL, D, T, U, UU, I, IPK, T725, T639, T785, CASE> delegate) {
        super(delegate);
    }

    public void testEmptyGrouping() throws Exception {

        // [#1665] Test the empty GROUP BY clause
        assertEquals(1, (int) create().selectOne()
            .from(TBook())
            .groupBy()
            .fetchOne(0, Integer.class));

        // [#1665] Test the empty GROUP BY clause
        assertEquals(1, (int) create().selectOne()
            .from(TBook())
            .groupBy()
            .having("1 = 1")
            .fetchOne(0, Integer.class));
    }

    public void testGrouping() throws Exception {

        // Test a simple group by query
        Field<Integer> count = count().as("c");
        Result<Record2<Integer, Integer>> result = create()
            .select(TBook_AUTHOR_ID(), count)
            .from(TBook())
            .groupBy(TBook_AUTHOR_ID()).fetch();

        assertEquals(2, result.size());
        assertEquals(2, (int) result.get(0).getValue(count));
        assertEquals(2, (int) result.get(1).getValue(count));

        // Test a group by query with a single HAVING clause
        Result<Record2<String, Integer>> result2 = create()
            .select(TAuthor_LAST_NAME(), count)
            .from(TBook())
            .join(TAuthor()).on(TBook_AUTHOR_ID().equal(TAuthor_ID()))
            .where(TBook_TITLE().notEqual("1984"))
            .groupBy(TAuthor_LAST_NAME())
            .having(count().equal(2))
            .fetch();

        assertEquals(1, result2.size());
        assertEquals(2, (int) result2.getValue(0, count));
        assertEquals("Coelho", result2.getValue(0, TAuthor_LAST_NAME()));

        // Test a group by query with a combined HAVING clause
        Result<Record2<String, Integer>> result3 = create()
            .select(TAuthor_LAST_NAME(), count)
            .from(TBook())
            .join(TAuthor()).on(TBook_AUTHOR_ID().equal(TAuthor_ID()))
            .where(TBook_TITLE().notEqual("1984"))
            .groupBy(TAuthor_LAST_NAME())
            .having(count().equal(2))
            .or(count().greaterOrEqual(2))
            .andExists(selectOne())
            .fetch();

        assertEquals(1, result3.size());
        assertEquals(2, (int) result3.getValue(0, count));
        assertEquals("Coelho", result3.getValue(0, TAuthor_LAST_NAME()));

        // Test a group by query with a plain SQL having clause
        Result<Record2<String, Integer>> result4 = create()
            .select(VLibrary_AUTHOR(), count)
            .from(VLibrary())
            .where(VLibrary_TITLE().notEqual("1984"))
            .groupBy(VLibrary_AUTHOR())

            // MySQL seems to have a bug with fully qualified view names in the
            // having clause. TODO: [#277] Fully analyse this issue
            .having("count(*) >= ?", 2)
            .fetch();

        assertEquals(1, result4.size());
        assertEquals(2, (int) result4.getValue(0, count));

        // SQLite loses type information when views select functions.
        // In this case: concatenation. So as a workaround, SQLlite only selects
        // FIRST_NAME in the view
        assertEquals("Paulo", result4.getValue(0, VLibrary_AUTHOR()).substring(0, 5));
    }

    public void testGroupByCubeRollup() throws Exception {
        switch (dialect()) {
            /* [pro] xx
            xxxx xxxxxxx
            xxxx xxxx
            xxxx xxxxxxx
            xx [/pro] */
            case DERBY:
            case FIREBIRD:
            case H2:
            case HSQLDB:
            case POSTGRES:
            case SQLITE:
                log.info("SKIPPING", "Group by CUBE / ROLLUP tests");
                return;
        }

        // Simple ROLLUP clause
        // --------------------
        SelectOrderByStep<Record2<Integer, Integer>> step = create()
                .select(
                    TBook_ID(),
                    TBook_AUTHOR_ID())
                .from(TBook())
                .groupBy(rollup(
                    TBook_ID(),
                    TBook_AUTHOR_ID()));

        // MySQL doesn't really support ORDER BY clauses with ROLLUP
        if (!asList(MARIADB, MYSQL).contains(dialect())) {
            step.orderBy(
                TBook_ID().asc().nullsLast(),
                TBook_AUTHOR_ID().asc().nullsLast());
        }

        Result<Record2<Integer, Integer>>  result = step.fetch();

        assertEquals(Arrays.asList(1, 1, 2, 2, 3, 3, 4, 4, null), result.getValues(0));
        assertEquals(Arrays.asList(1, null, 1, null, 2, null, 2, null, null), result.getValues(1));

        if (asList(MARIADB, MYSQL).contains(dialect())) {
            log.info("SKIPPING", "CUBE and GROUPING SETS tests");
            return;
        }

        /* [pro] xx
        xx xxxxxx xxxxxx
        xx xxxxxxxxxxxxx
        xxxxxxxxxxxxxx xxxxxxxxxx x xxxxxxxxxxxxxxxxxxxxxx xxxxxxxxxxxxxxxxxxx
        xx xxxxxxxxxxxx xxxxxxxxxxxxxxxxxxxxxxxxxxxx
            xxxxxxxxxx x xxxxxx

        xxxxxxxxxxxxxxxxxxxxxxx xxxxxxxx xxxxxxxx xxxxxxxxx xxxxxxx x xxxxxxxx
                xxxxxxxx
                    xxxxxxxxxxx
                    xxxxxxxxxxxxxxxxxx
                    xxxxxxxxxxxxxxxxxxxxx
                    xxxxxxxxxxx
                xxxxxxxxxxxxxx
                xxxxxxxxxxxxxxxx
                    xxxxxxxxxxx
                    xxxxxxxxxxxxxxxxxxx
                xxxxxxxxx
                    xxxxxxxxxxxxxxxxxxxxxxxxxxxxxx
                    xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx

        xxxxxxxxxxxxxxx xxxxxxxxxxxxxxxx
        xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx xx xx xx xx xx xx xx xxx xxxxxxxxxxxxxxxxxxxxxx
        xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx xxxxx xx xxxxx xx xxxxx xx xxxxx xxx xxxxxxxxxxxxxxxxxxxxxx
        xxxxxxxxxxxxxxxxxxxxxxxxxxxxx xx xx xx xx xx xx xx xxx xxxxxxxxxxxxxxxxxxxxxx

        xx xxxxxxxxxxxxx xxxxxxxxxxxxxxxxxxxxxxxxxxxx
            xxxxxxxxxxxxxxxxxxxxxxxxxxxxx xx xx xx xx xx xx xx xxx xxxxxxxxxxxxxxxxxxxxxx

        xx xxxx xxxxxx
        xx xxxxxxxxxxx
        xxxxxxxxxxxxxxxxxxxxxxx xxxxxxxx xxxxxxxx xxxxxxxxx xxxxxxx x xxxxxxxxxxxxxxxx
                    xxxxxxxxxxx
                    xxxxxxxxxxxxxxxxxx
                    xxxxxxxxxxxxxxxxxxxxx
                    xxxxxxxxxxx
                xxxxxxxxxxxxxx
                xxxxxxxxxxxxxx
                    xxxxxxxxxxx
                    xxxxxxxxxxxxxxxxxxx
                xxxxxxxxx
                    xxxxxxxxxxxxxxxxxxxxxxxxxxxxxx
                    xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx

        xxxxxxxxxxxxxxxx xxxxxxxxxxxxxxxx
        xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx xxxxx xxxxx xx xx xx xx xx xx xx xxx xxxxxxxxxxxxxxxxxxxxxx
        xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx xx xx xxxxx xx xxxxx xx xxxxx xx xxxxx xxx xxxxxxxxxxxxxxxxxxxxxx
        xxxxxxxxxxxxxxxxxxxxxxxxxxxxx xx xx xx xx xx xx xx xx xx xxx xxxxxxxxxxxxxxxxxxxxxx

        xx xxxxxxxxxxxxx xxxxxxxxxxxxxxxxxxxxxxxxxxxx
            xxxxxxxxxxxxxxxxxxxxxxxxxxxxx xx xx xx xx xx xx xx xx xx xxx xxxxxxxxxxxxxxxxxxxxxx

        xx xxxxxxxx xxxx xxxxxx
        xx xxxxxxxxxxxxxxxxxxxx
        xxxxxxxxxxxxxxxxxxxxxxx xxxxxxxx xxxxxxxx xxxxxxxxx xxxxxxx x xxxxxxxxxxxxxxxx
                    xxxxxxxxxxx
                    xxxxxxxxxxxxxxxxxx
                    xxxxxxxxxxxxxxxxxxxxx
                    xxxxxxxxxxx
                xxxxxxxxxxxxxx
                xxxxxxxxxxxxxxxxxxxxxx
                    xxx xxxxxxxxxx x xxxxxxxxxxxxxxxxxx xxxxxxxxxx xx
                    xxx xxxxxxxxxx x xxxxxxxxxxxxxxxxxx xxxxxxxxxxxxxxxxxxx xx
                    xxx xxxxxxxxxxxx
                    xxx xxxxxxxxxxxxx
                xxxxxxxxx
                    xxxxxxxxxxxxxxxxxxxxxxxxxxxxxx
                    xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx

        xxxxxxxxxxxxxxx xxxxxxxxxxxxxxxx
        xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx xxxxx xxxxx xxxxx xxxxx xx xx xx xxx xxxxxxxxxxxxxxxxxxxxxx
        xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx xxxxx xx xx xx xx xx xx xxx xxxxxxxxxxxxxxxxxxxxxx
        xxxxxxxxxxxxxxxxxxxxxxxxxxxxx xx xx xx xx xx xx xx xxx xxxxxxxxxxxxxxxxxxxxxx

        xx xxxxxxxxxxxxx xxxxxxxxxxxxxxxxxxxxxxxxxxxx
            xxxxxxxxxxxxxxxxxxxxxxxxxxxxx xx xx xx xx xx xx xx xxx xxxxxxxxxxxxxxxxxxxxxx
        xx [/pro] */
    }

    public void testHavingWithoutGrouping() throws Exception {
        try {
            assertEquals(Integer.valueOf(1), create()
                .selectOne()
                .from(TBook())
                .where(TBook_AUTHOR_ID().equal(1))
                .having(count().greaterOrEqual(2))
                .fetchOne(0));
            assertEquals(null, create()
                .selectOne()
                .from(TBook())
                .where(TBook_AUTHOR_ID().equal(1))
                .having(count().greaterOrEqual(3))
                .fetchOne(0));
        }
        catch (DataAccessException e) {

            // HAVING without GROUP BY is not supported by some dialects,
            // So this exception is OK
            switch (dialect()) {

                // [#1665] TODO: Add support for the empty GROUP BY () clause
                case SQLITE:
                    log.info("SKIPPING", "HAVING without GROUP BY is not supported: " + e.getMessage());
                    break;

                default:
                    throw e;
            }
        }
    }
}
