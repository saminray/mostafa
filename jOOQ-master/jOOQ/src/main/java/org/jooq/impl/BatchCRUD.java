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

import static org.jooq.conf.SettingsTools.executeStaticStatements;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.jooq.AttachableInternal;
import org.jooq.Batch;
import org.jooq.BatchBindStep;
import org.jooq.Configuration;
import org.jooq.DSLContext;
import org.jooq.ExecuteContext;
import org.jooq.Query;
import org.jooq.UpdatableRecord;
import org.jooq.exception.ControlFlowSignal;
import org.jooq.exception.DataAccessException;

/**
 * @author Lukas Eder
 */
class BatchCRUD implements Batch {

    /**
     * Generated UID
     */
    private static final long          serialVersionUID = -2935544935267715011L;

    private final DSLContext             create;
    private final Configuration        configuration;
    private final UpdatableRecord<?>[] records;
    private final Action               action;

    BatchCRUD(Configuration configuration, Action action, UpdatableRecord<?>[] records) {
        this.create = DSL.using(configuration);
        this.configuration = configuration;
        this.action = action;
        this.records = records;
    }

    @Override
    public final int size() {
        return records.length;
    }

    @Override
    public final int[] execute() throws DataAccessException {

        // [#1180] Run batch queries with BatchMultiple, if no bind variables
        // should be used...
        if (executeStaticStatements(configuration.settings())) {
            return executeStatic();
        }
        else {
            return executePrepared();
        }
    }

    private final int[] executePrepared() {
        Map<String, List<Query>> queries = new LinkedHashMap<String, List<Query>>();
        QueryCollector collector = new QueryCollector();

        // Add the QueryCollector to intercept query execution after rendering
        Configuration local = configuration.derive(Utils.combine(
            configuration.executeListenerProviders(),
            new DefaultExecuteListenerProvider(collector)
        ));

        // [#1537] Communicate with UpdatableRecordImpl
        local.data(Utils.DATA_OMIT_RETURNING_CLAUSE, true);

        // [#1529] Avoid DEBUG logging of single INSERT / UPDATE statements
        local.settings().setExecuteLogging(false);

        for (int i = 0; i < records.length; i++) {
            Configuration previous = ((AttachableInternal) records[i]).configuration();

            try {
                records[i].attach(local);
                executeAction(i);
            }
            catch (QueryCollectorSignal e) {
                Query query = e.getQuery();
                String sql = e.getSQL();

                // Aggregate executable queries by identical SQL
                if (query.isExecutable()) {
                    List<Query> list = queries.get(sql);

                    if (list == null) {
                        list = new ArrayList<Query>();
                        queries.put(sql, list);
                    }

                    list.add(query);
                }
            }
            finally {
                records[i].attach(previous);
            }
        }

        // Execute one batch statement for each identical SQL statement. Every
        // SQL statement may have several queries with different bind values.
        // The order is preserved as much as possible
        List<Integer> result = new ArrayList<Integer>();
        for (Entry<String, List<Query>> entry : queries.entrySet()) {
            BatchBindStep batch = create.batch(entry.getValue().get(0));

            for (Query query : entry.getValue()) {
                batch.bind(query.getBindValues().toArray());
            }

            int[] array = batch.execute();
            for (int i : array) {
                result.add(i);
            }
        }

        int[] array = new int[result.size()];
        for (int i = 0; i < result.size(); i++) {
            array[i] = result.get(i);
        }

        updateChangedFlag();
        return array;
    }

    private final int[] executeStatic() {
        List<Query> queries = new ArrayList<Query>();
        QueryCollector collector = new QueryCollector();

        Configuration local = configuration.derive(Utils.combine(
            configuration.executeListenerProviders(),
            new DefaultExecuteListenerProvider(collector)
        ));

        for (int i = 0; i < records.length; i++) {
            Configuration previous = ((AttachableInternal) records[i]).configuration();

            try {
                records[i].attach(local);
                executeAction(i);
            }
            catch (QueryCollectorSignal e) {
                Query query = e.getQuery();

                if (query.isExecutable()) {
                    queries.add(query);
                }
            }
            finally {
                records[i].attach(previous);
            }
        }

        // Resulting statements can be batch executed in their requested order
        int[] result = create.batch(queries).execute();
        updateChangedFlag();
        return result;
    }

    private void executeAction(int i) {
        switch (action) {
            case STORE:
                records[i].store();
                break;
            case INSERT:
                records[i].insert();
                break;
            case UPDATE:
                records[i].update();
                break;
            case DELETE:
                records[i].delete();
                break;
        }
    }

    private final void updateChangedFlag() {
        // 1. Deleted records should be marked as changed, such that subsequent
        //    calls to store() will insert them again
        // 2. Stored records should be marked as unchanged
        for (UpdatableRecord<?> record : records) {
            record.changed(action == Action.DELETE);
        }
    }

    /**
     * The action to be performed by this operation
     */
    enum Action {

        /**
         * Corresponds to {@link UpdatableRecord#store()}
         */
        STORE,

        /**
         * Corresponds to {@link UpdatableRecord#insert()}
         */
        INSERT,

        /**
         * Corresponds to {@link UpdatableRecord#update()}
         */
        UPDATE,

        /**
         * Corresponds to {@link UpdatableRecord#delete()}
         */
        DELETE
    }

    /**
     * Collect queries
     * <p>
     * The query collector intercepts query execution after rendering. This
     * allows for rendering SQL according to the specific logic contained in
     * TableRecords without actually executing that SQL
     */
    private static class QueryCollector extends DefaultExecuteListener {

        /**
         * Generated UID
         */
        private static final long serialVersionUID = 7399239846062763212L;

        @Override
        public void renderEnd(ExecuteContext ctx) {
            throw new QueryCollectorSignal(ctx.sql(), ctx.query());
        }
    }

    /**
     * A query execution interception signal.
     * <p>
     * This exception is used as a signal for jOOQ's internals to abort query
     * execution, and return generated SQL back to batch execution.
     */
    private static class QueryCollectorSignal extends ControlFlowSignal {

        /**
         * Generated UID
         */
        private static final long serialVersionUID = -9047250761846931903L;
        private final String      sql;
        private final Query       query;

        QueryCollectorSignal(String sql, Query query) {
            this.sql = sql;
            this.query = query;
        }

        String getSQL() {
            return sql;
        }

        Query getQuery() {
            return query;
        }
    }
}
