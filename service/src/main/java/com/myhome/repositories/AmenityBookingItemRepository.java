package com.myhome.repositories;

import com.myhome.domain.AmenityBookingItem;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * provides methods for interacting with the database to find amenity booking items
 * by their IDs.
 */
public interface AmenityBookingItemRepository extends JpaRepository<AmenityBookingItem, String> {
  Optional<AmenityBookingItem> findByAmenityBookingItemId(String amenityBookingItemId);
}
