package splitwise

import splitwise.src.service.TransactionService
import splitwise.src.utils.InputExecutor


object HelloSplitwise extends App {

  while(true) {
    val input = scala.io.StdIn.readLine()
    InputExecutor.execute(input)
    println(s"USER_ASSOCIATIONS = ${TransactionService.getAllUserUserAssociation}")
  }


//  val userId1: Int = UserService.registerNewUser()
//  val userId2: Int = UserService.registerNewUser()
//  val userId3: Int = UserService.registerNewUser()
//
//  // print initial state
//  println(TransactionService.getAllUserUserAssociation)
//
//  // add transaction and get updated state
//  val _ = TransactionService.createNewTransaction(userId1, userId2, 10); println(TransactionService.getAllUserUserAssociation)
//  val _ = TransactionService.createNewTransaction(userId2, userId1, 90); println(TransactionService.getAllUserUserAssociation)
//  val _ = TransactionService.createNewTransaction(userId1, userId3, 40); println(TransactionService.getAllUserUserAssociation)
//  val _ = TransactionService.createNewTransaction(userId1, userId3, 50); println(TransactionService.getAllUserUserAssociation)
//  val _ = TransactionService.createNewTransaction(userId3, userId2, 90); println(TransactionService.getAllUserUserAssociation)
}
