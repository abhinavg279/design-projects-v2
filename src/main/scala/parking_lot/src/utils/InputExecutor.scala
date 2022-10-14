package parking_lot.src.utils

import restaurant.service.RestaurantRegistrationService
import restaurant.utils.CustomException.WithErrorHandling

import scala.language.implicitConversions

object InputExecutor {
  def execute(input: String): Unit = {
    WithErrorHandling {
      val (command, args) = parseInput(input)
      command match {
        case RegisterRestaurant =>
          val name = args.getParam("name")
          val startHour = args.getParam("start-hour").toInt
          val endHour = args.getParam("end-hour").toInt
          val tableCount = args.getParam("table-count").toInt
          RestaurantRegistrationService.addNewRestaurant(name, startHour, endHour, tableCount)
      }
    }
  }

  // PRIVATE METHODS
  private def parseInput(input: String) = {
    val s = input.split(' ').toSeq
    val (command, rest) = (s.head, s.tail)

    val args: Map[String, String] = rest.map { str =>
      val keyAndValue = str.split('=').toSeq

      if(keyAndValue.size != 2) throw illegalInput(input)
      val (key, value) = (keyAndValue.head, keyAndValue.last)

      if (key.startsWith("--")) {
        key.substring(2) -> value
      } else {
        throw new Exception(s"Failed to parse $input")
      }
    }.toMap

    (Commands.fromString(command), args)
  }

  private def illegalInput(input: String) = new Exception(s"Bad input $input")

  implicit class ArgumentParser(args: Map[String, String]) {
    def getParam(key: String): String = {
      args.getOrElse(key, throw new Exception(s"$key is required"))
    }
  }
}