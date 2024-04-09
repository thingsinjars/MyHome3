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
 * TODO
 */
@RequiredArgsConstructor
@Service
public class HouseSDJpaService implements HouseService {
  private final HouseMemberRepository houseMemberRepository;
  private final HouseMemberDocumentRepository houseMemberDocumentRepository;
  private final CommunityHouseRepository communityHouseRepository;

  /**
   * generates a unique identifier based on a randomly generated UUID string, returning
   * it as a string.
   * 
   * @returns a randomly generated unique string of characters.
   * 
   * The generated unique ID is a string of random characters, created using the
   * `UUID.randomUUID()` method.
   * It is a deterministic sequence of characters, meaning that it will always generate
   * the same output for the same input.
   * The length of the generated ID can vary between 10 and 36 characters, depending
   * on the system architecture.
   */
  private String generateUniqueId() {
    return UUID.randomUUID().toString();
  }

  /**
   * retrieves a set of `CommunityHouse` objects from the database using the `findAll()`
   * method of the `communityHouseRepository`. The retrieved objects are then added to
   * a new `Set` instance.
   * 
   * @returns a set of `CommunityHouse` objects representing all houses stored in the
   * repository.
   * 
   * 	- The `Set<CommunityHouse>` object represents a collection of all community houses
   * in the system.
   * 	- The set is created using the `new HashSet<>()` method, which ensures that the
   * elements in the set are unique and do not contain duplicates.
   * 	- The function first calls the `findAll()` method on the `communityHouseRepository`
   * object to retrieve a list of all community houses.
   * 	- Then, the `forEach()` method is called on the list of community houses to add
   * each one to the set.
   */
  @Override
  public Set<CommunityHouse> listAllHouses() {
    Set<CommunityHouse> communityHouses = new HashSet<>();
    communityHouseRepository.findAll().forEach(communityHouses::add);
    return communityHouses;
  }

  /**
   * retrieves a set of `CommunityHouse` objects from the database using the `findAll`
   * method and stores them in a new `Set`.
   * 
   * @param pageable pagination information for retrieving a subset of the community
   * houses from the repository.
   * 
   * 	- `Pageable` is an interface that provides methods for navigating and manipulating
   * a page of results. It typically contains several attributes such as `pageNumber`,
   * `pageSize`, and `sort`.
   * 
   * @returns a set of `CommunityHouse` objects.
   * 
   * The `Set<CommunityHouse>` object represents a collection of CommunityHouse instances
   * that have been retrieved from the database.
   * 
   * The `CommunityHouse` class has properties such as the name, address, and other
   * relevant details related to the community houses.
   * 
   * The `pageable` parameter is used to specify how the results should be paginated
   * and displayed.
   * 
   * Overall, the function returns a collection of CommunityHouses that have been
   * retrieved from the database based on the specified pageable criteria.
   */
  @Override
  public Set<CommunityHouse> listAllHouses(Pageable pageable) {
    Set<CommunityHouse> communityHouses = new HashSet<>();
    communityHouseRepository.findAll(pageable).forEach(communityHouses::add);
    return communityHouses;
  }

  /**
   * adds new house members to an existing community house, generates unique member
   * IDs, and updates the community house's member list in the repository.
   * 
   * @param houseId unique identifier of the house for which the members are being added.
   * 
   * 	- `houseId`: A string representing the unique identifier for a community house.
   * 	- `houseMembers`: A set of `HouseMember` objects that represent the members
   * associated with the specified community house.
   * 
   * The function first checks if there is already a saved community house with the
   * matching `houseId`. If such a community house is found, the function maps it to a
   * new set of `HouseMember` objects, each with a newly generated unique ID. The
   * existing members are then updated by setting their `CommunityHouse` field to the
   * mapped community house, and their IDs are saved in the `house MemberRepository`.
   * Finally, the updated community house is saved.
   * 
   * @param houseMembers set of HouseMembers that will be added or updated in the
   * community house.
   * 
   * 	- `houseId`: The unique identifier of the house where the members will be added.
   * 	- `houseMembers`: A set of HouseMember objects that contain information about the
   * members to be added. Each member has an ID generated using the `generateUniqueId()`
   * method and a reference to the corresponding CommunityHouse object.
   * 	- `communityHouseOptional`: An optional reference to a CommunityHouse object,
   * which is used to retrieve the house details and associated members. If absent, a
   * new CommunityHouse object will be created with the provided house ID.
   * 
   * @returns a set of house members, each with a unique ID and associated with a
   * specific community house.
   * 
   * 	- The output is a `Set` containing the newly added house members.
   * 	- The `Set` contains only the unique member IDs generated for each member in the
   * input `houseMembers` set.
   * 	- Each member ID is assigned a unique value using the `generateUniqueId()` method.
   * 	- Each member is associated with the corresponding community house by setting its
   * `CommunityHouse` field to the saved community house object.
   * 	- The `Set` also contains all the saved members from the input `houseMembers`
   * set, which are added to the community house's `HouseMembers` field.
   * 	- The community house is saved with its updated `HouseMembers` field after adding
   * the new members.
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
   * removes a member from a house by searching for the member in the house's members
   * list, removing them if found, and saving the changes to the community house and
   * member entities.
   * 
   * @param houseId ID of the community house that the member belongs to, which is used
   * to locate the relevant community house record in the database for removal of the
   * member.
   * 
   * 	- `Optional<CommunityHouse> communityHouseOptional`: This represents an optional
   * reference to a `CommunityHouse` object that may or may not be present in the repository.
   * 	- `map()`: This method is used to map the `Optional` reference to a `CommunityHouse`
   * object, which contains information about the house and its members.
   * 	- `findByHouseIdWithHouseMembers()`: This method from the `communityHouseRepository`
   * class is used to retrieve a `CommunityHouse` object based on the `houseId`.
   * 	- `getHouseMembers()`: This method returns a set of `HouseMember` objects that
   * belong to the specified `CommunityHouse`.
   * 	- `isEmpty()`: This method is used to check if the `HouseMembers` set is empty.
   * 	- `setHouseMembers()`: This method sets the `HouseMembers` set of the `CommunityHouse`
   * object to the given `Set`.
   * 	- `save()`: This method from the `communityHouseRepository` class is used to save
   * the modified `CommunityHouse` object in the database.
   * 	- `getMemberId()`: This method returns the ID of a specific `HouseMember` object.
   * 	- `setCommunityHouse(null)`: This method sets the `CommunityHouse` reference of
   * a specific `HouseMember` object to `null`.
   * 	- `save()`: This method from the `houseMemberRepository` class is used to save
   * the modified `HouseMember` object in the database.
   * 
   * @param memberId ID of the member to be removed from the community house.
   * 
   * 	- `houseId`: The ID of the house to which the member belongs.
   * 	- `memberId`: The unique identifier of the member to be removed from the house.
   * 
   * @returns a boolean value indicating whether the specified member was removed from
   * the house.
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
   * retrieves the details of a specific community house based on its ID, by querying
   * the `communityHouseRepository`.
   * 
   * @param houseId unique identifier for a specific community house to be retrieved
   * from the repository.
   * 
   * 	- `houseId`: A unique identifier for a community house.
   * 
   * @returns an optional instance of `CommunityHouse`.
   * 
   * 	- `Optional<CommunityHouse>`: This type represents an optional reference to a
   * `CommunityHouse` object, which means that the function may return either an instance
   * of `CommunityHouse` or `Optional.empty()`.
   * 	- `communityHouseRepository.findByHouseId(houseId)`: This method retrieves a
   * `CommunityHouse` object from the database based on the provided `houseId`. It
   * returns an instance of `CommunityHouse` if found, otherwise it returns `Optional.empty()`.
   */
  @Override
  public Optional<CommunityHouse> getHouseDetailsById(String houseId) {
    return communityHouseRepository.findByHouseId(houseId);
  }

  /**
   * retrieves a list of `HouseMember` objects associated with a specific `houseId`.
   * It returns an optional list, which means that if no `HouseMember` objects are
   * found, the function will return `Optional.ofNullable(emptyList())`.
   * 
   * @param houseId identifier of the house for which the list of members is being retrieved.
   * 
   * 	- `houseId`: This parameter represents the unique identifier for a house in the
   * community. It is typically an integer value.
   * 
   * @param pageable paging information for the HouseMember data, allowing the function
   * to retrieve a subset of the data from the repository based on the specified page
   * number and size.
   * 
   * 	- `houseId`: The unique identifier for the house being searched for members.
   * 	- `Pageable`: An interface representing a pagination mechanism, which allows for
   * the retrieval of a subset of data from a large dataset in a managed and efficient
   * manner.
   * 
   * @returns a Optional<List<HouseMember>> containing the list of HouseMembers associated
   * with the specified house ID.
   * 
   * 	- `Optional<List<HouseMember>>`: This is an optional list of HouseMembers, which
   * means that it may be empty if no HouseMembers are found for the given house ID.
   * 	- `getHouseMembersById(String houseId, Pageable pageable)`: This function takes
   * two parameters - `houseId` and `pageable`. The first parameter is a string
   * representing the ID of the house for which the HouseMembers are being retrieved,
   * while the second parameter is a `Pageable` object that defines how the list of
   * HouseMembers should be paginated.
   */
  @Override
  public Optional<List<HouseMember>> getHouseMembersById(String houseId, Pageable pageable) {
    return Optional.ofNullable(
        houseMemberRepository.findAllByCommunityHouse_HouseId(houseId, pageable)
    );
  }

  /**
   * retrieves a list of `HouseMember` objects from the database based on the user ID
   * and pageable parameters.
   * 
   * @param userId user for whom the list of HouseMembers is being retrieved.
   * 
   * 	- `userId`: A `String` representing the user ID for which the house members are
   * to be listed.
   * 
   * The function returns an optional list of `HouseMember` instances retrieved from
   * the `houseMemberRepository`. The returned list is filtered based on the
   * `communityHouse_Community_Admins_UserId` field in the database, using the `findAllBy`
   * method with the `pageable` parameter.
   * 
   * @param pageable pagination information for the query, allowing the function to
   * retrieve a subset of the house members for a given user ID.
   * 
   * 	- `userId`: A String representing the user ID for which house members are to be
   * listed.
   * 	- `pageable`: An instance of `Pageable`, which allows for pagination and sorting
   * of results. Its properties include `getPageNumber()` (an integer representing the
   * current page number), `getPageSize()` (an integer representing the number of
   * elements per page), and `getSort()` (a String representing the sort order, or an
   * empty String if no sorting is required).
   * 
   * @returns a pageable list of house members for the specified user ID.
   * 
   * 	- `Optional<List<HouseMember>>`: The output is an optional list of HouseMembers,
   * which means that it may be null if there are no HouseMembers found for the given
   * user ID.
   * 	- `listHouseMembersForHousesOfUserId(String userId, Pageable pageable)`: This
   * method takes a user ID as input and returns a pageable list of HouseMembers
   * associated with that user ID.
   */
  @Override
  public Optional<List<HouseMember>> listHouseMembersForHousesOfUserId(String userId,
      Pageable pageable) {
    return Optional.ofNullable(
        houseMemberRepository.findAllByCommunityHouse_Community_Admins_UserId(userId, pageable)
    );
  }
}
