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
 * is responsible for handling HTTP requests related to amenities in a community. The
 * controller provides endpoints for listing all amenities, adding new amenities,
 * updating existing amenities, and deleting amenities. The controller utilizes the
 * JPA service to interact with the database and perform CRUD operations on amenities.
 * The controller also provides methods for handling error responses and returning
 * appropriate status codes.
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
   * retrieves amenity details from the database using the `amenitySDJpaService`. It
   * then maps the result to an `AmenityDetailsResponse` object using the `amenityApiMapper`
   * and returns a `ResponseEntity` with a status of `OK` or an error message if the
   * amenity ID is not found.
   * 
   * @param amenityId identifier of an amenity that is being requested, and it is used
   * to retrieve the details of that amenity from the database by the `amenitySDJpaService`.
   * 
   * @returns an `OkResponseEntity` containing the details of the amenity with the
   * provided ID.
   * 
   * 	- `ResponseEntity`: This is an instance of `ResponseEntity`, which represents a
   * response to a REST API call. It contains information about the status code and
   * body of the response.
   * 	- `ok`: This is a method on `ResponseEntity` that indicates whether the response
   * was successful (i.e., with a 200 status code). If `orElse` is called with a non-empty
   * `Optional`, this property will be set to `true`. Otherwise, it will be set to `false`.
   * 	- `status`: This is an instance of `HttpStatus`, which represents the status code
   * of the response. In this case, it will be either `HttpStatus.NOT_FOUND` if the
   * amenity could not be found, or `HttpStatus.OK` otherwise.
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
   * list all amenities for a given community ID and map them to `GetAmenityDetailsResponse`
   * objects for return in a `ResponseEntity`.
   * 
   * @param communityId unique identifier of the community whose amenities are to be listed.
   * 
   * @returns a `ResponseEntity` object containing a set of `GetAmenityDetailsResponse`
   * objects, representing the list of amenities for the specified community.
   * 
   * 	- `Set<GetAmenityDetailsResponse>` represents a set of `GetAmenityDetailsResponse`
   * objects, which contain details of each amenity.
   * 	- `amenitySDJpaService.listAllAmenities(communityId)` returns a set of `Amenity`
   * objects, which are the entities being transformed into `GetAmenityDetailsResponse`
   * objects.
   * 	- `amenityApiMapper.amenitiesSetToAmenityDetailsResponseSet(amenities)` maps each
   * `Amenity` object to a `GetAmenityDetailsResponse` object, using a custom mapping
   * logic.
   * 
   * Therefore, the output of the `listAllAmenities` function is a set of
   * `GetAmenityDetailsResponse` objects, each containing details of an amenity.
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
   * adds amenities to a community through JPA service, maps the result to AddAmenityResponse
   * object and returns ResponseEntity with ok status or notFound status if error occurs.
   * 
   * @param communityId ID of the community to which the amenities are being added.
   * 
   * @param request AddAmenityRequest object containing the amenities to be added to a
   * community, which is used by the `amenitySDJpaService` to create new amenities in
   * the community.
   * 
   * 	- `communityId`: The ID of the community to which the amenities will be added.
   * 	- `request.getAmenities()`: An array of `AddAmenityRequest.Amenity` objects
   * representing the amenities to be added to the community. Each object in the array
   * contains properties such as `name`, `description`, and `type`.
   * 
   * @returns a `ResponseEntity` object with an `ok` status and a list of created amenities.
   * 
   * 	- `ResponseEntity<AddAmenityResponse>`: This is an entity that contains a
   * `AddAmenityResponse` object and an `HttpStatusCode`. The `AddAmenityResponse`
   * object represents the result of adding amenities to a community, and it has several
   * attributes, including `amenities`, which is a list of added amenities.
   * 	- `map(function)`: This method is used to map the output of the `createAmenities()`
   * method, which returns a list of amenities, to an `AddAmenityResponse` object. The
   * `map()` method takes a function as its argument, which in this case is a lambda
   * expression that extracts the `amenities` list from the returned list of amenities
   * and returns it as a `AddAmenityResponse` object.
   * 	- `orElse(function)`: This method is used to provide an alternative output if the
   * `createAmenities()` method returns an empty list. The `orElse()` method takes a
   * function as its argument, which in this case is a lambda expression that creates
   * a `ResponseEntity` with an `HttpStatusCode` of `404` and builds it using the
   * `build()` method.
   * 	- `map(function)`: This method is used to map the output of the `orElse()` method
   * to an `AddAmenityResponse` object. The `map()` method takes a function as its
   * argument, which in this case is a lambda expression that extracts the `HttpStatusCode`
   * and the `AddAmenityResponse` object from the returned `ResponseEntity` and returns
   * them as a single output.
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
   * indicating the result of the operation.
   * 
   * @param amenityId ID of the amenity to be deleted.
   * 
   * @returns a `ResponseEntity` object with a status code of either `NO_CONTENT` or
   * `NOT_FOUND`, depending on whether the amenity was successfully deleted or not.
   * 
   * 	- `HttpStatus.NO_CONTENT`: This status code indicates that the amenity was
   * successfully deleted and no content was returned in response.
   * 	- `HttpStatus.NOT_FOUND`: This status code indicates that the amenity could not
   * be found, and the function returned an error message.
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
   * updates an amenity in the system by receiving a request with the amenity details
   * and updating the amenity information in the database using the `amenitySDJpaService`.
   * If the update is successful, it returns a `ResponseEntity` with a `HttpStatus.NO_CONTENT`.
   * 
   * @param amenityId unique identifier of the amenity being updated.
   * 
   * @param request UpdateAmenityRequest object that contains the details of the amenity
   * to be updated.
   * 
   * 	- `@Valid`: Indicates that the input request body must contain valid data according
   * to the schema defined in the JPA annotation.
   * 	- `@RequestBody`: Marks the request body as a serialized object containing the
   * update amenity details.
   * 	- `UpdateAmenityRequest` is the class that contains the attributes of the amenity
   * to be updated, along with any additional information required for the update operation.
   * 
   * @returns a `ResponseEntity` object with a status code indicating whether the update
   * was successful or not.
   * 
   * 	- `HttpStatus.NO_CONTENT`: This indicates that the amenity was successfully
   * updated, and no additional content was returned in the response body.
   * 	- `HttpStatus.NOT_FOUND`: This indicates that the amenity could not be found, and
   * the function returned an error message.
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
