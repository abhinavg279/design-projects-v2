package splitwise.src.service

import splitwise.src.model.User
import splitwise.src.model.handler.UserStorageHandler

object UserService {
  def registerNewUser(): Int = {
    UserStorageHandler.addUser()
  }

  def getUser(id: Int): User = {
    UserStorageHandler.getUser(id)
  }
}
