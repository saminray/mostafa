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
package org.jooq;

import static org.jooq.SQLDialect.CUBRID;
// ...

import org.jooq.api.annotation.State;
import org.jooq.api.annotation.Transition;

/**
 * This type is used for the {@link Merge}'s DSL API.
 * <p>
 * Example: <code><pre>
 * DSLContext create = DSL.using(configuration);
 *
 * create.mergeInto(table)
 *       .using(select)
 *       .on(condition)
 *       .whenMatchedThenUpdate()
 *       .set(field1, value1)
 *       .set(field2, value2)
 *       .whenNotMatchedThenInsert(field1, field2)
 *       .values(value1, value2)
 *       .execute();
 * </pre></code>
 *
 * @author Lukas Eder
 */
@State
public interface MergeMatchedDeleteStep<R extends Record> extends MergeNotMatchedStep<R> {

    /**
     * Add an additional <code>DELETE WHERE</code> clause to the preceding
     * <code>WHEN MATCHED THEN UPDATE</code> clause.
     * <p>
     * <b>Note:</b> This syntax is only available for the
     * {@link SQLDialect#CUBRID} and {@link SQLDialect#ORACLE} databases!
     * <p>
     * See <a href=
     * "http://docs.oracle.com/cd/B19306_01/server.102/b14200/statements_9016.htm"
     * >http://docs.oracle.com/cd/B19306_01/server.102/b14200/statements_9016.
     * htm</a> for a full definition of the Oracle <code>MERGE</code> statement
     */
    @Support({ CUBRID })
    @Transition(
        name = "DELETE WHERE",
        args = "Condition"
    )
    MergeNotMatchedStep<R> deleteWhere(Condition condition);

    /**
     * Add an additional <code>DELETE WHERE</code> clause to the preceding
     * <code>WHEN MATCHED THEN UPDATE</code> clause.
     * <p>
     * <b>Note:</b> This syntax is only available for the
     * {@link SQLDialect#CUBRID} and {@link SQLDialect#ORACLE} databases!
     * <p>
     * See <a href=
     * "http://docs.oracle.com/cd/B19306_01/server.102/b14200/statements_9016.htm"
     * >http://docs.oracle.com/cd/B19306_01/server.102/b14200/statements_9016.
     * htm</a> for a full definition of the Oracle <code>MERGE</code> statement
     */
    @Support({ CUBRID })
    @Transition(
        name = "DELETE WHERE",
        args = "Condition"
    )
    MergeNotMatchedStep<R> deleteWhere(Field<Boolean> condition);
}
