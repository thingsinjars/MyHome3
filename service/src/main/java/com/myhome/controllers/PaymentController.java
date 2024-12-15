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
 * is responsible for handling requests related to payments within a community. It
 * provides methods for listing all payments associated with a member ID, scheduling
 * payments, and retrieving payment details for a specified payment ID. The class
 * also includes a method for checking if an admin is present in a specific community
 * by querying the community details and admins based on the user ID.
 */
@RestController
@RequiredArgsConstructor
@Slf4j
public class PaymentController implements PaymentsApi {
  private final PaymentService paymentService;
  private final CommunityService communityService;
  private final SchedulePaymentApiMapper schedulePaymentApiMapper;

  /**
   * takes a `SchedulePaymentRequest` object, validates it, and then schedules a payment
   * for the community member based on the admin's approval.
   * 
   * @param request SchedulePaymentRequest object containing the information necessary
   * to schedule a payment for a member of a community.
   * 
   * * `request.getMemberId()`: The member ID of the house member who is requesting the
   * payment.
   * * `request.getAdminId()`: The admin ID of the community admin who is responsible
   * for managing payments in the community.
   * * `paymentService.getHouseMember(request.getMemberId())`: A method call to retrieve
   * the house member with the provided member ID from the payment service. If the
   * member ID does not exist, a `RuntimeException` is thrown.
   * * `communityService.findCommunityAdminById(request.getAdminId())`: A method call
   * to retrieve the community admin with the provided admin ID from the community
   * service. If the admin ID does not exist, a `RuntimeException` is thrown.
   * * `isUserAdminOfCommunityHouse(houseMember.getCommunityHouse(), admin)`: A boolean
   * method that checks whether the given admin is an admin of the same community house
   * as the house member. If the admin is not an admin of the same community house, a
   * `RuntimeException` is thrown.
   * * `schedulePaymentApiMapper.enrichSchedulePaymentRequest(request, admin, houseMember)`:
   * A method call to enrich the input request with additional information from the
   * community service and the payment service. The resulting `EnrichedSchedulePaymentRequest`
   * object is used to create a `PaymentDto` object.
   * * `schedulePaymentApiMapper.enrichedSchedulePaymentRequestToPaymentDto(paymentRequest)`:
   * A method call to transform the enriched `EnrichedSchedulePaymentRequest` object
   * into a `PaymentDto` object.
   * 
   * @returns a `SchedulePaymentResponse` object containing the scheduled payment information.
   * 
   * * `ResponseEntity`: This is the type of response entity returned by the function,
   * which has a status code and a body.
   * * `HttpStatus`: The status code of the response entity, which indicates whether
   * the request was successful (200-299) or not (400-599).
   * * `SchedulePaymentResponse`: This is the type of response entity returned by the
   * function, which contains information about the scheduled payment.
   * * `body`: The body of the response entity contains the `SchedulePaymentResponse`
   * object, which has various properties and attributes, including:
   * 	+ `paymentId`: A unique identifier for the scheduled payment.
   * 	+ `paymentDate`: The date on which the payment is scheduled to be made.
   * 	+ `amount`: The amount of the scheduled payment.
   * 	+ `description`: A brief description of the scheduled payment.
   * 	+ `status`: The status of the scheduled payment (e.g., "scheduled", "paid", etc.).
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
   * determines whether a given `User` is an admin of a specified `CommunityHouse` by
   * checking if the user is present in the community's admin list.
   * 
   * @param communityHouse Community House object that is being checked for the presence
   * of the specified `admin`.
   * 
   * * `CommunityHouse`: A class that represents a community house with various attributes
   * and methods.
   * * `getCommunity()`: A method that returns a `Community` object, which is the parent
   * class of `CommunityHouse`.
   * * `getAdmins()`: A method that returns a list of `User` objects, representing the
   * admins of the community house.
   * 
   * @param admin User object that is being checked whether they are an admin of the CommunityHouse.
   * 
   * * `CommunityHouse communityHouse`: This is an object of the class `CommunityHouse`,
   * which contains information about a house or community.
   * * `getAdmins()`: This is a method of the `CommunityHouse` object that returns a
   * list of objects representing the admins of the house or community.
   * * `contains(admin)`: This is a method of the `List` class that checks if an object
   * is present in the list. In this case, it checks if the `admin` object is present
   * in the list of admins of the house or community.
   * 
   * @returns a boolean value indicating whether the specified User is an admin of the
   * Community House.
   * 
   * * `communityHouse`: A reference to an object of the `CommunityHouse` class, which
   * represents a community house.
   * * `getCommunity()`: A method that returns a reference to the community associated
   * with the `communityHouse` object.
   * * `getAdmins()`: A method that returns a list of `User` objects representing the
   * administrators of the community.
   * * `contains()`: A method that checks if a given `User` object is present in the
   * list of admins returned by the `getAdmins()` method.
   */
  private boolean isUserAdminOfCommunityHouse(CommunityHouse communityHouse, User admin) {
    return communityHouse.getCommunity()
        .getAdmins()
        .contains(admin);
  }

  /**
   * receives a payment ID and queries the payment service for details. The response
   * is then transformed into a `SchedulePaymentResponse` object using a mapping function,
   * and finally returned as an `OK` ResponseEntity.
   * 
   * @param paymentId id of the payment for which details are requested.
   * 
   * @returns a `ResponseEntity` object representing the payment details.
   * 
   * * `paymentId`: The unique identifier for the payment being retrieved.
   * * `paymentService`: An instance of the `PaymentService` class, which provides the
   * functionality to retrieve payment details.
   * * `schedulePaymentApiMapper`: A mapping function that converts a `Payment` object
   * into a `SchedulePaymentResponse` object.
   * * `ResponseEntity`: The type of response returned by the `orElseGet` method, which
   * is either an `ok` response or a `notFound` response.
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
   * receives a member ID and queries multiple APIs to retrieve and transform data into
   * a response entity containing a list of payments for that member.
   * 
   * @param memberId id of the house member for whom all payments are to be listed.
   * 
   * @returns a `ResponseEntity` object containing a list of `MemberPayment` objects.
   * 
   * * `ResponseEntity<ListMemberPaymentsResponse>` is the generic type of the output,
   * indicating that it is an entity containing a list of member payments.
   * * `ListMemberPaymentsResponse` is a class that contains the list of member payments.
   * * `payments()` is a method of the `ListMemberPaymentsResponse` class that returns
   * the list of member payments.
   * * `ok` is a method of the `ResponseEntity` class that indicates whether the response
   * is successful or not. In this case, it is always successful.
   * * `notFound()` is a method of the `ResponseEntity` class that indicates that the
   * response represents a 404 status code (not found).
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
   * list all payments scheduled by an admin in a community. It checks if the admin is
   * in the given community, retrieves the payments for that admin, converts them to a
   * REST API response format, and returns it as a `ResponseEntity`.
   * 
   * @param communityId community that the admin belongs to, which is used to filter
   * the payments scheduled by the admin.
   * 
   * @param adminId id of the admin for whom scheduled payments are to be listed.
   * 
   * @param pageable page request parameters, such as the number of payments to display
   * per page, and is used to retrieve a subset of the payments scheduled by the admin.
   * 
   * * `communityId`: The ID of the community to filter payments for.
   * * `adminId`: The ID of the admin to filter payments by.
   * * `isAdminInGivenCommunity`: A boolean indicating whether the admin is in the given
   * community.
   * 
   * These properties are used in the function to filter and retrieve payments for the
   * specified admin in the given community, and then map them to a REST API response.
   * 
   * @returns a `ResponseEntity` object containing a `ListAdminPaymentsResponse` body
   * with the scheduled payments for the given admin and community.
   * 
   * * `payments`: A list of `AdminPayment` objects, representing the scheduled payments
   * for the given admin in the specified community.
   * * `pageInfo`: The `PageInfo` object containing information about the page of
   * payments that were retrieved, including the total number of payments and the total
   * number of pages.
   * 
   * The function first checks if the admin is present in the given community using the
   * `isAdminInGivenCommunity` method. If the admin is present, it retrieves the scheduled
   * payments for the admin using the `paymentService.getPaymentsByAdmin()` method and
   * maps them to an `AdminPaymentSet` object using the `schedulePaymentApiMapper`.
   * Finally, it returns a `ResponseEntity` object with the `payments` property set to
   * the `AdminPaymentSet` object and the `pageInfo` property set to the `PageInfo` object.
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
   * takes a community ID and an administrator ID as input, and returns a boolean value
   * indicating whether the administrator is an admin in the specified community.
   * 
   * @param communityId identifier of a community for which the method checks if an
   * admin with the provided `adminId` is present as an administrator.
   * 
   * @param adminId 12-digit user ID of the admin to check if they are an administrator
   * in the specified community.
   * 
   * @returns a `Boolean` value indicating whether the specified user is an administrator
   * in the given community.
   * 
   * * The function returns a `Boolean` value indicating whether the given admin is an
   * administrator in the provided community.
   * * The function first retrieves the community details using
   * `communityService.getCommunityDetailsByIdWithAdmins(communityId)`, which returns
   * a `List<Community>` object containing the community details and a list of admins.
   * * The function then maps the `admins` list to a stream of `Admin` objects, using
   * `map(Community::getAdmins)`.
   * * The function then maps the `Admin` objects to a stream of `UserId` values using
   * `map(admin -> admin.getUserId())`.
   * * The function finally checks if there is at least one admin with the given `UserId`,
   * using `orElseThrow()`. If no such admin is found, the function throws a
   * `RuntimeException` with the community ID as the message.
   * 
   * In summary, the function returns a `Boolean` value indicating whether an admin
   * exists in a given community based on the community details and admins list retrieved
   * from the service.
   */
  private Boolean isAdminInGivenCommunity(String communityId, String adminId) {
    return communityService.getCommunityDetailsByIdWithAdmins(communityId)
        .map(Community::getAdmins)
        .map(admins -> admins.stream().anyMatch(admin -> admin.getUserId().equals(adminId)))
        .orElseThrow(
            () -> new RuntimeException("Community with given id not exists: " + communityId));
  }
}
