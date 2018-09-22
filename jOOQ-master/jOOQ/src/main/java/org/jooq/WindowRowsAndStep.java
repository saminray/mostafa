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

// ...
// ...
import static org.jooq.SQLDialect.POSTGRES;
// ...
// ...

import org.jooq.api.annotation.State;
import org.jooq.api.annotation.Transition;

/**
 * This type is used for the window function DSL API.
 * <p>
 * Example: <code><pre>
 * field.firstValue()
 *      .ignoreNulls()
 *      .over()
 *      .partitionBy(AUTHOR_ID)
 *      .orderBy(PUBLISHED_IN.asc())
 *      .rowsBetweenUnboundedPreceding()
 *      .andUnboundedFollowing()
 * </pre></code>
 *
 * @param <T> The function return type
 * @author Lukas Eder
 */
@State
public interface WindowRowsAndStep<T> {

    /**
     * Add a <code>... AND UNBOUNDED PRECEDING</code> frame clause to the window
     * function.
     */
    @Support({ POSTGRES })
    @Transition(
        name = "AND UNBOUNDED PRECEDING"
    )
    WindowFinalStep<T> andUnboundedPreceding();

    /**
     * Add a <code>... AND [number] PRECEDING</code> frame clause to the window
     * function.
     */
    @Support({ POSTGRES })
    @Transition(
        name = "AND PRECEDING",
        args = "Integer"
    )
    WindowFinalStep<T> andPreceding(int number);

    /**
     * Add a <code>... AND CURRENT ROW</code> frame clause to the window
     * function.
     */
    @Support({ POSTGRES })
    @Transition(
        name = "AND CURRENT ROW"
    )
    WindowFinalStep<T> andCurrentRow();

    /**
     * Add a <code>... AND UNBOUNDED FOLLOWING</code> frame clause to the window
     * function.
     */
    @Support({ POSTGRES })
    @Transition(
        name = "AND UNBOUNDED FOLLOWING"
    )
    WindowFinalStep<T> andUnboundedFollowing();

    /**
     * Add a <code>... AND [number] FOLLOWING</code> frame clause to the window
     * function.
     */
    @Support({ POSTGRES })
    @Transition(
        name = "AND FOLLOWING",
        args = "Integer"
    )
    WindowFinalStep<T> andFollowing(int number);
}
