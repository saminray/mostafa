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
import static org.jooq.SQLDialect.DERBY;
import static org.jooq.SQLDialect.FIREBIRD;
import static org.jooq.SQLDialect.H2;
import static org.jooq.SQLDialect.HSQLDB;
// ...
import static org.jooq.SQLDialect.MARIADB;
import static org.jooq.SQLDialect.MYSQL;
// ...
import static org.jooq.SQLDialect.POSTGRES;
import static org.jooq.SQLDialect.SQLITE;
// ...
// ...

import org.jooq.api.annotation.State;
import org.jooq.api.annotation.Transition;

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
public interface SelectLimitStep<R extends Record> extends SelectForUpdateStep<R> {

    /**
     * Add a <code>LIMIT</code> clause to the query
     * <p>
     * If there is no <code>LIMIT</code> or <code>TOP</code> clause in your
     * RDBMS, this may be simulated with a <code>ROW_NUMBER()</code> window
     * function and nested <code>SELECT</code> statements.
     * <p>
     * This is the same as calling {@link #limit(int, int)} with offset = 0, or
     * calling <code>.limit(numberOfRows).offset(0)</code>
     */
    @Support
    @Transition(
        name = "LIMIT",
        args = "Integer"
    )
    SelectOffsetStep<R> limit(int numberOfRows);

    /**
     * Add a <code>LIMIT</code> clause to the query using named parameters
     * <p>
     * Note that some dialects do not support bind values at all in
     * <code>LIMIT</code> or <code>TOP</code> clauses!
     * <p>
     * If there is no <code>LIMIT</code> or <code>TOP</code> clause in your
     * RDBMS, or the <code>LIMIT</code> or <code>TOP</code> clause does not
     * support bind values, this may be simulated with a
     * <code>ROW_NUMBER()</code> window function and nested <code>SELECT</code>
     * statements.
     * <p>
     * This is the same as calling {@link #limit(int, int)} with offset = 0, or
     * calling <code>.limit(numberOfRows).offset(0)</code>
     */
    @Support({ CUBRID, DERBY, FIREBIRD, H2, HSQLDB, MARIADB, MYSQL, POSTGRES, SQLITE })
    SelectOffsetStep<R> limit(Param<Integer> numberOfRows);

    /**
     * Add a <code>LIMIT</code> clause to the query
     * <p>
     * Note that some dialects do not support bind values at all in
     * <code>LIMIT</code> or <code>TOP</code> clauses!
     * <p>
     * If there is no <code>LIMIT</code> or <code>TOP</code> clause in your
     * RDBMS, or if your RDBMS does not natively support offsets, this is
     * simulated with a <code>ROW_NUMBER()</code> window function and nested
     * <code>SELECT</code> statements.
     */
    @Support({ CUBRID, DERBY, FIREBIRD, H2, HSQLDB, MARIADB, MYSQL, POSTGRES, SQLITE })
    @Transition(
        name = "LIMIT",
        args = {
            "Integer",
            "Integer"
        }
    )
    SelectForUpdateStep<R> limit(int offset, int numberOfRows);

    /**
     * Add a <code>LIMIT</code> clause to the query using named parameters
     * <p>
     * Note that some dialects do not support bind values at all in
     * <code>LIMIT</code> or <code>TOP</code> clauses!
     * <p>
     * If there is no <code>LIMIT</code> or <code>TOP</code> clause in your
     * RDBMS, or the <code>LIMIT</code> or <code>TOP</code> clause does not
     * support bind values, or if your RDBMS does not natively support offsets,
     * this may be simulated with a <code>ROW_NUMBER()</code> window function
     * and nested <code>SELECT</code> statements.
     */
    @Support({ CUBRID, DERBY, FIREBIRD, H2, HSQLDB, MARIADB, MYSQL, POSTGRES, SQLITE })
    SelectForUpdateStep<R> limit(int offset, Param<Integer> numberOfRows);

    /**
     * Add a <code>LIMIT</code> clause to the query using named parameters
     * <p>
     * Note that some dialects do not support bind values at all in
     * <code>LIMIT</code> or <code>TOP</code> clauses!
     * <p>
     * If there is no <code>LIMIT</code> or <code>TOP</code> clause in your
     * RDBMS, or the <code>LIMIT</code> or <code>TOP</code> clause does not
     * support bind values, or if your RDBMS does not natively support offsets,
     * this may be simulated with a <code>ROW_NUMBER()</code> window function
     * and nested <code>SELECT</code> statements.
     */
    @Support({ CUBRID, DERBY, FIREBIRD, H2, HSQLDB, MARIADB, MYSQL, POSTGRES, SQLITE })
    SelectForUpdateStep<R> limit(Param<Integer> offset, int numberOfRows);

    /**
     * Add a <code>LIMIT</code> clause to the query using named parameters
     * <p>
     * Note that some dialects do not support bind values at all in
     * <code>LIMIT</code> or <code>TOP</code> clauses!
     * <p>
     * If there is no <code>LIMIT</code> or <code>TOP</code> clause in your
     * RDBMS, or the <code>LIMIT</code> or <code>TOP</code> clause does not
     * support bind values, or if your RDBMS does not natively support offsets,
     * this may be simulated with a <code>ROW_NUMBER()</code> window function
     * and nested <code>SELECT</code> statements.
     */
    @Support({ CUBRID, DERBY, FIREBIRD, H2, HSQLDB, MARIADB, MYSQL, POSTGRES, SQLITE })
    SelectForUpdateStep<R> limit(Param<Integer> offset, Param<Integer> numberOfRows);
}
