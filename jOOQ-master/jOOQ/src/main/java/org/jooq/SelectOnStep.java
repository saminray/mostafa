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

import java.util.Collection;

import org.jooq.api.annotation.State;
import org.jooq.api.annotation.Transition;
import org.jooq.exception.DataAccessException;
import org.jooq.impl.DSL;

/**
 * This type is used for the {@link Select}'s DSL API when selecting generic
 * {@link Record} types.
 * <p>
 * Example: <code><pre>
 * -- get all authors' first and last names, and the number
 * -- of books they've written in German, if they have written
 * -- more than five books in German in the last three years
 * -- (from 2011), and sort those authors by last names
 * -- limiting results to the second and third row
 *
 *   SELECT T_AUTHOR.FIRST_NAME, T_AUTHOR.LAST_NAME, COUNT(*)
 *     FROM T_AUTHOR
 *     JOIN T_BOOK ON T_AUTHOR.ID = T_BOOK.AUTHOR_ID
 *    WHERE T_BOOK.LANGUAGE = 'DE'
 *      AND T_BOOK.PUBLISHED > '2008-01-01'
 * GROUP BY T_AUTHOR.FIRST_NAME, T_AUTHOR.LAST_NAME
 *   HAVING COUNT(*) > 5
 * ORDER BY T_AUTHOR.LAST_NAME ASC NULLS FIRST
 *    LIMIT 2
 *   OFFSET 1
 *      FOR UPDATE
 *       OF FIRST_NAME, LAST_NAME
 *       NO WAIT
 * </pre></code> Its equivalent in jOOQ <code><pre>
 * create.select(TAuthor.FIRST_NAME, TAuthor.LAST_NAME, create.count())
 *       .from(T_AUTHOR)
 *       .join(T_BOOK).on(TBook.AUTHOR_ID.equal(TAuthor.ID))
 *       .where(TBook.LANGUAGE.equal("DE"))
 *       .and(TBook.PUBLISHED.greaterThan(parseDate('2008-01-01')))
 *       .groupBy(TAuthor.FIRST_NAME, TAuthor.LAST_NAME)
 *       .having(create.count().greaterThan(5))
 *       .orderBy(TAuthor.LAST_NAME.asc().nullsFirst())
 *       .limit(2)
 *       .offset(1)
 *       .forUpdate()
 *       .of(TAuthor.FIRST_NAME, TAuthor.LAST_NAME)
 *       .noWait();
 * </pre></code> Refer to the manual for more details
 *
 * @author Lukas Eder
 */
@State
public interface SelectOnStep<R extends Record> {

    /**
     * Add an <code>ON</code> clause to the previous <code>JOIN</code>.
     */
    @Support
    @Transition(
        name = "ON",
        args = "Condition+"
    )
    SelectOnConditionStep<R> on(Condition... conditions);

    /**
     * Add an <code>ON</code> clause to the previous <code>JOIN</code>.
     */
    @Support
    @Transition(
        name = "ON",
        args = "Condition"
    )
    SelectOnConditionStep<R> on(Field<Boolean> condition);

    /**
     * Add an <code>ON</code> clause to the previous <code>JOIN</code>.
     * <p>
     * <b>NOTE</b>: When inserting plain SQL into jOOQ objects, you must
     * guarantee syntax integrity. You may also create the possibility of
     * malicious SQL injection. Be sure to properly use bind variables and/or
     * escape literals when concatenated into SQL clauses!
     *
     * @see DSL#condition(String)
     */
    @Support
    SelectOnConditionStep<R> on(String sql);

    /**
     * Add an <code>ON</code> clause to the previous <code>JOIN</code>.
     * <p>
     * <b>NOTE</b>: When inserting plain SQL into jOOQ objects, you must
     * guarantee syntax integrity. You may also create the possibility of
     * malicious SQL injection. Be sure to properly use bind variables and/or
     * escape literals when concatenated into SQL clauses!
     *
     * @see DSL#condition(String, Object...)
     */
    @Support
    SelectOnConditionStep<R> on(String sql, Object... bindings);

    /**
     * Add an <code>ON</code> clause to the previous <code>JOIN</code>.
     * <p>
     * <b>NOTE</b>: When inserting plain SQL into jOOQ objects, you must
     * guarantee syntax integrity. You may also create the possibility of
     * malicious SQL injection. Be sure to properly use bind variables and/or
     * escape literals when concatenated into SQL clauses!
     *
     * @see DSL#condition(String, QueryPart...)
     */
    @Support
    SelectOnConditionStep<R> on(String sql, QueryPart... parts);

    /**
     * Join the previous table on a non-ambiguous foreign key relationship
     * between the two joined tables.
     * <p>
     * See {@link TableOnStep#onKey(ForeignKey)} for examples.
     *
     * @see TableOnStep#onKey(ForeignKey)
     * @throws DataAccessException If there is no non-ambiguous key definition
     *             known to jOOQ
     */
    @Support
    SelectJoinStep<R> onKey() throws DataAccessException;

    /**
     * Join the previous table on a non-ambiguous foreign key relationship
     * between the two joined tables.
     * <p>
     * See {@link TableOnStep#onKey(ForeignKey)} for examples.
     *
     * @see TableOnStep#onKey(ForeignKey)
     * @throws DataAccessException If there is no non-ambiguous key definition
     *             known to jOOQ
     */
    @Support
    SelectJoinStep<R> onKey(TableField<?, ?>... keyFields) throws DataAccessException;

    /**
     * Join the table on a non-ambiguous foreign key relationship between the
     * two joined tables.
     * <p>
     * See {@link TableOnStep#onKey(ForeignKey)} for examples.
     *
     * @see TableOnStep#onKey(ForeignKey)
     */
    @Support
    SelectJoinStep<R> onKey(ForeignKey<?, ?> key);

    /**
     * Join the previous table with the <code>USING(column [, column...])</code>
     * syntax.
     * <p>
     * If this is not supported by your RDBMS, then jOOQ will try to simulate
     * this behaviour using the information provided in this query.
     */
    @Support
    @Transition(
        name = "USING",
        args = "Field+"
    )
    SelectJoinStep<R> using(Field<?>... fields);

    /**
     * Join the previous table with the <code>USING(column [, column...])</code>
     * syntax.
     * <p>
     * If this is not supported by your RDBMS, then jOOQ will try to simulate
     * this behaviour using the information provided in this query.
     */
    @Support
    SelectJoinStep<R> using(Collection<? extends Field<?>> fields);
}
