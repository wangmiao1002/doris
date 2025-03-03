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
// This file is copied from
// https://github.com/ClickHouse/ClickHouse/blob/master/src/Functions/array/registerFunctionsArray.cpp
// and modified by Doris

#include "vec/functions/simple_function_factory.h"

namespace doris::vectorized {

void register_function_array_element(SimpleFunctionFactory&);
void register_function_array_index(SimpleFunctionFactory&);
void register_function_array_size(SimpleFunctionFactory&);
void register_function_array_aggregation(SimpleFunctionFactory&);
void register_function_arrays_overlap(SimpleFunctionFactory&);
void register_function_array_distinct(SimpleFunctionFactory&);

void register_function_array(SimpleFunctionFactory& factory) {
    register_function_array_element(factory);
    register_function_array_index(factory);
    register_function_array_size(factory);
    register_function_array_aggregation(factory);
    register_function_arrays_overlap(factory);
    register_function_array_distinct(factory);
}

} // namespace doris::vectorized
