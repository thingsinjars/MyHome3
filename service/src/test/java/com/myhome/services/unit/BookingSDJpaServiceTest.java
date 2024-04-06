package com.myhome.services.unit;

import com.myhome.domain.AmenityBookingItem;
import com.myhome.repositories.AmenityBookingItemRepository;
import com.myhome.services.springdatajpa.BookingSDJpaService;
import helpers.TestUtils;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;

/**
 * TODO
 */
public class BookingSDJpaServiceTest {

  private static final String TEST_BOOKING_ID = "test-booking-id";
  private static final String TEST_AMENITY_ID = "test-amenity-id";
  private static final String TEST_AMENITY_ID_2 = "test-amenity-id-2";
  private final String TEST_AMENITY_DESCRIPTION = "test-amenity-description";

  @Mock
  private AmenityBookingItemRepository bookingItemRepository;

  @InjectMocks
  private BookingSDJpaService bookingSDJpaService;

  /**
   * initialize Mockito Annotations for the current class, enabling mocking of classes
   * and methods.
   */
  @BeforeEach
  private void init() {
    MockitoAnnotations.initMocks(this);
  }

  /**
   * deletes a booking item from the repository, given the amenity ID and booking ID.
   * It utilizes mocking to verify the correct calls to the `bookingItemRepository`.
   */
  @Test
  void deleteBookingItem() {
    // given
    AmenityBookingItem testBookingItem = getTestBookingItem();

    given(bookingItemRepository.findByAmenityBookingItemId(TEST_BOOKING_ID))
        .willReturn(Optional.of(testBookingItem));
    testBookingItem.setAmenity(TestUtils.AmenityHelpers
        .getTestAmenity(TEST_AMENITY_ID, TEST_AMENITY_DESCRIPTION));

    // when
    boolean bookingDeleted = bookingSDJpaService.deleteBooking(TEST_AMENITY_ID, TEST_BOOKING_ID);

    // then
    assertTrue(bookingDeleted);
    verify(bookingItemRepository).findByAmenityBookingItemId(TEST_BOOKING_ID);
    verify(bookingItemRepository).delete(testBookingItem);
  }

  /**
   * verifies that a booking with the given amenity ID and booking ID does not exist
   * before deleting it, using the `bookingSDJpaService` to delete the booking and
   * verify the `bookingItemRepository`.
   */
  @Test
  void deleteBookingNotExists() {
    // given
    given(bookingItemRepository.findByAmenityBookingItemId(TEST_BOOKING_ID))
        .willReturn(Optional.empty());

    // when
    boolean bookingDeleted = bookingSDJpaService.deleteBooking(TEST_AMENITY_ID, TEST_BOOKING_ID);

    // then
    assertFalse(bookingDeleted);
    verify(bookingItemRepository).findByAmenityBookingItemId(TEST_BOOKING_ID);
    verify(bookingItemRepository, never()).delete(any());
  }

  /**
   * tests the deletion of a booking amenity that does not exist in the database. It
   * verifies that the method returns `false`, updates the amenity ID of the booking
   * item, and calls the `verify` methods to confirm the expected actions on the repository.
   */
  @Test
  void deleteBookingAmenityNotExists() {
    // given
    AmenityBookingItem testBookingItem = getTestBookingItem();

    given(bookingItemRepository.findByAmenityBookingItemId(TEST_BOOKING_ID))
        .willReturn(Optional.of(testBookingItem));
    testBookingItem.setAmenity(TestUtils.AmenityHelpers
        .getTestAmenity(TEST_AMENITY_ID_2, TEST_AMENITY_DESCRIPTION));
    // when
    boolean bookingDeleted = bookingSDJpaService.deleteBooking(TEST_AMENITY_ID, TEST_BOOKING_ID);

    // then
    assertFalse(bookingDeleted);
    assertNotEquals(TEST_AMENITY_ID, testBookingItem.getAmenity().getAmenityId());
    verify(bookingItemRepository).findByAmenityBookingItemId(TEST_BOOKING_ID);
    verify(bookingItemRepository, never()).delete(any());
  }

  /**
   * creates a new instance of `AmenityBookingItem` with a predefined ID for testing purposes.
   * 
   * @returns a new instance of `AmenityBookingItem` with a pre-defined ID.
   * 
   * 	- `AmenityBookingItemId`: This is an identifier for the booking item, set to a
   * specific value (`TEST_BOOKING_ID`).
   * 	- The `AmenityBookingItem` class itself, which represents a booking item with its
   * own properties and methods.
   */
  private AmenityBookingItem getTestBookingItem() {
    return new AmenityBookingItem()
        .withAmenityBookingItemId(TEST_BOOKING_ID);
  }
}
