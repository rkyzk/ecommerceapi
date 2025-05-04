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
| 1 |Sign up user         | /api/signup         |post|
| 2 |Sign in user         | /api/signin         |post|
| 3 |Sign out user        | /api/signout        |post|
| 4 |get current username | /api/username       |get |
| 5 |get current user details | /api/user         |get |

#### CartController
| Nr| Feature                        | API Endpoint        | method |
| - | :----------------------------- | :------------------ | :----- |
| 6 |add product to cart        | /api/cart/products/{productId}/quantity/{quantity}|post|
| 7 |update product quantity in cart | /api/cart/products/{productId}/quantity/{quantity}|put|      | 8 |delete product from cart | /api/carts/{cartId}/products/{productId} |delete| 
| 9 |get all carts | /api/carts   |get| 
| 10|get current user's cart | /api/carts/user/cart  |get| 

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
