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

  def collectMax(count: Int): MutableSet[Seq[Char]] = {
    val paths = nextNodes.toSeq.sortBy(_.count).reverse.foldLeft((MutableSet.empty[Seq[Char]], count)) {
      case ((res, remaining), node) =>
        if(remaining > node.count) (res.addAll(node.collectMax(node.count)), remaining - node.count)
        else if (remaining > 0) (res.addAll(node.collectMax(remaining)), 0)
        else (res, 0)
    }._1
    val res = paths.map((path: Seq[Char]) => value +: path).addAll((1 to isWordEnd).map(_ => Seq(value)))
    if (res.isEmpty) MutableSet(Seq(value))
    else res
  }

  def getTopWords(values: Seq[Char], count: Int): MutableSet[Seq[Char]] = {
    _getTopWords(values, count).map(t => values ++ t.tail)
  }

  def _getTopWords(values: Seq[Char], count: Int): MutableSet[Seq[Char]] = {
    if (values.nonEmpty) {
      nextNodes.find(_.value == values.head) match {
        case Some(nextNode) =>
          if (values.size == 1) nextNode.collectMax(count)
          else nextNode._getTopWords(values.tail, count)
        case None => MutableSet.empty[Seq[Char]]
      }
    } else MutableSet.empty[Seq[Char]]
  }

  def getWords2(values: Seq[Char]): MutableSet[Seq[Char]] = {
    _getTopWords(values, count).map(t => values ++ t.tail)
  }

  def getWords(values: Seq[Char]): MutableSet[Seq[Char]] = {
    _getWords(values).map(t => values ++ t.tail)
  }

  def _getWords(values: Seq[Char]): MutableSet[Seq[Char]] = {
    if(values.nonEmpty) {
      nextNodes.find(_.value == values.head) match {
        case Some(nextNode) =>
          if(values.size == 1) nextNode.collectAll()
          else nextNode._getWords(values.tail)
        case None => MutableSet.empty[Seq[Char]]
      }
    } else MutableSet.empty[Seq[Char]]
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
