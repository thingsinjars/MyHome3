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
   * retrieves amenity details from the database using `amenitySDJpaService`. The method
   * maps the retrieved data to a `GetAmenityDetailsResponse` object using `amenityApiMapper`,
   * and returns an `OK` response entity if successful.
   * 
   * @param amenityId identifier of the amenity for which details are requested.
   * 
   * 	- `amenitySDJpaService`: This is an instance of `AmenitySDJpaService`, which is
   * a Java class that provides methods for interacting with the amenity data in the system.
   * 	- `getAmenityDetails()`: This method is part of the `AmenitySDJpaService` interface
   * and returns an instance of `GetAmenityDetailsResponse`.
   * 	- `@PathVariable`: This annotation indicates that the `amenityId` parameter is
   * passed through the URL path, which means it is obtained from the HTTP request.
   * 	- `amenityId`: This variable represents the unique identifier for the amenity
   * being queried. Its properties are not explicitly mentioned in the code snippet provided.
   * 
   * @returns an `ResponseEntity` object containing the amenity details as a `GetAmenityDetailsResponse`.
   * 
   * 	- `ResponseEntity<GetAmenityDetailsResponse>`: This is a class that represents
   * an entity with a response status and a body containing the amenity details.
   * 	- `status()`: This is a method that returns the HTTP status code of the response,
   * which can be either `OK` or `NOT_FOUND`.
   * 	- `build()`: This is a method that builds a new `ResponseEntity` object with the
   * specified status and body.
   * 	- `map()`: These are methods that take a function as an argument and apply it to
   * the output of the `getAmenityDetails` function. The functions are used to map the
   * original output to a new response entity with the `amenityToAmenityDetailsResponse`
   * method, which is responsible for transforming the original amenity object into an
   * `AmenityDetailsResponse` object.
   * 	- `orElse()`: This is a method that returns the default response entity if the
   * result of the `map()` methods is `null`. The default response entity has a status
   * code of `NOT_FOUND`.
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
   * retrieves a set of amenities from the database using `JPA`, and maps them to a set
   * of `GetAmenityDetailsResponse` objects using `Mapper`. The resulting set of
   * `GetAmenityDetailsResponse` objects is then returned in an `Ok` response entity.
   * 
   * @param communityId identifier of the community whose amenities will be listed.
   * 
   * 	- `communityId`: This is a String variable representing the community ID. It is
   * passed as a Path Variable to the function.
   * 
   * The `listAllAmenities` function retrieves all amenities associated with the given
   * community ID using the `amenitySDJpaService`. The retrieved amenities are then
   * converted into a Set of `GetAmenityDetailsResponse` objects using the `amenityApiMapper`.
   * Finally, an `ResponseEntity` is created with the converted response set and returned.
   * 
   * @returns a set of `GetAmenityDetailsResponse` objects containing the details of
   * all amenities for a given community.
   * 
   * 	- `ResponseEntity`: This is the generic type of the response entity, which indicates
   * that it contains a set of `GetAmenityDetailsResponse` objects.
   * 	- `Set<GetAmenityDetailsResponse>`: This is the actual set of `GetAmenityDetailsResponse`
   * objects contained within the response entity.
   * 	- `amenitiesSetToAmenityDetailsResponseSet()`: This is a method that takes a set
   * of `Amenity` objects and returns a set of `GetAmenityDetailsResponse` objects,
   * each containing details about a single amenity.
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
   * `amenitySDJpaService` and returning an `AddAmenityResponse` object with the added
   * amenities.
   * 
   * @param communityId identifier of the community to which the amenities will be added.
   * 
   * 	- `communityId`: A string representing the unique identifier for a community.
   * 	- `request`: An object containing the amenities to be added to the community.
   * 
   * The function first calls the `createAmenities` method of the `amenitySDJpaService`,
   * passing in the amenities and the community ID as parameters. This method creates
   * a list of amenities in the database. Then, it maps the list of amenities to an
   * instance of `AddAmenityResponse`. Finally, it returns a `ResponseEntity` with a
   * status code of `ok` or `notFound`, depending on whether the amenities were
   * successfully added to the community.
   * 
   * @param request AddAmenityRequest object containing the amenities to be added to
   * the community, which is used by the `amenitySDJpaService` to create the new amenities
   * in the database.
   * 
   * 	- `communityId`: A string representing the ID of the community to which amenities
   * will be added.
   * 	- `request.getAmenities()`: An array of `AddAmenityRequest.Amenity` objects,
   * representing the amenities to be added to the community.
   * 
   * @returns a `ResponseEntity` object representing either a successful addition of
   * amenities to the community or an error message indicating that the community does
   * not exist.
   * 
   * 	- `ResponseEntity<AddAmenityResponse>`: This is an entity object that contains
   * the response to the add amenities request. It has a `body` field that contains an
   * instance of `AddAmenityResponse`.
   * 	- `AddAmenityResponse`: This is an object that contains information about the
   * added amenities. It has a `amenities` field that contains a list of `Amenity`
   * objects representing the added amenities.
   * 	- `ok`: This is a boolean value indicating whether the add amenities request was
   * successful or not. If the request was successful, this field will be set to `true`,
   * otherwise it will be set to `false`.
   * 	- `notFound`: This is an error message that indicates that the community with the
   * provided `communityId` could not be found.
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
   * indicating the outcome of the operation.
   * 
   * @param amenityId ID of an amenity to be deleted.
   * 
   * 	- `amenityId`: A string representing the unique identifier for an amenity in the
   * system.
   * 
   * The function checks whether the amenity with the provided `amenityId` exists in
   * the database and deletes it if found. If successful, a `HttpStatus.NO_CONTENT`
   * response is returned, indicating that the amenity has been deleted successfully.
   * Otherwise, a `HttpStatus.NOT_FOUND` response is returned, indicating that the
   * amenity could not be found in the database.
   * 
   * @returns a HTTP `NO_CONTENT` status code indicating the amenity was successfully
   * deleted.
   * 
   * 	- `HttpStatus.NO_CONTENT`: The HTTP status code indicating that the amenity was
   * successfully deleted.
   * 	- `HttpStatus.NOT_FOUND`: The HTTP status code indicating that the specified
   * amenity could not be found in the database.
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
   * updates an amenity in the system by passing the amenity ID and update request to
   * the API, then checking if the update was successful or not and returning the
   * appropriate response.
   * 
   * @param amenityId ID of the amenity being updated.
   * 
   * 	- `amenityId`: A String representing the unique identifier for an amenity.
   * 
   * @param request UpdateAmenityRequest object containing the details of the amenity
   * to be updated, which is then converted into an AmenityDto object through the use
   * of the amenityApiMapper method.
   * 
   * 	- `@Valid`: Indicates that the request body must be validated by the API.
   * 	- `@RequestBody`: Marks the request body as a serializable Java object.
   * 	- `UpdateAmenityRequest`: The class that defines the structure of the request body.
   * 
   * @returns a `ResponseEntity` object with a status code of either `NO_CONTENT` or
   * `NOT_FOUND`, depending on whether the amenity was updated successfully.
   * 
   * 	- `HttpStatus`: This is an instance of the `HttpStatus` class, which represents
   * the HTTP status code of the response. The value of this field indicates whether
   * the request was successful (200-level status codes) or not (400-level status codes).
   * 	- `ResponseEntity`: This is a class that holds the HTTP response entity, which
   * includes the status code, headers, and body. In this case, the body is an instance
   * of the `Void` type, indicating that there is no response data to return.
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
