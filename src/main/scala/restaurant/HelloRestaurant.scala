package restaurant

import restaurant.model.Slot
import restaurant.service.{BookingService, RestaurantRegistrationService}
import restaurant.utils.errors.Error.WithErrorHandling

import scala.util.Random

object HelloRestaurant extends App {
  WithErrorHandling {
    val resId: Int = RestaurantRegistrationService.addNewRestaurant("res1", 9, 12, 2)
    val slots: Iterable[Slot] = BookingService.getAvailableSlots(resId)
    println(s"Available Slots: $slots")
    val slotToBook = Random.shuffle(slots).head.id
    println(s"Booking $slotToBook")
    BookingService.bookASlot(slotToBook, 1)
    println(s"Available Slots Now: ${BookingService.getAvailableSlots(resId)}")
  }
}
