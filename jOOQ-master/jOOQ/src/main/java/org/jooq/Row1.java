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
 * A model type for a row value expression with degree <code>1</code>.
 * <p>
 * Note: Not all databases support row value expressions, but many row value
 * expression operations can be simulated on all databases. See relevant row
 * value expression method Javadocs for details.
 *
 * @author Lukas Eder
 */
@Generated("This class was generated using jOOQ-tools")
@State
public interface Row1<T1> extends Row {

    // ------------------------------------------------------------------------
    // Field accessors
    // ------------------------------------------------------------------------

    /**
     * Get the first field.
     */
    Field<T1> field1();

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
     * @see #equal(Row1)
     * @see #notEqual(Row1)
     * @see #lessThan(Row1)
     * @see #lessOrEqual(Row1)
     * @see #greaterThan(Row1)
     * @see #greaterOrEqual(Row1)
     */
    @Support
    Condition compare(Comparator comparator, Row1<T1> row);

    /**
     * Compare this row value expression with a record record
     * using a dynamic comparator.
     * <p>
     * See the explicit comparison methods for details. Note, not all 
     * {@link Comparator} types are supported
     *
     * @see #equal(Row1)
     * @see #notEqual(Row1)
     * @see #lessThan(Row1)
     * @see #lessOrEqual(Row1)
     * @see #greaterThan(Row1)
     * @see #greaterOrEqual(Row1)
     */
    @Support
    Condition compare(Comparator comparator, Record1<T1> record);

    /**
     * Compare this row value expression with another row value expression
     * using a dynamic comparator.
     * <p>
     * See the explicit comparison methods for details. Note, not all 
     * {@link Comparator} types are supported
     *
     * @see #equal(Row1)
     * @see #notEqual(Row1)
     * @see #lessThan(Row1)
     * @see #lessOrEqual(Row1)
     * @see #greaterThan(Row1)
     * @see #greaterOrEqual(Row1)
     */
    @Support
    Condition compare(Comparator comparator, T1 t1);

    /**
     * Compare this row value expression with another row value expression
     * using a dynamic comparator.
     * <p>
     * See the explicit comparison methods for details. Note, not all 
     * {@link Comparator} types are supported
     *
     * @see #equal(Row1)
     * @see #notEqual(Row1)
     * @see #lessThan(Row1)
     * @see #lessOrEqual(Row1)
     * @see #greaterThan(Row1)
     * @see #greaterOrEqual(Row1)
     */
    @Support
    Condition compare(Comparator comparator, Field<T1> t1);

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
    Condition equal(Row1<T1> row);

    /**
     * Compare this row value expression with a record for equality.
     *
     * @see #equal(Row1)
     */
    @Support
    Condition equal(Record1<T1> record);

    /**
     * Compare this row value expression with another row value expression for
     * equality.
     *
     * @see #equal(Row1)
     */
    @Support
    Condition equal(T1 t1);

    /**
     * Compare this row value expression with another row value expression for
     * equality.
     *
     * @see #equal(Row1)
     */
    @Support
    Condition equal(Field<T1> t1);

    /**
     * Compare this row value expression with a subselect for equality.
     *
     * @see #equal(Row1)
     */
    @Support
    Condition equal(Select<? extends Record1<T1>> select);

    /**
     * Compare this row value expression with another row value expression for
     * equality.
     *
     * @see #equal(Row1)
     */
    @Support
    Condition eq(Row1<T1> row);

    /**
     * Compare this row value expression with a record for equality.
     *
     * @see #equal(Row1)
     */
    @Support
    Condition eq(Record1<T1> record);

    /**
     * Compare this row value expression with another row value expression for
     * equality.
     *
     * @see #equal(Row1)
     */
    @Support
    Condition eq(T1 t1);

    /**
     * Compare this row value expression with another row value expression for
     * equality.
     *
     * @see #equal(Row1)
     */
    @Support
    Condition eq(Field<T1> t1);

    /**
     * Compare this row value expression with a subselect for equality.
     *
     * @see #equal(Row1)
     */
    @Support
    Condition eq(Select<? extends Record1<T1>> select);

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
    Condition notEqual(Row1<T1> row);

    /**
     * Compare this row value expression with a record for non-equality
     *
     * @see #notEqual(Row1)
     */
    @Support
    Condition notEqual(Record1<T1> record);

    /**
     * Compare this row value expression with another row value expression for.
     * non-equality
     *
     * @see #notEqual(Row1)
     */
    @Support
    Condition notEqual(T1 t1);

    /**
     * Compare this row value expression with another row value expression for
     * non-equality.
     *
     * @see #notEqual(Row1)
     */
    @Support
    Condition notEqual(Field<T1> t1);

    /**
     * Compare this row value expression with a subselect for non-equality.
     *
     * @see #notEqual(Row1)
     */
    @Support
    Condition notEqual(Select<? extends Record1<T1>> select);

    /**
     * Compare this row value expression with another row value expression for
     * non-equality.
     *
     * @see #notEqual(Row1)
     */
    @Support
    Condition ne(Row1<T1> row);

    /**
     * Compare this row value expression with a record for non-equality.
     *
     * @see #notEqual(Row1)
     */
    @Support
    Condition ne(Record1<T1> record);

    /**
     * Compare this row value expression with another row value expression for
     * non-equality.
     *
     * @see #notEqual(Row1)
     */
    @Support
    Condition ne(T1 t1);

    /**
     * Compare this row value expression with another row value expression for
     * non-equality.
     *
     * @see #notEqual(Row1)
     */
    @Support
    Condition ne(Field<T1> t1);

    /**
     * Compare this row value expression with a subselect for non-equality.
     *
     * @see #notEqual(Row1)
     */
    @Support
    Condition ne(Select<? extends Record1<T1>> select);

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
    Condition lessThan(Row1<T1> row);

    /**
     * Compare this row value expression with a record for order.
     *
     * @see #lessThan(Row1)
     */
    @Support
    Condition lessThan(Record1<T1> record);

    /**
     * Compare this row value expression with another row value expression for
     * order.
     *
     * @see #lessThan(Row1)
     */
    @Support
    Condition lessThan(T1 t1);

    /**
     * Compare this row value expression with another row value expression for
     * order.
     *
     * @see #lessThan(Row1)
     */
    @Support
    Condition lessThan(Field<T1> t1);

    /**
     * Compare this row value expression with a subselect for order.
     *
     * @see #lessThan(Row1)
     */
    @Support
    Condition lessThan(Select<? extends Record1<T1>> select);

    /**
     * Compare this row value expression with another row value expression for
     * order.
     *
     * @see #lessThan(Row1)
     */
    @Support
    Condition lt(Row1<T1> row);

    /**
     * Compare this row value expression with a record for order.
     *
     * @see #lessThan(Row1)
     */
    @Support
    Condition lt(Record1<T1> record);

    /**
     * Compare this row value expression with another row value expression for
     * order.
     *
     * @see #lessThan(Row1)
     */
    @Support
    Condition lt(T1 t1);

    /**
     * Compare this row value expression with another row value expression for
     * order.
     *
     * @see #lessThan(Row1)
     */
    @Support
    Condition lt(Field<T1> t1);

    /**
     * Compare this row value expression with a subselect for order.
     *
     * @see #lessThan(Row1)
     */
    @Support
    Condition lt(Select<? extends Record1<T1>> select);

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
    Condition lessOrEqual(Row1<T1> row);

    /**
     * Compare this row value expression with a record for order.
     *
     * @see #lessOrEqual(Row1)
     */
    @Support
    Condition lessOrEqual(Record1<T1> record);

    /**
     * Compare this row value expression with another row value expression for
     * order.
     *
     * @see #lessOrEqual(Row1)
     */
    @Support
    Condition lessOrEqual(T1 t1);

    /**
     * Compare this row value expression with another row value expression for
     * order.
     *
     * @see #lessOrEqual(Row1)
     */
    @Support
    Condition lessOrEqual(Field<T1> t1);

    /**
     * Compare this row value expression with a subselect for order.
     *
     * @see #lessOrEqual(Row1)
     */
    @Support
    Condition lessOrEqual(Select<? extends Record1<T1>> select);

    /**
     * Compare this row value expression with another row value expression for
     * order.
     *
     * @see #lessOrEqual(Row1)
     */
    @Support
    Condition le(Row1<T1> row);

    /**
     * Compare this row value expression with a record for order.
     *
     * @see #lessOrEqual(Row1)
     */
    @Support
    Condition le(Record1<T1> record);

    /**
     * Compare this row value expression with another row value expression for
     * order.
     *
     * @see #lessOrEqual(Row1)
     */
    @Support
    Condition le(T1 t1);

    /**
     * Compare this row value expression with another row value expression for
     * order.
     *
     * @see #lessOrEqual(Row1)
     */
    @Support
    Condition le(Field<T1> t1);

    /**
     * Compare this row value expression with a subselect for order.
     *
     * @see #lessOrEqual(Row1)
     */
    @Support
    Condition le(Select<? extends Record1<T1>> select);

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
    Condition greaterThan(Row1<T1> row);

    /**
     * Compare this row value expression with a record for order.
     *
     * @see #greaterThan(Row1)
     */
    @Support
    Condition greaterThan(Record1<T1> record);

    /**
     * Compare this row value expression with another row value expression for
     * order.
     *
     * @see #greaterThan(Row1)
     */
    @Support
    Condition greaterThan(T1 t1);

    /**
     * Compare this row value expression with another row value expression for
     * order.
     *
     * @see #greaterThan(Row1)
     */
    @Support
    Condition greaterThan(Field<T1> t1);

    /**
     * Compare this row value expression with a subselect for order.
     *
     * @see #greaterThan(Row1)
     */
    @Support
    Condition greaterThan(Select<? extends Record1<T1>> select);

    /**
     * Compare this row value expression with another row value expression for
     * order.
     *
     * @see #greaterThan(Row1)
     */
    @Support
    Condition gt(Row1<T1> row);

    /**
     * Compare this row value expression with a record for order.
     *
     * @see #greaterThan(Row1)
     */
    @Support
    Condition gt(Record1<T1> record);

    /**
     * Compare this row value expression with another row value expression for
     * order.
     *
     * @see #greaterThan(Row1)
     */
    @Support
    Condition gt(T1 t1);

    /**
     * Compare this row value expression with another row value expression for
     * order.
     *
     * @see #greaterThan(Row1)
     */
    @Support
    Condition gt(Field<T1> t1);

    /**
     * Compare this row value expression with a subselect for order.
     *
     * @see #greaterThan(Row1)
     */
    @Support
    Condition gt(Select<? extends Record1<T1>> select);

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
    Condition greaterOrEqual(Row1<T1> row);

    /**
     * Compare this row value expression with a record for order.
     *
     * @see #greaterOrEqual(Row1)
     */
    @Support
    Condition greaterOrEqual(Record1<T1> record);

    /**
     * Compare this row value expression with another row value expression for
     * order.
     *
     * @see #greaterOrEqual(Row1)
     */
    @Support
    Condition greaterOrEqual(T1 t1);

    /**
     * Compare this row value expression with another row value expression for
     * order.
     *
     * @see #greaterOrEqual(Row1)
     */
    @Support
    Condition greaterOrEqual(Field<T1> t1);

    /**
     * Compare this row value expression with a subselect for order.
     *
     * @see #greaterOrEqual(Row1)
     */
    @Support
    Condition greaterOrEqual(Select<? extends Record1<T1>> select);

    /**
     * Compare this row value expression with another row value expression for
     * order.
     *
     * @see #greaterOrEqual(Row1)
     */
    @Support
    Condition ge(Row1<T1> row);

    /**
     * Compare this row value expression with a record for order.
     *
     * @see #greaterOrEqual(Row1)
     */
    @Support
    Condition ge(Record1<T1> record);

    /**
     * Compare this row value expression with another row value expression for
     * order.
     *
     * @see #greaterOrEqual(Row1)
     */
    @Support
    Condition ge(T1 t1);

    /**
     * Compare this row value expression with another row value expression for
     * order.
     *
     * @see #greaterOrEqual(Row1)
     */
    @Support
    Condition ge(Field<T1> t1);

    /**
     * Compare this row value expression with a subselect for order.
     *
     * @see #greaterOrEqual(Row1)
     */
    @Support
    Condition ge(Select<? extends Record1<T1>> select);

    // ------------------------------------------------------------------------
    // [NOT] BETWEEN predicates
    // ------------------------------------------------------------------------

    /**
     * Check if this row value expression is within a range of two other row
     * value expressions.
     *
     * @see #between(Row1, Row1)
     */
    @Support
    BetweenAndStep1<T1> between(T1 minValue1);

    /**
     * Check if this row value expression is within a range of two other row
     * value expressions.
     *
     * @see #between(Row1, Row1)
     */
    @Support
    BetweenAndStep1<T1> between(Field<T1> minValue1);

    /**
     * Check if this row value expression is within a range of two other row
     * value expressions.
     *
     * @see #between(Row1, Row1)
     */
    @Support
    BetweenAndStep1<T1> between(Row1<T1> minValue);

    /**
     * Check if this row value expression is within a range of two records.
     *
     * @see #between(Row1, Row1)
     */
    @Support
    BetweenAndStep1<T1> between(Record1<T1> minValue);

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
    Condition between(Row1<T1> minValue,
                      Row1<T1> maxValue);

    /**
     * Check if this row value expression is within a range of two records.
     * <p>
     * This is the same as calling <code>between(minValue).and(maxValue)</code>
     *
     * @see #between(Row1, Row1)
     */
    @Support
    Condition between(Record1<T1> minValue,
                      Record1<T1> maxValue);

    /**
     * Check if this row value expression is within a symmetric range of two
     * other row value expressions.
     *
     * @see #betweenSymmetric(Row1, Row1)
     */
    @Support
    BetweenAndStep1<T1> betweenSymmetric(T1 minValue1);

    /**
     * Check if this row value expression is within a symmetric range of two
     * other row value expressions.
     *
     * @see #betweenSymmetric(Row1, Row1)
     */
    @Support
    BetweenAndStep1<T1> betweenSymmetric(Field<T1> minValue1);

    /**
     * Check if this row value expression is within a symmetric range of two
     * other row value expressions.
     *
     * @see #betweenSymmetric(Row1, Row1)
     */
    @Support
    BetweenAndStep1<T1> betweenSymmetric(Row1<T1> minValue);

    /**
     * Check if this row value expression is within a symmetric range of two
     * records.
     *
     * @see #betweenSymmetric(Row1, Row1)
     */
    @Support
    BetweenAndStep1<T1> betweenSymmetric(Record1<T1> minValue);

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
    Condition betweenSymmetric(Row1<T1> minValue,
                               Row1<T1> maxValue);

    /**
     * Check if this row value expression is within a symmetric range of two
     * records.
     * <p>
     * This is the same as calling <code>betweenSymmetric(minValue).and(maxValue)</code>
     *
     * @see #betweenSymmetric(Row1, Row1)
     */
    @Support
    Condition betweenSymmetric(Record1<T1> minValue,
                               Record1<T1> maxValue);

    /**
     * Check if this row value expression is not within a range of two other
     * row value expressions.
     *
     * @see #between(Row1, Row1)
     */
    @Support
    BetweenAndStep1<T1> notBetween(T1 minValue1);

    /**
     * Check if this row value expression is not within a range of two other
     * row value expressions.
     *
     * @see #notBetween(Row1, Row1)
     */
    @Support
    BetweenAndStep1<T1> notBetween(Field<T1> minValue1);

    /**
     * Check if this row value expression is not within a range of two other
     * row value expressions.
     *
     * @see #notBetween(Row1, Row1)
     */
    @Support
    BetweenAndStep1<T1> notBetween(Row1<T1> minValue);

    /**
     * Check if this row value expression is within a range of two records.
     *
     * @see #notBetween(Row1, Row1)
     */
    @Support
    BetweenAndStep1<T1> notBetween(Record1<T1> minValue);

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
    Condition notBetween(Row1<T1> minValue,
                         Row1<T1> maxValue);

    /**
     * Check if this row value expression is within a range of two records.
     * <p>
     * This is the same as calling <code>notBetween(minValue).and(maxValue)</code>
     *
     * @see #notBetween(Row1, Row1)
     */
    @Support
    Condition notBetween(Record1<T1> minValue,
                         Record1<T1> maxValue);

    /**
     * Check if this row value expression is not within a symmetric range of two
     * other row value expressions.
     *
     * @see #notBetweenSymmetric(Row1, Row1)
     */
    @Support
    BetweenAndStep1<T1> notBetweenSymmetric(T1 minValue1);

    /**
     * Check if this row value expression is not within a symmetric range of two
     * other row value expressions.
     *
     * @see #notBetweenSymmetric(Row1, Row1)
     */
    @Support
    BetweenAndStep1<T1> notBetweenSymmetric(Field<T1> minValue1);

    /**
     * Check if this row value expression is not within a symmetric range of two
     * other row value expressions.
     *
     * @see #notBetweenSymmetric(Row1, Row1)
     */
    @Support
    BetweenAndStep1<T1> notBetweenSymmetric(Row1<T1> minValue);

    /**
     * Check if this row value expression is not within a symmetric range of two
     * records.
     *
     * @see #notBetweenSymmetric(Row1, Row1)
     */
    @Support
    BetweenAndStep1<T1> notBetweenSymmetric(Record1<T1> minValue);

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
    Condition notBetweenSymmetric(Row1<T1> minValue,
                                  Row1<T1> maxValue);

    /**
     * Check if this row value expression is not within a symmetric range of two
     * records.
     * <p>
     * This is the same as calling <code>notBetweenSymmetric(minValue).and(maxValue)</code>
     *
     * @see #notBetweenSymmetric(Row1, Row1)
     */
    @Support
    Condition notBetweenSymmetric(Record1<T1> minValue,
                                  Record1<T1> maxValue);

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
    Condition in(Collection<? extends Row1<T1>> rows);

    /**
     * Compare this row value expression with a set of row value expressions for
     * equality.
     *
     * @see #in(Collection)
     */
    @Support
    Condition in(Row1<T1>... rows);

    /**
     * Compare this row value expression with a set of records for equality.
     *
     * @see #in(Collection)
     */
    @Support
    Condition in(Record1<T1>... record);

    /**
     * Compare this row value expression with a subselect for equality.
     *
     * @see #in(Collection)
     */
    @Support
    Condition in(Select<? extends Record1<T1>> select);

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
    Condition notIn(Collection<? extends Row1<T1>> rows);

    /**
     * Compare this row value expression with a set of row value expressions for
     * equality.
     *
     * @see #notIn(Collection)
     */
    @Support
    Condition notIn(Row1<T1>... rows);

    /**
     * Compare this row value expression with a set of records for non-equality.
     *
     * @see #notIn(Collection)
     */
    @Support
    Condition notIn(Record1<T1>... record);

    /**
     * Compare this row value expression with a subselect for non-equality.
     *
     * @see #notIn(Collection)
     */
    @Support
    Condition notIn(Select<? extends Record1<T1>> select);

}
