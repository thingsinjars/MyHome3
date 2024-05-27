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
   * Takes a `SchedulePaymentRequest` object, validates it, and then schedules a payment
   * for the provided member using the community admin. If the member is not found or
   * the admin is not an admin of the community house, it returns a
   * `ResponseEntity.notFound().build()`. Otherwise, it creates a `PaymentDto` object
   * from the enriched request and schedules the payment, creating a `SchedulePaymentResponse`.
   * 
   * @param request SchedulePaymentRequest object that contains the details of the
   * payment to be scheduled, including the member ID and the admin ID.
   * 
   * @returns a `SchedulePaymentResponse` object containing the scheduled payment information.
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
   * Determines if a given user is an administrator of a community house by checking
   * if their username is present in the community house's admin list.
   * 
   * @param communityHouse Community House for which the admin status is being checked.
   * 
   * @param admin User object to check if they are an administrator of the Community House.
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
   * Receives a payment ID and uses it to retrieve payment details from the service,
   * mapping the response to a `SchedulePaymentResponse` object and returning it as an
   * `ResponseEntity`.
   * 
   * @param paymentId identifier of the payment for which details are to be retrieved.
   * 
   * @returns a `ResponseEntity` object containing the payment details.
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
   * Retrieves all payments for a specific house member using the payment service and
   * maps them to a `ListMemberPaymentsResponse` object, returning it as an `OK` response
   * or an empty `Not Found` response if no payments are found.
   * 
   * @param memberId ID of the member for which all payments should be listed.
   * 
   * @returns a `ResponseEntity<ListMemberPaymentsResponse>` object containing the list
   * of member payments.
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
   * Receives a request to list all payments scheduled by an admin, checks if the admin
   * is present in the given community, retrieves the payments for the specified admin
   * using the payment service, maps them to a REST API response, and returns the response.
   * 
   * @param communityId ID of the community for which the payments are scheduled, and
   * is used to filter the payments returned in the response.
   * 
   * @param adminId identifier of the admin whose scheduled payments are to be listed.
   * 
   * @param pageable pagination information for the list of payments to be retrieved,
   * allowing the method to fetch only the requested subset of payments.
   * 
   * @returns a `ResponseEntity` object containing a `ListAdminPaymentsResponse` body
   * with the scheduled payments and pagination metadata.
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
   * Checks if a user is an admin in a specific community by querying the community
   * details and admins, and then checking if the user id matches that of an admin.
   * 
   * @param communityId unique identifier of the community for which the admin status
   * is to be checked.
   * 
   * @param adminId ID of an admin user in the community to be checked for membership.
   * 
   * @returns a boolean value indicating whether the specified admin is an administrator
   * of the given community.
   */
  private Boolean isAdminInGivenCommunity(String communityId, String adminId) {
    return communityService.getCommunityDetailsByIdWithAdmins(communityId)
        .map(Community::getAdmins)
        .map(admins -> admins.stream().anyMatch(admin -> admin.getUserId().equals(adminId)))
        .orElseThrow(
            () -> new RuntimeException("Community with given id not exists: " + communityId));
  }
}
