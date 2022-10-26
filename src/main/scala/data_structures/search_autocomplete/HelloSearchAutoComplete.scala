package data_structures.search_autocomplete

object HelloSearchAutoComplete extends App {
  val root = new AutoCompleteNode(' ')
  root.insert("i love ironman", 10)
  root.insert("i love leetcode", 4)
  root.insert("ihits", 12)
  root.print()
}
