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

// ...
// ...
import static org.jooq.SQLDialect.CUBRID;
// ...
import static org.jooq.SQLDialect.DERBY;
import static org.jooq.SQLDialect.FIREBIRD;
import static org.jooq.SQLDialect.H2;
import static org.jooq.SQLDialect.HSQLDB;
// ...
import static org.jooq.SQLDialect.MARIADB;
import static org.jooq.SQLDialect.MYSQL;
// ...
// ...
// ...
import static org.jooq.SQLDialect.POSTGRES;
import static org.jooq.SQLDialect.SQLITE;
// ...
// ...

import java.sql.Timestamp;
import java.util.Collection;
import java.util.List;

import org.jooq.api.annotation.State;
import org.jooq.api.annotation.Transition;
import org.jooq.conf.Settings;
import org.jooq.impl.DSL;

/**
 * A table to be used in queries
 *
 * @param <R> The record type associated with this table
 * @author Lukas Eder
 */
@State(
    name = "Table",
    aliases = {
        "AliasedTable",
        "JoinedTable",
        "PivotTable",
        "DividedTable",
        "UnnestedTable"
    },
    terminal = true
)
public interface Table<R extends Record> extends TableLike<R> {

    /**
     * Get the table schema.
     */
    Schema getSchema();

    /**
     * The name of this table.
     */
    String getName();

    /**
     * The comment given to the table.
     * <p>
     * If this <code>Table</code> is a generated table from your database, it
     * may provide its DDL comment through this method. All other table
     * expressions return the empty string <code>""</code> here, never
     * <code>null</code>.
     */
    String getComment();

    /**
     * The record type produced by this table.
     */
    RecordType<R> recordType();

    /**
     * The record type produced by this table.
     */
    Class<? extends R> getRecordType();

    /**
     * Retrieve the table's <code>IDENTITY</code> information, if available.
     * <p>
     * With SQL:2003, the concept of <code>IDENTITY</code> columns was
     * introduced in most RDBMS. These are special kinds of columns that have
     * auto-increment functionality when <code>INSERT</code> statements are
     * performed.
     * <p>
     * An <code>IDENTITY</code> column is usually part of the
     * <code>PRIMARY KEY</code> or of a <code>UNIQUE KEY</code> in the table,
     * although in some RDBMS, this is not required. There can only be at most
     * one <code>IDENTITY</code> column.
     * <p>
     * Note: Unfortunately, this is not supported in the Oracle dialect, where
     * identities simulated by triggers cannot be formally detected.
     *
     * @return The table's <code>IDENTITY</code> information, or
     *         <code>null</code>, if no such information is available.
     */
    Identity<R, ? extends Number> getIdentity();
    /**
     * Retrieve the table's primary key
     *
     * @return The primary key. This is never <code>null</code> for an updatable
     *         table.
     */
    UniqueKey<R> getPrimaryKey();

    /**
     * A "version" field holding record version information used for optimistic
     * locking
     * <p>
     * jOOQ supports optimistic locking in {@link UpdatableRecord#store()} and
     * {@link UpdatableRecord#delete()} if
     * {@link Settings#isExecuteWithOptimisticLocking()} is enabled. Optimistic
     * locking is performed in a single <code>UPDATE</code> or
     * <code>DELETE</code> statement if tables provide a "version" or
     * "timestamp" field, or in two steps using an additional
     * <code>SELECT .. FOR UPDATE</code> statement otherwise.
     * <p>
     * This method is overridden in generated subclasses if their corresponding
     * tables have been configured accordingly. A table may have both a
     * "version" and a "timestamp" field.
     *
     * @return The "version" field, or <code>null</code>, if this table has no
     *         "version" field.
     * @see #getRecordTimestamp()
     * @see UpdatableRecord#store()
     * @see UpdatableRecord#delete()
     * @see Settings#isExecuteWithOptimisticLocking()
     */
    TableField<R, ? extends Number> getRecordVersion();

    /**
     * A "timestamp" field holding record timestamp information used for
     * optimistic locking
     * <p>
     * jOOQ supports optimistic locking in {@link UpdatableRecord#store()} and
     * {@link UpdatableRecord#delete()} if
     * {@link Settings#isExecuteWithOptimisticLocking()} is enabled. Optimistic
     * locking is performed in a single <code>UPDATE</code> or
     * <code>DELETE</code> statement if tables provide a "version" or
     * "timestamp" field, or in two steps using an additional
     * <code>SELECT .. FOR UPDATE</code> statement otherwise.
     * <p>
     * This method is overridden in generated subclasses if their corresponding
     * tables have been configured accordingly. A table may have both a
     * "version" and a "timestamp" field.
     *
     * @return The "timestamp" field, or <code>null</code>, if this table has no
     *         "timestamp" field.
     * @see #getRecordVersion()
     * @see UpdatableRecord#store()
     * @see UpdatableRecord#delete()
     * @see Settings#isExecuteWithOptimisticLocking()
     */
    TableField<R, ? extends java.util.Date> getRecordTimestamp();

    /**
     * Retrieve all of the table's unique keys.
     *
     * @return All keys. This is never <code>null</code>. This is never empty
     *         for a {@link Table} with a {@link Table#getPrimaryKey()}. This
     *         method returns an unmodifiable list.
     */
    List<UniqueKey<R>> getKeys();

    /**
     * Get a list of <code>FOREIGN KEY</code>'s of a specific table, referencing
     * a this table.
     *
     * @param <O> The other table's record type
     * @param other The other table of the foreign key relationship
     * @return Some other table's <code>FOREIGN KEY</code>'s towards an this
     *         table. This is never <code>null</code>. This method returns an
     *         unmodifiable list.
     */
    <O extends Record> List<ForeignKey<O, R>> getReferencesFrom(Table<O> other);

    /**
     * Get the list of <code>FOREIGN KEY</code>'s of this table
     *
     * @return This table's <code>FOREIGN KEY</code>'s. This is never
     *         <code>null</code>.
     */
    List<ForeignKey<R, ?>> getReferences();

    /**
     * Get a list of <code>FOREIGN KEY</code>'s of this table, referencing a
     * specific table.
     *
     * @param <O> The other table's record type
     * @param other The other table of the foreign key relationship
     * @return This table's <code>FOREIGN KEY</code>'s towards an other table.
     *         This is never <code>null</code>.
     */
    <O extends Record> List<ForeignKey<R, O>> getReferencesTo(Table<O> other);

    // -------------------------------------------------------------------------
    // XXX: Aliasing clauses
    // -------------------------------------------------------------------------

    /**
     * Create an alias for this table.
     * <p>
     * Note that the case-sensitivity of the returned table depends on
     * {@link Settings#getRenderNameStyle()}. By default, table aliases are
     * quoted, and thus case-sensitive!
     *
     * @param alias The alias name
     * @return The table alias
     */
    @Support
    @Transition(
        name = "AS",
        to = "AliasedTable",
        args = "String"
    )
    Table<R> as(String alias);

    /**
     * Create an alias for this table and its fields
     * <p>
     * Note that the case-sensitivity of the returned table and columns depends
     * on {@link Settings#getRenderNameStyle()}. By default, table aliases are
     * quoted, and thus case-sensitive!
     * <p>
     * <h5>Derived column lists for table references</h5>
     * <p>
     * Note, not all databases support derived column lists for their table
     * aliases. On the other hand, some databases do support derived column
     * lists, but only for derived tables. jOOQ will try to turn table
     * references into derived tables to make this syntax work. In other words,
     * the following statements are equivalent: <code><pre>
     * -- Using derived column lists to rename columns (e.g. Postgres)
     * SELECT t.a, t.b
     * FROM my_table t(a, b)
     *
     * -- Nesting table references within derived tables (e.g. SQL Server)
     * SELECT t.a, t.b
     * FROM (
     *   SELECT * FROM my_table
     * ) t(a, b)
     * </pre></code>
     * <p>
     * <h5>Derived column lists for derived tables</h5>
     * <p>
     * Other databases may not support derived column lists at all, but they do
     * support common table expressions. The following statements are
     * equivalent: <code><pre>
     * -- Using derived column lists to rename columns (e.g. Postgres)
     * SELECT t.a, t.b
     * FROM (
     *   SELECT 1, 2
     * ) AS t(a, b)
     *
     * -- Using UNION ALL to produce column names (e.g. MySQL)
     * SELECT t.a, t.b
     * FROM (
     *   SELECT null a, null b FROM DUAL WHERE 1 = 0
     *   UNION ALL
     *   SELECT 1, 2 FROM DUAL
     * ) t
     * </pre></code>
     *
     * @param alias The alias name
     * @param fieldAliases The field aliases. Excess aliases are ignored,
     *            missing aliases will be substituted by this table's field
     *            names.
     * @return The table alias
     */
    @Support
    @Transition(
        name = "AS",
        to = "AliasedTable",
        args = {
            "String",
            "String+"
        }
    )
    Table<R> as(String alias, String... fieldAliases);

    // -------------------------------------------------------------------------
    // XXX: JOIN clauses on tables
    // -------------------------------------------------------------------------

    /**
     * Join a table to this table using a {@link JoinType}
     * <p>
     * Depending on the <code>JoinType</code>, a subsequent
     * {@link TableOnStep#on(Condition...)} or
     * {@link TableOnStep#using(Field...)} clause is required. If it is required
     * but omitted, a {@link DSL#trueCondition()}, i.e. <code>1 = 1</code>
     * condition will be rendered
     */
    @Support
    TableOptionalOnStep join(TableLike<?> table, JoinType type);

    /**
     * <code>INNER JOIN</code> a table to this table.
     */
    @Support
    @Transition(
        name = "JOIN",
        args = "Table"
    )
    TableOnStep join(TableLike<?> table);

    /**
     * <code>INNER JOIN</code> a table to this table.
     * <p>
     * <b>NOTE</b>: When inserting plain SQL into jOOQ objects, you must
     * guarantee syntax integrity. You may also create the possibility of
     * malicious SQL injection. Be sure to properly use bind variables and/or
     * escape literals when concatenated into SQL clauses!
     *
     * @see DSL#table(String)
     */
    @Support
    @Transition(
        name = "JOIN",
        args = "Table"
    )
    TableOnStep join(String sql);

    /**
     * <code>INNER JOIN</code> a table to this table.
     * <p>
     * <b>NOTE</b>: When inserting plain SQL into jOOQ objects, you must
     * guarantee syntax integrity. You may also create the possibility of
     * malicious SQL injection. Be sure to properly use bind variables and/or
     * escape literals when concatenated into SQL clauses!
     *
     * @see DSL#table(String, Object...)
     */
    @Support
    @Transition(
        name = "JOIN",
        args = "Table"
    )
    TableOnStep join(String sql, Object... bindings);

    /**
     * <code>INNER JOIN</code> a table to this table.
     * <p>
     * <b>NOTE</b>: When inserting plain SQL into jOOQ objects, you must
     * guarantee syntax integrity. You may also create the possibility of
     * malicious SQL injection. Be sure to properly use bind variables and/or
     * escape literals when concatenated into SQL clauses!
     *
     * @see DSL#table(String, QueryPart...)
     */
    @Support
    @Transition(
        name = "JOIN",
        args = "Table"
    )
    TableOnStep join(String sql, QueryPart... parts);

    /**
     * <code>LEFT OUTER JOIN</code> a table to this table.
     */
    @Support
    @Transition(
        name = "LEFT OUTER JOIN",
        args = "Table"
    )
    TablePartitionByStep leftOuterJoin(TableLike<?> table);

    /**
     * <code>LEFT OUTER JOIN</code> a table to this table.
     * <p>
     * <b>NOTE</b>: When inserting plain SQL into jOOQ objects, you must
     * guarantee syntax integrity. You may also create the possibility of
     * malicious SQL injection. Be sure to properly use bind variables and/or
     * escape literals when concatenated into SQL clauses!
     *
     * @see DSL#table(String)
     */
    @Support
    @Transition(
        name = "LEFT OUTER JOIN",
        args = "Table"
    )
    TablePartitionByStep leftOuterJoin(String sql);

    /**
     * <code>LEFT OUTER JOIN</code> a table to this table.
     * <p>
     * <b>NOTE</b>: When inserting plain SQL into jOOQ objects, you must
     * guarantee syntax integrity. You may also create the possibility of
     * malicious SQL injection. Be sure to properly use bind variables and/or
     * escape literals when concatenated into SQL clauses!
     *
     * @see DSL#table(String, Object...)
     */
    @Support
    @Transition(
        name = "LEFT OUTER JOIN",
        args = "Table"
    )
    TablePartitionByStep leftOuterJoin(String sql, Object... bindings);

    /**
     * <code>LEFT OUTER JOIN</code> a table to this table.
     * <p>
     * <b>NOTE</b>: When inserting plain SQL into jOOQ objects, you must
     * guarantee syntax integrity. You may also create the possibility of
     * malicious SQL injection. Be sure to properly use bind variables and/or
     * escape literals when concatenated into SQL clauses!
     *
     * @see DSL#table(String, QueryPart...)
     */
    @Support
    @Transition(
        name = "LEFT OUTER JOIN",
        args = "Table"
    )
    TablePartitionByStep leftOuterJoin(String sql, QueryPart... parts);

    /**
     * <code>RIGHT OUTER JOIN</code> a table to this table.
     * <p>
     * This is only possible where the underlying RDBMS supports it
     */
    @Support({ CUBRID, DERBY, FIREBIRD, H2, HSQLDB, MARIADB, MYSQL, POSTGRES })
    @Transition(
        name = "RIGHT OUTER JOIN",
        args = "Table"
    )
    TablePartitionByStep rightOuterJoin(TableLike<?> table);

    /**
     * <code>RIGHT OUTER JOIN</code> a table to this table.
     * <p>
     * This is only possible where the underlying RDBMS supports it
     * <p>
     * <b>NOTE</b>: When inserting plain SQL into jOOQ objects, you must
     * guarantee syntax integrity. You may also create the possibility of
     * malicious SQL injection. Be sure to properly use bind variables and/or
     * escape literals when concatenated into SQL clauses!
     *
     * @see DSL#table(String)
     */
    @Support({ CUBRID, DERBY, FIREBIRD, H2, HSQLDB, MARIADB, MYSQL, POSTGRES })
    @Transition(
        name = "RIGHT OUTER JOIN",
        args = "Table"
    )
    TablePartitionByStep rightOuterJoin(String sql);

    /**
     * <code>RIGHT OUTER JOIN</code> a table to this table.
     * <p>
     * This is only possible where the underlying RDBMS supports it
     * <p>
     * <b>NOTE</b>: When inserting plain SQL into jOOQ objects, you must
     * guarantee syntax integrity. You may also create the possibility of
     * malicious SQL injection. Be sure to properly use bind variables and/or
     * escape literals when concatenated into SQL clauses!
     *
     * @see DSL#table(String, Object...)
     */
    @Support({ CUBRID, DERBY, FIREBIRD, H2, HSQLDB, MARIADB, MYSQL, POSTGRES })
    @Transition(
        name = "RIGHT OUTER JOIN",
        args = "Table"
    )
    TablePartitionByStep rightOuterJoin(String sql, Object... bindings);

    /**
     * <code>RIGHT OUTER JOIN</code> a table to this table.
     * <p>
     * This is only possible where the underlying RDBMS supports it
     * <p>
     * <b>NOTE</b>: When inserting plain SQL into jOOQ objects, you must
     * guarantee syntax integrity. You may also create the possibility of
     * malicious SQL injection. Be sure to properly use bind variables and/or
     * escape literals when concatenated into SQL clauses!
     *
     * @see DSL#table(String, QueryPart...)
     */
    @Support({ CUBRID, DERBY, FIREBIRD, H2, HSQLDB, MARIADB, MYSQL, POSTGRES })
    @Transition(
        name = "RIGHT OUTER JOIN",
        args = "Table"
    )
    TablePartitionByStep rightOuterJoin(String sql, QueryPart... parts);

    /**
     * <code>FULL OUTER JOIN</code> a table to this table.
     * <p>
     * This is only possible where the underlying RDBMS supports it
     */
    @Support({ FIREBIRD, HSQLDB, POSTGRES })
    @Transition(
        name = "FULL OUTER JOIN",
        args = "Table"
    )
    TableOnStep fullOuterJoin(TableLike<?> table);

    /**
     * <code>FULL OUTER JOIN</code> a table to this table.
     * <p>
     * This is only possible where the underlying RDBMS supports it
     * <p>
     * <b>NOTE</b>: When inserting plain SQL into jOOQ objects, you must
     * guarantee syntax integrity. You may also create the possibility of
     * malicious SQL injection. Be sure to properly use bind variables and/or
     * escape literals when concatenated into SQL clauses!
     *
     * @see DSL#table(String)
     */
    @Support({ FIREBIRD, HSQLDB, POSTGRES })
    @Transition(
        name = "FULL OUTER JOIN",
        args = "Table"
    )
    TableOnStep fullOuterJoin(String sql);

    /**
     * <code>FULL OUTER JOIN</code> a table to this table.
     * <p>
     * This is only possible where the underlying RDBMS supports it
     * <p>
     * <b>NOTE</b>: When inserting plain SQL into jOOQ objects, you must
     * guarantee syntax integrity. You may also create the possibility of
     * malicious SQL injection. Be sure to properly use bind variables and/or
     * escape literals when concatenated into SQL clauses!
     *
     * @see DSL#table(String, Object...)
     */
    @Support({ FIREBIRD, HSQLDB, POSTGRES })
    @Transition(
        name = "FULL OUTER JOIN",
        args = "Table"
    )
    TableOnStep fullOuterJoin(String sql, Object... bindings);

    /**
     * <code>FULL OUTER JOIN</code> a table to this table.
     * <p>
     * This is only possible where the underlying RDBMS supports it
     * <p>
     * <b>NOTE</b>: When inserting plain SQL into jOOQ objects, you must
     * guarantee syntax integrity. You may also create the possibility of
     * malicious SQL injection. Be sure to properly use bind variables and/or
     * escape literals when concatenated into SQL clauses!
     *
     * @see DSL#table(String, QueryPart...)
     */
    @Support({ FIREBIRD, HSQLDB, POSTGRES })
    @Transition(
        name = "FULL OUTER JOIN",
        args = "Table"
    )
    TableOnStep fullOuterJoin(String sql, QueryPart... parts);

    /**
     * <code>CROSS JOIN</code> a table to this table.
     * <p>
     * If this syntax is unavailable, it is simulated with a regular
     * <code>INNER JOIN</code>. The following two constructs are equivalent:
     * <code><pre>
     * A cross join B
     * A join B on 1 = 1
     * </pre></code>
     */
    @Support({ CUBRID, DERBY, FIREBIRD, H2, HSQLDB, MARIADB, MYSQL, POSTGRES, SQLITE })
    @Transition(
        name = "CROSS JOIN",
        args = "Table",
        to = "JoinedTable"
    )
    Table<Record> crossJoin(TableLike<?> table);

    /**
     * <code>CROSS JOIN</code> a table to this table.
     * <p>
     * If this syntax is unavailable, it is simulated with a regular
     * <code>INNER JOIN</code>. The following two constructs are equivalent:
     * <code><pre>
     * A cross join B
     * A join B on 1 = 1
     * </pre></code>
     * <p>
     * <b>NOTE</b>: When inserting plain SQL into jOOQ objects, you must
     * guarantee syntax integrity. You may also create the possibility of
     * malicious SQL injection. Be sure to properly use bind variables and/or
     * escape literals when concatenated into SQL clauses!
     *
     * @see DSL#table(String)
     */
    @Support({ CUBRID, DERBY, FIREBIRD, H2, HSQLDB, MARIADB, MYSQL, POSTGRES, SQLITE })
    @Transition(
        name = "CROSS JOIN",
        args = "Table",
        to = "JoinedTable"
    )
    Table<Record> crossJoin(String sql);

    /**
     * <code>CROSS JOIN</code> a table to this table.
     * <p>
     * If this syntax is unavailable, it is simulated with a regular
     * <code>INNER JOIN</code>. The following two constructs are equivalent:
     * <code><pre>
     * A cross join B
     * A join B on 1 = 1
     * </pre></code>
     * <p>
     * <b>NOTE</b>: When inserting plain SQL into jOOQ objects, you must
     * guarantee syntax integrity. You may also create the possibility of
     * malicious SQL injection. Be sure to properly use bind variables and/or
     * escape literals when concatenated into SQL clauses!
     *
     * @see DSL#table(String, Object...)
     */
    @Support({ CUBRID, DERBY, FIREBIRD, H2, HSQLDB, MARIADB, MYSQL, POSTGRES, SQLITE })
    @Transition(
        name = "CROSS JOIN",
        args = "Table",
        to = "JoinedTable"
    )
    Table<Record> crossJoin(String sql, Object... bindings);

    /**
     * <code>CROSS JOIN</code> a table to this table.
     * <p>
     * If this syntax is unavailable, it is simulated with a regular
     * <code>INNER JOIN</code>. The following two constructs are equivalent:
     * <code><pre>
     * A cross join B
     * A join B on 1 = 1
     * </pre></code>
     * <p>
     * <b>NOTE</b>: When inserting plain SQL into jOOQ objects, you must
     * guarantee syntax integrity. You may also create the possibility of
     * malicious SQL injection. Be sure to properly use bind variables and/or
     * escape literals when concatenated into SQL clauses!
     *
     * @see DSL#table(String, QueryPart...)
     */
    @Support({ CUBRID, DERBY, FIREBIRD, H2, HSQLDB, MARIADB, MYSQL, POSTGRES, SQLITE })
    @Transition(
        name = "CROSS JOIN",
        args = "Table",
        to = "JoinedTable"
    )
    Table<Record> crossJoin(String sql, QueryPart... parts);

    /**
     * <code>NATURAL JOIN</code> a table to this table.
     * <p>
     * If this is not supported by your RDBMS, then jOOQ will try to simulate
     * this behaviour using the information provided in this query.
     */
    @Support
    @Transition(
        name = "NATURAL JOIN",
        args = "Table",
        to = "JoinedTable"
    )
    Table<Record> naturalJoin(TableLike<?> table);

    /**
     * <code>NATURAL JOIN</code> a table to this table.
     * <p>
     * If this is not supported by your RDBMS, then jOOQ will try to simulate
     * this behaviour using the information provided in this query.
     * <p>
     * <b>NOTE</b>: When inserting plain SQL into jOOQ objects, you must
     * guarantee syntax integrity. You may also create the possibility of
     * malicious SQL injection. Be sure to properly use bind variables and/or
     * escape literals when concatenated into SQL clauses!
     *
     * @see DSL#table(String)
     */
    @Support
    @Transition(
        name = "NATURAL JOIN",
        args = "Table",
        to = "JoinedTable"
    )
    Table<Record> naturalJoin(String sql);

    /**
     * <code>NATURAL JOIN</code> a table to this table.
     * <p>
     * If this is not supported by your RDBMS, then jOOQ will try to simulate
     * this behaviour using the information provided in this query.
     * <p>
     * <b>NOTE</b>: When inserting plain SQL into jOOQ objects, you must
     * guarantee syntax integrity. You may also create the possibility of
     * malicious SQL injection. Be sure to properly use bind variables and/or
     * escape literals when concatenated into SQL clauses!
     *
     * @see DSL#table(String, Object...)
     */
    @Support
    @Transition(
        name = "NATURAL JOIN",
        args = "Table",
        to = "JoinedTable"
    )
    Table<Record> naturalJoin(String sql, Object... bindings);

    /**
     * <code>NATURAL JOIN</code> a table to this table.
     * <p>
     * If this is not supported by your RDBMS, then jOOQ will try to simulate
     * this behaviour using the information provided in this query.
     * <p>
     * <b>NOTE</b>: When inserting plain SQL into jOOQ objects, you must
     * guarantee syntax integrity. You may also create the possibility of
     * malicious SQL injection. Be sure to properly use bind variables and/or
     * escape literals when concatenated into SQL clauses!
     *
     * @see DSL#table(String, QueryPart...)
     */
    @Support
    @Transition(
        name = "NATURAL JOIN",
        args = "Table",
        to = "JoinedTable"
    )
    Table<Record> naturalJoin(String sql, QueryPart... parts);

    /**
     * <code>NATURAL LEFT OUTER JOIN</code> a table to this table.
     * <p>
     * If this is not supported by your RDBMS, then jOOQ will try to simulate
     * this behaviour using the information provided in this query.
     */
    @Support
    @Transition(
        name = "NATURAL LEFT OUTER JOIN",
        args = "Table",
        to = "JoinedTable"
    )
    Table<Record> naturalLeftOuterJoin(TableLike<?> table);

    /**
     * <code>NATURAL LEFT OUTER JOIN</code> a table to this table.
     * <p>
     * If this is not supported by your RDBMS, then jOOQ will try to simulate
     * this behaviour using the information provided in this query.
     * <p>
     * <b>NOTE</b>: When inserting plain SQL into jOOQ objects, you must
     * guarantee syntax integrity. You may also create the possibility of
     * malicious SQL injection. Be sure to properly use bind variables and/or
     * escape literals when concatenated into SQL clauses!
     *
     * @see DSL#table(String)
     */
    @Support
    @Transition(
        name = "NATURAL LEFT OUTER JOIN",
        args = "Table",
        to = "JoinedTable"
    )
    Table<Record> naturalLeftOuterJoin(String sql);

    /**
     * <code>NATURAL LEFT OUTER JOIN</code> a table to this table.
     * <p>
     * If this is not supported by your RDBMS, then jOOQ will try to simulate
     * this behaviour using the information provided in this query.
     * <p>
     * <b>NOTE</b>: When inserting plain SQL into jOOQ objects, you must
     * guarantee syntax integrity. You may also create the possibility of
     * malicious SQL injection. Be sure to properly use bind variables and/or
     * escape literals when concatenated into SQL clauses!
     *
     * @see DSL#table(String, Object...)
     */
    @Support
    @Transition(
        name = "NATURAL LEFT OUTER JOIN",
        args = "Table",
        to = "JoinedTable"
    )
    Table<Record> naturalLeftOuterJoin(String sql, Object... bindings);

    /**
     * <code>NATURAL LEFT OUTER JOIN</code> a table to this table.
     * <p>
     * If this is not supported by your RDBMS, then jOOQ will try to simulate
     * this behaviour using the information provided in this query.
     * <p>
     * <b>NOTE</b>: When inserting plain SQL into jOOQ objects, you must
     * guarantee syntax integrity. You may also create the possibility of
     * malicious SQL injection. Be sure to properly use bind variables and/or
     * escape literals when concatenated into SQL clauses!
     *
     * @see DSL#table(String, QueryPart...)
     */
    @Support
    @Transition(
        name = "NATURAL LEFT OUTER JOIN",
        args = "Table",
        to = "JoinedTable"
    )
    Table<Record> naturalLeftOuterJoin(String sql, QueryPart... parts);

    /**
     * <code>NATURAL RIGHT OUTER JOIN</code> a table to this table.
     * <p>
     * If this is not supported by your RDBMS, then jOOQ will try to simulate
     * this behaviour using the information provided in this query.
     */
    @Support({ CUBRID, DERBY, FIREBIRD, H2, HSQLDB, MARIADB, MYSQL, POSTGRES })
    @Transition(
        name = "NATURAL RIGHT OUTER JOIN",
        args = "Table",
        to = "JoinedTable"
    )
    Table<Record> naturalRightOuterJoin(TableLike<?> table);

    /**
     * <code>NATURAL RIGHT OUTER JOIN</code> a table to this table.
     * <p>
     * If this is not supported by your RDBMS, then jOOQ will try to simulate
     * this behaviour using the information provided in this query.
     * <p>
     * <b>NOTE</b>: When inserting plain SQL into jOOQ objects, you must
     * guarantee syntax integrity. You may also create the possibility of
     * malicious SQL injection. Be sure to properly use bind variables and/or
     * escape literals when concatenated into SQL clauses!
     *
     * @see DSL#table(String)
     */
    @Support({ CUBRID, DERBY, FIREBIRD, H2, HSQLDB, MARIADB, MYSQL, POSTGRES })
    @Transition(
        name = "NATURAL RIGHT OUTER JOIN",
        args = "Table",
        to = "JoinedTable"
    )
    Table<Record> naturalRightOuterJoin(String sql);

    /**
     * <code>NATURAL RIGHT OUTER JOIN</code> a table to this table.
     * <p>
     * If this is not supported by your RDBMS, then jOOQ will try to simulate
     * this behaviour using the information provided in this query.
     * <p>
     * <b>NOTE</b>: When inserting plain SQL into jOOQ objects, you must
     * guarantee syntax integrity. You may also create the possibility of
     * malicious SQL injection. Be sure to properly use bind variables and/or
     * escape literals when concatenated into SQL clauses!
     *
     * @see DSL#table(String, Object...)
     */
    @Support({ CUBRID, DERBY, FIREBIRD, H2, HSQLDB, MARIADB, MYSQL, POSTGRES })
    @Transition(
        name = "NATURAL RIGHT OUTER JOIN",
        args = "Table",
        to = "JoinedTable"
    )
    Table<Record> naturalRightOuterJoin(String sql, Object... bindings);

    /**
     * <code>NATURAL RIGHT OUTER JOIN</code> a table to this table.
     * <p>
     * If this is not supported by your RDBMS, then jOOQ will try to simulate
     * this behaviour using the information provided in this query.
     * <p>
     * <b>NOTE</b>: When inserting plain SQL into jOOQ objects, you must
     * guarantee syntax integrity. You may also create the possibility of
     * malicious SQL injection. Be sure to properly use bind variables and/or
     * escape literals when concatenated into SQL clauses!
     *
     * @see DSL#table(String, QueryPart...)
     */
    @Support({ CUBRID, DERBY, FIREBIRD, H2, HSQLDB, MARIADB, MYSQL, POSTGRES })
    @Transition(
        name = "NATURAL RIGHT OUTER JOIN",
        args = "Table",
        to = "JoinedTable"
    )
    Table<Record> naturalRightOuterJoin(String sql, QueryPart... parts);

    // -------------------------------------------------------------------------
    // XXX: APPLY clauses on tables
    // -------------------------------------------------------------------------

    /* [pro] xx

    xxx
     x xxxxxxxxxxx xxxxxxxxxxxx x xxxxx xx xxxx xxxxxx
     xx
    xxxxxxxxxx xxxxxxxxxx xxxxxxxxxx xxxxxx xx
    xxxxxxxxxxxx
        xxxx x xxxxxx xxxxxxx
        xxxx x xxxxxxxx
        xx x xxxxxxxxxxxxx
    x
    xxxxxxxxxxxxx xxxxxxxxxxxxxxxxxxxxxxx xxxxxxx

    xxx
     x xxxxxxxxxxx xxxxxxxxxxxx x xxxxx xx xxxx xxxxxx
     x xxx
     x xxxxxxxxxxxx xxxx xxxxxxxxx xxxxx xxx xxxx xxxx xxxxxxxx xxx xxxx
     x xxxxxxxxx xxxxxx xxxxxxxxxx xxx xxx xxxx xxxxxx xxx xxxxxxxxxxx xx
     x xxxxxxxxx xxx xxxxxxxxxx xx xxxx xx xxxxxxxx xxx xxxx xxxxxxxxx xxxxxx
     x xxxxxx xxxxxxxx xxxx xxxxxxxxxxxx xxxx xxx xxxxxxxx
     x
     x xxxx xxxxxxxxxxxxxxxxx
     xx
    xxxxxxxxxx xxxxxxxxxx xxxxxxxxxx xxxxxx xx
    xxxxxxxxxxxx
        xxxx x xxxxxx xxxxxxx
        xxxx x xxxxxxxx
        xx x xxxxxxxxxxxxx
    x
    xxxxxxxxxxxxx xxxxxxxxxxxxxxxxx xxxxx

    xxx
     x xxxxxxxxxxx xxxxxxxxxxxx x xxxxx xx xxxx xxxxxx
     x xxx
     x xxxxxxxxxxxx xxxx xxxxxxxxx xxxxx xxx xxxx xxxx xxxxxxxx xxx xxxx
     x xxxxxxxxx xxxxxx xxxxxxxxxx xxx xxx xxxx xxxxxx xxx xxxxxxxxxxx xx
     x xxxxxxxxx xxx xxxxxxxxxx xx xxxx xx xxxxxxxx xxx xxxx xxxxxxxxx xxxxxx
     x xxxxxx xxxxxxxx xxxx xxxxxxxxxxxx xxxx xxx xxxxxxxx
     x
     x xxxx xxxxxxxxxxxxxxxxx xxxxxxxxxx
     xx
    xxxxxxxxxx xxxxxxxxxx xxxxxxxxxx xxxxxx xx
    xxxxxxxxxxxx
        xxxx x xxxxxx xxxxxxx
        xxxx x xxxxxxxx
        xx x xxxxxxxxxxxxx
    x
    xxxxxxxxxxxxx xxxxxxxxxxxxxxxxx xxxx xxxxxxxxx xxxxxxxxxx

    xxx
     x xxxxxxxxxxx xxxxxxxxxxxx x xxxxx xx xxxx xxxxxx
     x xxx
     x xxxxxxxxxxxx xxxx xxxxxxxxx xxxxx xxx xxxx xxxx xxxxxxxx xxx xxxx
     x xxxxxxxxx xxxxxx xxxxxxxxxx xxx xxx xxxx xxxxxx xxx xxxxxxxxxxx xx
     x xxxxxxxxx xxx xxxxxxxxxx xx xxxx xx xxxxxxxx xxx xxxx xxxxxxxxx xxxxxx
     x xxxxxx xxxxxxxx xxxx xxxxxxxxxxxx xxxx xxx xxxxxxxx
     x
     x xxxx xxxxxxxxxxxxxxxxx xxxxxxxxxxxxx
     xx
    xxxxxxxxxx xxxxxxxxxx xxxxxxxxxx xxxxxx xx
    xxxxxxxxxxxx
        xxxx x xxxxxx xxxxxxx
        xxxx x xxxxxxxx
        xx x xxxxxxxxxxxxx
    x
    xxxxxxxxxxxxx xxxxxxxxxxxxxxxxx xxxx xxxxxxxxxxxx xxxxxxx

    xxx
     x xxxxxxxxxxx xxxxxxxxxxxx x xxxxx xx xxxx xxxxxx
     xx
    xxxxxxxxxx xxxxxxxxxx xxxxxxxxxx xxxxxx xx
    xxxxxxxxxxxx
        xxxx x xxxxxx xxxxxxx
        xxxx x xxxxxxxx
        xx x xxxxxxxxxxxxx
    x
    xxxxxxxxxxxxx xxxxxxxxxxxxxxxxxxxxxxx xxxxxxx

    xxx
     x xxxxxxxxxxx xxxxxxxxxxxx x xxxxx xx xxxx xxxxxx
     x xxx
     x xxxxxxxxxxxx xxxx xxxxxxxxx xxxxx xxx xxxx xxxx xxxxxxxx xxx xxxx
     x xxxxxxxxx xxxxxx xxxxxxxxxx xxx xxx xxxx xxxxxx xxx xxxxxxxxxxx xx
     x xxxxxxxxx xxx xxxxxxxxxx xx xxxx xx xxxxxxxx xxx xxxx xxxxxxxxx xxxxxx
     x xxxxxx xxxxxxxx xxxx xxxxxxxxxxxx xxxx xxx xxxxxxxx
     x
     x xxxx xxxxxxxxxxxxxxxxx
     xx
    xxxxxxxxxx xxxxxxxxxx xxxxxxxxxx xxxxxx xx
    xxxxxxxxxxxx
        xxxx x xxxxxx xxxxxxx
        xxxx x xxxxxxxx
        xx x xxxxxxxxxxxxx
    x
    xxxxxxxxxxxxx xxxxxxxxxxxxxxxxx xxxxx

    xxx
     x xxxxxxxxxxx xxxxxxxxxxxx x xxxxx xx xxxx xxxxxx
     x xxx
     x xxxxxxxxxxxx xxxx xxxxxxxxx xxxxx xxx xxxx xxxx xxxxxxxx xxx xxxx
     x xxxxxxxxx xxxxxx xxxxxxxxxx xxx xxx xxxx xxxxxx xxx xxxxxxxxxxx xx
     x xxxxxxxxx xxx xxxxxxxxxx xx xxxx xx xxxxxxxx xxx xxxx xxxxxxxxx xxxxxx
     x xxxxxx xxxxxxxx xxxx xxxxxxxxxxxx xxxx xxx xxxxxxxx
     x
     x xxxx xxxxxxxxxxxxxxxxx xxxxxxxxxx
     xx
    xxxxxxxxxx xxxxxxxxxx xxxxxxxxxx xxxxxx xx
    xxxxxxxxxxxx
        xxxx x xxxxxx xxxxxxx
        xxxx x xxxxxxxx
        xx x xxxxxxxxxxxxx
    x
    xxxxxxxxxxxxx xxxxxxxxxxxxxxxxx xxxx xxxxxxxxx xxxxxxxxxx

    xxx
     x xxxxxxxxxxx xxxxxxxxxxxx x xxxxx xx xxxx xxxxxx
     x xxx
     x xxxxxxxxxxxx xxxx xxxxxxxxx xxxxx xxx xxxx xxxx xxxxxxxx xxx xxxx
     x xxxxxxxxx xxxxxx xxxxxxxxxx xxx xxx xxxx xxxxxx xxx xxxxxxxxxxx xx
     x xxxxxxxxx xxx xxxxxxxxxx xx xxxx xx xxxxxxxx xxx xxxx xxxxxxxxx xxxxxx
     x xxxxxx xxxxxxxx xxxx xxxxxxxxxxxx xxxx xxx xxxxxxxx
     x
     x xxxx xxxxxxxxxxxxxxxxx xxxxxxxxxxxxx
     xx
    xxxxxxxxxx xxxxxxxxxx xxxxxxxxxx xxxxxx xx
    xxxxxxxxxxxx
        xxxx x xxxxxx xxxxxxx
        xxxx x xxxxxxxx
        xx x xxxxxxxxxxxxx
    x
    xxxxxxxxxxxxx xxxxxxxxxxxxxxxxx xxxx xxxxxxxxxxxx xxxxxxx

    xx [/pro] */

    // -------------------------------------------------------------------------
    // XXX: Exotic and vendor-specific clauses on tables
    // -------------------------------------------------------------------------

    /* [pro] xx
    xxx
     x xxxxxxx x xxx xxxxxx xxxxx xxxxx xxxx xxx xxxxx xxxxxxxxxxxx
     x xxx
     x xxxx xxxxx xxxxx xxxx xx xx xxxxxx xxxxxxxx xxxxx xx xxxxx xxx xx xxxxx
     x xxxxxxxx xxxxx xxxxxxxx
     x xxx
     x xxxxxxxx
     x xxx
     x xxxxxxxxxxx
     x xxxxxxxxxxxxxxx
     x       xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx
     x       xxxxxxxxx
     x xxxxxxxxxxxxx
     x xxx
     x xxx xxxxxx xxxxxxxxxxxxxxxxxx xxxxx xxxxxx xxx
     x xxxxxx xxxxxxxxxxxxxxxxxxxxxxxxxxxx xxx xxxxxx xxxxxxxxxxxxxxxxxxxxxxxxxxxx
     x
     x xxxx xx
     x      xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx
     x xxxx xxxxxxxxxxxxxxxxxxxxxxxxxxx
     x xxxx xxxxxxxxxxxxxxxxxxxxxxxxxxx
     xx
    xxxxxxxxxx xxxxxxxxxx xxxxxx xx
    xxxxxxxxxxxx
        xxxx x xxxxxxx
        xxxx x xxxxxxxx
    x
    xxxxxxxx xxxxxxxxxxx xxxxxx

    xxx
     x xxxxxx x xxx xxxxxxxxxxxxxxxxxx xxxxxxxxx xxxx xxxx xxxxxx xxxxxxxx xx
     x xxxx xxxxxxx xxxx
     x xxx
     x xxxx xxx xxxx xxxxxxxx xx xxxx xxxx
     x xxxx
     x xxxx xxxxxx xxxxxxxxxxxxxxxxxxxxx xxxxxxxxxxxx
     x xxxx xxxxxx xxxxxxxxxxxxxxxxxxxxx xxxx xxx xxxxxxxxxx xxxxxxxxxxxxxxx
     x xxxxxxxxx xxxxxxxx xx xxxxx xxxx xxxxx xx xxxxxxxxxx xxxx xxx xxxxxxxxxx
     x xxxxxxxxxxxxxxx
     x xxxxx
     x
     x xxxxxx xxxxxxxxxxxxxxxxxx xxx xxxxxxxxx xxxxxxxxx xxxx xxx xxxxxxxxx
     x xxxxxxx x xxx xxxxxx xx xxxxxx xxx xxxxxxxxxxxxxxxxxx xxxxxxxxxx
     xx
    xxxxxxxxxx xxxxxxxxxx xxxxxxxxx xx
    xxxxxxxxxxxx
        xxxx x xxxxxxxx
        xxxx x xxxxxxxx
    x
    xxxxxxxxxxxx xxxxxxxxxxxxxxxxx xxxxxxxxxxxxxxxxxxxx

    xxx
     x xxxxxx x xxx xxxxxxxxxxxxxxxxxx xxxxxxxxx xxxx xxxx xxxxxx xxxxxxxx xx
     x xxxx xxxxxxx xxxx
     x xxx
     x xxx xxxx xxxxxxxx xxx xxxxxx xxxxxxxxxxxxxxxxx
     x
     x xxxxxx xxxxxxxxxxxxxxxxxx xxx xxxxxxxxx xxxxxxxxx xxxx xxx xxxxxxxxx
     x xxxxxxx x xxx xxxxxx xx xxxxxx xxx xxxxxxxxxxxxxxxxxx xxxxxxxxxx
     x xxxx xxxxxxxxxxxxxxxx
     xx
    xxxxxxxxxx xxxxxxxxxx xxxxxxxxx xx
    xxxxxxxxxxxx
        xxxx x xxxxxxxx
        xxxx x xxxxxxxx
    x
    xxxxxxxxxxxx xxxxxxxxxxxxxxxxxx xxxxxxx xxxxxxxxx xxxxxxxxxxxxxxxxxxxx

    xx [/pro] */
    /**
     * Create a new <code>TABLE</code> reference from this table, applying
     * relational division.
     * <p>
     * Relational division is the inverse of a cross join operation. The
     * following is an approximate definition of a relational division:
     * <code><pre>
     * Assume the following cross join / cartesian product
     * C = A × B
     *
     * Then it can be said that
     * A = C ÷ B
     * B = C ÷ A
     * </pre></code>
     * <p>
     * With jOOQ, you can simplify using relational divisions by using the
     * following syntax: <code><pre>
     * C.divideBy(B).on(C.ID.equal(B.C_ID)).returning(C.TEXT)
     * </pre></code>
     * <p>
     * The above roughly translates to <code><pre>
     * SELECT DISTINCT C.TEXT FROM C "c1"
     * WHERE NOT EXISTS (
     *   SELECT 1 FROM B
     *   WHERE NOT EXISTS (
     *     SELECT 1 FROM C "c2"
     *     WHERE "c2".TEXT = "c1".TEXT
     *     AND "c2".ID = B.C_ID
     *   )
     * )
     * </pre></code>
     * <p>
     * Or in plain text: Find those TEXT values in C whose ID's correspond to
     * all ID's in B. Note that from the above SQL statement, it is immediately
     * clear that proper indexing is of the essence. Be sure to have indexes on
     * all columns referenced from the <code>on(...)</code> and
     * <code>returning(...)</code> clauses.
     * <p>
     * For more information about relational division and some nice, real-life
     * examples, see
     * <ul>
     * <li><a
     * href="http://en.wikipedia.org/wiki/Relational_algebra#Division">http
     * ://en.wikipedia.org/wiki/Relational_algebra#Division</a></li>
     * <li><a href=
     * "http://www.simple-talk.com/sql/t-sql-programming/divided-we-stand-the-sql-of-relational-division/"
     * >http://www.simple-talk.com/sql/t-sql-programming/divided-we-stand-the-
     * sql-of-relational-division/</a></li>
     * </ul>
     * <p>
     * This has been observed to work with all dialects
     */
    @Support
    @Transition(
        name = "DIVIDE BY",
        args = "Table"
    )
    DivideByOnStep divideBy(Table<?> divisor);

    /* [pro] xx
    xxx
     x xxxxxx xx xxxxxx xxxxxxxxxxxxxxxxxx xxxxxxxxx xxxxxxxx xxxxx xxxxxx xxxx
     x xxxx xxxxxx
     xx
    xxxxxxxxxx xxxxxx xx
    xxxxxxxxxxxx
        xxxx x xxxxxxxxx xxxxxxx xxxxx
        xxxx x xxxxxxx
    x
    xxxxxxxxxxxxxxxxxxxxxxxxx xxxxxxx xxxxxxxxxxxxxxxxxxxxxxxxx xxxxx

    xxx
     x xxxxxx xx xxxxxx xxxxxxxxxxxxxxxxxx xxxxxxxxx xxxxxxxx xxxxx xxxxxx xxxx
     x xxxx xxxxxx
     xx
    xxxxxxxxxx xxxxxx xx
    xxxxxxxxxxxx
        xxxx x xxxxxxxxx xxxxxxx xxxxx
        xxxx x xxxxxxx
    x
    xxxxxxxxxxxxxxxxxxxxxxxxx xxxxxxx xxxxxxxxxxxxxxxxxxxxxxxxxx xxxxxxx xxxxxxx xxxxx

    xxx
     x xxxxxx xx xxxxxx xxxxxxxxxxxxxxxxxx xxxxxxxxx xxxxxxxx xxxxx xxxxxx xxxx
     x xxxx xxxxxx
     xx
    xxxxxxxxxx xxxxxx xx
    xxxxxxxxxxxx
        xxxx x xxxxxxxxx xxxxxxx xxx xxxxxxxxx
    x
    xxxxxxxxxxxxxxxxxxxxxxxxx xxxxxxx xxxxxxxxxxxxxxxxxxxxxxxxxxxxx

    xxx
     x xxxxxx xx xxxxxx xxxxxxxxxxxxxxxxxx xxxxxxxxx xxxxxxxx xxxxx xxxxxx xxxx
     x xxxx xxxxxx
     xx
    xxxxxxxxxx xxxxxx xx
    xxxxxxxxxxxx
        xxxx x xxxxxxxxx xxxxxxx xxxxxxxxxxx
        xxxx x xxxxxxx
    x
    xxxxxxxxxxxxxxxxxxxxxxxxx xxxxxxxxxx xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx xxxxxxxxxxx

    xxx
     x xxxxxx xx xxxxxx xxxxxxxxxxxxxxxxxx xxxxxxxxx xxxxxxxx xxxxx xxxxxx xxxx
     x xxxx xxxxxx
     xx
    xxxxxxxxxx xxxxxx xx
    xxxxxxxxxxxx
        xxxx x xxxxxxxxx xxxxxxx xxxxxxxxxxx
        xxxx x xxxxxxx
    x
    xxxxxxxxxxxxxxxxxxxxxxxxx xxxxxxxxxx xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx xxxxxxxxxxx

    xxx
     x xxxxxx xx xxxxxx xxxxxxxxxxxxxxxxxx xxxxxxxxx xxxxxxxx xxxxx xxxxxx xxxx
     x xxxx xxxxxx
     xx
    xxxxxxxxxx xxxxxx xx
    xxxxxxxxxxxx
        xxxx x xxxxxxxxx xxxxxxx xxxxxxxxx xxxxxxxxx
    x
    xxxxxxxxxxxxxxxxxxxxxxxxx xxxxxxxxxx xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx

    xxx
     x xxxxxx xx xxxxxx xxxxxxxxxxxxxxxxxx xxxxxxxxx xxxxx xxxxxx xxxx xxxx
     x xxxxxx
     xx
    xxxxxxxxxx xxxxxx xx
    xxxxxxxxxxxx
        xxxx x xxx xx xxxxx
        xxxx x xxxxxxx
    x
    xxxxxxxx xxxxxxxxxxxxxx xxxxx

    xxx
     x xxxxxx xx xxxxxx xxxxxxxxxxxxxxxxxx xxxxxxxxx xxxxx xxxxxx xxxx xxxx
     x xxxxxx
     xx
    xxxxxxxxxx xxxxxx xx
    xxxxxxxxxxxx
        xxxx x xxx xx xxxxx
        xxxx x xxxxxxx
    x
    xxxxxxxx xxxxxxxxxxxxxxx xxxxxxx xxxxxxx xxxxx

    xxx
     x xxxxxx xx xxxxxx xxxxxxxxxxxxxxxxxx xxxxxxxxx xxxxx xxxxxx xxxx xxxx
     x xxxxxx
     xx
    xxxxxxxxxx xxxxxx xx
    xxxxxxxxxxxx
        xxxx x xxx xx xxxxxxxxxxx
        xxxx x xxxxxxx
    x
    xxxxxxxx xxxxxxxxxxxxxxxxxxxxxxx xxxxxxxxxxx

    xxx
     x xxxxxx xx xxxxxx xxxxxxxxxxxxxxxxxx xxxxxxxxx xxxxx xxxxxx xxxx xxxx
     x xxxxxx
     xx
    xxxxxxxxxx xxxxxx xx
    xxxxxxxxxxxx
        xxxx x xxx xx xxxxxxxxxxx
        xxxx x xxxxxxx
    x
    xxxxxxxx xxxxxxxxxxxxxxxxxxxxxxxxxxxxxx xxxxxxxxxxx
    xx [/pro] */
}
