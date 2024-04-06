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

package com.myhome.domain;

import java.math.BigDecimal;
import java.util.HashSet;
import java.util.Set;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.ManyToOne;
import javax.persistence.NamedAttributeNode;
import javax.persistence.NamedEntityGraph;
import javax.persistence.NamedEntityGraphs;
import javax.persistence.OneToMany;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import lombok.With;

/**
 * represents a specific amenity available for booking at a CommunityHouse, with
 * associated attributes such as ID, name, description, and price, as well as
 * relationships with other entities like Community and BookingItems.
 * Fields:
 * 	- amenityId (String): in the Amenity class represents a unique identifier for
 * each amenity.
 * 	- name (String): in the Amenity class represents a string attribute that contains
 * the name of an amenity.
 * 	- description (String): in the Amenity class represents a brief textual description
 * of the amenity, likely used for informational purposes or to provide a concise
 * summary of the amenity's purpose or features.
 * 	- price (BigDecimal): represents a monetary value.
 * 	- community (Community): in the Amenity class represents an association to a
 * Community entity.
 * 	- communityHouse (CommunityHouse): represents an association between an amenity
 * and one or more community houses where the amenity can be booked for use.
 * 	- bookingItems (Set<AmenityBookingItem>): is a collection of AmenityBookingItem
 * objects linked to the amenity via the amenityId attribute, used for storing and
 * managing bookings for the amenity.
 */
@Entity
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@With
@NamedEntityGraphs({
    @NamedEntityGraph(
        name = "Amenity.community",
        attributeNodes = {
            @NamedAttributeNode("community"),
        }
    ),
    @NamedEntityGraph(
        name = "Amenity.bookingItems",
        attributeNodes = {
            @NamedAttributeNode("bookingItems"),
        }
    )
})

public class Amenity extends BaseEntity {
  @Column(nullable = false, unique = true)
  private String amenityId;
  @Column(nullable = false)
  private String name;
  @Column(nullable = false)
  private String description;
  @Column(nullable = false)
  private BigDecimal price;
  @ManyToOne(fetch = FetchType.LAZY)
  private Community community;
  @ManyToOne
  private CommunityHouse communityHouse;
  @ToString.Exclude
  @OneToMany(fetch = FetchType.LAZY, mappedBy = "amenity")
  private Set<AmenityBookingItem> bookingItems = new HashSet<>();
}
