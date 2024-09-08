# **ING Interview Project**

A project which provides a basic CRUD API for store items.

Includes authentication using JWT.

## **Prerequisites**
- Java 21
- Maven 3.9
- Git

## **Installation**

  1. Clone the repository:
     ```bash
     git clone https://github.com/iordacheviorel/ing-store.git
     cd ing-store
     cd checkout master
     ```
  2. Build and run:
     ```bash
     mvn clean install
     mvn spring-boot:run
     ```

# API Endpoints

You can also import the endpoint collections in postman:
[endpoints_postman_collection.json](endpoints_postman_collection.json)


## Authentication

For endpoints requiring authentication, you need to include a Bearer token in the `Authorization` header.

It can be obtained by calling `auth/login` API endpoint.

Replace `<token>` with the actual token obtained from the login process.

Login using the super_admin account which is already created:
```json
{
    "email": "super.admin@mail.com",
    "password": "123456"
}
```

#### **URL:**
```
http://localhost:8005
```

## Endpoints

### 1. User Signup

**Endpoint:** `/auth/signup`  
**Method:** POST  
**Headers:**
- Content-Type: application/json

**Request Body:**

```json
{
  "email": "vio_6@mail.com",
  "password": "123456",
  "fullName": "Viorel Iordache 6"
}
```

**Description:** Registers a new user with the provided email, password, and full name.

---

### 2. Admin SignUp

**Endpoint:** `/admins`  
**Method:** POST  
**Headers:**
- Content-Type: application/json
- Authorization: Bearer `<token>`

**Request Body:**

```json
{
  "email": "vio_admin_6@mail.com",
  "password": "123456",
  "fullName": "Viorel Iordache Admin 6"
}
```

**Description:** Registers a new admin with the provided email, password, and full name.



**Authorization:** Requires a Bearer token.

---

### 3. User Login

**Endpoint:** `/auth/login`  
**Method:** POST  
**Headers:**
- Content-Type: application/json

**Request Body:**

```json
{
  "email": "super.admin@mail.com",
  "password": "123456"
}
```

**Description:** Authenticates a user and returns a token for further requests.

---

### 4. Get User Information

**Endpoint:** `/users/me`  
**Method:** GET  
**Headers:**
- Authorization: Bearer `<token>`

**Description:** Retrieves the information of the currently authenticated user.

**Authorization:** Requires a Bearer token.

---

### 5. Get All Users

**Endpoint:** `/users`  
**Method:** GET  
**Headers:**
- Authorization: Bearer `<token>`

**Description:** Retrieves a list of all users.

**Authorization:** Requires a Bearer token.

---

### 6. Create Item

**Endpoint:** `/items/create`  
**Method:** POST  
**Headers:**
- Content-Type: application/json
- Authorization: Bearer `<token>`

**Request Body:**

```json
{
  "name": "BLACK T-Shirt",
  "description": "This is a BLACK T-Shirt.",
  "quantity": 101,
  "price": 91.99,
  "sku": "TSBKMA101"
}
```

**Description:** Creates a new item with the specified details.

**Authorization:** Requires a Bearer token.

---

### 7. Get All Items

**Endpoint:** `/items`  
**Method:** GET  
**Headers:**
- Content-Type: application/json
- Authorization: Bearer `<token>`

**Description:** Retrieves a list of all items.

**Authorization:** Requires a Bearer token.

---

### 8. Update Item Price

**Endpoint:** `/items/{sku}`  
**Method:** PATCH  
**Headers:**
- Content-Type: application/json
- Authorization: Bearer `<token>`

**Request Body:**

```json
{
  "price": 1110.50
}
```

**Description:** Updates the price of an item identified by its SKU. 
SKU stand for Stock Keeping Unit, a unique ID used in retail stores.

**Authorization:** Requires a Bearer token.

---

### 9. Delete Item

**Endpoint:** `/items/{sku}`  
**Method:** DELETE  
**Headers:**
- Content-Type: application/json
- Authorization: Bearer `<token>`

**Description:** Deletes an item identified by its SKU.

**Authorization:** Requires a Bearer token.

---



---