package com.myhome.services;

/**
 * provides a method for deleting bookings based on their amenity and ID.
 */
public interface BookingService {

  boolean deleteBooking(String amenityId, String bookingId);

}
