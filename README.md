# ECサイトREST API

<img src="./src/main/resources/images/homepage.jpg" style="width: 85%;" alt="Landing page" title="ホームページ">

ECサイトのREST API。
Vite + Reactのフロントエンドプロジェクトで利用し、デプロイしたサイトは下リンク先で公開：<br/>
[Wild Blossum Garden](http://wild-blossom-garden.s3-website-ap-northeast-1.amazonaws.com)<br/>

<a href="https://github.com/rkyzk/vite-react" target="_blank">フロントエンドプロジェクトのリポジトリ<a/><br/>

### 目次

<a href="#overview">1. 概要<a/><br/>
<a href="#languages">2. 使用言語、フレームワーク<a/><br/>
<a href="#endpoints">3. 各機能のエンドポイント<a/><br/>
<a href="#functions">４. 各機能概要<a/><br/>
<a href="#spring-security">5. Spring SecurityのカスタマイズとJWT・リフレッシュトークンの利用について<a/><br/>
<a href="#stripe-api">6. カード決済処理におけるStripe APIの利用について<a/><br/>
<a href="#cors-setting">7. CORS設定について<a/><br/>
<a href="#er-diagram">8. ER図<a/><br/>
<a href="#tests">9. テスト<a/><br/>
<a href="#credits">10. 参考資料<a/><br/>

<h2 id="overview">1. 概要<h2/>

チューリップ、ヒヤシンスなどの球根を販売するECサイトのREST API。
ユーザ、商品、カート情報、注文内容などのデータを取得、登録、更新、削除する。
認証機能にJWTとリフレッシュトークンを使用。カード決済処理はStripe APIを利用。

<h2 id="languages">2. 使用言語、フレームワーク、DB<h2/>

Java(Spring Boot)
PostgreSQL

<h2 id="endpoints">3. 各機能のエンドポイント</h2>

#### ユーザアカウント登録、ログイン、ログアウト

| Nr  | 機能                      | API エンドポイント       | メソッド |
| --- | :----------------------- | :----------------- | :----- |
| 1   | アカウント登録              | /api/auth/signup   | post   |
| 2   | ログイン                  | /api/auth/signin   | post   |
| 3   | ログアウト                 | /api/auth/signout  | post   |
| 4   | ユーザ名取得               | /api/auth/username | get    |
| 5   | ユーザ情報取得              | /api/auth/user     | get    |

#### 住所登録、取得、更新、削除

| Nr  | 機能                      | API エンドポイント       | メソッド |
| --- | :--------------------- | :------------------------- | :----- |
| 1   | 住所を登録（ユーザと紐づける）| /api/addresses             | post   |
| 2   | 住所を登録（ユーザと紐付けない。ユーザIDをnullとする。）| /api/addresses/anonym| post   |
| 3   | ログイン中ユーザの住所を取得| /api/user/addresses        | post   |
| 4   | 指定アドレスIDの住所を取得 | /api/addresses/{addressId} | get    |
| 5   | 指定アドレスIDの住所を更新 | /api/addresses/{addressId} | put    |
| 6   | 指定アドレスIDの住所を削除 | /api/addresses/{addressId} | delete |

- ユーザが住所を保存することを選択した場合、エンドポイントNr.1で処理し、保存しないと選択した場合
  Nr.2で処理する。（注文データと紐づけるためデータをDB登録する。）
- Nr.6ではテーブル「Order」住所データが存在する場合は、住所データの物理削除を行わず、住所データのユーザIDをnullに更新する。

#### 商品カテゴリー登録、取得、更新、削除

| Nr  | 機能                      | API エンドポイント       | メソッド |
| --- | :--------------------- | :------------------------- | :----- |
| 1  | 全カテゴリーを取得        | /api/public/categories     | get    |
| 2  | カテゴリーを追加          | /api/admin/categories      | post    |
| 3  | カテゴリーを更新          | /api/categories/{categoryId} | get    |
| 4  | カテゴリーを削除          | /api/admin/categories/delete/{categoryId}| delete   |

#### カート情報登録、取得、更新、削除

| Nr  | 機能                      | API エンドポイント       | メソッド |
| --- | :----------------------- | :----------------- | :----- |
| 1   | カートに商品を登録   | /api/cart/products/{productId}/quantity/{quantity} | post  |
| 2   | カートの商品個数を更新 | /api/cart/products/{productId}/quantity/{quantity} | put  |
| 3   | カートから商品を削除 | /api/carts/{cartId}/products/{productId} | delete |
| 4   | 全カート情報を取得 | /api/carts      | get    |
| 5   | ログイン中ユーザのカートを取得  | /api/carts/user/cart       | get    |

#### 商品情報登録、取得、更新、削除

| Nr  | 機能                      | API エンドポイント       | メソッド |
| --- | :--------------------- | :------------------------- | :----- |
| 1   | 全商品情報を取得          | /api/public/products     | get    |
| 2   | お勧め商品の取得          | /api/public/products/featured            | get    |
| 3   | 指定カテゴリーの商品を取得  | /api/public/categories/{categoryId}/products | get    |
| 4   | 商品を追加              | /api/admin/category/{categoryId}/product     | post   |
| 5   | 指定IDの商品データを更新 | /api/admin/products/{prodId}                 | put    |
| 6   | 指定IDの商品の削除日を設定 | /api/admin/products/delete/{prodId}          | put    |

#### 注文データ登録、取得およびclientSecretの取得
| Nr  | 機能                                        | API エンドポイント       | メソッド |
| --- | :----------------------------------- | :-------------------------- | :----- |
| 1   | 注文データを登録（登録済み住所を使用し、新規住所は登録なし）| /order       | post   |
| 2   | 注文データを登録（新規住所登録あり）           | /order/newaddresses| post   |
| 3   | ログイン中ユーザの注文履歴データを取得         | /order-history          | get   |
| 4   | Payment intent作成しclientSecretを返却する | /order/stripe-client-secret | post   |

#### レビュー情報登録、取得

| Nr  | 機能                      | API エンドポイント       | メソッド |
| --- | :------------------------ | :-------------------------- | :----- |
| 1   | レビューを取得              | /api/public/reviews  | post   |
| 2   | 指定注文IDに紐づくレビューを登録| /api/review/{orderId}| post   |
<h2 id="functions">4. 各機能概要</h2>
TBD
<h2 id="spring-security">Spring SecurityのカスタマイズとJWT・リフレッシュトークンの利用について<h2/>
<a href="/documents/SpringSecurityのカスタマイズについて.md">5. Spring Securityのカスタマイズについて<a/><br/>
<a href="/documents/JWT・リフレッシュトークンに係る仕様について.md">JWT・リフレッシュトークンに係る仕様について<a/>
<h2 id="stripe-api">6. カード決済処理におけるStripe APIの利用について</h2>
TBD
<h2 id="cors-setting">7. CORS設定について</h2>
TBD
<h2 id="er-diagram">8. ER図</h2>

<img src="./src/main/resources/ER.png" alt="er-diagram" width="800px" />

<h2 id="tests">9. テスト<h2/>
全般的に動作確認済み。
単体テストJunit実施中、その後結合テストを実施予定。

<h2 id="credits">10. 参考資料<h2/>
Udemyのコース「Java Spring Boot professional eCommerce project master class」を参考に作成。<br/>
https://github.com/EmbarkXOfficial/spring-boot-course
