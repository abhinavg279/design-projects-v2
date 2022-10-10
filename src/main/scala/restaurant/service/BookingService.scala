package restaurant.service

import restaurant.model.Slot
import restaurant.model.handler.{BookingStorageHandler, RestaurantStorageHandler, RestaurantTableStorageHandler, SlotStorageHandler}

object BookingService {
  def getAvailableSlots(restaurantId: Int): Iterable[Slot] = {
    val _ = RestaurantStorageHandler.getRestaurant(restaurantId)
    val tables = RestaurantTableStorageHandler.getAllTables(restaurantId)
    SlotStorageHandler.getAvailableSlots(tables.toSeq)
  }

  def bookASlot(slotId: Int, userId: Int): Int = {
    val bookingId = BookingStorageHandler.addBooking(slotId, userId)
    val _ = SlotStorageHandler.bookSlot(slotId, bookingId)
    bookingId
  }
}
