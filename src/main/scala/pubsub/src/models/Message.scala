package pubsub.src.models

class Message(value: String) {
  override def hashCode(): Int = value.hashCode()
  override def toString: String = s"MESSAGE=$value"
}