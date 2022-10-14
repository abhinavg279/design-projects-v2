package splitwise.src.model.handler


import splitwise.src.model.Transaction

import java.util.concurrent.atomic.AtomicInteger
import scala.collection.mutable.{Map => MutableMap}


object TransactionStorageHandler {
  private val allTransactions: MutableMap[Int, Transaction] = MutableMap.empty
  private val nextTransactionId: AtomicInteger = new AtomicInteger(1)

  def addTransaction(lenderId: Int, borrowerId: Int, amount: BigDecimal): Int = {
    addOrUpdateTransaction(Transaction(getTransactionId, lenderId, borrowerId, amount))
  }

  // PRIVATE METHODS
  private def addOrUpdateTransaction(transaction: Transaction): Int = {
    allTransactions.addOne((transaction.transactionId, transaction))
    //println(allTransactions)
    transaction.transactionId
  }

  private def getTransactionId: Int = {
    nextTransactionId.getAndIncrement()
  }
}