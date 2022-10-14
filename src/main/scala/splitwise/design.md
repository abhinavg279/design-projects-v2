// 7:20 - 8:20
// 11:30 - 12:30
UserUserAssociation
UserGroupAssociation
Transactions
    Lender
    Borrower
    Amount


## triggers
1. add user to group
2. Add transaction
   1. add transaction record
   2. get all the user group, all the users in those groups
   3. get all the useruserassociations for these users, and calculate net amount against each user
   4. now run the algo and update the useruserassociations

## algo
1. list all the users and the corresponding amount of lent or borrowed money
2. make two priority_queues keeps matching top ones