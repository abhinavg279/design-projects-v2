package restaurant.model

case class RestaurantTable(id: Int, restaurantId: Int, slotIds: Seq[Int])
