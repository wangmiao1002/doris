// Licensed to the Apache Software Foundation (ASF) under one
// or more contributor license agreements.  See the NOTICE file
// distributed with this work for additional information
// regarding copyright ownership.  The ASF licenses this file
// to you under the Apache License, Version 2.0 (the
// "License"); you may not use this file except in compliance
// with the License.  You may obtain a copy of the License at
//
//   http://www.apache.org/licenses/LICENSE-2.0
//
// Unless required by applicable law or agreed to in writing,
// software distributed under the License is distributed on an
// "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
// KIND, either express or implied.  See the License for the
// specific language governing permissions and limitations
// under the License.

package org.apache.doris.nereids.operators.plans.logical;

import org.apache.doris.nereids.operators.OperatorType;
import org.apache.doris.nereids.operators.plans.AggPhase;
import org.apache.doris.nereids.trees.expressions.Expression;
import org.apache.doris.nereids.trees.expressions.NamedExpression;
import org.apache.doris.nereids.trees.expressions.Slot;
import org.apache.doris.nereids.trees.plans.Plan;

import com.google.common.collect.ImmutableList;
import org.apache.commons.lang3.StringUtils;

import java.util.List;
import java.util.Objects;

/**
 * Logical Aggregation plan operator.
 * <p>
 * eg:select a, sum(b), c from table group by a, c;
 * groupByExprList: Column field after group by. eg: a, c;
 * outputExpressionList: Column field after select. eg: a, sum(b), c;
 * partitionExprList: Column field after partition by.
 * <p>
 * Each agg node only contains the select statement field of the same layer,
 * and other agg nodes in the subquery contain.
 * Note: In general, the output of agg is a subset of the group by column plus aggregate column.
 * In special cases. this relationship does not hold. for example, select k1+1, sum(v1) from table group by k1.
 */
public class LogicalAggregate extends LogicalUnaryOperator {

    private final boolean disassembled;
    private final List<Expression> groupByExpressionList;
    private final List<NamedExpression> outputExpressionList;
    private final List<Expression> partitionExprList;
    private final AggPhase aggPhase;

    /**
     * Desc: Constructor for LogicalAggregate.
     */
    public LogicalAggregate(List<Expression> groupByExpressionList, List<NamedExpression> outputExpressionList) {
        this(groupByExpressionList, outputExpressionList, false, AggPhase.GLOBAL);
    }

    public LogicalAggregate(List<Expression> groupByExpressionList,
            List<NamedExpression> outputExpressionList,
            boolean disassembled, AggPhase aggPhase) {
        this(groupByExpressionList, outputExpressionList, null, disassembled, aggPhase);
    }

    /**
     * Whole parameters constructor for LogicalAggregate.
     */
    public LogicalAggregate(List<Expression> groupByExpressionList,
            List<NamedExpression> outputExpressionList,
            List<Expression> partitionExprList,
            boolean disassembled, AggPhase aggPhase) {
        super(OperatorType.LOGICAL_AGGREGATION);
        this.groupByExpressionList = groupByExpressionList;
        this.outputExpressionList = outputExpressionList;
        this.partitionExprList = partitionExprList;
        this.disassembled = disassembled;
        this.aggPhase = aggPhase;
    }

    public List<Expression> getPartitionExprList() {
        return partitionExprList == null ? groupByExpressionList : partitionExprList;
    }

    public List<Expression> getGroupByExpressionList() {
        return groupByExpressionList;
    }

    public List<NamedExpression> getOutputExpressionList() {
        return outputExpressionList;
    }

    public AggPhase getAggPhase() {
        return aggPhase;
    }

    @Override
    public String toString() {
        return "LogicalAggregate (phase: [" + aggPhase.name() + "], outputExpressionList: ["
                + StringUtils.join(outputExpressionList, ", ")
                + "], groupByExprList: [" + StringUtils.join(groupByExpressionList, ", ") + "])";
    }

    @Override
    public List<Slot> computeOutput(Plan input) {
        return outputExpressionList.stream()
                .map(NamedExpression::toSlot)
                .collect(ImmutableList.toImmutableList());
    }

    @Override
    public List<Expression> getExpressions() {
        return new ImmutableList.Builder<Expression>()
                .addAll(groupByExpressionList)
                .addAll(outputExpressionList)
                .build();
    }

    public boolean isDisassembled() {
        return disassembled;
    }

    /**
     * Determine the equality with another operator
     */
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        LogicalAggregate that = (LogicalAggregate) o;
        return Objects.equals(groupByExpressionList, that.groupByExpressionList)
                && Objects.equals(outputExpressionList, that.outputExpressionList)
                && Objects.equals(partitionExprList, that.partitionExprList)
                && aggPhase == that.aggPhase;
    }

    @Override
    public int hashCode() {
        return Objects.hash(groupByExpressionList, outputExpressionList, partitionExprList, aggPhase);
    }

    public LogicalAggregate withGroupByAndOutput(List<Expression> groupByExprList,
            List<NamedExpression> outputExpressionList) {
        return new LogicalAggregate(groupByExprList, outputExpressionList, partitionExprList, disassembled, aggPhase);
    }
}
