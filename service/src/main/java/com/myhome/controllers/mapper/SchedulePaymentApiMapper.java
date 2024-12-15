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
 * maps a Schedule Payment Request to a Payment Dto using enriched user details, and
 * provides additional functionality for recurring payments, member ID, and community
 * IDs.
 */
@Mapper
public interface SchedulePaymentApiMapper {

  /**
   * builds a `UserDto` object from a given `adminId`.
   * 
   * @param adminId user ID of an admin in the `UserDto` object constructed by the function.
   * 
   * @returns a `UserDto` object with a `userId` field set to the input `adminId`.
   * 
   * * `userId`: This field contains the value of the `adminId` parameter passed to the
   * function, which is used as the `userId` attribute of the resulting `UserDto`.
   */
  @Named("adminIdToAdmin")
  static UserDto adminIdToAdminDto(String adminId) {
    return UserDto.builder()
        .userId(adminId)
        .build();
  }

  /**
   * maps a `memberId` string parameter to a `HouseMemberDto` object, providing a
   * convenient conversion for further processing or storage.
   * 
   * @param memberId identifier of a member in the `HouseMemberDto` object created by
   * the function.
   * 
   * @returns a `HouseMemberDto` object containing the input `memberId`.
   * 
   * * `memberId`: A string attribute that holds the member ID of the House Member.
   * * `HouseMemberDto`: The output is a instance of the `HouseMemberDto` class, which
   * represents a House Member in a structured format.
   */
  @Named("memberIdToMember")
  static HouseMemberDto memberIdToMemberDto(String memberId) {
    return new HouseMemberDto()
        .memberId(memberId);
  }

  /**
   * transforms a `UserDto` object into a string representation of the user's ID.
   * 
   * @param userDto user object containing the user ID, which is returned as the output
   * of the `adminToAdminId` function.
   * 
   * * The `UserDto` object contains a `userId` property that returns a string value
   * representing the user ID.
   * 
   * @returns a string representing the user ID of the admin.
   */
  @Named("adminToAdminId")
  static String adminToAdminId(UserDto userDto) {
    return userDto.getUserId();
  }

  /**
   * returns the `MemberId` of a given `HouseMemberDto`.
   * 
   * @param houseMemberDto HouseMember object that contains information about a member
   * of a house, which is used to retrieve the member's ID.
   * 
   * * `getMemberId()` returns the member ID.
   * 
   * @returns a string representing the member ID of the inputted HouseMemberDto object.
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
   * maps the user details from a payment request to an admin and another member,
   * leveraging the `@MappingTarget` annotation to generate a Builder instance for the
   * PaymentDto class.
   * 
   * @param paymentDto PaymentDto object to which user fields will be mapped.
   * 
   * * `PaymentDto.PaymentDtoBuilder`: This is an instance of a builder class for the
   * `PaymentDto` class, which is annotated with `@Builder`. The use of `AfterMapping`
   * requires passing in the instance of the builder class.
   * * `EnrichedSchedulePaymentRequest`: This is the original input passed to the
   * function, which has been enriched with additional information.
   * * `getEnrichedRequestMember()` and `getEnrichedRequestAdmin()`: These are methods
   * that extract the relevant user details from the enriched input, such as members
   * or admins, and return them as instances of the `PaymentDto` class.
   * 
   * @param enrichedSchedulePaymentRequest PaymentDto and provides the user details
   * that need to be converted to admin and house member details for further processing.
   * 
   * * `PaymentDto.PaymentDtoBuilder`: This is an instance of the `PaymentDto.PaymentDtoBuilder`
   * class, which is a builder class for the `PaymentDto` class.
   * * `EnrichedSchedulePaymentRequest`: This is the input parameter for the function,
   * which contains various properties and attributes related to the payment request.
   * These include:
   * 	+ `member`: A reference to a member of the house associated with the payment request.
   * 	+ `admin`: A reference to an administrator associated with the payment request.
   * 
   * The function modifies these properties by calling the `getEnrichedRequestMember`
   * and `getEnrichedRequestAdmin` methods, which are responsible for extracting the
   * relevant information from the input `enrichedSchedulePaymentRequest`.
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
   * enriches a `SchedulePaymentRequest` object with additional information about the
   * admin and member, such as their IDs, names, email addresses, and community membership.
   * 
   * @param request Schedule Payment Request that is being enriched, providing its type,
   * description, recurring status, charge amount, due date, administrator ID, and other
   * relevant information.
   * 
   * * `type`: The type of schedule payment request, which is an immutable property.
   * * `description`: A description of the schedule payment request, which is an immutable
   * property.
   * * `isRecurring`: Indicates whether the schedule payment request is recurring or
   * not, which is a Boolean property.
   * * `charge`: The charge amount for the schedule payment request, which is an immutable
   * property.
   * * `dueDate`: The due date of the schedule payment request, which is an immutable
   * property.
   * * `adminId`: The ID of the admin who created the schedule payment request, which
   * is an immutable property.
   * * `adminName`: The name of the admin who created the schedule payment request,
   * which is an immutable property.
   * * `adminEmail`: The email address of the admin who created the schedule payment
   * request, which is an immutable property.
   * * `encryptedPassword`: The encrypted password of the admin who created the schedule
   * payment request, which is an immutable property.
   * * `communityIds`: A set of community IDs associated with the schedule payment
   * request, which is generated from the IDs of the communities to which the admin
   * belongs, using the `map` and `collect` methods of the Stream API.
   * * `memberId`: The ID of the member who requested the schedule payment, which is
   * an immutable property.
   * * `memberName`: The name of the member who requested the schedule payment, which
   * is an immutable property.
   * * `houseMemberDocumentFilename`: The filename of the House Member document associated
   * with the member, if available, which is an immutable property.
   * * `communityHouseId`: The ID of the community to which the member belongs, if
   * available, which is an immutable property.
   * 
   * @param admin User object that contains information about the administrator who is
   * enriching the payment request, including their ID, name, email, encrypted password,
   * and communities.
   * 
   * * `getCommunities()` returns a stream of `Community` objects representing the
   * user's communities.
   * * `map(Collectors.toSet())` converts the stream into a set of community IDs.
   * 
   * The remaining properties of `admin` are:
   * 
   * * `id`: the user's ID
   * * `name`: the user's name
   * * `email`: the user's email address
   * * `encryptedPassword`: the user's encrypted password
   * 
   * The `member` object has the following properties:
   * 
   * * `getMemberId()` returns the member's ID
   * * `getHouseMemberDocument()` returns a `Document` object representing the member's
   * house document (if available)
   * 
   * @param member HouseMember object associated with the payment request, providing
   * its member ID, house ID, and community ID.
   * 
   * * `member.getMemberId()`: The unique identifier for the member in the system.
   * * `member.getId()`: The internal ID of the member within the `HouseMember` document.
   * * `member.getHouseMemberDocument() != null ? member.getHouseMemberDocument().getDocumentFilename()
   * : ""`: The filename of the member's House Member document, or an empty string if
   * no document is available.
   * * `member.getName()`: The name of the member.
   * * `member.getCommunityHouse() != null ? member.getCommunityHouse().getHouseId() :
   * ""`: The ID of the community house that the member belongs to, or an empty string
   * if no community house is associated with the member.
   * 
   * @returns an enriched `SchedulePaymentRequest` object containing additional fields.
   * 
   * * `type`: The type of payment request, which is an arbitrary string.
   * * `description`: A brief description of the payment request, which is a string.
   * * `isRecurring`: A boolean indicating whether the payment request is recurring or
   * not.
   * * `charge`: The charge amount for the payment request, which is a decimal value.
   * * `dueDate`: The due date of the payment request, which is a date in the format `YYYY-MM-DD`.
   * * `adminId`: The ID of the admin who created the payment request, which is an integer.
   * * `adminName`: The name of the admin who created the payment request, which is a
   * string.
   * * `adminEmail`: The email address of the admin who created the payment request,
   * which is a string.
   * * `encryptedPassword`: The encrypted password of the admin who created the payment
   * request, which is a string.
   * * `communityIds`: A set of community IDs associated with the payment request, which
   * is a collection of strings.
   * * `memberId`: The ID of the member for whom the payment request is made, which is
   * an integer.
   * * `houseMemberDocumentFilename`: The filename of the House Member document associated
   * with the payment request (empty string if not available).
   * * `memberName`: The name of the member for whom the payment request is made, which
   * is a string.
   * * `communityHouseId`: The ID of the community house associated with the payment
   * request (empty string if not available).
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
   * creates a `UserDto` object containing the admin's details, based on the enriched
   * schedule payment request provided as input.
   * 
   * @param enrichedSchedulePaymentRequest administrative user for whom the method is
   * generating the enriched request.
   * 
   * * `userId`: The ID of the administrator associated with the schedule payment request.
   * * `id`: The entity ID of the administrator associated with the schedule payment request.
   * * `name`: The name of the administrator associated with the schedule payment request.
   * * `email`: The email address of the administrator associated with the schedule
   * payment request.
   * * `encryptedPassword`: The encrypted password of the administrator associated with
   * the schedule payment request.
   * 
   * @returns a `UserDto` object containing the admin's details.
   * 
   * * `userId`: The ID of the admin associated with the enriched schedule payment request.
   * * `id`: The entity ID of the admin associated with the enriched schedule payment
   * request.
   * * `name`: The name of the admin associated with the enriched schedule payment request.
   * * `email`: The email address of the admin associated with the enriched schedule
   * payment request.
   * * `encryptedPassword`: The encrypted password of the admin associated with the
   * enriched schedule payment request.
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
   * transforms an `EnrichedSchedulePaymentRequest` object into a `HouseMemberDto`
   * object, including member ID, name, and entity ID.
   * 
   * @param enrichedSchedulePaymentRequest payment request with additional details such
   * as member entity ID, member ID, and house member name.
   * 
   * * `getMemberEntityId()` returns the entity ID of the member associated with the
   * schedule payment request.
   * * `getMemberId()` returns the ID of the member associated with the schedule payment
   * request.
   * * `getHouseMemberName()` returns the name of the house member associated with the
   * schedule payment request.
   * 
   * @returns a `HouseMemberDto` object with member ID, name, and entity ID.
   * 
   * * `id`: This property represents the ID of the house member entity associated with
   * the enriched schedule payment request.
   * * `memberId`: This property contains the unique identifier of the member associated
   * with the enriched schedule payment request.
   * * `name`: This property holds the name of the house member associated with the
   * enriched schedule payment request.
   */
  default HouseMemberDto getEnrichedRequestMember(EnrichedSchedulePaymentRequest enrichedSchedulePaymentRequest) {
    return new HouseMemberDto()
        .id(enrichedSchedulePaymentRequest.getMemberEntityId())
        .memberId(enrichedSchedulePaymentRequest.getMemberId())
        .name(enrichedSchedulePaymentRequest.getHouseMemberName());
  }
}
