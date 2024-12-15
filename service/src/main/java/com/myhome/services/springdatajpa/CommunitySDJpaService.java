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
 * is responsible for managing communities in a system. It provides various methods
 * for creating, updating, and deleting communities, as well as their houses and
 * members. The service uses JPA (Java Persistence API) to interact with the database
 * and provides transactional support for atomicity and consistency.
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
   * creates a new community and adds an administrator with the specified user ID to
   * it, saves the community to the repository, and logs a trace message.
   * 
   * @param communityDto CommunityDTO object containing the data for the community to
   * be created, which is then used to create a new community instance and save it to
   * the repository.
   * 
   * * `communityDto.setCommunityId(generateUniqueId());`: This line sets the `id`
   * property of the newly created community to a generated unique identifier.
   * * `String userId = (String) SecurityContextHolder.getContext().getAuthentication().getPrincipal();`:
   * This line retrieves the current user's ID from the security context holder.
   * * `Community community = addAdminToCommunity(communityMapper.communityDtoToCommunity(communityDto),
   * userId);`: This line creates a new community instance and adds an admin to it using
   * the `addAdminToCommunity` method, passing in the deserialized `communityDto` and
   * the user's ID as arguments.
   * * `Community savedCommunity = communityRepository.save(community);`: This line
   * saves the newly created community instance to the repository, which persists the
   * changes to the underlying data storage.
   * 
   * @returns a saved community object in the repository.
   * 
   * * `community`: The created community object, which contains the unique ID generated
   * by the function, as well as other properties such as the user ID of the admin who
   * added it.
   * * `savedCommunity`: The saved community object in the repository, which contains
   * the ID of the community and other attributes such as its name and description.
   * * `log.trace()`: A logging statement that indicates the community was saved to the
   * repository with its ID.
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
   * adds a user as an administrator to a community by retrieving the user's existing
   * communities and then adding the specified community to those communities.
   * 
   * @param community Community object to which an administrator is being added.
   * 
   * The `Community` object represents a community in the system, with attributes such
   * as `id`, `name`, and `description`. Additionally, it has a set of `User` objects
   * representing its admins, which are stored in the `admins` field. The function
   * modifies this field by adding a new admin to the community if one is present, or
   * replacing the existing admins with a new set if none were previously present.
   * 
   * @param userId ID of the user to be added as an administrator to the
   * given community.
   * 
   * @returns a modified Community object with the added admin and their associated communities.
   * 
   * * The `community` variable is updated with the added admin(s) by setting its
   * `admins` field to a new Set containing all the admins.
   * * The `userId` parameter is passed as a parameter to the repository method
   * `findByUserIdWithCommunities`, indicating which user to find and add as an admin.
   * * The `ifPresent` method is used to handle the case where a user with the specified
   * `userId` exists in the database, allowing for efficient handling of non-existent
   * users.
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
   * takes a `Pageable` object and returns a `Set` of `Community` objects that are
   * retrieved from the database using the `findAll` method of the `communityRepository`.
   * 
   * @param pageable page of data to be retrieved from the `CommunityRepository`, and
   * it is used to control the iteration over the results in the `listAll()` method.
   * 
   * * `Pageable`: This interface represents a pagination mechanism that allows for the
   * retrieval of a subset of objects from a data source. It contains various attributes
   * such as `getPageNumber()` for getting the current page number and `getPageSize()`
   * for getting the number of objects per page.
   * 
   * @returns a set of `Community` objects.
   * 
   * The Set<Community> object represents a collection of Community objects that have
   * been retrieved from the database using the findAll method of the communityRepository.
   * 
   * Each element in the set is a Community object, which contains information about a
   * particular community, such as its name, description, and location.
   * 
   * The Set<Community> object is an unmodifiable collection, meaning that it cannot
   * be modified or changed once it has been created. This ensures that the data remains
   * consistent and tamper-proof.
   * 
   * The pageable argument passed to the findAll method represents a pagination
   * configuration, which determines how many Community objects are retrieved from the
   * database at a time. This allows for efficient retrieval of large datasets in smaller
   * chunks, rather than loading all the data into memory at once.
   */
  @Override
  public Set<Community> listAll(Pageable pageable) {
    Set<Community> communityListSet = new HashSet<>();
    communityRepository.findAll(pageable).forEach(communityListSet::add);
    return communityListSet;
  }

  /**
   * returns a set of all `Community` objects stored in the repository, by calling
   * `findAll()` and adding each object to the set using `add()`.
   * 
   * @returns a set of all communities stored in the repository.
   * 
   * * The output is of type `Set`, indicating that it is a collection of elements, in
   * this case, `Community` objects.
   * * The set contains all the communities retrieved from the database using the
   * `findAll()` method of the `communityRepository`.
   * * The `HashSet` implementation ensures that the set will contain no duplicates and
   * will not allow for null values.
   */
  @Override public Set<Community> listAll() {
    Set<Community> communities = new HashSet<>();
    communityRepository.findAll().forEach(communities::add);
    return communities;
  }

  /**
   * retrieves a list of community houses associated with a given community ID. If a
   * match is found, it returns an optional list of community houses; otherwise, it
   * returns an empty list.
   * 
   * @param communityId community ID that is used to filter the community houses in the
   * database.
   * 
   * @param pageable pagination information for the community houses, allowing the
   * function to retrieve a specific page of results from the database.
   * 
   * * `communityId`: The ID of the community for which the houses are sought.
   * * `Pageable`: A pageable object that provides a way to navigate through a large
   * dataset. It has various attributes such as `size`, `pageNumber`, `totalPages`, and
   * `totalElements`.
   * 
   * @returns a pageable list of community houses associated with the provided community
   * ID.
   * 
   * * The `Optional<List<CommunityHouse>>` represents a list of community houses that
   * match the given community ID.
   * * If the `exists` boolean is true, then the list is not empty and contains at least
   * one community house.
   * * If the `exists` boolean is false, then the list is empty and no community houses
   * were found for the given community ID.
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
   * queries the community and communityAdmin tables to retrieve a list of community
   * admins for a given community ID, returning an optional list of users if found,
   * otherwise returning an empty list.
   * 
   * @param communityId identifier of the community whose admin users are to be retrieved.
   * 
   * @param pageable page of results to be returned by the query, allowing for pagination
   * and control over the number of results returned per page.
   * 
   * * `communityId`: A string representing the unique identifier for a community.
   * * `Pageable`: An object that represents a page of data in a collection. It contains
   * the page number and the size of each page.
   * 
   * @returns a `Optional` of a list of `User` objects, where each element in the list
   * represents an admin for the specified community.
   * 
   * * `Optional<List<User>>`: The output is an optional list of users who are community
   * admins for the specified community ID. If no users exist with the provided community
   * ID, the output will be `Optional.empty()`.
   * * `List<User>`: The list contains all the users who are community admins for the
   * specified community ID.
   * * `Pageable`: The pageable interface is used to define a page of results that can
   * be retrieved from the database.
   * * `communityId`: The input parameter represents the ID of the community for which
   * the list of community admins is being retrieved.
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
   * retrieves a `User` object representing the community administrator with the specified
   * `adminId`.
   * 
   * @param adminId user ID of the community administrator to be retrieved.
   * 
   * @returns an Optional object containing a `User` instance if the admin exists,
   * otherwise it is empty.
   * 
   * The `Optional<User>` return type indicates that the function may return `None` if
   * no community administrator is found for the given `adminId`.
   * 
   * The `findByUserId` method call within the function retrieves a `User` object from
   * the `communityAdminRepository`, where the `User.id()` field matches the `adminId`
   * parameter passed to the function.
   * 
   * The returned `Optional<User>` contains the retrieved `User` object if found,
   * otherwise it is `None`.
   */
  @Override
  public Optional<User> findCommunityAdminById(String adminId) {
    return communityAdminRepository.findByUserId(adminId);
  }

  /**
   * retrieves the details of a community with the provided `communityId`.
   * 
   * @param communityId ID of a community that is being retrieved from the database.
   * 
   * @returns an optional instance of `Community`.
   * 
   * Optional<Community> represents an optional Community object, which means that it
   * may be null if no Community with the given communityId exists.
   * Community is a class that contains information about a community, including its
   * id, name, and location.
   */
  @Override public Optional<Community> getCommunityDetailsById(String communityId) {
    return communityRepository.findByCommunityId(communityId);
  }

  /**
   * retrieves a community's details and admins based on its ID.
   * 
   * @param communityId ID of the community for which details and administrators are
   * being retrieved.
   * 
   * @returns an optional object containing the details of a community along with its
   * administrators.
   * 
   * * `Optional<Community>` represents an optional Community object that may or may
   * not be present depending on the existence of a community with the specified ID.
   * * `communityRepository.findByCommunityIdWithAdmins(communityId)` is used to retrieve
   * a Community object associated with the given ID, along with its admin users.
   */
  @Override
  public Optional<Community> getCommunityDetailsByIdWithAdmins(String communityId) {
    return communityRepository.findByCommunityIdWithAdmins(communityId);
  }

  /**
   * takes a community ID and a set of admin IDs, adds the admins to the community's
   * admin list, and returns an Optional<Community> representing the updated community.
   * 
   * @param communityId identifier of the community for which the admins are being added.
   * 
   * @param adminsIds set of user IDs of the admins to be added to the community.
   * 
   * * `Set<String> adminsIds`: This is a set of strings representing user IDs that
   * will be added as admins to a community.
   * * `communityId`: This is the ID of the community that the admins will be added to.
   * 
   * @returns an optional `Community` object that has been updated with the provided admins.
   * 
   * * `Optional<Community> communitySearch`: This represents an optional `Community`
   * object that is found by its `communityId`. If no such `Community` object exists,
   * this will be `Optional.empty()`.
   * * `map()` method: This method returns a new `Optional` containing the result of
   * applying the given function to the `Community` object. In this case, the function
   * adds the provided admins to the community by updating the `admins` field and then
   * saving the modified `Community` object.
   * * `orElseGet()` method: This method returns a new `Optional` containing either the
   * result of the given function or an empty `Optional` if the original `Optional` is
   * empty. In this case, the function is called with the `admin` object and its
   * `Community` field is updated and saved. If the `Optional` returned by the
   * `findByCommunityIdWithAdmins` method is empty, this method will return an empty `Optional`.
   * * `save()` method: This method saves the modified `Community` object in the repository.
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
   * adds new or modified houses to a community by checking if they already exist in
   * the community, creating a unique ID if necessary, and then adding them to the
   * community's house list. If a house already exists, it is updated with a new unique
   * ID and saved.
   * 
   * @param communityId ID of the community to which the houses will be added.
   * 
   * @param houses set of houses to be added to the community.
   * 
   * * `houses` is a `Set` containing `CommunityHouse` objects.
   * * Each `CommunityHouse` object has several attributes, including `houseId`, `name`,
   * and `community`.
   * * The `houseId` attribute is a unique identifier for each house.
   * * The `name` attribute is the name of the house.
   * * The `community` attribute refers to the community that the house belongs to.
   * 
   * The function first searches for an existing community with the given `communityId`
   * using the `communityRepository`. If a match is found, the function adds each house
   * in the `houses` set to the existing community and saves the community. Otherwise,
   * the function creates a new community with the `generateUniqueId()` method, adds
   * each house to the community, and saves the community.
   * 
   * @returns a set of unique house IDs that have been added to the community, along
   * with the updated community entity.
   * 
   * * `Set<String> addedIds`: This set contains the house IDs that were successfully
   * added to the community. These IDs are unique and have been generated using a unique
   * identifier algorithm.
   * * `Optional<Community> communitySearch`: This is an optional object that represents
   * the community being searched for. If the community is found, the function will
   * return the community with its houses. Otherwise, the function will return an empty
   * set.
   * * `Set<CommunityHouse> communityHouses`: This set contains the existing houses in
   * the community. The function iterates over these houses and checks if each one
   * matches the house being added. If a match is found, the house ID is already occupied,
   * and a new unique ID is generated for the added house.
   * * `CommunityHouseRepository save(house)`: This method saves the updated house
   * object in the repository.
   * 
   * The function returns a set of house IDs that have been successfully added to the
   * community. These IDs are unique and have been generated using a unique identifier
   * algorithm. The function also checks if the house exists in the community before
   * adding it, and if so, it updates the existing house object instead of creating a
   * new one.
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
   * removes an admin from a community by searching for the community in the repository,
   * removing the admin from the community's admin list, and saving the community if
   * the removal was successful.
   * 
   * @param communityId unique identifier of a community for which an admin is to be removed.
   * 
   * @param adminId ID of an admin to be removed from a community.
   * 
   * @returns a boolean value indicating whether the admin was successfully removed
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
   * deletes a community based on its ID, retrieves all houses associated with it, and
   * then removes each house from the community using its ID before deleting the community
   * itself.
   * 
   * @param communityId Id of the community to be deleted.
   * 
   * @returns a boolean value indicating whether the community was successfully deleted.
   * 
   * * `return`: This indicates the function returns a boolean value indicating whether
   * the community was successfully deleted.
   * * `communityRepository.findByCommunityIdWithHouses(communityId)`: This is a method
   * call that retrieves all houses associated with the given community ID. The method
   * returns a stream of `CommunityHouse` objects, which are then mapped to a set of
   * house IDs using the `map()` method.
   * * `.map(community -> { ... })`: This is a lambda expression that takes the `Community`
   * object returned by the previous method call and performs an operation on it. The
   * lambda expression returns a new stream of `String` values, which represent the
   * house IDs associated with the community.
   * * `Set<String> houseIds = ...`: This line of code creates a set of `String` values
   * that represent the house IDs associated with the community.
   * * `houseIds.forEach(houseId -> removeHouseFromCommunityByHouseId(community,
   * houseId))`: This line of code performs an operation on each value in the `houseIds`
   * set. Specifically, it calls the `removeHouseFromCommunityByHouseId()` function
   * with the community and house ID arguments.
   * * `communityRepository.delete(community)`: This is a method call that deletes the
   * community from the repository.
   * * `.orElse(false)`: This line of code returns the result of the previous method
   * call, or `false` if the community could not be deleted.
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
   * generates a unique identifier based on a Universally Unique Identifier (UUID)
   * generated using the `UUID.randomUUID()` method.
   * 
   * @returns a unique, randomly generated string of letters and numbers.
   */
  private String generateUniqueId() {
    return UUID.randomUUID().toString();
  }

  /**
   * removes a house from a community by first removing it from the community's houses
   * collection, then deleting its members from the house, and finally saving the
   * community and deleting the house.
   * 
   * @param community Community object that contains the houses to be removed, and is
   * used to perform the actual removal of the houses from the community.
   * 
   * * `community`: A `Community` object that represents a community in the application.
   * It has various attributes such as `id`, `name`, `description`, and `members`.
   * * `houseId`: The unique identifier of the house to be removed from the community.
   * 
   * The function first checks if the `community` parameter is null, and returns false
   * if it is. Otherwise, it finds an optional `CommunityHouse` object using the
   * `findByHouseIdWithHouseMembers` method of the `communityHouseRepository`. If there
   * is no house with the given `houseId`, the function returns false.
   * 
   * Next, the function modifies the `community` object by removing the house with the
   * specified `houseId` from its `houses` set. This is necessary because the Set
   * relationship between a community and its houses is not mutable, and removing a
   * house without first updating the community would result in a broken Set.
   * 
   * After removing the house, the function streams the members of the house and maps
   * their `memberId` attributes to a `Set`. It then deletes each member from the house
   * using the `deleteMemberFromHouse` function.
   * 
   * Finally, the function saves the updated community using the `communityRepository`,
   * and deletes the removed house using the `communityHouseRepository`. The function
   * returns true if the removal was successful, or false otherwise.
   * 
   * @param houseId id of the house to be removed from the community.
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
