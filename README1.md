# Maker-Checker Banking API

Spring Boot REST API for maker-checker banking workflows.

## Run

```powershell
mvn spring-boot:run
```

Base URL:

```text
http://localhost:8000/api
```

The application uses MySQL configured in `src/main/resources/application.properties`.

## Common Rules

- `transactionType` must be `FUND_TRANSFER`.
- New transfers start with status `PENDING_APPROVAL`.
- The debit account must belong to `makerId` and must be `ACTIVE`.
- The beneficiary must be `ACTIVE`.
- The account must have enough `availableBalance`.
- A maker cannot approve their own transaction.
- RBAC permissions are checked during transaction creation and checker approval.
- RBAC allows a request when any active rule for an assigned role matches. Avoid assigning broad roles together with restricted roles.

## 1. Login

### POST `/login`

```http
POST http://localhost:8000/api/login
Content-Type: application/json
```

```json
{
  "userId": 101
}
```

## 2. Accounts

### POST `/accounts`

Creates an account. The response contains the generated database `id`; use that value as `debitAccountId` in transfer requests.

```http
POST http://localhost:8000/api/accounts
Content-Type: application/json
```

```json
{
  "accountNumber": "10000010222",
  "accountType": "CURRENT",
  "currency": "INR",
  "balance": 50000,
  "availableBalance": 50000,
  "status": "ACTIVE",
  "userId": 102
}
```

Allowed `accountType` values: `CURRENT`, `SAVINGS`.

Allowed `status` values: `ACTIVE`, `INACTIVE`.

### GET `/accounts/user/{userId}`

```http
GET http://localhost:8000/api/accounts/user/101
```

### GET `/accounts/{accountId}`

```http
GET http://localhost:8000/api/accounts/1
```

### GET `/accounts/{accountId}/transactions`

```http
GET http://localhost:8000/api/accounts/1/transactions
```

## 3. Beneficiaries

### POST `/beneficiaries`

```http
POST http://localhost:8000/api/beneficiaries
Content-Type: application/json
```

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

```http
GET http://localhost:8000/api/beneficiaries/user/101
```

### GET `/beneficiaries/{beneficiaryId}`

```http
GET http://localhost:8000/api/beneficiaries/1
```

### PUT `/beneficiaries/{beneficiaryId}`

```http
PUT http://localhost:8000/api/beneficiaries/1
Content-Type: application/json
```

```json
{
  "beneficiaryName": "Updated Beneficiary",
  "accountNumber": "BEN-2001",
  "bankName": "Example Bank",
  "ifscCode": "EXMP0001234",
  "createdBy": 101
}
```

### PUT `/beneficiaries/{beneficiaryId}/activate`

```http
PUT http://localhost:8000/api/beneficiaries/1/activate
```

### PUT `/beneficiaries/{beneficiaryId}/deactivate`

```http
PUT http://localhost:8000/api/beneficiaries/1/deactivate
```

## 4. Maker Transfers

### POST `/transfers`

The maker must use an account owned by the same `makerId`.

```http
POST http://localhost:8000/api/transfers
Content-Type: application/json
```

Example for `MAKER1` user `101`:

```json
{
  "makerId": 101,
  "debitAccountId": 1,
  "beneficiaryId": 1,
  "amount": 4999,
  "transactionType": "FUND_TRANSFER",
  "remarks": "Maker1 transfer"
}
```

The response contains a `transactionId`, for example `TXN100034`.

### GET `/transfers/{transactionId}`

```http
GET http://localhost:8000/api/transfers/TXN100034
```

### GET `/transfers/user/{userId}/history`

```http
GET http://localhost:8000/api/transfers/user/101/history
```

### GET `/transfers/user/{userId}/status/{status}`

```http
GET http://localhost:8000/api/transfers/user/101/status/PENDING_APPROVAL
```

Valid statuses:

```text
PENDING_APPROVAL, APPROVED, PROCESSING, SUCCESS, FAILED, REJECTED
```

### GET `/transfers/user/{userId}/type/{transactionType}`

```http
GET http://localhost:8000/api/transfers/user/101/type/FUND_TRANSFER
```

## 5. Checker Actions

Replace `{transactionId}` with a transaction whose status is `PENDING_APPROVAL`.

### GET `/checkers/{checkerId}/pending`

```http
GET http://localhost:8000/api/checkers/201/pending
```

### GET `/checkers/{checkerId}/transactions/{transactionId}`

```http
GET http://localhost:8000/api/checkers/201/transactions/TXN100034
```

### POST `/checkers/{checkerId}/approve/{transactionId}`

```http
POST http://localhost:8000/api/checkers/201/approve/TXN100034
Content-Type: application/json
```

```json
{
  "remarks": "Approved after review"
}
```

The response becomes `SUCCESS` after processing.

### POST `/checkers/{checkerId}/reject/{transactionId}`

```http
POST http://localhost:8000/api/checkers/201/reject/TXN100034
Content-Type: application/json
```

```json
{
  "remarks": "Rejected after review",
  "rejectionReason": "Beneficiary verification incomplete"
}
```

### GET `/checkers/{checkerId}/history`

```http
GET http://localhost:8000/api/checkers/201/history
```

## 6. Dashboard

### GET `/dashboard/{userId}/accounts`

```http
GET http://localhost:8000/api/dashboard/101/accounts
```

### GET `/dashboard/{userId}/recent-transactions`

```http
GET http://localhost:8000/api/dashboard/101/recent-transactions
```

### GET `/dashboard/{userId}/pending-transactions`

```http
GET http://localhost:8000/api/dashboard/101/pending-transactions
```

### GET `/dashboard/checker/{checkerId}/pending-approvals`

```http
GET http://localhost:8000/api/dashboard/checker/201/pending-approvals
```

## 7. RBAC Roles and Rules

RBAC administration endpoints are available under `/api/rbac`.

### POST `/rbac/roles`

```http
POST http://localhost:8000/api/rbac/roles
Content-Type: application/json
```

```json
{
  "roleName": "MAKER1_UNDER_500"
}
```

### POST `/rbac/user-roles`

Assigns a role to a user.

```http
POST http://localhost:8000/api/rbac/user-roles
Content-Type: application/json
```

```json
{
  "userId": 101,
  "roleName": "MAKER1"
}
```

### GET `/rbac/user-roles/{userId}`

```http
GET http://localhost:8000/api/rbac/user-roles/101
```

### GET `/rbac/rules`

```http
GET http://localhost:8000/api/rbac/rules
```

### POST `/rbac/rules`

Creates an active rule.

```http
POST http://localhost:8000/api/rbac/rules
Content-Type: application/json
```

```json
{
  "roleName": "MAKER1_UNDER_500",
  "permission": "CREATE_TRANSACTION",
  "operator": "LESS_THAN",
  "limitAmount": 500
}
```

Supported operators:

```text
ANY, LESS_THAN, LESS_THAN_OR_EQUAL, GREATER_THAN, GREATER_THAN_OR_EQUAL
```

## 8. Default Library Rules

The RBAC library seeds these rules on startup:

| Role | Permission | Operator | Limit |
|---|---|---|---:|
| MAKER1 | CREATE_TRANSACTION | LESS_THAN | 5000 |
| MAKER2 | CREATE_TRANSACTION | GREATER_THAN | 10000 |
| MAKER2 | CREATE_BENEFICIARY | ANY | none |
| CHECKER1 | APPROVE_TRANSACTION | LESS_THAN | 5000 |
| CHECKER2 | APPROVE_TRANSACTION | LESS_THAN | 20000 |
| CHECKER3 | APPROVE_TRANSACTION | ANY | none |

Example assignments:

```json
{
  "userId": 101,
  "roleName": "MAKER1"
}
```

```json
{
  "userId": 102,
  "roleName": "MAKER2"
}
```

```json
{
  "userId": 201,
  "roleName": "CHECKER1"
}
```

```json
{
  "userId": 202,
  "roleName": "CHECKER2"
}
```

```json
{
  "userId": 203,
  "roleName": "CHECKER3"
}
```

## 9. Error Responses

Validation and business errors use HTTP `400`:

```json
{
  "message": "Debit account does not belong to the maker",
  "status": 400,
  "timestamp": "2026-08-24T14:32:29.0942743"
}
```

RBAC denial uses HTTP `403`:

```json
{
  "message": "Access denied for user 201 and permission APPROVE_TRANSACTION",
  "status": 403,
  "timestamp": "2026-08-24T14:32:29.0942743"
}
```

A transaction can be approved or rejected only once while it is `PENDING_APPROVAL`.

## 10. Audit Trail

New transaction lifecycle events are stored in the `audit_trail` table when a transaction is created, approved, or rejected.

### GET `/audit`

```http
GET http://localhost:8000/api/audit
```

### GET `/audit/user/{userId}`

```http
GET http://localhost:8000/api/audit/user/101
```

Example response:

```json
[
  {
    "id": 1,
    "userId": 101,
    "action": "CREATE_TRANSACTION",
    "entityType": "FUND_TRANSFER",
    "entityId": "TXN100035",
    "status": "PENDING_APPROVAL",
    "details": "Transaction created with amount 4999",
    "createdAt": "2026-08-24T15:30:00"
  }
]
```

Audit records are not backfilled for transactions created before audit logging was enabled. Create a new transaction or perform a new approval/rejection to generate records.
