package restaurant.model.handler

import restaurant.model.RestaurantTable
import restaurant.utils.CustomException.objectNotAvailable

import scala.collection.immutable

object RestaurantTableStorageHandler {
  private var allRestaurantTables = Map.empty[Int, RestaurantTable]
  private var nextRestaurantTableId = 1

  def getAllTables(restaurantId: Int): immutable.Iterable[Int] = {
    allRestaurantTables.collect { case (_, table) if table.restaurantId == restaurantId => table.id }
  }

  def createNewRestaurantTables(restaurantId: Int, tableCount: Int): Seq[Int] = {
    val restaurantTables = (1 to tableCount).map { _ =>
      RestaurantTable(getRestaurantTableId, restaurantId, Seq.empty)
    }
    addOrUpdateRestaurantTable(restaurantTables)
  }

  def addSlots(slotsByRestaurantTableId: Map[Int, Seq[Int]]): Seq[Int] = {
    slotsByRestaurantTableId.map { case (tableId, newSlotIds) =>
      val table = getRestaurantTableById(tableId)
      addOrUpdateRestaurantTable(Seq(table.copy(slotIds = table.slotIds ++ newSlotIds))).head
    }.toSeq
  }

  // PRIVATE METHODS
  private def addOrUpdateRestaurantTable(restaurantTables: Seq[RestaurantTable]): Seq[Int] = {
    allRestaurantTables ++= restaurantTables.map(rt => (rt.id, rt))
    restaurantTables.map(_.id)
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
