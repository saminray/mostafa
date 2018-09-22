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

import static org.jooq.tools.Convert.convert;

import java.util.LinkedHashMap;
import java.util.Map;

import org.jooq.Converter;

/**
 * A base class for enum conversion.
 *
 * @author Lukas Eder
 */
public class EnumConverter<T, U extends Enum<U>> implements Converter<T, U> {

    /**
     * Generated UID
     */
    private static final long serialVersionUID = -6094337837408829491L;

    private final Class<T>    fromType;
    private final Class<U>    toType;
    private final Map<T, U>   lookup;
    private final EnumType    enumType;

    public EnumConverter(Class<T> fromType, Class<U> toType) {
        this.fromType = fromType;
        this.toType = toType;
        this.enumType = Number.class.isAssignableFrom(fromType) ? EnumType.ORDINAL : EnumType.STRING;

        this.lookup = new LinkedHashMap<T, U>();
        for (U u : toType.getEnumConstants()) {
            this.lookup.put(to(u), u);
        }
    }

    @Override
    public final U from(T databaseObject) {
        return lookup.get(databaseObject);
    }

    /**
     * Subclasses may override this method to provide a custom reverse mapping
     * implementation
     * <p>
     * {@inheritDoc}
     */
    @Override
    public T to(U userObject) {
        if (userObject == null) {
            return null;
        }
        else if (enumType == EnumType.ORDINAL) {
            return convert(userObject.ordinal(), fromType);
        }
        else {
            return convert(userObject.name(), fromType);
        }
    }

    @Override
    public final Class<T> fromType() {
        return fromType;
    }

    @Override
    public final Class<U> toType() {
        return toType;
    }

    /**
     * The type of the converted <code>Enum</code>.
     * <p>
     * This corresponds to JPA's <code>EnumType</code>
     */
    enum EnumType {

        /**
         * Ordinal enum type
         */
        ORDINAL,

        /**
         * String enum type
         */
        STRING
    }

    @Override
    public String toString() {
        return "EnumConverter [ from : " + fromType.getName() + ", to : " + toType.getName() + " ]";
    }
}
