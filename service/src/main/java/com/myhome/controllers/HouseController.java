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
 * TODO
 */
@RestController
@RequiredArgsConstructor
@Slf4j
public class HouseController implements HousesApi {
  private final HouseMemberMapper houseMemberMapper;
  private final HouseService houseService;
  private final HouseApiMapper houseApiMapper;

  /**
   * receives a pageable request and returns a set of houses through the
   * `houseService.listAllHouses()` method, which is then converted to a REST API
   * response using `houseApiMapper.communityHouseSetToRestApiResponseCommunityHouseSet()`.
   * The response is then returned with the list of houses in the set.
   * 
   * @param pageable default page size and sorting options for listing all houses.
   * 
   * 	- `@PageableDefault`: This annotation is used to specify default values for the
   * pageable parameters, in this case, the size of the page to be retrieved. The value
   * `200` indicates that the function will return a maximum of 200 houses per page.
   * 	- `size`: This property represents the number of houses to be returned per page.
   * It can take any non-negative integer value, and in this case, it is set to `200`.
   * 
   * In summary, the `pageable` input to the function has a single property, `size`,
   * which specifies the maximum number of houses to be retrieved per page.
   * 
   * @returns a `GetHouseDetailsResponse` object containing a set of `CommunityHouse`
   * objects converted from the service's response.
   * 
   * 	- `GetHouseDetailsResponse`: This class represents the response to the list all
   * houses request. It has a set of `CommunityHouse` objects as its attribute.
   * 	- `CommunityHouse`: This class represents an individual house in the list. It has
   * several attributes, including the house ID, name, address, and more.
   * 	- `Pageable`: This interface provides methods for pagination, which is used to
   * page the results of the list all houses request. The `size` attribute specifies
   * the number of houses to return per page.
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
   * receives a house ID and retrieves the details of that house from the service layer,
   * mapping the response to a `GetHouseDetailsResponse` object and returning it as an
   * `ResponseEntity`.
   * 
   * @param houseId id of the house for which details are requested, and is used to
   * retrieve the relevant house details from the service.
   * 
   * 	- `log.trace("Received request to get details of a house with id[{}],"` - This
   * line traces the receiving of the request for the details of a particular house
   * using the `log` facility in the logging framework. The format string `"Received
   * request to get details of a house with id[{}]"`, where `houseId` is the actual
   * value being passed as an argument, is used to generate a log message that provides
   * additional context and information about the request.
   * 	- `houseService.getHouseDetailsById(houseId)` - This line calls the `getHouseDetailsById`
   * method of the `houseService` class, passing in the `houseId` as an argument. This
   * method is responsible for retrieving the details of a particular house based on
   * its ID.
   * 	- `map(houseApiMapper::communityHouseToRestApiResponseCommunityHouse)` - This
   * line uses the `map` method to apply a function to the result of the `getHouseDetailsById`
   * call. The function being applied is `houseApiMapper::communityHouseToRestApiResponseCommunityHouse`,
   * which is responsible for converting the house details from the API format used by
   * the `houseService` to the REST API format expected by the `getHouseDetails` function.
   * 	- `map(Collections::singleton)` - This line uses the `map` method again to apply
   * a function to the result of the previous mapping operation. The function being
   * applied is `Collections::singleton`, which returns a single item (i.e., the converted
   * house details) from the result of the previous mapping operation.
   * 	- `map(getHouseDetailsResponseCommunityHouses -> new
   * GetHouseDetailsResponse().houses(getHouseDetailsResponseCommunityHouses))` - This
   * line uses the `map` method again to apply a function to the result of the previous
   * mapping operation. The function being applied is `getHouseDetailsResponseCommunityHouses
   * -> new GetHouseDetailsResponse().houses(getHouseDetailsResponseCommunityHouses)`,
   * which creates a new `GetHouseDetailsResponse` instance and sets its `houses` field
   * to the result of the previous mapping operation.
   * 	- `map(ResponseEntity::ok) - This line uses the `map` method again to apply a
   * function to the result of the previous mapping operation. The function being applied
   * is `ResponseEntity::ok`, which returns a `ResponseEntity` instance with an HTTP
   * status code of 200 (i.e., OK).
   * 	- `orElse(ResponseEntity.notFound().build())` - This line provides an alternative
   * to the previous mapping operation if it fails. The function being applied is
   * `ResponseEntity.notFound().build()`, which creates a new `ResponseEntity` instance
   * with an HTTP status code of 404 (i.e., NOT FOUND).
   * 
   * @returns a `ResponseEntity` object containing a list of house details in REST API
   * format.
   * 
   * 	- `ResponseEntity<GetHouseDetailsResponse>`: This is the generic type of the
   * response entity, which is a wrapper class for the actual response data.
   * 	- `getHouseDetailsResponseCommunityHouses`: This is a list of community houses,
   * which is the primary output of the function. Each house in the list is represented
   * as an object with several attributes, including the house ID, name, and address.
   * 	- `houses(getHouseDetailsResponseCommunityHouses)`: This is a method that takes
   * a list of community houses as input and returns a single list of houses, which is
   * the actual output of the function.
   * 	- `ok`: This is a constant that indicates the response status code for a successful
   * request. In this case, it means the response was successful and the requested data
   * was found.
   * 	- `notFound()`: This is a builder class that creates a response entity with a 404
   * status code, indicating that the requested data could not be found.
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
   * receives a house ID and page size, retrieves the members of the house using the
   * `houseService`, maps them to a `HashSet`, converts them to a REST API response,
   * and returns it as an `ResponseEntity`.
   * 
   * @param houseId ID of the house for which members are to be listed.
   * 
   * 	- `houseId`: The unique identifier for a house, which can be used to retrieve
   * information about the house and its members.
   * 	- `@PageableDefault(size = 200)`: An annotation that specifies the default page
   * size for the list of members returned in the response.
   * 
   * @param pageable 200 members of the house that are to be listed, as specified by
   * the default page size of 200.
   * 
   * The `@PageableDefault` annotation specifies that the page size should be 200 by default.
   * 
   * @returns a `ListHouseMembersResponse` object containing the list of members of the
   * specified house.
   * 
   * 	- `ResponseEntity`: This is the generic type of the response entity, which is an
   * extension of the `ResponseEntity` class.
   * 	- `ListHouseMembersResponse`: This is a custom response class that represents the
   * list of house members. It has a single field called `members`, which is a list of
   * `HouseMember` objects.
   * 	- `ok`: This is a method on the `ResponseEntity` class that indicates the response
   * was successful and includes the requested data.
   * 	- `notFound`: This is a method on the `ResponseEntity` class that indicates the
   * response was not successful (i.e., the house could not be found).
   * 	- `houseId`: This is the ID of the house for which members are being listed.
   * 	- `pageable`: This is a parameter that represents the pageable request, which
   * allows for pagination of the list of members. It has a default value of
   * `@PageableDefault(size = 200)`, which sets the page size to 200.
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
   * receives a request to add members to a house, validates the request, and adds the
   * members to the house's member list. If the addition is successful, it returns a
   * response indicating the new members, otherwise it returns a NOT_FOUND status.
   * 
   * @param houseId ID of the house for which members are being added, and it is used
   * to identify the house in the addHouseMembers method.
   * 
   * 	- `houseId`: A string representing the ID of the house to which members will be
   * added.
   * 
   * The function then performs the following operations:
   * 
   * 	- Logs a trace message with the ID of the house and the request.
   * 	- Maps the `AddHouseMemberRequest` DTO to a `Set` of `House Member` objects using
   * the `houseMemberMapper`.
   * 	- Calls the `addHouseMembers` method on the `houseService` with the ID of the
   * house and the `Set` of `House Member` objects as arguments.
   * 	- Checks if any members were saved successfully, and if not, returns a `ResponseEntity`
   * with a `HttpStatus.NOT_FOUND` status code.
   * 	- If successful, creates a new `AddHouseMemberResponse` object with the saved
   * `House Member` objects, and returns it as the response entity with a `HttpStatus.CREATED`
   * status code.
   * 
   * @param request AddHouseMemberRequest object containing the information about the
   * members to be added to the house.
   * 
   * 	- `houseId`: The ID of the house to which members will be added.
   * 	- `request.getMembers()`: A set of `HouseMemberDto` objects representing the new
   * members to be added to the house.
   * 	- `houseService.addHouseMembers(houseId, members)`: This method adds the members
   * provided in the `request.getMembers()` set to the house with the given ID. The
   * return value is a set of newly created `HouseMember` objects.
   * 
   * @returns a `ResponseEntity` object with a status code of either `HttpStatus.NOT_FOUND`
   * or `HttpStatus.CREATED`, depending on whether any members were added successfully.
   * 
   * 	- `response`: This is an instance of `AddHouseMemberResponse`, which contains a
   * list of `House Member` objects representing the newly added members to the house.
   * 	- `savedHouseMembers`: This is a set of `House Member` objects representing the
   * members that were successfully saved in the database.
   * 	- `size`: The size of the `savedHouseMembers` set, which can be used to determine
   * the number of successfully added members.
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
   * deletes a member from a house based on the specified house ID and member ID,
   * returning a response entity with HTTP status code indicating the result of the operation.
   * 
   * @param houseId unique identifier of the house for which a member is to be deleted.
   * 
   * 	- `log`: a logging object used to log messages related to the function's execution.
   * 	- `houseService`: an interface or class that provides methods for managing houses.
   * 	- `houseId`: a string representing the unique identifier of a house.
   * 	- `memberId`: a string representing the unique identifier of a member to be deleted
   * from the specified house.
   * 
   * @param memberId ID of the member to be deleted from the specified house.
   * 
   * 	- `houseId`: The unique identifier for a house, which is used to identify the
   * house in the system.
   * 	- `memberId`: A unique identifier for a member within a house, which is used to
   * identify the member in the system.
   * 
   * @returns a HTTP NO_CONTENT status code indicating successful deletion of the member
   * from the house.
   * 
   * 	- The `ResponseEntity` object is built with an HTTP status code of either
   * `NO_CONTENT` (204) or `NOT_FOUND` (404).
   * 	- The `status` field of the `ResponseEntity` object contains the HTTP status code.
   * 	- The `build()` method is used to create the complete `ResponseEntity` object.
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