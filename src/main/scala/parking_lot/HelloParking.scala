package parking_lot
import org.joda.time.DateTime
import parking_lot.src.model.{Bike, Car}
import parking_lot.src.service.{BookingService, ParkingRegistrationService}
import splitwise.src.utils.CustomException.WithErrorHandling



object HelloParking extends App {
  WithErrorHandling {
    val (parkingLotId, _) = ParkingRegistrationService.registerParkingLot("parking-01", Seq(Car, Car, Car, Bike, Bike, Car, Bike, Bike))
    val availableSlots = ParkingRegistrationService.getAvailableSlots(parkingLotName = "parking-01", vehicleType = Car)
    val bookingId: Unit = BookingService.createBooking(slotId = 0, endTime = DateTime.now().plusHours(1), Car)
    val _ = BookingService.claimVehicle(slotId = 2)
  }
}
