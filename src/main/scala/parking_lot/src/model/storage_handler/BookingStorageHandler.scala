package parking_lot.src.model.storage_handler

import org.joda.time.DateTime
import parking_lot.src.model.{Booking, BookingStatus}

import java.util.concurrent.atomic.AtomicInteger
import scala.collection.mutable.{Map => MutableMap}

object BookingStorageHandler {
  private val allBooking: MutableMap[Int, Booking] = MutableMap.empty
  private val nextBookingId: AtomicInteger = new AtomicInteger(1)

  def addBookings(slotId: Int, startTime: DateTime, endTime: DateTime, cost: BigDecimal,
                  bookingStatus: BookingStatus): Int = {
    val booking = Booking(getBookingId, slotId, startTime, endTime, cost, bookingStatus)
    addOrUpdateBooking(booking)
  }

  // PRIVATE METHODS
  private def addOrUpdateBooking(booking: Booking): Int = {
    allBooking.addOne((booking.id, booking))
    println(allBooking)
    booking.id
  }

  private def getBookingId: Int = {
    nextBookingId.getAndIncrement()
  }
}