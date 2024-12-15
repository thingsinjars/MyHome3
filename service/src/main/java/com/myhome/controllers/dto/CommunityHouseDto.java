/*
 * Copyright 2020 Prathab Murugan
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.myhome.controllers.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * has properties for a unique identifier and name for representing community houses
 * within a given context.
 * Fields:
 * 	- houseId (String): in the CommunityHouseDto class represents a unique identifier
 * for a community house within a given context.
 * 	- name (String): represents the name of a house within the context of the
 * CommunityHouseDto class.
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class CommunityHouseDto {
  private String houseId;
  private String name;
}
