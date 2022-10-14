package parking_lot.src.service

import org.joda.time.DateTime
import parking_lot.src.CostStrategy
import parking_lot.src.CostStrategy.FixedPriceStrategy
import parking_lot.src.model.storage_handler.{BookingStorageHandler, SlotStorageHandler}
import parking_lot.src.model.{Booked, VehicleType}

object BookingService {
  val costStrategy: CostStrategy = FixedPriceStrategy

  def createBooking(slotId: Int, endTime: DateTime, vehicleType: VehicleType): Unit = {
    val startTime = DateTime.now()
    val cost = costStrategy.calculatePrice(startTime, endTime, vehicleType)
    val bookingId = BookingStorageHandler.addBookings(slotId, startTime, endTime, cost, bookingStatus = Booked)
    val _ = SlotStorageHandler.addBookingId(slotId, bookingId)
  }

  def claimVehicle(slotId: Int): Unit = {
    val _ = SlotStorageHandler.freeSlot(slotId)
  }
}
