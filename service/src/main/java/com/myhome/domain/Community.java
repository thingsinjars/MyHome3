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

import java.util.HashSet;
import java.util.Set;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.ManyToMany;
import javax.persistence.NamedAttributeNode;
import javax.persistence.NamedEntityGraph;
import javax.persistence.NamedEntityGraphs;
import javax.persistence.OneToMany;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;
import lombok.With;

/**
 * Entity identifying a valid user in the service.
 */
/**
 * represents a valid user in the service, with attributes for name, communityId,
 * district, admins, houses, amenities, and relationships with other entities through
 * many-to-many and one-to-many associations.
 * Fields:
 * 	- admins (Set<User>): in the Community class represents a set of user accounts
 * associated with the community.
 * 	- houses (Set<CommunityHouse>): of the Community class contains a set of House
 * objects associated with each community.
 * 	- name (String): in the Community class represents a unique identifier for a
 * community within a specific district.
 * 	- communityId (String): in the Community class represents an identifier for a
 * specific community within the application's scope.
 * 	- district (String): represents a string value representing a geographical location
 * associated with the Community entity.
 * 	- amenities (Set<Amenity>): in the Community class represents a set of Amenity
 * objects associated with the community, which can be retrieved through the many-to-many
 * relationship with the Amenity class.
 */
@AllArgsConstructor
@NoArgsConstructor
@Data
@EqualsAndHashCode(callSuper = false, of = {"communityId", "name", "district"})
@Entity
@With
@NamedEntityGraphs({
    @NamedEntityGraph(
        name = "Community.amenities",
        attributeNodes = {
            @NamedAttributeNode("amenities"),
        }
    ),
    @NamedEntityGraph(
        name = "Community.admins",
        attributeNodes = {
            @NamedAttributeNode("admins"),
        }
    ),
    @NamedEntityGraph(
        name = "Community.houses",
        attributeNodes = {
            @NamedAttributeNode("houses"),
        }
    )
})
public class Community extends BaseEntity {
  @ToString.Exclude
  @ManyToMany(fetch = FetchType.LAZY)
  private Set<User> admins = new HashSet<>();
  @ToString.Exclude
  @OneToMany(fetch = FetchType.LAZY)
  private Set<CommunityHouse> houses = new HashSet<>();
  @Column(nullable = false)
  private String name;
  @Column(unique = true, nullable = false)
  private String communityId;
  @Column(nullable = false)
  private String district;
  @ToString.Exclude
  @OneToMany(fetch = FetchType.LAZY, mappedBy = "community", orphanRemoval = true)
  private Set<Amenity> amenities = new HashSet<>();
}
