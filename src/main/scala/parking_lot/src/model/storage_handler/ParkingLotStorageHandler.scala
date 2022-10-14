package parking_lot.src.model.storage_handler

import parking_lot.src.model.ParkingLot
import parking_lot.src.utils.CustomException

import java.util.concurrent.atomic.AtomicInteger
import scala.collection.mutable.{Map => MutableMap}

object ParkingLotStorageHandler {
  private val allParkingLot: MutableMap[Int, ParkingLot] = MutableMap.empty
  private val nextParkingLotId: AtomicInteger = new AtomicInteger(1)

  def addParkingLot(name: String): Int = {
    addOrUpdateParkingLot(ParkingLot(getParkingLotId, name))
  }

  def getParkingLot(name: String): ParkingLot = {
    allParkingLot.collectFirst { case (_, lot) if lot.name == name =>
      lot
    }.getOrElse(throw CustomException.objectNotAvailable[ParkingLot](name))
  }

  // PRIVATE METHODS
  private def addOrUpdateParkingLot(parkingLot: ParkingLot): Int = {
    allParkingLot.addOne((parkingLot.id, parkingLot))
    println(allParkingLot)
    parkingLot.id
  }

  private def getParkingLotId: Int = {
    nextParkingLotId.getAndIncrement()
  }
}
