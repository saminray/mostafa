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

import org.jooq.api.annotation.State;
import org.jooq.api.annotation.Transition;

/**
 * A wrapper for a {@link Field} and a {@link SortField}
 *
 * @param <T> The field type
 * @author Lukas Eder
 * @see Field#asc()
 * @see Field#desc()
 */
@State(
    terminal = true
)
public interface SortField<T> extends QueryPart {

    /**
     * The name of this sort field
     */
    String getName();

    /**
     * Get the underlying sort order of this sort field
     */
    SortOrder getOrder();

    /**
     * Add a <code>NULLS FIRST</code> clause to this sort field
     */
    @Support
    @Transition(
        name = "NULLS FIRST"
    )
    SortField<T> nullsFirst();

    /**
     * Add a <code>NULLS LAST</code> clause to this sort field
     */
    @Support
    @Transition(
        name = "NULLS LAST"
    )
    SortField<T> nullsLast();

}
