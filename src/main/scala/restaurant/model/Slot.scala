package restaurant.model

import org.joda.time.LocalTime

case class Slot(id: Int, restaurantTableId: Int, startTime: LocalTime, endTime: LocalTime, activeBookingId: Option[Int])

