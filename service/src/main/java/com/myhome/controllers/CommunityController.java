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

import com.myhome.api.CommunitiesApi;
import com.myhome.controllers.dto.CommunityDto;
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
import com.myhome.model.ListCommunityAdminsResponse;
import com.myhome.services.CommunityService;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;
import javax.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

/**
 * REST Controller which provides endpoints for managing community
 */
/**
 * TODO
 */
@RequiredArgsConstructor
@RestController
@Slf4j
public class CommunityController implements CommunitiesApi {
  private final CommunityService communityService;
  private final CommunityApiMapper communityApiMapper;

  /**
   * receives a `CreateCommunityRequest` from the client and creates a new community
   * using the provided information. It then mappers the created community into a
   * `CreateCommunityResponse` and returns it as a response entity with a status code
   * of `CREATED`.
   * 
   * @param request CreateCommunityRequest object that contains the details of the
   * community to be created, which is used by the method to create the community and
   * return the response.
   * 
   * 	- `@Valid` - The input is validated using bean validation.
   * 	- `@RequestBody` - The input is passed as a JSON object in the request body.
   * 	- `CreateCommunityRequest request` - The input represents a create community
   * request, containing attributes such as name, description, and tags.
   * 
   * @returns a `CreateCommunityResponse` object containing the created community details.
   * 
   * 	- `CreateCommunityResponse`: This class represents the response to the create
   * community request, containing information about the created community.
   * 	- `communityId`: A unique identifier for the created community.
   * 	- `name`: The name of the created community.
   * 	- `description`: A brief description of the created community.
   * 	- `members`: A list of members in the created community.
   * 	- `owners`: A list of owners in the created community.
   * 
   * The function returns a `ResponseEntity` with a status code of `CREATED` and the
   * `CreateCommunityResponse` object as its body.
   */
  @Override
  public ResponseEntity<CreateCommunityResponse> createCommunity(@Valid @RequestBody
      CreateCommunityRequest request) {
    log.trace("Received create community request");
    CommunityDto requestCommunityDto =
        communityApiMapper.createCommunityRequestToCommunityDto(request);
    Community createdCommunity = communityService.createCommunity(requestCommunityDto);
    CreateCommunityResponse createdCommunityResponse =
        communityApiMapper.communityToCreateCommunityResponse(createdCommunity);
    return ResponseEntity.status(HttpStatus.CREATED).body(createdCommunityResponse);
  }

  /**
   * receives a `Pageable` object and lists all communities from the service, maps them
   * to a REST API response, and returns it in a `ResponseEntity` with an HTTP status
   * code of OK and the response body containing the listed communities.
   * 
   * @param pageable page request parameters, such as the number of communities to
   * display per page, and enables the function to retrieve the requested number of
   * communities from the database.
   * 
   * 	- `@PageableDefault(size = 200)` - This annotation sets the default page size to
   * 200.
   * 	- `Pageable` - This interface defines the methods for navigating through a page
   * of results, such as `getPageNumber()` and `getPageSize()`.
   * 
   * @returns a list of community details in REST API format.
   * 
   * 	- `GetCommunityDetailsResponse`: This class represents the response to the list
   * community request. It has a field `getCommunities()` containing a set of
   * `GetCommunityDetailsResponseCommunity` objects.
   * 	- `GetCommunityDetailsResponseCommunity`: This class represents an individual
   * community detail in the response. It has fields for the community ID, name, and description.
   */
  @Override
  public ResponseEntity<GetCommunityDetailsResponse> listAllCommunity(
      @PageableDefault(size = 200) Pageable pageable) {
    log.trace("Received request to list all community");

    Set<Community> communityDetails = communityService.listAll(pageable);
    Set<GetCommunityDetailsResponseCommunity> communityDetailsResponse =
        communityApiMapper.communitySetToRestApiResponseCommunitySet(communityDetails);

    GetCommunityDetailsResponse response = new GetCommunityDetailsResponse();
    response.getCommunities().addAll(communityDetailsResponse);

    return ResponseEntity.status(HttpStatus.OK).body(response);
  }

  /**
   * retrieves community details for a given ID using the `communityService` and maps
   * the response to a `GetCommunityDetailsResponse`.
   * 
   * @param communityId identifier of the community for which details are requested.
   * 
   * 	- `log.trace("Received request to get details about community with id[{}],",
   * communityId)`: This line logs a trace message indicating that a request has been
   * received to retrieve details about a community with a specific ID. The ID is
   * included in the message as a parameter.
   * 	- `@PathVariable String communityId`: This annotation indicates that the `communityId`
   * variable is passed in from the URL path and is of type `String`.
   * 	- `communityService.getCommunityDetailsById(communityId)`: This line calls the
   * `getCommunityDetailsById` method of the `communityService` class, passing in the
   * `communityId` as a parameter. This method retrieves the details of a community
   * with the provided ID.
   * 	- `map(Function<T, ResponseEntity> mappingFunction)`: This line applies a mapping
   * function to the result of the `getCommunityDetailsById` method. The function maps
   * the resulting `Community` object to a `GetCommunityDetailsResponse` object.
   * 	- `new GetCommunityDetailsResponse().communities(communities)`: This line creates
   * a new instance of the `GetCommunityDetailsResponse` class and sets its `communities`
   * field to the list of communities retrieved from the database.
   * 	- `map(Function<T, ResponseEntity> mappingFunction)`: This line applies another
   * mapping function to the result of the previous mapping function. The function maps
   * the resulting `ResponseEntity` object to an instance of `ResponseEntity` with a
   * status code of `OK`.
   * 
   * Therefore, the `listCommunityDetails` function retrieves the details of a community
   * with a specific ID and returns a `GetCommunityDetailsResponse` object containing
   * the list of communities retrieved from the database.
   * 
   * @returns a `ResponseEntity` object representing a successful response with a list
   * of communities.
   * 
   * 	- `ResponseEntity<GetCommunityDetailsResponse>`: This is the type of the returned
   * response entity, which contains a list of community details in the `communities`
   * field.
   * 	- `GetCommunityDetailsResponse`: This class represents the response to the request
   * for community details. It has a single field called `communities`, which is a list
   * of community details.
   * 	- `communities(List<Community>)': This method converts the list of community
   * details returned by the service into a list of `GetCommunityDetailsResponse`
   * objects, which are then returned in the response entity.
   * 	- `map(Function<ResponseEntity<GetCommunityDetailsResponse>,
   * ResponseEntity<GetCommunityDetailsResponse>> mapper)`: This line uses a lambda
   * function to map the original response entity to a new one with the same type but
   * with additional fields added. In this case, the lambda function takes the original
   * response entity and returns a new one with the `communities` field set to a list
   * of community details.
   * 	- `map(Function<ResponseEntity<GetCommunityDetailsResponse>,
   * ResponseEntity<GetCommunityDetailsResponse>> mapper)`: This line is similar to the
   * previous one, but it maps the original response entity to a new one with the same
   * type but with different fields removed. In this case, the lambda function takes
   * the original response entity and returns a new one with the `communities` field removed.
   * 	- `map(Function<ResponseEntity<GetCommunityDetailsResponse>,
   * ResponseEntity<GetCommunityDetailsResponse>> mapper)`: This line is similar to the
   * previous two, but it maps the original response entity to a new one with a different
   * type. In this case, the lambda function takes the original response entity and
   * returns a new one with the `communities` field set to a list of `Community` objects
   * instead of a list of `GetCommunityDetailsResponse` objects.
   * 	- `orElseGet(() -> ResponseEntity.notFound().build())`: This line provides an
   * alternative way to handle the response if the original request fails. It returns
   * a `ResponseEntity.notFound()` object, which is a standard response for a 404 status
   * code.
   */
  @Override
  public ResponseEntity<GetCommunityDetailsResponse> listCommunityDetails(
      @PathVariable String communityId) {
    log.trace("Received request to get details about community with id[{}]", communityId);

    return communityService.getCommunityDetailsById(communityId)
        .map(communityApiMapper::communityToRestApiResponseCommunity)
        .map(Arrays::asList)
        .map(HashSet::new)
        .map(communities -> new GetCommunityDetailsResponse().communities(communities))
        .map(ResponseEntity::ok)
        .orElseGet(() -> ResponseEntity.notFound().build());
  }

  /**
   * receives a community ID and page number, retrieves the list of admins for that
   * community from the database using `communityService.findCommunityAdminsById`, maps
   * the results to a `HashSet`, converts the `HashSet` to a `ListCommunityAdminsResponse`
   * object, and returns the response entity.
   * 
   * @param communityId ID of the community for which the admins are to be listed.
   * 
   * 	- `String communityId`: The ID of the community for which the admins need to be
   * listed.
   * 
   * The function performs the following operations:
   * 
   * 1/ Logs a trace message to track the request.
   * 2/ Calls the `findCommunityAdminsById` method of the `communityService` to retrieve
   * a list of admins for the specified community ID.
   * 3/ Maps the result to a new `HashSet` object.
   * 4/ Calls the `communityApiMapper` to map the `CommunityAdmin` objects to the desired
   * REST API response format.
   * 5/ Maps the resulting `List<CommunityAdmin>` to a `ListCommunityAdminsResponse` object.
   * 6/ Returns an `ResponseEntity` with a status code of `OK`. If the call fails, it
   * returns an `ResponseEntity` with a status code of `NOT FOUND`.
   * 
   * @param pageable page number and page size of the result list, which allows for
   * pagination of the community admins.
   * 
   * 	- `@PageableDefault(size = 200)` specifies that the pageable should have a default
   * size of 200.
   * 	- `Pageable` is an interface that provides methods for navigating through a
   * sequence of objects, typically in a paginated manner.
   * 	- `map()` method is used to map the deserialized input to a new object, which in
   * this case is a `HashSet` containing the community admins.
   * 
   * @returns a `ResponseEntity` object containing a list of community admins.
   * 
   * 	- `ResponseEntity<ListCommunityAdminsResponse>`: This is the overall response
   * entity, which contains the list of community admins in its `admins` field.
   * 	- `ListCommunityAdminsResponse`: This class represents the list of community
   * admins, which includes a list of `CommunityAdmin` objects.
   * 	- `CommunityAdmin`: This class represents a single community admin, including
   * their ID, username, and email address.
   * 	- `HashSet<CommunityAdmin>`: This is the container for the list of community
   * admins, which is created using the `map()` method to transform the result of the
   * `findCommunityAdminsById()` method into a list of `CommunityAdmin` objects.
   * 	- `communityApiMapper::communityAdminSetToRestApiResponseCommunityAdminSet`: This
   * method is used to map the list of `CommunityAdmin` objects to a list of
   * `ListCommunityAdminsResponse` objects, which is the desired output format for the
   * function.
   */
  @Override
  public ResponseEntity<ListCommunityAdminsResponse> listCommunityAdmins(
      @PathVariable String communityId,
      @PageableDefault(size = 200) Pageable pageable) {
    log.trace("Received request to list all admins of community with id[{}]", communityId);

    return communityService.findCommunityAdminsById(communityId, pageable)
        .map(HashSet::new)
        .map(communityApiMapper::communityAdminSetToRestApiResponseCommunityAdminSet)
        .map(admins -> new ListCommunityAdminsResponse().admins(admins))
        .map(ResponseEntity::ok)
        .orElseGet(() -> ResponseEntity.notFound().build());
  }

  /**
   * receives a community ID and pageable parameters, retrieves community houses from
   * the service, maps them to a set, converts them to a REST API response, and returns
   * an `ResponseEntity`.
   * 
   * @param communityId identifier of the community for which the user is requesting
   * to list all houses.
   * 
   * 	- `communityId`: A string representing the unique identifier for a community.
   * 
   * @param pageable default page request, allowing for pagination of results from the
   * community service.
   * 
   * The `@PageableDefault(size = 200)` annotation on the `pageable` parameter sets the
   * default page size to 200.
   * 
   * @returns a `ResponseEntity` containing a list of houses belonging to the specified
   * community.
   * 
   * 	- `ResponseEntity<GetHouseDetailsResponse>`: This represents an entity that
   * contains a response to the list community houses request. The response is in the
   * form of a list of `CommunityHouseSet`, which are sets of `CommunityHouse` objects.
   * 	- `GetHouseDetailsResponse`: This class represents the response to the list
   * community houses request, which contains a list of `CommunityHouse` objects.
   * 	- `houses`: This is a list of `CommunityHouse` objects that make up the response.
   * Each object in the list contains information about a particular house in the community.
   * 	- `Pageable`: This represents the pageable response, which allows for pagination
   * of the list of houses. The `size` attribute specifies the number of houses to
   * include on each page.
   */
  @Override
  public ResponseEntity<GetHouseDetailsResponse> listCommunityHouses(
      @PathVariable String communityId,
      @PageableDefault(size = 200) Pageable pageable) {
    log.trace("Received request to list all houses of community with id[{}]", communityId);

    return communityService.findCommunityHousesById(communityId, pageable)
        .map(HashSet::new)
        .map(communityApiMapper::communityHouseSetToRestApiResponseCommunityHouseSet)
        .map(houses -> new GetHouseDetailsResponse().houses(houses))
        .map(ResponseEntity::ok)
        .orElseGet(() -> ResponseEntity.notFound().build());
  }

  /**
   * adds admins to a community based on a request and returns a response entity with
   * the updated community information.
   * 
   * @param communityId identifier of the community whose admins are being added.
   * 
   * 	- `communityId`: A string representing the ID of the community for which admins
   * are being added.
   * 
   * @param request AddCommunityAdminRequest object containing the information about
   * the new admin users to be added to the community, which is validated and used to
   * add them to the community.
   * 
   * 	- `@Valid`: Indicates that the request body must contain a valid `AddCommunityAdminRequest`
   * object.
   * 	- `@PathVariable`: Represents the community ID passed as a path variable in the
   * URL.
   * 	- `@RequestBody`: Marks the `AddCommunityAdminRequest` object as the request body,
   * which is deserialized from the JSON format.
   * 	- `AddCommunityAdminRequest`: A Java class that contains properties for adding
   * admins to a community. These properties may include the user IDs of the admins to
   * be added, and other relevant details.
   * 
   * @returns a `ResponseEntity` with a status code of `CREATED` and a `AddCommunityAdminResponse`
   * object containing the updated list of admins for the specified community.
   * 
   * 	- `ResponseEntity`: This is an instance of the `ResponseEntity` class, which
   * represents a response message with a status code and a body. The status code
   * indicates whether the request was successful or not, while the body contains the
   * response data.
   * 	- `status`: This is an instance of the `HttpStatus` class, which represents the
   * HTTP status code of the response. The possible values are 200 (OK), 400 (Bad
   * Request), 401 (Unauthorized), etc.
   * 	- `body`: This is an instance of the `AddCommunityAdminResponse` class, which
   * contains the response data for the request. Specifically, it has a single attribute
   * called `admins`, which is a set of strings representing the IDs of the newly added
   * community administrators.
   */
  @Override
  public ResponseEntity<AddCommunityAdminResponse> addCommunityAdmins(
      @PathVariable String communityId, @Valid @RequestBody
      AddCommunityAdminRequest request) {
    log.trace("Received request to add admin to community with id[{}]", communityId);
    Optional<Community> communityOptional =
        communityService.addAdminsToCommunity(communityId, request.getAdmins());
    return communityOptional.map(community -> {
      Set<String> adminsSet = community.getAdmins()
          .stream()
          .map(User::getUserId)
          .collect(Collectors.toSet());
      AddCommunityAdminResponse response = new AddCommunityAdminResponse().admins(adminsSet);
      return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }).orElse(ResponseEntity.status(HttpStatus.NOT_FOUND).build());
  }

  /**
   * adds houses to a community identified by its ID. It receives a request with the
   * houses to be added, converts them into a set of community house objects, and then
   * adds them to the community using the `communityService`. If successful, it returns
   * a response with the IDs of the added houses.
   * 
   * @param communityId identifier of the community to which the houses are being added.
   * 
   * 	- `communityId`: A string representing the ID of the community to which houses
   * will be added.
   * 
   * The function performs the following operations:
   * 
   * 1/ Logs a trace message with the community ID.
   * 2/ Maps the `AddCommunityHouseRequest` body's `houses` field to a set of
   * `CommunityHouse` objects using the `communityApiMapper`.
   * 3/ Calls the `addHousesToCommunity` method on the `communityService` with the
   * community ID and the set of `CommunityHouse` objects as arguments.
   * 4/ Checks if any houses were successfully added to the community by comparing the
   * size of the `houseIds` set to 0. If it is not 0, a `AddCommunityHouseResponse`
   * object is created with the `houseIds` set as its body and the response status code
   * is set to `HttpStatus.CREATED`.
   * 5/ Otherwise, the response status code is set to `HttpStatus.BAD_REQUEST`.
   * 
   * @param request AddCommunityHouseRequest object containing the house names to be
   * added to the specified community, which is passed from the client side as a valid
   * JSON request body.
   * 
   * 	- `@Valid`: This annotation is used to indicate that the input request body must
   * be valid according to the specified schema.
   * 	- `@PathVariable`: This annotation is used to inject a path variable from the URL
   * into the function as a String parameter. In this case, it is used to pass the
   * `communityId` parameter from the URL.
   * 	- `@RequestBody`: This annotation is used to indicate that the input request body
   * must be serialized and passed to the function as a single entity.
   * 	- `AddCommunityHouseRequest`: This is the class that contains the properties of
   * the input request, which are described below:
   * 	+ `getHouses()`: This method returns a set of `CommunityHouseName` objects, which
   * represent the houses to be added to the community.
   * 	+ `getCommunityId()`: This method returns the ID of the community where the houses
   * will be added.
   * 
   * The function first logs a trace message indicating that it has received a request
   * to add houses to a community with the specified ID. Then, it performs the following
   * actions:
   * 
   * 1/ It converts the set of `CommunityHouseName` objects returned by `request.getHouses()`
   * into a set of `CommunityHouse` objects using the
   * `communityApiMapper.communityHouseNamesSetToCommunityHouseSet()` method.
   * 2/ It adds the houses to the community using the `communityService.addHousesToCommunity()`
   * method, passing in the ID of the community and the set of `CommunityHouse` objects
   * as arguments.
   * 3/ If the addition was successful (i.e., the number of added houses is non-zero
   * and the number of house IDs in the response is non-zero), it returns a `ResponseEntity`
   * with a status code of `CREATED` and a body containing an `AddCommunityHouseResponse`
   * object. Otherwise, it returns a `ResponseEntity` with a status code of `BAD_REQUEST`.
   * 
   * @returns a `ResponseEntity` object with a status code of `CREATED` and a
   * `AddCommunityHouseResponse` object containing the added house IDs.
   * 
   * 	- `AddCommunityHouseResponse`: This is the class that represents the response to
   * the API request. It has a single property called `houses`, which is a set of strings
   * representing the IDs of the added houses.
   * 	- `HttpStatus`: This is an enumeration that represents the HTTP status code of
   * the response. In this case, it can be either `CREATED` or `BAD_REQUEST`.
   * 	- `ResponseEntity`: This is a class that represents the overall response to the
   * API request. It has a `status` property and an `body` property, where the body
   * contains the `AddCommunityHouseResponse` object.
   */
  @Override
  public ResponseEntity<AddCommunityHouseResponse> addCommunityHouses(
      @PathVariable String communityId, @Valid @RequestBody
      AddCommunityHouseRequest request) {
    log.trace("Received request to add house to community with id[{}]", communityId);
    Set<CommunityHouseName> houseNames = request.getHouses();
    Set<CommunityHouse> communityHouses =
        communityApiMapper.communityHouseNamesSetToCommunityHouseSet(houseNames);
    Set<String> houseIds = communityService.addHousesToCommunity(communityId, communityHouses);
    if (houseIds.size() != 0 && houseNames.size() != 0) {
      AddCommunityHouseResponse response = new AddCommunityHouseResponse();
      response.setHouses(houseIds);
      return ResponseEntity.status(HttpStatus.CREATED).body(response);
    } else {
      return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
    }
  }

  /**
   * deletes a house from a community based on the given community ID and house ID,
   * returning a response entity indicating whether the operation was successful or not.
   * 
   * @param communityId 12-digit unique identifier of a community that contains the
   * house to be removed.
   * 
   * 	- `String communityId`: The unique identifier for a community.
   * 	- `String houseId`: The unique identifier for a house within a community.
   * 
   * @param houseId identifier of the house to be removed from the specified community.
   * 
   * 	- `communityId`: The ID of the community that the house belongs to.
   * 	- `houseId`: The unique identifier of the house to be removed from the community.
   * 
   * @returns a `ResponseEntity` object representing a successful deletion of a house
   * from a community, with a status code of `noContent`.
   * 
   * 	- `ResponseEntity<Void>`: The type of the output is a response entity with a void
   * type.
   * 	- `<Void>`: The type parameter of the response entity is void.
   * 	- `.noContent()`: The `build()` method returns a response entity with a status
   * code of 204 (No Content).
   * 	- `.orElseGet()`: This method allows for alternative ways to return a response
   * entity if the `getCommunityDetailsById` and `removeHouseFromCommunityByHouseId`
   * methods return a null value.
   */
  @Override
  public ResponseEntity<Void> removeCommunityHouse(
      @PathVariable String communityId, @PathVariable String houseId
  ) {
    log.trace(
        "Received request to delete house with id[{}] from community with id[{}]",
        houseId, communityId);

    Optional<Community> communityOptional = communityService.getCommunityDetailsById(communityId);

    return communityOptional.filter(
        community -> communityService.removeHouseFromCommunityByHouseId(community, houseId))
        .map(removed -> ResponseEntity.noContent().<Void>build())
        .orElseGet(() -> ResponseEntity.notFound().build());
  }

  /**
   * receives the community ID and admin ID as path variables, and uses the `communityService`
   * to remove an admin from a community. If successful, it returns a HTTP NO_CONTENT
   * status code, otherwise it returns a NOT_FOUND status code.
   * 
   * @param communityId id of the community that the admin belongs to.
   * 
   * 	- `communityId`: This is a string that represents the unique identifier for a
   * community in the application. It could be a UUID or any other suitable identifier.
   * 
   * @param adminId identifier of the admin to be removed from the community.
   * 
   * 	- `communityId`: The ID of the community where the admin is to be removed.
   * 	- `adminId`: The ID of the admin to be removed from the community.
   * 
   * @returns a `ResponseEntity` with a status code of either `NO_CONTENT` or `NOT_FOUND`,
   * depending on whether the admin was successfully removed from the community.
   * 
   * 	- `HttpStatus.NO_CONTENT`: This status code indicates that the admin was successfully
   * removed from the community.
   * 	- `HttpStatus.NOT_FOUND`: This status code indicates that the admin could not be
   * found in the community, which means that either the admin ID or the community ID
   * is invalid.
   */
  @Override
  public ResponseEntity<Void> removeAdminFromCommunity(
      @PathVariable String communityId, @PathVariable String adminId) {
    log.trace(
        "Received request to delete an admin from community with community id[{}] and admin id[{}]",
        communityId, adminId);
    boolean adminRemoved = communityService.removeAdminFromCommunity(communityId, adminId);
    if (adminRemoved) {
      return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    } else {
      return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
    }
  }

  /**
   * deletes a community identified by the `communityId` parameter, returning a
   * `ResponseEntity` object indicating whether the deletion was successful or not.
   * 
   * @param communityId ID of the community to be deleted.
   * 
   * 	- `communityId`: A string representing the ID of the community to be deleted.
   * 
   * @returns a HTTP NO_CONTENT status code if the community was successfully deleted,
   * and a NOT_FOUND status code otherwise.
   * 
   * 	- HttpStatus.NO_CONTENT: This indicates that the community was successfully deleted.
   * 	- HttpStatus.NOT_FOUND: This indicates that the specified community could not be
   * found.
   */
  @Override
  public ResponseEntity<Void> deleteCommunity(@PathVariable String communityId) {
    log.trace("Received delete community request");
    boolean isDeleted = communityService.deleteCommunity(communityId);
    if (isDeleted) {
      return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    } else {
      return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
    }
  }
}
