package parking_lot.src.model

sealed trait VehicleType

case object Car extends VehicleType

case object Bike extends VehicleType

case class Slot(id: Int, parkingLotId: Int, vehicleType: VehicleType, activeBookingId: Option[Int])

