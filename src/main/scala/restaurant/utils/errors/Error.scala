package restaurant.utils.errors

import scala.reflect.ClassTag
import scala.util.{Failure, Success, Try}

object Error {
  trait KnownException extends Throwable {
    def message: String
    override def getMessage: String = message
  }

  case class ObjectNotAvailable(id: Int, className: String) extends KnownException {
    override def message: String = s"$className with id $id could not be found"
  }

  case class SlotAlreadyBooked(id: Int) extends Exception(s"Slot with ${id} is already booked")

  case class SlotAlreadyReleased(id: Int) extends Exception(s"Slot with ${id} is already released")

  def objectNotAvailable[T](id: Int)(implicit classTag: ClassTag[T]): ObjectNotAvailable = {
    val className = classTag.runtimeClass.getName
    ObjectNotAvailable(id, className)
  }

  object WithErrorHandling {
    def apply[T](f: => T): Unit  = {
      Try {
        f
      } match {
        case Failure(e: ObjectNotAvailable) => println(s"${e.getMessage}. Please try again.")
        case Failure(e) => println(e.getMessage)
        case Success(value) => println(value)
      }
    }
  }
}
