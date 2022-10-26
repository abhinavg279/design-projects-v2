package data_structures.search_autocomplete

import scala.collection.mutable.{PriorityQueue => MutablePriorityQueue, Set => MutableSet}

case class AutoCompleteNode(value: Char) {
  var nextCharactersWithWeight: MutablePriorityQueue[(Int, Char)] = MutablePriorityQueue.empty(Ordering.fromLessThan((a, b) => a._1 < b._1))
  var nextNodes: MutableSet[AutoCompleteNode] = MutableSet.empty
  var isWordEnd: Boolean = false

  def insert(values: Seq[Char], weight: Int): Unit = {
    if(values.nonEmpty) {
      nextCharactersWithWeight.addOne((weight, values.head))
      nextNodes.find(_.value == values.head) match {
        case Some(nextNode) =>
          nextNode.insert(values.tail, weight)

        case None =>
          val nextNode = new AutoCompleteNode(values.head)
          nextNodes.addOne(nextNode)
          nextNode.insert(values.tail, weight)
      }
    }

    if(values.size == 1) isWordEnd = true
  }

  def getTopHits(count: Int) = {

  }

  def print(): Unit = {
    println(s"$value: $nextCharactersWithWeight")
    nextNodes.foreach(_.print())
  }
}
