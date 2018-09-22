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

import org.jooq.Comparator;
import org.jooq.api.annotation.State;

import java.util.Collection;

import javax.annotation.Generated;

/**
 * A model type for a row value expression with degree <code>4</code>.
 * <p>
 * Note: Not all databases support row value expressions, but many row value
 * expression operations can be simulated on all databases. See relevant row
 * value expression method Javadocs for details.
 *
 * @author Lukas Eder
 */
@Generated("This class was generated using jOOQ-tools")
@State
public interface Row4<T1, T2, T3, T4> extends Row {

    // ------------------------------------------------------------------------
    // Field accessors
    // ------------------------------------------------------------------------

    /**
     * Get the first field.
     */
    Field<T1> field1();

    /**
     * Get the second field.
     */
    Field<T2> field2();

    /**
     * Get the third field.
     */
    Field<T3> field3();

    /**
     * Get the fourth field.
     */
    Field<T4> field4();

    // ------------------------------------------------------------------------
    // Generic comparison predicates
    // ------------------------------------------------------------------------
    
    /**
     * Compare this row value expression with another row value expression
     * using a dynamic comparator.
     * <p>
     * See the explicit comparison methods for details. Note, not all 
     * {@link Comparator} types are supported
     *
     * @see #equal(Row4)
     * @see #notEqual(Row4)
     * @see #lessThan(Row4)
     * @see #lessOrEqual(Row4)
     * @see #greaterThan(Row4)
     * @see #greaterOrEqual(Row4)
     */
    @Support
    Condition compare(Comparator comparator, Row4<T1, T2, T3, T4> row);

    /**
     * Compare this row value expression with a record record
     * using a dynamic comparator.
     * <p>
     * See the explicit comparison methods for details. Note, not all 
     * {@link Comparator} types are supported
     *
     * @see #equal(Row4)
     * @see #notEqual(Row4)
     * @see #lessThan(Row4)
     * @see #lessOrEqual(Row4)
     * @see #greaterThan(Row4)
     * @see #greaterOrEqual(Row4)
     */
    @Support
    Condition compare(Comparator comparator, Record4<T1, T2, T3, T4> record);

    /**
     * Compare this row value expression with another row value expression
     * using a dynamic comparator.
     * <p>
     * See the explicit comparison methods for details. Note, not all 
     * {@link Comparator} types are supported
     *
     * @see #equal(Row4)
     * @see #notEqual(Row4)
     * @see #lessThan(Row4)
     * @see #lessOrEqual(Row4)
     * @see #greaterThan(Row4)
     * @see #greaterOrEqual(Row4)
     */
    @Support
    Condition compare(Comparator comparator, T1 t1, T2 t2, T3 t3, T4 t4);

    /**
     * Compare this row value expression with another row value expression
     * using a dynamic comparator.
     * <p>
     * See the explicit comparison methods for details. Note, not all 
     * {@link Comparator} types are supported
     *
     * @see #equal(Row4)
     * @see #notEqual(Row4)
     * @see #lessThan(Row4)
     * @see #lessOrEqual(Row4)
     * @see #greaterThan(Row4)
     * @see #greaterOrEqual(Row4)
     */
    @Support
    Condition compare(Comparator comparator, Field<T1> t1, Field<T2> t2, Field<T3> t3, Field<T4> t4);

    // ------------------------------------------------------------------------
    // Equal / Not equal comparison predicates
    // ------------------------------------------------------------------------

    /**
     * Compare this row value expression with another row value expression for
     * equality.
     * <p>
     * Row equality comparison predicates can be simulated in those databases
     * that do not support such predicates natively:
     * <code>(A, B) = (1, 2)</code> is equivalent to
     * <code>A = 1 AND B = 2</code>
     */
    @Support
    Condition equal(Row4<T1, T2, T3, T4> row);

    /**
     * Compare this row value expression with a record for equality.
     *
     * @see #equal(Row4)
     */
    @Support
    Condition equal(Record4<T1, T2, T3, T4> record);

    /**
     * Compare this row value expression with another row value expression for
     * equality.
     *
     * @see #equal(Row4)
     */
    @Support
    Condition equal(T1 t1, T2 t2, T3 t3, T4 t4);

    /**
     * Compare this row value expression with another row value expression for
     * equality.
     *
     * @see #equal(Row4)
     */
    @Support
    Condition equal(Field<T1> t1, Field<T2> t2, Field<T3> t3, Field<T4> t4);

    /**
     * Compare this row value expression with a subselect for equality.
     *
     * @see #equal(Row4)
     */
    @Support
    Condition equal(Select<? extends Record4<T1, T2, T3, T4>> select);

    /**
     * Compare this row value expression with another row value expression for
     * equality.
     *
     * @see #equal(Row4)
     */
    @Support
    Condition eq(Row4<T1, T2, T3, T4> row);

    /**
     * Compare this row value expression with a record for equality.
     *
     * @see #equal(Row4)
     */
    @Support
    Condition eq(Record4<T1, T2, T3, T4> record);

    /**
     * Compare this row value expression with another row value expression for
     * equality.
     *
     * @see #equal(Row4)
     */
    @Support
    Condition eq(T1 t1, T2 t2, T3 t3, T4 t4);

    /**
     * Compare this row value expression with another row value expression for
     * equality.
     *
     * @see #equal(Row4)
     */
    @Support
    Condition eq(Field<T1> t1, Field<T2> t2, Field<T3> t3, Field<T4> t4);

    /**
     * Compare this row value expression with a subselect for equality.
     *
     * @see #equal(Row4)
     */
    @Support
    Condition eq(Select<? extends Record4<T1, T2, T3, T4>> select);

    /**
     * Compare this row value expression with another row value expression for
     * non-equality.
     * <p>
     * Row non-equality comparison predicates can be simulated in those
     * databases that do not support such predicates natively:
     * <code>(A, B) <> (1, 2)</code> is equivalent to
     * <code>NOT(A = 1 AND B = 2)</code>
     */
    @Support
    Condition notEqual(Row4<T1, T2, T3, T4> row);

    /**
     * Compare this row value expression with a record for non-equality
     *
     * @see #notEqual(Row4)
     */
    @Support
    Condition notEqual(Record4<T1, T2, T3, T4> record);

    /**
     * Compare this row value expression with another row value expression for.
     * non-equality
     *
     * @see #notEqual(Row4)
     */
    @Support
    Condition notEqual(T1 t1, T2 t2, T3 t3, T4 t4);

    /**
     * Compare this row value expression with another row value expression for
     * non-equality.
     *
     * @see #notEqual(Row4)
     */
    @Support
    Condition notEqual(Field<T1> t1, Field<T2> t2, Field<T3> t3, Field<T4> t4);

    /**
     * Compare this row value expression with a subselect for non-equality.
     *
     * @see #notEqual(Row4)
     */
    @Support
    Condition notEqual(Select<? extends Record4<T1, T2, T3, T4>> select);

    /**
     * Compare this row value expression with another row value expression for
     * non-equality.
     *
     * @see #notEqual(Row4)
     */
    @Support
    Condition ne(Row4<T1, T2, T3, T4> row);

    /**
     * Compare this row value expression with a record for non-equality.
     *
     * @see #notEqual(Row4)
     */
    @Support
    Condition ne(Record4<T1, T2, T3, T4> record);

    /**
     * Compare this row value expression with another row value expression for
     * non-equality.
     *
     * @see #notEqual(Row4)
     */
    @Support
    Condition ne(T1 t1, T2 t2, T3 t3, T4 t4);

    /**
     * Compare this row value expression with another row value expression for
     * non-equality.
     *
     * @see #notEqual(Row4)
     */
    @Support
    Condition ne(Field<T1> t1, Field<T2> t2, Field<T3> t3, Field<T4> t4);

    /**
     * Compare this row value expression with a subselect for non-equality.
     *
     * @see #notEqual(Row4)
     */
    @Support
    Condition ne(Select<? extends Record4<T1, T2, T3, T4>> select);

    // ------------------------------------------------------------------------
    // Ordering comparison predicates
    // ------------------------------------------------------------------------

    /**
     * Compare this row value expression with another row value expression for
     * order.
     * <p>
     * Row order comparison predicates can be simulated in those
     * databases that do not support such predicates natively:
     * <code>(A, B, C) < (1, 2, 3)</code> is equivalent to
     * <code>A < 1 OR (A = 1 AND B < 2) OR (A = 1 AND B = 2 AND C < 3)</code>
     */
    @Support
    Condition lessThan(Row4<T1, T2, T3, T4> row);

    /**
     * Compare this row value expression with a record for order.
     *
     * @see #lessThan(Row4)
     */
    @Support
    Condition lessThan(Record4<T1, T2, T3, T4> record);

    /**
     * Compare this row value expression with another row value expression for
     * order.
     *
     * @see #lessThan(Row4)
     */
    @Support
    Condition lessThan(T1 t1, T2 t2, T3 t3, T4 t4);

    /**
     * Compare this row value expression with another row value expression for
     * order.
     *
     * @see #lessThan(Row4)
     */
    @Support
    Condition lessThan(Field<T1> t1, Field<T2> t2, Field<T3> t3, Field<T4> t4);

    /**
     * Compare this row value expression with a subselect for order.
     *
     * @see #lessThan(Row4)
     */
    @Support
    Condition lessThan(Select<? extends Record4<T1, T2, T3, T4>> select);

    /**
     * Compare this row value expression with another row value expression for
     * order.
     *
     * @see #lessThan(Row4)
     */
    @Support
    Condition lt(Row4<T1, T2, T3, T4> row);

    /**
     * Compare this row value expression with a record for order.
     *
     * @see #lessThan(Row4)
     */
    @Support
    Condition lt(Record4<T1, T2, T3, T4> record);

    /**
     * Compare this row value expression with another row value expression for
     * order.
     *
     * @see #lessThan(Row4)
     */
    @Support
    Condition lt(T1 t1, T2 t2, T3 t3, T4 t4);

    /**
     * Compare this row value expression with another row value expression for
     * order.
     *
     * @see #lessThan(Row4)
     */
    @Support
    Condition lt(Field<T1> t1, Field<T2> t2, Field<T3> t3, Field<T4> t4);

    /**
     * Compare this row value expression with a subselect for order.
     *
     * @see #lessThan(Row4)
     */
    @Support
    Condition lt(Select<? extends Record4<T1, T2, T3, T4>> select);

    /**
     * Compare this row value expression with another row value expression for
     * order.
     * <p>
     * Row order comparison predicates can be simulated in those
     * databases that do not support such predicates natively:
     * <code>(A, B) <= (1, 2)</code> is equivalent to
     * <code>A < 1 OR (A = 1 AND B < 2) OR (A = 1 AND B = 2)</code>
     */
    @Support
    Condition lessOrEqual(Row4<T1, T2, T3, T4> row);

    /**
     * Compare this row value expression with a record for order.
     *
     * @see #lessOrEqual(Row4)
     */
    @Support
    Condition lessOrEqual(Record4<T1, T2, T3, T4> record);

    /**
     * Compare this row value expression with another row value expression for
     * order.
     *
     * @see #lessOrEqual(Row4)
     */
    @Support
    Condition lessOrEqual(T1 t1, T2 t2, T3 t3, T4 t4);

    /**
     * Compare this row value expression with another row value expression for
     * order.
     *
     * @see #lessOrEqual(Row4)
     */
    @Support
    Condition lessOrEqual(Field<T1> t1, Field<T2> t2, Field<T3> t3, Field<T4> t4);

    /**
     * Compare this row value expression with a subselect for order.
     *
     * @see #lessOrEqual(Row4)
     */
    @Support
    Condition lessOrEqual(Select<? extends Record4<T1, T2, T3, T4>> select);

    /**
     * Compare this row value expression with another row value expression for
     * order.
     *
     * @see #lessOrEqual(Row4)
     */
    @Support
    Condition le(Row4<T1, T2, T3, T4> row);

    /**
     * Compare this row value expression with a record for order.
     *
     * @see #lessOrEqual(Row4)
     */
    @Support
    Condition le(Record4<T1, T2, T3, T4> record);

    /**
     * Compare this row value expression with another row value expression for
     * order.
     *
     * @see #lessOrEqual(Row4)
     */
    @Support
    Condition le(T1 t1, T2 t2, T3 t3, T4 t4);

    /**
     * Compare this row value expression with another row value expression for
     * order.
     *
     * @see #lessOrEqual(Row4)
     */
    @Support
    Condition le(Field<T1> t1, Field<T2> t2, Field<T3> t3, Field<T4> t4);

    /**
     * Compare this row value expression with a subselect for order.
     *
     * @see #lessOrEqual(Row4)
     */
    @Support
    Condition le(Select<? extends Record4<T1, T2, T3, T4>> select);

    /**
     * Compare this row value expression with another row value expression for
     * order.
     * <p>
     * Row order comparison predicates can be simulated in those
     * databases that do not support such predicates natively:
     * <code>(A, B, C) > (1, 2, 3)</code> is equivalent to
     * <code>A > 1 OR (A = 1 AND B > 2) OR (A = 1 AND B = 2 AND C > 3)</code>
     */
    @Support
    Condition greaterThan(Row4<T1, T2, T3, T4> row);

    /**
     * Compare this row value expression with a record for order.
     *
     * @see #greaterThan(Row4)
     */
    @Support
    Condition greaterThan(Record4<T1, T2, T3, T4> record);

    /**
     * Compare this row value expression with another row value expression for
     * order.
     *
     * @see #greaterThan(Row4)
     */
    @Support
    Condition greaterThan(T1 t1, T2 t2, T3 t3, T4 t4);

    /**
     * Compare this row value expression with another row value expression for
     * order.
     *
     * @see #greaterThan(Row4)
     */
    @Support
    Condition greaterThan(Field<T1> t1, Field<T2> t2, Field<T3> t3, Field<T4> t4);

    /**
     * Compare this row value expression with a subselect for order.
     *
     * @see #greaterThan(Row4)
     */
    @Support
    Condition greaterThan(Select<? extends Record4<T1, T2, T3, T4>> select);

    /**
     * Compare this row value expression with another row value expression for
     * order.
     *
     * @see #greaterThan(Row4)
     */
    @Support
    Condition gt(Row4<T1, T2, T3, T4> row);

    /**
     * Compare this row value expression with a record for order.
     *
     * @see #greaterThan(Row4)
     */
    @Support
    Condition gt(Record4<T1, T2, T3, T4> record);

    /**
     * Compare this row value expression with another row value expression for
     * order.
     *
     * @see #greaterThan(Row4)
     */
    @Support
    Condition gt(T1 t1, T2 t2, T3 t3, T4 t4);

    /**
     * Compare this row value expression with another row value expression for
     * order.
     *
     * @see #greaterThan(Row4)
     */
    @Support
    Condition gt(Field<T1> t1, Field<T2> t2, Field<T3> t3, Field<T4> t4);

    /**
     * Compare this row value expression with a subselect for order.
     *
     * @see #greaterThan(Row4)
     */
    @Support
    Condition gt(Select<? extends Record4<T1, T2, T3, T4>> select);

    /**
     * Compare this row value expression with another row value expression for
     * order.
     * <p>
     * Row order comparison predicates can be simulated in those
     * databases that do not support such predicates natively:
     * <code>(A, B) >= (1, 2)</code> is equivalent to
     * <code>A > 1 OR (A = 1 AND B > 2) OR (A = 1 AND B = 2)</code>
     */
    @Support
    Condition greaterOrEqual(Row4<T1, T2, T3, T4> row);

    /**
     * Compare this row value expression with a record for order.
     *
     * @see #greaterOrEqual(Row4)
     */
    @Support
    Condition greaterOrEqual(Record4<T1, T2, T3, T4> record);

    /**
     * Compare this row value expression with another row value expression for
     * order.
     *
     * @see #greaterOrEqual(Row4)
     */
    @Support
    Condition greaterOrEqual(T1 t1, T2 t2, T3 t3, T4 t4);

    /**
     * Compare this row value expression with another row value expression for
     * order.
     *
     * @see #greaterOrEqual(Row4)
     */
    @Support
    Condition greaterOrEqual(Field<T1> t1, Field<T2> t2, Field<T3> t3, Field<T4> t4);

    /**
     * Compare this row value expression with a subselect for order.
     *
     * @see #greaterOrEqual(Row4)
     */
    @Support
    Condition greaterOrEqual(Select<? extends Record4<T1, T2, T3, T4>> select);

    /**
     * Compare this row value expression with another row value expression for
     * order.
     *
     * @see #greaterOrEqual(Row4)
     */
    @Support
    Condition ge(Row4<T1, T2, T3, T4> row);

    /**
     * Compare this row value expression with a record for order.
     *
     * @see #greaterOrEqual(Row4)
     */
    @Support
    Condition ge(Record4<T1, T2, T3, T4> record);

    /**
     * Compare this row value expression with another row value expression for
     * order.
     *
     * @see #greaterOrEqual(Row4)
     */
    @Support
    Condition ge(T1 t1, T2 t2, T3 t3, T4 t4);

    /**
     * Compare this row value expression with another row value expression for
     * order.
     *
     * @see #greaterOrEqual(Row4)
     */
    @Support
    Condition ge(Field<T1> t1, Field<T2> t2, Field<T3> t3, Field<T4> t4);

    /**
     * Compare this row value expression with a subselect for order.
     *
     * @see #greaterOrEqual(Row4)
     */
    @Support
    Condition ge(Select<? extends Record4<T1, T2, T3, T4>> select);

    // ------------------------------------------------------------------------
    // [NOT] BETWEEN predicates
    // ------------------------------------------------------------------------

    /**
     * Check if this row value expression is within a range of two other row
     * value expressions.
     *
     * @see #between(Row4, Row4)
     */
    @Support
    BetweenAndStep4<T1, T2, T3, T4> between(T1 minValue1, T2 minValue2, T3 minValue3, T4 minValue4);

    /**
     * Check if this row value expression is within a range of two other row
     * value expressions.
     *
     * @see #between(Row4, Row4)
     */
    @Support
    BetweenAndStep4<T1, T2, T3, T4> between(Field<T1> minValue1, Field<T2> minValue2, Field<T3> minValue3, Field<T4> minValue4);

    /**
     * Check if this row value expression is within a range of two other row
     * value expressions.
     *
     * @see #between(Row4, Row4)
     */
    @Support
    BetweenAndStep4<T1, T2, T3, T4> between(Row4<T1, T2, T3, T4> minValue);

    /**
     * Check if this row value expression is within a range of two records.
     *
     * @see #between(Row4, Row4)
     */
    @Support
    BetweenAndStep4<T1, T2, T3, T4> between(Record4<T1, T2, T3, T4> minValue);

    /**
     * Check if this row value expression is within a range of two other row
     * value expressions.
     * <p>
     * This is the same as calling <code>between(minValue).and(maxValue)</code>
     * <p>
     * The expression <code>A BETWEEN B AND C</code> is equivalent to the
     * expression <code>A >= B AND A <= C</code> for those SQL dialects that do
     * not properly support the <code>BETWEEN</code> predicate for row value
     * expressions
     */
    @Support
    Condition between(Row4<T1, T2, T3, T4> minValue,
                      Row4<T1, T2, T3, T4> maxValue);

    /**
     * Check if this row value expression is within a range of two records.
     * <p>
     * This is the same as calling <code>between(minValue).and(maxValue)</code>
     *
     * @see #between(Row4, Row4)
     */
    @Support
    Condition between(Record4<T1, T2, T3, T4> minValue,
                      Record4<T1, T2, T3, T4> maxValue);

    /**
     * Check if this row value expression is within a symmetric range of two
     * other row value expressions.
     *
     * @see #betweenSymmetric(Row4, Row4)
     */
    @Support
    BetweenAndStep4<T1, T2, T3, T4> betweenSymmetric(T1 minValue1, T2 minValue2, T3 minValue3, T4 minValue4);

    /**
     * Check if this row value expression is within a symmetric range of two
     * other row value expressions.
     *
     * @see #betweenSymmetric(Row4, Row4)
     */
    @Support
    BetweenAndStep4<T1, T2, T3, T4> betweenSymmetric(Field<T1> minValue1, Field<T2> minValue2, Field<T3> minValue3, Field<T4> minValue4);

    /**
     * Check if this row value expression is within a symmetric range of two
     * other row value expressions.
     *
     * @see #betweenSymmetric(Row4, Row4)
     */
    @Support
    BetweenAndStep4<T1, T2, T3, T4> betweenSymmetric(Row4<T1, T2, T3, T4> minValue);

    /**
     * Check if this row value expression is within a symmetric range of two
     * records.
     *
     * @see #betweenSymmetric(Row4, Row4)
     */
    @Support
    BetweenAndStep4<T1, T2, T3, T4> betweenSymmetric(Record4<T1, T2, T3, T4> minValue);

    /**
     * Check if this row value expression is within a symmetric range of two
     * other row value expressions.
     * <p>
     * This is the same as calling <code>betweenSymmetric(minValue).and(maxValue)</code>
     * <p>
     * The expression <code>A BETWEEN SYMMETRIC B AND C</code> is equivalent to
     * the expression <code>(A >= B AND A <= C) OR (A >= C AND A <= B)</code>
     * for those SQL dialects that do not properly support the
     * <code>BETWEEN</code> predicate for row value expressions
     */
    @Support
    Condition betweenSymmetric(Row4<T1, T2, T3, T4> minValue,
                               Row4<T1, T2, T3, T4> maxValue);

    /**
     * Check if this row value expression is within a symmetric range of two
     * records.
     * <p>
     * This is the same as calling <code>betweenSymmetric(minValue).and(maxValue)</code>
     *
     * @see #betweenSymmetric(Row4, Row4)
     */
    @Support
    Condition betweenSymmetric(Record4<T1, T2, T3, T4> minValue,
                               Record4<T1, T2, T3, T4> maxValue);

    /**
     * Check if this row value expression is not within a range of two other
     * row value expressions.
     *
     * @see #between(Row4, Row4)
     */
    @Support
    BetweenAndStep4<T1, T2, T3, T4> notBetween(T1 minValue1, T2 minValue2, T3 minValue3, T4 minValue4);

    /**
     * Check if this row value expression is not within a range of two other
     * row value expressions.
     *
     * @see #notBetween(Row4, Row4)
     */
    @Support
    BetweenAndStep4<T1, T2, T3, T4> notBetween(Field<T1> minValue1, Field<T2> minValue2, Field<T3> minValue3, Field<T4> minValue4);

    /**
     * Check if this row value expression is not within a range of two other
     * row value expressions.
     *
     * @see #notBetween(Row4, Row4)
     */
    @Support
    BetweenAndStep4<T1, T2, T3, T4> notBetween(Row4<T1, T2, T3, T4> minValue);

    /**
     * Check if this row value expression is within a range of two records.
     *
     * @see #notBetween(Row4, Row4)
     */
    @Support
    BetweenAndStep4<T1, T2, T3, T4> notBetween(Record4<T1, T2, T3, T4> minValue);

    /**
     * Check if this row value expression is not within a range of two other
     * row value expressions.
     * <p>
     * This is the same as calling <code>notBetween(minValue).and(maxValue)</code>
     * <p>
     * The expression <code>A NOT BETWEEN B AND C</code> is equivalent to the
     * expression <code>A < B OR A > C</code> for those SQL dialects that do
     * not properly support the <code>BETWEEN</code> predicate for row value
     * expressions
     */
    @Support
    Condition notBetween(Row4<T1, T2, T3, T4> minValue,
                         Row4<T1, T2, T3, T4> maxValue);

    /**
     * Check if this row value expression is within a range of two records.
     * <p>
     * This is the same as calling <code>notBetween(minValue).and(maxValue)</code>
     *
     * @see #notBetween(Row4, Row4)
     */
    @Support
    Condition notBetween(Record4<T1, T2, T3, T4> minValue,
                         Record4<T1, T2, T3, T4> maxValue);

    /**
     * Check if this row value expression is not within a symmetric range of two
     * other row value expressions.
     *
     * @see #notBetweenSymmetric(Row4, Row4)
     */
    @Support
    BetweenAndStep4<T1, T2, T3, T4> notBetweenSymmetric(T1 minValue1, T2 minValue2, T3 minValue3, T4 minValue4);

    /**
     * Check if this row value expression is not within a symmetric range of two
     * other row value expressions.
     *
     * @see #notBetweenSymmetric(Row4, Row4)
     */
    @Support
    BetweenAndStep4<T1, T2, T3, T4> notBetweenSymmetric(Field<T1> minValue1, Field<T2> minValue2, Field<T3> minValue3, Field<T4> minValue4);

    /**
     * Check if this row value expression is not within a symmetric range of two
     * other row value expressions.
     *
     * @see #notBetweenSymmetric(Row4, Row4)
     */
    @Support
    BetweenAndStep4<T1, T2, T3, T4> notBetweenSymmetric(Row4<T1, T2, T3, T4> minValue);

    /**
     * Check if this row value expression is not within a symmetric range of two
     * records.
     *
     * @see #notBetweenSymmetric(Row4, Row4)
     */
    @Support
    BetweenAndStep4<T1, T2, T3, T4> notBetweenSymmetric(Record4<T1, T2, T3, T4> minValue);

    /**
     * Check if this row value expression is not within a symmetric range of two
     * other row value expressions.
     * <p>
     * This is the same as calling <code>notBetweenSymmetric(minValue).and(maxValue)</code>
     * <p>
     * The expression <code>A NOT BETWEEN SYMMETRIC B AND C</code> is equivalent
     * to the expression <code>(A < B OR A > C) AND (A < C OR A > B)</code> for
     * those SQL dialects that do not properly support the <code>BETWEEN</code>
     * predicate for row value expressions
     */
    @Support
    Condition notBetweenSymmetric(Row4<T1, T2, T3, T4> minValue,
                                  Row4<T1, T2, T3, T4> maxValue);

    /**
     * Check if this row value expression is not within a symmetric range of two
     * records.
     * <p>
     * This is the same as calling <code>notBetweenSymmetric(minValue).and(maxValue)</code>
     *
     * @see #notBetweenSymmetric(Row4, Row4)
     */
    @Support
    Condition notBetweenSymmetric(Record4<T1, T2, T3, T4> minValue,
                                  Record4<T1, T2, T3, T4> maxValue);

    // ------------------------------------------------------------------------
    // [NOT] DISTINCT predicates
    // ------------------------------------------------------------------------


    // ------------------------------------------------------------------------
    // [NOT] IN predicates
    // ------------------------------------------------------------------------

    /**
     * Compare this row value expression with a set of row value expressions for
     * equality.
     * <p>
     * Row IN predicates can be simulated in those databases that do not support
     * such predicates natively: <code>(A, B) IN ((1, 2), (3, 4))</code> is
     * equivalent to <code>((A, B) = (1, 2)) OR ((A, B) = (3, 4))</code>, which
     * is equivalent to <code>(A = 1 AND B = 2) OR (A = 3 AND B = 4)</code>
     */
    @Support
    Condition in(Collection<? extends Row4<T1, T2, T3, T4>> rows);

    /**
     * Compare this row value expression with a set of row value expressions for
     * equality.
     *
     * @see #in(Collection)
     */
    @Support
    Condition in(Row4<T1, T2, T3, T4>... rows);

    /**
     * Compare this row value expression with a set of records for equality.
     *
     * @see #in(Collection)
     */
    @Support
    Condition in(Record4<T1, T2, T3, T4>... record);

    /**
     * Compare this row value expression with a subselect for equality.
     *
     * @see #in(Collection)
     */
    @Support
    Condition in(Select<? extends Record4<T1, T2, T3, T4>> select);

    /**
     * Compare this row value expression with a set of row value expressions for
     * equality.
     * <p>
     * Row NOT IN predicates can be simulated in those databases that do not
     * support such predicates natively:
     * <code>(A, B) NOT IN ((1, 2), (3, 4))</code> is equivalent to
     * <code>NOT(((A, B) = (1, 2)) OR ((A, B) = (3, 4)))</code>, which is
     * equivalent to <code>NOT((A = 1 AND B = 2) OR (A = 3 AND B = 4))</code>
     */
    @Support
    Condition notIn(Collection<? extends Row4<T1, T2, T3, T4>> rows);

    /**
     * Compare this row value expression with a set of row value expressions for
     * equality.
     *
     * @see #notIn(Collection)
     */
    @Support
    Condition notIn(Row4<T1, T2, T3, T4>... rows);

    /**
     * Compare this row value expression with a set of records for non-equality.
     *
     * @see #notIn(Collection)
     */
    @Support
    Condition notIn(Record4<T1, T2, T3, T4>... record);

    /**
     * Compare this row value expression with a subselect for non-equality.
     *
     * @see #notIn(Collection)
     */
    @Support
    Condition notIn(Select<? extends Record4<T1, T2, T3, T4>> select);

}
