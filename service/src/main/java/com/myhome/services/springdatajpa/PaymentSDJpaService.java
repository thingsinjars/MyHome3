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

package com.myhome.services.springdatajpa;

import com.myhome.controllers.dto.PaymentDto;
import com.myhome.controllers.dto.mapper.PaymentMapper;
import com.myhome.domain.HouseMember;
import com.myhome.domain.Payment;
import com.myhome.domain.User;
import com.myhome.repositories.HouseMemberRepository;
import com.myhome.repositories.PaymentRepository;
import com.myhome.repositories.UserRepository;
import com.myhome.services.PaymentService;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

/**
 * Implements {@link PaymentService} and uses Spring Data JPA Repository to do its work
 */
/**
 * is an implementation of the `PaymentSDJpaService` interface in a Spring Boot
 * application. It provides various methods for retrieving and manipulating payment
 * data in a database using JPA (Java Persistence API) technology. The class contains
 * several methods that accept a `PaymentDto` object as input, convert it to a
 * corresponding `Payment` object, save the `Payment` object to the database, and
 * return the converted `PaymentDto` object as output. Additionally, the class provides
 * a method for generating a unique payment ID for a `PaymentDto` object using the
 * `UUID` class.
 */
@Service
@Slf4j
@RequiredArgsConstructor
public class PaymentSDJpaService implements PaymentService {
  private final PaymentRepository paymentRepository;
  private final UserRepository adminRepository;
  private final PaymentMapper paymentMapper;
  private final HouseMemberRepository houseMemberRepository;

  /**
   * generates a payment ID and creates a new payment record in the repository.
   * 
   * @param request payment details required to schedule a payment.
   * 
   * * `generatePaymentId`: A method called `generatePaymentId` is called on the input
   * object to generate an ID for the payment.
   * * `createPaymentInRepository`: The `createPaymentInRepository` method is called
   * on the input object to create a payment in the repository.
   * 
   * @returns a PaymentDto object containing the scheduled payment information.
   * 
   * * `PaymentDto`: This is the type of the object that is returned by the function,
   * which contains information about a payment.
   * * `generatePaymentId(request)`: This is an action performed inside the function,
   * which generates a unique identifier for the payment.
   * * `createPaymentInRepository(request)`: This is another action performed inside
   * the function, which creates a new payment object in the repository.
   */
  @Override
  public PaymentDto schedulePayment(PaymentDto request) {
    generatePaymentId(request);
    return createPaymentInRepository(request);
  }

  /**
   * retrieves a payment detail by its ID from the repository and maps it to a `PaymentDto`.
   * 
   * @param paymentId ID of the payment that is being retrieved, and it is used to
   * identify the relevant payment data in the database.
   * 
   * @returns an Optional<PaymentDto> containing a PaymentDto object representing the
   * payment details for the specified payment ID.
   * 
   * * `Optional<PaymentDto>` is the type of the returned value, indicating that it may
   * be present or absent.
   * * `paymentRepository.findByPaymentId(paymentId)` is a method call that retrieves
   * a `Payment` object based on the given `paymentId`.
   * * `map( paymentMapper::paymentToPaymentDto)` is a method call that converts the
   * retrieved `Payment` object into a `PaymentDto` object.
   */
  @Override
  public Optional<PaymentDto> getPaymentDetails(String paymentId) {
    return paymentRepository.findByPaymentId(paymentId)
        .map(paymentMapper::paymentToPaymentDto);
  }

  /**
   * retrieves a HouseMember object from the repository based on the inputted member ID.
   * 
   * @param memberId ID of the House Member to be retrieved from the repository.
   * 
   * @returns an Optional<HouseMember> object containing the House Member with the
   * provided member ID, if found in the repository.
   * 
   * * `Optional<HouseMember>`: This represents an optional reference to a HouseMember
   * object, indicating that the function may or may not return a valid HouseMember
   * instance depending on whether one exists with the given `memberId`.
   * * `houseMemberRepository.findByMemberId(memberId)`: This method is used to retrieve
   * a HouseMember object based on its `memberId` field. It returns an Optional<HouseMember>
   * reference, which can be empty if no matching HouseMember is found.
   */
  @Override
  public Optional<HouseMember> getHouseMember(String memberId) {
    return houseMemberRepository.findByMemberId(memberId);
  }

  /**
   * retrieves a set of payments associated with a specific member ID from the payment
   * repository.
   * 
   * @param memberId member ID for which the payments are to be retrieved.
   * 
   * @returns a set of Payment objects that match the specified member ID.
   * 
   * * `Set<Payment>`: This is the type of the output, which is a set of payments
   * belonging to a specific member.
   * * `memberId`: This is the identifier of the member whose payments are being retrieved.
   * * `paymentRepository`: This is the repository responsible for storing and retrieving
   * payments.
   * * `ExampleMatcher`: This is an object used to define the matching criteria for the
   * payments, including the member ID.
   * * `ignoringMatcher`: This is a property of the `ExampleMatcher` object that ignores
   * certain fields in the payment objects.
   * * `payment`: This is the base class for all payments, which contains the common
   * attributes and methods for all payments.
   * 
   * The function returns a set of payments belonging to the specified member, which
   * are retrieved from the payment repository using the defined matching criteria.
   */
  @Override
  public Set<Payment> getPaymentsByMember(String memberId) {
    ExampleMatcher ignoringMatcher = ExampleMatcher.matchingAll()
        .withMatcher("memberId",
            ExampleMatcher.GenericPropertyMatchers.startsWith().ignoreCase())
        .withIgnorePaths("paymentId", "charge", "type", "description", "recurring", "dueDate",
            "admin");

    Example<Payment> paymentExample =
        Example.of(new Payment(null, null, null, null, false, null, null,
                new HouseMember().withMemberId(memberId)),
            ignoringMatcher);

    return new HashSet<>(paymentRepository.findAll(paymentExample));
  }

  /**
   * retrieves all payments for a specific admin user from the repository, ignoring
   * certain fields related to payment details and membership.
   * 
   * @param adminId user ID of the admin to whom the payments belong, and is used by
   * the ExampleMatcher to filter the results based on the match of the `user.userId`
   * field.
   * 
   * @param pageable pagination information for the query, allowing the method to
   * retrieve a specified number of payments per page.
   * 
   * * `Pageable pageable`: This represents a Pageable object that contains information
   * about the pagination of the payment data. It has various attributes such as the
   * current page number, the total number of pages, the number of items per page, and
   * the total number of items in the dataset.
   * * `String adminId`: This is the ID of the administrator for whom the payments are
   * being retrieved.
   * * `ExampleMatcher ignoringMatcher`: This is an ExampleMatcher object that defines
   * the matching criteria for the payment data. It ignores certain fields such as
   * "paymentId", "charge", "type", "description", "recurring", "dueDate", and "memberId".
   * 
   * @returns a paginated list of payments matching the specified admin ID, with ignored
   * fields.
   * 
   * * `Page<Payment>`: This is a pageable collection of payments that match the specified
   * administrator ID. The pageable interface allows for pagination and sorting of the
   * results.
   * * `paymentExample`: This is an example of a payment object that is used to define
   * the matching criteria for the query. It includes fields such as the payment amount,
   * payment date, and administrator ID.
   * * `paymentRepository`: This is a repository interface that provides methods for
   * storing and retrieving payments. The `findAll` method is used in this case to
   * retrieve all payments that match the specified administrator ID.
   */
  @Override
  public Page<Payment> getPaymentsByAdmin(String adminId, Pageable pageable) {
    ExampleMatcher ignoringMatcher = ExampleMatcher.matchingAll()
        .withMatcher("adminId",
            ExampleMatcher.GenericPropertyMatchers.startsWith().ignoreCase())
        .withIgnorePaths("paymentId", "charge", "type", "description", "recurring", "dueDate",
            "memberId");

    Example<Payment> paymentExample =
        Example.of(
            new Payment(null, null, null, null, false, null, new User().withUserId(adminId), null),
            ignoringMatcher);

    return paymentRepository.findAll(paymentExample, pageable);
  }

  /**
   * creates a new payment entity by mapping a `PaymentDto` request to a `Payment`
   * object, saving both the admin and payment entities in their respective repositories,
   * and returning the mapped `PaymentDto`.
   * 
   * @param request PaymentDto object that contains the details of the payment to be
   * created, which is then used to create the corresponding Payment object and save
   * it in the repository.
   * 
   * * PaymentDto request contains the admin details which are saved in the `adminRepository`.
   * * The payment details are saved in the `paymentRepository`.
   * * The function converts the `PaymentDto` to a `Payment` object using the
   * `paymentMapper`, and then saves both the admin and payment objects in their
   * respective repositories.
   * 
   * @returns a `PaymentDto` object representing the saved payment data.
   * 
   * * `paymentMapper.paymentToPaymentDto(payment)`: This method creates a `PaymentDto`
   * object from a `Payment` object. The resulting `PaymentDto` object contains the
   * same data as the original `Payment` object, but in a more convenient format for
   * use in other parts of the application.
   * * `adminRepository.save(payment.getAdmin())`: This method saves the `Admin` object
   * associated with the `Payment` object to the database.
   * * `paymentRepository.save(payment)`: This method saves the `Payment` object to the
   * database.
   */
  private PaymentDto createPaymentInRepository(PaymentDto request) {
    Payment payment = paymentMapper.paymentDtoToPayment(request);

    adminRepository.save(payment.getAdmin());
    paymentRepository.save(payment);

    return paymentMapper.paymentToPaymentDto(payment);
  }

  /**
   * generates a unique payment ID using the `UUID.randomUUID()` method and assigns it
   * to the `PaymentDto` object's `paymentId` field.
   * 
   * @param request PaymentDto class and is used to set the payment ID field of the
   * object to a randomly generated UUID string.
   * 
   * * `request`: A `PaymentDto` object, which contains the required information for
   * generating a unique payment ID.
   */
  private void generatePaymentId(PaymentDto request) {
    request.setPaymentId(UUID.randomUUID().toString());
  }
}
