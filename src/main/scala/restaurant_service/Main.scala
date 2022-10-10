package restaurant_service

import org.joda.time.{DateTime, LocalTime}

import scala.reflect.ClassTag

object Main extends App {
  case class A(a: Int)

  val a = A(1)
  println(a)

  a.copy(a = 3)
  println(a)

  println(a.copy(a = 4))
}
