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

import static org.jooq.impl.DSL.name;
import static org.jooq.impl.DSL.select;
import static org.jooq.impl.DSL.tableByName;
import static org.jooq.impl.DSL.val;
import static org.junit.Assert.assertEquals;

import java.sql.Date;

import org.jooq.CommonTableExpression;
import org.jooq.Record;
import org.jooq.Record1;
import org.jooq.Record2;
import org.jooq.Record3;
import org.jooq.Record6;
import org.jooq.Result;
import org.jooq.Table;
import org.jooq.TableRecord;
import org.jooq.UpdatableRecord;
import org.jooq.test.BaseTest;
import org.jooq.test.jOOQAbstractTest;

import org.junit.Test;

/**
 * @author Lukas Eder
 */
public class CTETests<
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

    public CTETests(jOOQAbstractTest<A, AP, B, S, B2S, BS, L, X, DATE, BOOL, D, T, U, UU, I, IPK, T725, T639, T785, CASE> delegate) {
        super(delegate);
    }

    public void testCTESimple() throws Exception {
        switch (dialect().family()) {
            case MARIADB:
            case MYSQL:
                log.info("SKIPPING", "Common Table Expression tests");
                return;
        }

        Result<Record> result1 =
        create().with("t", "f1", "f2").as(select(val(1), val("a")))
                .select()
                .from(tableByName("t"))
                .fetch();

        assertEquals(1, result1.size());
        assertEquals(2, result1.fields().length);
        assertEquals("f1", result1.field(0).getName());
        assertEquals("f2", result1.field(1).getName());
        assertEquals(Integer.class, result1.field(0).getType());
        assertEquals(String.class, result1.field(1).getType());
        assertEquals(1, result1.getValue(0, 0));
        assertEquals("a", result1.getValue(0, 1));
    }

    public void testCTEMultiple() throws Exception {
        CommonTableExpression<Record2<Integer, String>> t1 = name("t1").fields("f1", "f2").as(select(val(1), val("a")));
        CommonTableExpression<Record2<Integer, String>> t2 = name("t2").fields("f3", "f4").as(select(val(2), val("b")));

        Result<?> result2 =
        create().with(t1)
                .with(t2)
                .select(
                    t1.field("f1").add(t2.field("f3")).as("add"),
                    t1.field("f2").concat(t2.field("f4")).as("concat"))
                .from(t1, t2)
                .fetch();

        assertEquals(1, result2.size());
        assertEquals(2, result2.fields().length);
        assertEquals("add", result2.field(0).getName());
        assertEquals("concat", result2.field(1).getName());
        assertEquals(Integer.class, result2.field(0).getType());
        assertEquals(String.class, result2.field(1).getType());
        assertEquals(3, result2.getValue(0, 0));
        assertEquals("ab", result2.getValue(0, 1));


        // Try again but this time with varags CTE lists
        assertEquals(result2,
            create().with(t1, t2)
                    .select(
                        t1.field("f1").add(t2.field("f3")).as("add"),
                        t1.field("f2").concat(t2.field("f4")).as("concat"))
                    .from(t1, t2)
                    .fetch()
        );
    }

    public void testCTEAliasing() throws Exception {
        CommonTableExpression<Record2<Integer, String>> t1 = name("t1").fields("f1", "f2").as(select(val(1), val("a")));
        CommonTableExpression<Record2<Integer, String>> t2 = name("t2").fields("f3", "f4").as(select(val(2), val("b")));

        // Try renaming the CTEs when referencing them
        Table<Record2<Integer, String>> a1 = t1.as("a1");
        Table<Record2<Integer, String>> a2 = t2.as("a2");

        Result<?> result3 =
        create().with(t1)
                .with(t2)
                .select(
                    a1.field("f1").add(a2.field("f3")).as("add"),
                    a1.field("f2").concat(a2.field("f4")).as("concat"))
                .from(a1, a2)
                .fetch();

        assertEquals(1, result3.size());
        assertEquals(2, result3.fields().length);
        assertEquals("add", result3.field(0).getName());
        assertEquals("concat", result3.field(1).getName());
        assertEquals(Integer.class, result3.field(0).getType());
        assertEquals(String.class, result3.field(1).getType());
        assertEquals(3, result3.getValue(0, 0));
        assertEquals("ab", result3.getValue(0, 1));

        // Try renaming the CTEs and their columns when referencing them
        Table<Record2<Integer, String>> b1 = t1.as("a1", "i1", "s1");
        Table<Record2<Integer, String>> b2 = t2.as("a2", "i2", "s2");

        Result<?> result4 =
        create().with(t1)
                .with(t2)
                .select(
                    b1.field("i1").add(b2.field("i2")).as("add"),
                    b1.field("s1").concat(b2.field("s2")).as("concat"))
                .from(b1, b2)
                .fetch();

        assertEquals(1, result4.size());
        assertEquals(2, result4.fields().length);
        assertEquals("add", result4.field(0).getName());
        assertEquals("concat", result4.field(1).getName());
        assertEquals(Integer.class, result4.field(0).getType());
        assertEquals(String.class, result4.field(1).getType());
        assertEquals(3, result4.getValue(0, 0));
        assertEquals("ab", result4.getValue(0, 1));

    }

    public void testCTEWithNoExplicitColumnLists() throws Exception {
        Result<Record> result1 =
        create().with("a").as(select(
                                val(1).as("x"),
                                val("a").as("y")
                             ))
                .select()
                .from("a")
                .fetch();

        assertEquals(1, result1.size());
        assertEquals(2, result1.fields().length);
        assertEquals("x", result1.field(0).getName());
        assertEquals("y", result1.field(1).getName());
        assertEquals(Integer.class, result1.field(0).getType());
        assertEquals(String.class, result1.field(1).getType());
        assertEquals(1, result1.getValue(0, 0));
        assertEquals("a", result1.getValue(0, 1));

        // TODO: Test CTE with no explicit column lists
        // TODO: Test recursive CTE
        // TODO: Test CTE with UNIONs (may not work due to #1658)
        // TODO: Test CTE with LIMIT .. OFFSET (may not work due to ROWNUM emulation, etc)
        // TODO: Test CTE with complex subqueries
        // TODO: Run tests on all databases, not just SQL Server
    }
}
