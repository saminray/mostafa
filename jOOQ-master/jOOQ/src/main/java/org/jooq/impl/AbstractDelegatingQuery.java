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

import java.util.List;
import java.util.Map;

import org.jooq.AttachableInternal;
import org.jooq.Clause;
import org.jooq.Configuration;
import org.jooq.Context;
import org.jooq.Param;
import org.jooq.Query;
import org.jooq.conf.ParamType;

/**
 * @author Lukas Eder
 */
abstract class AbstractDelegatingQuery<Q extends Query> extends AbstractQueryPart implements Query {

    /**
     * Generated UID
     */
    private static final long serialVersionUID = 6710523592699040547L;
    private final Q           delegate;

    AbstractDelegatingQuery(Q delegate) {
        this.delegate = delegate;
    }

    @Override
    public final Configuration configuration() {
        if (delegate instanceof AttachableInternal) {
            return ((AttachableInternal) delegate).configuration();
        }

        return super.configuration();
    }

    @Override
    public final List<Object> getBindValues() {
        return delegate.getBindValues();
    }

    @Override
    public final Map<String, Param<?>> getParams() {
        return delegate.getParams();
    }

    @Override
    public final Param<?> getParam(String name) {
        return delegate.getParam(name);
    }

    @Override
    public final void accept(Context<?> context) {
        context.visit(delegate);
    }

    @Override
    public final Clause[] clauses(Context<?> ctx) {

        // Delegate queries don't emit clauses themselves.
        return null;
    }

    @Override
    public final String getSQL() {
        return delegate.getSQL();
    }

    @Override
    @Deprecated
    public final String getSQL(boolean inline) {
        return delegate.getSQL(inline);
    }

    @Override
    public final String getSQL(ParamType paramType) {
        return delegate.getSQL(paramType);
    }

    @Override
    public final void attach(Configuration configuration) {
        delegate.attach(configuration);
    }

    @Override
    public final void detach() {
        delegate.detach();
    }

    @Override
    public final int execute() {
        return delegate.execute();
    }

    @Override
    public final boolean isExecutable() {
        return delegate.isExecutable();
    }

    @SuppressWarnings("unchecked")
    @Override
    public final Q bind(String param, Object value) {
        return (Q) delegate.bind(param, value);
    }

    @SuppressWarnings("unchecked")
    @Override
    public final Q bind(int index, Object value) {
        return (Q) delegate.bind(index, value);
    }

    @SuppressWarnings("unchecked")
    @Override
    public final Q queryTimeout(int timeout) {
        return (Q) delegate.queryTimeout(timeout);
    }

    @SuppressWarnings("unchecked")
    @Override
    public final Q keepStatement(boolean keepStatement) {
        return (Q) delegate.keepStatement(keepStatement);
    }

    @Override
    public final void close() {
        delegate.close();
    }

    @Override
    public final void cancel() {
        delegate.cancel();
    }

    final Q getDelegate() {
        return delegate;
    }
}
