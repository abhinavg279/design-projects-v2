package splitwise.src.model.handler

import splitwise.src.model.UserUserAssociation

import java.util.concurrent.atomic.AtomicInteger
import scala.collection.mutable.{Map => MutableMap}


object UserUserAssociationStorageHandler {
  private val allUserUserAssociations: MutableMap[Int, UserUserAssociation] = MutableMap.empty
  private val nextUserUserAssociationId: AtomicInteger = new AtomicInteger(1)

  def addUserUserAssociation(lenderId: Int, borrowerId: Int, amount: BigDecimal): Int = {
    getUserUserAssociation(lenderId, borrowerId) match {
      case Some(userUserAssociation) =>
        addOrUpdateUserUserAssociation(UserUserAssociation(userUserAssociation.id, lenderId, borrowerId, amount))
      case None =>
        getUserUserAssociation(borrowerId, lenderId) match {
          case Some(userUserAssociation) =>
            addOrUpdateUserUserAssociation(UserUserAssociation(userUserAssociation.id, lenderId, borrowerId, amount))
          case None =>
            addOrUpdateUserUserAssociation(UserUserAssociation(getUserUserAssociationId, lenderId, borrowerId, amount))
        }
    }
  }

  def getUserUserAssociation(userIds: Seq[Int]): Seq[UserUserAssociation] = {
    allUserUserAssociations.collect {
      case (_, association) if userIds.contains(association.lenderId) || userIds.contains(association.borrowerId) =>
        association
    }.toSeq
  }

  def setAmountToZero(associationIds: Seq[Int]): Seq[Int] = {
    allUserUserAssociations.collect {
      case (id, association) if associationIds.contains(id) =>
        addOrUpdateUserUserAssociation(association.copy(amount = 0))
    }.toSeq
  }

  def getUserUserAssociation(lenderId: Int, borrowerId: Int): Option[UserUserAssociation] = {
    allUserUserAssociations.collectFirst {
      case (_, association) if association.lenderId == lenderId && association.borrowerId == borrowerId =>
        association
    }
  }

  def getAll: Seq[UserUserAssociation] = allUserUserAssociations.values.toSeq

  // PRIVATE METHODS
  private def addOrUpdateUserUserAssociation(association: UserUserAssociation): Int = {
    allUserUserAssociations.addOne((association.id, association))
    //println(allUserUserAssociations)
    association.id
  }

  private def getUserUserAssociationId: Int = {
    nextUserUserAssociationId.getAndIncrement()
  }
}
