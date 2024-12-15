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

import com.myhome.api.HousesApi;
import com.myhome.controllers.dto.mapper.HouseMemberMapper;
import com.myhome.controllers.mapper.HouseApiMapper;
import com.myhome.domain.CommunityHouse;
import com.myhome.domain.HouseMember;
import com.myhome.model.AddHouseMemberRequest;
import com.myhome.model.AddHouseMemberResponse;
import com.myhome.model.GetHouseDetailsResponse;
import com.myhome.model.GetHouseDetailsResponseCommunityHouse;
import com.myhome.model.ListHouseMembersResponse;
import com.myhome.services.HouseService;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;
import javax.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

/**
 * provides functions for managing houses and their members. These functions include
 * getting house details, listing all members of a house, adding members to a house,
 * and deleting members from a house. The class uses generic types and method references
 * to work with different response entities and status codes. Additionally, the class
 * logs messages related to the execution of its functions using a logging object.
 */
@RestController
@RequiredArgsConstructor
@Slf4j
public class HouseController implements HousesApi {
  private final HouseMemberMapper houseMemberMapper;
  private final HouseService houseService;
  private final HouseApiMapper houseApiMapper;

  /**
   * receives a pageable request parameter and lists all houses from the database using
   * the `houseService`. The list is then mapped to a REST API response using
   * `houseApiMapper`, and returned as a `GetHouseDetailsResponse` object in a `ResponseEntity`.
   * 
   * @param pageable page request parameters, such as the page number and size of the
   * result set, which allows the listAllHouses method to filter and sort the house
   * details based on the user's preferences.
   * 
   * * `@PageableDefault`: This annotation indicates that the `pageable` parameter has
   * default values for its properties, which are specified in the annotation metadata.
   * The defaults are specified as an immutable Map, where each key-value pair represents
   * a property of the pageable object. In this case, the default values are `size=200`,
   * indicating that the list of houses will be returned with up to 200 elements per page.
   * * `Pageable`: This interface defines the methods required for pagination, including
   * `getTotalElements()` (which returns the total number of houses in the list),
   * `getTotalPages()` (which returns the total number of pages that can be generated
   * from the list), and `getPageAtPosition()` (which returns a page at a specific position).
   * * `size`: This property represents the number of elements to return per page. It
   * is set to `200` by default, but can be changed depending on the requirements of
   * the application.
   * 
   * @returns a `GetHouseDetailsResponse` object containing a set of `CommunityHouse`
   * objects converted from the list of houses retrieved from the database.
   * 
   * * `GetHouseDetailsResponse`: This class represents the response to the list all
   * houses request.
   * * `setHouses()`: This method returns a set of `GetHouseDetailsResponseCommunityHouse`
   * objects, which contain the details of each house in the community.
   * * `HttpStatus.OK`: The status code of the response indicates that the request was
   * successful.
   * * `body()`: This method returns the response body, which contains the
   * `GetHouseDetailsResponse` object.
   */
  @Override
  public ResponseEntity<GetHouseDetailsResponse> listAllHouses(
      @PageableDefault(size = 200) Pageable pageable) {
    log.trace("Received request to list all houses");

    Set<CommunityHouse> houseDetails =
        houseService.listAllHouses(pageable);
    Set<GetHouseDetailsResponseCommunityHouse> getHouseDetailsResponseSet =
        houseApiMapper.communityHouseSetToRestApiResponseCommunityHouseSet(houseDetails);

    GetHouseDetailsResponse response = new GetHouseDetailsResponse();

    response.setHouses(getHouseDetailsResponseSet);

    return ResponseEntity.status(HttpStatus.OK).body(response);
  }

  /**
   * retrieves house details for a given id and maps them to a `GetHouseDetailsResponse`.
   * 
   * @param houseId id of the house for which details are requested to be retrieved.
   * 
   * @returns a `GetHouseDetailsResponse` object containing a list of houses with their
   * details.
   * 
   * * `ResponseEntity<GetHouseDetailsResponse>`: This is a functional response entity
   * that returns a `GetHouseDetailsResponse` object.
   * * `getHouseDetailsResponseCommunityHouses`: This is a list of community houses
   * that are returned as part of the response.
   * * `houses(getHouseDetailsResponseCommunityHouses)`: This is a method that takes a
   * list of community houses and returns them in the response.
   * * `map(Function<GetHouseDetailsResponse, ResponseEntity<GetHouseDetailsResponse>>
   * mapper)`: This line uses a lambda function to map the `getHouseDetailsResponseCommunityHouses`
   * list to a `ResponseEntity<GetHouseDetailsResponse>` object.
   * * `orElse(ResponseEntity.notFound().build())`: This line provides an alternative
   * response if the `map` method does not return a valid response. It returns a
   * `ResponseEntity.notFound()` object by default.
   */
  @Override
  public ResponseEntity<GetHouseDetailsResponse> getHouseDetails(String houseId) {
    log.trace("Received request to get details of a house with id[{}]", houseId);
    return houseService.getHouseDetailsById(houseId)
        .map(houseApiMapper::communityHouseToRestApiResponseCommunityHouse)
        .map(Collections::singleton)
        .map(getHouseDetailsResponseCommunityHouses -> new GetHouseDetailsResponse().houses(getHouseDetailsResponseCommunityHouses))
        .map(ResponseEntity::ok)
        .orElse(ResponseEntity.notFound().build());
  }

  /**
   * retrieves all members of a house identified by `houseId`, converts them to a set
   * using `HashSet::new`, maps each member to a REST API response, and returns the
   * resulting list of members in the `ListHouseMembersResponse`.
   * 
   * @param houseId unique identifier of the house for which members are to be listed.
   * 
   * @param pageable page request, specifying the number of members to return and the
   * starting point for the list.
   * 
   * * `size`: The maximum number of members to return per page.
   * * `sort`: The field by which the members will be sorted.
   * * `direction`: The direction of the sort (ascending or descending).
   * 
   * @returns a `ResponseEntity<ListHouseMembersResponse>` object containing a list of
   * `HouseMember` objects in a REST API format.
   * 
   * * `ResponseEntity<ListHouseMembersResponse>`: This is the generic type of the
   * returned response entity, which represents a list of `HouseMember` objects.
   * * `ListHouseMembersResponse`: This class represents the contents of the list
   * returned by the function. It has a single property, `members`, which is a list of
   * `HouseMember` objects.
   * * `members`: This property is a list of `HouseMember` objects, which are the actual
   * members of the house being listed. Each object in the list contains the member's
   * ID, name, and any other relevant information.
   */
  @Override
  public ResponseEntity<ListHouseMembersResponse> listAllMembersOfHouse(
      String houseId,
      @PageableDefault(size = 200) Pageable pageable) {
    log.trace("Received request to list all members of the house with id[{}]", houseId);

    return houseService.getHouseMembersById(houseId, pageable)
        .map(HashSet::new)
        .map(houseMemberMapper::houseMemberSetToRestApiResponseHouseMemberSet)
        .map(houseMembers -> new ListHouseMembersResponse().members(houseMembers))
        .map(ResponseEntity::ok)
        .orElse(ResponseEntity.notFound().build());
  }

  /**
   * adds members to a house based on the provided request. It retrieves the existing
   * members of the house, updates the member list with new members, and returns the
   * updated list or a NOT_FOUND status if no new members were added.
   * 
   * @param houseId identifier of the house for which members are being added.
   * 
   * @param request AddHouseMemberRequest object containing the member information to
   * be added to the specified house.
   * 
   * * `houseId`: The ID of the house to which members are being added.
   * * `request.getMembers()`: A set of `HouseMemberDto` objects representing the new
   * members to be added to the house.
   * * `houseService.addHouseMembers(houseId, members)`: An API call that adds the
   * members to the house, returning a set of saved members.
   * 
   * @returns a `ResponseEntity` object containing the added house members as a list
   * of `AddHouseMemberResponse` objects.
   * 
   * * `AddHouseMemberResponse`: This is a response object that contains information
   * about the added members to the house.
   * 	+ `setMembers`: This is a set of `House Member` objects that represent the added
   * members to the house. These objects have attributes such as `id`, `name`, `email`,
   * and `role`.
   * 
   * The function returns an `AddHouseMemberResponse` object depending on whether any
   * members were added successfully or not. If no members were added, it returns a
   * `ResponseEntity` with a status code of `NOT_FOUND`. Otherwise, it returns a
   * `ResponseEntity` with a status code of `CREATED` and the `AddHouseMemberResponse`
   * object as the body.
   */
  @Override
  public ResponseEntity<AddHouseMemberResponse> addHouseMembers(
      @PathVariable String houseId, @Valid AddHouseMemberRequest request) {

    log.trace("Received request to add member to the house with id[{}]", houseId);
    Set<HouseMember> members =
        houseMemberMapper.houseMemberDtoSetToHouseMemberSet(request.getMembers());
    Set<HouseMember> savedHouseMembers = houseService.addHouseMembers(houseId, members);

    if (savedHouseMembers.size() == 0 && request.getMembers().size() != 0) {
      return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
    } else {
      AddHouseMemberResponse response = new AddHouseMemberResponse();
      response.setMembers(
          houseMemberMapper.houseMemberSetToRestApiResponseAddHouseMemberSet(savedHouseMembers));
      return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }
  }

  /**
   * deletes a member from a house based on the provided house ID and member ID, returning
   * an HTTP response indicating whether the operation was successful or not.
   * 
   * @param houseId 12-digit unique identifier of the house for which the member is to
   * be deleted.
   * 
   * @param memberId ID of the member to be deleted from the specified house.
   * 
   * @returns a `ResponseEntity` object with a status code of either `NO_CONTENT` or
   * `NOT_FOUND`, depending on whether the member was successfully deleted or not.
   * 
   * * The `ResponseEntity` object represents the result of the delete operation, with
   * a status code indicating whether the operation was successful or not.
   * * The `HttpStatus` field contains the status code of the response, which is either
   * `NO_CONTENT` (204) if the member was successfully deleted, or `NOT_FOUND` (404) otherwise.
   * 
   * Overall, the output of the `deleteHouseMember` function indicates whether the
   * delete operation was successful or not, and provides additional information about
   * the outcome of the operation.
   */
  @Override
  public ResponseEntity<Void> deleteHouseMember(String houseId, String memberId) {
    log.trace("Received request to delete a member from house with house id[{}] and member id[{}]",
        houseId, memberId);
    boolean isMemberDeleted = houseService.deleteMemberFromHouse(houseId, memberId);
    if (isMemberDeleted) {
      return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    } else {
      return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
    }
  }
}