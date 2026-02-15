# ECサイトREST API

<img src="./src/main/resources/images/homepage.jpg" style="width: 85%;" alt="Landing page" title="ホームページ">

ECサイトのREST API。
Vite + Reactのフロントエンドプロジェクトで利用し、デプロイしたサイトを下記パスで公開している。<br/>
[Wild Blossum Garden](http://wild-blossom-garden.s3-website-ap-northeast-1.amazonaws.com/products?category=4)

### 目次

1. 概要
2. 使用言語、フレームワーク
3. 主な機能
4. ER図
5. テスト
6. 参考資料

### 概要

チューリップ、ヒヤシンスなどの球根を販売するECサイトのREST API。
ユーザ、商品、カート情報、注文内容などのデータを取得、登録、更新、削除する。
ログイン機能にJWTとリフレッシュトークンを使用。カード決済処理はStripe APIを利用。

### 使用言語、フレームワーク、DB

Java(Spring Boot)
PostgreSQL

### エンドポイント

#### AuthController

| Nr  | Feature                  | API Endpoint       | method |
| --- | :----------------------- | :----------------- | :----- |
| 1   | sign up                  | /api/auth/signup   | post   |
| 2   | sign in                  | /api/auth/signin   | post   |
| 3   | sign out                 | /api/auth/signout  | post   |
| 4   | get current username     | /api/auth/username | get    |
| 5   | get current user details | /api/auth/user     | get    |

#### CartController

| Nr  | Feature                                     | API Endpoint                                       | method |
| --- | :------------------------------------------ | :------------------------------------------------- | :----- | --- | --- | ------------------------ | ---------------------------------------- | ------ |
| 6   | add product to cart                         | /api/cart/products/{productId}/quantity/{quantity} | post   |
| 7   | update product quantity in cart             | /api/cart/products/{productId}/quantity/{quantity} | put    |     | 8   | delete product from cart | /api/carts/{cartId}/products/{productId} | delete |
| 9   | get all carts that haven't been checked out | /api/carts                                         | get    |
| 10  | get current user's cart                     | /api/carts/user/cart                               | get    |

#### AddressController

| Nr  | Feature                | API Endpoint               | method |
| --- | :--------------------- | :------------------------- | :----- |
| 11  | add addresses          | /api/addresses             | post   |
| 12  | get user's addresses   | /api/user/addresses        | post   |
| 13  | get address by user id | /api/addresses/{addressId} | get    |
| 14  | updated address by id  | /api/addresses/{addressId} | put    |
| 15  | delete address by id   | /api/addresses/{addressId} | delete |

#### ProductController

| Nr  | Feature                                                | API Endpoint                             | method |
| --- | :----------------------------------------------------- | :--------------------------------------- | :----- |
| 16  | get all products                                       | /api/public/products                     | get    |
| 17  | get featured products                                  | /api/public/products/featured            | get    |
| 18  | get products by category                               | /public/categories/{categoryId}/products | get    |
| 19  | add product                                            | /admin/category/{categoryId}/product     | post   |
| 20  | update product by id                                   | /admin/products/{prodId}                 | put    |
| 21  | delete product by id (insert date in col 'deleted_at') | /admin/products/delete/{prodId}          | put    |

#### OrderController

| Nr  | Feature                                        | API Endpoint                | method |
| --- | :--------------------------------------------- | :-------------------------- | :----- |
| 22  | place order                                    | /order/cart/{cartId}        | post   |
| 23  | place order as guest                           | /order/guest                | post   |
| 24  | place order as user                            | /order                      | post   |
| 25  | add new addresses and place order as user      | /order/address/add          | post   |
| 26  | create payment intent and return client secret | /order/stripe-client-secret | post   |

### ER図

<img src="./src/main/resources/ER.png" alt="er-diagram" width="800px" />

### 概要
全般的に動作確認済み。
単体テストJunit、結合テストを月以降実施予定。

### 参考資料

Udemyのコース"Java Spring Boot professional eCommerce project master class"を参考にし
作成。<br/>
https://github.com/EmbarkXOfficial/spring-boot-course
