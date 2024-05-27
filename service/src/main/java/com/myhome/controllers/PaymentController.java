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
 * is responsible for handling requests related to payments in a community house. It
 * provides methods for listing all payments associated with a given member ID,
 * scheduling payments, and listing all scheduled payments for an admin. The controller
 * uses the `paymentService` to retrieve payment details and maps them to a standardized
 * response format using the `schedulePaymentApiMapper`. The `isAdminInGivenCommunity`
 * method is used to check if an admin is present in a given community, which is
 * necessary for listing scheduled payments.
 */
@RestController
@RequiredArgsConstructor
@Slf4j
public class PaymentController implements PaymentsApi {
  private final PaymentService paymentService;
  private final CommunityService communityService;
  private final SchedulePaymentApiMapper schedulePaymentApiMapper;

  /**
   * receives a Schedule Payment Request from an admin of a community house, validates
   * the request, and schedules payment for the requested amount from the community
   * fund to the member's account. If the admin is not the house member's admin, the
   * function returns a `HttpStatus.NOT_FOUND` response.
   * 
   * @param request SchedulePaymentRequest object containing the information necessary
   * to schedule a payment for a community house member.
   * 
   * 	- `request.getMemberId()` - The ID of the house member for whom payment is being
   * scheduled.
   * 	- `request.getAdminId()` - The ID of the community admin who is authorizing the
   * payment.
   * 	- `paymentService.getHouseMember(request.getMemberId())` - This method retrieves
   * the house member with the given ID from the payment service. If the member does
   * not exist, a `RuntimeException` is thrown.
   * 	- `communityService.findCommunityAdminById(request.getAdminId())` - This method
   * retrieves the community admin with the given ID from the community service. If the
   * admin does not exist, a `RuntimeException` is thrown.
   * 	- `isUserAdminOfCommunityHouse(houseMember.getCommunityHouse(), admin)` - This
   * method checks whether the admin is a user administrator of the community house.
   * If the admin is not an administrator, a `HttpStatus.NOT_FOUND` response is returned.
   * 
   * @returns a `SchedulePaymentResponse` object containing the scheduled payment information.
   * 
   * 	- `paymentResponse`: This is an instance of `SchedulePaymentResponse`, which
   * contains information about the scheduled payment.
   * 	- `status`: This is a field in the `paymentResponse` object that indicates the
   * status of the payment schedule. It can be one of the following values: `CREATED`,
   * indicating that the payment has been successfully scheduled; or `NOT_FOUND`,
   * indicating that the member or admin could not be found.
   * 	- `body`: This is a field in the `ResponseEntity` object that contains the detailed
   * response body, which in this case is the `paymentResponse`.
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
   * checks if a specified `User` is an admin of a `CommunityHouse`. It does so by
   * querying the community house's admins and checking if the user is present in that
   * list.
   * 
   * @param communityHouse CommunityHouse object that is being checked for an admin.
   * 
   * 	- `communityHouse`: This is an instance of the `CommunityHouse` class, which has
   * a `getCommunity()` method that returns a reference to a `Community` object.
   * 	- `getAdmins()`: This is a method of the `Community` object that returns a list
   * of `User` objects representing the community administrators.
   * 
   * @param admin User object to check if it is an administrator of the specified CommunityHouse.
   * 
   * 	- `communityHouse`: This is an object of type `CommunityHouse`, which represents
   * a community house.
   * 	- `getAdmins()`: This is a method that returns a list of objects of type `User`,
   * representing the administrators of the community house.
   * 
   * Therefore, the function checks if the input `admin` is present in the list of
   * administrators of the community house. If `admin` is not found in the list, the
   * function returns `false`. Otherwise, it returns `true`.
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
   * receives a payment ID and queries the payment service to retrieve the associated
   * payment details. It then maps the response to a `SchedulePaymentResponse` object
   * and returns it as an `ResponseEntity`.
   * 
   * @param paymentId identifier of the payment for which details are to be retrieved.
   * 
   * @returns a `ResponseEntity` object representing the payment details.
   * 
   * 	- `paymentId`: The unique identifier of the payment for which details are being
   * retrieved.
   * 	- `paymentService`: An instance of the `PaymentService` class, which provides
   * methods for interacting with the payment system.
   * 	- `schedulePaymentApiMapper`: An instance of the `SchedulePaymentApiMapper` class,
   * which maps the payment details to a `SchedulePaymentResponse` object.
   * 	- `ResponseEntity`: An instance of the `ResponseEntity` class, which represents
   * the result of the function execution. It can be either `ok` or `notFound`.
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
   * receives a member ID and retrieves the associated payments from the payment service,
   * then maps them to a list of `MemberPaymentSet` objects and returns it as a `ResponseEntity`.
   * 
   * @param memberId identifier of the house member for whom all payments are to be listed.
   * 
   * @returns a `ResponseEntity` object representing a list of member payments.
   * 
   * 	- `ResponseEntity<ListMemberPaymentsResponse>`: This is the generic type of the
   * response entity, which represents a list of member payments.
   * 	- `payments`: This is a list of `Payment` objects, which contain information about
   * each payment made by the member.
   * 	- `ok`: This is a boolean value indicating whether the list of member payments
   * was successfully retrieved. If `false`, it means that the member payment data could
   * not be fetched.
   * 
   * The function first logs a trace message to indicate that it has received a request
   * to list all member payments. Then, it retrieves the house member with the given
   * ID using the `paymentService`. Next, it calls the `getPaymentsByMember` method of
   * the `paymentService` to retrieve the list of payments for the specified member.
   * The response from this call is then mapped to a `List<Payment>` object using the
   * `map()` method.
   * 
   * Finally, the function returns an `ResponseEntity` with a status code of `ok` if
   * the list of member payments was successfully retrieved, or it returns a
   * `ResponseEntity.notFound().build()` if there was an error in retrieving the data.
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
   * retrieves all scheduled payments for a given admin in a community, checks if the
   * admin is a member of the community, and returns a response entity with the list
   * of payments and page information.
   * 
   * @param communityId community associated with the administrator whose scheduled
   * payments are to be listed.
   * 
   * @param adminId 1-based integer ID of the admin for whom scheduled payments are to
   * be listed.
   * 
   * @param pageable page request parameters, such as the page number and size, which
   * are used to retrieve a subset of the scheduled payments for the given admin ID.
   * 
   * 	- `communityId`: A string representing the community ID.
   * 	- `adminId`: A string representing the admin ID.
   * 	- `isAdminInGivenCommunity`: A boolean indicating whether the admin is present
   * in the given community.
   * 	- `paymentService`: An interface or class providing methods for retrieving payments
   * by admin ID.
   * 	- `schedulePaymentApiMapper`: An interface or class mapping API responses to Admin
   * Payment Set objects.
   * 
   * The function first checks if the admin is present in the given community, and then
   * retrieves the payments for that admin using the `paymentService`. The retrieved
   * payments are then mapped to an Admin Payment Set object using the `schedulePaymentApiMapper`,
   * and returned as part of the response.
   * 
   * @returns a `ListAdminPaymentsResponse` object containing the scheduled payments
   * for the given admin and community.
   * 
   * 	- `payments`: This is a list of `AdminPayment` objects, representing the scheduled
   * payments for the given admin in the community.
   * 	- `pageInfo`: This is a `PageInfo` object, providing information about the total
   * number of payments and the current page being returned.
   * 
   * The output of this function can be destructured as follows:
   * ```
   * ListAdminPaymentsResponse response = listAllAdminScheduledPayments(communityId,
   * adminId, Pageable pageable) {
   *   // ...
   *   List<AdminPayment> payments = response.payments;
   *   PageInfo pageInfo = response.pageInfo;
   * }
   * ```
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
   * takes a community ID and an admin ID as input, retrieves the details of the community
   * and its admins, and checks if the specified admin is present in the list of admins
   * for that community.
   * 
   * @param communityId ID of a community that is being checked for the presence of an
   * admin with the specified `adminId`.
   * 
   * @param adminId identifier of an administrator to check if they are part of the
   * specified community.
   * 
   * @returns a boolean value indicating whether the specified admin is an administrator
   * of the given community.
   * 
   * 	- The function returns a `Boolean` value indicating whether the given `adminId`
   * is an admin in the specified `communityId`.
   * 	- The function first retrieves the community details using
   * `communityService.getCommunityDetailsByIdWithAdmins(communityId)`, which returns
   * a `Future` object containing the community details along with the list of admins
   * for that community.
   * 	- The function then maps the list of admins to a stream of `Admin` objects using
   * `admins -> admins.stream()`.
   * 	- The function then maps the `Admin` objects to a stream of `UserId` objects using
   * `admins -> admins.stream().map(Admin::getUserId)`.
   * 	- Finally, the function uses `orElseThrow()` to return a `Boolean` value indicating
   * whether the given `adminId` is an admin in the specified `communityId`, or throws
   * a `RuntimeException` if the community does not exist with the given `id`.
   */
  private Boolean isAdminInGivenCommunity(String communityId, String adminId) {
    return communityService.getCommunityDetailsByIdWithAdmins(communityId)
        .map(Community::getAdmins)
        .map(admins -> admins.stream().anyMatch(admin -> admin.getUserId().equals(adminId)))
        .orElseThrow(
            () -> new RuntimeException("Community with given id not exists: " + communityId));
  }
}
