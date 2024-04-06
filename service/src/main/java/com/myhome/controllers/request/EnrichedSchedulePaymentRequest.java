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

package com.myhome.controllers.request;

import com.myhome.model.SchedulePaymentRequest;
import java.math.BigDecimal;
import java.util.Set;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

/**
 * This class is used to enrich the normal SchedulePaymentRequest with details relating to the admin
 * and house member in order to map to the User and HouseMember fields of payment successfully. By
 * doing this, you can avoid having to specify all the extra details in the request and just use the
 * IDs to get the data to enrich this request
 */
/**
 * extends SchedulePaymentRequest and provides additional information on admin and
 * house member details to enrich the payment request.
 * Fields:
 * 	- adminEntityId (Long): represents an entity associated with the admin user, such
 * as a department or organization, based on which the request is enriched.
 * 	- adminName (String): represents the human name of an administrative user associated
 * with the payment request.
 * 	- adminEmail (String): represents an email address associated with the admin
 * entity ID.
 * 	- adminEncryptedPassword (String): likely stores an encrypted password for an
 * administrator of a house member in the system.
 * 	- adminCommunityIds (Set<String>): stores a set of community IDs associated with
 * the admin user who created or updated the EnrichedSchedulePaymentRequest.
 * 	- memberEntityId (Long): represents an entity in the system of interest.
 * 	- houseMemberDocumentName (String): represents the name of a document related to
 * the member's house information.
 * 	- houseMemberName (String): represents the name of a member of a particular household.
 * 	- houseMemberHouseID (String): represents the unique identifier of the member's
 * house within their community.
 */
@NoArgsConstructor
@AllArgsConstructor
@Data
@EqualsAndHashCode(callSuper = false)
public class EnrichedSchedulePaymentRequest extends SchedulePaymentRequest {
  private Long adminEntityId;
  private String adminName;
  private String adminEmail;
  private String adminEncryptedPassword;
  private Set<String> adminCommunityIds;
  private Long memberEntityId;
  private String houseMemberDocumentName;
  private String houseMemberName;
  private String houseMemberHouseID;

  public EnrichedSchedulePaymentRequest(String type, String description, boolean recurring,
      BigDecimal charge, String dueDate, String adminId, Long adminEntityId, String adminName,
      String adminEmail, String adminEncryptedPassword, Set<String> adminCommunityIds,
      String memberId, Long memberEntityId, String houseMemberDocumentName, String houseMemberName,
      String houseMemberHouseID) {

    super.type(type).description(description).recurring(recurring).charge(charge).dueDate(dueDate).adminId(adminId).memberId(memberId);

    this.adminName = adminName;
    this.adminEmail = adminEmail;
    this.adminEncryptedPassword = adminEncryptedPassword;
    this.adminCommunityIds = adminCommunityIds;
    this.adminEntityId = adminEntityId;
    this.memberEntityId = memberEntityId;
    this.houseMemberDocumentName = houseMemberDocumentName;
    this.houseMemberName = houseMemberName;
    this.houseMemberHouseID = houseMemberHouseID;
  }
}
