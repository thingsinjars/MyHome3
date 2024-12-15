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

import java.util.Set;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * represents a community object with an ID, community ID, name, district, and set
 * of admins.
 * Fields:
 * 	- id (Long): represents a unique identifier for a community object in the program.
 * 	- communityId (String): represents a unique identifier for a specific community.
 * 	- name (String): represents a string value representing the name of a community.
 * 	- district (String): in the CommunityDto class represents a string value specifying
 * the name of the district associated with the community.
 * 	- admins (Set<UserDto>): in the CommunityDto class represents a set of UserDtos
 * containing the administrators of a community.
 */
@AllArgsConstructor
@NoArgsConstructor
@Data
public class CommunityDto {
  private Long id;
  private String communityId;
  private String name;
  private String district;
  private Set<UserDto> admins;
}
