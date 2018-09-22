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

import static org.jooq.Clause.CONDITION;
import static org.jooq.Clause.CONDITION_AND;
import static org.jooq.Clause.CONDITION_OR;
import static org.jooq.Operator.AND;
import static org.jooq.impl.DSL.trueCondition;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.jooq.Clause;
import org.jooq.Condition;
import org.jooq.Context;
import org.jooq.Operator;

/**
 * @author Lukas Eder
 */
class CombinedCondition extends AbstractCondition {

    private static final long     serialVersionUID = -7373293246207052549L;
    private static final Clause[] CLAUSES_AND      = { CONDITION, CONDITION_AND };
    private static final Clause[] CLAUSES_OR       = { CONDITION, CONDITION_OR };

    private final Operator        operator;
    private final List<Condition> conditions;

    CombinedCondition(Operator operator, Collection<? extends Condition> conditions) {
        if (operator == null) {
            throw new IllegalArgumentException("The argument 'operator' must not be null");
        }
        if (conditions == null) {
            throw new IllegalArgumentException("The argument 'conditions' must not be null");
        }
        for (Condition condition : conditions) {
            if (condition == null) {
                throw new IllegalArgumentException("The argument 'conditions' must not contain null");
            }
        }

        this.operator = operator;
        this.conditions = new ArrayList<Condition>();

        init(operator, conditions);
    }

    private final void init(Operator op, Collection<? extends Condition> cond) {
        for (Condition condition : cond) {
            if (condition instanceof CombinedCondition) {
                CombinedCondition combinedCondition = (CombinedCondition) condition;
                if (combinedCondition.operator == op) {
                    this.conditions.addAll(combinedCondition.conditions);
                }
                else {
                    this.conditions.add(condition);
                }
            }
            else {
                this.conditions.add(condition);
            }
        }
    }

    @Override
    public final Clause[] clauses(Context<?> ctx) {
        return operator == AND ? CLAUSES_AND : CLAUSES_OR;
    }

    @Override
    public final void accept(Context<?> ctx) {
        if (conditions.isEmpty()) {
            ctx.visit(trueCondition());
        }
        else if (conditions.size() == 1) {
            ctx.visit(conditions.get(0));
        }
        else {
            ctx.sql("(")
               .formatIndentStart()
               .formatNewLine();

            String operatorName = operator.name().toLowerCase() + " ";
            String separator = "";

            for (int i = 0; i < conditions.size(); i++) {
                if (i > 0) {
                    ctx.formatSeparator();
                }

                ctx.keyword(separator);
                ctx.visit(conditions.get(i));
                separator = operatorName;
            }

            ctx.formatIndentEnd()
               .formatNewLine()
               .sql(")");
        }
    }
}
