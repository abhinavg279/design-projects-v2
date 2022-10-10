# Models
## User
1. userId
2. metadata

## UserGroup
1. userGroupId
2. metadata

## UserUserGroupAssociation
1. userId
2. userGroupId

## Transactions
1. userId1
2. userId2
3. userGroupId
4. amount
5. transactionType
   1. Lent
   2. Borrowed
   3. Settlement, can only be done by the one who owes, can be considered as Lent 

## PendingSettlements (Not a source of truth)
1. userId1
2. userId2
3. amount
4. settlementType
   1. Positive
   2. Negative

# Services
## TransactionDAO
1. createTransaction()
2. getUserTransactions()

## UserDAO
1. createUser()
2. createGroup()
3. addUserToGroup()

## SettlementDAO
1. createOrUpdatePendingSettlements()
2. getUserPendingSettlements()

## AccountService
1. createTransaction()
   1. calculateAndUpdatePendingSettlements()
2. getUserTransactions()
3. getUserPendingSettlements()

# Algorithms
## Calculate pending settlements
1. get all the transaction for a user, where he may be lender or borrower
2. resolve it and update in PendingSettlements


































