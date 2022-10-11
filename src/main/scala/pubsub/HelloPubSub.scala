package pubsub

import pubsub.src.models.{Message, Topic}
import pubsub.src.{Publisher, Subscriber}

object HelloPubSub extends App {
  val sub1 = new Subscriber {
    override def name: String = "SUBSCRIBER-01"
    override def processMessage(topic: Topic, message: Message): Unit = ()
  }

  val pub1 = new Publisher {
    override def name: String = "PUBLISHER-01"
  }

  val topic1 = new Topic("TOPIC-01")
  val topic2 = new Topic("TOPIC-02")

  pub1.publish(topic1, new Message("Hello-01"))
  sub1.fetchAndConsumeNext()

  sub1.subscribe(topic1)
  sub1.subscribe(topic2)
  pub1.publish(topic1, new Message("Hello-01"))
  pub1.publish(topic2, new Message("Hello-02"))
  pub1.publish(topic1, new Message("Hello-03"))
  pub1.publish(topic1, new Message("Hello-04"))
  sub1.fetchAndConsumeNext()
  sub1.fetchAndConsumeNext()
  sub1.fetchAndConsumeNext()
  sub1.fetchAndConsumeNext()
  sub1.fetchAndConsumeNext()
}
