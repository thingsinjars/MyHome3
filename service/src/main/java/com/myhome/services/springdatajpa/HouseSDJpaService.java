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

package com.myhome.services.springdatajpa;

import com.myhome.domain.CommunityHouse;
import com.myhome.domain.HouseMember;
import com.myhome.repositories.CommunityHouseRepository;
import com.myhome.repositories.HouseMemberDocumentRepository;
import com.myhome.repositories.HouseMemberRepository;
import com.myhome.services.HouseService;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

/**
 * provides methods for managing house members within a Spring Boot application using
 * JPA and Hibernate. The class offers functionality for adding, updating, deleting,
 * and retrieving house members based on their unique identifiers. Additionally, it
 * provides methods for listing house members for a specific user ID using pagination.
 */
@RequiredArgsConstructor
@Service
public class HouseSDJpaService implements HouseService {
  private final HouseMemberRepository houseMemberRepository;
  private final HouseMemberDocumentRepository houseMemberDocumentRepository;
  private final CommunityHouseRepository communityHouseRepository;

  /**
   * generates a unique identifier using the `UUID` class and returns it as a string.
   * 
   * @returns a randomly generated unique identifier in the form of a string.
   */
  private String generateUniqueId() {
    return UUID.randomUUID().toString();
  }

  /**
   * retrieves a set of `CommunityHouse` objects from the repository using findAll()
   * method and returns the set.
   * 
   * @returns a set of `CommunityHouse` objects representing all houses in the database.
   * 
   * * The output is a `Set` of `CommunityHouse` objects, which represents a collection
   * of all the community houses in the system.
   * * The `Set` is created using the `HashSet` class, which provides an unordered set
   * of elements with no duplicates.
   * * The function uses the `findAll()` method from the `communityHouseRepository` to
   * fetch all the community houses from the database or data source.
   * * Each `CommunityHouse` object in the set is added to the set using the `add()`
   * method, which adds the element to the set without affecting its order.
   */
  @Override
  public Set<CommunityHouse> listAllHouses() {
    Set<CommunityHouse> communityHouses = new HashSet<>();
    communityHouseRepository.findAll().forEach(communityHouses::add);
    return communityHouses;
  }

  /**
   * takes a `Pageable` object and returns a set of `CommunityHouse` objects retrieved
   * from the repository.
   * 
   * @param pageable page number and the number of houses to display on each page,
   * allowing for pagination of the list of community houses.
   * 
   * * `Pageable pageable`: Represents an object that allows for navigating through a
   * collection of items, such as a list or a set, in a specific order. It provides
   * methods for moving to previous or next pages of results, as well as for retrieving
   * the current page of results.
   * 
   * @returns a set of `CommunityHouse` objects.
   * 
   * * `Set<CommunityHouse> communityHouses`: This is the set of all houses returned
   * by the function, which is an instance of `HashSet`.
   * * `pageable`: This is the pageable object passed as a parameter to the function,
   * which contains information about the pagination of the houses.
   */
  @Override
  public Set<CommunityHouse> listAllHouses(Pageable pageable) {
    Set<CommunityHouse> communityHouses = new HashSet<>();
    communityHouseRepository.findAll(pageable).forEach(communityHouses::add);
    return communityHouses;
  }

  /**
   * adds new members to a community house by generating unique IDs, associating them
   * with the community house, and saving them in the database.
   * 
   * @param houseId unique identifier of the house for which the members are being added.
   * 
   * @param houseMembers set of house members to be added to the community house, and
   * its elements are modified by generating unique member IDs and associating each
   * member with the corresponding community house.
   * 
   * * `houseMembers`: A set of `HouseMember` objects that represent the members of the
   * house.
   * * `houseId`: The unique identifier of the house to which the members belong.
   * * `memberId`: Each member's unique identifier, generated using `generateUniqueId()`.
   * * `CommunityHouse`: Each member's community house, which is the house that contains
   * the members.
   * * `setMemberId()` and `setCommunityHouse()`: Methods used to set the unique
   * identifier and community house of each member.
   * 
   * The function first checks if a community house with the provided `houseId` exists
   * in the repository. If it does, it proceeds to deserialize the input `houseMembers`
   * into a new set of `HouseMember` objects, each with a unique identifier generated
   * using `generateUniqueId()`. The original members are then saved in the repository
   * along with their updated community house. If no matching community house is found,
   * the function returns an empty set.
   * 
   * @returns a set of house members that have been added to a community house.
   * 
   * * The output is a `Set` of `HouseMember` objects, which represents the newly added
   * members to the community house.
   * * The `Set` contains only the unique member IDs generated by the function for each
   * member.
   * * Each `House Member` object in the set has a `CommunityHouse` field set to the
   * corresponding community house.
   * * The `HouseMember` objects are not necessarily in the same order as they were
   * passed in, as the function modifies their `MemberId` and saves them in a new order.
   * * The function returns the `Set` of `House Member` objects that have been saved
   * to the database.
   */
  @Override public Set<HouseMember> addHouseMembers(String houseId, Set<HouseMember> houseMembers) {
    Optional<CommunityHouse> communityHouseOptional =
        communityHouseRepository.findByHouseIdWithHouseMembers(houseId);
    return communityHouseOptional.map(communityHouse -> {
      Set<HouseMember> savedMembers = new HashSet<>();
      houseMembers.forEach(member -> member.setMemberId(generateUniqueId()));
      houseMembers.forEach(member -> member.setCommunityHouse(communityHouse));
      houseMemberRepository.saveAll(houseMembers).forEach(savedMembers::add);

      communityHouse.getHouseMembers().addAll(savedMembers);
      communityHouseRepository.save(communityHouse);
      return savedMembers;
    }).orElse(new HashSet<>());
  }

  /**
   * deletes a member from a house by removing them from the house's membership list
   * and saving the changes to the database.
   * 
   * @param houseId id of the house for which the member is being removed.
   * 
   * @param memberId member ID to be removed from the community house.
   * 
   * @returns a boolean value indicating whether the specified member was successfully
   * removed from the house.
   */
  @Override
  public boolean deleteMemberFromHouse(String houseId, String memberId) {
    Optional<CommunityHouse> communityHouseOptional =
        communityHouseRepository.findByHouseIdWithHouseMembers(houseId);
    return communityHouseOptional.map(communityHouse -> {
      boolean isMemberRemoved = false;
      if (!CollectionUtils.isEmpty(communityHouse.getHouseMembers())) {
        Set<HouseMember> houseMembers = communityHouse.getHouseMembers();
        for (HouseMember member : houseMembers) {
          if (member.getMemberId().equals(memberId)) {
            houseMembers.remove(member);
            communityHouse.setHouseMembers(houseMembers);
            communityHouseRepository.save(communityHouse);
            member.setCommunityHouse(null);
            houseMemberRepository.save(member);
            isMemberRemoved = true;
            break;
          }
        }
      }
      return isMemberRemoved;
    }).orElse(false);
  }

  /**
   * retrieves the details of a specific house based on its ID, using a repository to
   * query the database.
   * 
   * @param houseId unique identifier of a house for which details are to be retrieved.
   * 
   * @returns an optional object containing the details of a house with the provided
   * ID, if found in the repository.
   * 
   * * `Optional<CommunityHouse>`: This represents an optional result, meaning that the
   * function may return `None` if no house details are found for the provided ID.
   * * `communityHouseRepository.findByHouseId(houseId)`: This is a method call that
   * retrieves the community house details for the given ID from the repository. The
   * method returns an `Optional` object, which contains either the detailed information
   * of the house or `None` if it does not exist.
   */
  @Override
  public Optional<CommunityHouse> getHouseDetailsById(String houseId) {
    return communityHouseRepository.findByHouseId(houseId);
  }

  /**
   * retrieves a paginated list of `HouseMember` objects associated with a given `houseId`.
   * 
   * @param houseId id of the community house whose members are being retrieved.
   * 
   * @param pageable pagination information for the requested house members, allowing
   * the function to filter and return only a subset of the total number of house members
   * based on the specified page size and current page number.
   * 
   * * `houseId`: The identifier of the house for which members are to be retrieved.
   * * `Pageable`: A class that provides a way to page through large data sets efficiently.
   * It consists of various attributes such as `getNumberOfElements`, `getNumberOfPages`,
   * `getPageSize`, and `getTotalElements`.
   * 
   * @returns a list of `HouseMember` objects associated with the specified house ID.
   * 
   * * `Optional<List<HouseMember>>`: This type represents an optional list of house
   * members, where `Optional` is an interface that provides a way to safely handle
   * null values. The list contains `HouseMember` objects.
   * * `houseId`: This parameter represents the unique identifier for a particular
   * community house.
   * * `Pageable pageable`: This parameter represents the pagination information for
   * the house members, which determines how many members are returned in each page.
   */
  @Override
  public Optional<List<HouseMember>> getHouseMembersById(String houseId, Pageable pageable) {
    return Optional.ofNullable(
        houseMemberRepository.findAllByCommunityHouse_HouseId(houseId, pageable)
    );
  }

  /**
   * retrieves a list of `HouseMember` objects associated with a user's houses using
   * the `houseMemberRepository`. The list is filtered and paginated based on the input
   * `pageable` object.
   * 
   * @param userId identifier of the user for whom the list of house members is being
   * retrieved.
   * 
   * @param pageable PageRequest object that specifies the pagination criteria for
   * retrieving the list of house members.
   * 
   * * `userId`: The unique identifier of the user for whom the house members are being
   * retrieved.
   * * `pageable`: An object that encapsulates paging information, such as the page
   * number, size, and Sort parameters.
   * 
   * @returns a Optional<List<HouseMember>> object containing the list of HouseMembers
   * associated with the specified user ID.
   * 
   * * The `Optional` object represents a possible non-null value, which is a List of
   * HouseMember objects.
   * * The `listHouseMembersForHousesOfUserId` function returns an Optional object
   * because it makes use of a database query to retrieve data and the result may be
   * null if no matching rows are found in the database.
   * * The List of HouseMember objects contained within the Optional object represents
   * the list of HouseMembers associated with the specified user Id, fetched using the
   * `houseMemberRepository.findAllByCommunityHouse_Community_Admins_UserId` method.
   */
  @Override
  public Optional<List<HouseMember>> listHouseMembersForHousesOfUserId(String userId,
      Pageable pageable) {
    return Optional.ofNullable(
        houseMemberRepository.findAllByCommunityHouse_Community_Admins_UserId(userId, pageable)
    );
  }
}
