package data_structures.graph

object HelloGraph extends App {
  val root = GraphNode("")
  root.insert(Seq("1A", "2A", "3A"))
  root.insert(Seq("1A", "2B", "3C"))
  root.insert(Seq("1B", "2B", "3A"))
  root.insert(Seq("1A", "2A", "3A"))
  root.print()
}
