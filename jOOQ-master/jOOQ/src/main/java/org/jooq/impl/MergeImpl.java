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

import static org.jooq.Clause.MERGE;
import static org.jooq.Clause.MERGE_DELETE_WHERE;
import static org.jooq.Clause.MERGE_MERGE_INTO;
import static org.jooq.Clause.MERGE_ON;
import static org.jooq.Clause.MERGE_SET;
import static org.jooq.Clause.MERGE_SET_ASSIGNMENT;
import static org.jooq.Clause.MERGE_USING;
import static org.jooq.Clause.MERGE_VALUES;
import static org.jooq.Clause.MERGE_WHEN_MATCHED_THEN_UPDATE;
import static org.jooq.Clause.MERGE_WHEN_NOT_MATCHED_THEN_INSERT;
import static org.jooq.Clause.MERGE_WHERE;
import static org.jooq.SQLDialect.H2;
// ...
import static org.jooq.impl.DSL.condition;
import static org.jooq.impl.DSL.exists;
import static org.jooq.impl.DSL.notExists;
import static org.jooq.impl.DSL.nullSafe;
import static org.jooq.impl.Utils.DATA_WRAP_DERIVED_TABLES_IN_PARENTHESES;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.jooq.BindContext;
import org.jooq.Clause;
import org.jooq.Condition;
import org.jooq.Configuration;
import org.jooq.Context;
import org.jooq.Field;
import org.jooq.MergeKeyStep1;
import org.jooq.MergeKeyStep10;
import org.jooq.MergeKeyStep11;
import org.jooq.MergeKeyStep12;
import org.jooq.MergeKeyStep13;
import org.jooq.MergeKeyStep14;
import org.jooq.MergeKeyStep15;
import org.jooq.MergeKeyStep16;
import org.jooq.MergeKeyStep17;
import org.jooq.MergeKeyStep18;
import org.jooq.MergeKeyStep19;
import org.jooq.MergeKeyStep2;
import org.jooq.MergeKeyStep20;
import org.jooq.MergeKeyStep21;
import org.jooq.MergeKeyStep22;
import org.jooq.MergeKeyStep3;
import org.jooq.MergeKeyStep4;
import org.jooq.MergeKeyStep5;
import org.jooq.MergeKeyStep6;
import org.jooq.MergeKeyStep7;
import org.jooq.MergeKeyStep8;
import org.jooq.MergeKeyStep9;
import org.jooq.MergeMatchedDeleteStep;
import org.jooq.MergeMatchedSetMoreStep;
import org.jooq.MergeNotMatchedSetMoreStep;
import org.jooq.MergeNotMatchedValuesStep1;
import org.jooq.MergeNotMatchedValuesStep10;
import org.jooq.MergeNotMatchedValuesStep11;
import org.jooq.MergeNotMatchedValuesStep12;
import org.jooq.MergeNotMatchedValuesStep13;
import org.jooq.MergeNotMatchedValuesStep14;
import org.jooq.MergeNotMatchedValuesStep15;
import org.jooq.MergeNotMatchedValuesStep16;
import org.jooq.MergeNotMatchedValuesStep17;
import org.jooq.MergeNotMatchedValuesStep18;
import org.jooq.MergeNotMatchedValuesStep19;
import org.jooq.MergeNotMatchedValuesStep2;
import org.jooq.MergeNotMatchedValuesStep20;
import org.jooq.MergeNotMatchedValuesStep21;
import org.jooq.MergeNotMatchedValuesStep22;
import org.jooq.MergeNotMatchedValuesStep3;
import org.jooq.MergeNotMatchedValuesStep4;
import org.jooq.MergeNotMatchedValuesStep5;
import org.jooq.MergeNotMatchedValuesStep6;
import org.jooq.MergeNotMatchedValuesStep7;
import org.jooq.MergeNotMatchedValuesStep8;
import org.jooq.MergeNotMatchedValuesStep9;
import org.jooq.MergeNotMatchedValuesStepN;
import org.jooq.MergeOnConditionStep;
import org.jooq.MergeOnStep;
import org.jooq.MergeUsingStep;
import org.jooq.Operator;
import org.jooq.QueryPart;
import org.jooq.Record;
import org.jooq.Record1;
import org.jooq.Row;
import org.jooq.Select;
import org.jooq.Table;
import org.jooq.TableLike;
import org.jooq.UniqueKey;
import org.jooq.exception.SQLDialectNotSupportedException;
import org.jooq.tools.StringUtils;

/**
 * The SQL standard MERGE statement
 *
 * @author Lukas Eder
 */
@SuppressWarnings({ "unchecked", "rawtypes" })
class MergeImpl<R extends Record, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22> extends AbstractQuery
implements

    // Cascading interface implementations for Merge behaviour
    MergeUsingStep<R>,

// [jooq-tools] START [implementsKeyStep]

    MergeKeyStep1<R, T1>,
    MergeKeyStep2<R, T1, T2>,
    MergeKeyStep3<R, T1, T2, T3>,
    MergeKeyStep4<R, T1, T2, T3, T4>,
    MergeKeyStep5<R, T1, T2, T3, T4, T5>,
    MergeKeyStep6<R, T1, T2, T3, T4, T5, T6>,
    MergeKeyStep7<R, T1, T2, T3, T4, T5, T6, T7>,
    MergeKeyStep8<R, T1, T2, T3, T4, T5, T6, T7, T8>,
    MergeKeyStep9<R, T1, T2, T3, T4, T5, T6, T7, T8, T9>,
    MergeKeyStep10<R, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10>,
    MergeKeyStep11<R, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11>,
    MergeKeyStep12<R, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12>,
    MergeKeyStep13<R, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13>,
    MergeKeyStep14<R, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14>,
    MergeKeyStep15<R, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15>,
    MergeKeyStep16<R, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16>,
    MergeKeyStep17<R, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17>,
    MergeKeyStep18<R, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18>,
    MergeKeyStep19<R, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19>,
    MergeKeyStep20<R, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20>,
    MergeKeyStep21<R, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21>,
    MergeKeyStep22<R, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22>,

// [jooq-tools] END [implementsKeyStep]

    MergeOnStep<R>,
    MergeOnConditionStep<R>,
    MergeMatchedSetMoreStep<R>,
    MergeMatchedDeleteStep<R>,
    MergeNotMatchedSetMoreStep<R>,

// [jooq-tools] START [implementsNotMatchedValuesStep]

    MergeNotMatchedValuesStep1<R, T1>,
    MergeNotMatchedValuesStep2<R, T1, T2>,
    MergeNotMatchedValuesStep3<R, T1, T2, T3>,
    MergeNotMatchedValuesStep4<R, T1, T2, T3, T4>,
    MergeNotMatchedValuesStep5<R, T1, T2, T3, T4, T5>,
    MergeNotMatchedValuesStep6<R, T1, T2, T3, T4, T5, T6>,
    MergeNotMatchedValuesStep7<R, T1, T2, T3, T4, T5, T6, T7>,
    MergeNotMatchedValuesStep8<R, T1, T2, T3, T4, T5, T6, T7, T8>,
    MergeNotMatchedValuesStep9<R, T1, T2, T3, T4, T5, T6, T7, T8, T9>,
    MergeNotMatchedValuesStep10<R, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10>,
    MergeNotMatchedValuesStep11<R, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11>,
    MergeNotMatchedValuesStep12<R, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12>,
    MergeNotMatchedValuesStep13<R, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13>,
    MergeNotMatchedValuesStep14<R, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14>,
    MergeNotMatchedValuesStep15<R, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15>,
    MergeNotMatchedValuesStep16<R, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16>,
    MergeNotMatchedValuesStep17<R, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17>,
    MergeNotMatchedValuesStep18<R, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18>,
    MergeNotMatchedValuesStep19<R, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19>,
    MergeNotMatchedValuesStep20<R, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20>,
    MergeNotMatchedValuesStep21<R, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21>,
    MergeNotMatchedValuesStep22<R, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22>,

// [jooq-tools] END [implementsNotMatchedValuesStep]

    MergeNotMatchedValuesStepN<R> {

    /**
     * Generated UID
     */
    private static final long           serialVersionUID = -8835479296876774391L;
    private static final Clause[]       CLAUSES          = { MERGE };

    private final Table<R>              table;
    private final ConditionProviderImpl on;
    private TableLike<?>                using;

    // [#998] Oracle extensions to the MERGE statement
    private Condition                   matchedWhere;
    private Condition                   matchedDeleteWhere;
    private Condition                   notMatchedWhere;

    // Flags to keep track of DSL object creation state
    private boolean                     matchedClause;
    private FieldMapForUpdate           matchedUpdate;
    private boolean                     notMatchedClause;
    private FieldMapForInsert           notMatchedInsert;

    // Objects for the H2-specific syntax
    private boolean                     h2Style;
    private QueryPartList<Field<?>>     h2Fields;
    private QueryPartList<Field<?>>     h2Keys;
    private QueryPartList<Field<?>>     h2Values;
    private Select<?>                   h2Select;

    MergeImpl(Configuration configuration, Table<R> table) {
        this(configuration, table, null);
    }

    MergeImpl(Configuration configuration, Table<R> table, Collection<? extends Field<?>> fields) {
        super(configuration);

        this.table = table;
        this.on = new ConditionProviderImpl();

        if (fields != null) {
            h2Style = true;
            h2Fields = new QueryPartList<Field<?>>(fields);
        }
    }

    // -------------------------------------------------------------------------
    // H2-specific MERGE API
    // -------------------------------------------------------------------------

    QueryPartList<Field<?>> getH2Fields() {
        if (h2Fields == null) {
            h2Fields = new QueryPartList<Field<?>>(table.fields());
        }

        return h2Fields;
    }

    QueryPartList<Field<?>> getH2Keys() {
        if (h2Keys == null) {
            h2Keys = new QueryPartList<Field<?>>();
        }

        return h2Keys;
    }

    QueryPartList<Field<?>> getH2Values() {
        if (h2Values == null) {
            h2Values = new QueryPartList<Field<?>>();
        }

        return h2Values;
    }

    @Override
    public final MergeImpl select(Select select) {
        h2Style = true;
        h2Select = select;
        return this;
    }

    @Override
    public final MergeImpl key(Field<?>... k) {
        return key(Arrays.asList(k));
    }

    @Override
    public final MergeImpl key(Collection<? extends Field<?>> keys) {
        h2Style = true;
        getH2Keys().addAll(keys);
        return this;
    }

    // -------------------------------------------------------------------------
    // Shared MERGE API
    // -------------------------------------------------------------------------

// [jooq-tools] START [values]

    @Override
    public final MergeImpl values(T1 value1) {
        return values(new Object[] { value1 });
    }

    @Override
    public final MergeImpl values(T1 value1, T2 value2) {
        return values(new Object[] { value1, value2 });
    }

    @Override
    public final MergeImpl values(T1 value1, T2 value2, T3 value3) {
        return values(new Object[] { value1, value2, value3 });
    }

    @Override
    public final MergeImpl values(T1 value1, T2 value2, T3 value3, T4 value4) {
        return values(new Object[] { value1, value2, value3, value4 });
    }

    @Override
    public final MergeImpl values(T1 value1, T2 value2, T3 value3, T4 value4, T5 value5) {
        return values(new Object[] { value1, value2, value3, value4, value5 });
    }

    @Override
    public final MergeImpl values(T1 value1, T2 value2, T3 value3, T4 value4, T5 value5, T6 value6) {
        return values(new Object[] { value1, value2, value3, value4, value5, value6 });
    }

    @Override
    public final MergeImpl values(T1 value1, T2 value2, T3 value3, T4 value4, T5 value5, T6 value6, T7 value7) {
        return values(new Object[] { value1, value2, value3, value4, value5, value6, value7 });
    }

    @Override
    public final MergeImpl values(T1 value1, T2 value2, T3 value3, T4 value4, T5 value5, T6 value6, T7 value7, T8 value8) {
        return values(new Object[] { value1, value2, value3, value4, value5, value6, value7, value8 });
    }

    @Override
    public final MergeImpl values(T1 value1, T2 value2, T3 value3, T4 value4, T5 value5, T6 value6, T7 value7, T8 value8, T9 value9) {
        return values(new Object[] { value1, value2, value3, value4, value5, value6, value7, value8, value9 });
    }

    @Override
    public final MergeImpl values(T1 value1, T2 value2, T3 value3, T4 value4, T5 value5, T6 value6, T7 value7, T8 value8, T9 value9, T10 value10) {
        return values(new Object[] { value1, value2, value3, value4, value5, value6, value7, value8, value9, value10 });
    }

    @Override
    public final MergeImpl values(T1 value1, T2 value2, T3 value3, T4 value4, T5 value5, T6 value6, T7 value7, T8 value8, T9 value9, T10 value10, T11 value11) {
        return values(new Object[] { value1, value2, value3, value4, value5, value6, value7, value8, value9, value10, value11 });
    }

    @Override
    public final MergeImpl values(T1 value1, T2 value2, T3 value3, T4 value4, T5 value5, T6 value6, T7 value7, T8 value8, T9 value9, T10 value10, T11 value11, T12 value12) {
        return values(new Object[] { value1, value2, value3, value4, value5, value6, value7, value8, value9, value10, value11, value12 });
    }

    @Override
    public final MergeImpl values(T1 value1, T2 value2, T3 value3, T4 value4, T5 value5, T6 value6, T7 value7, T8 value8, T9 value9, T10 value10, T11 value11, T12 value12, T13 value13) {
        return values(new Object[] { value1, value2, value3, value4, value5, value6, value7, value8, value9, value10, value11, value12, value13 });
    }

    @Override
    public final MergeImpl values(T1 value1, T2 value2, T3 value3, T4 value4, T5 value5, T6 value6, T7 value7, T8 value8, T9 value9, T10 value10, T11 value11, T12 value12, T13 value13, T14 value14) {
        return values(new Object[] { value1, value2, value3, value4, value5, value6, value7, value8, value9, value10, value11, value12, value13, value14 });
    }

    @Override
    public final MergeImpl values(T1 value1, T2 value2, T3 value3, T4 value4, T5 value5, T6 value6, T7 value7, T8 value8, T9 value9, T10 value10, T11 value11, T12 value12, T13 value13, T14 value14, T15 value15) {
        return values(new Object[] { value1, value2, value3, value4, value5, value6, value7, value8, value9, value10, value11, value12, value13, value14, value15 });
    }

    @Override
    public final MergeImpl values(T1 value1, T2 value2, T3 value3, T4 value4, T5 value5, T6 value6, T7 value7, T8 value8, T9 value9, T10 value10, T11 value11, T12 value12, T13 value13, T14 value14, T15 value15, T16 value16) {
        return values(new Object[] { value1, value2, value3, value4, value5, value6, value7, value8, value9, value10, value11, value12, value13, value14, value15, value16 });
    }

    @Override
    public final MergeImpl values(T1 value1, T2 value2, T3 value3, T4 value4, T5 value5, T6 value6, T7 value7, T8 value8, T9 value9, T10 value10, T11 value11, T12 value12, T13 value13, T14 value14, T15 value15, T16 value16, T17 value17) {
        return values(new Object[] { value1, value2, value3, value4, value5, value6, value7, value8, value9, value10, value11, value12, value13, value14, value15, value16, value17 });
    }

    @Override
    public final MergeImpl values(T1 value1, T2 value2, T3 value3, T4 value4, T5 value5, T6 value6, T7 value7, T8 value8, T9 value9, T10 value10, T11 value11, T12 value12, T13 value13, T14 value14, T15 value15, T16 value16, T17 value17, T18 value18) {
        return values(new Object[] { value1, value2, value3, value4, value5, value6, value7, value8, value9, value10, value11, value12, value13, value14, value15, value16, value17, value18 });
    }

    @Override
    public final MergeImpl values(T1 value1, T2 value2, T3 value3, T4 value4, T5 value5, T6 value6, T7 value7, T8 value8, T9 value9, T10 value10, T11 value11, T12 value12, T13 value13, T14 value14, T15 value15, T16 value16, T17 value17, T18 value18, T19 value19) {
        return values(new Object[] { value1, value2, value3, value4, value5, value6, value7, value8, value9, value10, value11, value12, value13, value14, value15, value16, value17, value18, value19 });
    }

    @Override
    public final MergeImpl values(T1 value1, T2 value2, T3 value3, T4 value4, T5 value5, T6 value6, T7 value7, T8 value8, T9 value9, T10 value10, T11 value11, T12 value12, T13 value13, T14 value14, T15 value15, T16 value16, T17 value17, T18 value18, T19 value19, T20 value20) {
        return values(new Object[] { value1, value2, value3, value4, value5, value6, value7, value8, value9, value10, value11, value12, value13, value14, value15, value16, value17, value18, value19, value20 });
    }

    @Override
    public final MergeImpl values(T1 value1, T2 value2, T3 value3, T4 value4, T5 value5, T6 value6, T7 value7, T8 value8, T9 value9, T10 value10, T11 value11, T12 value12, T13 value13, T14 value14, T15 value15, T16 value16, T17 value17, T18 value18, T19 value19, T20 value20, T21 value21) {
        return values(new Object[] { value1, value2, value3, value4, value5, value6, value7, value8, value9, value10, value11, value12, value13, value14, value15, value16, value17, value18, value19, value20, value21 });
    }

    @Override
    public final MergeImpl values(T1 value1, T2 value2, T3 value3, T4 value4, T5 value5, T6 value6, T7 value7, T8 value8, T9 value9, T10 value10, T11 value11, T12 value12, T13 value13, T14 value14, T15 value15, T16 value16, T17 value17, T18 value18, T19 value19, T20 value20, T21 value21, T22 value22) {
        return values(new Object[] { value1, value2, value3, value4, value5, value6, value7, value8, value9, value10, value11, value12, value13, value14, value15, value16, value17, value18, value19, value20, value21, value22 });
    }


    @Override
    public final MergeImpl values(Field<T1> value1) {
        return values(new Field[] { value1 });
    }

    @Override
    public final MergeImpl values(Field<T1> value1, Field<T2> value2) {
        return values(new Field[] { value1, value2 });
    }

    @Override
    public final MergeImpl values(Field<T1> value1, Field<T2> value2, Field<T3> value3) {
        return values(new Field[] { value1, value2, value3 });
    }

    @Override
    public final MergeImpl values(Field<T1> value1, Field<T2> value2, Field<T3> value3, Field<T4> value4) {
        return values(new Field[] { value1, value2, value3, value4 });
    }

    @Override
    public final MergeImpl values(Field<T1> value1, Field<T2> value2, Field<T3> value3, Field<T4> value4, Field<T5> value5) {
        return values(new Field[] { value1, value2, value3, value4, value5 });
    }

    @Override
    public final MergeImpl values(Field<T1> value1, Field<T2> value2, Field<T3> value3, Field<T4> value4, Field<T5> value5, Field<T6> value6) {
        return values(new Field[] { value1, value2, value3, value4, value5, value6 });
    }

    @Override
    public final MergeImpl values(Field<T1> value1, Field<T2> value2, Field<T3> value3, Field<T4> value4, Field<T5> value5, Field<T6> value6, Field<T7> value7) {
        return values(new Field[] { value1, value2, value3, value4, value5, value6, value7 });
    }

    @Override
    public final MergeImpl values(Field<T1> value1, Field<T2> value2, Field<T3> value3, Field<T4> value4, Field<T5> value5, Field<T6> value6, Field<T7> value7, Field<T8> value8) {
        return values(new Field[] { value1, value2, value3, value4, value5, value6, value7, value8 });
    }

    @Override
    public final MergeImpl values(Field<T1> value1, Field<T2> value2, Field<T3> value3, Field<T4> value4, Field<T5> value5, Field<T6> value6, Field<T7> value7, Field<T8> value8, Field<T9> value9) {
        return values(new Field[] { value1, value2, value3, value4, value5, value6, value7, value8, value9 });
    }

    @Override
    public final MergeImpl values(Field<T1> value1, Field<T2> value2, Field<T3> value3, Field<T4> value4, Field<T5> value5, Field<T6> value6, Field<T7> value7, Field<T8> value8, Field<T9> value9, Field<T10> value10) {
        return values(new Field[] { value1, value2, value3, value4, value5, value6, value7, value8, value9, value10 });
    }

    @Override
    public final MergeImpl values(Field<T1> value1, Field<T2> value2, Field<T3> value3, Field<T4> value4, Field<T5> value5, Field<T6> value6, Field<T7> value7, Field<T8> value8, Field<T9> value9, Field<T10> value10, Field<T11> value11) {
        return values(new Field[] { value1, value2, value3, value4, value5, value6, value7, value8, value9, value10, value11 });
    }

    @Override
    public final MergeImpl values(Field<T1> value1, Field<T2> value2, Field<T3> value3, Field<T4> value4, Field<T5> value5, Field<T6> value6, Field<T7> value7, Field<T8> value8, Field<T9> value9, Field<T10> value10, Field<T11> value11, Field<T12> value12) {
        return values(new Field[] { value1, value2, value3, value4, value5, value6, value7, value8, value9, value10, value11, value12 });
    }

    @Override
    public final MergeImpl values(Field<T1> value1, Field<T2> value2, Field<T3> value3, Field<T4> value4, Field<T5> value5, Field<T6> value6, Field<T7> value7, Field<T8> value8, Field<T9> value9, Field<T10> value10, Field<T11> value11, Field<T12> value12, Field<T13> value13) {
        return values(new Field[] { value1, value2, value3, value4, value5, value6, value7, value8, value9, value10, value11, value12, value13 });
    }

    @Override
    public final MergeImpl values(Field<T1> value1, Field<T2> value2, Field<T3> value3, Field<T4> value4, Field<T5> value5, Field<T6> value6, Field<T7> value7, Field<T8> value8, Field<T9> value9, Field<T10> value10, Field<T11> value11, Field<T12> value12, Field<T13> value13, Field<T14> value14) {
        return values(new Field[] { value1, value2, value3, value4, value5, value6, value7, value8, value9, value10, value11, value12, value13, value14 });
    }

    @Override
    public final MergeImpl values(Field<T1> value1, Field<T2> value2, Field<T3> value3, Field<T4> value4, Field<T5> value5, Field<T6> value6, Field<T7> value7, Field<T8> value8, Field<T9> value9, Field<T10> value10, Field<T11> value11, Field<T12> value12, Field<T13> value13, Field<T14> value14, Field<T15> value15) {
        return values(new Field[] { value1, value2, value3, value4, value5, value6, value7, value8, value9, value10, value11, value12, value13, value14, value15 });
    }

    @Override
    public final MergeImpl values(Field<T1> value1, Field<T2> value2, Field<T3> value3, Field<T4> value4, Field<T5> value5, Field<T6> value6, Field<T7> value7, Field<T8> value8, Field<T9> value9, Field<T10> value10, Field<T11> value11, Field<T12> value12, Field<T13> value13, Field<T14> value14, Field<T15> value15, Field<T16> value16) {
        return values(new Field[] { value1, value2, value3, value4, value5, value6, value7, value8, value9, value10, value11, value12, value13, value14, value15, value16 });
    }

    @Override
    public final MergeImpl values(Field<T1> value1, Field<T2> value2, Field<T3> value3, Field<T4> value4, Field<T5> value5, Field<T6> value6, Field<T7> value7, Field<T8> value8, Field<T9> value9, Field<T10> value10, Field<T11> value11, Field<T12> value12, Field<T13> value13, Field<T14> value14, Field<T15> value15, Field<T16> value16, Field<T17> value17) {
        return values(new Field[] { value1, value2, value3, value4, value5, value6, value7, value8, value9, value10, value11, value12, value13, value14, value15, value16, value17 });
    }

    @Override
    public final MergeImpl values(Field<T1> value1, Field<T2> value2, Field<T3> value3, Field<T4> value4, Field<T5> value5, Field<T6> value6, Field<T7> value7, Field<T8> value8, Field<T9> value9, Field<T10> value10, Field<T11> value11, Field<T12> value12, Field<T13> value13, Field<T14> value14, Field<T15> value15, Field<T16> value16, Field<T17> value17, Field<T18> value18) {
        return values(new Field[] { value1, value2, value3, value4, value5, value6, value7, value8, value9, value10, value11, value12, value13, value14, value15, value16, value17, value18 });
    }

    @Override
    public final MergeImpl values(Field<T1> value1, Field<T2> value2, Field<T3> value3, Field<T4> value4, Field<T5> value5, Field<T6> value6, Field<T7> value7, Field<T8> value8, Field<T9> value9, Field<T10> value10, Field<T11> value11, Field<T12> value12, Field<T13> value13, Field<T14> value14, Field<T15> value15, Field<T16> value16, Field<T17> value17, Field<T18> value18, Field<T19> value19) {
        return values(new Field[] { value1, value2, value3, value4, value5, value6, value7, value8, value9, value10, value11, value12, value13, value14, value15, value16, value17, value18, value19 });
    }

    @Override
    public final MergeImpl values(Field<T1> value1, Field<T2> value2, Field<T3> value3, Field<T4> value4, Field<T5> value5, Field<T6> value6, Field<T7> value7, Field<T8> value8, Field<T9> value9, Field<T10> value10, Field<T11> value11, Field<T12> value12, Field<T13> value13, Field<T14> value14, Field<T15> value15, Field<T16> value16, Field<T17> value17, Field<T18> value18, Field<T19> value19, Field<T20> value20) {
        return values(new Field[] { value1, value2, value3, value4, value5, value6, value7, value8, value9, value10, value11, value12, value13, value14, value15, value16, value17, value18, value19, value20 });
    }

    @Override
    public final MergeImpl values(Field<T1> value1, Field<T2> value2, Field<T3> value3, Field<T4> value4, Field<T5> value5, Field<T6> value6, Field<T7> value7, Field<T8> value8, Field<T9> value9, Field<T10> value10, Field<T11> value11, Field<T12> value12, Field<T13> value13, Field<T14> value14, Field<T15> value15, Field<T16> value16, Field<T17> value17, Field<T18> value18, Field<T19> value19, Field<T20> value20, Field<T21> value21) {
        return values(new Field[] { value1, value2, value3, value4, value5, value6, value7, value8, value9, value10, value11, value12, value13, value14, value15, value16, value17, value18, value19, value20, value21 });
    }

    @Override
    public final MergeImpl values(Field<T1> value1, Field<T2> value2, Field<T3> value3, Field<T4> value4, Field<T5> value5, Field<T6> value6, Field<T7> value7, Field<T8> value8, Field<T9> value9, Field<T10> value10, Field<T11> value11, Field<T12> value12, Field<T13> value13, Field<T14> value14, Field<T15> value15, Field<T16> value16, Field<T17> value17, Field<T18> value18, Field<T19> value19, Field<T20> value20, Field<T21> value21, Field<T22> value22) {
        return values(new Field[] { value1, value2, value3, value4, value5, value6, value7, value8, value9, value10, value11, value12, value13, value14, value15, value16, value17, value18, value19, value20, value21, value22 });
    }

// [jooq-tools] END [values]

    @Override
    public final MergeImpl values(Object... values) {

        // [#1541] The VALUES() clause is also supported in the H2-specific
        // syntax, in case of which, the USING() was not added
        if (using == null) {
            h2Style = true;
            getH2Values().addAll(Utils.fields(values, getH2Fields().toArray(new Field[0])));
        }
        else {
            Field<?>[] fields = notMatchedInsert.keySet().toArray(new Field[0]);
            notMatchedInsert.putValues(Utils.fields(values, fields));
        }

        return this;
    }

    @Override
    public final MergeImpl values(Field<?>... values) {
        return values((Object[]) values);
    }

    @Override
    public final MergeImpl values(Collection<?> values) {
        return values(values.toArray());
    }

    // -------------------------------------------------------------------------
    // Merge API
    // -------------------------------------------------------------------------

    @Override
    public final MergeImpl using(TableLike<?> u) {
        this.using = u;
        return this;
    }

    @Override
    public final MergeImpl usingDual() {
        this.using = create().selectOne();
        return this;
    }

    @Override
    public final MergeImpl on(Condition... conditions) {
        on.addConditions(conditions);
        return this;
    }

    @Override
    public final MergeOnConditionStep<R> on(Field<Boolean> condition) {
        return on(condition(condition));
    }

    @Override
    public final MergeImpl on(String sql) {
        return on(condition(sql));
    }

    @Override
    public final MergeImpl on(String sql, Object... bindings) {
        return on(condition(sql, bindings));
    }

    @Override
    public final MergeImpl on(String sql, QueryPart... parts) {
        return on(condition(sql, parts));
    }

    @Override
    public final MergeImpl and(Condition condition) {
        on.addConditions(condition);
        return this;
    }

    @Override
    public final MergeImpl and(Field<Boolean> condition) {
        return and(condition(condition));
    }

    @Override
    public final MergeImpl and(String sql) {
        return and(condition(sql));
    }

    @Override
    public final MergeImpl and(String sql, Object... bindings) {
        return and(condition(sql, bindings));
    }

    @Override
    public final MergeImpl and(String sql, QueryPart... parts) {
        return and(condition(sql, parts));
    }

    @Override
    public final MergeImpl andNot(Condition condition) {
        return and(condition.not());
    }

    @Override
    public final MergeImpl andNot(Field<Boolean> condition) {
        return and(condition(condition));
    }

    @Override
    public final MergeImpl andExists(Select<?> select) {
        return and(exists(select));
    }

    @Override
    public final MergeImpl andNotExists(Select<?> select) {
        return and(notExists(select));
    }

    @Override
    public final MergeImpl or(Condition condition) {
        on.addConditions(Operator.OR, condition);
        return this;
    }

    @Override
    public final MergeImpl or(Field<Boolean> condition) {
        return and(condition(condition));
    }

    @Override
    public final MergeImpl or(String sql) {
        return or(condition(sql));
    }

    @Override
    public final MergeImpl or(String sql, Object... bindings) {
        return or(condition(sql, bindings));
    }

    @Override
    public final MergeImpl or(String sql, QueryPart... parts) {
        return or(condition(sql, parts));
    }

    @Override
    public final MergeImpl orNot(Condition condition) {
        return or(condition.not());
    }

    @Override
    public final MergeImpl orNot(Field<Boolean> condition) {
        return and(condition(condition));
    }

    @Override
    public final MergeImpl orExists(Select<?> select) {
        return or(exists(select));
    }

    @Override
    public final MergeImpl orNotExists(Select<?> select) {
        return or(notExists(select));
    }

    @Override
    public final MergeImpl whenMatchedThenUpdate() {
        matchedClause = true;
        matchedUpdate = new FieldMapForUpdate(MERGE_SET_ASSIGNMENT);

        notMatchedClause = false;
        return this;
    }

    @Override
    public final <T> MergeImpl set(Field<T> field, T value) {
        return set(field, Utils.field(value, field));
    }

    @Override
    public final <T> MergeImpl set(Field<T> field, Field<T> value) {
        if (matchedClause) {
            matchedUpdate.put(field, nullSafe(value));
        }
        else if (notMatchedClause) {
            notMatchedInsert.put(field, nullSafe(value));
        }
        else {
            throw new IllegalStateException("Cannot call where() on the current state of the MERGE statement");
        }

        return this;
    }

    @Override
    public final <T> MergeImpl set(Field<T> field, Select<? extends Record1<T>> value) {
        return set(field, value.<T>asField());
    }

    @Override
    public final MergeImpl set(Map<? extends Field<?>, ?> map) {
        if (matchedClause) {
            matchedUpdate.set(map);
        }
        else if (notMatchedClause) {
            notMatchedInsert.set(map);
        }
        else {
            throw new IllegalStateException("Cannot call where() on the current state of the MERGE statement");
        }

        return this;
    }

    @Override
    public final MergeImpl set(Record record) {
        return set(Utils.map(record));
    }

    @Override
    public final MergeImpl whenNotMatchedThenInsert() {
        return whenNotMatchedThenInsert(Collections.<Field<?>>emptyList());
    }

// [jooq-tools] START [whenNotMatchedThenInsert]

    @Override
    @SuppressWarnings("hiding")
    public final <T1> MergeImpl whenNotMatchedThenInsert(Field<T1> field1) {
        return whenNotMatchedThenInsert(new Field[] { field1 });
    }

    @Override
    @SuppressWarnings("hiding")
    public final <T1, T2> MergeImpl whenNotMatchedThenInsert(Field<T1> field1, Field<T2> field2) {
        return whenNotMatchedThenInsert(new Field[] { field1, field2 });
    }

    @Override
    @SuppressWarnings("hiding")
    public final <T1, T2, T3> MergeImpl whenNotMatchedThenInsert(Field<T1> field1, Field<T2> field2, Field<T3> field3) {
        return whenNotMatchedThenInsert(new Field[] { field1, field2, field3 });
    }

    @Override
    @SuppressWarnings("hiding")
    public final <T1, T2, T3, T4> MergeImpl whenNotMatchedThenInsert(Field<T1> field1, Field<T2> field2, Field<T3> field3, Field<T4> field4) {
        return whenNotMatchedThenInsert(new Field[] { field1, field2, field3, field4 });
    }

    @Override
    @SuppressWarnings("hiding")
    public final <T1, T2, T3, T4, T5> MergeImpl whenNotMatchedThenInsert(Field<T1> field1, Field<T2> field2, Field<T3> field3, Field<T4> field4, Field<T5> field5) {
        return whenNotMatchedThenInsert(new Field[] { field1, field2, field3, field4, field5 });
    }

    @Override
    @SuppressWarnings("hiding")
    public final <T1, T2, T3, T4, T5, T6> MergeImpl whenNotMatchedThenInsert(Field<T1> field1, Field<T2> field2, Field<T3> field3, Field<T4> field4, Field<T5> field5, Field<T6> field6) {
        return whenNotMatchedThenInsert(new Field[] { field1, field2, field3, field4, field5, field6 });
    }

    @Override
    @SuppressWarnings("hiding")
    public final <T1, T2, T3, T4, T5, T6, T7> MergeImpl whenNotMatchedThenInsert(Field<T1> field1, Field<T2> field2, Field<T3> field3, Field<T4> field4, Field<T5> field5, Field<T6> field6, Field<T7> field7) {
        return whenNotMatchedThenInsert(new Field[] { field1, field2, field3, field4, field5, field6, field7 });
    }

    @Override
    @SuppressWarnings("hiding")
    public final <T1, T2, T3, T4, T5, T6, T7, T8> MergeImpl whenNotMatchedThenInsert(Field<T1> field1, Field<T2> field2, Field<T3> field3, Field<T4> field4, Field<T5> field5, Field<T6> field6, Field<T7> field7, Field<T8> field8) {
        return whenNotMatchedThenInsert(new Field[] { field1, field2, field3, field4, field5, field6, field7, field8 });
    }

    @Override
    @SuppressWarnings("hiding")
    public final <T1, T2, T3, T4, T5, T6, T7, T8, T9> MergeImpl whenNotMatchedThenInsert(Field<T1> field1, Field<T2> field2, Field<T3> field3, Field<T4> field4, Field<T5> field5, Field<T6> field6, Field<T7> field7, Field<T8> field8, Field<T9> field9) {
        return whenNotMatchedThenInsert(new Field[] { field1, field2, field3, field4, field5, field6, field7, field8, field9 });
    }

    @Override
    @SuppressWarnings("hiding")
    public final <T1, T2, T3, T4, T5, T6, T7, T8, T9, T10> MergeImpl whenNotMatchedThenInsert(Field<T1> field1, Field<T2> field2, Field<T3> field3, Field<T4> field4, Field<T5> field5, Field<T6> field6, Field<T7> field7, Field<T8> field8, Field<T9> field9, Field<T10> field10) {
        return whenNotMatchedThenInsert(new Field[] { field1, field2, field3, field4, field5, field6, field7, field8, field9, field10 });
    }

    @Override
    @SuppressWarnings("hiding")
    public final <T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11> MergeImpl whenNotMatchedThenInsert(Field<T1> field1, Field<T2> field2, Field<T3> field3, Field<T4> field4, Field<T5> field5, Field<T6> field6, Field<T7> field7, Field<T8> field8, Field<T9> field9, Field<T10> field10, Field<T11> field11) {
        return whenNotMatchedThenInsert(new Field[] { field1, field2, field3, field4, field5, field6, field7, field8, field9, field10, field11 });
    }

    @Override
    @SuppressWarnings("hiding")
    public final <T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12> MergeImpl whenNotMatchedThenInsert(Field<T1> field1, Field<T2> field2, Field<T3> field3, Field<T4> field4, Field<T5> field5, Field<T6> field6, Field<T7> field7, Field<T8> field8, Field<T9> field9, Field<T10> field10, Field<T11> field11, Field<T12> field12) {
        return whenNotMatchedThenInsert(new Field[] { field1, field2, field3, field4, field5, field6, field7, field8, field9, field10, field11, field12 });
    }

    @Override
    @SuppressWarnings("hiding")
    public final <T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13> MergeImpl whenNotMatchedThenInsert(Field<T1> field1, Field<T2> field2, Field<T3> field3, Field<T4> field4, Field<T5> field5, Field<T6> field6, Field<T7> field7, Field<T8> field8, Field<T9> field9, Field<T10> field10, Field<T11> field11, Field<T12> field12, Field<T13> field13) {
        return whenNotMatchedThenInsert(new Field[] { field1, field2, field3, field4, field5, field6, field7, field8, field9, field10, field11, field12, field13 });
    }

    @Override
    @SuppressWarnings("hiding")
    public final <T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14> MergeImpl whenNotMatchedThenInsert(Field<T1> field1, Field<T2> field2, Field<T3> field3, Field<T4> field4, Field<T5> field5, Field<T6> field6, Field<T7> field7, Field<T8> field8, Field<T9> field9, Field<T10> field10, Field<T11> field11, Field<T12> field12, Field<T13> field13, Field<T14> field14) {
        return whenNotMatchedThenInsert(new Field[] { field1, field2, field3, field4, field5, field6, field7, field8, field9, field10, field11, field12, field13, field14 });
    }

    @Override
    @SuppressWarnings("hiding")
    public final <T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15> MergeImpl whenNotMatchedThenInsert(Field<T1> field1, Field<T2> field2, Field<T3> field3, Field<T4> field4, Field<T5> field5, Field<T6> field6, Field<T7> field7, Field<T8> field8, Field<T9> field9, Field<T10> field10, Field<T11> field11, Field<T12> field12, Field<T13> field13, Field<T14> field14, Field<T15> field15) {
        return whenNotMatchedThenInsert(new Field[] { field1, field2, field3, field4, field5, field6, field7, field8, field9, field10, field11, field12, field13, field14, field15 });
    }

    @Override
    @SuppressWarnings("hiding")
    public final <T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16> MergeImpl whenNotMatchedThenInsert(Field<T1> field1, Field<T2> field2, Field<T3> field3, Field<T4> field4, Field<T5> field5, Field<T6> field6, Field<T7> field7, Field<T8> field8, Field<T9> field9, Field<T10> field10, Field<T11> field11, Field<T12> field12, Field<T13> field13, Field<T14> field14, Field<T15> field15, Field<T16> field16) {
        return whenNotMatchedThenInsert(new Field[] { field1, field2, field3, field4, field5, field6, field7, field8, field9, field10, field11, field12, field13, field14, field15, field16 });
    }

    @Override
    @SuppressWarnings("hiding")
    public final <T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17> MergeImpl whenNotMatchedThenInsert(Field<T1> field1, Field<T2> field2, Field<T3> field3, Field<T4> field4, Field<T5> field5, Field<T6> field6, Field<T7> field7, Field<T8> field8, Field<T9> field9, Field<T10> field10, Field<T11> field11, Field<T12> field12, Field<T13> field13, Field<T14> field14, Field<T15> field15, Field<T16> field16, Field<T17> field17) {
        return whenNotMatchedThenInsert(new Field[] { field1, field2, field3, field4, field5, field6, field7, field8, field9, field10, field11, field12, field13, field14, field15, field16, field17 });
    }

    @Override
    @SuppressWarnings("hiding")
    public final <T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18> MergeImpl whenNotMatchedThenInsert(Field<T1> field1, Field<T2> field2, Field<T3> field3, Field<T4> field4, Field<T5> field5, Field<T6> field6, Field<T7> field7, Field<T8> field8, Field<T9> field9, Field<T10> field10, Field<T11> field11, Field<T12> field12, Field<T13> field13, Field<T14> field14, Field<T15> field15, Field<T16> field16, Field<T17> field17, Field<T18> field18) {
        return whenNotMatchedThenInsert(new Field[] { field1, field2, field3, field4, field5, field6, field7, field8, field9, field10, field11, field12, field13, field14, field15, field16, field17, field18 });
    }

    @Override
    @SuppressWarnings("hiding")
    public final <T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19> MergeImpl whenNotMatchedThenInsert(Field<T1> field1, Field<T2> field2, Field<T3> field3, Field<T4> field4, Field<T5> field5, Field<T6> field6, Field<T7> field7, Field<T8> field8, Field<T9> field9, Field<T10> field10, Field<T11> field11, Field<T12> field12, Field<T13> field13, Field<T14> field14, Field<T15> field15, Field<T16> field16, Field<T17> field17, Field<T18> field18, Field<T19> field19) {
        return whenNotMatchedThenInsert(new Field[] { field1, field2, field3, field4, field5, field6, field7, field8, field9, field10, field11, field12, field13, field14, field15, field16, field17, field18, field19 });
    }

    @Override
    @SuppressWarnings("hiding")
    public final <T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20> MergeImpl whenNotMatchedThenInsert(Field<T1> field1, Field<T2> field2, Field<T3> field3, Field<T4> field4, Field<T5> field5, Field<T6> field6, Field<T7> field7, Field<T8> field8, Field<T9> field9, Field<T10> field10, Field<T11> field11, Field<T12> field12, Field<T13> field13, Field<T14> field14, Field<T15> field15, Field<T16> field16, Field<T17> field17, Field<T18> field18, Field<T19> field19, Field<T20> field20) {
        return whenNotMatchedThenInsert(new Field[] { field1, field2, field3, field4, field5, field6, field7, field8, field9, field10, field11, field12, field13, field14, field15, field16, field17, field18, field19, field20 });
    }

    @Override
    @SuppressWarnings("hiding")
    public final <T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21> MergeImpl whenNotMatchedThenInsert(Field<T1> field1, Field<T2> field2, Field<T3> field3, Field<T4> field4, Field<T5> field5, Field<T6> field6, Field<T7> field7, Field<T8> field8, Field<T9> field9, Field<T10> field10, Field<T11> field11, Field<T12> field12, Field<T13> field13, Field<T14> field14, Field<T15> field15, Field<T16> field16, Field<T17> field17, Field<T18> field18, Field<T19> field19, Field<T20> field20, Field<T21> field21) {
        return whenNotMatchedThenInsert(new Field[] { field1, field2, field3, field4, field5, field6, field7, field8, field9, field10, field11, field12, field13, field14, field15, field16, field17, field18, field19, field20, field21 });
    }

    @Override
    @SuppressWarnings("hiding")
    public final <T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22> MergeImpl whenNotMatchedThenInsert(Field<T1> field1, Field<T2> field2, Field<T3> field3, Field<T4> field4, Field<T5> field5, Field<T6> field6, Field<T7> field7, Field<T8> field8, Field<T9> field9, Field<T10> field10, Field<T11> field11, Field<T12> field12, Field<T13> field13, Field<T14> field14, Field<T15> field15, Field<T16> field16, Field<T17> field17, Field<T18> field18, Field<T19> field19, Field<T20> field20, Field<T21> field21, Field<T22> field22) {
        return whenNotMatchedThenInsert(new Field[] { field1, field2, field3, field4, field5, field6, field7, field8, field9, field10, field11, field12, field13, field14, field15, field16, field17, field18, field19, field20, field21, field22 });
    }


// [jooq-tools] END [whenNotMatchedThenInsert]

    @Override
    public final MergeImpl whenNotMatchedThenInsert(Field<?>... fields) {
        return whenNotMatchedThenInsert(Arrays.asList(fields));
    }

    @Override
    public final MergeImpl whenNotMatchedThenInsert(Collection<? extends Field<?>> fields) {
        notMatchedClause = true;
        notMatchedInsert = new FieldMapForInsert();
        notMatchedInsert.putFields(fields);

        matchedClause = false;
        return this;
    }

    @Override
    public final MergeImpl where(Condition condition) {
        if (matchedClause) {
            matchedWhere = condition;
        }
        else if (notMatchedClause) {
            notMatchedWhere = condition;
        }
        else {
            throw new IllegalStateException("Cannot call where() on the current state of the MERGE statement");
        }

        return this;
    }

    @Override
    public final MergeMatchedDeleteStep<R> where(Field<Boolean> condition) {
        return where(condition(condition));
    }

    @Override
    public final MergeImpl deleteWhere(Condition condition) {
        matchedDeleteWhere = condition;
        return this;
    }

    @Override
    public final MergeImpl deleteWhere(Field<Boolean> condition) {
        return deleteWhere(condition(condition));
    }

    // -------------------------------------------------------------------------
    // QueryPart API
    // -------------------------------------------------------------------------

    /**
     * Return a standard MERGE statement simulating the H2-specific syntax
     */
    private final QueryPart getStandardMerge(Configuration config) {
        switch (config.dialect().family()) {
            /* [pro] xx
            xxxx xxxx
            xxxx xxxxxxx
            xxxx xxxxxxxxxx
            xxxx xxxxxxx
            xx [/pro] */
            case CUBRID:
            case HSQLDB: {

                // The SRC for the USING() clause:
                // ------------------------------
                Table<?> src;
                if (h2Select != null) {
                    List<Field<?>> v = new ArrayList<Field<?>>();
                    Row row = h2Select.fieldsRow();

                    for (int i = 0; i < row.size(); i++) {
                        v.add(row.field(i).as("s" + (i + 1)));
                    }

                    // [#579] TODO: Currently, this syntax may require aliasing
                    // on the call-site
                    src = create(config).select(v).from(h2Select).asTable("src");
                }
                else {
                    List<Field<?>> v = new ArrayList<Field<?>>();

                    for (int i = 0; i < getH2Values().size(); i++) {
                        v.add(getH2Values().get(i).as("s" + (i + 1)));
                    }

                    src = create(config).select(v).asTable("src");
                }

                // The condition for the ON clause:
                // --------------------------------
                Set<Field<?>> onFields = new HashSet<Field<?>>();
                Condition condition = null;
                if (getH2Keys().isEmpty()) {
                    UniqueKey<?> key = table.getPrimaryKey();

                    if (key != null) {
                        onFields.addAll(key.getFields());

                        for (int i = 0; i < key.getFields().size(); i++) {
                            Condition rhs = key.getFields().get(i).equal((Field) src.field(i));

                            if (condition == null) {
                                condition = rhs;
                            }
                            else {
                                condition = condition.and(rhs);
                            }
                        }
                    }

                    // This should probably execute an INSERT statement
                    else {
                        throw new IllegalStateException("Cannot omit KEY() clause on a non-Updatable Table");
                    }
                }
                else {
                    for (int i = 0; i < getH2Keys().size(); i++) {
                        int matchIndex = getH2Fields().indexOf(getH2Keys().get(i));
                        if (matchIndex == -1) {
                            throw new IllegalStateException("Fields in KEY() clause must be part of the fields specified in MERGE INTO table (...)");
                        }

                        onFields.addAll(getH2Keys());
                        Condition rhs = getH2Keys().get(i).equal((Field) src.field(matchIndex));

                        if (condition == null) {
                            condition = rhs;
                        }
                        else {
                            condition = condition.and(rhs);
                        }
                    }
                }

                // INSERT and UPDATE clauses
                // -------------------------
                Map<Field<?>, Field<?>> update = new LinkedHashMap<Field<?>, Field<?>>();
                Map<Field<?>, Field<?>> insert = new LinkedHashMap<Field<?>, Field<?>>();

                for (int i = 0; i < src.fieldsRow().size(); i++) {

                    // Oracle does not allow to update fields from the ON clause
                    if (!onFields.contains(getH2Fields().get(i))) {
                        update.put(getH2Fields().get(i), src.field(i));
                    }

                    insert.put(getH2Fields().get(i), src.field(i));
                }

                return create(config).mergeInto(table)
                                     .using(src)
                                     .on(condition)
                                     .whenMatchedThenUpdate()
                                     .set(update)
                                     .whenNotMatchedThenInsert()
                                     .set(insert);
            }
            default:
                throw new SQLDialectNotSupportedException("The H2-specific MERGE syntax is not supported in dialect : " + config.dialect());
        }
    }

    @Override
    public final void accept(Context<?> ctx) {
        if (h2Style) {
            if (ctx.configuration().dialect() == H2) {
                toSQLH2(ctx);
            }
            else {
                ctx.visit(getStandardMerge(ctx.configuration()));
            }
        }
        else {
            toSQLStandard(ctx);
        }
    }

    private final void toSQLH2(Context<?> ctx) {
        ctx.keyword("merge into")
           .sql(" ")
           .declareTables(true)
           .visit(table)
           .formatSeparator();

        ctx.sql("(");
        Utils.fieldNames(ctx, getH2Fields());
        ctx.sql(")");

        if (!getH2Keys().isEmpty()) {
            ctx.sql(" ").keyword("key").sql(" (");
            Utils.fieldNames(ctx, getH2Keys());
            ctx.sql(")");
        }

        if (h2Select != null) {
            ctx.sql(" ")
               .visit(h2Select);
        }
        else {
            ctx.sql(" ").keyword("values").sql(" (")
               .visit(getH2Values())
               .sql(")");
        }
    }

    private final void toSQLStandard(Context<?> ctx) {
        ctx.start(MERGE_MERGE_INTO)
           .keyword("merge into").sql(" ")
           .declareTables(true)
           .visit(table)
           .declareTables(false)
           .end(MERGE_MERGE_INTO)
           .formatSeparator()
           .start(MERGE_USING)
           .declareTables(true)
           .keyword("using").sql(" ")
           .formatIndentStart()
           .formatNewLine();
        ctx.data(DATA_WRAP_DERIVED_TABLES_IN_PARENTHESES, true);
        ctx.visit(using);
        ctx.data(DATA_WRAP_DERIVED_TABLES_IN_PARENTHESES, null);
        ctx.formatIndentEnd()
           .declareTables(false);

        /* [pro] xx
        xxxxxx xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx x
            xxxx xxxxxxxxxx
            xxxx xxxxxxx x
                xx xxxxxx xxxxxxxxxx xxxxxxx x
                    xxx xxxx x xxxxxxxxxxxxxxxxxx

                    xxxxxxxxx xxxxxxxxxxxxxxxxxxxxxx xx
                       xxxxxxxxxxxxxx
                       xxxxxxxxxx
                       xxxxxxxxxx

                    xxxxxx xxxxxxxxx x xxx
                    xxx xxxxxxxxx xxxxx x xxxxxxxxxxxx xxxxxxxxxxxxxxxx x

                        xx xxxx xxxxxx xxx xxxxxxx
                        xx xxxxxx xxxxxxx xxxx
                        xxxxxx xxxx x xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx
                            x xxxxxxxx x xxxx x xxx x xxxxxxxxxxxxxxxxx
                            x xxxxxxxxxxxxxxxx

                        xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx
                        xxxxxxxxx x xx xx
                    x

                    xxxxxxxxxxxxx
                x

                xxxxxx
            x
        x

        xx [/pro] */
        boolean onParentheses = false/* [pro] xx xx xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx xx xxxxxxxx [/pro] */;
        ctx.end(MERGE_USING)
           .formatSeparator()
           .start(MERGE_ON)
           // Oracle ON ( ... ) parentheses are a mandatory syntax element
           .keyword("on").sql(onParentheses ? " (" : " ")
           .visit(on)
           .sql(onParentheses ? ")" : "")
           .end(MERGE_ON)
           .start(MERGE_WHEN_MATCHED_THEN_UPDATE)
           .start(MERGE_SET);

        // [#999] WHEN MATCHED clause is optional
        if (matchedUpdate != null) {
            ctx.formatSeparator()
               .keyword("when matched then update set")
               .formatIndentStart()
               .formatSeparator()
               .visit(matchedUpdate)
               .formatIndentEnd();
        }

        ctx.end(MERGE_SET)
           .start(MERGE_WHERE);

        // [#998] Oracle MERGE extension: WHEN MATCHED THEN UPDATE .. WHERE
        if (matchedWhere != null) {
            ctx.formatSeparator()
               .keyword("where").sql(" ")
               .visit(matchedWhere);
        }

        ctx.end(MERGE_WHERE)
           .start(MERGE_DELETE_WHERE);

        // [#998] Oracle MERGE extension: WHEN MATCHED THEN UPDATE .. DELETE WHERE
        if (matchedDeleteWhere != null) {
            ctx.formatSeparator()
               .keyword("delete where").sql(" ")
               .visit(matchedDeleteWhere);
        }

        ctx.end(MERGE_DELETE_WHERE)
           .end(MERGE_WHEN_MATCHED_THEN_UPDATE)
           .start(MERGE_WHEN_NOT_MATCHED_THEN_INSERT);

        // [#999] WHEN NOT MATCHED clause is optional
        if (notMatchedInsert != null) {
            ctx.formatSeparator()
               .keyword("when not matched then insert").sql(" ");
            notMatchedInsert.toSQLReferenceKeys(ctx);
            ctx.formatSeparator()
               .start(MERGE_VALUES)
               .keyword("values").sql(" ")
               .visit(notMatchedInsert)
               .end(MERGE_VALUES);
        }

        ctx.start(MERGE_WHERE);

        // [#998] Oracle MERGE extension: WHEN NOT MATCHED THEN INSERT .. WHERE
        if (notMatchedWhere != null) {
            ctx.formatSeparator()
               .keyword("where").sql(" ")
               .visit(notMatchedWhere);
        }

        ctx.end(MERGE_WHERE)
           .end(MERGE_WHEN_NOT_MATCHED_THEN_INSERT);
        /* [pro] xx

        xxxxxx xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx x
            xxxx xxxxxxxxxx
                xxxxxxxxxxxxx
                xxxxxx
        x
        xx [/pro] */
    }

    @Override
    public final Clause[] clauses(Context<?> ctx) {
        return CLAUSES;
    }
}
