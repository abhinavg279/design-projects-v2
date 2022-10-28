package data_structures.trie_v2

import scala.collection.mutable.{TreeMap => MutableMap}

case class Node(value: Char) {
  var count: Int = 0
  var wordEnds: Int = 0
  var nextNodes: MutableMap[Char, Node] = MutableMap.empty[Char, Node]

  def insert(values: Seq[Char]): Unit = {
    count += 1

    if(values.nonEmpty) {
      nextNodes.get(values.head) match {
        case Some(nextNode) =>
          nextNode.insert(values.tail)
        case None =>
          val newNode = Node(values.head)
          nextNodes.addOne((values.head, newNode))
          newNode.insert(values.tail)
      }
    } else wordEnds += 1
  }

  def delete(values: Seq[Char]): Boolean = {
    val isDeleted = {
      if(values.nonEmpty) {
        nextNodes.get(values.head) match {
          case Some(nextNode) => nextNode.delete(values.tail)
          case None => false
        }
      }

      else {
        if(wordEnds > 0) {
          wordEnds -= 1
          true
        }

        else false
      }
    }

    if(isDeleted) {
      count -= 1
      if(values.nonEmpty) nextNodes.get(values.head).map { nextNode =>
        if(nextNode.count == 0) nextNodes.remove(values.head)
      }
    }

    isDeleted
  }

  def collectAll(values: Seq[Char]): Seq[Seq[Char]] = {
    if(values.nonEmpty) {
      nextNodes.get(values.head) match {
        case Some(nextNode) => nextNode.collectAll(values.tail)
        case None => Seq.empty
      }
    } else collectAll()
  }

  def collectTop(count: Int): Seq[Seq[Char]] = {
    val updatedCount = count - wordEnds


    val nextPaths = nextNodes.toSeq.sortBy(_._2.count).reverse.foldLeft((Seq.empty[Seq[Char]], updatedCount)) {
      case ((paths, remaining), (_, nextNode)) =>
        if(remaining > nextNode.count) {
          (paths ++ nextNode.collectTop(nextNode.count), remaining - nextNode.count)
        } else if (remaining > 0) {
          (paths ++ nextNode.collectTop(remaining), 0)
        } else {
          (paths, 0)
        }
    }._1

    nextPaths.map(path => value +: path) ++ (1 to wordEnds).map(_ => Seq(value))
  }

  def collectAll(): Seq[Seq[Char]] = {
    val nextPaths = nextNodes.flatMap(_._2.collectAll()).toSeq
    nextPaths.map(path => value +: path) ++ (1 to wordEnds).map(_ => Seq(value))
  }

  final def print(): Unit = {
    println(s"Char: $value, Count: $count, WordEnds: $wordEnds")
    nextNodes.foreach(_._2.print())
  }
}

