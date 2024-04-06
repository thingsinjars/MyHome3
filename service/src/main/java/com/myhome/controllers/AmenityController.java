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

import com.myhome.api.AmenitiesApi;
import com.myhome.controllers.mapper.AmenityApiMapper;
import com.myhome.domain.Amenity;
import com.myhome.model.AddAmenityRequest;
import com.myhome.model.AddAmenityResponse;
import com.myhome.model.AmenityDto;
import com.myhome.model.GetAmenityDetailsResponse;
import com.myhome.model.UpdateAmenityRequest;
import com.myhome.services.AmenityService;
import java.util.Set;
import javax.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

/**
 * TODO
 */
@RestController
@Slf4j
@RequiredArgsConstructor
public class AmenityController implements AmenitiesApi {

  private final AmenityService amenitySDJpaService;
  private final AmenityApiMapper amenityApiMapper;

  /**
   * retrieves amenity details from the database using `amenitySDJpaService`. If the
   * amenity is found, it returns an `ResponseEntity` with a status code of `OK` and
   * the converted `AmenityDetailsResponse`. Otherwise, it returns an `ResponseEntity`
   * with a status code of `NOT_FOUND`.
   * 
   * @param amenityId unique identifier of an amenity that is being requested by the user.
   * 
   * 	- `amenitySDJpaService`: A service for accessing amenity data from a Java Persistence
   * API (JPA) database.
   * 	- `amenityId`: The primary key of an amenity in the JPA database, which identifies
   * a specific amenity record.
   * 	- `amenityApiMapper`: A mapper class that maps the JPA entity to a
   * `GetAmenityDetailsResponse` object for API consumption.
   * 
   * @returns a `ResponseEntity` object representing the amenity details or a
   * `HttpStatus.NOT_FOUND` status code if the amenity cannot be found.
   * 
   * 	- `ResponseEntity`: This is the type of the output return from the function, which
   * indicates whether the operation was successful or not.
   * 	- `<GetAmenityDetailsResponse>`: This is the class that represents the response
   * entity, which contains information about the amenity details.
   * 	- `amenityId`: This is the parameter passed to the function, representing the
   * unique identifier of the amenity for which details are being requested.
   * 	- `amenitySDJpaService`: This is a Java interface that provides methods for
   * interacting with the amenity data store.
   * 	- `amenityApiMapper`: This is a Java class that maps the amenity data from the
   * data store to the response entity.
   * 	- `map(Function<ResponseEntity, GetAmenityDetailsResponse> mapper)`: This line
   * calls the `map` method on the `ResponseEntity` object, passing in a lambda function
   * that maps the `ResponseEntity` object to a `GetAmenityDetailsResponse` object. The
   * `map` method is used to transform the output of the function into the desired
   * response entity format.
   * 	- `orElse(ResponseEntity.status(HttpStatus.NOT_FOUND).build());`: This line
   * provides an alternative outcome for the function, where if the `getAmenityDetails`
   * method fails, it returns a response entity with a status code of `HttpStatus.NOT_FOUND`.
   */
  @Override
  public ResponseEntity<GetAmenityDetailsResponse> getAmenityDetails(
      @PathVariable String amenityId) {
    return amenitySDJpaService.getAmenityDetails(amenityId)
        .map(amenityApiMapper::amenityToAmenityDetailsResponse)
        .map(ResponseEntity::ok)
        .orElse(ResponseEntity.status(HttpStatus.NOT_FOUND).build());
  }

  /**
   * retrieves a set of amenities from the database using `JpaService`, maps them to
   * `GetAmenityDetailsResponse` objects using `ApiMapper`, and returns a response
   * entity with the mapped amenities.
   * 
   * @param communityId ID of the community for which the amenities are to be listed.
   * 
   * 	- `communityId`: This parameter represents a unique identifier for a community.
   * It is a string variable that contains the ID of the community.
   * 
   * The `listAllAmenities` function retrieves all amenities associated with a particular
   * community using the `amenitySDJpaService`. The retrieved amenities are then
   * transformed into a set of `GetAmenityDetailsResponse` objects through the
   * `amenityApiMapper`. Finally, the function returns an `ResponseEntity` object with
   * the converted set of `GetAmenityDetailsResponse` objects.
   * 
   * @returns a set of `GetAmenityDetailsResponse` objects containing information about
   * the amenities for a given community.
   * 
   * 	- `ResponseEntity<Set<GetAmenityDetailsResponse>>`: This is an entity that contains
   * a set of `GetAmenityDetailsResponse` objects in its body.
   * 	- `Set<GetAmenityDetailsResponse>`: This is a set of objects that contain the
   * details of each amenity, such as name, description, and images.
   * 	- `amenitySDJpaService.listAllAmenities(communityId)`: This method returns a set
   * of `Amenity` objects, which are used to populate the `GetAmenityDetailsResponse`
   * objects in the output set.
   * 	- `amenityApiMapper.amenitiesSetToAmenityDetailsResponseSet(amenities)`: This
   * method is responsible for mapping the `Amenity` objects to `GetAmenityDetailsResponse`
   * objects, which are then added to the output set.
   */
  @Override
  public ResponseEntity<Set<GetAmenityDetailsResponse>> listAllAmenities(
      @PathVariable String communityId) {
    Set<Amenity> amenities = amenitySDJpaService.listAllAmenities(communityId);
    Set<GetAmenityDetailsResponse> response =
        amenityApiMapper.amenitiesSetToAmenityDetailsResponseSet(amenities);
    return ResponseEntity.ok(response);
  }

  /**
   * adds amenities to a community by calling the `createAmenities` method of the
   * `amenitySDJpaService` and returning an `AddAmenityResponse` object containing the
   * added amenities.
   * 
   * @param communityId ID of the community to which the amenities will be added.
   * 
   * 	- `communityId`: A string representing the ID of the community to which amenities
   * will be added.
   * 	- `@PathVariable`: An annotation indicating that the value of the `communityId`
   * field is passed from the URL path as a String.
   * 
   * @param request AddAmenityRequest object containing the amenities to be added to
   * the community, which is used by the `amenitySDJpaService` to create the new amenities
   * in the database.
   * 
   * 	- `communityId`: The ID of the community to which the amenities will be added.
   * 	- `request.getAmenities()`: An array of `AddAmenityRequest.Amenity` objects
   * containing the amenities to be added to the community.
   * 	- `request.getAmenities().size()`: The number of amenities in the array.
   * 
   * @returns a `ResponseEntity` object with an `ok` status and a list of created amenities.
   * 
   * 	- `ResponseEntity<AddAmenityResponse>`: This is the type of the output returned
   * by the function, which is an entity containing a `AddAmenityResponse` object.
   * 	- `AddAmenityResponse`: This is a class that represents the response to the add
   * amenity request, containing an array of `Amenity` objects representing the newly
   * created amenities.
   * 	- `amenitySDJpaService.createAmenities(request.getAmenities(), communityId)`:
   * This is a call to the `amenitySDJpaService` method that creates a new amenity for
   * the given community ID using the request's `Amenity` objects.
   * 	- `.map(amenityList -> new AddAmenityResponse().amenities(amenityList))` : This
   * line maps the `amenityList` to a new `AddAmenityResponse` object, setting the
   * `amenities` property to the list of newly created amenities.
   * 	- `.map(ResponseEntity::ok)`: This line maps the result of the previous mapping
   * to an `Ok` response entity, indicating that the add amenity request was successful.
   * 	- `.orElse(ResponseEntity.notFound().build())`: This line provides an alternative
   * outcome in case the add amenity request failed, returning a `NotFound` response
   * entity instead.
   */
  @Override
  public ResponseEntity<AddAmenityResponse> addAmenityToCommunity(
      @PathVariable String communityId,
      @RequestBody AddAmenityRequest request) {
    return amenitySDJpaService.createAmenities(request.getAmenities(), communityId)
        .map(amenityList -> new AddAmenityResponse().amenities(amenityList))
        .map(ResponseEntity::ok)
        .orElse(ResponseEntity.notFound().build());
  }

  /**
   * deletes an amenity from the database based on its ID, returning a HTTP status code
   * indicating whether the operation was successful or not.
   * 
   * @param amenityId identifier of an amenity to be deleted.
   * 
   * 	- `amenityId`: This is a string input parameter that represents the unique
   * identifier for an amenity in the system.
   * 	- `amenitySDJpaService`: This is an instance of `AmenitySDJpaService`, which is
   * a class that provides methods for interacting with the amenity data stored in the
   * database.
   * 
   * @returns a `ResponseEntity` with a status code of either `NO_CONTENT` or `NOT_FOUND`,
   * depending on whether the amenity was successfully deleted.
   * 
   * 	- `HttpStatus.NO_CONTENT`: This status code indicates that the requested resource
   * has been successfully deleted and no content was returned in the response.
   * 	- `HttpStatus.NOT_FOUND`: This status code indicates that the requested amenity
   * could not be found, which means it may have been deleted or it may never have
   * existed in the first place.
   */
  @Override
  public ResponseEntity deleteAmenity(@PathVariable String amenityId) {
    boolean isAmenityDeleted = amenitySDJpaService.deleteAmenity(amenityId);
    if (isAmenityDeleted) {
      return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    } else {
      return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
    }
  }

  /**
   * updates an amenity in the database based on a request body containing the amenity
   * details. If the update is successful, it returns a `ResponseEntity` with a
   * `HttpStatus.NO_CONTENT`. Otherwise, it returns a `ResponseEntity` with a `HttpStatus.NOT_FOUND`.
   * 
   * @param amenityId ID of the amenity being updated.
   * 
   * 	- `amenityId`: The unique identifier for an amenity.
   * 
   * The function updates the amenity information in the database using the
   * `amenitySDJpaService`. If the update is successful, a `ResponseEntity` with a
   * status code of `NO_CONTENT` is returned. Otherwise, a `ResponseEntity` with a
   * status code of `NOT_FOUND` is returned.
   * 
   * @param request `UpdateAmenityRequest` object containing the details of the amenity
   * to be updated, which is then converted into an `AmenityDto` object by the
   * `amenityApiMapper` and used for updating the amenity in the database.
   * 
   * 	- `@Valid`: This annotation indicates that the `request` object is validated by
   * the framework before it is processed further.
   * 	- `@RequestBody`: This annotation specifies that the `request` object should be
   * serialized and sent as the request body in the HTTP request.
   * 	- `UpdateAmenityRequest`: This class represents the request body of the `updateAmenity`
   * function, containing attributes for updating an amenity.
   * 
   * @returns a `ResponseEntity` object with a status code of either `NO_CONTENT` or
   * `NOT_FOUND`, depending on whether the amenity was successfully updated or not.
   * 
   * 	- `HttpStatus`: This is an instance of the `HttpStatus` class, which represents
   * the status code of the response. In this case, it can be either `NO_CONTENT` or `NOT_FOUND`.
   * 	- `ResponseEntity`: This is a class that holds the status code and the body of
   * the response. The body can be either an empty object (`NO_CONTENT`) or a `Void`
   * object (`NOT_FOUNDED`).
   */
  @Override
  public ResponseEntity<Void> updateAmenity(@PathVariable String amenityId,
      @Valid @RequestBody UpdateAmenityRequest request) {
    AmenityDto amenityDto = amenityApiMapper.updateAmenityRequestToAmenityDto(request);
    amenityDto.setAmenityId(amenityId);
    boolean isUpdated = amenitySDJpaService.updateAmenity(amenityDto);
    if (isUpdated) {
      return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    } else {
      return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
    }
  }
}
