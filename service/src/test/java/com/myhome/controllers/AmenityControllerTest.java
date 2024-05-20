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

import com.myhome.controllers.mapper.AmenityApiMapper;
import com.myhome.domain.Amenity;
import com.myhome.model.AddAmenityRequest;
import com.myhome.model.AddAmenityResponse;
import com.myhome.model.AmenityDto;
import com.myhome.model.GetAmenityDetailsResponse;
import com.myhome.model.UpdateAmenityRequest;
import com.myhome.services.AmenityService;
import java.math.BigDecimal;
import java.util.HashSet;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import static java.util.Collections.singletonList;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;

/**
 * is a test class for the AmenityController class in a Java-based application. The
 * test class creates mock objects and utilizes various methods to test the functionality
 * of the AmenityController. The test class includes three main methods: `testUpdateAmenity`,
 * `testGetAmenity`, and `testDeleteAmenity`. These methods test different scenarios
 * related to updating, getting, and deleting amenities in the application.
 */
class AmenityControllerTest {

  private static final String TEST_AMENITY_NAME = "test-amenity-name";
  private static final BigDecimal TEST_AMENITY_PRICE = BigDecimal.valueOf(1);
  private final String TEST_AMENITY_ID = "test-amenity-id";
  private final String TEST_AMENITY_DESCRIPTION = "test-amenity-description";
  private final String TEST_COMMUNITY_ID = "1";

  @Mock
  private AmenityService amenitySDJpaService;
  @Mock
  private AmenityApiMapper amenityApiMapper;

  @InjectMocks
  private AmenityController amenityController;

  /**
   * tests the `addAmenityToCommunity` method by providing an amenity and a community
   * ID, then asserts that the method returns a response with a status code of `OK`.
   */
  @Test
  void shouldAddAmenityToCommunity() {
    // given
    final String communityId = "communityId";
    final AmenityDto amenityDto =
        new AmenityDto().id(1L)
            .amenityId("amenityId")
            .name("name")
            .description("description")
            .price(BigDecimal.ONE)
            .communityId("");
    final HashSet<AmenityDto> amenities = new HashSet<>(singletonList(amenityDto));
    final AddAmenityRequest request = new AddAmenityRequest().amenities(amenities);
    given(amenitySDJpaService.createAmenities(amenities, communityId))
        .willReturn(Optional.of(singletonList(amenityDto)));

    // when
    final ResponseEntity<AddAmenityResponse> response =
        amenityController.addAmenityToCommunity(communityId, request);

    // then
    assertEquals(HttpStatus.OK, response.getStatusCode());
  }

  /**
   * tests whether adding an amenity to a non-existent community returns a `HttpStatus.NOT_FOUND`.
   */
  @Test
  void shouldNotAddAmenityWhenCommunityNotExists() {
    // given
    final String communityId = "communityId";
    final AmenityDto amenityDto = new AmenityDto();
    final HashSet<AmenityDto> amenities = new HashSet<>(singletonList(amenityDto));
    final AddAmenityRequest request = new AddAmenityRequest().amenities(amenities);
    given(amenitySDJpaService.createAmenities(amenities, communityId))
        .willReturn(Optional.empty());

    // when
    final ResponseEntity<AddAmenityResponse> response =
        amenityController.addAmenityToCommunity(communityId, request);

    // then
    assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
  }

  /**
   * initializes mock objects using MockitoAnnotations.
   */
  @BeforeEach
  private void init() {
    MockitoAnnotations.initMocks(this);
  }

  /**
   * retrieves an amenity's details given its ID, creates mock responses for the service
   * and mapper layers, and verifies their invocation.
   */
  @Test
  void getAmenityDetails() {
    // given
    Amenity testAmenity = getTestAmenity();
    GetAmenityDetailsResponse expectedResponseBody = new GetAmenityDetailsResponse()
        .amenityId(testAmenity.getAmenityId())
        .description(testAmenity.getDescription());

    given(amenitySDJpaService.getAmenityDetails(TEST_AMENITY_ID))
        .willReturn(Optional.of(testAmenity));
    given(amenityApiMapper.amenityToAmenityDetailsResponse(testAmenity))
        .willReturn(expectedResponseBody);

    // when
    ResponseEntity<GetAmenityDetailsResponse> response =
        amenityController.getAmenityDetails(TEST_AMENITY_ID);

    // then
    assertEquals(expectedResponseBody, response.getBody());
    assertEquals(HttpStatus.OK, response.getStatusCode());
    verify(amenitySDJpaService).getAmenityDetails(TEST_AMENITY_ID);
    verify(amenityApiMapper).amenityToAmenityDetailsResponse(testAmenity);
  }

  /**
   * verifies that when an amenity with the given ID does not exist, it returns a
   * `HttpStatus.NOT_FOUND` response and calls the `amenitySDJpaService.getAmenityDetails`
   * method to retrieve the details.
   */
  @Test
  void getAmenityDetailsNotExists() {
    // given
    given(amenitySDJpaService.getAmenityDetails(TEST_AMENITY_ID))
        .willReturn(Optional.empty());

    // when
    ResponseEntity<GetAmenityDetailsResponse> response =
        amenityController.getAmenityDetails(TEST_AMENITY_ID);

    // then
    assertNull(response.getBody());
    assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    verify(amenitySDJpaService).getAmenityDetails(TEST_AMENITY_ID);
    verify(amenityApiMapper, never()).amenityToAmenityDetailsResponse(any());
  }

  /**
   * tests whether deleting an amenity using the `amenityController` results in a
   * successful response with a status code of `HttpStatus.NO_CONTENT` and verifies
   * that the amenity is deleted from the database using `amenitySDJpaService`.
   */
  @Test
  void deleteAmenity() {
    // given
    given(amenitySDJpaService.deleteAmenity(TEST_AMENITY_ID))
        .willReturn(true);

    // when
    ResponseEntity response = amenityController.deleteAmenity(TEST_AMENITY_ID);

    // then
    assertNull(response.getBody());
    assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
    verify(amenitySDJpaService).deleteAmenity(TEST_AMENITY_ID);
  }

  /**
   * tests the scenario where an attempt is made to delete an amenity that does not
   * exist in the database. It verifies that a `HttpStatus.NOT_FOUND` response is
   * returned and also confirms that the `amenitySDJpaService.deleteAmenity()` method
   * is called with the correct argument.
   */
  @Test
  void deleteAmenityNotExists() {
    // given
    given(amenitySDJpaService.deleteAmenity(TEST_AMENITY_ID))
        .willReturn(false);

    // when
    ResponseEntity response = amenityController.deleteAmenity(TEST_AMENITY_ID);

    // then
    assertNull(response.getBody());
    assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    verify(amenitySDJpaService).deleteAmenity(TEST_AMENITY_ID);
  }

  /**
   * tests the update amenity endpoint by providing a valid request and verifying that
   * it returns a successful response with a NO_CONTENT status code, and also verify
   * that the amenity is updated in the database.
   */
  @Test
  void shouldUpdateAmenitySuccessfully() {
    // given
    AmenityDto amenityDto = getTestAmenityDto();
    UpdateAmenityRequest request = getUpdateAmenityRequest();

    given(amenityApiMapper.updateAmenityRequestToAmenityDto(request))
        .willReturn(amenityDto);
    given(amenitySDJpaService.updateAmenity(amenityDto))
        .willReturn(true);

    // when
    ResponseEntity<Void> responseEntity =
        amenityController.updateAmenity(TEST_AMENITY_ID, request);

    // then
    assertEquals(HttpStatus.NO_CONTENT, responseEntity.getStatusCode());
    verify(amenityApiMapper).updateAmenityRequestToAmenityDto(request);
    verify(amenitySDJpaService).updateAmenity(amenityDto);
  }

  /**
   * tests whether an attempt to update an amenity that does not exist will result in
   * a `HttpStatus.NOT_FOUND` response and verifies the appropriate methods were called
   * to handle the update request.
   */
  @Test
  void shouldNotUpdateCommunityAmenityIfAmenityNotExists() {
    // given
    AmenityDto amenityDto = getTestAmenityDto();
    UpdateAmenityRequest request = getUpdateAmenityRequest();

    given(amenityApiMapper.updateAmenityRequestToAmenityDto(request))
        .willReturn(amenityDto);
    given(amenitySDJpaService.updateAmenity(amenityDto))
        .willReturn(false);

    // when
    ResponseEntity<Void> responseEntity =
        amenityController.updateAmenity(TEST_AMENITY_ID, request);

    // then
    assertEquals(HttpStatus.NOT_FOUND, responseEntity.getStatusCode());
    verify(amenityApiMapper).updateAmenityRequestToAmenityDto(request);
    verify(amenitySDJpaService).updateAmenity(amenityDto);
  }

  /**
   * creates a new instance of `Amenity` with a predefined amenity ID and description
   * for testing purposes.
   * 
   * @returns a new instance of the `Amenity` class with pre-defined ID and description.
   * 
   * 	- `amenityId`: A unique identifier for the amenity, set to `TEST_AMENITY_ID`.
   * 	- `description`: A brief description of the amenity, set to `TEST_AMENITY_DESCRIPTION`.
   */
  private Amenity getTestAmenity() {
    return new Amenity()
        .withAmenityId(TEST_AMENITY_ID)
        .withDescription(TEST_AMENITY_DESCRIPTION);
  }

  /**
   * creates a new instance of the `AmenityDto` class with predefined values for an
   * amenity, including its ID, name, description, price, and community ID.
   * 
   * @returns a `AmenityDto` object containing test data for an amenity.
   * 
   * 	- `id`: A long integer value representing the unique identifier for the amenity.
   * 	- `amenityId`: An integer value representing the amenity ID.
   * 	- `name`: A string value representing the name of the amenity.
   * 	- `description`: A string value representing the description of the amenity.
   * 	- `price`: A double value representing the price of the amenity.
   * 	- `communityId`: An integer value representing the community ID associated with
   * the amenity.
   */
  private AmenityDto getTestAmenityDto() {
    return new AmenityDto()
        .id(1L)
        .amenityId(TEST_AMENITY_ID)
        .name(TEST_AMENITY_NAME)
        .description(TEST_AMENITY_DESCRIPTION)
        .price(TEST_AMENITY_PRICE)
        .communityId(TEST_COMMUNITY_ID);
  }

  /**
   * creates an instance of the `UpdateAmenityRequest` class with specified name,
   * description, price, and community ID.
   * 
   * @returns an UpdateAmenityRequest object containing the specified name, description,
   * price, and community ID.
   * 
   * 	- name: A String variable called TEST_AMENITY_NAME that holds the name of the
   * amenity to be updated.
   * 	- description: A String variable called TEST_AMENITY_DESCRIPTION that holds the
   * description of the amenity to be updated.
   * 	- price: An Long variable called 1L that holds the price of the amenity to be updated.
   * 	- communityId: An Integer variable called TEST_COMMUNITY_ID that holds the ID of
   * the community where the amenity is located.
   */
  private UpdateAmenityRequest getUpdateAmenityRequest() {
    return new UpdateAmenityRequest()
        .name(TEST_AMENITY_NAME)
        .description(TEST_AMENITY_DESCRIPTION)
        .price(1L)
        .communityId(TEST_COMMUNITY_ID);
  }
}