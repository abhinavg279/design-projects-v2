package parking_lot.src.service

import parking_lot.src.model.VehicleType
import parking_lot.src.model.storage_handler.{ParkingLotStorageHandler, SlotStorageHandler}

object ParkingRegistrationService {
  def registerParkingLot(name: String, availableSlots: Seq[VehicleType]): (Int, Seq[Int]) = {
    val parkingLotId = ParkingLotStorageHandler.addParkingLot(name)
    val slotsIds = SlotStorageHandler.addSlots(parkingLotId, availableSlots)
    (parkingLotId, slotsIds)
  }

  def getAvailableSlots(parkingLotName: String, vehicleType: VehicleType): Seq[Int] = {
    val parkingLot = ParkingLotStorageHandler.getParkingLot(parkingLotName)
    SlotStorageHandler.getAvailableSlots(parkingLot.id, vehicleType)
  }
}
