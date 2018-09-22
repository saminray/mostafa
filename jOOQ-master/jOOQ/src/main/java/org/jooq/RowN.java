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
 * A model type for a row value expression with degree <code>N > 22</code>.
 * <p>
 * Note: Not all databases support row value expressions, but many row value
 * expression operations can be simulated on all databases. See relevant row
 * value expression method Javadocs for details.
 *
 * @author Lukas Eder
 */
@Generated("This class was generated using jOOQ-tools")
@State
public interface RowN extends Row {

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
     * @see #equal(RowN)
     * @see #notEqual(RowN)
     * @see #lessThan(RowN)
     * @see #lessOrEqual(RowN)
     * @see #greaterThan(RowN)
     * @see #greaterOrEqual(RowN)
     */
    @Support
    Condition compare(Comparator comparator, RowN row);

    /**
     * Compare this row value expression with a record record
     * using a dynamic comparator.
     * <p>
     * See the explicit comparison methods for details. Note, not all 
     * {@link Comparator} types are supported
     *
     * @see #equal(RowN)
     * @see #notEqual(RowN)
     * @see #lessThan(RowN)
     * @see #lessOrEqual(RowN)
     * @see #greaterThan(RowN)
     * @see #greaterOrEqual(RowN)
     */
    @Support
    Condition compare(Comparator comparator, Record record);

    /**
     * Compare this row value expression with another row value expression
     * using a dynamic comparator.
     * <p>
     * See the explicit comparison methods for details. Note, not all 
     * {@link Comparator} types are supported
     *
     * @see #equal(RowN)
     * @see #notEqual(RowN)
     * @see #lessThan(RowN)
     * @see #lessOrEqual(RowN)
     * @see #greaterThan(RowN)
     * @see #greaterOrEqual(RowN)
     */
    @Support
    Condition compare(Comparator comparator, Object... values);

    /**
     * Compare this row value expression with another row value expression
     * using a dynamic comparator.
     * <p>
     * See the explicit comparison methods for details. Note, not all 
     * {@link Comparator} types are supported
     *
     * @see #equal(RowN)
     * @see #notEqual(RowN)
     * @see #lessThan(RowN)
     * @see #lessOrEqual(RowN)
     * @see #greaterThan(RowN)
     * @see #greaterOrEqual(RowN)
     */
    @Support
    Condition compare(Comparator comparator, Field<?>... values);

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
    Condition equal(RowN row);

    /**
     * Compare this row value expression with a record for equality.
     *
     * @see #equal(RowN)
     */
    @Support
    Condition equal(Record record);

    /**
     * Compare this row value expression with another row value expression for
     * equality.
     *
     * @see #equal(RowN)
     */
    @Support
    Condition equal(Object... values);

    /**
     * Compare this row value expression with another row value expression for
     * equality.
     *
     * @see #equal(RowN)
     */
    @Support
    Condition equal(Field<?>... values);

    /**
     * Compare this row value expression with a subselect for equality.
     *
     * @see #equal(RowN)
     */
    @Support
    Condition equal(Select<? extends Record> select);

    /**
     * Compare this row value expression with another row value expression for
     * equality.
     *
     * @see #equal(RowN)
     */
    @Support
    Condition eq(RowN row);

    /**
     * Compare this row value expression with a record for equality.
     *
     * @see #equal(RowN)
     */
    @Support
    Condition eq(Record record);

    /**
     * Compare this row value expression with another row value expression for
     * equality.
     *
     * @see #equal(RowN)
     */
    @Support
    Condition eq(Object... values);

    /**
     * Compare this row value expression with another row value expression for
     * equality.
     *
     * @see #equal(RowN)
     */
    @Support
    Condition eq(Field<?>... values);

    /**
     * Compare this row value expression with a subselect for equality.
     *
     * @see #equal(RowN)
     */
    @Support
    Condition eq(Select<? extends Record> select);

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
    Condition notEqual(RowN row);

    /**
     * Compare this row value expression with a record for non-equality
     *
     * @see #notEqual(RowN)
     */
    @Support
    Condition notEqual(Record record);

    /**
     * Compare this row value expression with another row value expression for.
     * non-equality
     *
     * @see #notEqual(RowN)
     */
    @Support
    Condition notEqual(Object... values);

    /**
     * Compare this row value expression with another row value expression for
     * non-equality.
     *
     * @see #notEqual(RowN)
     */
    @Support
    Condition notEqual(Field<?>... values);

    /**
     * Compare this row value expression with a subselect for non-equality.
     *
     * @see #notEqual(RowN)
     */
    @Support
    Condition notEqual(Select<? extends Record> select);

    /**
     * Compare this row value expression with another row value expression for
     * non-equality.
     *
     * @see #notEqual(RowN)
     */
    @Support
    Condition ne(RowN row);

    /**
     * Compare this row value expression with a record for non-equality.
     *
     * @see #notEqual(RowN)
     */
    @Support
    Condition ne(Record record);

    /**
     * Compare this row value expression with another row value expression for
     * non-equality.
     *
     * @see #notEqual(RowN)
     */
    @Support
    Condition ne(Object... values);

    /**
     * Compare this row value expression with another row value expression for
     * non-equality.
     *
     * @see #notEqual(RowN)
     */
    @Support
    Condition ne(Field<?>... values);

    /**
     * Compare this row value expression with a subselect for non-equality.
     *
     * @see #notEqual(RowN)
     */
    @Support
    Condition ne(Select<? extends Record> select);

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
    Condition lessThan(RowN row);

    /**
     * Compare this row value expression with a record for order.
     *
     * @see #lessThan(RowN)
     */
    @Support
    Condition lessThan(Record record);

    /**
     * Compare this row value expression with another row value expression for
     * order.
     *
     * @see #lessThan(RowN)
     */
    @Support
    Condition lessThan(Object... values);

    /**
     * Compare this row value expression with another row value expression for
     * order.
     *
     * @see #lessThan(RowN)
     */
    @Support
    Condition lessThan(Field<?>... values);

    /**
     * Compare this row value expression with a subselect for order.
     *
     * @see #lessThan(RowN)
     */
    @Support
    Condition lessThan(Select<? extends Record> select);

    /**
     * Compare this row value expression with another row value expression for
     * order.
     *
     * @see #lessThan(RowN)
     */
    @Support
    Condition lt(RowN row);

    /**
     * Compare this row value expression with a record for order.
     *
     * @see #lessThan(RowN)
     */
    @Support
    Condition lt(Record record);

    /**
     * Compare this row value expression with another row value expression for
     * order.
     *
     * @see #lessThan(RowN)
     */
    @Support
    Condition lt(Object... values);

    /**
     * Compare this row value expression with another row value expression for
     * order.
     *
     * @see #lessThan(RowN)
     */
    @Support
    Condition lt(Field<?>... values);

    /**
     * Compare this row value expression with a subselect for order.
     *
     * @see #lessThan(RowN)
     */
    @Support
    Condition lt(Select<? extends Record> select);

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
    Condition lessOrEqual(RowN row);

    /**
     * Compare this row value expression with a record for order.
     *
     * @see #lessOrEqual(RowN)
     */
    @Support
    Condition lessOrEqual(Record record);

    /**
     * Compare this row value expression with another row value expression for
     * order.
     *
     * @see #lessOrEqual(RowN)
     */
    @Support
    Condition lessOrEqual(Object... values);

    /**
     * Compare this row value expression with another row value expression for
     * order.
     *
     * @see #lessOrEqual(RowN)
     */
    @Support
    Condition lessOrEqual(Field<?>... values);

    /**
     * Compare this row value expression with a subselect for order.
     *
     * @see #lessOrEqual(RowN)
     */
    @Support
    Condition lessOrEqual(Select<? extends Record> select);

    /**
     * Compare this row value expression with another row value expression for
     * order.
     *
     * @see #lessOrEqual(RowN)
     */
    @Support
    Condition le(RowN row);

    /**
     * Compare this row value expression with a record for order.
     *
     * @see #lessOrEqual(RowN)
     */
    @Support
    Condition le(Record record);

    /**
     * Compare this row value expression with another row value expression for
     * order.
     *
     * @see #lessOrEqual(RowN)
     */
    @Support
    Condition le(Object... values);

    /**
     * Compare this row value expression with another row value expression for
     * order.
     *
     * @see #lessOrEqual(RowN)
     */
    @Support
    Condition le(Field<?>... values);

    /**
     * Compare this row value expression with a subselect for order.
     *
     * @see #lessOrEqual(RowN)
     */
    @Support
    Condition le(Select<? extends Record> select);

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
    Condition greaterThan(RowN row);

    /**
     * Compare this row value expression with a record for order.
     *
     * @see #greaterThan(RowN)
     */
    @Support
    Condition greaterThan(Record record);

    /**
     * Compare this row value expression with another row value expression for
     * order.
     *
     * @see #greaterThan(RowN)
     */
    @Support
    Condition greaterThan(Object... values);

    /**
     * Compare this row value expression with another row value expression for
     * order.
     *
     * @see #greaterThan(RowN)
     */
    @Support
    Condition greaterThan(Field<?>... values);

    /**
     * Compare this row value expression with a subselect for order.
     *
     * @see #greaterThan(RowN)
     */
    @Support
    Condition greaterThan(Select<? extends Record> select);

    /**
     * Compare this row value expression with another row value expression for
     * order.
     *
     * @see #greaterThan(RowN)
     */
    @Support
    Condition gt(RowN row);

    /**
     * Compare this row value expression with a record for order.
     *
     * @see #greaterThan(RowN)
     */
    @Support
    Condition gt(Record record);

    /**
     * Compare this row value expression with another row value expression for
     * order.
     *
     * @see #greaterThan(RowN)
     */
    @Support
    Condition gt(Object... values);

    /**
     * Compare this row value expression with another row value expression for
     * order.
     *
     * @see #greaterThan(RowN)
     */
    @Support
    Condition gt(Field<?>... values);

    /**
     * Compare this row value expression with a subselect for order.
     *
     * @see #greaterThan(RowN)
     */
    @Support
    Condition gt(Select<? extends Record> select);

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
    Condition greaterOrEqual(RowN row);

    /**
     * Compare this row value expression with a record for order.
     *
     * @see #greaterOrEqual(RowN)
     */
    @Support
    Condition greaterOrEqual(Record record);

    /**
     * Compare this row value expression with another row value expression for
     * order.
     *
     * @see #greaterOrEqual(RowN)
     */
    @Support
    Condition greaterOrEqual(Object... values);

    /**
     * Compare this row value expression with another row value expression for
     * order.
     *
     * @see #greaterOrEqual(RowN)
     */
    @Support
    Condition greaterOrEqual(Field<?>... values);

    /**
     * Compare this row value expression with a subselect for order.
     *
     * @see #greaterOrEqual(RowN)
     */
    @Support
    Condition greaterOrEqual(Select<? extends Record> select);

    /**
     * Compare this row value expression with another row value expression for
     * order.
     *
     * @see #greaterOrEqual(RowN)
     */
    @Support
    Condition ge(RowN row);

    /**
     * Compare this row value expression with a record for order.
     *
     * @see #greaterOrEqual(RowN)
     */
    @Support
    Condition ge(Record record);

    /**
     * Compare this row value expression with another row value expression for
     * order.
     *
     * @see #greaterOrEqual(RowN)
     */
    @Support
    Condition ge(Object... values);

    /**
     * Compare this row value expression with another row value expression for
     * order.
     *
     * @see #greaterOrEqual(RowN)
     */
    @Support
    Condition ge(Field<?>... values);

    /**
     * Compare this row value expression with a subselect for order.
     *
     * @see #greaterOrEqual(RowN)
     */
    @Support
    Condition ge(Select<? extends Record> select);

    // ------------------------------------------------------------------------
    // [NOT] BETWEEN predicates
    // ------------------------------------------------------------------------

    /**
     * Check if this row value expression is within a range of two other row
     * value expressions.
     *
     * @see #between(RowN, RowN)
     */
    @Support
    BetweenAndStepN between(Object... minValues);

    /**
     * Check if this row value expression is within a range of two other row
     * value expressions.
     *
     * @see #between(RowN, RowN)
     */
    @Support
    BetweenAndStepN between(Field<?>... minValues);

    /**
     * Check if this row value expression is within a range of two other row
     * value expressions.
     *
     * @see #between(RowN, RowN)
     */
    @Support
    BetweenAndStepN between(RowN minValue);

    /**
     * Check if this row value expression is within a range of two records.
     *
     * @see #between(RowN, RowN)
     */
    @Support
    BetweenAndStepN between(Record minValue);

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
    Condition between(RowN minValue,
                      RowN maxValue);

    /**
     * Check if this row value expression is within a range of two records.
     * <p>
     * This is the same as calling <code>between(minValue).and(maxValue)</code>
     *
     * @see #between(RowN, RowN)
     */
    @Support
    Condition between(Record minValue,
                      Record maxValue);

    /**
     * Check if this row value expression is within a symmetric range of two
     * other row value expressions.
     *
     * @see #betweenSymmetric(RowN, RowN)
     */
    @Support
    BetweenAndStepN betweenSymmetric(Object... minValues);

    /**
     * Check if this row value expression is within a symmetric range of two
     * other row value expressions.
     *
     * @see #betweenSymmetric(RowN, RowN)
     */
    @Support
    BetweenAndStepN betweenSymmetric(Field<?>... minValues);

    /**
     * Check if this row value expression is within a symmetric range of two
     * other row value expressions.
     *
     * @see #betweenSymmetric(RowN, RowN)
     */
    @Support
    BetweenAndStepN betweenSymmetric(RowN minValue);

    /**
     * Check if this row value expression is within a symmetric range of two
     * records.
     *
     * @see #betweenSymmetric(RowN, RowN)
     */
    @Support
    BetweenAndStepN betweenSymmetric(Record minValue);

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
    Condition betweenSymmetric(RowN minValue,
                               RowN maxValue);

    /**
     * Check if this row value expression is within a symmetric range of two
     * records.
     * <p>
     * This is the same as calling <code>betweenSymmetric(minValue).and(maxValue)</code>
     *
     * @see #betweenSymmetric(RowN, RowN)
     */
    @Support
    Condition betweenSymmetric(Record minValue,
                               Record maxValue);

    /**
     * Check if this row value expression is not within a range of two other
     * row value expressions.
     *
     * @see #between(RowN, RowN)
     */
    @Support
    BetweenAndStepN notBetween(Object... minValues);

    /**
     * Check if this row value expression is not within a range of two other
     * row value expressions.
     *
     * @see #notBetween(RowN, RowN)
     */
    @Support
    BetweenAndStepN notBetween(Field<?>... minValues);

    /**
     * Check if this row value expression is not within a range of two other
     * row value expressions.
     *
     * @see #notBetween(RowN, RowN)
     */
    @Support
    BetweenAndStepN notBetween(RowN minValue);

    /**
     * Check if this row value expression is within a range of two records.
     *
     * @see #notBetween(RowN, RowN)
     */
    @Support
    BetweenAndStepN notBetween(Record minValue);

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
    Condition notBetween(RowN minValue,
                         RowN maxValue);

    /**
     * Check if this row value expression is within a range of two records.
     * <p>
     * This is the same as calling <code>notBetween(minValue).and(maxValue)</code>
     *
     * @see #notBetween(RowN, RowN)
     */
    @Support
    Condition notBetween(Record minValue,
                         Record maxValue);

    /**
     * Check if this row value expression is not within a symmetric range of two
     * other row value expressions.
     *
     * @see #notBetweenSymmetric(RowN, RowN)
     */
    @Support
    BetweenAndStepN notBetweenSymmetric(Object... minValues);

    /**
     * Check if this row value expression is not within a symmetric range of two
     * other row value expressions.
     *
     * @see #notBetweenSymmetric(RowN, RowN)
     */
    @Support
    BetweenAndStepN notBetweenSymmetric(Field<?>... minValues);

    /**
     * Check if this row value expression is not within a symmetric range of two
     * other row value expressions.
     *
     * @see #notBetweenSymmetric(RowN, RowN)
     */
    @Support
    BetweenAndStepN notBetweenSymmetric(RowN minValue);

    /**
     * Check if this row value expression is not within a symmetric range of two
     * records.
     *
     * @see #notBetweenSymmetric(RowN, RowN)
     */
    @Support
    BetweenAndStepN notBetweenSymmetric(Record minValue);

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
    Condition notBetweenSymmetric(RowN minValue,
                                  RowN maxValue);

    /**
     * Check if this row value expression is not within a symmetric range of two
     * records.
     * <p>
     * This is the same as calling <code>notBetweenSymmetric(minValue).and(maxValue)</code>
     *
     * @see #notBetweenSymmetric(RowN, RowN)
     */
    @Support
    Condition notBetweenSymmetric(Record minValue,
                                  Record maxValue);

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
    Condition in(Collection<? extends RowN> rows);

    /**
     * Compare this row value expression with a set of row value expressions for
     * equality.
     *
     * @see #in(Collection)
     */
    @Support
    Condition in(RowN... rows);

    /**
     * Compare this row value expression with a set of records for equality.
     *
     * @see #in(Collection)
     */
    @Support
    Condition in(Record... record);

    /**
     * Compare this row value expression with a subselect for equality.
     *
     * @see #in(Collection)
     */
    @Support
    Condition in(Select<? extends Record> select);

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
    Condition notIn(Collection<? extends RowN> rows);

    /**
     * Compare this row value expression with a set of row value expressions for
     * equality.
     *
     * @see #notIn(Collection)
     */
    @Support
    Condition notIn(RowN... rows);

    /**
     * Compare this row value expression with a set of records for non-equality.
     *
     * @see #notIn(Collection)
     */
    @Support
    Condition notIn(Record... record);

    /**
     * Compare this row value expression with a subselect for non-equality.
     *
     * @see #notIn(Collection)
     */
    @Support
    Condition notIn(Select<? extends Record> select);

}
