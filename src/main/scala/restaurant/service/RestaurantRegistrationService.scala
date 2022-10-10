package restaurant.service

import restaurant.model.handler.{RestaurantStorageHandler, RestaurantTableStorageHandler, SlotStorageHandler}

object RestaurantRegistrationService {
  /*
  * 1. add res
  * 2. add tables
  * 3. add slots
  * */
  def addNewRestaurant(name: String, startHour: Int, endHour: Int, tableCount: Int): Int = {
    val restaurantId = RestaurantStorageHandler.createNewRestaurant(name)
    val restaurantTableIds = RestaurantTableStorageHandler.createNewRestaurantTables(restaurantId, tableCount)
    val _ = RestaurantStorageHandler.addTables(restaurantId, restaurantTableIds)
    val slotsByTableId = SlotStorageHandler.createNewSlot(startHour, endHour, restaurantTableIds)
    val _ = RestaurantTableStorageHandler.addSlots(slotsByTableId)
    restaurantId
  }
}
