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
package com.myhome.domain;

import com.fasterxml.jackson.annotation.JsonFormat;
import java.math.BigDecimal;
import java.time.LocalDate;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.ManyToOne;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

/**
 * Entity identifying a payment in the service. This could be an electricity bill, house rent, water
 * charge etc
 */
/**
 * represents an entity for a payment made by a User to a HouseMember, with fields
 * for payment ID, charge amount, type, description, recurring status, and due date,
 * as well as relationships with the admin and member entities.
 * Fields:
 * 	- paymentId (String): represents a unique identifier for each payment made by a
 * user.
 * 	- charge (BigDecimal): in the Payment class represents an amount of money to be
 * paid by a user or member.
 * 	- type (String): represents the category of the payment, such as "electricity
 * bill", "house rent", or "water charge".
 * 	- description (String): in the Payment class represents a brief textual description
 * of the payment, possibly including information about the type of charge or the
 * date it was due.
 * 	- recurring (boolean): in the Payment entity represents whether a payment is
 * recurring or not.
 * 	- dueDate (LocalDate): represents the date on which a payment is due to be made.
 * 	- admin (User): represents a user who made a payment.
 * 	- member (HouseMember): in the Payment class represents an association between a
 * payment and a house member.
 */
@AllArgsConstructor
@NoArgsConstructor
@Data
@EqualsAndHashCode(callSuper = false)
@Entity
public class Payment extends BaseEntity {
  @Column(unique = true, nullable = false)
  private String paymentId;
  @Column(nullable = false)
  private BigDecimal charge;
  @Column(nullable = false)
  private String type;
  @Column(nullable = false)
  private String description;
  @Column(nullable = false)
  private boolean recurring;
  @JsonFormat(pattern = "yyyy-MM-dd")
  private LocalDate dueDate;
  @ManyToOne(fetch = FetchType.LAZY)
  private User admin;
  @ManyToOne(fetch = FetchType.LAZY)
  private HouseMember member;
}
