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
 * TODO
 */
@RestController
@Slf4j
@RequiredArgsConstructor
public class BookingController implements BookingsApi {

  private final BookingService bookingSDJpaService;

  /**
   * deletes a booking based on its amenity ID and ID, returning a HTTP status code
   * indicating whether the operation was successful or not.
   * 
   * @param amenityId ID of an amenity that is associated with the booking to be deleted.
   * 
   * 	- `amenityId`: A string representing the unique identifier for an amenity.
   * 
   * @param bookingId unique identifier of the booking to be deleted.
   * 
   * 	- `amenityId`: The ID of the amenity for which the booking is to be deleted.
   * 	- `bookingId`: The unique identifier of the booking to be deleted.
   * 
   * @returns a `ResponseEntity` with a `NO_CONTENT` status code if the booking was
   * successfully deleted, or a `NOT_FOUND` status code otherwise.
   * 
   * 	- `isBookingDeleted`: This boolean property indicates whether the booking was
   * successfully deleted or not.
   * 	- `HttpStatus`: The HTTP status code returned by the function, which can be either
   * `NO_CONTENT` (204) or `NOT_FOUND` (404).
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
