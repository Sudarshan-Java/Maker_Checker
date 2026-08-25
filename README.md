# Maker-Checker Banking Backend

Spring Boot API for corporate banking maker-checker workflows. The service is self-contained and does not call a reward or gamification service.

## Run

```bash
mvn spring-boot:run
```

Base URL: `http://localhost:8000/api`

Configure MySQL in `src/main/resources/application.properties`.

## API conventions

- JSON request and response bodies are used where a body is shown.
- Successful create operations return HTTP `201`; other successful operations return HTTP `200`.
- Validation, missing resources, and business-rule failures are returned as `ApiErrorResponse` with `timestamp`, `status`, `error`, `message`, and `path` fields.
- `transactionType` currently accepts only `FUND_TRANSFER`.
- Transfer statuses are `PENDING_APPROVAL`, `APPROVED`, `PROCESSING`, `SUCCESS`, `FAILED`, and `REJECTED`.

## Login

### POST `/login`

Checks that the user has an account and records a local login event. It does not issue a reward.

Request:

```json
{"userId": 101}
```

Response `200`:

```json
{"success": true, "message": "Login successful.", "data": {"userId": 101}}
```

## Accounts

### GET `/accounts/user/{userId}`

Returns all accounts for a user.

### GET `/accounts/{accountId}`

Returns one account.

### GET `/accounts/{accountId}/transactions`

Returns transfers ordered by creation time, newest first, for the debit account.

Account response shape:

```json
{
	"id": 10,
	"accountNumber": "ACC-101",
	"accountType": "SAVINGS",
	"currency": "INR",
	"balance": 50000.00,
	"availableBalance": 50000.00,
	"status": "ACTIVE"
}
```

## Beneficiaries

### POST `/beneficiaries`

Creates an active beneficiary. All fields are required.

Request:

```json
{
	"beneficiaryName": "Ravi Kumar",
	"accountNumber": "BEN-2001",
	"bankName": "Example Bank",
	"ifscCode": "EXMP0001234",
	"createdBy": 101
}
```

### GET `/beneficiaries/user/{userId}`

Returns beneficiaries created by the user.

### GET `/beneficiaries/{beneficiaryId}`

Returns one beneficiary.

### PUT `/beneficiaries/{beneficiaryId}`

Updates `beneficiaryName`, `accountNumber`, `bankName`, `ifscCode`, and `createdBy` using the same request shape as create.

### PUT `/beneficiaries/{beneficiaryId}/activate`

Sets the beneficiary status to `ACTIVE`.

### PUT `/beneficiaries/{beneficiaryId}/deactivate`

Sets the beneficiary status to `INACTIVE`.

Beneficiary response shape:

```json
{
	"id": 20,
	"beneficiaryName": "Ravi Kumar",
	"accountNumber": "BEN-2001",
	"bankName": "Example Bank",
	"ifscCode": "EXMP0001234",
	"createdBy": 101,
	"status": "ACTIVE",
	"createdAt": "2026-08-24T10:15:30",
	"updatedAt": null
}
```

## Transfers

### POST `/transfers`

Creates a transfer in `PENDING_APPROVAL`. The maker must own the debit account, the account and beneficiary must be active, sufficient available balance is required, and the amount cannot exceed `100000` by default.

Request:

```json
{
	"makerId": 101,
	"debitAccountId": 10,
	"beneficiaryId": 20,
	"amount": 12000.00,
	"transactionType": "FUND_TRANSFER",
	"remarks": "Invoice payment"
}
```

### GET `/transfers/{transactionId}`

Returns one transfer, for example `/transfers/TXN100001`.

### GET `/transfers/user/{userId}/history`

Returns the maker's transfers, newest first.

### GET `/transfers/user/{userId}/status/{status}`

Filters a maker's transfers by a valid transfer status.

### GET `/transfers/user/{userId}/type/{transactionType}`

Filters a maker's transfers by `FUND_TRANSFER`.

Transfer response shape:

```json
{
	"transactionId": "TXN100001",
	"makerId": 101,
	"debitAccountId": 10,
	"beneficiaryId": 20,
	"amount": 12000.00,
	"transactionType": "FUND_TRANSFER",
	"remarks": "Invoice payment",
	"status": "PENDING_APPROVAL",
	"createdAt": "2026-08-24T10:20:00",
	"updatedAt": null,
	"approvedBy": null,
	"approvedAt": null,
	"rejectionReason": null
}
```

## Checker actions

The checker ID and transaction ID are path parameters. A checker cannot approve their own transfer.

### GET `/checkers/{checkerId}/pending`

Returns all transfers currently pending approval.

### GET `/checkers/{checkerId}/transactions/{transactionId}`

Returns a pending transfer. Non-pending transfers are rejected.

### POST `/checkers/{checkerId}/approve/{transactionId}`

Approves and processes the transfer. The debit account balance and available balance are reduced, and the transfer becomes `SUCCESS`.

Request body, fields optional:

```json
{"remarks": "Approved after review", "rejectionReason": null}
```

### POST `/checkers/{checkerId}/reject/{transactionId}`

Rejects a pending transfer. `rejectionReason` is required and the account balance is unchanged.

Request:

```json
{"remarks": "Rejected by checker", "rejectionReason": "Beneficiary verification incomplete"}
```

### GET `/checkers/{checkerId}/history`

Returns the checker's approval history.

Response item:

```json
{
	"transactionId": "TXN100001",
	"action": "APPROVED",
	"remarks": "Approved after review",
	"createdAt": "2026-08-24T10:25:00"
}
```

## Dashboard

### GET `/dashboard/{userId}/accounts`

Returns the user's account totals:

```json
{"totalBalance": 50000.00, "totalAvailableBalance": 50000.00, "numberOfAccounts": 1}
```

### GET `/dashboard/{userId}/recent-transactions`

Returns up to 10 recent transfers created by the user.

### GET `/dashboard/{userId}/pending-transactions`

Returns the user's transfers with status `PENDING_APPROVAL`.

### GET `/dashboard/checker/{checkerId}/pending-approvals`

Returns all transfers with status `PENDING_APPROVAL` available for checker review.

## Test

```bash
mvn test
```
