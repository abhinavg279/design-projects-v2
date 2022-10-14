package splitwise.src.utils

sealed trait Commands {
  def asString: String
}

object Commands {
  def fromString(string: String): Commands = {
    all.collectFirst { case ic if ic.asString == string => ic }.getOrElse(
      throw CustomException.objectNotAvailable[Commands](string))
  }

  private val all = Set(RegisterUser, CreateTransaction)
}

object RegisterUser extends Commands {
  override def asString: String = "RU"
}

object CreateTransaction extends Commands {
  override def asString: String = "CT"
}
