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
package org.jooq.impl;

import static org.jooq.Clause.CONDITION;
import static org.jooq.impl.DSL.condition;
import static org.jooq.impl.DSL.exists;
import static org.jooq.impl.DSL.notExists;

import java.util.Arrays;

import org.jooq.Clause;
import org.jooq.Condition;
import org.jooq.Context;
import org.jooq.Field;
import org.jooq.Operator;
import org.jooq.QueryPart;
import org.jooq.Select;

/**
 * @author Lukas Eder
 */
abstract class AbstractCondition extends AbstractQueryPart implements Condition {

    /**
     * Generated UID
     */
    private static final long     serialVersionUID = -6683692251799468624L;
    private static final Clause[] CLAUSES          = { CONDITION };

    AbstractCondition() {}

    @Override
    public Clause[] clauses(Context<?> ctx) {
        return CLAUSES;
    }

    @Override
    public final Condition and(Condition other) {
        return new CombinedCondition(Operator.AND, Arrays.asList(this, other));
    }

    /*
     * Subclasses may override this implementation when implementing
     * A BETWEEN B AND C
     */
    @Override
    public Condition and(Field<Boolean> other) {
        return and(condition(other));
    }

    @Override
    public final Condition or(Condition other) {
        return new CombinedCondition(Operator.OR, Arrays.asList(this, other));
    }

    @Override
    public final Condition or(Field<Boolean> other) {
        return or(condition(other));
    }

    @Override
    public final Condition and(String sql) {
        return and(condition(sql));
    }

    @Override
    public final Condition and(String sql, Object... bindings) {
        return and(condition(sql, bindings));
    }

    @Override
    public final Condition and(String sql, QueryPart... parts) {
        return and(condition(sql, parts));
    }

    @Override
    public final Condition or(String sql) {
        return or(condition(sql));
    }

    @Override
    public final Condition or(String sql, Object... bindings) {
        return or(condition(sql, bindings));
    }

    @Override
    public final Condition or(String sql, QueryPart... parts) {
        return or(condition(sql, parts));
    }

    @Override
    public final Condition andNot(Condition other) {
        return and(other.not());
    }

    @Override
    public final Condition andNot(Field<Boolean> other) {
        return andNot(condition(other));
    }

    @Override
    public final Condition orNot(Condition other) {
        return or(other.not());
    }

    @Override
    public final Condition orNot(Field<Boolean> other) {
        return orNot(condition(other));
    }

    @Override
    public final Condition andExists(Select<?> select) {
        return and(exists(select));
    }

    @Override
    public final Condition andNotExists(Select<?> select) {
        return and(notExists(select));
    }

    @Override
    public final Condition orExists(Select<?> select) {
        return or(exists(select));
    }

    @Override
    public final Condition orNotExists(Select<?> select) {
        return or(notExists(select));
    }

    @Override
    public final Condition not() {
        return new NotCondition(this);
    }
}
