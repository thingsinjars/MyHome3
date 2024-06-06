package com.myhome.services.springdatajpa;

import com.myhome.domain.AmenityBookingItem;
import com.myhome.repositories.AmenityBookingItemRepository;
import com.myhome.services.BookingService;
import java.util.Optional;
import javax.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

/**
 * is responsible for deleting bookings from a repository based on amenity ID and
 * booking ID. It first finds the booking item with the given IDs, then checks if the
 * amenity associated with the booking item matches the provided amenity ID. If it
 * does, the function deletes the booking item from the repository and returns `true`.
 * Otherwise, it returns `false`.
 */
@Service
@RequiredArgsConstructor
public class BookingSDJpaService implements BookingService {

  private final AmenityBookingItemRepository bookingRepository;

  /**
   * deletes a booking item from the repository based on its amenity booking item ID,
   * returning `true` if successful and `false` otherwise.
   * 
   * @param amenityId identifier of an amenity that is associated with the booking to
   * be deleted, and is used to determine whether the booking should be deleted based
   * on the amenity ID match.
   * 
   * @param bookingId id of an existing booking that needs to be deleted, and is used
   * to locate the relevant booking record in the repository for deletion.
   * 
   * @returns a boolean value indicating whether the booking item was found and deleted
   * successfully.
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
