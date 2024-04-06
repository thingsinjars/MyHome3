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

package com.myhome.controllers;

import com.myhome.controllers.dto.CommunityDto;
import com.myhome.controllers.dto.UserDto;
import com.myhome.controllers.mapper.CommunityApiMapper;
import com.myhome.domain.Community;
import com.myhome.domain.CommunityHouse;
import com.myhome.domain.User;
import com.myhome.model.AddCommunityAdminRequest;
import com.myhome.model.AddCommunityAdminResponse;
import com.myhome.model.AddCommunityHouseRequest;
import com.myhome.model.AddCommunityHouseResponse;
import com.myhome.model.CommunityHouseName;
import com.myhome.model.CreateCommunityRequest;
import com.myhome.model.CreateCommunityResponse;
import com.myhome.model.GetCommunityDetailsResponse;
import com.myhome.model.GetCommunityDetailsResponseCommunity;
import com.myhome.model.GetHouseDetailsResponse;
import com.myhome.model.GetHouseDetailsResponseCommunityHouse;
import com.myhome.model.ListCommunityAdminsResponse;
import com.myhome.model.ListCommunityAdminsResponseCommunityAdmin;
import com.myhome.services.CommunityService;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import static java.util.Collections.singletonList;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;

class CommunityControllerTest {
  private static final String COMMUNITY_ADMIN_ID = "1";
  private static final String COMMUNITY_ADMIN_NAME = "Test Name";
  private static final String COMMUNITY_ADMIN_EMAIL = "testadmin@myhome.com";
  private static final String COMMUNITY_ADMIN_PASSWORD = "testpassword@myhome.com";
  private static final String COMMUNITY_HOUSE_ID = "2";
  private static final String COMMUNITY_HOUSE_NAME = "Test House";
  private static final String COMMUNITY_NAME = "Test Community";
  private static final String COMMUNITY_ID = "3";
  private static final String COMMUNITY_DISTRICT = "Wonderland";

  @Mock
  private CommunityService communityService;

  @Mock
  private CommunityApiMapper communityApiMapper;

  @InjectMocks
  private CommunityController communityController;

  /**
   * initializes mock objects using MockitoAnnotations, making it easier to use mock
   * objects during testing.
   */
  @BeforeEach
  private void init() {
    MockitoAnnotations.initMocks(this);
  }

  /**
   * generates a test instance of the `CommunityDto` class, setting the `communityId`,
   * `name`, `district`, and `admins` fields with appropriate values.
   * 
   * @returns a `CommunityDto` object containing the specified community details and administrators.
   */
  private CommunityDto createTestCommunityDto() {
    Set<UserDto> communityAdminDtos = new HashSet<>();
    UserDto userDto = UserDto.builder()
        .userId(COMMUNITY_ADMIN_ID)
        .name(COMMUNITY_ADMIN_NAME)
        .email(COMMUNITY_ADMIN_NAME)
        .password(COMMUNITY_ADMIN_PASSWORD)
        .communityIds(new HashSet<>(singletonList(COMMUNITY_ID)))
        .build();

    communityAdminDtos.add(userDto);
    CommunityDto communityDto = new CommunityDto();
    communityDto.setCommunityId(COMMUNITY_ID);
    communityDto.setName(COMMUNITY_NAME);
    communityDto.setDistrict(COMMUNITY_DISTRICT);
    communityDto.setAdmins(communityAdminDtos);

    return communityDto;
  }

  /**
   * creates a new instance of the `CommunityHouse` class with provided community, name,
   * ID, and initial members and groups sets.
   * 
   * @param community Community object that the `createTestCommunityHouse()` method
   * creates a new instance of the `CommunityHouse` class for.
   * 
   * @returns a new `CommunityHouse` instance representing a test community house with
   * a unique name and ID.
   */
  private CommunityHouse createTestCommunityHouse(Community community) {
    return new CommunityHouse(community, COMMUNITY_HOUSE_NAME, COMMUNITY_HOUSE_ID, new HashSet<>(),
        new HashSet<>());
  }

  /**
   * creates a new community with a default set of admins, houses and other fields
   * populated with test data.
   * 
   * @returns a `Community` object representing a test community with a name, ID,
   * district, and an admin user.
   */
  private Community createTestCommunity() {
    Community community =
        new Community(new HashSet<>(), new HashSet<>(), COMMUNITY_NAME, COMMUNITY_ID,
            COMMUNITY_DISTRICT, new HashSet<>());
    User admin = new User(COMMUNITY_ADMIN_NAME, COMMUNITY_ADMIN_ID, COMMUNITY_ADMIN_EMAIL, true,
        COMMUNITY_ADMIN_PASSWORD, new HashSet<>(), null);
    community.getAdmins().add(admin);
    community.getHouses().add(createTestCommunityHouse(community));
    admin.getCommunities().add(community);

    return community;
  }

  /**
   * verifies that the community creation request is successfully processed by the
   * service, resulting in a `HttpStatus.CREATED` response and a matching `CreateCommunityResponse`.
   */
  @Test
  void shouldCreateCommunitySuccessfully() {
    // given
    CreateCommunityRequest request =
        new CreateCommunityRequest()
            .name(COMMUNITY_NAME)
            .district(COMMUNITY_DISTRICT);
    CommunityDto communityDto = createTestCommunityDto();
    CreateCommunityResponse response =
        new CreateCommunityResponse()
            .communityId(COMMUNITY_ID);
    Community community = createTestCommunity();

    given(communityApiMapper.createCommunityRequestToCommunityDto(request))
        .willReturn(communityDto);
    given(communityService.createCommunity(communityDto))
        .willReturn(community);
    given(communityApiMapper.communityToCreateCommunityResponse(community))
        .willReturn(response);

    // when
    ResponseEntity<CreateCommunityResponse> responseEntity =
        communityController.createCommunity(request);

    // then
    assertEquals(HttpStatus.CREATED, responseEntity.getStatusCode());
    assertEquals(response, responseEntity.getBody());
    verify(communityApiMapper).createCommunityRequestToCommunityDto(request);
    verify(communityApiMapper).communityToCreateCommunityResponse(community);
    verify(communityService).createCommunity(communityDto);
  }

  /**
   * tests the `listAllCommunity()` method of a community controller. It verifies that
   * the method returns a list of communities in the expected format and status code.
   */
  @Test
  void shouldListAllCommunitiesSuccessfully() {
    // given
    Set<Community> communities = new HashSet<>();
    Community community = createTestCommunity();
    communities.add(community);

    Set<GetCommunityDetailsResponseCommunity> communityDetailsResponse
        = new HashSet<>();
    communityDetailsResponse.add(
        new GetCommunityDetailsResponseCommunity()
            .communityId(COMMUNITY_ID)
            .name(COMMUNITY_NAME)
            .district(COMMUNITY_DISTRICT)
    );

    GetCommunityDetailsResponse response = new GetCommunityDetailsResponse();
    response.getCommunities().addAll(communityDetailsResponse);

    Pageable pageable = PageRequest.of(0, 1);
    given(communityService.listAll(pageable))
        .willReturn(communities);
    given(communityApiMapper.communitySetToRestApiResponseCommunitySet(communities))
        .willReturn(communityDetailsResponse);

    // when
    ResponseEntity<GetCommunityDetailsResponse> responseEntity =
        communityController.listAllCommunity(pageable);

    // then
    assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
    assertEquals(response, responseEntity.getBody());
    verify(communityApiMapper).communitySetToRestApiResponseCommunitySet(communities);
    verify(communityService).listAll(pageable);
  }

  /**
   * tests whether the `listCommunityDetails` endpoint returns the correct community
   * details when the ID is provided. It uses mocks to verify the calls to the service
   * and API mapper.
   */
  @Test
  void shouldGetCommunityDetailsSuccessfully() {
    // given
    Optional<Community> communityOptional = Optional.of(createTestCommunity());
    Community community = communityOptional.get();
    GetCommunityDetailsResponseCommunity communityDetails =
        new GetCommunityDetailsResponseCommunity()
            .communityId(COMMUNITY_ID)
            .name(COMMUNITY_NAME)
            .district(COMMUNITY_DISTRICT);

    Set<GetCommunityDetailsResponseCommunity> communityDetailsResponse
        = new HashSet<>();
    communityDetailsResponse.add(communityDetails);

    GetCommunityDetailsResponse response =
        new GetCommunityDetailsResponse().communities(communityDetailsResponse);

    given(communityService.getCommunityDetailsById(COMMUNITY_ID))
        .willReturn(communityOptional);
    given(communityApiMapper.communityToRestApiResponseCommunity(community))
        .willReturn(communityDetails);

    // when
    ResponseEntity<GetCommunityDetailsResponse> responseEntity =
        communityController.listCommunityDetails(COMMUNITY_ID);

    // then
    assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
    assertEquals(response, responseEntity.getBody());
    verify(communityService).getCommunityDetailsById(COMMUNITY_ID);
    verify(communityApiMapper).communityToRestApiResponseCommunity(community);
  }

  /**
   * tests that listing community details with a non-existent ID returns a
   * `HttpStatus.NOT_FOUND` response and an empty body, while verifying the calls to
   * the community service and community API mapper.
   */
  @Test
  void shouldGetNotFoundListCommunityDetailsSuccess() {
    // given
    given(communityService.getCommunityDetailsById(COMMUNITY_ID))
        .willReturn(Optional.empty());

    // when
    ResponseEntity<GetCommunityDetailsResponse> responseEntity =
        communityController.listCommunityDetails(COMMUNITY_ID);

    // then
    assertEquals(HttpStatus.NOT_FOUND, responseEntity.getStatusCode());
    assertNull(responseEntity.getBody());
    verify(communityService).getCommunityDetailsById(COMMUNITY_ID);
    verifyNoInteractions(communityApiMapper);
  }

  /**
   * tests the `listCommunityAdmins` endpoint by providing a community ID and a pageable
   * request object, then verifying that the response status code is `OK` and the
   * response contains the expected community admins in the format expected by the API.
   */
  @Test
  void shouldListCommunityAdminsSuccess() {
    // given
    Community community = createTestCommunity();
    List<User> admins = new ArrayList<>(community.getAdmins());
    Optional<List<User>> communityAdminsOptional = Optional.of(admins);

    Pageable pageable = PageRequest.of(0, 1);

    given(communityService.findCommunityAdminsById(COMMUNITY_ID, pageable))
        .willReturn(communityAdminsOptional);

    Set<User> adminsSet = new HashSet<>(admins);

    Set<ListCommunityAdminsResponseCommunityAdmin> listAdminsResponses = new HashSet<>();
    listAdminsResponses.add(
        new ListCommunityAdminsResponseCommunityAdmin()
            .adminId(COMMUNITY_ADMIN_ID)
    );

    given(communityApiMapper.communityAdminSetToRestApiResponseCommunityAdminSet(adminsSet))
        .willReturn(listAdminsResponses);

    ListCommunityAdminsResponse response =
        new ListCommunityAdminsResponse().admins(listAdminsResponses);

    // when
    ResponseEntity<ListCommunityAdminsResponse> responseEntity =
        communityController.listCommunityAdmins(COMMUNITY_ID, pageable);

    // then
    assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
    assertEquals(response, responseEntity.getBody());
    verify(communityApiMapper).communityAdminSetToRestApiResponseCommunityAdminSet(adminsSet);
    verify(communityService).findCommunityAdminsById(COMMUNITY_ID, pageable);
  }

  /**
   * tests that when no community admins are found for a given community ID and page
   * number, the API returns a `HttpStatus.NOT_FOUND` status code and an empty list of
   * admins.
   */
  @Test
  void shouldReturnNoAdminDetailsNotFoundSuccess() {
    // given
    Pageable pageable = PageRequest.of(0, 1);

    given(communityService.findCommunityAdminsById(COMMUNITY_ID, pageable))
        .willReturn(Optional.empty());

    // when
    ResponseEntity<ListCommunityAdminsResponse> responseEntity =
        communityController.listCommunityAdmins(COMMUNITY_ID, pageable);

    // then
    assertEquals(HttpStatus.NOT_FOUND, responseEntity.getStatusCode());
    assertNull(responseEntity.getBody());
    verify(communityService).findCommunityAdminsById(COMMUNITY_ID, pageable);
    verifyNoInteractions(communityApiMapper);
  }

  /**
   * tests the ability to successfully add community admins to a community using the
   * `communityController`. It provides an AddCommunityAdminRequest, creates a test
   * community, adds admins to the request, and then verifies that the response is
   * successful and the admins are added to the community.
   */
  @Test
  void shouldAddCommunityAdminSuccess() {
    // given
    AddCommunityAdminRequest addRequest = new AddCommunityAdminRequest();
    Community community = createTestCommunity();
    Set<User> communityAdmins = community.getAdmins();
    for (User admin : communityAdmins) {
      addRequest.getAdmins().add(admin.getUserId());
    }

    Set<String> adminIds = addRequest.getAdmins();
    AddCommunityAdminResponse response = new AddCommunityAdminResponse().admins(adminIds);

    given(communityService.addAdminsToCommunity(COMMUNITY_ID, adminIds))
        .willReturn(Optional.of(community));

    // when
    ResponseEntity<AddCommunityAdminResponse> responseEntity =
        communityController.addCommunityAdmins(COMMUNITY_ID, addRequest);

    // then
    assertEquals(HttpStatus.CREATED, responseEntity.getStatusCode());
    assertEquals(response, responseEntity.getBody());
    verify(communityService).addAdminsToCommunity(COMMUNITY_ID, adminIds);
  }

  /**
   * tests whether an attempt to add admins to a community that does not exist returns
   * a `HttpStatus.NOT_FOUND` response and no admin details in the body of the response.
   */
  @Test
  void shouldNotAddAdminToCommunityNotFoundSuccessfully() {
    // given
    AddCommunityAdminRequest addRequest = new AddCommunityAdminRequest();
    Community community = createTestCommunity();
    Set<User> communityAdmins = community.getAdmins();
    for (User admin : communityAdmins) {
      addRequest.getAdmins().add(admin.getUserId());
    }

    Set<String> adminIds = addRequest.getAdmins();

    given(communityService.addAdminsToCommunity(COMMUNITY_ID, adminIds))
        .willReturn(Optional.empty());

    // when
    ResponseEntity<AddCommunityAdminResponse> responseEntity =
        communityController.addCommunityAdmins(COMMUNITY_ID, addRequest);

    // then
    assertEquals(HttpStatus.NOT_FOUND, responseEntity.getStatusCode());
    assertNull(responseEntity.getBody());
    verify(communityService).addAdminsToCommunity(COMMUNITY_ID, adminIds);
  }

  /**
   * tests the listCommunityHouses endpoint by providing a community ID and page number
   * to retrieve a list of community houses. It verifies that the response status code
   * is OK, the response body contains the expected list of houses, and that the
   * findCommunityHousesById and communityApiMapper methods are called with the correct
   * parameters.
   */
  @Test
  void shouldListCommunityHousesSuccess() {
    Community community = createTestCommunity();
    List<CommunityHouse> houses = new ArrayList<>(community.getHouses());
    Set<CommunityHouse> housesSet = new HashSet<>(houses);
    Set<GetHouseDetailsResponseCommunityHouse> getHouseDetailsSet = new HashSet<>();
    getHouseDetailsSet.add(new GetHouseDetailsResponseCommunityHouse()
        .houseId(COMMUNITY_HOUSE_ID)
        .name(COMMUNITY_NAME)
    );

    GetHouseDetailsResponse response = new GetHouseDetailsResponse().houses(getHouseDetailsSet);
    Pageable pageable = PageRequest.of(0, 1);

    given(communityService.findCommunityHousesById(COMMUNITY_ID, pageable))
        .willReturn(Optional.of(houses));
    given(communityApiMapper.communityHouseSetToRestApiResponseCommunityHouseSet(housesSet))
        .willReturn(getHouseDetailsSet);

    // when
    ResponseEntity<GetHouseDetailsResponse> responseEntity =
        communityController.listCommunityHouses(COMMUNITY_ID, pageable);

    //then
    assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
    assertEquals(response, responseEntity.getBody());
    verify(communityService).findCommunityHousesById(COMMUNITY_ID, pageable);
    verify(communityApiMapper).communityHouseSetToRestApiResponseCommunityHouseSet(housesSet);
  }

  /**
   * verifies that when a community with the given ID does not exist, the `listCommunityHouses`
   * endpoint returns a `HttpStatus.NOT_FOUND` status code and an empty response body.
   */
  @Test
  void testListCommunityHousesCommunityNotExistSuccess() {
    // given
    Pageable pageable = PageRequest.of(0, 1);
    given(communityService.findCommunityHousesById(COMMUNITY_ID, pageable))
        .willReturn(Optional.empty());

    // when
    ResponseEntity<GetHouseDetailsResponse> responseEntity =
        communityController.listCommunityHouses(COMMUNITY_ID, pageable);

    // then
    assertEquals(HttpStatus.NOT_FOUND, responseEntity.getStatusCode());
    assertNull(responseEntity.getBody());
    verify(communityService).findCommunityHousesById(COMMUNITY_ID, pageable);
    verifyNoInteractions(communityApiMapper);
  }

  /**
   * verifies that the `AddCommunityHouse` endpoint adds houses to a community successfully.
   * It uses mock objects and assertions to check the response status code, body content,
   * and calls to the `communityApiMapper` and `communityService` classes.
   */
  @Test
  void shouldAddCommunityHouseSuccessfully() {
    // given
    AddCommunityHouseRequest addCommunityHouseRequest = new AddCommunityHouseRequest();
    Community community = createTestCommunity();
    Set<CommunityHouse> communityHouses = community.getHouses();
    Set<CommunityHouseName> communityHouseNames = new HashSet<>();
    communityHouseNames.add(new CommunityHouseName().name(COMMUNITY_HOUSE_NAME));

    Set<String> houseIds = new HashSet<>();
    for (CommunityHouse house : communityHouses) {
      houseIds.add(house.getHouseId());
    }

    addCommunityHouseRequest.getHouses().addAll(communityHouseNames);

    AddCommunityHouseResponse response = new AddCommunityHouseResponse().houses(houseIds);

    given(communityApiMapper.communityHouseNamesSetToCommunityHouseSet(communityHouseNames))
        .willReturn(communityHouses);
    given(communityService.addHousesToCommunity(COMMUNITY_ID, communityHouses))
        .willReturn(houseIds);

    // when
    ResponseEntity<AddCommunityHouseResponse> responseEntity =
        communityController.addCommunityHouses(COMMUNITY_ID, addCommunityHouseRequest);

    // then
    assertEquals(HttpStatus.CREATED, responseEntity.getStatusCode());
    assertEquals(response, responseEntity.getBody());
    verify(communityApiMapper).communityHouseNamesSetToCommunityHouseSet(communityHouseNames);
    verify(communityService).addHousesToCommunity(COMMUNITY_ID, communityHouses);
  }

  /**
   * verifies that if an empty `AddCommunityHouseRequest` is passed to the `addCommunityHouses`
   * method, it should return a `ResponseEntity` with a `HttpStatus.BAD_REQUEST` status
   * code and no body.
   */
  @Test
  void shouldThrowBadRequestWithEmptyAddHouseRequest() {
    // given
    AddCommunityHouseRequest emptyRequest = new AddCommunityHouseRequest();

    given(communityApiMapper.communityHouseNamesSetToCommunityHouseSet(emptyRequest.getHouses()))
        .willReturn(new HashSet<>());
    given(communityService.addHousesToCommunity(COMMUNITY_ID, new HashSet<>()))
        .willReturn(new HashSet<>());

    // when
    ResponseEntity<AddCommunityHouseResponse> responseEntity =
        communityController.addCommunityHouses(COMMUNITY_ID, emptyRequest);

    // then
    assertEquals(HttpStatus.BAD_REQUEST, responseEntity.getStatusCode());
    assertNull(responseEntity.getBody());
    verify(communityApiMapper).communityHouseNamesSetToCommunityHouseSet(new HashSet<>());
    verify(communityService).addHousesToCommunity(COMMUNITY_ID, new HashSet<>());
  }

  /**
   * verifies that the `removeCommunityHouse` endpoint removes a house from a community
   * successfully by asserting the status code of the response entity and verifying the
   * calls to the `communityService`.
   */
  @Test
  void shouldRemoveCommunityHouseSuccessfully() {
    // given
    Community community = createTestCommunity();

    given(communityService.getCommunityDetailsById(COMMUNITY_ID))
        .willReturn(Optional.of(community));
    given(communityService.removeHouseFromCommunityByHouseId(createTestCommunity(),
        COMMUNITY_HOUSE_ID))
        .willReturn(true);

    // when
    ResponseEntity<Void> responseEntity =
        communityController.removeCommunityHouse(COMMUNITY_ID, COMMUNITY_HOUSE_ID);

    // then
    assertEquals(HttpStatus.NO_CONTENT, responseEntity.getStatusCode());
    verify(communityService).removeHouseFromCommunityByHouseId(community, COMMUNITY_HOUSE_ID);
    verify(communityService).getCommunityDetailsById(COMMUNITY_ID);
  }

  /**
   * tests whether removing a community house with a non-existent ID returns a
   * `HttpStatus.NOT_FOUND` response and verifies that the corresponding method call
   * to remove the house from the community is not made.
   */
  @Test
  void shouldNotRemoveCommunityHouseIfNotFoundSuccessfully() {
    // given
    Community community = createTestCommunity();

    given(communityService.getCommunityDetailsById(COMMUNITY_ID))
        .willReturn(Optional.of(community));
    given(communityService.removeHouseFromCommunityByHouseId(community, COMMUNITY_HOUSE_ID))
        .willReturn(false);

    // when
    ResponseEntity<Void> responseEntity =
        communityController.removeCommunityHouse(COMMUNITY_ID, COMMUNITY_HOUSE_ID);

    // then
    assertEquals(HttpStatus.NOT_FOUND, responseEntity.getStatusCode());
    verify(communityService).removeHouseFromCommunityByHouseId(community, COMMUNITY_HOUSE_ID);
  }

  /**
   * verifies that removing a community house by its ID fails when the community
   * associated with it cannot be found.
   */
  @Test
  void shouldNotRemoveCommunityHouseIfCommunityNotFound() {
    //given
    Community community = createTestCommunity();

    given(communityService.getCommunityDetailsById(COMMUNITY_ID))
        .willReturn(Optional.empty());

    // when
    ResponseEntity<Void> responseEntity =
        communityController.removeCommunityHouse(COMMUNITY_ID, COMMUNITY_HOUSE_ID);

    // then
    assertEquals(HttpStatus.NOT_FOUND, responseEntity.getStatusCode());
    verify(communityService).getCommunityDetailsById(COMMUNITY_ID);
    verify(communityService, never()).removeHouseFromCommunityByHouseId(community,
        COMMUNITY_HOUSE_ID);
  }

  /**
   * verifies that removing an admin from a community returns a `HttpStatus.NO_CONTENT`
   * response and invokes the `removeAdminFromCommunity` method of the `communityService`.
   */
  @Test
  void shouldRemoveAdminFromCommunitySuccessfully() {
    // given
    given(communityService.removeAdminFromCommunity(COMMUNITY_ID, COMMUNITY_ADMIN_ID))
        .willReturn(true);

    // when
    ResponseEntity<Void> responseEntity =
        communityController.removeAdminFromCommunity(COMMUNITY_ID, COMMUNITY_ADMIN_ID);

    // then
    assertEquals(HttpStatus.NO_CONTENT, responseEntity.getStatusCode());
    verify(communityService).removeAdminFromCommunity(COMMUNITY_ID, COMMUNITY_ADMIN_ID);
  }

  /**
   * tests that the `removeAdminFromCommunity` method returns a `HttpStatus.NOT_FOUND`
   * response when the admin to be removed is not found in the community.
   */
  @Test
  void shouldNotRemoveAdminIfNotFoundSuccessfully() {
    // given
    given(communityService.removeAdminFromCommunity(COMMUNITY_ID, COMMUNITY_ADMIN_ID))
        .willReturn(false);

    // when
    ResponseEntity<Void> responseEntity =
        communityController.removeAdminFromCommunity(COMMUNITY_ID, COMMUNITY_ADMIN_ID);

    // then
    assertEquals(HttpStatus.NOT_FOUND, responseEntity.getStatusCode());
    verify(communityService).removeAdminFromCommunity(COMMUNITY_ID, COMMUNITY_ADMIN_ID);
  }

  /**
   * tests whether deleting a community returns a NO_CONTENT status code and verifies
   * that the community service was called with the correct ID.
   */
  @Test
  void shouldDeleteCommunitySuccessfully() {
    // given
    given(communityService.deleteCommunity(COMMUNITY_ID))
        .willReturn(true);

    // when
    ResponseEntity<Void> responseEntity =
        communityController.deleteCommunity(COMMUNITY_ID);

    // then
    assertEquals(HttpStatus.NO_CONTENT, responseEntity.getStatusCode());
    verify(communityService).deleteCommunity(COMMUNITY_ID);
  }

  /**
   * verifies that deleting a community with a non-existent ID results in a `NOT_FOUND`
   * status code and triggers the expected method calls on the `communityService`.
   */
  @Test
  void shouldNotDeleteCommunityNotFoundSuccessfully() {
    // given
    given(communityService.deleteCommunity(COMMUNITY_ID))
        .willReturn(false);

    // when
    ResponseEntity<Void> responseEntity =
        communityController.deleteCommunity(COMMUNITY_ID);

    // then
    assertEquals(HttpStatus.NOT_FOUND, responseEntity.getStatusCode());
    verify(communityService).deleteCommunity(COMMUNITY_ID);
  }

  /**
   * creates a mock instance of `CommunityHouse`, setting its name, ID, and member list
   * to empty sets. It returns the mock instance.
   * 
   * @returns a mock instance of the `CommunityHouse` class.
   */
  private CommunityHouse getMockCommunityHouse() {
    CommunityHouse communityHouse = new CommunityHouse();
    communityHouse.setName(COMMUNITY_HOUSE_NAME);
    communityHouse.setHouseId(COMMUNITY_HOUSE_ID);
    communityHouse.setHouseMembers(new HashSet<>());

    return communityHouse;
  }

  /**
   * creates a new instance of the `Community` class, setting its admins, districts,
   * and email addresses. It then adds the admin user to the community and sets the
   * community house. The function returns the created community instance.
   * 
   * @param admins set of users who will be administrators for the generated mock community.
   * 
   * @returns a mock Community object representing a community with admins, houses, and
   * other attributes.
   */
  private Community getMockCommunity(Set<User> admins) {
    Community community =
        new Community(admins, new HashSet<>(), COMMUNITY_NAME, COMMUNITY_ID,
            COMMUNITY_DISTRICT, new HashSet<>());
    User admin = new User(COMMUNITY_ADMIN_NAME, COMMUNITY_ADMIN_ID, COMMUNITY_ADMIN_EMAIL, true,
        COMMUNITY_ADMIN_PASSWORD, new HashSet<>(), new HashSet<>());
    community.getAdmins().add(admin);
    admin.getCommunities().add(community);

    CommunityHouse communityHouse = getMockCommunityHouse();
    communityHouse.setCommunity(community);
    community.getHouses().add(communityHouse);

    return community;
  }
}
