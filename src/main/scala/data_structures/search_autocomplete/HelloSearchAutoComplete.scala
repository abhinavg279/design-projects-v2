package data_structures.search_autocomplete

object HelloSearchAutoComplete extends App {
  val root = new AutoCompleteNode(' ')
  root.insert("abcd", 10)
  root.insert("abce", 4)
  root.insert("abcf", 12)
  root.insert("abef", 15)
  println(root.getTopHits("abcd", 2))
}
