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
package org.jooq.conf;

import static org.jooq.conf.ParamType.INDEXED;
import static org.jooq.conf.ParamType.INLINED;
import static org.jooq.conf.StatementType.PREPARED_STATEMENT;
import static org.jooq.conf.StatementType.STATIC_STATEMENT;
import static org.jooq.tools.StringUtils.defaultIfNull;

import java.io.File;
import java.io.InputStream;
import java.sql.PreparedStatement;
import java.sql.Statement;

import javax.xml.bind.JAXB;

/**
 * Convenience methods for jOOQ runtime settings.
 *
 * @author Lukas Eder
 */
public final class SettingsTools {

    private static final Settings DEFAULT_SETTINGS;

    static {
        Settings settings = null;
        String property = System.getProperty("org.jooq.settings");

        if (property != null) {

            // Check classpath first
            InputStream in = SettingsTools.class.getResourceAsStream(property);
            if (in != null) {
                settings = JAXB.unmarshal(in, Settings.class);
            }
            else {
                settings = JAXB.unmarshal(new File(property), Settings.class);
            }
        }

        if (settings == null) {
            InputStream in = SettingsTools.class.getResourceAsStream("/jooq-settings.xml");

            if (in != null) {
                settings = JAXB.unmarshal(in, Settings.class);
            }
        }

        if (settings == null) {
            settings = new Settings();
        }

        DEFAULT_SETTINGS = settings;
    }

    /**
     * Get the parameter type from the settings.
     * <p>
     * The {@link ParamType} can be overridden by the {@link StatementType}.
     * If the latter is set to {@link StatementType#STATIC_STATEMENT}, then the
     * former defaults to {@link ParamType#INLINED}.
     */
    public static final ParamType getParamType(Settings settings) {
        if (executeStaticStatements(settings)) {
            return INLINED;
        }
        else if (settings != null) {
            ParamType result = settings.getParamType();

            if (result != null) {
                return result;
            }
        }

        return INDEXED;
    }

    /**
     * Get the statement type from the settings.
     */
    public static final StatementType getStatementType(Settings settings) {
        if (settings != null) {
            StatementType result = settings.getStatementType();

            if (result != null) {
                return result;
            }
        }

        return PREPARED_STATEMENT;
    }

    /**
     * Whether a {@link PreparedStatement} should be executed.
     */
    public static final boolean executePreparedStatements(Settings settings) {
        return getStatementType(settings) == PREPARED_STATEMENT;
    }

    /**
     * Whether static {@link Statement} should be executed.
     */
    public static final boolean executeStaticStatements(Settings settings) {
        return getStatementType(settings) == STATIC_STATEMENT;
    }

    /**
     * Whether primary keys should be updatable.
     */
    public static final boolean updatablePrimaryKeys(Settings settings) {
        return defaultIfNull(settings.isUpdatablePrimaryKeys(), false);
    }

    /**
     * Whether primary keys should be updatable.
     */
    public static final boolean reflectionCaching(Settings settings) {
        return defaultIfNull(settings.isReflectionCaching(), true);
    }

    /**
     * Lazy access to {@link RenderMapping}.
     */
    public static final RenderMapping getRenderMapping(Settings settings) {
        if (settings.getRenderMapping() == null) {
            settings.setRenderMapping(new RenderMapping());
        }

        return settings.getRenderMapping();
    }

    /**
     * Retrieve the configured default settings.
     * <p>
     * <ul>
     * <li>If the JVM flag <code>-Dorg.jooq.settings</code> points to a valid
     * settings file on the classpath, this will be loaded</li>
     * <li>If the JVM flag <code>-Dorg.jooq.settings</code> points to a valid
     * settings file on the file system, this will be loaded</li>
     * <li>If a valid settings file is found on the classpath at
     * <code>/jooq-settings.xml</code>, this will be loaded</li>
     * <li>Otherwise, a new <code>Settings</code> object is created with its
     * defaults</li>
     * </ul>
     */
    public static final Settings defaultSettings() {

        // Clone the DEFAULT_SETTINGS to prevent modification
        return clone(DEFAULT_SETTINGS);
    }

    /**
     * Clone some settings.
     */
    public static final Settings clone(Settings settings) {
        return (Settings) settings.clone();
    }
}
