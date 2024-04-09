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
 * is a Java class that implements the BookingsApi interface and provides methods for
 * managing bookings. The class has a single method, deleteBooking(), which takes two
 * path variables (amenityId and bookingId) and uses the BookingService to delete a
 * booking. If the booking is successfully deleted, a ResponseEntity with a status
 * code of NO_CONTENT is returned. Otherwise, a ResponseEntity with a status code of
 * NOT_FOUND is returned.
 */
@RestController
@Slf4j
@RequiredArgsConstructor
public class BookingController implements BookingsApi {

  private final BookingService bookingSDJpaService;

  /**
   * deletes a booking based on the provided amenity ID and booking ID, returning a
   * ResponseEntity with a status code indicating whether the operation was successful
   * or not.
   * 
   * @param amenityId unique identifier for an amenity associated with the booking being
   * deleted.
   * 
   * @param bookingId ID of the booking that needs to be deleted.
   * 
   * @returns a `ResponseEntity` with a status code of either `NO_CONTENT` or `NOT_FOUND`,
   * depending on whether the booking was successfully deleted.
   * 
   * 	- `ResponseEntity`: This is the class that represents an HTTP response entity,
   * which contains the status code and body of the response. In this case, the status
   * code is `NO_CONTENT` or `NOT_FOUND`, depending on whether the booking was successfully
   * deleted or not.
   * 	- `status`: This is a method that returns the HTTP status code of the response
   * entity. The value of this method is `NO_CONTENT` if the booking was successfully
   * deleted, and `NOT_FOUND` otherwise.
   * 	- `build()`: This is a method that builds the response entity by setting its
   * status code and body.
   * 
   * In summary, the output of the `deleteBooking` function is an HTTP response entity
   * with a status code indicating whether the booking was successfully deleted or not.
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
