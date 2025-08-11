# Ecommerce REST API

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

The REST API is deployed on AWS
at http://ecommerce-rest-api-2025.ap-northeast-1.elasticbeanstalk.com

The front end is being developed as of May 2025.

### Contents
1. Overview
2. Technologies used
3. Features
4. Entity Relations
5. Credits

### Overview
REST API for an ecommerce application


### Technologies used
Java(Spring Boot)
PostgreSQL

### Features

#### AuthController
| Nr| Feature                 | API Endpoint        | method |
| - | :------------------ | :------------------ |:-----|
| 1 |sign up              | /api/auth/signup         |post|
| 2 |sign in              | /api/auth/signin         |post|
| 3 |sign out             | /api/auth/signout        |post|
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

#### OrderController
| Nr| Feature                        | API Endpoint        | method |
| - | :----------------------------- | :------------------ | :----- |
| 22 |place order        | /order/cart/{cartId}|post|
| 23 |place order as guest | /order/guest |post|
| 24 |place order as user | /order |post|
| 25 |add new addresses and place order as user | /order/address/add  |post|
| 26 |create payment intent and return client secret | /order/stripe-client-secret  |post|

 

### ER diagram
<img src="./src/main/resources/ER.png" alt="er-diagram" width="800px" />


### Credits

I leaned the methods to build this app with Udemy course "Java Spring Boot professional eCommerce project master class"</br>
https://github.com/EmbarkXOfficial/spring-boot-course

Product Descriptions are taken from following websites:

https://www.sarahraven.com/products/tulipa-whittallii?srsltid=AfmBOor-8ebD1y54HUaz5pb0V_RNpwp12nB5E3S6ak-L3nn57mopU9rt
