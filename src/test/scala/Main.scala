import restaurant_service.models.RestaurantDAO
import restaurant_service.utils.H2Database
import com.typesafe.config.ConfigFactory
import restaurant_service.models.Restaurants
import slick.basic.DatabaseConfig
import slick.jdbc.H2Profile.api._
import slick.jdbc.JdbcProfile

import scala.concurrent.Await
import scala.concurrent.duration.Duration
import scala.concurrent.ExecutionContext.Implicits.global

object Main extends App {
  // initialize
  H2Database

  val f = for {
    restaurantId <- H2Database.db.run(RestaurantDAO.addNewRestaurant("Rest1"))
    restaurantId <- H2Database.db.run(RestaurantDAO.addNewRestaurant("Rest2"))
    restaurantId <- H2Database.db.run(RestaurantDAO.addNewRestaurant("Rest2"))
    restaurants <- H2Database.db.run(RestaurantDAO.getAllRestaurants)
  } yield {
    print(restaurants)
  }

  Await.result(f, Duration.Inf)
}
