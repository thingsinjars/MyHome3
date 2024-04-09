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
 * TODO
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
   * tests the `addAmenityToCommunity` method of an amenity controller by providing a
   * new amenity to be added to an existing community, and verifying that the method
   * returns a successful response with the added amenity.
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
   * checks that an amenity is not added to a community when the community does not exist.
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
   * retrieves an amenity's details from the database and API, compares them to the
   * expected response body, and verifies the HTTP status code and calls to the database
   * and API methods.
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
   * verifies that the `amenitySDJpaService.getAmenityDetails()` method returns an empty
   * optional when the amenity with the given ID does not exist in the database.
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
   * tests whether deleting an amenity using the `amenityController` and `amenitySDJpaService`
   * succeeds, returns null, has the expected HTTP status code, and calls the `deleteAmenity`
   * method of the `amenitySDJpaService`.
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
   * tests whether an amenity with a non-existent ID can be deleted using the
   * `amenityController`. It verifies that the method returns a `ResponseEntity` with
   * a `null` body and a `HttpStatus.NOT_FOUND` status code, and also verifies that the
   * `amenitySDJpaService` method is called with the non-existent ID.
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
   * tests the update amenity endpoint by providing a test amenity DTO, updating it
   * using the API and JPA services, and verifying the response status code and the
   * execution of the API and JPA methods.
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
   * tests whether an attempt to update an amenity that does not exist results in a
   * `HttpStatus.NOT_FOUND` response. It uses mocking and verification to check the
   * behavior of the `amenityController` and `amenityApiMapper`.
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
   * creates a new instance of the `Amenity` class with predefined ID and description
   * for testing purposes.
   * 
   * @returns a new instance of the `Amenity` class with predefined ID and description.
   * 
   * 1/ Amenity ID (withAmenityId): This is an integer that uniquely identifies the
   * amenity within the system.
   * 2/ Description (withDescription): This is a string that provides a brief description
   * of the amenity, which can be used for identification or reference purposes.
   */
  private Amenity getTestAmenity() {
    return new Amenity()
        .withAmenityId(TEST_AMENITY_ID)
        .withDescription(TEST_AMENITY_DESCRIPTION);
  }

  /**
   * creates a new `AmenityDto` instance with test data for an amenity, including its
   * ID, name, description, price, and community ID.
   * 
   * @returns a `AmenityDto` object containing test data for an amenity.
   * 
   * 	- `id`: A long value representing the amenity's unique identifier (1L).
   * 	- `amenityId`: An integer value indicating the amenity's ID (TEST_AMENITY_ID).
   * 	- `name`: A string value representing the amenity's name (TEST_AMENITY_NAME).
   * 	- `description`: A string value providing a brief description of the amenity (TEST_AMENITY_DESCRIPTION).
   * 	- `price`: A double value representing the price of the amenity (TEST_AMENITY_PRICE).
   * 	- `communityId`: An integer value indicating the community ID associated with the
   * amenity (TEST_COMMUNITY_ID).
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
   * creates a new `UpdateAmenityRequest` object with specified name, description, price
   * and community ID.
   * 
   * @returns an `UpdateAmenityRequest` object containing the name, description, price,
   * and community ID of a fictitious amenity.
   * 
   * 	- name: A string variable representing the name of the amenity to be updated.
   * 	- description: A string variable representing the description of the amenity to
   * be updated.
   * 	- price: An integer variable representing the price of the amenity to be updated.
   * 	- communityId: An integer variable representing the ID of the community where the
   * amenity is located.
   */
  private UpdateAmenityRequest getUpdateAmenityRequest() {
    return new UpdateAmenityRequest()
        .name(TEST_AMENITY_NAME)
        .description(TEST_AMENITY_DESCRIPTION)
        .price(1L)
        .communityId(TEST_COMMUNITY_ID);
  }
}