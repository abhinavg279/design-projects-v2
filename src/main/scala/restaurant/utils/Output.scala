package restaurant.utils

sealed trait Output {
  def name: String
  def send(message: Any): Unit = {
    println(s"[$name] $message")
  }
}

object Terminal extends Output {
  override def name: String = "TERMINAL"
}

object Response extends Output {
  override def name: String = "RESPONSE"
}