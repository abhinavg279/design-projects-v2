package restaurant

import restaurant.model.Slot
import restaurant.service.{BookingService, RestaurantRegistrationService}
import restaurant.utils.CustomException.WithErrorHandling
import restaurant.utils.{Input, Response}

import scala.io.StdIn.readLine
import scala.util.Random

object HelloRestaurant extends App {
  while(true) {
    Response.send("Please enter a command: ")
    val input = readLine()
    Input.execute(input)
  }

//  WithErrorHandling {
//    val resId: Int = RestaurantRegistrationService.addNewRestaurant("res1", 9, 12, 2)
//    val slots: Iterable[Slot] = BookingService.getAvailableSlots(resId)
//    Response.send(s"Available Slots: $slots")
//    BookingService.getAvailableSlots(3)
//    val slotToBook = Random.shuffle(slots).head.id
//    Response.send(s"Booking $slotToBook")
//    BookingService.bookASlot(slotToBook, 1)
//    BookingService.bookASlot(slotToBook, 1)
//    Response.send(s"Available Slots Now: ${BookingService.getAvailableSlots(resId)}")
//  }
}
