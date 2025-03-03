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

package org.apache.doris.nereids.operators;

import org.apache.doris.nereids.memo.GroupExpression;
import org.apache.doris.nereids.trees.TreeNode;
import org.apache.doris.nereids.trees.plans.Plan;
import org.apache.doris.nereids.trees.plans.PlanOperatorVisitor;

/**
 * interface for all concrete operator.
 */
public interface Operator {
    OperatorType getType();

    <NODE_TYPE extends TreeNode<NODE_TYPE>> NODE_TYPE toTreeNode(GroupExpression groupExpression);

    <R, C> R accept(PlanOperatorVisitor<R, C> visitor, Plan plan, C context);

    <R, C> R accept(OperatorVisitor<R, C> visitor, Operator operator, C context);

    <R, C> R accept(OperatorVisitor<R, C> visitor, C context);
}
