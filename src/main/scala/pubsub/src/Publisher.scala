package pubsub.src

import pubsub.src.models.{Message, Topic}

trait Publisher {
  def name: String

  override def toString: String = s"PUBLISHER=$name"

  def publish(topic: Topic, message: Message): Unit = {
    Broker.addNewMessage(topic, message)
    println(s"[$name] Pushed message $message to topic $topic")
  }
}