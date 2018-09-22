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

import java.sql.Connection;
import java.sql.SQLException;

import javax.sql.DataSource;

import org.jooq.ConnectionProvider;
import org.jooq.exception.DataAccessException;

/**
 * A default implementation for a pooled {@link DataSource}-oriented
 * {@link ConnectionProvider}
 * <p>
 * This implementation wraps a JDBC {@link DataSource}. jOOQ will use that data
 * source for initialising connections, and creating statements.
 * <p>
 * Use this connection provider if you want to run distributed transactions,
 * such as <code>javax.transaction.UserTransaction</code>. jOOQ will
 * {@link Connection#close() close()} all connections after query execution (and
 * result fetching) in order to return the connection to the connection pool. If
 * you do not use distributed transactions, this will produce driver-specific
 * behaviour at the end of query execution at <code>close()</code> invocation
 * (e.g. a transaction rollback). Use a {@link DefaultConnectionProvider}
 * instead, to control the connection's lifecycle, or implement your own
 * {@link ConnectionProvider}.
 *
 * @author Aaron Digulla
 * @author Lukas Eder
 */
public class DataSourceConnectionProvider implements ConnectionProvider {

    private final DataSource dataSource;

    public DataSourceConnectionProvider(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    public DataSource dataSource() {
        return dataSource;
    }

    @Override
    public Connection acquire() {
        try {
            return dataSource.getConnection();
        }
        catch (SQLException e) {
            throw new DataAccessException("Error getting connection from data source " + dataSource, e);
        }
    }

    @Override
    public void release(Connection connection) {
        try {
            connection.close();
        }
        catch (SQLException e) {
            throw new DataAccessException("Error closing connection " + connection, e);
        }
    }
}
