package restaurant.model.handler

import org.joda.time.LocalTime
import restaurant.model.Slot
import restaurant.utils.CustomException.{SlotAlreadyBooked, SlotAlreadyReleased, objectNotAvailable}

object SlotStorageHandler {
  private var allSlots = Map.empty[Int, Slot]
  private var nextSlotId = 1

  def getAvailableSlots(restaurantTableIds: Seq[Int]): Iterable[Slot] = {
    allSlots.collect { case (_, slot) if restaurantTableIds.contains(slot.restaurantTableId) && slot.activeBookingId.isEmpty =>
      slot
    }
  }

  def createNewSlot(startHour: Int, endHour: Int, restaurantTableIds: Seq[Int]): Map[Int, Seq[Int]] = {
    val slotsByRestaurantTableId: Map[Int, Seq[Slot]] = restaurantTableIds.map { restaurantTableId =>
      restaurantTableId ->
        (startHour until endHour).map(hour => Slot(getSlotId, restaurantTableId, new LocalTime(hour, 0),
          new LocalTime(hour + 1, 0), None))
    }.toMap

    val slotsOfAllTables: Seq[Slot] = slotsByRestaurantTableId.values.flatten.toSeq
    addOrUpdateSlots(slotsOfAllTables)

    slotsByRestaurantTableId.map { case (tableId, slots) =>
      tableId -> slots.map(_.id)
    }
  }

  def isSlotAvailable(slotId: Int): Boolean = {
    getSlotById(slotId).activeBookingId.isEmpty
  }

  def bookSlot(slotId: Int, bookingId: Int): Int = {
    if (isSlotAvailable(slotId)) {
      val slot = getSlotById(slotId)
      addOrUpdateSlots(Seq(slot.copy(activeBookingId = Some(bookingId)))).head
    } else {
      throw SlotAlreadyBooked(slotId)
    }
  }

  def releaseSlot(slotId: Int): Int = {
    if (!isSlotAvailable(slotId)) {
      val slot = getSlotById(slotId)
      addOrUpdateSlots(Seq(slot.copy(activeBookingId = None))).head
    } else {
      throw SlotAlreadyReleased(slotId)
    }

  }

  // PRIVATE METHODS
  private def addOrUpdateSlots(slots: Seq[Slot]): Seq[Int] = {
    allSlots ++= slots.map(s => (s.id, s))
    slots.map(_.id)
  }

  private def getSlotById(slotId: Int): Slot = {
    allSlots.getOrElse(slotId, throw objectNotAvailable[Slot](slotId))
  }

  private def getSlotId: Int = {
    val res = nextSlotId
    nextSlotId += 1
    res
  }
}
