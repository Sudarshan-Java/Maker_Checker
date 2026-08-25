# RBAC Roles and Rules API

## Base URL

```text
http://localhost:8000/api
```

Start the application before calling these APIs:

```powershell
mvn spring-boot:run
```

## 1. Create a Role

### API

```http
POST http://localhost:8000/api/rbac/roles
Content-Type: application/json
```

### JSON format

```json
{
  "roleName": "ROLE_NAME"
}
```

### Create Maker1

```json
{
  "roleName": "MAKER1"
}
```

### Create Maker2

```json
{
  "roleName": "MAKER2"
}
```

### Create Maker3

```json
{
  "roleName": "MAKER3"
}
```

### Create Checker1

```json
{
  "roleName": "CHECKER1"
}
```

### Create Checker2

```json
{
  "roleName": "CHECKER2"
}
```

### Create Checker3

```json
{
  "roleName": "CHECKER3"
}
```

The role API returns `201 Created`. Roles already seeded by the library are returned without creating a duplicate.

## 2. Create a Rule

### API

```http
POST http://localhost:8000/api/rbac/rules
Content-Type: application/json
```

### JSON format

```json
{
  "roleName": "ROLE_NAME",
  "permission": "PERMISSION_NAME",
  "operator": "OPERATOR",
  "limitAmount": 0
}
```

For an `ANY` rule, use `null` for `limitAmount`.

Supported operators:

```text
ANY
LESS_THAN
LESS_THAN_OR_EQUAL
GREATER_THAN
GREATER_THAN_OR_EQUAL
```

## 3. Maker Rules

### Maker1: create transactions below 5000

```json
{
  "roleName": "MAKER1",
  "permission": "CREATE_TRANSACTION",
  "operator": "LESS_THAN",
  "limitAmount": 5000
}
```

### Maker2: create transactions above 10000

```json
{
  "roleName": "MAKER2",
  "permission": "CREATE_TRANSACTION",
  "operator": "GREATER_THAN",
  "limitAmount": 10000
}
```

### Maker2: create beneficiaries

```json
{
  "roleName": "MAKER2",
  "permission": "CREATE_BENEFICIARY",
  "operator": "ANY",
  "limitAmount": null
}
```

### Maker3: create beneficiaries only

```json
{
  "roleName": "MAKER3",
  "permission": "CREATE_BENEFICIARY",
  "operator": "ANY",
  "limitAmount": null
}
```

Maker3 must not have a `CREATE_TRANSACTION` rule. Therefore Maker3 can create beneficiaries but cannot create transactions.

## 4. Checker Rules

### Checker1: approve transactions below 5000

```json
{
  "roleName": "CHECKER1",
  "permission": "APPROVE_TRANSACTION",
  "operator": "LESS_THAN",
  "limitAmount": 5000
}
```

### Checker2: approve transactions below 20000

```json
{
  "roleName": "CHECKER2",
  "permission": "APPROVE_TRANSACTION",
  "operator": "LESS_THAN",
  "limitAmount": 20000
}
```

### Checker3: approve transactions of any amount

```json
{
  "roleName": "CHECKER3",
  "permission": "APPROVE_TRANSACTION",
  "operator": "ANY",
  "limitAmount": null
}
```

## 5. Assign a Role to a User

### API

```http
POST http://localhost:8000/api/rbac/user-roles
Content-Type: application/json
```

### JSON format

```json
{
  "userId": 0,
  "roleName": "ROLE_NAME"
}
```

### Assign roles to users

Maker1, user `101`:

```json
{
  "userId": 101,
  "roleName": "MAKER1"
}
```

Maker2, user `102`:

```json
{
  "userId": 102,
  "roleName": "MAKER2"
}
```

Maker3, user `103`:

```json
{
  "userId": 103,
  "roleName": "MAKER3"
}
```

Checker1, user `201`:

```json
{
  "userId": 201,
  "roleName": "CHECKER1"
}
```

Checker2, user `202`:

```json
{
  "userId": 202,
  "roleName": "CHECKER2"
}
```

Checker3, user `203`:

```json
{
  "userId": 203,
  "roleName": "CHECKER3"
}
```

## 6. Verify Roles and Rules

Get all rules:

```http
GET http://localhost:8000/api/rbac/rules
```

Get roles assigned to Maker3:

```http
GET http://localhost:8000/api/rbac/user-roles/103
```

Expected Maker3 assignment:

```json
[
  {
    "userId": 103,
    "roleName": "MAKER3"
  }
]
```

## 7. Expected Permissions

| User | Role | Allowed operation |
|---:|---|---|
| 101 | MAKER1 | Create transactions below 5000 |
| 102 | MAKER2 | Create transactions above 10000 and create beneficiaries |
| 103 | MAKER3 | Create beneficiaries only |
| 201 | CHECKER1 | Approve transactions below 5000 |
| 202 | CHECKER2 | Approve transactions below 20000 |
| 203 | CHECKER3 | Approve transactions of any amount |

RBAC rules are additive. If a user has multiple roles, access is granted when any active rule matches. Do not assign `CHECKER3` together with `CHECKER1` or `CHECKER2` when you want to enforce an approval limit.
