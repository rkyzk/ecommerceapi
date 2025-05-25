# Ecommerce REST API

### Contents
1. Overview
2. Technologies used
3. Features
4. Entity Relations

### Overview
REST API for an ecommerce application


### Technologies used
Java(Spring Boot)
postgresql

### Features

#### AuthController
| Nr| Feature                 | API Endpoint        | method |
| - | :------------------ | :------------------ |:-----|
| 1 |Sign up              | /api/auth/signup         |post|
| 2 |Sign in              | /api/auth/signin         |post|
| 3 |Sign out             | /api/auth/signout        |post|
| 4 |get current username | /api/auth/username       |get |
| 5 |get current user details | /api/auth/user         |get |

#### CartController
| Nr| Feature                        | API Endpoint        | method |
| - | :----------------------------- | :------------------ | :----- |
| 6 |add product to cart        | /api/cart/products/{productId}/quantity/{quantity}|post|
| 7 |update product quantity in cart | /api/cart/products/{productId}/quantity/{quantity}|put|      | 8 |delete product from cart | /api/carts/{cartId}/products/{productId} |delete| 
| 9 |get all carts that haven't been checked out | /api/carts   |get| 
| 10|get current user's cart | /api/carts/user/cart  |get| 

#### AddressController
| Nr| Feature                        | API Endpoint        | method |
| - | :----------------------------- | :------------------ | :----- |
| 11 |add addresses        | /api/addresses|post|
| 12 |get user's addresses | /api/user/addresses |post|      
| 13 |get address by user id | /api/addresses/{addressId} |get| 
| 14 |updated address by id | /api/addresses/{addressId}   |put| 
| 15 |delete address by id | /api/addresses/{addressId}  |delete| 

#### ProductController
| Nr| Feature                        | API Endpoint        | method |
| - | :----------------------------- | :------------------ | :----- |
| 16 |get all products        | /api/public/products|get|
| 17 |get featured products | /api/public/products/featured |get|
| 18 |get products by category | /public/categories/{categoryId}/products |get|
| 19 |add product | /admin/category/{categoryId}/product   |post|
| 20 |update product by id | /admin/products/{prodId}  |put|
| 21 |delete product by id (insert date in col 'deleted_at') | /admin/products/delete/{prodId}  |put|

- Sign up, Sign in/out for users, sellers, administrators
- authentication and role based authorization

#### for administrators
- add, update, delete products

#### for users
- browse through products
- add, update, delete items in shopping cart
- place orders
- 

### ER diagram
<img src="./src/main/resources/ER.png" alt="er-diagram" width="800px" />


### Credits

I leaned the methods to build this app with Udemy course "Java Spring Boot professional eCommerce project master class"</br>
https://github.com/EmbarkXOfficial/spring-boot-course
