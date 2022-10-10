package restaurant_service

import org.joda.time.LocalTime

import scala.reflect.ClassTag

object HelloRestaurant extends App {
  case class ObjectNotAvailable(id: Int, className: String) extends Exception(s"${className} with id ${id} could not be found")

  case class SlotAlreadyBooked(id: Int) extends Exception(s"Slot with ${id} is already booked")

  case class SlotAlreadyReleased(id: Int) extends Exception(s"Slot with ${id} is already released")

  def objectNotAvailable[T](id: Int)(implicit classTag: ClassTag[T]): ObjectNotAvailable = {
    val className = classTag.runtimeClass.getName
    ObjectNotAvailable(id, className)
  }

  case class Slot(id: Int, restaurantTableId: Int, startTime: LocalTime, endTime: LocalTime, activeBookingId: Option[Int])

  object SlotStorageHandler {
    private val allSlots = Map.empty[Int, Slot]
    private var nextSlotId = 1

    def createNewSlot(startHour: Int, endHour: Int, restaurantTableIds: Seq[Int]): Map[Int, Seq[Int]] = {
      val slotsByRestaurantTableId: Map[Int, Seq[Slot]] = restaurantTableIds.map { restaurantTableId =>
        restaurantTableId ->
          (startHour to endHour).map(hour => Slot(getSlotId, restaurantTableId, new LocalTime(hour, 0),
            new LocalTime(hour + 1, 0), None))
      }.toMap

      val slotsOfAllTables: Iterable[Slot] = slotsByRestaurantTableId.values.flatten
      allSlots += slotsOfAllTables

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
        slot.copy(activeBookingId = Some(bookingId))
        addOrUpdateSlot(slot)
      }

      throw SlotAlreadyBooked(slotId)
    }

    def releaseSlot(slotId: Int): Int = {
      if (!isSlotAvailable(slotId)) {
        val slot = getSlotById(slotId)
        val c = slot.copy()
        addOrUpdateSlot(slot.copy(activeBookingId = None))
      }

      throw SlotAlreadyReleased(slotId)
    }

    // PRIVATE METHODS
    private def addOrUpdateSlot(slot: Slot): Int = {
      allSlots += slot
      slot.id
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


  /////////////////////////////////////////////////////////////////////////////
  case class RestaurantTable(id: Int, restaurantId: Int, slotIds: Seq[Int])

  object RestaurantTableStorageHandler {
    private val allRestaurantTables = Map.empty[Int, RestaurantTable]
    private var nextRestaurantTableId = 1

    def createNewRestaurantTables(restaurantId: Int, tableCount: Int): Seq[Int] = {
      val restaurantTables = (1 to tableCount).map { _ =>
        RestaurantTable(getRestaurantTableId, restaurantId, Seq.empty)
      }
      allRestaurantTables += restaurantTables
      restaurantTables.map(_.id)
    }

    def addSlots(slotsByRestaurantTableId: Map[Int, Seq[Int]]): Seq[Int] = {
      slotsByRestaurantTableId.map { case (tableId, newSlotIds) =>
        val table = getRestaurantTableById(tableId)
        addOrUpdateRestaurantTable(table.copy(slotIds = table.slotIds ++ newSlotIds))
      }.toSeq
    }

    // PRIVATE METHODS
    private def addOrUpdateRestaurantTable(restaurantTable: RestaurantTable): Int = {
      allRestaurantTables += restaurantTable
      restaurantTable.id
    }

    private def getRestaurantTableById(restaurantTableId: Int): RestaurantTable = {
      allRestaurantTables.getOrElse(restaurantTableId, throw objectNotAvailable[RestaurantTable](restaurantTableId))
    }

    private def getRestaurantTableId: Int = {
      val res = nextRestaurantTableId
      nextRestaurantTableId += 1
      res
    }
  }

  case class Restaurant(id: Int, name: String, restaurantTableIds: Seq[Int])

  object RestaurantStorageHandler {
    private val allRestaurants = Map.empty[Int, Restaurant]
    private var nextRestaurantId = 1

    def createNewRestaurant(name: String): Int = {
      val restaurant = Restaurant(getRestaurantId, name, Seq.empty[Int])
      allRestaurants += restaurant
      restaurant.id
    }

    def addTables(restaurantId: Int, tableIds: Seq[Int]): Int = {
      val res = getRestaurantById(restaurantId)
      val existingTables = res.restaurantTableIds
      addOrUpdateRestaurant(res.copy(restaurantTableIds = existingTables ++ tableIds))
    }

    // PRIVATE METHODS
    private def addOrUpdateRestaurant(restaurant: Restaurant): Int = {
      allRestaurants += restaurant
      restaurant.id
    }

    private def getRestaurantById(restaurantId: Int): Restaurant = {
      allRestaurants.getOrElse(restaurantId, throw objectNotAvailable[Restaurant](restaurantId))
    }

    private def getRestaurantId: Int = {
      val res = nextRestaurantId
      nextRestaurantId += 1
      res
    }
  }


  ////////////////////////////////////////////////////////////////////////////////////////////////////////
  object RestaurantRegistrationService {
    /*
    * 1. add res
    * 2. add tables
    * 3. add slots
    * */
    def addNewRestaurant(name: String, tableCount: Int): Int = {
      val restaurantId = RestaurantStorageHandler.createNewRestaurant(name)
      val restaurantTableIds = RestaurantTableStorageHandler.createNewRestaurantTables(restaurantId, tableCount)
      val _ = RestaurantStorageHandler.addTables(restaurantId, restaurantTableIds)
      val slotsByTableId = SlotStorageHandler.createNewSlot(9, 22, restaurantTableIds)
      val _ = RestaurantTableStorageHandler.addSlots(slotsByTableId)
      restaurantId
    }
  }
}
