package data_structures.trie

object HelloTrie extends App {
  val root = TrieNode('*')
  root.insert("aBcdef")
  root.insert("abCd")
  root.insert("abcEf")
  root.print()

  root.delete("abcdef")

  root.print()

//  root.print()
//  println(root.find("abcd"))
//  println(root.delete("abcd"))
//  println(root.find("abcd"))
//  root.print()

  println(root.collectAll())
}
