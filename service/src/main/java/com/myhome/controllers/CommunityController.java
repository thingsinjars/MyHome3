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
 * is responsible for handling requests related to managing communities in an API.
 * It receives requests to add admins, houses, and community users, as well as remove
 * admins, houses, and community users from a community. The controller uses the
 * `communityService` class to perform these actions and returns a response entity
 * indicating whether the operation was successful or not. The controller also has
 * methods for deleting a community and returning a response entity with a status
 * code indicating whether the deletion was successful or not.
 */
@RequiredArgsConstructor
@RestController
@Slf4j
public class CommunityController implements CommunitiesApi {
  private final CommunityService communityService;
  private final CommunityApiMapper communityApiMapper;

  /**
   * receives a `CreateCommunityRequest` object from the client and creates a new
   * community instance using the provided details. It then maps the created community
   * to a `CreateCommunityResponse` object and returns it as a response entity with a
   * status of `HttpStatus.CREATED`.
   * 
   * @param request CreateCommunityRequest object containing the details of the community
   * to be created.
   * 
   * * `@Valid`: The request body is validated using the `@Valid` annotation.
   * * `@RequestBody`: The request body is serialized and sent as a parameter to the function.
   * * `CreateCommunityRequest`: The request class that contains the properties of the
   * community to be created.
   * * `log.trace()`: A log statement that traces the receipt of the create community
   * request.
   * 
   * @returns a `CreateCommunityResponse` object containing the created community details.
   * 
   * * `CreateCommunityResponse createdCommunityResponse`: This is an instance of the
   * `CreateCommunityResponse` class, which contains information about the newly created
   * community. The response includes the ID of the created community, its name, and a
   * URL for linking to it.
   * * `HttpStatus.CREATED`: This is the status code returned by the server, indicating
   * that the request was successful and the community was created.
   * * `ResponseEntity`: This is an instance of the `ResponseEntity` class, which
   * represents a response from the server. It contains information about the status
   * of the request, as well as the body of the response.
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
   * retrieves a list of communities from the service and maps them to a REST API
   * response, which is then returned as a `GetCommunityDetailsResponse`.
   * 
   * @param pageable page parameters for the list of communities, such as the page
   * number and size of the list, which are used to retrieve the data from the database
   * in a paginated manner.
   * 
   * * `@PageableDefault(size = 200)` specifies that the `pageable` object has a default
   * size of 200.
   * * `Pageable pageable` represents an object that allows for navigating through a
   * collection of objects, such as a list or a set, by specifying the number of elements
   * to skip, the number of elements to include in each page, and the total number of
   * pages.
   * 
   * @returns a list of community details in a REST API response format.
   * 
   * * `GetCommunityDetailsResponse`: This is the class that represents the response
   * to the API call. It has a list of `Community` objects as its attribute.
   * * `communitySetToRestApiResponseCommunitySet`: This is a method that maps the
   * `Set<Community>` object returned by the `listAllCommunity` function to a corresponding
   * `List<GetCommunityDetailsResponseCommunity>` object. This method is used to transform
   * the internal representation of the data into the desired format for the API response.
   * * `communityService`: This is an instance of a service class that provides access
   * to the community data. It is used to retrieve the data from the backend system.
   * * `log`: This is a logging utility that is used to trace the execution of the
   * function and provide diagnostic information.
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
   * receives a community ID and retrieves community details from the service, mapping
   * them to a response entity for return.
   * 
   * @param communityId identifier of the community for which details are requested.
   * 
   * @returns a `ResponseEntity` object containing a list of community details.
   * 
   * * `ResponseEntity<GetCommunityDetailsResponse>` is a class that represents a
   * response entity with a status code and a body containing the details of the community.
   * * `getCommunityDetailsById(communityId)` is a method that retrieves the details
   * of a specific community based on its ID.
   * * `map(Function<? super T, R> mapper)` is a method that maps the result of the
   * previous method call to a new response entity with a different type. In this case,
   * it maps the list of communities to a response entity with a status code of `ok`.
   * * `orElseGet(() -> ResponseEntity.notFound().build()` is a method that provides
   * an alternative value if the original method call returns `null`. In this case, it
   * returns a response entity with a status code of `not Found`.
   * 
   * The various attributes of the output are:
   * 
   * * Status code: The status code of the response entity, which indicates whether the
   * request was successful (200) or not (404 for example).
   * * Body: The body of the response entity, which contains the details of the community.
   * * Type: The type of the response entity, which is `GetCommunityDetailsResponse`
   * in this case.
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
   * receives a community ID and page number, retrieves the admins of that community,
   * maps them to a Rest API response format, and returns it as a `ResponseEntity`.
   * 
   * @param communityId ID of the community for which the list of admins is requested.
   * 
   * @param pageable paging information for the list of community admins, allowing the
   * method to fetch a subset of the admins starting from a specified page and with a
   * maximum number of results per page.
   * 
   * * `@PageableDefault(size = 200)`: This annotation specifies the default page size
   * for pagination, which is set to 200 in this example. The page size determines the
   * number of community admins returned per page.
   * * `Pageable`: This interface represents a pageable object, which provides methods
   * for navigating through a sequence of pages. In this function, `pageable` is used
   * to determine the pagination settings for the list of community admins.
   * 
   * @returns a `ListCommunityAdminsResponse` object containing the list of community
   * admins.
   * 
   * * `ResponseEntity<ListCommunityAdminsResponse>`: This is the overall response
   * entity that contains the list of community admins.
   * * `ListCommunityAdminsResponse`: This class represents the list of community admins
   * in a REST API format. It has an `admins` field that contains the list of admins
   * for the specified community ID.
   * * `map(Function<T, U> mappingFunction)`: This method is used to map the input data
   * to the desired output format using a provided function. In this case, it maps the
   * `HashSet` of community admins to the `ListCommunityAdminsResponse` class.
   * * `orElseGet(() -> ResponseEntity.notFound().build())`: This method is used to
   * provide an alternative response if the original data cannot be processed successfully.
   * In this case, it returns a `ResponseEntity.notFound()` response if the community
   * ID is not found.
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
   * receives a community ID and page number, retrieves all houses associated with that
   * community from the database, maps them to a `HashSet`, converts them into a
   * `GetHouseDetailsResponse`, and returns the response entity.
   * 
   * @param communityId unique identifier for a community that the user wants to retrieve
   * house details for.
   * 
   * @param pageable page size and sort order for listing community houses, allowing
   * for pagination of the results.
   * 
   * * `@PageableDefault(size = 200)`: This annotation specifies the default page size
   * for the list of houses returned in the response. The value `200` represents the
   * maximum number of houses to be listed per page.
   * * `Pageable`: This is an interface that defines the `getPageNumber()` and
   * `getPageSize()` methods, which are used to control the pagination of the list of
   * houses.
   * 
   * @returns a `GetHouseDetailsResponse` object containing a list of houses for a
   * specified community.
   * 
   * * `ResponseEntity<GetHouseDetailsResponse>` is an object that represents a response
   * with a status code and a body containing the list of houses.
   * * `getHouses()` returns a list of `CommunityHouseSet`, which is a set of house
   * details in JSON format.
   * * `map(Function<List<CommunityHouse>, GetHouseDetailsResponse>)` is a method that
   * takes a function as an argument and applies it to the list of houses, transforming
   * them into a response object.
   * * `orElseGet(() -> ResponseEntity.notFound().build())` is a method that returns a
   * default response if the function provided as an argument does not produce a valid
   * response. In this case, the default response is `ResponseEntity.notFound().build()`,
   * which means "not found".
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
   * adds admins to a community based on a request. It retrieves the community and adds
   * the provided admins to it, returning a response with the updated admins set.
   * 
   * @param communityId ID of the community for which admins are being added.
   * 
   * @param request AddCommunityAdminRequest object containing the information about
   * the admins to be added to the community.
   * 
   * * `@Valid`: This annotation indicates that the input request body must be validated
   * according to the specified validation rules.
   * * `@PathVariable`: This annotation specifies that the `communityId` parameter is
   * passed from the URL path variable.
   * * `AddCommunityAdminRequest`: This class represents the request body, which contains
   * the list of admins to be added to the community. Its properties are:
   * 	+ `admins()`: A list of admin user IDs.
   * 
   * @returns a `ResponseEntity` object with a status code of `CREATED` and a
   * `AddCommunityAdminResponse` object containing the set of admins for the specified
   * community.
   * 
   * * `ResponseEntity`: This is an instance of the `ResponseEntity` class, which
   * represents a response to a HTTP request. It has a status code and a body, which
   * in this case contains the `AddCommunityAdminResponse`.
   * * `AddCommunityAdminResponse`: This is a custom response class that contains
   * information about the added admins to the community. It has a single attribute
   * called `admins`, which is a set of user IDs.
   * * `HttpStatus.CREATED`: This is an instance of the `HttpStatus` class, which
   * represents a HTTP status code indicating that the request was successful and the
   * resource was created.
   * * `body`: This is the body of the response entity, which contains the `AddCommunityAdminResponse`.
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
   * adds one or more houses to a community identified by its ID. It receives a request
   * body containing the house names and returns a response with the IDs of the added
   * houses.
   * 
   * @param communityId ID of the community to which new houses will be added.
   * 
   * @param request AddCommunityHouseRequest object that contains the houses to be added
   * to the community.
   * 
   * * `@Valid`: The `AddCommunityHouseRequest` object is validated using the `@Valid`
   * annotation.
   * * `@PathVariable String communityId`: The community ID is passed as a path variable
   * in the URL.
   * * `@RequestBody AddCommunityHouseRequest request`: The `AddCommunityHouseRequest`
   * object is serialized and passed in the body of the HTTP request.
   * 
   * @returns a `ResponseEntity` with a `HttpStatus.CREATED` code and a `AddCommunityHouseResponse`
   * object containing the newly created house IDs.
   * 
   * * `AddCommunityHouseResponse`: This is the class that represents the response to
   * the API request.
   * * `setHouses()`: This method sets the `houses` attribute of the `AddCommunityHouseResponse`
   * object to a set of strings representing the IDs of the added houses.
   * * `status()`: The HTTP status code of the response, which is set to `HttpStatus.CREATED`
   * if the request was successful and `HttpStatus.BAD_REQUEST` otherwise.
   * * `body()`: The body of the response, which is an instance of `AddCommunityHouseResponse`.
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
   * deletes a house from a community based on its ID, using the community service to
   * retrieve the community details and remove the house from the community.
   * 
   * @param communityId ID of the community that the house to be deleted belongs to.
   * 
   * @param houseId ID of the house to be removed from the specified community.
   * 
   * @returns a `ResponseEntity` object with a status code of `noContent`, indicating
   * that the house was successfully removed from the community.
   * 
   * * `ResponseEntity<Void>`: This represents an empty response entity with a type of
   * Void.
   * * `noContent()`: This is a builder class that creates an instance of `ResponseEntity`
   * with a status code of 204 (No Content) and a body of Void.
   * * `<Void>`: This is the type of the body of the response entity, indicating that
   * the response contains no content.
   * 
   * The function first logs a trace message to indicate that it has received a request
   * to delete a house from a community. Then, it uses the `getCommunityDetailsById`
   * method of the `communityService` class to obtain an optional reference to a
   * `Community` object associated with the provided community ID. If the community is
   * found, the function uses the `removeHouseFromCommunityByHouseId` method to remove
   * the house from the community. Finally, the function returns a response entity with
   * a status code of 204 (No Content) and an empty body, indicating that the request
   * was successful.
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
   * removes an admin from a community based on the community ID and admin ID provided
   * in the path variables. It first checks if the admin was successfully removed, then
   * returns a NO_CONTENT status code if successful or a NOT_FOUND status code otherwise.
   * 
   * @param communityId identifier of the community to which the admin belongs.
   * 
   * @param adminId ID of the admin to be removed from the community.
   * 
   * @returns a response entity with a status code of either `NO_CONTENT` or `NOT_FOUND`,
   * depending on whether the admin was successfully removed from the community.
   * 
   * * `HttpStatus.NO_CONTENT`: This status code indicates that the requested resource
   * has been successfully deleted and no content was returned in the response body.
   * * `HttpStatus.NOT_FOUND`: This status code indicates that the admin to be removed
   * could not be found in the community with the provided ID.
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
   * handles requests to delete a community. It checks if the community is deleted
   * successfully and returns a `NO_CONTENT` status code if successful, or a `NOT_FOUND`
   * status code otherwise.
   * 
   * @param communityId ID of the community to be deleted.
   * 
   * @returns a `ResponseEntity` with a status code of either `NO_CONTENT` or `NOT_FOUND`,
   * depending on whether the community was successfully deleted.
   * 
   * * `ResponseEntity<Void>`: This is the type of the output returned by the function.
   * It represents an empty response entity with a status code.
   * * `HttpStatus.NO_CONTENT`: This is the status code associated with the output. It
   * indicates that the requested resource has been successfully deleted and no content
   * was returned.
   * * `HttpStatus.NOT_FOUND`: This is the alternative status code associated with the
   * output when the delete operation fails due to the community not found.
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
