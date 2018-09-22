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
import static org.jooq.SQLDialect.H2;
import static org.jooq.SQLDialect.HSQLDB;
import static org.jooq.SQLDialect.MARIADB;
import static org.jooq.SQLDialect.MYSQL;
// ...
// ...
import static org.jooq.SQLDialect.POSTGRES;
// ...

import java.util.Collection;

import org.jooq.api.annotation.State;
import org.jooq.api.annotation.Transition;

/**
 * An ordered aggregate function.
 * <p>
 * An ordered aggregate function is an aggregate function with a mandatory
 * Oracle-specific <code>WITHIN GROUP (ORDER BY ..)</code> clause. An example is
 * <code>LISTAGG</code>: <code><pre>
 * SELECT   LISTAGG(TITLE, ', ')
 *          WITHIN GROUP (ORDER BY TITLE)
 * FROM     T_BOOK
 * GROUP BY AUTHOR_ID
 * </pre></code> The above function groups books by author and aggregates titles
 * into a concatenated string.
 * <p>
 * Ordered aggregate functions can be further converted into window functions
 * using the <code>OVER(PARTITION BY ..)</code> clause. For example: <code><pre>
 * SELECT LISTAGG(TITLE, ', ')
 *        WITHIN GROUP (ORDER BY TITLE)
 *        OVER (PARTITION BY AUTHOR_ID)
 * FROM   T_BOOK
 * </pre></code>
 *
 * @author Lukas Eder
 */
@State
public interface OrderedAggregateFunction<T> {

    /**
     * Add an <code>WITHIN GROUP (ORDER BY ..)</code> clause to the ordered
     * aggregate function
     */
    @Support({ CUBRID, H2, HSQLDB, MARIADB, MYSQL, POSTGRES })
    @Transition(
        name = "WITHIN GROUP ORDER BY",
        args = "Field+",
        to = "OrderedAggregateFunction"
    )
    AggregateFunction<T> withinGroupOrderBy(Field<?>... fields);

    /**
     * Add an <code>WITHIN GROUP (ORDER BY ..)</code> clause to the ordered
     * aggregate function
     */
    @Support({ CUBRID, H2, HSQLDB, MARIADB, MYSQL, POSTGRES })
    @Transition(
        name = "WITHIN GROUP ORDER BY",
        args = "SortField+",
        to = "OrderedAggregateFunction"
    )
    AggregateFunction<T> withinGroupOrderBy(SortField<?>... fields);

    /**
     * Add an <code>WITHIN GROUP (ORDER BY ..)</code> clause to the ordered
     * aggregate function
     */
    @Support({ CUBRID, H2, HSQLDB, MARIADB, MYSQL, POSTGRES })
    @Transition(
        name = "WITHIN GROUP ORDER BY",
        args = "SortField+",
        to = "OrderedAggregateFunction"
    )
    AggregateFunction<T> withinGroupOrderBy(Collection<? extends SortField<?>> fields);
}
