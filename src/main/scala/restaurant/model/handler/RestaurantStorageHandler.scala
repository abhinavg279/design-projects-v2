package restaurant.model.handler

import restaurant.model.Restaurant
import restaurant.utils.errors.Error.objectNotAvailable

object RestaurantStorageHandler {
  private var allRestaurants = Map.empty[Int, Restaurant]
  private var nextRestaurantId = 1

  def createNewRestaurant(name: String): Int = {
    val restaurant = Restaurant(getRestaurantId, name, Seq.empty[Int])
    addOrUpdateRestaurant(restaurant)
  }

  def addTables(restaurantId: Int, tableIds: Seq[Int]): Int = {
    val res = getRestaurantById(restaurantId)
    val existingTables = res.restaurantTableIds
    addOrUpdateRestaurant(res.copy(restaurantTableIds = existingTables ++ tableIds))
  }

  // PRIVATE METHODS
  private def addOrUpdateRestaurant(restaurant: Restaurant): Int = {
    allRestaurants += ((restaurant.id, restaurant))
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
