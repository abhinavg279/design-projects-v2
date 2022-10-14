package splitwise.src.model.handler

import splitwise.src.model.User
import splitwise.src.utils.CustomException.objectNotAvailable

import java.util.concurrent.atomic.AtomicInteger
import scala.collection.mutable.{Map => MutableMap}


object UserStorageHandler {
  private val allUsers: MutableMap[Int, User] = MutableMap.empty
  private val nextUserId: AtomicInteger = new AtomicInteger(1)

  def getUser(id: Int): User = {
    allUsers.getOrElse(id, throw objectNotAvailable[User](id))
  }

  def addUser(): Int = {
    addOrUpdateUser(User(getUserId, 0, "LENT"))
  }

  // PRIVATE METHODS
  private def addOrUpdateUser(association: User): Int = {
    allUsers.addOne((association.userId, association))
    //println(allUsers)
    association.userId
  }

  private def getUserId: Int = {
    nextUserId.getAndIncrement()
  }
}
