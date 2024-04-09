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

package com.myhome.services.unit;

import helpers.TestUtils;
import com.myhome.controllers.dto.CommunityDto;
import com.myhome.controllers.dto.mapper.CommunityMapper;
import com.myhome.domain.Community;
import com.myhome.domain.CommunityHouse;
import com.myhome.domain.HouseMember;
import com.myhome.domain.User;
import com.myhome.repositories.CommunityHouseRepository;
import com.myhome.repositories.CommunityRepository;
import com.myhome.repositories.UserRepository;
import com.myhome.services.HouseService;
import com.myhome.services.springdatajpa.CommunitySDJpaService;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;

/**
 * TODO
 */
public class CommunitySDJpaServiceTest {

  private final String TEST_COMMUNITY_ID = "test-community-id";
  private final String TEST_COMMUNITY_NAME = "test-community-name";
  private final String TEST_COMMUNITY_DISTRICT = "test-community-name";

  private final int TEST_ADMINS_COUNT = 2;
  private final int TEST_HOUSES_COUNT = 2;
  private final int TEST_HOUSE_MEMBERS_COUNT = 2;
  private final int TEST_COMMUNITIES_COUNT = 2;

  private final String TEST_ADMIN_ID = "test-admin-id";
  private final String TEST_ADMIN_NAME = "test-user-name";
  private final String TEST_ADMIN_EMAIL = "test-user-email";
  private final String TEST_ADMIN_PASSWORD = "test-user-password";
  private final String TEST_HOUSE_ID = "test-house-id";

  @Mock
  private CommunityRepository communityRepository;
  @Mock
  private UserRepository communityAdminRepository;
  @Mock
  private CommunityMapper communityMapper;
  @Mock
  private CommunityHouseRepository communityHouseRepository;
  @Mock
  private HouseService houseService;

  @InjectMocks
  private CommunitySDJpaService communitySDJpaService;

  /**
   * initiates Mockito annotations for unit testing by calling `MockitoAnnotations.initMocks(this)`.
   */
  @BeforeEach
  private void init() {
    MockitoAnnotations.initMocks(this);
  }

  /**
   * creates a new `User` object with specified name, ID, email, and password, and
   * initializes the `userRole` and `groupMembership` sets to empty lists.
   * 
   * @returns a `User` object containing the specified fields.
   * 
   * 	- The `User` object is constructed with the given name, ID, email, and password.
   * 	- The `HashSet` objects represent the admin's role assignments and group memberships,
   * respectively.
   * 	- The `HashSet` object representing the admin's role assignments is empty,
   * indicating that the admin has no roles assigned.
   * 	- The `HashSet` object representing the admin's group memberships is also empty,
   * indicating that the admin is not a member of any groups.
   */
  private User getTestAdmin() {
    return new User(
        TEST_ADMIN_NAME,
        TEST_ADMIN_ID,
        TEST_ADMIN_EMAIL,
        false,
        TEST_ADMIN_PASSWORD,
        new HashSet<>(),
        new HashSet<>());
  }

  /**
   * retrieves a set of communities from the community repository using the `findAll()`
   * method and compares it with the expected result obtained by calling the
   * `communitySDJpaService.listAll()`. It also verifies that the `communityRepository`
   * was actually called once to retrieve all communities.
   */
  @Test
  void listAllCommunities() {
    // given
    Set<Community> communities = TestUtils.CommunityHelpers.getTestCommunities(TEST_COMMUNITIES_COUNT);
    given(communityRepository.findAll())
        .willReturn(communities);

    // when
    Set<Community> resultCommunities = communitySDJpaService.listAll();

    // then
    assertEquals(communities, resultCommunities);
    verify(communityRepository).findAll();
  }

  /**
   * creates a new community object and maps it to a corresponding DTO object, saving
   * it to the database while authenticating the user through SecurityContextHolder.
   */
  @Test
  void createCommunity() {
    // given
    CommunityDto testCommunityDto = getTestCommunityDto();
    Community testCommunity = TestUtils.CommunityHelpers.getTestCommunity(TEST_COMMUNITY_ID, TEST_COMMUNITY_NAME, TEST_COMMUNITY_DISTRICT, 0, 0);
    UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(TEST_ADMIN_ID,
            null, Collections.emptyList());
    SecurityContextHolder.getContext().setAuthentication(authentication);

    given(communityMapper.communityDtoToCommunity(testCommunityDto))
        .willReturn(testCommunity);
    given(communityAdminRepository.findByUserIdWithCommunities(TEST_ADMIN_ID))
            .willReturn(Optional.of(getTestAdmin()));
    given(communityRepository.save(testCommunity))
        .willReturn(testCommunity);

    // when
    Community createdCommunity = communitySDJpaService.createCommunity(testCommunityDto);

    // then
    assertNotNull(createdCommunity);
    assertEquals(testCommunityDto.getName(), createdCommunity.getName());
    assertEquals(testCommunityDto.getDistrict(), createdCommunity.getDistrict());
    verify(communityMapper).communityDtoToCommunity(testCommunityDto);
    verify(communityAdminRepository).findByUserIdWithCommunities(TEST_ADMIN_ID);
    verify(communityRepository).save(testCommunity);
  }

  /**
   * given a community ID, retrieves all houses associated with that community from the
   * repository, and returns them as an Optional list.
   */
  @Test
  void findCommunityHousesById() {
    // given
    Community testCommunity = TestUtils.CommunityHelpers.getTestCommunity();
    List<CommunityHouse> testCommunityHouses = new ArrayList<>(testCommunity.getHouses());
    given(communityRepository.existsByCommunityId(TEST_COMMUNITY_ID))
        .willReturn(true);
    given(communityHouseRepository.findAllByCommunity_CommunityId(TEST_COMMUNITY_ID, null))
        .willReturn(testCommunityHouses);

    // when
    Optional<List<CommunityHouse>> resultCommunityHousesOptional =
        communitySDJpaService.findCommunityHousesById(TEST_COMMUNITY_ID, null);

    // then
    assertTrue(resultCommunityHousesOptional.isPresent());
    List<CommunityHouse> resultCommunityHouses = resultCommunityHousesOptional.get();
    assertEquals(testCommunityHouses, resultCommunityHouses);
    verify(communityRepository).existsByCommunityId(TEST_COMMUNITY_ID);
    verify(communityHouseRepository).findAllByCommunity_CommunityId(TEST_COMMUNITY_ID, null);
  }

  /**
   * verifies that a community with the specified ID does not exist in the repository
   * by asserting that the `existsByCommunityId` method returns `false`. It also verifies
   * that no community houses are found when querying the repository using the ID.
   */
  @Test
  void findCommunityHousesByIdNotExist() {
    // given
    given(communityRepository.existsByCommunityId(TEST_COMMUNITY_ID))
        .willReturn(false);

    // when
    Optional<List<CommunityHouse>> resultCommunityHousesOptional =
        communitySDJpaService.findCommunityHousesById(TEST_COMMUNITY_ID, null);

    // then
    assertFalse(resultCommunityHousesOptional.isPresent());
    verify(communityRepository).existsByCommunityId(TEST_COMMUNITY_ID);
    verify(communityHouseRepository, never()).findAllByCommunity_CommunityId(TEST_COMMUNITY_ID,
        null);
  }

  /**
   * queries the community repository and communityAdminRepository to retrieve a list
   * of admins for a given community ID. It verifies that the result is present and
   * matches the expected list of admins.
   */
  @Test
  void findCommunityAdminsById() {
    // given
    Community testCommunity = TestUtils.CommunityHelpers.getTestCommunity();
    List<User> testCommunityAdmins = new ArrayList<>(testCommunity.getAdmins());
    given(communityRepository.existsByCommunityId(TEST_COMMUNITY_ID))
        .willReturn(true);
    given(communityAdminRepository.findAllByCommunities_CommunityId(TEST_COMMUNITY_ID, null))
        .willReturn(testCommunityAdmins);

    // when
    Optional<List<User>> resultAdminsOptional =
        communitySDJpaService.findCommunityAdminsById(TEST_COMMUNITY_ID, null);

    // then
    assertTrue((resultAdminsOptional.isPresent()));
    List<User> resultAdmins = resultAdminsOptional.get();
    assertEquals(testCommunityAdmins, resultAdmins);
    verify(communityRepository).existsByCommunityId(TEST_COMMUNITY_ID);
    verify(communityAdminRepository).findAllByCommunities_CommunityId(TEST_COMMUNITY_ID, null);
  }

  /**
   * verifies that a community admin does not exist for a given community ID by querying
   * the community repository and asserting the result.
   */
  @Test
  void findCommunityAdminsByIdNotExists() {
    // given
    given(communityRepository.existsByCommunityId(TEST_COMMUNITY_ID))
        .willReturn(false);

    // when
    Optional<List<User>> resultAdminsOptional =
        communitySDJpaService.findCommunityAdminsById(TEST_COMMUNITY_ID, null);

    // then
    assertFalse((resultAdminsOptional.isPresent()));
    verify(communityRepository).existsByCommunityId(TEST_COMMUNITY_ID);
  }

  /**
   * adds a set of users as admins to a community, by first finding the community with
   * the given ID, then adding each user as an admin to the community using the
   * `communityAdminRepository`, and finally returning the updated community.
   */
  @Test
  void addAdminsToCommunity() {
    // given
    Community testCommunity = TestUtils.CommunityHelpers.getTestCommunity();
    Set<User> adminToAdd = TestUtils.UserHelpers.getTestUsers(TEST_ADMINS_COUNT);
    Set<String> adminToAddIds = adminToAdd.stream()
        .map(admin -> admin.getUserId())
        .collect(Collectors.toSet());

    given(communityRepository.findByCommunityIdWithAdmins(TEST_COMMUNITY_ID))
        .willReturn(Optional.of(testCommunity));
    given(communityRepository.save(testCommunity))
        .willReturn(testCommunity);
    adminToAdd.forEach(admin -> {
      given(communityAdminRepository.findByUserIdWithCommunities(admin.getUserId()))
          .willReturn(Optional.of(admin));
    });
    adminToAdd.forEach(admin -> {
      given(communityAdminRepository.save(admin))
          .willReturn(admin);
    });
    // when
    Optional<Community> updatedCommunityOptional =
        communitySDJpaService.addAdminsToCommunity(TEST_COMMUNITY_ID, adminToAddIds);

    // then
    assertTrue(updatedCommunityOptional.isPresent());
    adminToAdd.forEach(admin -> assertTrue(admin.getCommunities().contains(testCommunity)));
    verify(communityRepository).findByCommunityIdWithAdmins(TEST_COMMUNITY_ID);
    adminToAdd.forEach(
        admin -> verify(communityAdminRepository).findByUserIdWithCommunities(admin.getUserId()));
  }

  /**
   * adds admins to a community that does not exist in the repository. It verifies the
   * existence of the community before adding admins and updates the repository with
   * the added admins.
   */
  @Test
  void addAdminsToCommunityNotExist() {
    // given
    given(communityRepository.findByCommunityIdWithAdmins(TEST_COMMUNITY_ID))
        .willReturn(Optional.empty());

    // when
    Optional<Community> updatedCommunityOptional =
        communitySDJpaService.addAdminsToCommunity(TEST_COMMUNITY_ID, any());

    // then
    assertFalse(updatedCommunityOptional.isPresent());
    verify(communityRepository).findByCommunityIdWithAdmins(TEST_COMMUNITY_ID);
  }

  /**
   * retrieves the details of a community with a given ID from the repository, and
   * verifies that the retrieved community matches the expected one.
   */
  @Test
  void communityDetailsById() {
    // given
    Community testCommunity = TestUtils.CommunityHelpers.getTestCommunity();
    given(communityRepository.findByCommunityId(TEST_COMMUNITY_ID))
        .willReturn(Optional.of(testCommunity));

    // when
    Optional<Community> communityOptional =
        communitySDJpaService.getCommunityDetailsById(TEST_COMMUNITY_ID);

    // then
    assertTrue(communityOptional.isPresent());
    assertEquals(testCommunity, communityOptional.get());
    verify(communityRepository).findByCommunityId(TEST_COMMUNITY_ID);
  }

  /**
   * retrieves the community details for a given ID and admins, using the repository
   * to retrieve the community object and the service to perform the operation with admins.
   */
  @Test
  void communityDetailsByIdWithAdmins() {
    // given
    Community testCommunity = TestUtils.CommunityHelpers.getTestCommunity();
    given(communityRepository.findByCommunityIdWithAdmins(TEST_COMMUNITY_ID))
        .willReturn(Optional.of(testCommunity));

    // when
    Optional<Community> communityOptional =
        communitySDJpaService.getCommunityDetailsByIdWithAdmins(TEST_COMMUNITY_ID);

    // then
    assertTrue(communityOptional.isPresent());
    assertEquals(testCommunity, communityOptional.get());
    verify(communityRepository).findByCommunityIdWithAdmins(TEST_COMMUNITY_ID);
  }

  /**
   * takes a set of houses and a community ID as input, adds the houses to the community
   * in the database, and verifies that the added houses are associated with the correct
   * community.
   */
  @Test
  void addHousesToCommunity() {
    // given
    Community testCommunity = TestUtils.CommunityHelpers.getTestCommunity();
    Set<CommunityHouse> housesToAdd = TestUtils.CommunityHouseHelpers.getTestHouses(TEST_HOUSES_COUNT);

    given(communityRepository.findByCommunityIdWithHouses(TEST_COMMUNITY_ID))
        .willReturn(Optional.of(testCommunity));
    given(communityRepository.save(testCommunity))
        .willReturn(testCommunity);
    housesToAdd.forEach(house -> {
      given(communityHouseRepository.save(house))
          .willReturn(house);
    });

    // when
    Set<String> addedHousesIds =
        communitySDJpaService.addHousesToCommunity(TEST_COMMUNITY_ID, housesToAdd);

    // then
    assertEquals(housesToAdd.size(), addedHousesIds.size());
    housesToAdd.forEach(house -> {
      assertEquals(house.getCommunity(), testCommunity);
    });
    verify(communityRepository).findByCommunityIdWithHouses(TEST_COMMUNITY_ID);
    housesToAdd.forEach(house -> {
      verify(communityHouseRepository).save(house);
    });
  }

  /**
   * tests whether adding houses to a community that does not exist returns an empty
   * set of added house IDs. It uses stubs to mock the community repository's
   * findByCommunityIdWithHouses and save methods, as well as the communityHouseRepository's
   * save method, to verify their behavior.
   */
  @Test
  void addHousesToCommunityNotExist() {
    // given
    Set<CommunityHouse> housesToAdd = TestUtils.CommunityHouseHelpers.getTestHouses(TEST_HOUSES_COUNT);

    given(communityRepository.findByCommunityIdWithHouses(TEST_COMMUNITY_ID))
        .willReturn(Optional.empty());

    // when
    Set<String> addedHousesIds =
        communitySDJpaService.addHousesToCommunity(TEST_COMMUNITY_ID, housesToAdd);

    // then
    assertTrue(addedHousesIds.isEmpty());
    verify(communityRepository).findByCommunityIdWithHouses(TEST_COMMUNITY_ID);
    verify(communityRepository, never()).save(any());
    verify(communityHouseRepository, never()).save(any());
  }

  /**
   * adds a set of houses to an existing community in the database. It first retrieves
   * the community from the repository, then saves it and its associated houses, and
   * finally returns the IDs of the added houses.
   */
  @Test
  void addHousesToCommunityHouseExists() {
    // given
    Community testCommunity = TestUtils.CommunityHelpers.getTestCommunity();
    Set<CommunityHouse> houses = TestUtils.CommunityHouseHelpers.getTestHouses(TEST_HOUSES_COUNT);
    testCommunity.setHouses(houses);

    given(communityRepository.findByCommunityIdWithHouses(TEST_COMMUNITY_ID))
        .willReturn(Optional.of(testCommunity));
    given(communityRepository.save(testCommunity))
        .willReturn(testCommunity);
    houses.forEach(house -> given(communityHouseRepository.save(house)).willReturn(house));

    // when
    Set<String> addedHousesIds =
        communitySDJpaService.addHousesToCommunity(TEST_COMMUNITY_ID, houses);

    // then
    assertTrue(addedHousesIds.isEmpty());
    verify(communityRepository).findByCommunityIdWithHouses(TEST_COMMUNITY_ID);
    verify(communityRepository).save(testCommunity);
    verify(communityHouseRepository, never()).save(any());
  }

  /**
   * removes an admin from a community by retrieving the community with admins, removing
   * the admin from the community, and saving the updated community to the repository.
   */
  @Test
  void removeAdminFromCommunity() {
    // given
    Community testCommunity = TestUtils.CommunityHelpers.getTestCommunity();
    User testAdmin = getTestAdmin();
    testCommunity.getAdmins().add(testAdmin);

    given(communityRepository.findByCommunityIdWithAdmins(TEST_COMMUNITY_ID))
        .willReturn(Optional.of(testCommunity));
    given(communityRepository.save(testCommunity))
        .willReturn(testCommunity);

    // when
    boolean adminRemoved =
        communitySDJpaService.removeAdminFromCommunity(TEST_COMMUNITY_ID, TEST_ADMIN_ID);

    // then
    assertTrue(adminRemoved);
    verify(communityRepository).findByCommunityIdWithAdmins(TEST_COMMUNITY_ID);
    verify(communityRepository).save(testCommunity);
  }

  /**
   * does not remove an admin from a community that does not exist.
   */
  @Test
  void removeAdminFromCommunityNotExists() {
    // given
    given(communityRepository.findByCommunityIdWithAdmins(TEST_COMMUNITY_ID))
        .willReturn(Optional.empty());

    // when
    boolean adminRemoved =
        communitySDJpaService.removeAdminFromCommunity(TEST_COMMUNITY_ID, TEST_ADMIN_ID);

    // then
    assertFalse(adminRemoved);
    verify(communityRepository).findByCommunityIdWithAdmins(TEST_COMMUNITY_ID);
    verify(communityRepository, never()).save(any());
  }

  /**
   * does not remove an admin from a community that does not exist.
   */
  @Test
  void removeAdminFromCommunityAdminNotExists() {
    // given
    Community testCommunity = TestUtils.CommunityHelpers.getTestCommunity();

    given(communityRepository.findByCommunityIdWithAdmins(TEST_COMMUNITY_ID))
        .willReturn(Optional.of(testCommunity));
    given(communityRepository.save(testCommunity))
        .willReturn(testCommunity);

    // when
    boolean adminRemoved =
        communitySDJpaService.removeAdminFromCommunity(TEST_COMMUNITY_ID, TEST_ADMIN_ID);

    // then
    assertFalse(adminRemoved);
    verify(communityRepository).findByCommunityIdWithAdmins(TEST_COMMUNITY_ID);
    verify(communityRepository, never()).save(testCommunity);
  }

  /**
   * deletes a community from the database based on its ID, while also deleting all
   * associated houses. It uses mocking to verify the calls to the `communityRepository`
   * and `communitySDJpaService`.
   */
  @Test
  void deleteCommunity() {
    // given
    Community testCommunity = TestUtils.CommunityHelpers.getTestCommunity();
    Set<CommunityHouse> testCommunityHouses = TestUtils.CommunityHouseHelpers.getTestHouses(TEST_HOUSES_COUNT);
    testCommunity.setHouses(testCommunityHouses);

    given(communityRepository.findByCommunityIdWithHouses(TEST_COMMUNITY_ID))
        .willReturn(Optional.of(testCommunity));
    testCommunityHouses.forEach(house -> {
      given(communityHouseRepository.findByHouseId(house.getHouseId()))
          .willReturn(Optional.of(house));
    });

    testCommunityHouses.forEach(house -> {
      given(communityHouseRepository.findByHouseId(house.getHouseId()))
          .willReturn(Optional.of(house));
    });

    // when
    boolean communityDeleted = communitySDJpaService.deleteCommunity(TEST_COMMUNITY_ID);

    // then
    assertTrue(communityDeleted);
    verify(communityRepository).findByCommunityIdWithHouses(TEST_COMMUNITY_ID);
    verify(communityRepository).delete(testCommunity);
  }

  /**
   * tests whether a community with the given ID exists in the database before attempting
   * to delete it. If the community does not exist, the method asserts that the operation
   * fails and the necessary repository calls are verified to have not been made.
   */
  @Test
  void deleteCommunityNotExists() {
    // given
    Community testCommunity = TestUtils.CommunityHelpers.getTestCommunity();

    given(communityRepository.findByCommunityIdWithHouses(TEST_COMMUNITY_ID))
        .willReturn(Optional.empty());

    // when
    boolean communityDeleted = communitySDJpaService.deleteCommunity(TEST_COMMUNITY_ID);

    // then
    assertFalse(communityDeleted);
    verify(communityRepository).findByCommunityIdWithHouses(TEST_COMMUNITY_ID);
    verify(communityHouseRepository, never()).deleteByHouseId(any());
    verify(communityRepository, never()).delete(testCommunity);
  }

  /**
   * removes a specific house from a community based on its ID, updating the community's
   * house members and deleting the house from the database.
   */
  @Test
  void removeHouseFromCommunityByHouseId() {
    // given
    Community testCommunity = TestUtils.CommunityHelpers.getTestCommunity();
    CommunityHouse testHouse = TestUtils.CommunityHouseHelpers.getTestCommunityHouse(TEST_HOUSE_ID);
    Set<HouseMember> testHouseMembers = TestUtils.HouseMemberHelpers.getTestHouseMembers(TEST_HOUSE_MEMBERS_COUNT);
    testHouse.setHouseMembers(testHouseMembers);
    testCommunity.getHouses().add(testHouse);

    given(communityRepository.findByCommunityIdWithHouses(TEST_COMMUNITY_ID))
        .willReturn(Optional.of(testCommunity));
    given(communityHouseRepository.findByHouseIdWithHouseMembers(TEST_HOUSE_ID))
        .willReturn(Optional.of(testHouse));

    // when
    boolean houseDeleted =
        communitySDJpaService.removeHouseFromCommunityByHouseId(testCommunity, TEST_HOUSE_ID);

    // then
    assertTrue(houseDeleted);
    assertFalse(testCommunity.getHouses().contains(testHouse));
    verify(communityRepository).save(testCommunity);
    testHouse.getHouseMembers()
        .forEach(houseMember -> verify(houseService).deleteMemberFromHouse(TEST_HOUSE_ID,
            houseMember.getMemberId()));
    verify(communityHouseRepository).findByHouseIdWithHouseMembers(TEST_HOUSE_ID);
    verify(communityHouseRepository).deleteByHouseId(TEST_HOUSE_ID);
  }

  /**
   * verifies that a house cannot be removed from a community that does not exist. It
   * does this by asserting that the delete operation fails and no interactions with
   * the house or community services are made.
   */
  @Test
  void removeHouseFromCommunityByHouseIdCommunityNotExists() {
    // given
    Community testCommunity = TestUtils.CommunityHelpers.getTestCommunity();

    given(communityRepository.findByCommunityIdWithHouses(TEST_COMMUNITY_ID))
        .willReturn(Optional.empty());

    // when
    boolean houseDeleted =
        communitySDJpaService.removeHouseFromCommunityByHouseId(null, TEST_HOUSE_ID);

    // then
    assertFalse(houseDeleted);
    verify(communityHouseRepository, never()).findByHouseId(TEST_HOUSE_ID);
    verifyNoInteractions(houseService);
    verify(communityRepository, never()).save(testCommunity);
  }

  /**
   * checks whether a house can be removed from a community by its ID when the house
   * does not exist in the repository.
   */
  @Test
  void removeHouseFromCommunityByHouseIdHouseNotExists() {
    // given
    Community testCommunity = TestUtils.CommunityHelpers.getTestCommunity();

    given(communityHouseRepository.findByHouseIdWithHouseMembers(TEST_HOUSE_ID))
        .willReturn(Optional.empty());

    // when
    boolean houseDeleted =
        communitySDJpaService.removeHouseFromCommunityByHouseId(testCommunity, TEST_HOUSE_ID);

    // then
    assertFalse(houseDeleted);
    verify(communityHouseRepository).findByHouseIdWithHouseMembers(TEST_HOUSE_ID);
    verifyNoInteractions(houseService);
    verify(communityRepository, never()).save(testCommunity);
  }

  /**
   * attempts to remove a house from a community using its unique house ID, but does
   * not delete it if it is not already present in the community.
   */
  @Test
  void removeHouseFromCommunityByHouseIdHouseNotInCommunity() {
    // given
    Community testCommunity = TestUtils.CommunityHelpers.getTestCommunity();

    given(communityHouseRepository.findByHouseIdWithHouseMembers(TEST_HOUSE_ID))
        .willReturn(Optional.empty());

    // when
    boolean houseDeleted =
        communitySDJpaService.removeHouseFromCommunityByHouseId(testCommunity, TEST_HOUSE_ID);

    // then
    assertFalse(houseDeleted);
    verify(communityHouseRepository).findByHouseIdWithHouseMembers(TEST_HOUSE_ID);
    verifyNoInteractions(houseService);
    verify(communityRepository, never()).save(testCommunity);
  }

  /**
   * creates a new `CommunityDto` object with predefined values for community ID,
   * district, and name.
   * 
   * @returns a `CommunityDto` object with pre-populated values for community ID,
   * district, and name.
   * 
   * 	- `testCommunityDto`: A new instance of the `CommunityDto` class is created and
   * returned by the function.
   * 	- `setCommunityId()`: The `CommunityDto` object contains a `communityId` field
   * that sets the value of this field to `TEST_COMMUNITY_ID`.
   * 	- `setDistrict()`: The `CommunityDto` object contains a `district` field that
   * sets the value of this field to `TEST_COMMUNITY_DISTRICT`.
   * 	- `setName()`: The `CommunityDto` object contains a `name` field that sets the
   * value of this field to `TEST_COMMUNITY_NAME`.
   */
  private CommunityDto getTestCommunityDto() {
    CommunityDto testCommunityDto = new CommunityDto();
    testCommunityDto.setCommunityId(TEST_COMMUNITY_ID);
    testCommunityDto.setDistrict(TEST_COMMUNITY_DISTRICT);
    testCommunityDto.setName(TEST_COMMUNITY_NAME);
    return testCommunityDto;
  }

}
