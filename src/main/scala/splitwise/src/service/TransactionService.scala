package splitwise.src.service

import splitwise.src.model.UserUserAssociation
import splitwise.src.model.handler.{TransactionStorageHandler, UserUserAssociationStorageHandler}

import scala.collection.mutable
import scala.collection.mutable.{Map => MutableMap}


object TransactionService {
  def getAllUserUserAssociation: Seq[UserUserAssociation] = {
    UserUserAssociationStorageHandler.getAll
  }


  /**
   * 1. add transaction record
   * 2. get all the user group, all the users in those groups
   * 3. get all the useruserassociations for these users, and calculate net amount against each user
   * 4. now run the algo and update the useruserassociations
   *
   *
   * 1. list all the users and the corresponding amount of lent or borrowed money
   * 2. make two priority_queues keeps matching top ones
   */

  def createNewTransaction(lenderId: Int, borrowerId: Int, amount: BigDecimal): Unit = this.synchronized {
    val _ = UserService.getUser(lenderId)
    val _ = UserService.getUser(borrowerId)
    val _ = TransactionStorageHandler.addTransaction(lenderId, borrowerId, amount)
    val allUserAssociation = UserUserAssociationStorageHandler.getUserUserAssociation(Seq(lenderId, borrowerId))
    UserUserAssociationStorageHandler.setAmountToZero(allUserAssociation.map(_.id))
    val amountLent = MutableMap[Int, BigDecimal]((lenderId, amount), (borrowerId, -amount))
    allUserAssociation.foreach { association: UserUserAssociation =>
      amountLent.get(association.lenderId) match {
        case Some(currentLentAmount) => amountLent.addOne((association.lenderId, currentLentAmount + association.amount))
        case None => amountLent.addOne((association.lenderId, association.amount))
      }

      amountLent.get(association.borrowerId) match {
        case Some(currentLentAmount) => amountLent.addOne((association.borrowerId, currentLentAmount - association.amount))
        case None => amountLent.addOne((association.borrowerId, -association.amount))
      }
    }

    def comp(a: (Int, BigDecimal), b: (Int, BigDecimal)): Boolean = a._2 < b._2

    val borrowers = new mutable.PriorityQueue[(Int, BigDecimal)]()(Ordering.fromLessThan[(Int, BigDecimal)](comp))
    val lenders = new mutable.PriorityQueue[(Int, BigDecimal)]()(Ordering.fromLessThan[(Int, BigDecimal)](comp))

    amountLent.foreach { case (userId, amount) =>
      if (amount < 0) {
        borrowers.addOne((userId, -amount))
      } else {
        lenders.addOne((userId, amount))
      }
    }

    while (borrowers.nonEmpty) {
      val borrowerHere = borrowers.dequeue()
      val lenderHere = lenders.dequeue()

      if (borrowerHere._2 < lenderHere._2) {
        lenders.addOne((lenderHere._1, lenderHere._2 - borrowerHere._2))
        UserUserAssociationStorageHandler.addUserUserAssociation(lenderHere._1, borrowerHere._1, borrowerHere._2)
      } else if (borrowerHere._2 > lenderHere._2) {
        borrowers.addOne((borrowerHere._1, borrowerHere._2 - lenderHere._2))
        UserUserAssociationStorageHandler.addUserUserAssociation(lenderHere._1, borrowerHere._1, lenderHere._2)
      } else {
        UserUserAssociationStorageHandler.addUserUserAssociation(lenderHere._1, borrowerHere._1, lenderHere._2)
      }
    }
  }
}
