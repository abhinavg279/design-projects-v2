package data_structures.trie

import scala.annotation.tailrec
import scala.collection.mutable.{Set => MutableSet}

class TrieNode {
  var value: Char = ' '
  var count: Int = 0
  var isWordEnd: Int = 0
  var nextNodes: MutableSet[TrieNode] = MutableSet.empty[TrieNode]

  def insert(string: String): Unit = {
    insert(string.toCharArray.toSeq.map(_.toLower))
  }

  @tailrec
  private def insert(values: Seq[Char]): Unit = {
    count += 1
    if(values.isEmpty) isWordEnd += 1
    else {
      nextNodes.find(_.value == values.head) match {
        case Some(nextNode) =>
          nextNode.insert(values.tail)
        case None =>
          var nextNode = TrieNode(values.head)
          nextNodes.addOne(nextNode)
          nextNode.insert(values.tail)
      }
    }
  }


  def find(string: String): Seq[(Char, Int, Boolean)] = {
    find(string.toCharArray.toSeq.map(_.toLower))
  }

  private def find(values: Seq[Char]): Seq[(Char, Int, Boolean)] = {
    if(values.nonEmpty) {
      nextNodes.find(_.value == values.head) match {
        case Some(nextNode) =>
          var wordEnd = values.size == 1 && nextNode.isWordEnd > 0
          (nextNode.value, nextNode.count, wordEnd) +: nextNode.find(values.tail)
        case None =>
          Seq.empty
      }
    } else {
      Seq.empty
    }
  }

  def delete(values: Seq[Char]): Boolean = {
    val isDeleted = {
      if (values.isEmpty) false
      else {
        nextNodes.find(_.value == values.head) match {
          case Some(nextNode) =>
            if (nextNode.isWordEnd > 0 && values.size == 1) {
              nextNode.isWordEnd -= 1
              nextNode.count -= 1
              true
            }

            else nextNode.delete(values.tail)
          case None =>
            false
        }
      }
    }

    if(isDeleted) count -= 1
    nextNodes.find(_.count == 0).map(nextNodes.remove)
    isDeleted
  }

  def collectAll(): MutableSet[Seq[Char]] = {
    val paths: MutableSet[Seq[Char]] = nextNodes.flatMap(_.collectAll())
    val res = paths.map((path: Seq[Char]) => value +: path).addAll((1 to isWordEnd).map(_ => Seq(value)))
    if(res.isEmpty) MutableSet(Seq(value))
    else res
  }

  def print(): Unit = {
    println(s"($value, $count, $isWordEnd)")
    nextNodes.foreach(_.print())
  }
}

object TrieNode {
  def apply(value: Char): TrieNode = {
    var node = new TrieNode
    node.value = value.toLower
    node
  }
}
