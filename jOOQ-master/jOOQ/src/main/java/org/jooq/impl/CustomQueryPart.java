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

import static org.jooq.Clause.CUSTOM;

import org.jooq.BindContext;
import org.jooq.Clause;
import org.jooq.Context;
import org.jooq.QueryPart;
import org.jooq.RenderContext;
import org.jooq.exception.DataAccessException;

/**
 * A base class for custom {@link QueryPart} implementations in client code.
 * <p>
 * Client code may provide proper {@link QueryPart} implementations extending
 * this useful base class. All necessary parts of the {@link QueryPart}
 * interface are already implemented. Only these two methods need further
 * implementation:
 * <ul>
 * <li>{@link #toSQL(org.jooq.RenderContext)}</li>
 * <li>{@link #bind(org.jooq.BindContext)}</li>
 * </ul>
 * Refer to those methods' Javadoc for further details about their expected
 * behaviour.
 * <p>
 * Such custom <code>QueryPart</code> implementations can be useful in any of
 * these scenarios:
 * <ul>
 * <li>When reusing a custom <code>QueryPart</code> in other custom
 * <code>QueryPart</code>s, e.g. in {@link CustomCondition}, {@link CustomField}, {@link CustomTable}, etc.</li>
 * <li>When inlining a custom <code>QueryPart</code> in plain SQL methods, such
 * as {@link DSL#condition(String, QueryPart...)},
 * {@link DSL#field(String, QueryPart...)},
 * {@link DSL#table(String, QueryPart...)}</li>
 * </ul>
 *
 * @author Lukas Eder
 */
public abstract class CustomQueryPart extends AbstractQueryPart {

    /**
     * Generated UID
     */
    private static final long     serialVersionUID = -3439681086987884991L;
    private static final Clause[] CLAUSES          = { CUSTOM };

    protected CustomQueryPart() {
    }

    // -------------------------------------------------------------------------
    // Implementation required
    // -------------------------------------------------------------------------

    /**
     * Subclasses must implement this method
     * <hr/>
     * {@inheritDoc}
     */
    @Override
    public abstract void toSQL(RenderContext context);

    // -------------------------------------------------------------------------
    // Implementation optional
    // -------------------------------------------------------------------------

    /**
     * Subclasses may implement this method.
     * <hr/>
     * {@inheritDoc}
     */
    @Override
    public void accept(Context<?> ctx) {
        if (ctx instanceof RenderContext)
            toSQL((RenderContext) ctx);
        else
            bind((BindContext) ctx);
    }

    /**
     * Subclasses may implement this method
     * <hr/>
     * {@inheritDoc}
     */
    @Override
    public void bind(BindContext context) throws DataAccessException {
    }

    // -------------------------------------------------------------------------
    // No further overrides allowed
    // -------------------------------------------------------------------------

    @Override
    public final Clause[] clauses(Context<?> ctx) {
        return CLAUSES;
    }

    @Override
    public final boolean declaresFields() {
        return super.declaresFields();
    }

    @Override
    public final boolean declaresTables() {
        return super.declaresTables();
    }
}
