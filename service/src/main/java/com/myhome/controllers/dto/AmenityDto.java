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

import java.math.BigDecimal;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.With;

/**
 * represents an amenity with identifier, name, description, price, and communityId
 * fields for construction and manipulation of DTO objects representing amenities.
 * Fields:
 * 	- id (Long): represents an identifier for each amenity, which could be a unique
 * number assigned to it.
 * 	- amenityId (String): represents a unique identifier for an amenity within a community.
 * 	- name (String): represents a string value referring to the name of an amenity.
 * 	- description (String): represents a string value of variable length that could
 * be any textual information related to an amenity.
 * 	- price (BigDecimal): represents a decimal value representing the cost of an amenity.
 * 	- communityId (String): in the AmenityDto class represents a unique identifier
 * for the community to which an amenity belongs.
 */
@AllArgsConstructor
@NoArgsConstructor
@Builder
@With
@Data
public class AmenityDto {
  private Long id;
  private String amenityId;
  private String name;
  private String description;
  private BigDecimal price;
  private String communityId;
}
