package data_structures.graph

import scala.annotation.tailrec
import scala.collection.mutable.{Set => MutableSet}


case class GraphNode(value: String) {
  var count = 0
  val nodes: MutableSet[GraphNode] = MutableSet.empty

  @tailrec
  final def insert(value: Seq[String], delta: Int = 1) : Unit = {
    count += delta
    if (value.nonEmpty) {
      nodes.find(_.value == value.head) match {
        case Some(newNode) =>
          newNode.insert(value.tail, delta)
        case None =>
          val newNode = GraphNode(value.head)
          nodes.addOne(newNode)
          // println(s"added ${value.head}")
          newNode.insert(value.tail, delta)
      }
    }

    else ()
  }

  @tailrec
  final def getCount(values: Seq[String]): Int = {
    if (values.isEmpty) {
      this.count
    } else {
      this.nodes.find(_.value == values.head) match {
        case Some(nextNode) =>
          nextNode.getCount(values.tail)
        case None =>
          0
      }
    }
  }

  final def delete(values: Seq[String]): Boolean = {
    if (values.nonEmpty) {
      val isDeleted = this.nodes.find(_.value == values.head) match {
        case Some(nextNode) =>
          if (values.size == 1) {
            // println(s"Removing ${nextNode.value}")
            this.nodes.remove(nextNode)
          }
          else nextNode.delete(values.tail)
        case None => false
      }

      if (isDeleted) {
        this.count = this.count - 1
      }

      isDeleted
    }

    else {
      false
    }
  }

  final def print(): Unit = {
    println(s"${this.value}, ${this.count}")
    this.nodes.foreach(a => a.print())
  }
}


