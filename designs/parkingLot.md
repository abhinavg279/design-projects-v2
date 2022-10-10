## PS
1. Parking lot will have multiple types of parking
2. Parking lot will have multiple levels
3. Parking lot will have multiple entry exit points

## Objects
1. Parking Lot
    - Id
    - Name
    - Location
2. Parking Spot
    - Parking Spot Id
    - Parking Lot Id
    - Type of Vehicle
    - Floor-level
    - X-coordinate
    - Y-coordinate
    - isAvailable
    - closestExit
3. EntryExit
    - EntryExitId
    - X-coordinate
    - Y-coordinate
    - Level
    - Entry or Exit
    - Parking Lot Id
4. User
    - UserId
5. Bookings
    - BookingId
    - UserId
    - CarType
    - ParkingSpotId
    - EntryTime
    - ExitTime
    - Cost
    - PaymentId

## Flow
1. User registers himself
2. User requests for car parking with car type, at a certain point of time
3. System finds the closest available parking spot, and returns it to the user
4. User creates a booking, `exitTime`, `cost` and `paymentId` is empty
5. User requests for closest exit, we return it.
6. User goes to the exit, exitTime gets filled in, cost is calculate, user does a payment and paymentId is populated
