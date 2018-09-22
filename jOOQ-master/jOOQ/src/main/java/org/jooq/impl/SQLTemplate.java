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

import static org.jooq.Clause.TEMPLATE;

import java.util.List;

import org.jooq.Clause;
import org.jooq.Context;
import org.jooq.QueryPart;
import org.jooq.Template;

@SuppressWarnings("deprecation")
class SQLTemplate implements Template {

    private final String sql;

    SQLTemplate(String sql) {
        this.sql = sql;
    }

    @Override
    public final QueryPart transform(Object... input) {
        return new SQLTemplateQueryPart(sql, input);
    }

    private static class SQLTemplateQueryPart extends AbstractQueryPart {

        /**
         * Generated UID
         */
        private static final long     serialVersionUID = -7514156096865122018L;
        private static final Clause[] CLAUSES          = { TEMPLATE };
        private final String          sql;
        private final List<QueryPart> substitutes;

        SQLTemplateQueryPart(String sql, Object... input) {
            this.sql = sql;
            this.substitutes = Utils.queryParts(input);
        }

        @Override
        public final void accept(Context<?> ctx) {
            Utils.renderAndBind(ctx, sql, substitutes);
        }

        @Override
        public final Clause[] clauses(Context<?> ctx) {
            return CLAUSES;
        }
    }
}
