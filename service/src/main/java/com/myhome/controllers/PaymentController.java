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
 * is responsible for handling payment-related requests in a housing platform. It
 * provides endpoints for scheduling payments, listing all member and admin scheduled
 * payments, and listing all payments for a given community or member. The controller
 * uses dependency injection to inject the required services, such as the PaymentService,
 * CommunityService, and SchedulePaymentApiMapper. The controller also implements
 * security constraints using the @Auth decorator to ensure only authorized users can
 * access the endpoints.
 */
@RestController
@RequiredArgsConstructor
@Slf4j
public class PaymentController implements PaymentsApi {
  private final PaymentService paymentService;
  private final CommunityService communityService;
  private final SchedulePaymentApiMapper schedulePaymentApiMapper;

  /**
   * receives a Schedule Payment Request from the API and checks if the user is an admin
   * of the community house. If so, it schedules a payment using the API and returns
   * the response. Otherwise, it returns a `NOT_FOUND` status.
   * 
   * @param request SchedulePaymentRequest object containing the details of the payment
   * to be scheduled, which is used to generate an enriched Schedule Payment Request,
   * schedule the payment, and return the resulting Schedule Payment Response.
   * 
   * 	- `request.getMemberId()`: A unique identifier for a member within a community house.
   * 	- `request.getAdminId()`: A unique identifier for an administrator within a
   * community house.
   * 	- `paymentService.getHouseMember(request.getMemberId())`: Returns a HouseMember
   * object associated with the specified member ID, or throws an exception if the
   * member does not exist.
   * 	- `communityService.findCommunityAdminById(request.getAdminId())`: Returns a User
   * object associated with the specified administrator ID, or throws an exception if
   * the administrator does not exist.
   * 	- `isUserAdminOfCommunityHouse(houseMember.getCommunityHouse(), admin)`: A boolean
   * value indicating whether the administrator is an admin of the community house
   * associated with the member.
   * 
   * @returns a `SchedulePaymentResponse` object containing the scheduled payment details.
   * 
   * 	- `ResponseEntity`: This is an instance of the `ResponseEntity` class, which
   * represents a generic response object that can hold any type of data. In this case,
   * it holds a `SchedulePaymentResponse` object.
   * 	- `status`: This is a field of type `HttpStatus`, which indicates the HTTP status
   * code of the response. In this case, it is set to `CREATED`, indicating that the
   * payment has been scheduled successfully.
   * 	- `body`: This is a field of type `SchedulePaymentResponse`, which contains the
   * details of the scheduled payment.
   * 
   * The various attributes of the `SchedulePaymentResponse` object are as follows:
   * 
   * 	- `id`: A unique identifier for the scheduled payment.
   * 	- `paymentId`: The ID of the payment that has been scheduled.
   * 	- `amount`: The amount of the payment that has been scheduled.
   * 	- `memberId`: The ID of the member who has scheduled the payment.
   * 	- `adminId`: The ID of the admin who has scheduled the payment.
   * 	- `scheduledDate`: The date and time when the payment is scheduled to be made.
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
   * checks if a given `User` is an admin of a specified `CommunityHouse`. It does so
   * by checking if the `User` is present in the community's admin list.
   * 
   * @param communityHouse CommunityHouse object that is being checked for the presence
   * of the provided `admin` parameter among its list of admins.
   * 
   * 	- `communityHouse`: A `CommunityHouse` object that represents a community house
   * with various attributes and methods for managing community members and their roles.
   * 	- `getCommunity()`: A method that returns a `Community` object, representing the
   * community associated with the `CommunityHouse`.
   * 	- `getAdmins()`: A method that returns a list of `User` objects, representing the
   * admins of the community house.
   * 
   * @param admin User object to be checked for admin status within the CommunityHouse
   * community.
   * 
   * 	- `CommunityHouse communityHouse`: This represents an object of type `CommunityHouse`,
   * which contains information about a community house.
   * 	- `getCommunity()`: This method returns an object of type `Community`, which
   * contains information about the community associated with the community house.
   * 	- `getAdmins()`: This method returns a list of objects of type `User`, which
   * represents the admins of the community.
   * 
   * @returns a boolean value indicating whether the specified user is an admin of the
   * community house.
   */
  private boolean isUserAdminOfCommunityHouse(CommunityHouse communityHouse, User admin) {
    return communityHouse.getCommunity()
        .getAdmins()
        .contains(admin);
  }

  /**
   * retrieves payment details for a given payment ID from the payment service and maps
   * them to a `SchedulePaymentResponse` object using the provided API mapper. It returns
   * a `ResponseEntity` with the payment details or an error response if not found.
   * 
   * @param paymentId id of the payment for which details are being requested.
   * 
   * @returns a `ResponseEntity` object representing the payment details or an error
   * message indicating that the payment does not exist.
   * 
   * 	- `paymentId`: The unique identifier of the payment for which details are being
   * requested.
   * 	- `paymentService`: A service responsible for retrieving payment details.
   * 	- `schedulePaymentApiMapper`: An object that maps payment details to
   * `SchedulePaymentResponse` objects.
   * 	- `ResponseEntity`: A class representing a response entity, which contains the
   * status code and body of the response.
   * 	- `ok`: The status code indicating that the request was successful and the payment
   * details were retrieved.
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
   * receives a member ID and retrieves all payments associated with that member from
   * multiple sources, maps them to a standardized response format, and returns it as
   * a `ResponseEntity`.
   * 
   * @param memberId id of the house member for whom all payments are to be listed.
   * 
   * @returns a `List Member Payments Response` object containing the list of payments
   * for the specified member ID.
   * 
   * 	- `ResponseEntity`: This is an object that represents a response entity with a
   * status code and a body. The status code indicates whether the request was successful
   * or not, and the body contains the list of member payments.
   * 	- `ok`: This is a method on the `ResponseEntity` object that returns a `ResponseEntity`
   * instance with a status code of 200 (OK).
   * 	- `notFound`: This is a method on the `ResponseEntity` object that returns a
   * `ResponseEntity` instance with a status code of 404 (Not Found).
   * 	- `payments`: This is an attribute of the `ListMemberPaymentsResponse` class that
   * contains the list of member payments.
   * 
   * The `listAllMemberPayments` function takes in a `memberId` parameter and uses it
   * to retrieve the list of payments for that member from the payment service. It then
   * maps the payment service response to a `ListMemberPaymentsResponse` object, which
   * is then returned as the output of the function.
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
   * receives a request to list all scheduled payments for an admin, retrieves the
   * payments from the payment service, and returns them in a response entity along
   * with pagination information.
   * 
   * @param communityId community for which the payments are being listed, and is used
   * to filter the list of payments to only those that are scheduled by the specified
   * admin.
   * 
   * @param adminId ID of the admin for whom scheduled payments are to be listed, and
   * is used to filter the payments returned in the response.
   * 
   * @param pageable page number and size of the payment list that the administrator
   * wants to view, which is used to retrieve the relevant payments from the database.
   * 
   * 	- `communityId`: A string representing the ID of the community to filter payments
   * for.
   * 	- `adminId`: A string representing the ID of the admin to filter payments by.
   * 	- `isAdminInGivenCommunity`: A boolean indicating whether the given admin is
   * present in the specified community.
   * 
   * The function then makes use of these properties to retrieve a list of payments
   * scheduled by the admin and return it in the response.
   * 
   * @returns a `ResponseEntity` object containing a `ListAdminPaymentsResponse` body
   * with the scheduled payments and pagination information.
   * 
   * 	- `payments`: A list of `AdminPayment` objects representing the scheduled payments
   * for the given admin.
   * 	- `pageInfo`: Represents the pagination information of the payments, including
   * the current page, total pages, and total number of payments.
   * 
   * The function first checks if the admin is in the given community by calling
   * `isAdminInGivenCommunity`. If the admin is present in the community, it retrieves
   * the scheduled payments using `paymentService.getPaymentsByAdmin()` and maps them
   * to an `AdminPaymentSet` using `schedulePaymentApiMapper`. The mapped `AdminPaymentSet`
   * is then returned as the output of the function. If the admin is not present in the
   * community, a `ResponseEntity.notFound().build()` is returned indicating that the
   * admin is not found.
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
   * checks if a user is an admin in a specific community by querying the community
   * details and admins, then filtering the admins based on the user ID, and returning
   * true if the user is an admin or false otherwise.
   * 
   * @param communityId unique identifier of a Community, which is used to retrieve the
   * details of that Community and its associated Admins.
   * 
   * @param adminId 12-digit ID of an admin user who belongs to the community, and is
   * used to filter the list of admins in the community to check if the provided user
   * ID matches any of them.
   * 
   * @returns a `Boolean` value indicating whether the specified admin is an administrator
   * of the given community.
   * 
   * 	- `communityId`: The ID of the community being checked for the admin role.
   * 	- `adminId`: The ID of the admin to be checked for membership in the community.
   * 	- `map(Function)`: The map method is used to apply a function to each element of
   * the input stream, in this case, `Community::getAdmins`. This function takes a
   * `Community` object and returns a stream of `Admin` objects.
   * 	- `map(Function<Admin, Boolean>)`: The map method is applied again to the stream
   * of `Admin` objects, this time with a function that takes an `Admin` object and
   * returns a `Boolean` value indicating whether the admin is in the given community.
   * 	- `orElseThrow()`: This method is used to provide a default value if the stream
   * of `Admin` objects is empty. If no admins are found in the community, an exception
   * is thrown with a message containing the ID of the community and the ID of the admin.
   * 
   * Overall, the function returns a `Boolean` value indicating whether the specified
   * admin is present in the given community.
   */
  private Boolean isAdminInGivenCommunity(String communityId, String adminId) {
    return communityService.getCommunityDetailsByIdWithAdmins(communityId)
        .map(Community::getAdmins)
        .map(admins -> admins.stream().anyMatch(admin -> admin.getUserId().equals(adminId)))
        .orElseThrow(
            () -> new RuntimeException("Community with given id not exists: " + communityId));
  }
}
