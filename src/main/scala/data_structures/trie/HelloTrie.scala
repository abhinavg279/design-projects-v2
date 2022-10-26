package data_structures.trie

object HelloTrie extends App {
  val root = TrieNode('*')
  root.insert("aBcdef")
  root.insert("abCd")
  root.insert("abcEf")
  root.insert("aberd")
  root.insert("abere")
  root.insert("aberf")
  root.insert("aberg")
  root.insert("abergh")
  root.print()

//  root.delete("abcdef")
//  root.print()
//  root.print()
//  println(root.find("abcd"))
//  println(root.delete("abcd"))
//  println(root.find("abcd"))
//  root.print()
//  println(root.collectAll())
//  println(root.collectMax(3))

//  println(root.getWords("abcd"))
  println(root.getTopWords("abe", 2))
}
