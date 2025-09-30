# InmobiJavaTest4

Bài test cho vị trị Back-end

## Cài đặt môi trường

- Database: postgres
- Java SDK 17/21
- Visual Studio Code
- Node.js & npm
- Maven

## Cách Run Project

- Mở thư mục chính (thư mục chứa file 'pom.xml') bằng VS code
- Thay đổi connect string: file 'src/main/resources/config/application-dev.yml' (datasource)

```
  datasource:
    type: com.zaxxer.hikari.HikariDataSource
    url: jdbc:postgresql://localhost:5432/inmobijavatest4
    username: postgres
    password: admin
    hikari:
      poolName: Hikari
      auto-commit: false
```

- Load modules FE

```
npm install
```

- Run FE (chạy ở port: 9000)

npm start => run port riêng 9000 và gọi về api 8080

npm run webapp:build => cùng vs backend

```
npm start
```

- Run BE (port 8080)

```
./mvnw
```

## Hướng dẫn chạy test API

Đã add file postman có thể import vào để test ('Inmobi_java_test_4.postman_collection.json')

- Register: POST: api/register
- Login : POST: api/authenticate
  sau khi login thì lấy token add vào Bearer để gọi API
- Me: GET: api/me
- Leader Board: GET: api/leader-board
- Guess Number: POST: api/guess
- Buy Turn: POST: api/buy-turn

## Ngoài ra đã có giao diện Web cho 1 số tác vụ:

- Register => http://localhost:9000/account/register
- Login => http://localhost:9000/login
- User manager => http://localhost:9000/admin/user-management
- Config Tỷ lệ thắng => http://localhost:9000/app-config => edit 'winning_percentage'
- API Docs: =>http://localhost:9000/admin/docs
