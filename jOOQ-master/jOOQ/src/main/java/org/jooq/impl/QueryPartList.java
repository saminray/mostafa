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

import static java.util.Arrays.asList;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;

import org.jooq.Clause;
import org.jooq.Context;
import org.jooq.QueryPart;

/**
 * @author Lukas Eder
 */
class QueryPartList<T extends QueryPart> extends AbstractQueryPart implements List<T> {

    private static final long serialVersionUID = -2936922742534009564L;
    private final List<T>     wrappedList;

    QueryPartList() {
        this((Collection<T>) null);
    }

    QueryPartList(Collection<? extends T> wrappedList) {
        super();

        this.wrappedList = new ArrayList<T>();

        if (wrappedList != null) {
            addAll(wrappedList);
        }
    }

    QueryPartList(T... wrappedList) {
        this(asList(wrappedList));
    }

    @Override
    public final void accept(Context<?> ctx) {

        // Some lists render different SQL when empty
        if (isEmpty()) {
            toSQLEmptyList(ctx);
        }

        else {
            String separator = "";
            boolean indent = (size() > 1);

            if (indent)
                ctx.formatIndentStart();

            for (T queryPart : this) {
                ctx.sql(separator);

                if (indent)
                    ctx.formatNewLine();

                ctx.visit(queryPart);
                separator = ", ";
            }

            if (indent)
                ctx.formatIndentEnd();
        }
    }

    @Override
    public final Clause[] clauses(Context<?> ctx) {
        return null;
    }

    /**
     * Subclasses may override this method
     */
    @SuppressWarnings("unused")
    protected void toSQLEmptyList(Context<?> context) {
    }



    // -------------------------------------------------------------------------
    // Implementations from the List API
    // -------------------------------------------------------------------------

    @Override
    public final int size() {
        return wrappedList.size();
    }

    @Override
    public final boolean isEmpty() {
        return wrappedList.isEmpty();
    }

    @Override
    public final boolean contains(Object o) {
        return wrappedList.contains(o);
    }

    @Override
    public final Iterator<T> iterator() {
        return wrappedList.iterator();
    }

    @Override
    public final Object[] toArray() {
        return wrappedList.toArray();
    }

    @Override
    public final <E> E[] toArray(E[] a) {
        return wrappedList.toArray(a);
    }

    @Override
    public final boolean add(T e) {
        if (e != null) {
            return wrappedList.add(e);
        }

        return false;
    }

    @Override
    public final boolean remove(Object o) {
        return wrappedList.remove(o);
    }

    @Override
    public final boolean containsAll(Collection<?> c) {
        return wrappedList.containsAll(c);
    }

    @Override
    public final boolean addAll(Collection<? extends T> c) {
        return wrappedList.addAll(removeNulls(c));
    }

    @Override
    public final boolean addAll(int index, Collection<? extends T> c) {
        return wrappedList.addAll(index, removeNulls(c));
    }

    private final Collection<? extends T> removeNulls(Collection<? extends T> c) {

        // [#2145] Collections that contain nulls are quite rare, so it is wise
        // to add a relatively cheap defender check to avoid unnecessary loops
        if (c.contains(null)) {
            List<T> list = new ArrayList<T>(c);
            Iterator<T> it = list.iterator();

            while (it.hasNext()) {
                if (it.next() == null) {
                    it.remove();
                }
            }

            return list;
        }
        else {
            return c;
        }
    }

    @Override
    public final boolean removeAll(Collection<?> c) {
        return wrappedList.removeAll(c);
    }

    @Override
    public final boolean retainAll(Collection<?> c) {
        return wrappedList.retainAll(c);
    }

    @Override
    public final void clear() {
        wrappedList.clear();
    }

    @Override
    public final T get(int index) {
        return wrappedList.get(index);
    }

    @Override
    public final T set(int index, T element) {
        if (element != null) {
            return wrappedList.set(index, element);
        }

        return null;
    }

    @Override
    public final void add(int index, T element) {
        if (element != null) {
            wrappedList.add(index, element);
        }
    }

    @Override
    public final T remove(int index) {
        return wrappedList.remove(index);
    }

    @Override
    public final int indexOf(Object o) {
        return wrappedList.indexOf(o);
    }

    @Override
    public final int lastIndexOf(Object o) {
        return wrappedList.lastIndexOf(o);
    }

    @Override
    public final ListIterator<T> listIterator() {
        return wrappedList.listIterator();
    }

    @Override
    public final ListIterator<T> listIterator(int index) {
        return wrappedList.listIterator(index);
    }

    @Override
    public final List<T> subList(int fromIndex, int toIndex) {
        return wrappedList.subList(fromIndex, toIndex);
    }
}
