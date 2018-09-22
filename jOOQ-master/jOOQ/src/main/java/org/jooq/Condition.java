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
import org.jooq.impl.DSL;


/**
 * A condition to be used in a query's where part
 *
 * @author Lukas Eder
 */
@State(
    name = "Condition",
    aliases = {
        "CombinedPredicate",
        "ComparisonPredicate",
        "LikePredicate",
        "InPredicate",
        "ExistsPredicate",
        "NullPredicate",
        "DistinctPredicate",
        "BetweenPredicate"
    },
    terminal = true
)
public interface Condition extends QueryPart {

    /**
     * Combine this condition with another one using the {@link Operator#AND}
     * operator.
     *
     * @param other The other condition
     * @return The combined condition
     */
    @Support
    @Transition(
        name = "AND",
        args = "Condition",
        to = "CombinedPredicate"
    )
    Condition and(Condition other);

    /**
     * Combine this condition with another one using the {@link Operator#AND}
     * operator.
     *
     * @param other The other condition
     * @return The combined condition
     */
    @Support
    @Transition(
        name = "AND",
        args = "Condition",
        to = "CombinedPredicate"
    )
    Condition and(Field<Boolean> other);

    /**
     * Combine this condition with another one using the {@link Operator#AND}
     * operator.
     * <p>
     * <b>NOTE</b>: When inserting plain SQL into jOOQ objects, you must
     * guarantee syntax integrity. You may also create the possibility of
     * malicious SQL injection. Be sure to properly use bind variables and/or
     * escape literals when concatenated into SQL clauses!
     *
     * @param sql The other condition
     * @return The combined condition
     * @see DSL#condition(String)
     */
    @Support
    Condition and(String sql);

    /**
     * Combine this condition with another one using the {@link Operator#AND}
     * operator.
     * <p>
     * <b>NOTE</b>: When inserting plain SQL into jOOQ objects, you must
     * guarantee syntax integrity. You may also create the possibility of
     * malicious SQL injection. Be sure to properly use bind variables and/or
     * escape literals when concatenated into SQL clauses!
     *
     * @param sql The other condition
     * @param bindings The bindings
     * @return The combined condition
     * @see DSL#condition(String, Object...)
     */
    @Support
    Condition and(String sql, Object... bindings);

    /**
     * Combine this condition with another one using the {@link Operator#AND}
     * operator.
     * <p>
     * <b>NOTE</b>: When inserting plain SQL into jOOQ objects, you must
     * guarantee syntax integrity. You may also create the possibility of
     * malicious SQL injection. Be sure to properly use bind variables and/or
     * escape literals when concatenated into SQL clauses!
     *
     * @param sql The SQL clause, containing {numbered placeholders} where query
     *            parts can be injected
     * @param parts The {@link QueryPart} objects that are rendered at the
     *            {numbered placeholder} locations
     * @return The combined condition
     * @see DSL#condition(String, QueryPart...)
     */
    @Support
    Condition and(String sql, QueryPart... parts);

    /**
     * Combine this condition with a negated other one using the
     * {@link Operator#AND} operator.
     *
     * @param other The other condition
     * @return The combined condition
     */
    @Support
    @Transition(
        name = "AND NOT",
        args = "Condition",
        to = "CombinedPredicate"
    )
    Condition andNot(Condition other);

    /**
     * Combine this condition with a negated other one using the
     * {@link Operator#AND} operator.
     *
     * @param other The other condition
     * @return The combined condition
     */
    @Support
    @Transition(
        name = "AND NOT",
        args = "Condition",
        to = "CombinedPredicate"
    )
    Condition andNot(Field<Boolean> other);

    /**
     * Combine this condition with an EXISTS clause using the
     * {@link Operator#AND} operator.
     *
     * @param select The EXISTS's subquery
     * @return The combined condition
     */
    @Support
    @Transition(
        name = "AND EXISTS",
        args = "Select",
        to = "CombinedPredicate"
    )
    Condition andExists(Select<?> select);

    /**
     * Combine this condition with a NOT EXIST clause using the
     * {@link Operator#AND} operator.
     *
     * @param select The EXISTS's subquery
     * @return The combined condition
     */
    @Support
    @Transition(
        name = "AND NOT EXISTS",
        args = "Select",
        to = "CombinedPredicate"
    )
    Condition andNotExists(Select<?> select);

    /**
     * Combine this condition with another one using the {@link Operator#OR}
     * operator.
     *
     * @param other The other condition
     * @return The combined condition
     */
    @Support
    @Transition(
        name = "OR",
        args = "Condition",
        to = "CombinedPredicate"
    )
    Condition or(Condition other);

    /**
     * Combine this condition with another one using the {@link Operator#OR}
     * operator.
     *
     * @param other The other condition
     * @return The combined condition
     */
    @Support
    @Transition(
        name = "OR",
        args = "Condition",
        to = "CombinedPredicate"
    )
    Condition or(Field<Boolean> other);

    /**
     * Combine this condition with another one using the {@link Operator#OR}
     * operator.
     * <p>
     * <b>NOTE</b>: When inserting plain SQL into jOOQ objects, you must
     * guarantee syntax integrity. You may also create the possibility of
     * malicious SQL injection. Be sure to properly use bind variables and/or
     * escape literals when concatenated into SQL clauses!
     *
     * @param sql The other condition
     * @return The combined condition
     * @see DSL#condition(String)
     */
    @Support
    Condition or(String sql);

    /**
     * Combine this condition with another one using the {@link Operator#OR}
     * operator.
     * <p>
     * <b>NOTE</b>: When inserting plain SQL into jOOQ objects, you must
     * guarantee syntax integrity. You may also create the possibility of
     * malicious SQL injection. Be sure to properly use bind variables and/or
     * escape literals when concatenated into SQL clauses!
     *
     * @param sql The other condition
     * @param bindings The bindings
     * @return The combined condition
     * @see DSL#condition(String, Object...)
     */
    @Support
    Condition or(String sql, Object... bindings);

    /**
     * Combine this condition with another one using the {@link Operator#OR}
     * operator.
     * <p>
     * <b>NOTE</b>: When inserting plain SQL into jOOQ objects, you must
     * guarantee syntax integrity. You may also create the possibility of
     * malicious SQL injection. Be sure to properly use bind variables and/or
     * escape literals when concatenated into SQL clauses!
     *
     * @param sql The SQL clause, containing {numbered placeholders} where query
     *            parts can be injected
     * @param parts The {@link QueryPart} objects that are rendered at the
     *            {numbered placeholder} locations
     * @return The combined condition
     * @see DSL#condition(String, Object...)
     */
    @Support
    Condition or(String sql, QueryPart... parts);

    /**
     * Combine this condition with a negated other one using the
     * {@link Operator#OR} operator.
     *
     * @param other The other condition
     * @return The combined condition
     */
    @Support
    @Transition(
        name = "OR NOT",
        args = "Condition",
        to = "CombinedPredicate"
    )
    Condition orNot(Condition other);

    /**
     * Combine this condition with a negated other one using the
     * {@link Operator#OR} operator.
     *
     * @param other The other condition
     * @return The combined condition
     */
    @Support
    @Transition(
        name = "OR NOT",
        args = "Condition",
        to = "CombinedPredicate"
    )
    Condition orNot(Field<Boolean> other);

    /**
     * Combine this condition with an EXISTS clause using the
     * {@link Operator#OR} operator.
     *
     * @param select The EXISTS's subquery
     * @return The combined condition
     */
    @Support
    @Transition(
        name = "OR EXISTS",
        args = "Select",
        to = "CombinedPredicate"
    )
    Condition orExists(Select<?> select);

    /**
     * Combine this condition with a NOT EXIST clause using the
     * {@link Operator#OR} operator.
     *
     * @param select The EXISTS's subquery
     * @return The combined condition
     */
    @Support
    @Transition(
        name = "OR NOT EXISTS",
        args = "Select",
        to = "CombinedPredicate"
    )
    Condition orNotExists(Select<?> select);

    /**
     * Invert this condition
     * <p>
     * This is the same as calling {@link DSL#not(Condition)}
     *
     * @return This condition, inverted
     */
    @Support
    @Transition(
        name = "NOT",
        to = "CombinedPredicate"
    )
    Condition not();
}
