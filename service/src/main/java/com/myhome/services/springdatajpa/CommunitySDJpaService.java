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

import com.myhome.controllers.dto.CommunityDto;
import com.myhome.controllers.dto.mapper.CommunityMapper;
import com.myhome.domain.Community;
import com.myhome.domain.CommunityHouse;
import com.myhome.domain.HouseMember;
import com.myhome.domain.User;
import com.myhome.repositories.CommunityHouseRepository;
import com.myhome.repositories.CommunityRepository;
import com.myhome.repositories.UserRepository;
import com.myhome.services.CommunityService;
import com.myhome.services.HouseService;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;
import javax.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

/**
 * TODO
 */
@Slf4j
@RequiredArgsConstructor
@Service
public class CommunitySDJpaService implements CommunityService {
  private final CommunityRepository communityRepository;
  private final UserRepository communityAdminRepository;
  private final CommunityMapper communityMapper;
  private final CommunityHouseRepository communityHouseRepository;
  private final HouseService houseService;

  /**
   * creates a new community and adds an administrator with the user's ID to it, then
   * saves it to the repository for later retrieval.
   * 
   * @param communityDto CommunityDto object containing the data for the community to
   * be created, which is used to create a new community instance and save it to the repository.
   * 
   * 	- `communityDto.setCommunityId(generateUniqueId());`: This line generates a unique
   * ID for the community and sets it as the `id` attribute of the `Community` object.
   * 	- `String userId = (String) SecurityContextHolder.getContext().getAuthentication().getPrincipal();`:
   * This line retrieves the authenticated user's ID, which is used to add an admin to
   * the community.
   * 	- `Community community = addAdminToCommunity(communityMapper.communityDtoToCommunity(communityDto),
   * userId);`: This line adds an admin to the community using the `addAdminToCommunity`
   * method, which takes the `Community` object and the user ID as inputs.
   * 	- `Community savedCommunity = communityRepository.save(community);`: This line
   * saves the created community in the repository, using the `save` method of the
   * `CommunityRepository` interface.
   * 
   * @returns a saved community object in the repository.
   * 
   * 	- `community`: This is the community object that has been created and saved in
   * the repository. It has an `id` attribute that represents the unique identifier
   * assigned to the community.
   * 	- `savedCommunity`: This is the community object that has been saved to the
   * repository. It has an `id` attribute that represents the actual id of the community
   * in the database.
   * 	- `log.trace`: This line logs a trace message indicating that the community has
   * been saved to the repository with its actual id.
   */
  @Override
  public Community createCommunity(CommunityDto communityDto) {
    communityDto.setCommunityId(generateUniqueId());
    String userId = (String) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
    Community community = addAdminToCommunity(communityMapper.communityDtoToCommunity(communityDto),
        userId);
    Community savedCommunity = communityRepository.save(community);
    log.trace("saved community with id[{}] to repository", savedCommunity.getId());
    return savedCommunity;
  }

  /**
   * adds a user as an admin to a Community by updating the Community's admin set with
   * the provided user ID and then returning the updated Community object.
   * 
   * @param community Community object that is being updated with the provided `userId`.
   * 
   * 	- `community`: This is the Community object to which an admin will be added.
   * 	- `userId`: The user ID of the admin to be added to the community.
   * 	- `communityAdminRepository`: A repository for finding admins associated with a
   * given user ID and communities.
   * 	- `admins`: A set of admins associated with the community, which will be updated
   * upon successful addition of the new admin.
   * 	- `community.setAdmins()`: This method sets the list of admins associated with
   * the community to the new set containing the added admin.
   * 
   * @param userId ID of the user who is being added as an administrator to the specified
   * community.
   * 
   * 	- `community`: The Community object that is being updated to add an administrator.
   * 	- `userId`: A string representing the ID of the user who will be added as an
   * administrator to the Community.
   * 
   * @returns a modified Community object with the added admin user.
   * 
   * 	- The community object is updated by adding the specified user Id to the list of
   * admins.
   * 	- The admin object is created with the user Id and communities added to it.
   * 	- The admin object's set of communities is updated by adding the specified community
   * to it.
   * 	- The community object's set of admins is updated by adding the newly created
   * admin to it.
   */
  private Community addAdminToCommunity(Community community, String userId) {
    communityAdminRepository.findByUserIdWithCommunities(userId).ifPresent(admin -> {
      admin.getCommunities().add(community);
      Set<User> admins = new HashSet<>();
      admins.add(admin);
      community.setAdmins(admins);
    });
    return community;
  }

  /**
   * retrieves a list of communities from the repository and returns it as a set.
   * 
   * @param pageable pagination information for retrieving a subset of the Community
   * objects from the database, allowing the listAll method to retrieve the required
   * number of communities per page.
   * 
   * 	- `Pageable`: This is an interface that defines methods for pagination, such as
   * `getNumberOfElements()` and `getPagePosition()`.
   * 	- `Set<Community> communityListSet`: This is a set of community objects that will
   * be returned by the function.
   * 
   * The function first creates a new `HashSet` to store the list of communities and
   * then iterates over the result of `communityRepository.findAll(pageable)` using the
   * `forEach()` method, adding each community object to the `Set`. Finally, the function
   * returns the `Set`.
   * 
   * @returns a set of `Community` objects.
   * 
   * 	- `Set<Community> communityListSet`: This is a set of `Community` objects that
   * contains all the communities retrieved from the database.
   * 	- The elements in the set are obtained by calling the `forEach` method on the
   * `communityRepository.findAll(pageable)` result, and passing the `add` method as
   * an action to be performed on each element. This means that the set contains all
   * the communities retrieved from the database, regardless of their status (e.g.,
   * active or inactive).
   * 	- The `Set` type is used instead of a `List` to avoid any potential duplicates
   * in the list.
   */
  @Override
  public Set<Community> listAll(Pageable pageable) {
    Set<Community> communityListSet = new HashSet<>();
    communityRepository.findAll(pageable).forEach(communityListSet::add);
    return communityListSet;
  }

  /**
   * retrieves a list of all `Community` instances from the database and returns them
   * in a `Set`.
   * 
   * @returns a set of all available `Community` objects stored in the repository.
   * 
   * 	- The output is a `Set` of `Community` objects, which represents a collection of
   * all communities in the system.
   * 	- The `Set` is populated by calling the `findAll()` method on the `communityRepository`,
   * which retrieves all community objects from the database or storage.
   * 	- Each community object added to the `Set` is an instance of the `Community`
   * class, which has attributes such as name, location, and description.
   * 	- The `listAll` function returns a `Set` of these community objects, allowing for
   * efficient iteration and manipulation of all communities in the system.
   */
  @Override public Set<Community> listAll() {
    Set<Community> communities = new HashSet<>();
    communityRepository.findAll().forEach(communities::add);
    return communities;
  }

  /**
   * retrieves a list of community houses associated with a given community ID using a
   * pageable parameter. It first checks if the community exists, and then returns an
   * Optional containing the list of community houses if it does, or an empty Optional
   * otherwise.
   * 
   * @param communityId identifier of a community that the method is meant to find
   * Community Houses for.
   * 
   * 	- `communityId`: A string representing the unique identifier for a community. It
   * is used to filter the community houses in the database.
   * 
   * @param pageable paging information for the community houses to be retrieved,
   * allowing for efficient retrieval of a subset of the data.
   * 
   * 	- `communityId`: The unique identifier for the community whose houses are to be
   * retrieved.
   * 	- `pageable`: A Pageable object, which allows for pagination and sorting of the
   * house list based on various attributes such as creation date, price, and location.
   * 
   * @returns a `Optional` containing a list of `CommunityHouse` objects if the community
   * exists, otherwise an empty `Optional`.
   * 
   * 	- `Optional<List<CommunityHouse>>`: This is an optional list of community houses,
   * which means that it may or may not be present depending on whether any community
   * houses exist for the given community ID.
   * 	- `findAllByCommunity_CommunityId`: This method returns a list of all community
   * houses associated with the given community ID.
   * 	- `communityRepository.existsByCommunityId`: This method checks whether a community
   * exists with the given community ID. If it does, the function proceeds to return a
   * list of community houses associated with that community. If it doesn't, the function
   * returns an empty list.
   */
  @Override
  public Optional<List<CommunityHouse>> findCommunityHousesById(String communityId,
      Pageable pageable) {
    boolean exists = communityRepository.existsByCommunityId(communityId);
    if (exists) {
      return Optional.of(
          communityHouseRepository.findAllByCommunity_CommunityId(communityId, pageable));
    }
    return Optional.empty();
  }

  /**
   * retrieves a list of community admins for a given community ID using two repository
   * calls: `communityRepository.existsByCommunityId()` and
   * `communityAdminRepository.findAllByCommunities_CommunityId()`. If any admins exist,
   * the function returns an optional list of admins; otherwise, it returns an empty list.
   * 
   * @param communityId identifier of the community for which the list of community
   * admins is to be retrieved.
   * 
   * 	- `communityId`: A String representing the ID of a community.
   * 
   * @param pageable page of results that the user wants to view, allowing for pagination
   * and efficient retrieval of the desired data.
   * 
   * 	- `communityId`: A String that represents the ID of the community to find admins
   * for.
   * 	- `Pageable`: An interface that provides a way to page (i.e., limit and offset)
   * the results of a query. The properties of `pageable` may include `getPageNumber()`
   * (which returns the current page number), `getPageSize()` (which returns the number
   * of items per page), `getTotalElements()` (which returns the total number of elements
   * in the result set), and others.
   * 
   * @returns a `Optional<List<User>>` containing the list of community admins for the
   * specified community ID.
   * 
   * 	- `Optional<List<User>>`: The function returns an optional list of users who are
   * community admins for the given community ID. If no such users exist, the function
   * returns an empty Optional.
   * 	- `List<User>`: The list of users returned in the Optional contains the community
   * admins for the specified community ID.
   * 	- `Pageable`: The pageable parameter is passed to the `findAllByCommunities_CommunityId`
   * method, which allows for pagination of the results.
   */
  @Override
  public Optional<List<User>> findCommunityAdminsById(String communityId,
      Pageable pageable) {
    boolean exists = communityRepository.existsByCommunityId(communityId);
    if (exists) {
      return Optional.of(
          communityAdminRepository.findAllByCommunities_CommunityId(communityId, pageable)
      );
    }
    return Optional.empty();
  }

  /**
   * retrieves a `Optional<User>` instance containing the community administrator
   * associated with the specified `adminId`.
   * 
   * @param adminId user ID of the community administrator to be retrieved from the database.
   * 
   * 	- `communityAdminRepository`: This is an instance of `UserRepository`, which
   * represents a database repository for storing and retrieving user data.
   * 	- `findByUserId`: This method performs a query on the repository to find the
   * community administrator associated with the specified `adminId`.
   * 
   * @returns an optional `User` object representing the community administrator with
   * the provided `adminId`.
   * 
   * 	- `Optional<User>`: The type of the output indicates that it may contain a value
   * or be empty, which is represented by the `<>` symbol.
   * 	- `findByUserId(adminId)`: The method call within the `findCommunityAdminById`
   * function retrieves a user from the `communityAdminRepository` using the `findByUserId`
   * method and passing in the `adminId` parameter.
   */
  @Override
  public Optional<User> findCommunityAdminById(String adminId) {
    return communityAdminRepository.findByUserId(adminId);
  }

  /**
   * retrieves community details by ID from the repository.
   * 
   * @param communityId identifier of the community to retrieve details for.
   * 
   * 	- `communityId`: This is a string input parameter that represents the unique
   * identifier for a community. It is used to retrieve community details from the repository.
   * 
   * @returns an Optional<Community> object containing the details of the specified
   * community if found, or an emptyOptional if not found.
   * 
   * The Optional object represents a possible value of the Community object, which
   * contains information about a community.
   * 
   * If the Optional object is present, it means that the community with the specified
   * ID exists in the repository, and its details can be retrieved.
   * 
   * If the Optional object is absent, it means that either there is no community with
   * the specified ID or the repository could not find any matching community.
   */
  @Override public Optional<Community> getCommunityDetailsById(String communityId) {
    return communityRepository.findByCommunityId(communityId);
  }

  /**
   * retrieves a community's details along with its administrators from the repository.
   * 
   * @param communityId identity of the community for which details and administrators
   * are being requested.
   * 
   * The `findByCommunityIdWithAdmins` method returns an `Optional` object containing
   * the community details along with its admins. The `Optional` type allows for the
   * possibility that no community details or admins may be found.
   * 
   * @returns an optional `Community` object containing details of the specified community
   * and its administrators.
   * 
   * 	- The `Optional` class represents a container for a value that may or may not be
   * present. In this case, it contains a `Community` object if one exists with the
   * given `communityId`, otherwise it is empty.
   * 	- The `Community` object has several attributes: `id`, `name`, `description`,
   * `icon`, and `admins`. These attributes represent the details of the community,
   * including its identifier, name, description, icon, and list of administrators.
   */
  @Override
  public Optional<Community> getCommunityDetailsByIdWithAdmins(String communityId) {
    return communityRepository.findByCommunityIdWithAdmins(communityId);
  }

  /**
   * adds a set of admins to a community by finding the community, iterating over the
   * admins, and adding them as members of the community.
   * 
   * @param communityId ID of the community whose admins are to be added.
   * 
   * 	- `findByCommunityIdWithAdmins`: This method is used to find a community with the
   * given `communityId`. It returns an optional instance of `Community`.
   * 	- `getCommunities`: This method retrieves a set of `Community` instances that are
   * associated with the given `communityId`.
   * 	- `getAdmins`: This method retrieves a set of `User` instances that are associated
   * with the given `communityId`.
   * 	- `save`: This method saves a `Community` instance after modifying its properties.
   * 
   * The function takes two input parameters: `communityId` and `adminsIds`. The
   * `adminsIds` parameter is a set of strings that represent the user IDs of the admins
   * to be added to the community.
   * 
   * The function first calls `findByCommunityIdWithAdmins` to retrieve an optional
   * instance of `Community`. If the community is found, it then iterates over the
   * `adminsIds` set and calls `findByUserIdWithCommunities` to retrieve a set of `Admin`
   * instances associated with each user ID. Then, for each admin, it adds the admin
   * to the community by calling `save` on the admin instance. Finally, it saves the
   * modified community instance using `save`.
   * 
   * @param adminsIds IDs of users who are to be added as admins to a community.
   * 
   * 	- Set<String> adminsIds: A set of strings representing the IDs of the admins to
   * be added to the community.
   * 	- String communityId: The ID of the community where the admins will be added.
   * 
   * @returns an `Optional` object containing the updated community with added admins.
   * 
   * 	- The `Optional<Community>` return type indicates that the function may return
   * `None` if no community is found with the given `communityId`, or if there is an
   * error during the execution.
   * 	- The `map` method is used to transform the `Optional<Community>` into a
   * `Optional<User>` using the `save` method of the `UserRepository`. This method call
   * creates a new `User` object and saves it in the database, linking it to the community.
   * 	- The `orElseGet` method is used as a fallback to return an `Optional.empty()`
   * if the `map` method fails to find a `User` object associated with the given `adminId`.
   * 
   * Overall, this function is designed to add multiple admins to a community by finding
   * the community and then adding each admin to the community's list of admins, saving
   * the changes to the database.
   */
  @Override
  public Optional<Community> addAdminsToCommunity(String communityId, Set<String> adminsIds) {
    Optional<Community> communitySearch =
        communityRepository.findByCommunityIdWithAdmins(communityId);

    return communitySearch.map(community -> {
      adminsIds.forEach(adminId -> {
        communityAdminRepository.findByUserIdWithCommunities(adminId).map(admin -> {
          admin.getCommunities().add(community);
          community.getAdmins().add(communityAdminRepository.save(admin));
          return admin;
        });
      });
      return Optional.of(communityRepository.save(community));
    }).orElseGet(Optional::empty);
  }

  /**
   * adds new or modified houses to a community by first checking if the community
   * exists, then iterating through the provided houses and either adding them to the
   * community's house list or generating a unique ID for a new house.
   * 
   * @param communityId unique identifier of the community for which the houses are
   * being added, and is used to find the existing houses in the community and to save
   * the new houses in the community after they have been processed.
   * 
   * 	- `Optional<Community> communitySearch`: This represents an optional instance of
   * the `Community` class, which may or may not be present in the input. If present,
   * it contains a reference to a `Community` object that has been fetched from the repository.
   * 	- `Set<CommunityHouse> houses`: This is a set of `CommunityHouse` objects that
   * are being added to the community. Each element in the set represents a single
   * house, with its own unique `houseId` and `name`.
   * 
   * @param houses set of houses to be added to the community.
   * 
   * 	- `houses`: A set of `CommunityHouse` objects, each representing a house in a community.
   * 	- `communityId`: The ID of the community to which the houses belong.
   * 	- `CommunityHouse`: Represents a house in a community, having attributes such as
   * `houseId`, `name`, and `communities`.
   * 	- `generateUniqueId()`: A method that generates a unique ID for each newly created
   * house.
   * 
   * @returns a set of unique house IDs that have been added to the community, along
   * with the corresponding community ID.
   * 
   * 	- `Set<String> addedIds`: This set contains the house IDs that were newly created
   * or updated in the community. Each element in the set is a unique house ID.
   * 	- `Optional<Community> communitySearch`: This optional field represents the result
   * of searching for a community with the given `communityId`. If the community is
   * found, the `community` field will contain the community object, and the `map`
   * method will be called to update the houses in the community. Otherwise, the field
   * will be `None`, and no updates will be made to the community.
   * 	- `Set<CommunityHouse> houses`: This set contains the houses that are being added
   * or updated in the community. Each element in the set is a community house object.
   */
  @Override
  public Set<String> addHousesToCommunity(String communityId, Set<CommunityHouse> houses) {
    Optional<Community> communitySearch =
        communityRepository.findByCommunityIdWithHouses(communityId);

    return communitySearch.map(community -> {
      Set<String> addedIds = new HashSet<>();

      houses.forEach(house -> {
        if (house != null) {
          boolean houseExists = community.getHouses().stream()
              .noneMatch(communityHouse ->
                  communityHouse.getHouseId().equals(house.getHouseId())
                      && communityHouse.getName().equals(house.getName())
              );
          if (houseExists) {
            house.setHouseId(generateUniqueId());
            house.setCommunity(community);
            addedIds.add(house.getHouseId());
            communityHouseRepository.save(house);
            community.getHouses().add(house);
          }
        }
      });

      communityRepository.save(community);

      return addedIds;
    }).orElse(new HashSet<>());
  }

  /**
   * removes an admin from a community by finding the community and removing the admin
   * from its admin list if present, saving the community, and returning true if
   * successful, otherwise false.
   * 
   * @param communityId unique identifier of the community whose admins are to be removed.
   * 
   * 	- `communityId`: This is an String representing the unique identifier for a community.
   * 	- `adminId`: This is a String representing the unique identifier of an admin to
   * be removed from the community.
   * 
   * @param adminId ID of the admin to be removed from the community.
   * 
   * 	- `String communityId`: The unique identifier for a community in the system.
   * 	- `String adminId`: A unique identifier for an administrator within a community,
   * representing a user account ID.
   * 
   * @returns a boolean value indicating whether the admin has been successfully removed
   * from the community.
   */
  @Override
  public boolean removeAdminFromCommunity(String communityId, String adminId) {
    Optional<Community> communitySearch =
        communityRepository.findByCommunityIdWithAdmins(communityId);
    return communitySearch.map(community -> {
      boolean adminRemoved =
          community.getAdmins().removeIf(admin -> admin.getUserId().equals(adminId));
      if (adminRemoved) {
        communityRepository.save(community);
        return true;
      } else {
        return false;
      }
    }).orElse(false);
  }

  /**
   * deletes a community by finding all houses associated with it and removing them,
   * then deleting the community from the repository.
   * 
   * @param communityId ID of the community to be deleted.
   * 
   * 	- `communityRepository`: This is an instance of `CrudRepository`, which represents
   * a repository for working with communities in the application.
   * 	- `findByCommunityIdWithHouses()`: This method returns a stream of community
   * objects that match the specified `communtyId`. The method calls `map` on the
   * resulting stream to transform each community object into a new stream of `String`
   * values, representing the IDs of the houses associated with each community.
   * 	- `getHouses()`: This method returns a stream of `CommunityHouse` objects, which
   * represent the houses associated with each community. The method calls `map` on the
   * resulting stream to transform each `CommunityHouse` object into a new stream of
   * `String` values, representing the IDs of the houses.
   * 	- `collect(Collectors.toSet())`: This line collects the set of house IDs from the
   * transformed streams using the `toSet()` method.
   * 	- `removeHouseFromCommunityByHouseId()`: This is a utility method that removes a
   * house from a community based on its ID. The method takes two parameters: `community`
   * and `houseId`.
   * 	- `orElse(false)`: This line returns `true` if the `findByCommunityIdWithHouses`
   * method returns a non-empty stream, or `false` otherwise.
   * 
   * @returns a boolean value indicating whether the community was successfully deleted.
   */
  @Override
  @Transactional
  public boolean deleteCommunity(String communityId) {
    return communityRepository.findByCommunityIdWithHouses(communityId)
        .map(community -> {
          Set<String> houseIds = community.getHouses()
              .stream()
              .map(CommunityHouse::getHouseId)
              .collect(Collectors.toSet());

          houseIds.forEach(houseId -> removeHouseFromCommunityByHouseId(community, houseId));
          communityRepository.delete(community);

          return true;
        })
        .orElse(false);
  }

  /**
   * generates a unique identifier using the `UUID.randomUUID()` method and returns it
   * as a string.
   * 
   * @returns a unique, randomly generated string of characters.
   * 
   * 	- The output is a string that represents a unique identifier generated using the
   * `UUID` class.
   * 	- The `UUID.randomUUID()` method generates a universally unique identifier (UUID)
   * that is randomly generated and has no correlation with any other UUID.
   * 	- The resulting string has a maximum length of 36 characters, consisting of a
   * series of letters and numbers separated by dashes (-).
   */
  private String generateUniqueId() {
    return UUID.randomUUID().toString();
  }

  /**
   * removes a house from a community by first removing the house from the community's
   * houses collection, then deleting the house members associated with it, and finally
   * saving the community and deleting the house.
   * 
   * @param community Community object that contains the houses to be removed, and is
   * used to retrieve the Set of house members associated with each house and delete
   * them before deleting the houses themselves.
   * 
   * 	- `community`: A `Community` object, which represents a community of houses and
   * their members.
   * 	- `houseId`: The unique identifier of the house to be removed from the community.
   * 
   * The function first checks if the `community` is null or invalid, and returns false
   * in such cases. Otherwise, it uses an optional `CommunityHouse` object to find the
   * house with the specified `houseId`. If a house is found, the function performs
   * several operations:
   * 
   * 	- It removes the house from the community's list of houses using the `Set` data
   * structure.
   * 	- It streams the house's member IDs and collects them in a set using `Collectors.toSet()`.
   * 	- It deletes each member ID from the house using the `houseService.deleteMemberFromHouse()`
   * function.
   * 	- It saves the updated community using the `communityRepository.save()` function.
   * 	- It deletes the house with the specified `houseId` using the
   * `communityHouseRepository.deleteByHouseId()` function.
   * 
   * The function returns `true` if the operation was successful, and `false` otherwise.
   * 
   * @param houseId ID of the house to be removed from the community.
   * 
   * 	- `community`: A `Community` object representing the community where the house
   * to be removed is located.
   * 	- `houseId`: A string representing the unique identifier of the house to be removed.
   * 
   * The function first checks if the `community` parameter is null, and returns false
   * in that case. Then, it uses the `findByHouseIdWithHouseMembers` method provided
   * by the `communityHouseRepository` to find the house with the given `houseId`. If
   * no house is found, the function returns false.
   * 
   * Next, the function removes the house from the community's list of houses using the
   * `set` method. This is necessary because otherwise, the Set relationship between
   * the community and its houses would be broken, and the `remove` method would not
   * work correctly.
   * 
   * After removing the house from the community, the function uses a stream to collect
   * all the member IDs associated with the removed house. These member IDs are then
   * deleted from the house using the `deleteMemberFromHouse` service. Finally, the
   * function saves the updated community and deletes the removed house using the
   * `communityRepository` and `communityHouseRepository`, respectively.
   * 
   * In summary, the `removeHouseFromCommunityByHouseId` function is responsible for
   * removing a house from a community based on its unique identifier, while also
   * updating the community's houses list and deleting any member IDs associated with
   * the removed house.
   * 
   * @returns a boolean value indicating whether the house was successfully removed
   * from the community.
   */
  @Transactional
  @Override
  public boolean removeHouseFromCommunityByHouseId(Community community, String houseId) {
    if (community == null) {
      return false;
    } else {
      Optional<CommunityHouse> houseOptional =
          communityHouseRepository.findByHouseIdWithHouseMembers(houseId);
      return houseOptional.map(house -> {
        Set<CommunityHouse> houses = community.getHouses();
        houses.remove(
            house); //remove the house before deleting house members because otherwise the Set relationship would be broken and remove would not work

        Set<String> memberIds = house.getHouseMembers()
            .stream()
            .map(HouseMember::getMemberId)
            .collect(
                Collectors.toSet()); //streams are immutable so need to collect all the member IDs and then delete them from the house

        memberIds.forEach(id -> houseService.deleteMemberFromHouse(houseId, id));

        communityRepository.save(community);
        communityHouseRepository.deleteByHouseId(houseId);
        return true;
      }).orElse(false);
    }
  }
}
