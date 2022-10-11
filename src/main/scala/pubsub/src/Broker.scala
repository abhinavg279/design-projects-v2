package pubsub.src

import pubsub.src.models.{Message, Topic}

import scala.collection.mutable.{Map => MutableMap, Queue => MutableQueue, Set => MutableSet}

private object Broker {
  var messages: MutableMap[Int, Message] = MutableMap.empty
  var queues: MutableMap[Subscriber, MutableMap[Topic, MutableQueue[Int]]] = MutableMap.empty
  var subscribers: MutableMap[Topic, MutableSet[Subscriber]] = MutableMap.empty

  def subscribe(topic: Topic, subscriber: Subscriber): Unit = {
    subscribers.get(topic).map { s => s.addOne(subscriber) } match {
      case Some(_) =>
      case None =>
        subscribers.addOne((topic, MutableSet[Subscriber](subscriber)))
    }
  }

  def addNewMessage(topic: Topic, message: Message): Unit = {
    val subs = subscribers.getOrElse(topic, MutableSet.empty[Subscriber])
    val key = message.hashCode()
    messages.addOne((key, message))

    subs.foreach { subscriber =>
      val topicsWithQueuesOpt: Option[MutableMap[Topic, MutableQueue[Int]]] = queues.get(subscriber)
      topicsWithQueuesOpt match {
        case Some(topicsWithQueues) =>
          topicsWithQueues.get(topic) match {
            case Some(queue) =>
              queue.enqueue(key)
            case None =>
              topicsWithQueues.addOne((topic, MutableQueue[Int](key)))
          }
        case None =>
          queues.addOne((subscriber, MutableMap((topic, MutableQueue[Int](key)))))
      }
    }

    // println(queues)
  }

  def getMessages(subscriber: Subscriber): Option[(Topic, Message)] = {
    val allQueues = queues.getOrElse(subscriber, Map.empty[Topic, MutableQueue[Int]])
    allQueues.collectFirst { case (topic, messagesQueue) if messagesQueue.nonEmpty =>
      val messageId = messagesQueue.head
      (topic, messages.getOrElse(messageId, throw new IllegalStateException()))
    }
  }

  def markMessageAsRead(subscriber: Subscriber, topic: Topic, message: Message): Unit = {
    val queue = queues.getOrElse(subscriber, throw new IllegalStateException())
      .getOrElse(topic, throw new IllegalStateException())
    if(queue.isEmpty || queue.head != message.hashCode())
      throw new IllegalStateException()
    else queue.dequeue()

    // println(queues)
  }
}
