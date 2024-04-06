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

import com.myhome.api.PaymentsApi;
import com.myhome.controllers.dto.PaymentDto;
import com.myhome.controllers.mapper.SchedulePaymentApiMapper;
import com.myhome.controllers.request.EnrichedSchedulePaymentRequest;
import com.myhome.domain.Community;
import com.myhome.domain.CommunityHouse;
import com.myhome.domain.HouseMember;
import com.myhome.domain.Payment;
import com.myhome.domain.User;
import com.myhome.model.AdminPayment;
import com.myhome.model.ListAdminPaymentsResponse;
import com.myhome.model.ListMemberPaymentsResponse;
import com.myhome.model.SchedulePaymentRequest;
import com.myhome.model.SchedulePaymentResponse;
import com.myhome.services.CommunityService;
import com.myhome.services.PaymentService;
import com.myhome.utils.PageInfo;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import javax.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

/**
 * REST Controller which provides endpoints for managing payments
 */
/**
 * TODO
 */
@RestController
@RequiredArgsConstructor
@Slf4j
public class PaymentController implements PaymentsApi {
  private final PaymentService paymentService;
  private final CommunityService communityService;
  private final SchedulePaymentApiMapper schedulePaymentApiMapper;

  /**
   * receives a request to schedule a payment, checks if the user is an admin of the
   * community and the house, then schedules the payment using the `schedulePayment`
   * API and returns the response.
   * 
   * @param request SchedulePaymentRequest object containing information necessary for
   * scheduling a payment, which is passed through the function's methods to generate
   * a PaymentDto and then a SchedulePaymentResponse.
   * 
   * 	- `request.getMemberId()`: The ID of the house member who is requesting the payment.
   * 	- `request.getAdminId()`: The ID of the community admin who is responsible for
   * processing the payment.
   * 	- `paymentService.getHouseMember(request.getMemberId())`: This method retrieves
   * a `HouseMember` object from the service, using the `request.getMemberId()` as the
   * parameter. If the house member with the given ID does not exist, a `RuntimeException`
   * is thrown.
   * 	- `communityService.findCommunityAdminById(request.getAdminId())`: This method
   * retrieves a `User` object from the service, using the `request.getAdminId()` as
   * the parameter. If the admin with the given ID does not exist, a `RuntimeException`
   * is thrown.
   * 	- `isUserAdminOfCommunityHouse(houseMember.getCommunityHouse(), admin)`: This
   * method checks whether the admin is an admin of the community house associated with
   * the request. If the admin is not an admin of the community house, the method returns
   * `false`.
   * 
   * The rest of the function proceeds to schedule the payment using the enriched
   * `SchedulePaymentRequest`, and finally returns a `ResponseEntity` with the scheduled
   * payment response.
   * 
   * @returns a `SchedulePaymentResponse` object containing the scheduled payment details.
   * 
   * 	- `ResponseEntity`: This is a class that represents an HTTP response with a status
   * code and a body. In this case, the status code is `HttpStatus.CREATED`, which
   * indicates that the payment has been scheduled successfully.
   * 	- `body`: This is the body of the response, which contains the `SchedulePaymentResponse`
   * object.
   * 	- `SchedulePaymentResponse`: This class represents the response to the API call,
   * containing information about the scheduled payment. The properties of this class
   * include:
   * 	+ `status`: This indicates the status of the payment schedule, such as "scheduled"
   * or "cancelled".
   * 	+ `message`: This is a message indicating the result of the payment schedule,
   * such as "Payment scheduled successfully" or "Payment could not be scheduled".
   * 	+ `paymentId`: This is the ID of the payment that was scheduled.
   * 	+ `createdAt`: This is the date and time when the payment was scheduled.
   */
  @Override
  public ResponseEntity<SchedulePaymentResponse> schedulePayment(@Valid
      SchedulePaymentRequest request) {
    log.trace("Received schedule payment request");

    HouseMember houseMember = paymentService.getHouseMember(request.getMemberId())
        .orElseThrow(() -> new RuntimeException(
            "House member with given id not exists: " + request.getMemberId()));
    User admin = communityService.findCommunityAdminById(request.getAdminId())
        .orElseThrow(
            () -> new RuntimeException("Admin with given id not exists: " + request.getAdminId()));

    if (isUserAdminOfCommunityHouse(houseMember.getCommunityHouse(), admin)) {
      final EnrichedSchedulePaymentRequest paymentRequest =
          schedulePaymentApiMapper.enrichSchedulePaymentRequest(request, admin, houseMember);
      final PaymentDto paymentDto =
          schedulePaymentApiMapper.enrichedSchedulePaymentRequestToPaymentDto(paymentRequest);
      final PaymentDto processedPayment = paymentService.schedulePayment(paymentDto);
      final SchedulePaymentResponse paymentResponse =
          schedulePaymentApiMapper.paymentToSchedulePaymentResponse(processedPayment);
      return ResponseEntity.status(HttpStatus.CREATED).body(paymentResponse);
    }

    return ResponseEntity.notFound().build();
  }

  /**
   * checks if a user is an admin of a community house by checking if their username
   * is present in the community's admin list.
   * 
   * @param communityHouse community house that is being checked for the administrator
   * role of the given `admin`.
   * 
   * 1/ `communityHouse`: A `CommunityHouse` object that represents the community house
   * being checked for the user's admin status.
   * 2/ `getCommunity()`: A method that returns a `Community` object, which represents
   * the community associated with the `CommunityHouse`.
   * 3/ `getAdmins()`: A method that returns a `List<User>` object, which contains the
   * list of admins for the community associated with the `CommunityHouse`.
   * 
   * @param admin User object that the function checks if it is an administrator of the
   * CommunityHouse.
   * 
   * 	- `CommunityHouse communityHouse`: The class representing a community house, which
   * contains information about the community and its members.
   * 	- `getAdmins()`: A method that returns a list of users who are admins of the
   * community house.
   * 
   * @returns a boolean value indicating whether the specified user is an administrator
   * of the community house.
   */
  private boolean isUserAdminOfCommunityHouse(CommunityHouse communityHouse, User admin) {
    return communityHouse.getCommunity()
        .getAdmins()
        .contains(admin);
  }

  /**
   * receives a payment ID and retrieves the associated payment details from the payment
   * service using the `getPaymentDetails` method. It then maps the payment details to
   * a `SchedulePaymentResponse` object using the `paymentToSchedulePaymentResponse`
   * function and returns a `ResponseEntity` with an `OK` status code if the payment
   * details are found, or an `NOT_FOUND` status code otherwise.
   * 
   * @param paymentId id of the payment for which details are being requested.
   * 
   * 	- `log.trace`: Emits a trace message indicating that the method has received a
   * request to retrieve details about a payment with the specified `paymentId`.
   * 	- `paymentService.getPaymentDetails(paymentId)`: Calls the `getPaymentDetails()`
   * method of the `paymentService` object, passing in the `paymentId` as a parameter.
   * This method retrieves the details of a specific payment based on its ID.
   * 	- `schedulePaymentApiMapper.paymentToSchedulePaymentResponse(payment)`: Maps the
   * deserialized `payment` object to a `SchedulePaymentResponse` object using the
   * `schedulePaymentApiMapper` object. This mapping is necessary because the `payment`
   * object does not directly correspond to the expected response type of the API.
   * 	- `map(ResponseEntity::ok)`: Maps the result of the previous step to an instance
   * of `ResponseEntity` with a status code of 200 (OK).
   * 	- `orElseGet(() -> ResponseEntity.notFound().build())`: Provides an alternative
   * response in case the `paymentService.getPaymentDetails()` method does not return
   * a valid result. In this case, the alternative response is a `ResponseEntity` with
   * a status code of 404 (NOT FOUND) and a built message indicating that the payment
   * with the specified ID could not be found.
   * 
   * @returns a `ResponseEntity` object containing the details of the specified payment.
   * 
   * 	- `paymentId`: The identifier of the payment for which details are being requested.
   * 	- `paymentService`: A service used to retrieve payment details.
   * 	- `schedulePaymentApiMapper`: An API mapper used to transform payment details
   * into a `SchedulePaymentResponse` object.
   * 	- `ResponseEntity`: A class representing a response entity, which is used to wrap
   * the returned payment details in an HTTP response.
   * 
   * The function returns a `ResponseEntity` object with an `ok` status code if the
   * payment details are successfully retrieved, or an `notFound` status code if there
   * is no matching payment.
   */
  @Override
  public ResponseEntity<SchedulePaymentResponse> listPaymentDetails(String paymentId) {
    log.trace("Received request to get details about a payment with id[{}]", paymentId);

    return paymentService.getPaymentDetails(paymentId)
        .map(schedulePaymentApiMapper::paymentToSchedulePaymentResponse)
        .map(ResponseEntity::ok)
        .orElseGet(() -> ResponseEntity.notFound().build());
  }

  /**
   * retrieves a member's payments from the payment service and maps them to a response
   * entity using the `memberPaymentSetToRestApiResponseMemberPaymentSet` method.
   * 
   * @param memberId unique identifier for the member whose payments are to be listed.
   * 
   * 	- `log.trace()`: This line logs a trace message with the ID of the member for
   * whom payments are being listed.
   * 	- `paymentService.getHouseMember(memberId)`: This line retrieves the house member
   * associated with the given `memberId`. The method returns an Optional<HouseMember>,
   * which contains the house member if found, or an empty optional if not.
   * 	- ` paymentService.getPaymentsByMember(memberId)`: This line retrieves all payments
   * made by the house member associated with the given `memberId`. The method returns
   * an Optional<List<Payment>>, which contains the list of payments if found, or an
   * empty optional if not.
   * 	- `schedulePaymentApiMapper.memberPaymentSetToRestApiResponseMemberPaymentSet()`:
   * This line maps the list of payments to a response entity with the appropriate
   * structure for the API. The method takes the list of payments as input and returns
   * a ResponseEntity containing the mapped data.
   * 	- `new List Member Payments Response().payments(memberPayments)`: This line
   * constructs a new instance of the `ListMemberPaymentsResponse` class, passing in
   * the list of payments retrieved from the API. The response entity contains the list
   * of payments.
   * 	- `map(ResponseEntity::ok)`: This line maps the response entity to an HTTP 200
   * status code (OK), indicating that the list of payments was successfully retrieved.
   * 	- `orElseGet(() -> ResponseEntity.notFound().build())`: This line provides a
   * default response if the previous mapping fails, indicating that the house member
   * with the given ID could not be found. The response entity contains a 404 status
   * code (Not Found).
   * 
   * @returns a `List Member Payments Response` containing the member's payments.
   * 
   * 	- `ResponseEntity`: This is the primary output of the function, representing a
   * response entity with an `ok` status and a `ListMemberPaymentsResponse` body
   * containing the list of member payments.
   * 	- `ListMemberPaymentsResponse`: This class represents the response body of the
   * function, which contains a list of `MemberPayment` objects. Each `MemberPayment`
   * object in the list represents a payment made by a house member.
   * 	- `payments()`: This method returns a list of `MemberPayment` objects, which are
   * the payments made by the house members.
   * 	- `memberId`: This is the parameter passed to the function, representing the ID
   * of the house member for whom the payments are being listed.
   */
  @Override
  public ResponseEntity<ListMemberPaymentsResponse> listAllMemberPayments(String memberId) {
    log.trace("Received request to list all the payments for the house member with id[{}]",
        memberId);

    return paymentService.getHouseMember(memberId)
        .map(payments -> paymentService.getPaymentsByMember(memberId))
        .map(schedulePaymentApiMapper::memberPaymentSetToRestApiResponseMemberPaymentSet)
        .map(memberPayments -> new ListMemberPaymentsResponse().payments(memberPayments))
        .map(ResponseEntity::ok)
        .orElseGet(() -> ResponseEntity.notFound().build());
  }

  /**
   * retrieves a list of payments scheduled by an admin with a given ID, checks if the
   * admin is in the community, and returns the list of payments in a REST API response.
   * 
   * @param communityId community for which the scheduled payments are being listed,
   * and is used to filter the payments returned in the response.
   * 
   * 	- `communityId`: This is an String property that represents the community ID for
   * which the payments are being listed.
   * 
   * @param adminId ID of the admin who is authorized to view and manage payments within
   * a specific community, and it is used to filter the list of payments returned in
   * the response.
   * 
   * 	- `communityId`: The ID of the community that the admin belongs to.
   * 	- `adminId`: The ID of the admin who is requesting the list of scheduled payments.
   * 	- `pageable`: A Pageable object used to paginate the results.
   * 
   * @param pageable page number and size of the payment list that the admin wants to
   * see, allowing the function to paginate the list of payments accordingly.
   * 
   * 	- `communityId`: The ID of the community for which the payments are to be listed.
   * 	- `adminId`: The ID of the admin for whom the payments are to be listed.
   * 	- `isAdminInGivenCommunity`: A boolean indicating whether the admin with the given
   * ID is present in the specified community.
   * 	- `paymentService`: An instance of a payment service used to retrieve the payments
   * scheduled by the admin.
   * 	- `pageable`: A `Pageable` object representing the pagination information for the
   * payments.
   * 
   * @returns a `ResponseEntity` with a list of `AdminPayment` objects and a `PageInfo`
   * object.
   * 
   * 	- `payments`: A list of `AdminPayment` objects representing the scheduled payments
   * for the given admin and community.
   * 	- `pageInfo`: Contains information about the page of payments, including the total
   * number of payments and the current page number.
   * 
   * The function returns a `ResponseEntity` object with the list of scheduled payments
   * in the `body` field, and a `StatusCode` indicating whether the request was successful
   * or not in the `status` field.
   */
  @Override
  public ResponseEntity<ListAdminPaymentsResponse> listAllAdminScheduledPayments(
      String communityId, String adminId, Pageable pageable) {
    log.trace("Received request to list all the payments scheduled by the admin with id[{}]",
        adminId);

    final boolean isAdminInGivenCommunity = isAdminInGivenCommunity(communityId, adminId);

    if (isAdminInGivenCommunity) {
      final Page<Payment> paymentsForAdmin = paymentService.getPaymentsByAdmin(adminId, pageable);
      final List<Payment> payments = paymentsForAdmin.getContent();
      final Set<AdminPayment> adminPayments =
          schedulePaymentApiMapper.adminPaymentSetToRestApiResponseAdminPaymentSet(
              new HashSet<>(payments));
      final ListAdminPaymentsResponse response = new ListAdminPaymentsResponse()
          .payments(adminPayments)
          .pageInfo(PageInfo.of(pageable, paymentsForAdmin));
      return ResponseEntity.ok().body(response);
    }

    return ResponseEntity.notFound().build();
  }

  /**
   * checks if a user is an administrator in a specific community by retrieving the
   * community details and admins, then filtering the admins based on the user ID, and
   * finally returning whether the user is an admin or not.
   * 
   * @param communityId ID of a community that is being checked for an admin with the
   * provided `adminId`.
   * 
   * 	- `communityId`: A string that represents the ID of a community. This property
   * is used to identify the community in question.
   * 
   * The function first retrieves the details of the community with the given ID using
   * `communityService.getCommunityDetailsByIdWithAdmins()`.
   * 
   * Next, it maps the admins of the community to a stream of Boolean values using
   * `map(Community::getAdmins)`. This is done to filter out any non-admin users from
   * the community.
   * 
   * Finally, the function checks if there are any admin users in the community with
   * the given ID and user ID using `map(admins -> admins.stream().anyMatch(admin ->
   * admin.getUserId().equals(adminId))).orElseThrow()`. If no such admin is found, a
   * `RuntimeException` is thrown.
   * 
   * @param adminId 12-digit unique identifier of an administrator for a specific community.
   * 
   * 	- `adminId`: A string representing the ID of an administrator in the community.
   * 	- `getUserId()`: Returns the user ID of the administrator.
   * 
   * @returns a boolean value indicating whether the specified admin is an administrator
   * of the given community.
   * 
   * 	- The function returns a `Boolean` value indicating whether an admin with the
   * given `adminId` exists in the specified `communityId`.
   * 	- The function first retrieves the community details along with its admins using
   * the `communityService.getCommunityDetailsByIdWithAdmins()` method.
   * 	- It then maps the admins to a stream of admins, using the `map()` method.
   * 	- The stream is then filtered using the `anyMatch()` method to check if any admin
   * with the given `adminId` exists in the community.
   * 	- If no admin is found, the function throws a `RuntimeException`.
   * 
   * In summary, the function returns `True` if an admin with the given `adminId` exists
   * in the specified `communityId`, and `False` otherwise.
   */
  private Boolean isAdminInGivenCommunity(String communityId, String adminId) {
    return communityService.getCommunityDetailsByIdWithAdmins(communityId)
        .map(Community::getAdmins)
        .map(admins -> admins.stream().anyMatch(admin -> admin.getUserId().equals(adminId)))
        .orElseThrow(
            () -> new RuntimeException("Community with given id not exists: " + communityId));
  }
}
