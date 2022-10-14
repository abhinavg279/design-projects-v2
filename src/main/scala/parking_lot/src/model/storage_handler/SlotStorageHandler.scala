package parking_lot.src.model.storage_handler

import parking_lot.src.model.{Slot, VehicleType}
import parking_lot.src.utils.CustomException
import parking_lot.src.utils.CustomException.{SlotAlreadyBooked, SlotNotBooked}

import java.util.concurrent.atomic.AtomicInteger
import scala.collection.mutable.{Map => MutableMap}

object SlotStorageHandler {
  private val allSlot: MutableMap[Int, Slot] = MutableMap.empty
  private val nextSlotId: AtomicInteger = new AtomicInteger(1)

  def addBookingId(slotId: Int, bookingId: Int): Int = {
    val slot = getById(slotId)
    assert(slot.activeBookingId.isEmpty, throw SlotAlreadyBooked(slotId))
    addOrUpdateSlots(Seq(slot.copy(activeBookingId = Some(bookingId)))).head
  }

  def freeSlot(slotId: Int): Int = {
    val slot = getById(slotId)
    assert(slot.activeBookingId.nonEmpty, throw SlotNotBooked(slotId))
    addOrUpdateSlots(Seq(slot.copy(activeBookingId = None))).head
  }

  def addSlots(parkingLotId: Int, availableSlots: Seq[VehicleType]): Seq[Int] = {
    val slots = availableSlots.map { vehicleType =>
      Slot(getSlotId, parkingLotId, vehicleType, activeBookingId = None)
    }

    addOrUpdateSlots(slots)
  }

  def getAvailableSlots(parkingLotId: Int, vehicleType: VehicleType): Seq[Int] = {
    allSlot.collect { case (i, slot) if slot.parkingLotId == parkingLotId &&
      slot.vehicleType == vehicleType && slot.activeBookingId.isEmpty => slot.id
    }.toSeq
  }

  // PRIVATE METHODS
  private def addOrUpdateSlots(slots: Seq[Slot]): Seq[Int] = {
    allSlot.addAll(slots.map(s => (s.id, s)))
    println(allSlot)
    slots.map(_.id)
  }

  private def getById(slotId: Int) = {
    allSlot.getOrElse(slotId, throw CustomException.objectNotAvailable[Slot](slotId))
  }

  private def getSlotId: Int = {
    nextSlotId.getAndIncrement()
  }
}
