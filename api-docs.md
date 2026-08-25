# Corporate Banking API Specification

## Base URL

http://localhost:8000/api

---

## Account APIs

### 1. Get Account List

GET /accounts/user/{userId}

Example:

GET /api/accounts/user/101

Example response:

```json
[
  {
    "id": 1,
    "accountNumber": "10000012345",
    "accountType": "CURRENT",
    "currency": "INR",
    "balance": 100000.00,
    "availableBalance": 95000.00,
    "status": "ACTIVE"
  }
]
```

### 2. Get Account Details

GET /accounts/{accountId}

Example:

GET /api/accounts/1

Example response:

```json
{
  "id": 1,
  "accountNumber": "10000012345",
  "accountType": "CURRENT",
  "currency": "INR",
  "balance": 100000.00,
  "availableBalance": 95000.00,
  "status": "ACTIVE"
}
```

### 3. Get Recent Transactions For Account

GET /accounts/{accountId}/transactions

Example:

GET /api/accounts/1/transactions

Example response:

```json
[
  {
    "transactionId": "TXN100001",
    "makerId": 101,
    "debitAccountId": 1,
    "beneficiaryId": 10,
    "amount": 5000,
    "transactionType": "FUND_TRANSFER",
    "remarks": "Payment",
    "status": "SUCCESS",
    "createdAt": "2026-08-10T10:30:00",
    "updatedAt": "2026-08-10T11:00:00",
    "approvedBy": 201,
    "approvedAt": "2026-08-10T11:00:00",
    "rejectionReason": null
  }
]
```

---

## Beneficiary APIs

### 4. Create Beneficiary

POST /beneficiaries

Request body:

```json
{
  "beneficiaryName": "John",
  "accountNumber": "1234567890",
  "bankName": "ABC Bank",
  "ifscCode": "ABC0001234",
  "createdBy": 101
}
```

Example response:

```json
{
  "id": 10,
  "beneficiaryName": "John",
  "accountNumber": "1234567890",
  "bankName": "ABC Bank",
  "ifscCode": "ABC0001234",
  "createdBy": 101,
  "status": "ACTIVE"
}
```

### 5. Get Beneficiary List

GET /beneficiaries/user/{userId}

Example:

GET /api/beneficiaries/user/101

Example response:

```json
[
  {
    "id": 10,
    "beneficiaryName": "John",
    "accountNumber": "1234567890",
    "bankName": "ABC Bank",
    "ifscCode": "ABC0001234",
    "createdBy": 101,
    "status": "ACTIVE"
  }
]
```

### 6. Get Beneficiary Details

GET /beneficiaries/{beneficiaryId}

Example:

GET /api/beneficiaries/10

Example response:

```json
{
  "id": 10,
  "beneficiaryName": "John",
  "accountNumber": "1234567890",
  "bankName": "ABC Bank",
  "ifscCode": "ABC0001234",
  "createdBy": 101,
  "status": "ACTIVE"
}
```

### 7. Edit Beneficiary

PUT /beneficiaries/{beneficiaryId}

Request body:

```json
{
  "beneficiaryName": "John Updated",
  "accountNumber": "1234567890",
  "bankName": "XYZ Bank",
  "ifscCode": "XYZ0001234"
}
```

Example response:

```json
{
  "id": 10,
  "beneficiaryName": "John Updated",
  "accountNumber": "1234567890",
  "bankName": "XYZ Bank",
  "ifscCode": "XYZ0001234",
  "createdBy": 101,
  "status": "ACTIVE"
}
```

### 8. Activate Beneficiary

PUT /beneficiaries/{beneficiaryId}/activate

Example:

PUT /api/beneficiaries/10/activate

Example response:

```json
{
  "id": 10,
  "beneficiaryName": "John",
  "accountNumber": "1234567890",
  "bankName": "ABC Bank",
  "ifscCode": "ABC0001234",
  "createdBy": 101,
  "status": "ACTIVE"
}
```

### 9. Deactivate Beneficiary

PUT /beneficiaries/{beneficiaryId}/deactivate

Example:

PUT /api/beneficiaries/10/deactivate

Example response:

```json
{
  "id": 10,
  "beneficiaryName": "John",
  "accountNumber": "1234567890",
  "bankName": "ABC Bank",
  "ifscCode": "ABC0001234",
  "createdBy": 101,
  "status": "INACTIVE"
}
```

---

## Fund Transfer APIs

### 10. Create Fund Transfer

POST /transfers

Request body:

```json
{
  "makerId": 101,
  "debitAccountId": 1,
  "beneficiaryId": 10,
  "amount": 5000,
  "transactionType": "FUND_TRANSFER",
  "remarks": "Payment"
}
```

Example response:

```json
{
  "transactionId": "TXN100001",
  "makerId": 101,
  "debitAccountId": 1,
  "beneficiaryId": 10,
  "amount": 5000,
  "transactionType": "FUND_TRANSFER",
  "remarks": "Payment",
  "status": "PENDING_APPROVAL",
  "createdAt": "2026-08-10T10:30:00",
  "updatedAt": null,
  "approvedBy": null,
  "approvedAt": null,
  "rejectionReason": null
}
```

### 11. Get Transfer By ID

GET /transfers/{transactionId}

Example:

GET /api/transfers/TXN100001

Example response:

```json
{
  "transactionId": "TXN100001",
  "makerId": 101,
  "debitAccountId": 1,
  "beneficiaryId": 10,
  "amount": 5000,
  "transactionType": "FUND_TRANSFER",
  "status": "PENDING_APPROVAL",
  "createdAt": "2026-08-10T10:30:00",
  "approvedBy": null,
  "rejectionReason": null
}
```

### 12. Maker Transaction History

GET /transfers/user/{userId}/history

Example:

GET /api/transfers/user/101/history

Example response:

```json
[
  {
    "transactionId": "TXN100001",
    "createdAt": "2026-08-10T10:30:00",
    "debitAccount": "10000012345",
    "beneficiary": "John",
    "amount": 5000,
    "transactionType": "FUND_TRANSFER",
    "status": "SUCCESS",
    "createdBy": 101,
    "approvedBy": 201
  }
]
```

### 13. Filter Transfers By Status

GET /transfers/user/{userId}/status/{status}

Example:

GET /api/transfers/user/101/status/SUCCESS

Example response:

```json
[
  {
    "transactionId": "TXN100001",
    "makerId": 101,
    "debitAccountId": 1,
    "beneficiaryId": 10,
    "amount": 5000,
    "transactionType": "FUND_TRANSFER",
    "status": "SUCCESS",
    "createdAt": "2026-08-10T10:30:00"
  }
]
```

### 14. Filter Transfers By Type

GET /transfers/user/{userId}/type/{transactionType}

Example:

GET /api/transfers/user/101/type/FUND_TRANSFER

Example response:

```json
[
  {
    "transactionId": "TXN100001",
    "makerId": 101,
    "debitAccountId": 1,
    "beneficiaryId": 10,
    "amount": 5000,
    "transactionType": "FUND_TRANSFER",
    "status": "SUCCESS",
    "createdAt": "2026-08-10T10:30:00"
  }
]
```

---

## Checker APIs

### 15. Pending Transactions

GET /checkers/{checkerId}/pending

Example:

GET /api/checkers/201/pending

Example response:

```json
[
  {
    "transactionId": "TXN100001",
    "makerId": 101,
    "debitAccountId": 1,
    "beneficiaryId": 10,
    "amount": 5000,
    "transactionType": "FUND_TRANSFER",
    "status": "PENDING_APPROVAL",
    "createdAt": "2026-08-10T10:30:00"
  }
]
```

### 16. View Transaction Details

GET /checkers/{checkerId}/transactions/{transactionId}

Example:

GET /api/checkers/201/transactions/TXN100001

Example response:

```json
{
  "transactionId": "TXN100001",
  "makerId": 101,
  "debitAccountId": 1,
  "beneficiaryId": 10,
  "amount": 5000,
  "transactionType": "FUND_TRANSFER",
  "status": "PENDING_APPROVAL",
  "createdAt": "2026-08-10T10:30:00",
  "approvedBy": null,
  "rejectionReason": null
}
```

### 17. Approve Transaction

POST /checkers/{checkerId}/approve/{transactionId}

Request body:

```json
{
  "remarks": "Transaction approved"
}
```

Example:

POST /api/checkers/201/approve/TXN100001

Example response:

```json
{
  "transactionId": "TXN100001",
  "makerId": 101,
  "debitAccountId": 1,
  "beneficiaryId": 10,
  "amount": 5000,
  "transactionType": "FUND_TRANSFER",
  "status": "SUCCESS",
  "createdAt": "2026-08-10T10:30:00",
  "approvedBy": 201,
  "approvedAt": "2026-08-10T11:00:00"
}
```

### 18. Reject Transaction

POST /checkers/{checkerId}/reject/{transactionId}

Request body:

```json
{
  "rejectionReason": "Insufficient supporting information"
}
```

Example response:

```json
{
  "transactionId": "TXN100001",
  "status": "REJECTED",
  "rejectionReason": "Insufficient supporting information"
}
```

### 19. Checker Approval History

GET /checkers/{checkerId}/history

Example:

GET /api/checkers/201/history

Example response:

```json
[
  {
    "transactionId": "TXN100001",
    "action": "APPROVED",
    "remarks": "Looks good",
    "createdAt": "2026-08-10T11:00:00"
  },
  {
    "transactionId": "TXN100005",
    "action": "REJECTED",
    "remarks": "Invalid supporting information",
    "createdAt": "2026-08-10T11:20:00"
  }
]
```

---

## Dashboard APIs

### 20. Account Summary

GET /dashboard/{userId}/accounts

Example:

GET /api/dashboard/101/accounts

Example response:

```json
{
  "totalBalance": 100000.00,
  "totalAvailableBalance": 95000.00,
  "numberOfAccounts": 1
}
```

### 21. Recent Transactions

GET /dashboard/{userId}/recent-transactions

Example:

GET /api/dashboard/101/recent-transactions

Example response:

```json
[
  {
    "transactionId": "TXN100001",
    "makerId": 101,
    "debitAccountId": 1,
    "beneficiaryId": 10,
    "amount": 5000,
    "transactionType": "FUND_TRANSFER",
    "status": "SUCCESS",
    "createdAt": "2026-08-10T10:30:00"
  }
]
```

### 22. Pending Transactions

GET /dashboard/{userId}/pending-transactions

Example:

GET /api/dashboard/101/pending-transactions

Example response:

```json
[
  {
    "transactionId": "TXN100001",
    "makerId": 101,
    "debitAccountId": 1,
    "beneficiaryId": 10,
    "amount": 5000,
    "transactionType": "FUND_TRANSFER",
    "status": "PENDING_APPROVAL",
    "createdAt": "2026-08-10T10:30:00"
  }
]
```

### 23. Checker Pending Approvals

GET /dashboard/checker/{checkerId}/pending-approvals

Example:

GET /api/dashboard/checker/201/pending-approvals

Example response:

```json
[
  {
    "transactionId": "TXN100001",
    "makerId": 101,
    "debitAccountId": 1,
    "beneficiaryId": 10,
    "amount": 5000,
    "transactionType": "FUND_TRANSFER",
    "status": "PENDING_APPROVAL",
    "createdAt": "2026-08-10T10:30:00"
  }
]
```

---

## Gamification Integration APIs

This project now integrates with a separate generic Gamification Engine through Spring Cloud OpenFeign. The Maker-Checker service remains the owner of actual bank balances, while the Gamification Engine remains the source of truth for rewards and coins.

### Configuration

Application properties added:

```properties
server.port=8000

gamification.service.url=${GAMIFICATION_SERVICE_URL:http://localhost:8085}
gamification.application-id=${GAMIFICATION_APPLICATION_ID:CHECKER_MAKER}
```

The application ID used for all events is:

```text
CHECKER_MAKER
```

### Architecture flow

```text
Maker-Checker -> OpenFeign -> Gamification Engine
                     |
                     +--> Reward rules / coins / wallet
```

---

### 24. User Login

POST /login

Request body:

```json
{
  "userId": 101
}
```

Example:

POST /api/login

Example response (first login of the day):

```json
{
  "success": true,
  "message": "Login successful. Daily reward received.",
  "data": {
    "userId": 101,
    "rewarded": true,
    "coinsAwarded": 10,
    "coinBalance": 1510,
    "redeemableValue": 15.1
  }
}
```

Example response (same day subsequent login):

```json
{
  "success": true,
  "message": "Login successful. Daily reward already claimed.",
  "data": {
    "userId": 101,
    "rewarded": false,
    "coinsAwarded": 0,
    "coinBalance": 1510,
    "redeemableValue": 15.1
  }
}
```

This event is sent to the Gamification Engine with:

```json
{
  "applicationId": "CHECKER_MAKER",
  "eventType": "USER_LOGIN",
  "userId": "101",
  "eventId": "LOGIN-101-20260817-9d3d5d44-7d1b-4cf6-bc82-a7cb1eaf8b8f",
  "referenceId": "LOGIN-101-20260817-9d3d5d44-7d1b-4cf6-bc82-a7cb1eaf8b8f",
  "attributes": {
    "loginAt": "2026-08-17T10:23:15"
  }
}
```

---

### 25. Get Reward Wallet

GET /rewards/wallet/{userId}

Example:

GET /api/rewards/wallet/101

Example response:

```json
{
  "success": true,
  "data": {
    "userId": 101,
    "applicationId": "CHECKER_MAKER",
    "coinBalance": 1500,
    "conversion": {
      "coins": 100,
      "currency": "INR",
      "currencyValue": 1
    },
    "redeemableValue": 15.00,
    "availableCashback": 10.00,
    "claimedCashback": 20.00
  }
}
```

This API combines:

- coin balance and conversion from the Gamification Engine
- cashback summaries from Maker-Checker local reward records

---

### 26. Reward History

GET /rewards/{userId}/history

Example:

GET /api/rewards/101/history

Example response:

```json
[
  {
    "id": 1,
    "userId": 101,
    "applicationId": "CHECKER_MAKER",
    "eventId": "TXN100001",
    "transactionId": "TXN100001",
    "rewardType": "COINS",
    "coinsAwarded": 1000,
    "cashbackAmount": 10.00,
    "currency": "INR",
    "status": "AVAILABLE",
    "claimedAt": null,
    "createdAt": "2026-08-17T11:05:00"
  }
]
```

---

### 27. Claim Cashback

POST /rewards/{userId}/claim?eventId={eventId}

Example:

POST /api/rewards/101/claim?eventId=TXN100001

Example success response:

```json
{
  "success": true,
  "message": "Reward claimed successfully",
  "data": {
    "userId": 101,
    "eventId": "TXN100001",
    "status": "CLAIMED",
    "cashbackAmount": 10.00
  }
}
```

Claim behavior:

1. Validate that the reward record exists and is AVAILABLE
2. Find the user's active account
3. Credit account.balance and account.availableBalance
4. Mark the local reward as CLAIMED
5. Update the wallet state
6. Return success

If the same claim is attempted again, the second call is rejected because the reward is already CLAIMED.

---

### 28. Dashboard Rewards Summary

GET /dashboard/{userId}/rewards

Example:

GET /api/dashboard/101/rewards

Example response:

```json
{
  "userId": 101,
  "coinBalance": 1500,
  "redeemableValue": 15.00,
  "availableCashback": 10.00,
  "claimedCashback": 20.00,
  "currency": "INR"
}
```

This is the frontend-friendly reward overview card.

---

## Transaction Gamification Event

Once the transfer is approved and the account debit is completed successfully, Maker-Checker triggers a generic event to the Gamification Engine.

### 29. Successful Transfer Event Payload

Event target:

```text
POST /api/gamification/events
```

Request payload:

```json
{
  "applicationId": "CHECKER_MAKER",
  "eventType": "TRANSACTION_SUCCESS",
  "userId": "101",
  "eventId": "TXN100001",
  "referenceId": "TXN100001",
  "attributes": {
    "amount": 12000,
    "transactionType": "FUND_TRANSFER",
    "transactionId": "TXN100001"
  }
}
```

Example successful response from the Gamification Engine:

```json
{
  "success": true,
  "message": "Event processed successfully",
  "data": {
    "applicationId": "CHECKER_MAKER",
    "userId": "101",
    "eventId": "TXN100001",
    "rewarded": true,
    "rewardType": "COINS",
    "coinsAwarded": 1000,
    "coinBalance": 1500,
    "redeemableValue": 10.00,
    "currency": "INR"
  }
}
```

This happens only after transaction status becomes SUCCESS. It is not fired for:

- PENDING_APPROVAL
- PROCESSING
- REJECTED
- FAILED

---

## Complete End-to-End Process

### Transaction flow with gamification

1. Maker creates a transfer
2. Checker approves the transfer
3. Transaction moves to SUCCESS
4. Maker-Checker debits account balance
5. Maker-Checker sends TRANSACTION_SUCCESS to Gamification Engine
6. Gamification Engine evaluates rules and may return coinsAwarded + redeemableValue
7. Maker-Checker stores a local reward record
8. Frontend displays reward wallet and available cashback
9. User clicks Claim cashback
10. Maker-Checker credits the actual account balance
11. Reward status becomes CLAIMED

### Example data

#### Step 1: Create transfer request

```json
{
  "makerId": 101,
  "debitAccountId": 1,
  "beneficiaryId": 10,
  "amount": 12000,
  "transactionType": "FUND_TRANSFER",
  "remarks": "Office vendor payment"
}
```

#### Step 2: Approve transfer

```json
{
  "remarks": "Approved by checker"
}
```

#### Step 3: Post-approval successful transaction response

```json
{
  "transactionId": "TXN100001",
  "makerId": 101,
  "debitAccountId": 1,
  "beneficiaryId": 10,
  "amount": 12000,
  "transactionType": "FUND_TRANSFER",
  "status": "SUCCESS",
  "createdAt": "2026-08-17T11:00:00",
  "updatedAt": "2026-08-17T11:02:00",
  "approvedBy": 201,
  "approvedAt": "2026-08-17T11:02:00"
}
```

#### Step 4: Reward created in Maker-Checker

```json
{
  "id": 1,
  "userId": 101,
  "applicationId": "CHECKER_MAKER",
  "eventId": "TXN100001",
  "transactionId": "TXN100001",
  "rewardType": "COINS",
  "coinsAwarded": 1000,
  "cashbackAmount": 10.00,
  "currency": "INR",
  "status": "AVAILABLE",
  "createdAt": "2026-08-17T11:03:00"
}
```

#### Step 5: Wallet after reward

```json
{
  "success": true,
  "data": {
    "userId": 101,
    "applicationId": "CHECKER_MAKER",
    "coinBalance": 1500,
    "conversion": {
      "coins": 100,
      "currency": "INR",
      "currencyValue": 1
    },
    "redeemableValue": 15.00,
    "availableCashback": 10.00,
    "claimedCashback": 20.00
  }
}
```

#### Step 6: Reward claim and bank credit

Before claim:

```json
{
  "balance": 50000.00,
  "availableBalance": 50000.00
}
```

Claim request:

```http
POST /api/rewards/101/claim?eventId=TXN100001
```

After claim:

```json
{
  "balance": 50010.00,
  "availableBalance": 50010.00
}
```

Reward record status becomes:

```json
"status": "CLAIMED"
```

---

## Login + Daily Reward Example

### Daily first login

```json
{
  "applicationId": "CHECKER_MAKER",
  "eventType": "USER_LOGIN",
  "userId": "101",
  "eventId": "LOGIN-101-20260817-UUID",
  "referenceId": "LOGIN-101-20260817-UUID",
  "attributes": {
    "loginAt": "2026-08-17T10:23:15"
  }
}
```

Gamification Engine response:

```json
{
  "success": true,
  "message": "Event processed successfully",
  "data": {
    "applicationId": "CHECKER_MAKER",
    "userId": "101",
    "eventId": "LOGIN-101-20260817-UUID",
    "rewarded": true,
    "coinsAwarded": 10,
    "coinBalance": 1510,
    "redeemableValue": 15.10,
    "currency": "INR"
  }
}
```

### Same-day second login

Gamification Engine returns:

```json
{
  "success": true,
  "message": "Event processed successfully",
  "data": {
    "applicationId": "CHECKER_MAKER",
    "userId": "101",
    "eventId": "LOGIN-101-20260817-ANOTHER-UUID",
    "rewarded": false,
    "coinsAwarded": 0,
    "coinBalance": 1510,
    "redeemableValue": 15.10,
    "currency": "INR"
  }
}
```

---

## Error Response Format

Example:

```json
{
  "message": "Insufficient available balance",
  "status": 400,
  "timestamp": "2026-08-10T10:30:00"
}
```

---

## Status Values

Transaction statuses:

```text
PENDING_APPROVAL
APPROVED
PROCESSING
SUCCESS
FAILED
REJECTED
```

Beneficiary statuses:

```text
ACTIVE
INACTIVE
```

Account statuses:

```text
ACTIVE
INACTIVE
```
