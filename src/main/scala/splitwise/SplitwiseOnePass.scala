package splitwise

import scala.collection.mutable.{ Set => MutableSet, PriorityQueue => MutablePriorityQueue, Map => MutableMap }

object SplitwiseOnePass {
  case class ResolvedTransactions(lender: Int, borrower: Int, amount: BigDecimal)
  var allResolvedTransactions: MutableSet[ResolvedTransactions] = MutableSet.empty[ResolvedTransactions]

  def addTransactions(lender: Int, borrower: Int, amount: BigDecimal): Unit = {
    val users = Set(lender, borrower)
    val affectedUsers: MutableSet[Int] = allResolvedTransactions.collect {
      case s if users.contains(s.lender) || users.contains(s.borrower) =>
        Set(s.lender, s.borrower)
    }.flatten

    val affectedResolvedTransactions: MutableSet[ResolvedTransactions] = allResolvedTransactions.collect {
      case s if affectedUsers.contains(s.lender) || affectedUsers.contains(s.borrower) => s
    }

    val lentAmounts: MutableMap[Int, BigDecimal] = MutableMap((lender, amount), (borrower, -amount))
    affectedResolvedTransactions.foreach { case ResolvedTransactions(lender, borrower, amount) =>
      lentAmounts.get(lender) match {
        case Some(lentAmount) => lentAmounts.addOne((lender, lentAmount + amount))
        case None => lentAmounts.addOne((lender, amount))
      }

      lentAmounts.get(borrower) match {
        case Some(lentAmount) => lentAmounts.addOne((borrower, lentAmount - amount))
        case None => lentAmounts.addOne((borrower, -amount))
      }
    }

    val lenders = MutablePriorityQueue.empty[(BigDecimal, Int)](Ordering.fromLessThan((a, b) => a._1 > b._1))
    val borrowers = MutablePriorityQueue.empty[(BigDecimal  , Int)](Ordering.fromLessThan((a, b) => a._1 > b._1))

    lentAmounts.foreach { case (lender, amount) =>
      if (amount < 0) {
        borrowers.addOne((-amount, lender))
      }

      else if (amount > 0) {
        lenders.addOne((amount, lender))
      }
    }

    val newResolvedTransactions = MutableSet.empty[ResolvedTransactions]
    while(lenders.nonEmpty) {
      val (lentAmount, lenderId) = lenders.dequeue()
      val (borrowedAmount, borrowerId) = borrowers.dequeue()
      if (borrowedAmount > lentAmount) {
        newResolvedTransactions.addOne(ResolvedTransactions(lenderId, borrowerId, lentAmount))
        borrowers.addOne((borrowedAmount - lentAmount, borrowerId))
      }

      else if (lentAmount > borrowedAmount) {
        newResolvedTransactions.addOne(ResolvedTransactions(lenderId, borrowerId, borrowedAmount))
        lenders.addOne((lentAmount - borrowedAmount, lenderId))
      }

      else {
        newResolvedTransactions.addOne(ResolvedTransactions(lenderId, borrowerId, lentAmount))
      }
    }

    // replace transactions
    affectedResolvedTransactions.foreach(allResolvedTransactions.remove(_))
    allResolvedTransactions.addAll(newResolvedTransactions)

    println(allResolvedTransactions)
  }
}

