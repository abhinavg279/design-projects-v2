package data_structures.search_autocomplete

import scala.collection.mutable.{Map => MutableMap, TreeSet => MutableTreeSet}

case class AutoCompleteNode(value: Char) {
  var nextCharactersWithWeight: MutableTreeSet[(Int, Char)] = MutableTreeSet.empty(Ordering.fromLessThan((a, b) => a._1 > b._1))
  var nextNodes: MutableTreeSet[AutoCompleteNode] = MutableTreeSet.empty(Ordering.fromLessThan((a, b) => a.value < b.value))
  var isWordEnd: Int = 0

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

    else isWordEnd += 1
  }

  def getTopHits(values: Seq[Char], count: Int): Seq[Seq[Char]] = {
    _getTopHits(values, count).map(tail => values ++ tail.tail)
  }

  def _getTopHits(values: Seq[Char], count: Int): Seq[Seq[Char]] = {
    if(values.nonEmpty) {
      nextNodes.find(_.value == values.head) match {
        case Some(nextNode) =>
          if(values.size == 1) nextNode.getTopHits(count)
          else nextNode._getTopHits(values.tail, count)

        case None => Seq.empty
      }
    } else Seq.empty
  }

  def getTopHits(count: Int): Seq[Seq[Char]] = {
    val updatedCount = count - isWordEnd

    val nextTopValues = nextCharactersWithWeight.foldLeft((MutableMap.empty[Char, Int], updatedCount)) {
      case ((res, remaining), nextCharWithWeight: (Int, Char)) =>
        if(remaining > 0) res.get(nextCharWithWeight._2) match {
          case Some(i) => (res.addOne((nextCharWithWeight._2, i+1)), remaining-1)
          case None => (res.addOne((nextCharWithWeight._2, 1)), remaining-1)
        } else {
          (res, 0)
        }
    }._1.toSeq

    val paths: Seq[Seq[Char]] = nextTopValues.flatMap { case (c, i) =>
      nextNodes.find(_.value == c) match {
        case Some(nextNode) => nextNode.getTopHits(i)
        case None => Seq.empty
      }
    }

    val res = paths.map((path: Seq[Char]) => value +: path) ++ (1 to isWordEnd).map(_ => Seq(value))
    if(paths.isEmpty) Seq(Seq(value))
    else res
  }

  def print(): Unit = {
    println(s"$value: $isWordEnd: $nextCharactersWithWeight")
    nextNodes.foreach(_.print())
  }
}
