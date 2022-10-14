package splitwise.src.model

case class Transaction(transactionId: Int, lenderId: Int, borrowerId: Int, amount: BigDecimal)
