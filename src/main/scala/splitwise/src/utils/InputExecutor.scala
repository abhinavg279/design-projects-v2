package splitwise.src.utils

import splitwise.src.service.{TransactionService, UserService}
import splitwise.src.utils.CustomException.WithErrorHandling

import scala.language.implicitConversions

object InputExecutor {
  def execute(input: String): Unit = {
    WithErrorHandling {
      val (command, args) = parseInput(input)
      command match {
        case RegisterUser =>
          // val name = args.getParam("name")
          UserService.registerNewUser()
        case CreateTransaction =>
          val lenderId = args.getParam("ld").toInt
          val borrowerId = args.getParam("bd").toInt
          val amount = args.getParam("at").toDouble
          TransactionService.createNewTransaction(lenderId, borrowerId, amount)
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