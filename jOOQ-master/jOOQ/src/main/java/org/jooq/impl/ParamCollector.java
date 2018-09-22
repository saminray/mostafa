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

import java.sql.SQLException;
import java.util.LinkedHashMap;
import java.util.Map;

import org.jooq.BindContext;
import org.jooq.Configuration;
import org.jooq.Field;
import org.jooq.Param;
import org.jooq.QueryPart;
import org.jooq.QueryPartInternal;
import org.jooq.tools.StringUtils;

/**
 * A stub {@link BindContext} that acts as a collector of {@link Param}
 * {@link QueryPart}'s
 *
 * @author Lukas Eder
 */
class ParamCollector extends AbstractBindContext {

    final Map<String, Param<?>> result = new LinkedHashMap<String, Param<?>>();
    private final boolean       includeInlinedParams;

    ParamCollector(Configuration configuration, boolean includeInlinedParams) {
        super(configuration, null);

        this.includeInlinedParams = includeInlinedParams;
    }

    @Override
    protected final void bindInternal(QueryPartInternal internal) {
        if (internal instanceof Param) {
            Param<?> param = (Param<?>) internal;

            // [#3131] Inlined parameters should not be returned in some contexts
            if (includeInlinedParams || !param.isInline()) {
                String i = String.valueOf(nextIndex());

                if (StringUtils.isBlank(param.getParamName())) {
                    result.put(i, param);
                }
                else {
                    result.put(param.getParamName(), param);
                }
            }
        }
        else {
            super.bindInternal(internal);
        }
    }

    @Override
    protected final BindContext bindValue0(Object value, Field<?> field) throws SQLException {
        throw new UnsupportedOperationException();
    }
}
