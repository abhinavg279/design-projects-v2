package restaurant.utils

import scala.reflect.ClassTag
import scala.util.{Failure, Success, Try}

object CustomException {
  trait UserActionException extends Throwable {
    def message: String
    override def getMessage: String = message
  }

  case class ObjectNotAvailable(id: Any, className: String) extends UserActionException {
    override def message: String = s"$className with id $id could not be found"
  }

  case class SlotAlreadyBooked(id: Int) extends UserActionException {
    override def message: String = s"Slot with $id is already booked"
  }

  case class SlotAlreadyReleased(id: Int) extends UserActionException {
    override def message: String = s"Slot with $id is already released"
  }

  def objectNotAvailable[T](id: Any)(implicit classTag: ClassTag[T]): ObjectNotAvailable = {
    val className: String = classTag.runtimeClass.getName.split('.').toSeq.last
    ObjectNotAvailable(id, className)
  }

  object WithErrorHandling {
    def apply[T](f: => T): Unit  = {
      Try {
        f
      } match {
        case Failure(e: UserActionException) => Response.send(s"${e.getMessage}. Please try again.")
        case Failure(e) =>
          Terminal.send(s"Unhandled error: ${e.getMessage}")
          Response.send(s"Something happened.")
        case Success(value) => Response.send(value)
      }
    }
  }
}
