-- Licensed to the Apache Software Foundation (ASF) under one
-- or more contributor license agreements.  See the NOTICE file
-- distributed with this work for additional information
-- regarding copyright ownership.  The ASF licenses this file
-- to you under the Apache License, Version 2.0 (the
-- "License"); you may not use this file except in compliance
-- with the License.  You may obtain a copy of the License at
--
--   http://www.apache.org/licenses/LICENSE-2.0
--
-- Unless required by applicable law or agreed to in writing,
-- software distributed under the License is distributed on an
-- "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
-- KIND, either express or implied.  See the License for the
-- specific language governing permissions and limitations
-- under the License.

-- Modified

select /*+SET_VAR(exec_mem_limit=8589934592, parallel_fragment_exec_instance_num=1, enable_vectorized_engine=true, batch_size=4096, disable_join_reorder=true, enable_cost_based_join_reorder=false, enable_projection=true) */
    s_acctbal,
    s_name,
    n_name,
    p_partkey,
    p_mfgr,
    s_address,
    s_phone,
    s_comment
from
partsupp,
(
  select ps_partkey, min(ps_supplycost) as ps_s from
  partsupp, supplier, nation, region
  where s_suppkey = ps_suppkey
    and s_nationkey = n_nationkey
    and n_regionkey = r_regionkey
    and r_name = 'EUROPE'
  group by ps_partkey
) t1,
supplier,
part,
nation,
region
where p_partkey = t1.ps_partkey
    and p_partkey = partsupp.ps_partkey
    and s_suppkey = ps_suppkey
    and p_size = 15
    and p_type like '%BRASS'
    and s_nationkey = n_nationkey
    and n_regionkey = r_regionkey
    and r_name = 'EUROPE'
    and ps_supplycost = t1.ps_s
order by
    s_acctbal desc,
    n_name,
    s_name,
    p_partkey
limit 100;
