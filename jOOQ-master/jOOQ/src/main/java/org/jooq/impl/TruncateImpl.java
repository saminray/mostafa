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

import static org.jooq.Clause.TRUNCATE;
import static org.jooq.Clause.TRUNCATE_TRUNCATE;

import org.jooq.Clause;
import org.jooq.Configuration;
import org.jooq.Context;
import org.jooq.Record;
import org.jooq.SQLDialect;
import org.jooq.Table;
import org.jooq.TruncateCascadeStep;
import org.jooq.TruncateFinalStep;
import org.jooq.TruncateIdentityStep;

/**
 * @author Lukas Eder
 */
class TruncateImpl<R extends Record> extends AbstractQuery implements

    // Cascading interface implementations for Truncate behaviour
    TruncateIdentityStep<R> {

    /**
     * Generated UID
     */
    private static final long     serialVersionUID = 8904572826501186329L;
    private static final Clause[] CLAUSES          = { TRUNCATE };

    private final Table<R>    table;
    private Boolean           cascade;
    private Boolean           restartIdentity;

    public TruncateImpl(Configuration configuration, Table<R> table) {
        super(configuration);

        this.table = table;
    }

    @Override
    public final TruncateFinalStep<R> cascade() {
        cascade = true;
        return this;
    }

    @Override
    public final TruncateFinalStep<R> restrict() {
        cascade = false;
        return this;
    }

    @Override
    public final TruncateCascadeStep<R> restartIdentity() {
        restartIdentity = true;
        return this;
    }

    @Override
    public final TruncateCascadeStep<R> continueIdentity() {
        restartIdentity = false;
        return this;
    }

    @Override
    public final void accept(Context<?> ctx) {
        switch (ctx.configuration().dialect().family()) {

            // These dialects don't implement the TRUNCATE statement
            /* [pro] xx
            xxxx xxxxxxx
            xxxx xxxxxxx
            xx [/pro] */
            case FIREBIRD:
            case SQLITE: {
                ctx.visit(create(ctx).delete(table));
                break;
            }

            // All other dialects do
            default: {
                ctx.start(TRUNCATE_TRUNCATE)
                   .keyword("truncate table").sql(" ")
                   .visit(table);

                /* [pro] xx
                xx xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx xx xxxxxxxxxxxxxxx x
                    xxxxxxxxx xxxxxxxxxxxxxxxxxxxxxxxx
                x

                xx [/pro] */
                if (restartIdentity != null) {
                    ctx.formatSeparator()
                       .keyword(restartIdentity ? "restart identity" : "continue identity");
                }

                if (cascade != null) {
                    ctx.formatSeparator()
                       .keyword(cascade ? "cascade" : "restrict");
                }

                ctx.end(TRUNCATE_TRUNCATE);
                break;
            }
        }
    }

    @Override
    public final Clause[] clauses(Context<?> ctx) {
        return CLAUSES;
    }
}
