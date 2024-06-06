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

package com.myhome.controllers.response;

import java.math.BigDecimal;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * represents a response for scheduling payments, including payment ID, charge amount,
 * and other relevant information.
 * Fields:
 * 	- paymentId (String): represents a unique identifier for a payment.
 * 	- charge (BigDecimal): represents a monetary value.
 * 	- type (String): represents the payment's type, which could be "credit card",
 * "bank transfer", or "other".
 * 	- description (String): likely represents a brief textual explanation of the
 * payment or charge being made provided by the user or administrator.
 * 	- recurring (boolean): indicates whether a payment is a one-time payment or a
 * recurring payment.
 * 	- dueDate (String): represents a date when a payment is expected to be made.
 * 	- adminId (String): represents an identifier for the administrator who manages
 * the payment schedule.
 * 	- memberId (String): represents a unique identifier for a member associated with
 * the payment schedule.
 */
@AllArgsConstructor
@NoArgsConstructor
@Data
public class SchedulePaymentResponse {
  private String paymentId;
  private BigDecimal charge;
  private String type;
  private String description;
  private boolean recurring;
  private String dueDate;
  private String adminId;
  private String memberId;
}
