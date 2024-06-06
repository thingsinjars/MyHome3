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

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.With;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.NamedAttributeNode;
import javax.persistence.NamedEntityGraph;
import javax.persistence.NamedEntityGraphs;
import javax.persistence.OneToMany;
import java.util.HashSet;
import java.util.Set;

/**
 * Entity identifying a valid user in the service.
 */
/**
 * represents a valid user in a service with attributes for name, userId, email, and
 * encrypted password, as well as relationships for communities and userTokens.
 * Fields:
 * 	- name (String): represents a user's username or handle within an application or
 * service.
 * 	- userId (String): represents a unique identifier for a valid user in the service.
 * 	- email (String): in the User class represents a string value that identifies a
 * user's email address within an application or service.
 * 	- emailConfirmed (boolean): indicates whether an email address associated with
 * the User entity has been confirmed.
 * 	- encryptedPassword (String): in the User class represents a string value that
 * encapsulates an encrypted version of the user's password for security purposes.
 * 	- communities (Set<Community>): in the User class represents a set of Community
 * objects related to the User entity through a many-to-many relationship.
 * 	- userTokens (Set<SecurityToken>): in the User class represents a set of SecurityToken
 * objects associated with the user entity through a one-to-many relationship, where
 * the mappedBy attribute "tokenOwner" provides access to the related SecurityToken
 * objects.
 */
@AllArgsConstructor
@Getter
@NoArgsConstructor
@Data
@EqualsAndHashCode(callSuper = false, of = {"userId", "email"})
@Entity
@With
@NamedEntityGraphs({
    @NamedEntityGraph(
        name = "User.communities",
        attributeNodes = {
            @NamedAttributeNode("communities"),
        }
    ),
    @NamedEntityGraph(
        name = "User.userTokens",
        attributeNodes = {
            @NamedAttributeNode("userTokens"),
        }
    )
})
public class User extends BaseEntity {
  @Column(nullable = false)
  private String name;
  @Column(unique = true, nullable = false)
  private String userId;
  @Column(unique = true, nullable = false)
  private String email;
  @Column(nullable = false)
  private boolean emailConfirmed = false;
  @Column(nullable = false)
  private String encryptedPassword;
  @ManyToMany(mappedBy = "admins", fetch = FetchType.LAZY)
  private Set<Community> communities = new HashSet<>();
  @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL, mappedBy = "tokenOwner")
  private Set<SecurityToken> userTokens = new HashSet<>();
}
