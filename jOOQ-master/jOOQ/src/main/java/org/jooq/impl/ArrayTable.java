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

import static org.jooq.impl.DSL.fieldByName;

import java.util.ArrayList;
import java.util.List;

// ...
import org.jooq.Configuration;
import org.jooq.Context;
import org.jooq.Field;
import org.jooq.Param;
import org.jooq.Record;
import org.jooq.Table;
import org.jooq.UDTRecord;
import org.jooq.exception.DataTypeException;
import org.jooq.exception.SQLDialectNotSupportedException;
import org.jooq.util.h2.H2DataType;

/**
 * An unnested array
 *
 * @author Lukas Eder
 */
class ArrayTable extends AbstractTable<Record> {

    /**
     * Generated UID
     */
    private static final long    serialVersionUID = 2380426377794577041L;

    private final Field<?>       array;
    private final Fields<Record> field;
    private final String         alias;

    ArrayTable(Field<?> array) {
        this(array, "array_table");
    }

    @SuppressWarnings({ "unchecked" })
    ArrayTable(Field<?> array, String alias) {
        super(alias);

        Class<?> arrayType;

        // TODO [#523] Solve this in a more object-oriented way...
        if (array.getDataType().getType().isArray()) {
            arrayType = array.getDataType().getType().getComponentType();
        }

        /* [pro] xx
        xx xxxxxxx xxxx xxxxx xx xxxxxxx xxxx xxxxxxxxxxx xx xxxxxx xxxxxx x xxxxx xxxxx
        xxxx xx xxxxxx xxxxxxxxxx xxxxxxxxxxxxxx x
            xxxxxxxxx x xxxxxxxxxxxxxxxxxxx xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx
        x

        xx xxxxxxx xxxx xxxxx xx xxxxxxx xxxx xxxxxxxxxxx xx xxxxxx
        xx xxxxxx x xxxxx xxxxx xxxxxxxx xxxx xxxxxxxxx
        xxxx xx xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx x
            xx xxxx xxxxxx xxxx xxxxxxxxxxx xxxxxx xx xxxxxxxxx xx xxxxx xxxxxxxxx
            xxxxxxxxxxxxxx xxxxx x xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx xxxxxxxxxxxxxxxxxxxxxxxxxxxxxx xxx xxxxxxxxxxxxxxxxxxxxxxxx
            xxxxxxxxx x xxxxxxxxxxxxxxxxxxxxxxxxxxxxxx
        x

        xx [/pro] */
        // Is this case possible?
        else {
            arrayType = Object.class;
        }

        this.array = array;
        this.alias = alias;
        this.field = init(alias, arrayType);

        init(alias, arrayType);
    }

    @SuppressWarnings("unused")
    ArrayTable(Field<?> array, String alias, String[] fieldAliases) {
        super(alias);

        throw new UnsupportedOperationException("This constructor is not yet implemented");
    }

    private static final Fields<Record> init(String alias, Class<?> arrayType) {
        List<Field<?>> result = new ArrayList<Field<?>>();

        // [#1114] VARRAY/TABLE of OBJECT have more than one field
        if (UDTRecord.class.isAssignableFrom(arrayType)) {
            try {
                UDTRecord<?> record = (UDTRecord<?>) arrayType.newInstance();
                for (Field<?> f : record.fields()) {
                    result.add(fieldByName(f.getDataType(), alias, f.getName()));
                }
            }
            catch (Exception e) {
                throw new DataTypeException("Bad UDT Type : " + arrayType, e);
            }
        }

        // Simple array types have a synthetic field called "COLUMN_VALUE"
        else {
            result.add(fieldByName(DSL.getDataType(arrayType), alias, "COLUMN_VALUE"));
        }

        return new Fields<Record>(result);
    }

    @Override
    public final Class<? extends Record> getRecordType() {
        return RecordImpl.class;
    }

    @Override
    public final Table<Record> as(String as) {
        return new ArrayTable(array, as);
    }

    @Override
    public final Table<Record> as(String as, String... fieldAliases) {
        return new ArrayTable(array, as, fieldAliases);
    }

    @Override
    public final boolean declaresTables() {

        // Always true, because unnested tables are always aliased
        return true;
    }

    @Override
    public final void accept(Context<?> ctx) {
        ctx.visit(table(ctx.configuration()));
    }

    private final Table<Record> table(Configuration configuration) {
        switch (configuration.dialect().family()) {
            /* [pro] xx
            xxxx xxxxxxx x
                xx xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx x
                    xxxxxx xxxxxxxxxxxxxxxxxxxxx
                x
                xxxx x
                    xxxxxx xxx xxxxxxxxxxxxxxxxxxxxxxxxxxxxx
                x
            x

            xx [/pro] */
            case H2: {
                return new H2ArrayTable().as(alias);
            }

            // [#756] These dialects need special care when aliasing unnested
            // arrays
            case HSQLDB:
            case POSTGRES: {
                return new PostgresHSQLDBTable().as(alias);
            }

            // Other dialects can simulate unnested arrays using UNION ALL
            default: {
                if (array.getDataType().getType().isArray() && array instanceof Param) {
                    return simulate();
                }

                else {
                    throw new SQLDialectNotSupportedException("ARRAY TABLE is not supported for " + configuration.dialect());
                }
            }
        }
    }

    private class PostgresHSQLDBTable extends DialectArrayTable {

        /**
         * Generated UID
         */
        private static final long serialVersionUID = 6989279597964488457L;

        @Override
        public final void accept(Context<?> ctx) {
            ctx.sql("(").keyword("select").sql(" * ")
               .keyword("from").sql(" ").keyword("unnest").sql("(").visit(array).sql(") ")
               .keyword("as").sql(" ").literal(alias)
               .sql("(").literal("COLUMN_VALUE").sql("))");
        }
    }

    private class H2ArrayTable extends DialectArrayTable {

        /**
         * Generated UID
         */
        private static final long serialVersionUID = 8679404596822098711L;

        @Override
        public final void accept(Context<?> ctx) {
            ctx.keyword("table(").sql("COLUMN_VALUE ");

            // If the array type is unknown (e.g. because it's returned from
            // a stored function
            // Then the best choice for arbitrary types is varchar
            if (array.getDataType().getType() == Object[].class) {
                ctx.keyword(H2DataType.VARCHAR.getTypeName());
            }
            else {
                ctx.keyword(array.getDataType().getTypeName());
            }

            ctx.sql(" = ").visit(array).sql(")");
        }
    }

    /* [pro] xx
    xxxxxxx xxxxx xxxxxxxxxxxxxxxx xxxxxxx xxxxxxxxxxxxxxxxx x

        xxx
         x xxxxxxxxx xxx
         xx
        xxxxxxx xxxxxx xxxxx xxxx xxxxxxxxxxxxxxxx x xxxxxxxxxxxxxxxxxxxxx

        xxxxxxxxx
        xxxxxx xxxxx xxxx xxxxxxxxxxxxxxxxx xxxx x
            xxxxxxxxxxxxxxxxxx xxxxxxxxxxxxxxxxxxxxxxxxxx
        x
    x

    xx [/pro] */
    private abstract class DialectArrayTable extends AbstractTable<Record> {

        /**
         * Generated UID
         */
        private static final long serialVersionUID = 2662639259338694177L;

        DialectArrayTable() {
            super(alias);
        }

        @Override
        public final Class<? extends Record> getRecordType() {
            return RecordImpl.class;
        }

        @Override
        public final Table<Record> as(String as) {
            return new TableAlias<Record>(this, as);
        }

        @Override
        public final Table<Record> as(String as, String... fieldAliases) {
            return new TableAlias<Record>(this, as, fieldAliases);
        }

        @Override
        final Fields<Record> fields0() {
            return ArrayTable.this.fields0();
        }
    }

    @SuppressWarnings("unchecked")
    private final ArrayTableSimulation simulate() {
        return new ArrayTableSimulation(((Param<Object[]>) array).getValue(), alias);
    }

    @Override
    final Fields<Record> fields0() {
        return field;
    }
}
