package com.myhome.services.springdatajpa;

import com.myhome.domain.AmenityBookingItem;
import com.myhome.repositories.AmenityBookingItemRepository;
import com.myhome.services.BookingService;
import java.util.Optional;
import javax.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

/**
 * TODO
 */
@Service
@RequiredArgsConstructor
public class BookingSDJpaService implements BookingService {

  private final AmenityBookingItemRepository bookingRepository;

  /**
   * deletes a booking from the repository based on the amenity ID and booking ID
   * provided. It first finds the booking item with the given IDs, then checks if the
   * amenity associated with the booking item matches the provided amenity ID. If it
   * does, the function deletes the booking item from the repository and returns `true`.
   * Otherwise, it returns `false`.
   * 
   * @param amenityId ID of the amenity that the booking item belongs to, which is used
   * to determine whether the booking item should be deleted.
   * 
   * 	- `Optional<AmenityBookingItem> booking`: This is an optional reference to a
   * `AmenityBookingItem` object in the repository. If no matching booking item is
   * found, this will be `Optional.empty()`.
   * 	- `AmenityBookingItem amenityBookingItem`: This is a `AmenityBookingItem` class
   * that represents a single booking item in the database. It has an `AmenityId`
   * property that references the `amenityId` passed as input.
   * 	- `getAmenity():` This method returns a reference to the `Amenity` object associated
   * with the `AmenityBookingItem`.
   * 	- `orElse(false):` This method returns a boolean value indicating whether a booking
   * item with the specified `bookingId` exists in the database. If no such booking
   * item is found, it returns `false`. Otherwise, it returns `true`.
   * 
   * @param bookingId unique identifier of a booking item that needs to be deleted.
   * 
   * 	- `amenityId`: The ID of the amenity associated with the booking item to be deleted.
   * 	- `bookingItem`: The booking item containing the information about the booking,
   * including its ID.
   * 
   * @returns a boolean value indicating whether the booking item was successfully deleted.
   * 
   * 	- The function returns a boolean value indicating whether the booking item was
   * successfully deleted or not.
   * 	- The `Optional<AmenityBookingItem>` returned by the
   * `bookingRepository.findByAmenityBookingItemId(bookingId)` method represents the
   * found booking item, if any. If no booking item is found, the `Optional` will be `empty`.
   * 	- The `map` method is used to check whether the found booking item has the correct
   * amenity ID. If the amenity ID matches the input `amenityId`, the method returns a
   * boolean value indicating whether the booking item should be deleted or not.
   * 	- If the `Optional` is `empty`, the function returns `false`. Otherwise, it returns
   * `true`.
   */
  @Transactional
  @Override
  public boolean deleteBooking(String amenityId, String bookingId) {
    Optional<AmenityBookingItem> booking =
        bookingRepository.findByAmenityBookingItemId(bookingId);
    return booking.map(bookingItem -> {
      boolean amenityFound =
          bookingItem.getAmenity().getAmenityId().equals(amenityId);
      if (amenityFound) {
        bookingRepository.delete(bookingItem);
        return true;
      } else {
        return false;
      }
    }).orElse(false);
  }
}
