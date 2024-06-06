package com.myhome.controllers;

import com.myhome.api.BookingsApi;
import com.myhome.services.BookingService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

/**
 * implements the BookingsApi interface and provides a method for deleting bookings
 * based on amenity ID and booking ID. The class uses the BookingService to delete a
 * booking, and returns a ResponseEntity with a status code indicating whether the
 * operation was successful or not.
 */
@RestController
@Slf4j
@RequiredArgsConstructor
public class BookingController implements BookingsApi {

  private final BookingService bookingSDJpaService;

  /**
   * deletes a booking based on the amenity ID and booking ID provided. If the booking
   * is successfully deleted, a `NO_CONTENT` status code is returned. If the booking
   * cannot be found, a `NOT_FOUND` status code is returned.
   * 
   * @param amenityId ID of the amenity associated with the booking that is to be deleted.
   * 
   * @param bookingId ID of the booking that should be deleted.
   * 
   * @returns a `ResponseEntity` with a status code of either `NO_CONTENT` or `NOT_FOUND`,
   * depending on whether the booking was successfully deleted.
   * 
   * * The `@PathVariable` annotations represent the parameters passed to the function
   * from the URL path.
   * * `amenityId` and `bookingId` are the identifiers for the amenity and booking,
   * respectively, that are being deleted.
   * * `isBookingDeleted` is a boolean value indicating whether the booking was
   * successfully deleted or not.
   * * `ResponseEntity` is an class that represents a HTTP response entity, which
   * contains information about the status code, headers, and body of the response.
   * * The `status()` method of `ResponseEntity` returns the status code of the response,
   * which can be either `HttpStatus.NO_CONTENT` or `HttpStatus.NOT_FOUND`.
   * * The `build()` method of `ResponseEntity` creates a new instance of the class
   * with the specified status code and headers.
   */
  @Override
  public ResponseEntity<Void> deleteBooking(@PathVariable String amenityId,
      @PathVariable String bookingId) {
    boolean isBookingDeleted = bookingSDJpaService.deleteBooking(amenityId, bookingId);
    if (isBookingDeleted) {
      return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    } else {
      return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
    }
  }
}
