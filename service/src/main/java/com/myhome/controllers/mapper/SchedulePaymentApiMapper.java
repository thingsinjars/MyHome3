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

package com.myhome.controllers.mapper;

import com.myhome.controllers.dto.PaymentDto;
import com.myhome.controllers.dto.UserDto;
import com.myhome.controllers.request.EnrichedSchedulePaymentRequest;
import com.myhome.domain.Community;
import com.myhome.domain.HouseMember;
import com.myhome.domain.Payment;
import com.myhome.domain.User;
import com.myhome.model.AdminPayment;
import com.myhome.model.HouseMemberDto;
import com.myhome.model.MemberPayment;
import com.myhome.model.SchedulePaymentRequest;
import com.myhome.model.SchedulePaymentResponse;
import java.util.Set;
import java.util.stream.Collectors;

import org.mapstruct.AfterMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.Mappings;
import org.mapstruct.Named;

/**
 * provides a mapping between various request and response objects for schedule
 * payments, including PaymentDto, EnrichedSchedulePaymentRequest, SchedulePaymentResponse,
 * UserDto, HouseMemberDto, and others.
 */
@Mapper
public interface SchedulePaymentApiMapper {

  /**
   * converts a `String` representing an admin ID into a `UserDto` object with the
   * admin's ID as its `userId`.
   * 
   * @param adminId ID of an administrator for which an `AdminDto` object is to be constructed.
   * 
   * 	- `userId`: This field contains the user ID of the admin in question.
   * 
   * @returns a `UserDto` object with a `userId` field set to the given `adminId`.
   * 
   * 	- `userId`: A String attribute that contains the admin ID passed as an argument
   * to the function.
   * 	- Built using a `UserDto.builder()` method, which creates a new instance of the
   * `UserDto` class with the specified attributes.
   */
  @Named("adminIdToAdmin")
  static UserDto adminIdToAdminDto(String adminId) {
    return UserDto.builder()
        .userId(adminId)
        .build();
  }

  /**
   * converts a `memberId` string into an instance of `HouseMemberDto`.
   * 
   * @param memberId unique identifier of a member within the scope of the `HouseMemberDto`
   * class.
   * 
   * 	- `memberId`: A String attribute representing the unique identifier for a member
   * in the House.
   * 
   * @returns a `HouseMemberDto` object containing the `memberId` property.
   * 
   * 	- memberId: This is a String attribute that contains the member ID.
   */
  @Named("memberIdToMember")
  static HouseMemberDto memberIdToMemberDto(String memberId) {
    return new HouseMemberDto()
        .memberId(memberId);
  }

  /**
   * converts a `UserDto` object to its corresponding `UserId`.
   * 
   * @param userDto user object containing the user ID, which is returned as the output
   * of the `adminToAdminId` function.
   * 
   * 	- `UserId`: This field contains the user ID of the administrative user.
   * 
   * @returns a string representing the user ID of the admin entity.
   * 
   * 	- The output is a string representing a user ID.
   * 	- The user ID is obtained by using the `getUserId()` method of the `UserDto`
   * object passed as an argument to the function.
   * 	- The `UserDto` object contains information about a user, including their ID.
   * 	- The `adminToAdminId` function extracts the user ID from the `UserDto` object
   * and returns it as a string.
   */
  @Named("adminToAdminId")
  static String adminToAdminId(UserDto userDto) {
    return userDto.getUserId();
  }

  /**
   * maps a `HouseMemberDto` object to its corresponding member ID.
   * 
   * @param houseMemberDto House Member object containing information about a member
   * of a household, which is passed to the function to retrieve their member ID.
   * 
   * 	- `getMemberId()`: Returns the member ID of the House Member.
   * 
   * @returns a string representing the member ID of the inputted House Member DTO.
   * 
   * 	- The output is a string containing the member ID of the inputted House Member Dto.
   * 	- The string consists of a unique identifier for the member within the context
   * of the House application.
   * 	- The member ID serves as a reference point for interactions between members
   * within the application, enabling efficient communication and data exchange.
   */
  @Named("memberToMemberId")
  static String memberToMemberId(HouseMemberDto houseMemberDto) {
    return houseMemberDto.getMemberId();
  }

  @Mappings({
      @Mapping(source = "adminId", target = "admin", qualifiedByName = "adminIdToAdmin"),
      @Mapping(source = "memberId", target = "member", qualifiedByName = "memberIdToMember")
  })
  PaymentDto schedulePaymentRequestToPaymentDto(SchedulePaymentRequest schedulePaymentRequest);

  PaymentDto enrichedSchedulePaymentRequestToPaymentDto(
      EnrichedSchedulePaymentRequest enrichedSchedulePaymentRequest);

  /**
   * maps the user details of a `PaymentRequest` to its corresponding administrator and
   * member fields in a `PaymentDto`.
   * 
   * @param paymentDto PaymentDto instance that will be populated with user details
   * from the enriched schedule payment request.
   * 
   * 	- `PaymentDto.PaymentDtoBuilder`: This is an instance of a builder class for the
   * `PaymentDto` class, which is annotated with `@Builder`. The `PaymentDtoBuilder`
   * allows for the configuration and modification of the `PaymentDto` instance through
   * method calls.
   * 	- `EnrichedSchedulePaymentRequest`: This is an instance of a class that contains
   * enriched payment request details. It has various attributes, including `member`
   * and `admin`, which are used in the `setUserFields` function.
   * 
   * @param enrichedSchedulePaymentRequest enriched payment request with additional
   * user details, which are then mapped to admin and member fields in the `PaymentDto`
   * object using the `setUserFields` method.
   * 
   * 	- `paymentDto`: The `PaymentDto` object that is being populated with user details
   * from the input.
   * 	- `paymentDto.member`: A reference to the `Member` property of the `PaymentDto`,
   * which will be set to an enriched version of the `house Member` property of the input.
   * 	- `paymentDto.admin`: A reference to the `Admin` property of the `PaymentDto`,
   * which will be set to an enriched version of the `house Admin` property of the input.
   * 
   * The `enrichedSchedulePaymentRequest` object contains various properties and
   * attributes, including:
   * 
   * 	- `member`: A `Member` object representing the user's details for the payment request.
   * 	- `admin`: An `Admin` object representing the user's details for the payment request.
   */
  @AfterMapping
  default void setUserFields(@MappingTarget PaymentDto.PaymentDtoBuilder paymentDto, EnrichedSchedulePaymentRequest enrichedSchedulePaymentRequest) {
    // MapStruct and Lombok requires you to pass in the Builder instance of the class if that class is annotated with @Builder, or else the AfterMapping method is not used.
    // required to use AfterMapping to convert the user details of the payment request to admin, and same with house member
    paymentDto.member(getEnrichedRequestMember(enrichedSchedulePaymentRequest));
    paymentDto.admin(getEnrichedRequestAdmin(enrichedSchedulePaymentRequest));
  }

  Set<MemberPayment> memberPaymentSetToRestApiResponseMemberPaymentSet(
      Set<Payment> memberPaymentSet);

  @Mapping(target = "memberId", expression = "java(payment.getMember().getMemberId())")
  MemberPayment paymentToMemberPayment(Payment payment);

  Set<AdminPayment> adminPaymentSetToRestApiResponseAdminPaymentSet(
      Set<Payment> memberPaymentSet);

  @Mapping(target = "adminId", expression = "java(payment.getAdmin().getUserId())")
  AdminPayment paymentToAdminPayment(Payment payment);

  @Mappings({
      @Mapping(source = "admin", target = "adminId", qualifiedByName = "adminToAdminId"),
      @Mapping(source = "member", target = "memberId", qualifiedByName = "memberToMemberId")
  })
  SchedulePaymentResponse paymentToSchedulePaymentResponse(PaymentDto payment);

  /**
   * enriches a `SchedulePaymentRequest` object with additional information, such as
   * community IDs, admin and member details, and additional fields for recurring payments.
   * 
   * @param request Schedule Payment Request to be enriched, providing its type,
   * description, recurrence, charge, due date, and other relevant information.
   * 
   * 	- `type`: The type of payment request, which could be either "schedule" or "one-time".
   * 	- `description`: A brief description of the payment request.
   * 	- `isRecurring`: Whether the payment is recurring or not.
   * 	- `charge`: The amount of the payment request.
   * 	- `dueDate`: The date by which the payment is due.
   * 	- `adminId`: The ID of the administrator who created the payment request.
   * 	- `adminName`: The name of the administrator who created the payment request.
   * 	- `adminEmail`: The email address of the administrator who created the payment request.
   * 	- `encryptedPassword`: The encrypted password of the administrator who created
   * the payment request.
   * 	- `communityIds`: A set of community IDs that the payment request is related to.
   * 	- `memberId`: The ID of the member whose payment request this is.
   * 	- `houseMemberDocumentFilename`: The filename of the House Member document
   * associated with the member.
   * 	- `memberName`: The name of the member whose payment request this is.
   * 	- `communityHouseId`: The ID of the community house associated with the member.
   * 
   * @param admin user who is authorizing the payment request, providing their ID, name,
   * email address, and encrypted password to enrich the request with additional information.
   * 
   * 	- `getCommunities()` returns a stream of `Community` objects, which represent the
   * communities that the admin is a member of.
   * 	- `map()` is used to transform each `Community` object into a `String` representing
   * its community ID.
   * 	- `collect()` is used to collect the transformed strings into a set called `communityIds`.
   * 	- `getAdminId()` returns the ID of the admin who created the schedule payment request.
   * 	- `getId()` returns the ID of the admin who created the schedule payment request.
   * 	- `getName()` returns the name of the admin who created the schedule payment request.
   * 	- `getEmail()` returns the email address of the admin who created the schedule
   * payment request.
   * 	- `getEncryptedPassword()` returns the encrypted password of the admin who created
   * the schedule payment request.
   * 	- `getCommunityIds()` returns a set of community IDs that are associated with the
   * admin who created the schedule payment request.
   * 	- `getMemberId()` returns the ID of the member whose payment request is being enriched.
   * 	- `getHouseMemberDocument()` returns a `Document` object representing the member's
   * house membership document, or an empty string if no document is available.
   * 
   * @param member HouseMember object associated with the Schedule Payment Request,
   * providing additional information such as the member's ID and community house ID.
   * 
   * 	- `member.getMemberId()`: The unique identifier of the member in the community.
   * 	- `member.getId()`: The ID of the member.
   * 	- `member.getHouseMemberDocument() != null ? member.getHouseMemberDocument().getDocumentFilename()
   * : ""`: The filename of the document associated with the member's household, if available.
   * 	- `member.getName()`: The name of the member.
   * 	- `member.getCommunityHouse() != null ? member.getCommunityHouse().getHouseId()
   * : """: The ID of the community house associated with the member.
   * 
   * @returns an enriched `SchedulePaymentRequest` object containing additional information.
   * 
   * 	- `type`: The type of payment request, which can be either "ONE_TIME" or "RECURRING".
   * 	- `description`: A brief description of the payment request.
   * 	- `isRecurring`: Indicates whether the payment request is recurring or not.
   * 	- `charge`: The amount of the payment request.
   * 	- `dueDate`: The date by which the payment must be made.
   * 	- `adminId`: The ID of the admin who created the payment request.
   * 	- `adminName`: The name of the admin who created the payment request.
   * 	- `adminEmail`: The email address of the admin who created the payment request.
   * 	- `encryptedPassword`: The encrypted password of the admin who created the payment
   * request.
   * 	- `communityIds`: A set of community IDs associated with the payment request.
   * 	- `memberId`: The ID of the member who made the payment request.
   * 	- `houseMemberDocumentFilename`: The filename of the House Member document
   * associated with the payment request (only if it exists).
   * 	- `memberName`: The name of the member who made the payment request.
   * 	- `communityHouseId`: The ID of the community house associated with the payment
   * request (only if it exists).
   */
  default EnrichedSchedulePaymentRequest enrichSchedulePaymentRequest(
      SchedulePaymentRequest request, User admin, HouseMember member) {
    Set<String> communityIds = admin.getCommunities()
        .stream()
        .map(Community::getCommunityId)
        .collect(Collectors.toSet());
    return new EnrichedSchedulePaymentRequest(request.getType(),
        request.getDescription(),
        request.isRecurring(),
        request.getCharge(),
        request.getDueDate(),
        request.getAdminId(),
        admin.getId(),
        admin.getName(),
        admin.getEmail(),
        admin.getEncryptedPassword(),
        communityIds,
        member.getMemberId(),
        member.getId(),
        member.getHouseMemberDocument() != null ? member.getHouseMemberDocument()
            .getDocumentFilename() : "",
        member.getName(),
        member.getCommunityHouse() != null ? member.getCommunityHouse().getHouseId() : "");
  }

  /**
   * builds a `UserDto` object representing the administrator of an enriched schedule
   * payment request, fetching their details from the provided `EnrichedSchedulePaymentRequest`.
   * 
   * @param enrichedSchedulePaymentRequest administrative user for whom the `UserDto`
   * is being built, providing its ID, name, email, and encrypted password.
   * 
   * 	- `userId`: The ID of the user who made the request.
   * 	- `id`: The ID of the administrative entity associated with the request.
   * 	- `name`: The name of the administrative entity associated with the request.
   * 	- `email`: The email address of the administrative entity associated with the request.
   * 	- `encryptedPassword`: The encrypted password for the administrative entity
   * associated with the request.
   * 
   * @returns a `UserDto` object containing the administrator's details.
   * 
   * 	- `userId`: The ID of the administrator associated with the enriched schedule
   * payment request.
   * 	- `id`: The entity ID of the administrator associated with the enriched schedule
   * payment request.
   * 	- `name`: The name of the administrator associated with the enriched schedule
   * payment request.
   * 	- `email`: The email address of the administrator associated with the enriched
   * schedule payment request.
   * 	- `encryptedPassword`: The encrypted password of the administrator associated
   * with the enriched schedule payment request.
   */
  default UserDto getEnrichedRequestAdmin(EnrichedSchedulePaymentRequest enrichedSchedulePaymentRequest) {
    return UserDto.builder()
        .userId(enrichedSchedulePaymentRequest.getAdminId())
        .id(enrichedSchedulePaymentRequest.getAdminEntityId())
        .name(enrichedSchedulePaymentRequest.getAdminName())
        .email(enrichedSchedulePaymentRequest.getAdminEmail())
        .encryptedPassword(enrichedSchedulePaymentRequest.getAdminEncryptedPassword())
        .build();
  }

  /**
   * generates a `HouseMemberDto` object with information from an `EnrichedSchedulePaymentRequest`.
   * It provides the member entity ID, ID, and name.
   * 
   * @param enrichedSchedulePaymentRequest HouseMemberDto object that contains additional
   * information about the member, such as their ID, name, and entity ID.
   * 
   * 	- `getMemberEntityId()` returns the ID of the member entity associated with the
   * schedule payment request.
   * 	- `getMemberId()` returns the ID of the member associated with the schedule payment
   * request.
   * 	- `getHouseMemberName()` returns the name of the house member associated with the
   * schedule payment request.
   * 
   * @returns a `HouseMemberDto` object containing the member's ID, name, and entity ID.
   * 
   * 	- `id`: This property represents the ID of the house member entity associated
   * with the enriched schedule payment request.
   * 	- `memberId`: This property represents the ID of the member associated with the
   * enriched schedule payment request.
   * 	- `name`: This property represents the name of the house member associated with
   * the enriched schedule payment request.
   */
  default HouseMemberDto getEnrichedRequestMember(EnrichedSchedulePaymentRequest enrichedSchedulePaymentRequest) {
    return new HouseMemberDto()
        .id(enrichedSchedulePaymentRequest.getMemberEntityId())
        .memberId(enrichedSchedulePaymentRequest.getMemberId())
        .name(enrichedSchedulePaymentRequest.getHouseMemberName());
  }
}
