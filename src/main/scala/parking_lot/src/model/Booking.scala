package parking_lot.src.model

import org.joda.time.DateTime

sealed trait BookingStatus

case object Booked extends BookingStatus

case object Cancelled extends BookingStatus

case class Booking(id: Int, slotId: Int, startTime: DateTime, endTime: DateTime, cost: BigDecimal, bookingStatus: BookingStatus)

