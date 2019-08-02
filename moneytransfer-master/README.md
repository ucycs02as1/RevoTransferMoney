# RevoMoneyTransfer
A RESTful API for money transfers between accounts.

Developed using Java 8 and [Vert.x](http://vertx.io) based on the below requirments.
Explicit requirements:
1. You can use Java or Kotlin.
2. Keep it simple and to the point (e.g. no need to implement any authentication).
3. Assume the API is invoked by multiple systems and services on behalf of end users.
4. You can use frameworks/libraries if you like (except Spring), but don't forget about
requirement #2 and keep it simple and avoid heavy frameworks.
5. The datastore should run in-memory for the sake of this test.
6. The final result should be executable as a standalone program (should not require a
pre-installed container/server).
7. Demonstrate with tests that the API works as expected(not fully functional)


# Features
* Account creation/deletion
* Transfer creation
* Transfer execution
* Integration tests (using [REST-assured](http://rest-assured.io))
* The datastore runs in-memory for the sake of this test
* Stand-alone jar (no need for a pre-installed container/server)


# Requirements
* Java 8
* Maven

# How to build the application
Checkout the project from this repository, then run

mvn clean package

# How to run tests
Checkout the project from this repository, then run

mvn clean verify

# How to run the application

java -jar RevoMoneyTransfer-1.0-fat.jar

The application runs on port 8080

# How to use the application
# Accounts
# Create an account
The following request creates an account and returns it:
```
    POST localhost:8080/api/accounts
    {
		  "ownerName" : "Stavros Andreou"
          "accBalance" : 130000,
		  "accCurr" : "EUR"
		 
    }

Response:

    HTTP 201 Created
    {
        "id": 0,
        "accCurr": "EUR"
		"accBalance": "130000",
		"ownerName": "Stavros Andreou"

    }
```
# Update an account
The following request updates an account and returns it:
```
    PUT localhost:8080/api/accounts/0
    {
        "ownerName": "Stavros Andreou",
        "accCurr": "USD"
    }
```
Response:
```
    HTTP 200 OK
    {
        "accID": 0,
        "ownerName": "Stavros Andreou",
        "accBalance": "130000"
        "accCurr": "USD"
    }
```
* Updates only for ownerName, accCurr and accBalance.


# Delete an account
The following request deletes an account:
```
    DELETE localhost:8080/api/accounts/0
```
Response:
```
    HTTP 204 No Content
```
# Retrieve all accounts
The folowing request retrieves all accounts in the datastore
```
    GET localhost:8080/api/accounts
```
Response:
```
    HTTP 200 OK
    [ {
  "accID" : 0,
  "accCurr" : "EUR",
  "accBalance" : 130000,
  "ownerName" : "Stavros Andreou"
}, {
  "accID" : 1,
  "accCurr" : "EUR",
  "accBalance" : 30000,
  "ownerName" : "Andreas Andreou"
}, {
  "accID" : 2,
  "accCurr" : "GBP",
  "accBalance" : 10000,
  "ownerName" : "Elena Andreou"
} ]
```
# Retrieve one account
The following request retrieves one account in the datastore
```
    GET localhost:8080/api/accounts/0
```
Response:
```
    HTTP 200 OK
    {
        "accID" : 0,
		"accCurr" : "EUR",
		"accBalance" : 130000,
		"ownerName" : "Stavros Andreou"
    }
```
# Transfers
# Create a transfer
The following request creates a transfer and returns it:
```
    POST localhost:8080/api/transfers
    {
        "moveSourceAccID": "0",
        "moveDestinationAccID": "1",
        "moveAmount": "450",
        "moveCurr": "EUR",
        "moveComment": "IKEA"
    }
```
Response:
```
    HTTP 201 Created
    {
		 "id" : 0,
		 "status" : "DONE",
		 "moveCurr" : "EUR",
		 "moveAmount" : 500,
		 "moveComment" : "IKEA",
		 "moveSourceAccID" : 0,
		 "moveDestinationAccID" : 1
    }
```
# Try to execute a transfer
The following request tries to execute a transfer and returns it:
```
    PUT localhost:8080/api/transfers/0
```
Response:
```
    HTTP 200 OK
    {
        "id": 0,
        "moveSourceAccID": 0,
        "moveDestinationAccID": 1,
        "moveAmount": 450,
        "moveCurr": "EUR",
        "moveComment": "Rent",
        "status": "EXECUTED"
    }
```
