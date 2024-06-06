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
 * provides functionality for retrieving and manipulating amenities in a Java-based
 * application. The class includes methods for getting individual amenity details,
 * listing all amenities for a specific community, adding new amenities to a community,
 * deleting an amenity, and updating an existing amenity.
 */
/**
 * handles operations related to amenities in a community, including listing all
 * amenities for a given community ID, adding amenities to a community through JPA
 * service, deleting an amenity based on its ID, updating an amenity in the database,
 * and returning response entities indicating the outcome of each operation. The class
 * uses annotations such as `@PathVariable`, `@RequestBody`, and `@Valid` to specify
 * input parameters and return types, and it calls methods from other classes in the
 * hierarchy, such as `amenitySDJpaService` and `amenityApiMapper`, to perform database
 * operations and data mapping tasks.
 */
@RestController
@Slf4j
@RequiredArgsConstructor
public class AmenityController implements AmenitiesApi {

  private final AmenityService amenitySDJpaService;
  private final AmenityApiMapper amenityApiMapper;

  /**
   * retrieves amenity details for a given ID using JPA service and maps the result to
   * `GetAmenityDetailsResponse` object using API mapper.
   * 
   * @param amenityId identifier of an amenity for which details are requested.
   * 
   * @returns an `ResponseEntity` object containing the details of the specified amenity.
   * 
   * 	- `ResponseEntity<GetAmenityDetailsResponse>`: This is a generic response entity
   * that contains the amenity details in the `getAmenityDetailsResponse` object.
   * 	- `getAmenityDetailsResponse()`: This is a method that maps the amenity details
   * to an `AmenityDetailsResponse` object.
   * 	- `map(amenityApiMapper::amenityToAmenityDetailsResponse)`: This line calls the
   * `map` function again with the `amenityToAmenityDetailsResponse` method as its
   * argument, which converts the amenity details into an `AmenityDetailsResponse` object.
   * 	- `map(ResponseEntity::ok)`: This line calls the `map` function again with the
   * `ok` method as its argument, which sets the status code of the response entity to
   * 200 (OK).
   * 	- `orElse(ResponseEntity.status(HttpStatus.NOT_FOUND).build());`: This line
   * provides an alternative response if the amenity details cannot be retrieved. The
   * status code is set to `HttpStatus.NOT_FOUND` and the response entity is built with
   * the appropriate details.
   */
  /**
   * retrieves amenity details for a given amenity ID from the database and returns
   * them as a `ResponseEntity`.
   * 
   * @param amenityId ID of the amenity for which details are being requested.
   * 
   * @returns an `ResponseEntity` object representing a successful response with the
   * amenity details in the form of `AmenityDetailsResponse`.
   * 
   * * `ResponseEntity`: This is an object that represents a response entity with a
   * status code and a body. The status code indicates whether the request was successful
   * (OK) or not (e.g., NOT_FOUND).
   * * `map`: This method is used to transform the response entity into a more useful
   * data structure. In this case, it maps the response entity to an `AmenityDetailsResponse`
   * object using the `amenityToAmenityDetailsResponse` function.
   * * `orElse`: This method is used to provide an alternative response if the original
   * response is not successful (e.g., NOT_FOUND). It returns a new response entity
   * with a different status code and body.
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
   * returns a set of amenities for a given community ID, maps them to a set of
   * `GetAmenityDetailsResponse`, and returns an `Ok` response entity.
   * 
   * @param communityId ID of the community for which the list of amenities is being retrieved.
   * 
   * @returns a set of `GetAmenityDetailsResponse` objects containing the list of
   * amenities for a given community ID.
   * 
   * 	- `ResponseEntity`: This is the top-level class representing an HTTP response
   * entity, which contains a `body` property that holds the actual response data.
   * 	- `ok`: This is a sub-class of `ResponseEntity` that indicates the response status
   * code is 200 (OK).
   * 	- `Set<GetAmenityDetailsResponse>`: This is the set of `AmenityDetailsResponse`
   * objects returned by the function, which have been transformed from the `Amenity`
   * objects returned by the `listAllAmenities` method.
   * 	- `amenitySDJpaService`: This is a Java interface that provides methods for
   * interacting with the amenity data stored in a SQL database.
   * 	- `amenityApiMapper`: This is an instance of a class that maps `Amenity` objects
   * to `GetAmenityDetailsResponse` objects, which is used to transform the data returned
   * by the `listAllAmenities` method into the desired response format.
   */
  /**
   * lists all amenities associated with a given community ID using JPA and maps them
   * to `GetAmenityDetailsResponse` objects for return in an `ResponseEntity`.
   * 
   * @param communityId identifier of the community for which the list of amenities is
   * being retrieved.
   * 
   * @returns a set of `GetAmenityDetailsResponse` objects containing the list of
   * amenities for a given community ID.
   * 
   * * `ResponseEntity`: This is a class that represents a response entity, which
   * contains an entity body and an HTTP status code.
   * * `ok`: This is a constant that indicates the response status code is 200 (OK).
   * * `Set<GetAmenityDetailsResponse>`: This is a set of `GetAmenityDetailsResponse`
   * objects, which represent the list of amenities for a given community.
   * * `amenitySDJpaService`: This is an instance of `AmenitySDJpaService`, which
   * provides methods for interacting with the amenity data stored in the database.
   * * `amenitiesSetToAmenityDetailsResponseSet`: This is a method that takes a set of
   * `Amenity` objects and returns a set of `GetAmenityDetailsResponse` objects, each
   * containing the details of a single amenity.
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
   * takes a community ID and an AddAmenityRequest, creates new amenities using the JPA
   * service, and returns a ResponseEntity with the created amenities or a NOT_FOUND
   * response if any error occurs.
   * 
   * @param communityId ID of the community to which the amenities will be added.
   * 
   * @param request AddAmenityRequest object containing the amenities to be added to
   * the community, which is used by the method to create the new amenities in the database.
   * 
   * 	- `communityId`: The ID of the community to which the amenities will be added.
   * 	- `request.getAmenities()`: An array of `AddAmenityRequest.Amenity` objects
   * representing the amenities to be added to the community. Each object contains
   * information such as the name, type, and location of the amenity.
   * 
   * @returns a `ResponseEntity` object representing a successful addition of amenities
   * to a community.
   * 
   * 	- `ResponseEntity<AddAmenityResponse>`: This is a generic type that represents
   * an entity with a response message and data. In this case, the response message is
   * an instance of `AddAmenityResponse`, which contains information about the added amenities.
   * 	- `AddAmenityResponse`: This class represents the response message returned by
   * the function, containing a list of `Amenity` objects that were added to the community.
   * 	- `amenityList`: This is a list of `Amenity` objects that were added to the community.
   * 	- `communityId`: This is the ID of the community where the amenities were added.
   * 
   * Overall, the function returns a response entity with an `AddAmenityResponse` message
   * and a list of `Amenity` objects representing the added amenities.
   */
  /**
   * adds amenities to a community, by creating them in the database and returning a
   * response entity with the added amenities or a not found response if an error occurs.
   * 
   * @param communityId ID of the community to which the amenities will be added.
   * 
   * @param request AddAmenityRequest object containing the amenities to be added to
   * the community, which is used by the `amenitySDJpaService` to create the new amenities
   * in the database.
   * 
   * * `communityId`: The ID of the community where amenities will be added.
   * * `requestBody`: The body of the request contains the list of amenities to be
   * added, represented as an array of objects. Each object in the list contains the
   * name and description of the amenity.
   * 
   * @returns a `ResponseEntity` object with a status of `ok` and a `AddAmenityResponse`
   * object containing the created amenities.
   * 
   * * `ResponseEntity<AddAmenityResponse>`: This is an entity object that contains a
   * response message and a list of amenities.
   * * `ok`: This is a boolean property that indicates whether the operation was
   * successful or not. If the operation was successful, this property will be set to
   * true, otherwise it will be set to false.
   * * `notFound`: This is an error message that indicates that the community with the
   * given ID could not be found.
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
   * deletes an amenity based on its ID, returning a response entity with a status code
   * indicating the outcome of the operation.
   * 
   * @param amenityId identifier of an amenity to be deleted.
   * 
   * @returns a HTTP `NO_CONTENT` status code indicating successful deletion of the amenity.
   * 
   * 	- `isAmenityDeleted`: A boolean value indicating whether the amenity was successfully
   * deleted or not. If the amenity was successfully deleted, this property is set to
   * `true`, otherwise it is set to `false`.
   * 	- `HttpStatus`: The HTTP status code of the response entity. In case the amenity
   * was successfully deleted, the status code is set to `NO_CONTENT` (HTTP 204),
   * otherwise it is set to `NOT_FOUND` (HTTP 404).
   */
  /**
   * deletes an amenity from the database based on its ID, returning a HTTP status code
   * indicating whether the operation was successful or not.
   * 
   * @param amenityId ID of an amenity that is being deleted.
   * 
   * @returns a HTTP `NO_CONTENT` status code indicating that the amenity was successfully
   * deleted.
   * 
   * * `HttpStatus.NO_CONTENT`: This status code indicates that the amenity was
   * successfully deleted, and no further content is provided in the response body.
   * * `HttpStatus.NOT_FOUND`: This status code indicates that the amenity with the
   * specified ID could not be found, and the function returned an error message.
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
   * updates an amenity in the database based on a valid request body, and returns a
   * response entity indicating the result of the update operation.
   * 
   * @param amenityId ID of the amenity being updated, which is used to identify the
   * amenity in the database for updating purposes.
   * 
   * @param request UpdateAmenityRequest object containing the updated amenity data,
   * which is converted to an AmenityDto object by the amenityApiMapper and then used
   * to update the amenity in the database by the amenitySDJpaService.
   * 
   * 	- `@Valid`: This annotation is used to indicate that the request body must contain
   * valid data.
   * 	- `@RequestBody`: This annotation specifies that the request body contains the
   * request details.
   * 	- `UpdateAmenityRequest` : This is the class that represents the request body,
   * which contains fields for updating amenity details.
   * 
   * @returns a `ResponseEntity` with a `HttpStatus.NO_CONTENT` status code indicating
   * that the amenity was successfully updated.
   * 
   * 	- `isUpdated`: This boolean variable indicates whether the amenity was updated
   * successfully or not.
   * 	- `HttpStatus`: The HTTP status code associated with the response entity. In this
   * case, it can be either `NO_CONTENT` (204) or `NOT_FOUND` (404).
   */
  /**
   * updates an amenity based on the request received from the client, and returns a
   * response indicating whether the update was successful or not.
   * 
   * @param amenityId ID of the amenity to be updated.
   * 
   * @param request UpdateAmenityRequest object that contains the updated amenity details.
   * 
   * * `@Valid` annotation indicates that the input request body is validated using the
   * `JsonValidation` class.
   * * `@RequestBody` annotation specifies that the input request body should be used
   * directly as the input for the function.
   * * `UpdateAmenityRequest` is the class that represents the input request, which
   * contains attributes for updating an amenity.
   * 
   * @returns a `ResponseEntity` object with a status code of either `NO_CONTENT` or
   * `NOT_FOUND`, depending on whether the update was successful.
   * 
   * * `HttpStatus`: This is an instance of the `HttpStatus` class, which represents
   * the HTTP status code returned by the function. The value of this field is either
   * `NO_CONTENT` or `NOT_FOUND`, depending on whether the update was successful or not.
   * * `ResponseEntity`: This is an instance of the `ResponseEntity` class, which wraps
   * the HTTP response produced by the function. The `ResponseEntity` object contains
   * additional information such as the status code, headers, and body.
   * * `Void`: This is a reference to the void type, which represents the absence of
   * any value. In this case, it is used as the return type of the `updateAmenity`
   * function, indicating that no value is returned after the update operation is completed.
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
