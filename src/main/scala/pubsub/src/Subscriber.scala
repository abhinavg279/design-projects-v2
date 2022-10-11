package pubsub.src

import pubsub.src.models.{Message, Topic}

import scala.util.{Failure, Success, Try}

trait Subscriber {
  def name: String
  def processMessage(topic: Topic, message: Message): Unit

  override def toString: String = s"SUBSCRIBER=$name"

  def subscribe(topic: Topic): Unit = {
    Broker.subscribe(topic, this)
  }

  def fetchAndConsumeNext(): Unit = {
    Broker.getMessages(this) match {
      case Some((topic: Topic, message: Message)) =>
        Try {
          println(s"[$name] Processing message $message on topic $topic")
          processMessage(topic, message)
        } match {
          case Failure(exception) =>
            println(s"[$name] Failed to process message $message due to ${exception.getMessage}")
          case Success(value) =>
            markMessageAsRead(topic, message)
        }

      case None => println(s"[$name] Their is no more messages to consume")
    }
  }

  private def markMessageAsRead(topic: Topic, message: Message): Unit = {
    Broker.markMessageAsRead(this, topic, message)
  }
}