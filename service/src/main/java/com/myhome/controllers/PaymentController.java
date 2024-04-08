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
 * in this Java code provides RESTful API endpoints for handling payment-related
 * requests. It handles requests to create, read, update, and delete payments, as
 * well as listing all payments for a particular member or admin. The controller uses
 * dependency injection to inject required services, such as the PaymentService,
 * CommunityService, and SchedulePaymentApiMapper, which are used to handle payment-related
 * tasks. The controller also includes validation and error handling mechanisms to
 * ensure that requests are processed correctly and errors are handled appropriately.
 */
@RestController
@RequiredArgsConstructor
@Slf4j
public class PaymentController implements PaymentsApi {
  private final PaymentService paymentService;
  private final CommunityService communityService;
  private final SchedulePaymentApiMapper schedulePaymentApiMapper;

  /**
   * receives a `SchedulePaymentRequest` from an admin and checks if the user is an
   * admin of the community house. If so, it enriches the request with additional data,
   * schedules the payment through the `schedulePayment` API, and returns the response.
   * 
   * @param request SchedulePaymentRequest object containing the information necessary
   * to schedule a payment for a community house member.
   * 
   * 	- `request.getMemberId()`: The ID of the house member for whom payment is being
   * scheduled.
   * 	- `request.getAdminId()`: The ID of the community admin who is scheduling the payment.
   * 
   * @returns a `SchedulePaymentResponse` object representing the scheduled payment details.
   * 
   * 	- `ResponseEntity`: This is an instance of the `ResponseEntity` class, which
   * represents a response to a request. The status code of the response is set to
   * `HttpStatus.CREATED`, indicating that the payment has been scheduled successfully.
   * 	- `body`: This property contains the `SchedulePaymentResponse` object, which
   * provides information about the scheduled payment.
   * 	- `SchedulePaymentResponse`: This class represents the response to the `schedulePayment`
   * function, providing details about the scheduled payment, such as the payment date,
   * amount, and status.
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
   * checks if a given User is an administrator of a CommunityHouse by checking if their
   * ID exists in the community House's admin list.
   * 
   * @param communityHouse Community House object that is being checked for admin status
   * by the `isUserAdminOfCommunityHouse()` function.
   * 
   * 	- `communityHouse`: This is an instance of the `CommunityHouse` class, which
   * likely has various attributes or properties that describe its characteristics.
   * 	- `getCommunity()`: This method returns a reference to the `Community` object
   * associated with the `communityHouse` instance.
   * 	- `getAdmins()`: This method returns a list of `User` objects representing the
   * administrators of the community.
   * 
   * The function then checks if the specified `admin` is present in the list of admins
   * returned by `getAdmins()`. If it is, the function returns `true`, indicating that
   * the user is an administrator of the community; otherwise, it returns `false`.
   * 
   * @param admin User object that is being checked if they are an administrator of the
   * CommunityHouse.
   * 
   * 	- `CommunityHouse communityHouse`: This is an instance of the `CommunityHouse`
   * class, which represents a community house.
   * 	- `getAdmins()`: This method returns a list of `User` objects that represent the
   * admins of the community house.
   * 	- `contains()`: This method checks if the given `User` object is present in the
   * list of admins returned by `getAdmins()`.
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
   * retrieves payment details for a given payment ID from the payment service and maps
   * them to a `SchedulePaymentResponse` object using the `paymentToSchedulePaymentResponse`
   * mapper. It then returns a `ResponseEntity` with an `OK` status code or an empty
   * entity if the payment ID is not found.
   * 
   * @param paymentId identifier of the payment for which details are to be retrieved.
   * 
   * @returns a `ResponseEntity` object containing the payment details if the payment
   * ID exists, or a `ResponseEntity.notFound()` object otherwise.
   * 
   * 	- `paymentId`: The unique identifier for the payment being retrieved.
   * 	- `paymentToSchedulePaymentResponse`: A mapping from the payment details to a
   * `SchedulePaymentResponse` object, which contains information about the scheduled
   * payment.
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
   * retrieves all payments for a specific member ID using multiple calls to other
   * functions, transforms the response into a list of `MemberPaymentSet`, and returns
   * it as a `ResponseEntity`.
   * 
   * @param memberId House member for whom all payments should be listed.
   * 
   * @returns a `ResponseEntity` object containing a `List Member Payments Response`
   * with the list of payments for the specified member.
   * 
   * 	- `ResponseEntity<ListMemberPaymentsResponse>`: This is the generic type of the
   * response entity, which represents a list of member payments.
   * 	- `List Member Payments Response`: This is a nested class within `ListMemberPaymentsResponse`,
   * which contains the list of member payments.
   * 	- `payments()`: This method returns a list of `Payment` objects, which represent
   * the member payments.
   * 	- `ok()`: This method builds an `ResponseEntity` with a status code of 200 (OK)
   * and a body containing the list of member payments.
   * 	- `notFound()`: This method builds an `ResponseEntity` with a status code of 404
   * (Not Found) and a body containing an error message indicating that no member
   * payments could be found.
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
   * receives a community ID, an admin ID, and a pageable parameter, and lists all
   * payments scheduled by the admin with that ID within the given community.
   * 
   * @param communityId community for which the admin is searching scheduled payments.
   * 
   * @param adminId ID of the admin for whom scheduled payments are to be listed.
   * 
   * @param pageable pagination information for the payments to be listed, allowing the
   * function to retrieve only the requested portion of the list.
   * 
   * 	- `communityId`: A string representing the ID of the community for which the
   * payments are being listed.
   * 	- `adminId`: A string representing the ID of the admin for whom the payments are
   * being listed.
   * 	- `Pageable`: An object representing a page of payments, with properties such as
   * `pageNumber`, `pageSize`, and `totalElements`.
   * 
   * @returns a `ResponseEntity` object containing a `ListAdminPaymentsResponse` object
   * with scheduled payments for the provided admin ID.
   * 
   * 	- `payments`: A list of `AdminPayment` objects, representing the scheduled payments
   * for the admin with the given ID.
   * 	- `pageInfo`: Represents the pagination information for the payments, including
   * the total number of payments and the current page being displayed.
   * 	- `response`: The response entity with the list of payments and pagination information.
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
   * retrieves the details of a community and its admins, filters the list of admins
   * based on the provided admin ID, and returns a boolean value indicating whether the
   * specified admin is an admin in the given community.
   * 
   * @param communityId ID of the community for which admin status is to be checked.
   * 
   * @param adminId ID of the administrator being checked for membership in the specified
   * community.
   * 
   * @returns a boolean value indicating whether the specified admin is an administrator
   * of the given community.
   * 
   * 	- The function returns a `Boolean` value indicating whether an admin with the
   * given `adminId` exists in the specified `communityId`.
   * 	- If there is no admin with the given `adminId` in the community, the function
   * will throw a `RuntimeException`.
   * 	- The function first retrieves the details of the community with the given
   * `communityId`, using the `communityService.getCommunityDetailsByIdWithAdmins()` method.
   * 	- It then maps the admins of the community to a stream of admins, using the `map()`
   * method.
   * 	- Finally, it maps the stream of admins to a boolean value indicating whether an
   * admin with the given `adminId` exists in the community, using the `stream().anyMatch()`
   * method. If no such admin exists, the function returns `false`. Otherwise, it returns
   * `true`.
   */
  private Boolean isAdminInGivenCommunity(String communityId, String adminId) {
    return communityService.getCommunityDetailsByIdWithAdmins(communityId)
        .map(Community::getAdmins)
        .map(admins -> admins.stream().anyMatch(admin -> admin.getUserId().equals(adminId)))
        .orElseThrow(
            () -> new RuntimeException("Community with given id not exists: " + communityId));
  }
}
