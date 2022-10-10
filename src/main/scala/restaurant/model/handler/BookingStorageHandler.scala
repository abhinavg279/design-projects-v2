package restaurant.model.handler

import restaurant.model.Booking

object BookingStorageHandler {
  private var allBookings: Map[Int, Booking] = Map.empty
  private var nextBookingId: Int = 1

  def addBooking(slotId: Int, userId: Int): Int = {
    addOrUpdateBooking(Booking(getBookingId, slotId, userId))
  }

  // PRIVATE METHODS
  private def addOrUpdateBooking(booking: Booking): Int = {
    allBookings += ((booking.bookingId, booking))
    booking.bookingId
  }

  private def getBookingId: Int = {
    val res = nextBookingId
    nextBookingId += 1
    res
  }
}
