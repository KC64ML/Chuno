
  

# 📚 1. mysql

mysql 같은경우 docker image를 이용할 것이다.

참고 자료 : [젠킨스와 도커를 이용한 CICD 배포방법](https://dev-taerin.tistory.com/35)



```

# mysql 이미지 불러오기

sudo docker pull mysql

# 도커 이미지 확인

sudo docker images

# 도커 이름은 --name 뒤에 넣고, password는 root 패스워드(사용자 지정)

sudo docker run -d -p 3305:3306 -e MYSQL_ROOT_PASSWORD=ssafy --restart=unless-stopped -v /home/ubuntu/db:/var/lib/mysql --name D208_mysql mysql

# 도커 컨테이너 bash 접속

sudo docker exec -it d208_mysql bash

# mysql 접속

mysql -u root -p

# 패스워드 입력

ssafy

```


&nbsp;

`username` : root  
`password` : ssafy
  

![Untitled](https://user-images.githubusercontent.com/72541544/219372036-37eab172-df58-4ab5-a560-8be1404c2b21.png)


&nbsp;

`springboot - application.yml`

  
```yml
server:
  port: 8080
  servlet:
    context-path: /
    encoding:
      charset: UTF-8
      enabled: true
      force: true
#  address: localhost
spring:
  datasource:
    driver-class-name: com.mysql.cj.jdbc.Driver
    url: jdbc:mysql://i8d208.p.ssafy.io:3305/chuno?useSSL=false&autoReconnect=true&useUnicode=true&characterEncoding=utf-8&serverTimezone=UTC
    hikari:
      username: root
      password: ssafy
  jpa:
    database: mysql
    hibernate:
      ddl-auto: create
#    database-platform: org.hibernate.dialect.MySQL5InnoDBDialect
    properties:
      hibernate:
        show_sql: true
        format_sql: true
  servlet:
    multipart:
      location: C:\res_file\chuno
logging:
  level:
    root: info
    org.hibernate.type: trace
```


&nbsp;


**✔️ sql default**

```sql
insert into users
(reg_dt, catch_count, chaser_play_count, chaser_win_count, email, exp, is_deleted, is_manager, level, money, nickname, paper_count, path, save_name, runner_play_count, runner_win_count, phone)
values
(now(), 0, 11, 7, "ljc9393@nate.com", 0, 0, 0, 1, 0, "큰개님", 0, "", "", 30, 15, "01051411025"),
(now(), 0, 11, 7, "opi6@hanmail.net", 0, 0, 0, 1, 0, "인의동큰손", 0, "", "", 30, 15, "01026896940"),
(now(), 0, 11, 7, "lce511@naver.com", 0, 0, 0, 1, 85900, "채은짱님", 0, "", "", 30, 15, "01084054759"),
(now(), 0, 11, 7, "souk0712@naver.com", 0, 0, 0, 1, 80000, "모카", 0, "", "", 30, 15, "01082860799"),
(now(), 0, 11, 7, "asdf@naver.com", 0, 0, 0, 1, 0, "아무", 0, "", "", 13, 15, "01515498451"),
(now(), 0, 11, 7, "qwer@naver.com", 0, 0, 0, 1, 524, "이름", 0, "", "", 51, 15, "01059519858"),
(now(), 0, 11, 7, "zxcv@naver.com", 0, 0, 0, 1, 5000, "짓기", 0, "", "", 30, 15, "01098751351"),
(now(), 0, 11, 7, "grg@naver.com", 0, 0, 0, 1, 800, "귀찮아죽겠음", 0, "", "", 30, 15, "01849878512"),
(now(), 0, 11, 7, "tnth@naver.com", 0, 0, 0, 1, 7000, "닉넴뭐하지", 0, "", "", 30, 15, "01012168849"),
(now(), 0, 11, 7, "tnth2@naver.com", 0, 0, 0, 1, 7000, "닉넴뭐하지2", 0, "", "", 30, 15, "01012168849"),
(now(), 0, 11, 7, "tnth3@naver.com", 0, 0, 0, 1, 7000, "닉넴뭐하지3", 0, "", "", 30, 15, "01012168849"),
(now(), 0, 11, 7, "tnth4@naver.com", 0, 0, 0, 1, 7000, "닉넴뭐하지4", 0, "", "", 30, 15, "01012168849"),
(now(), 0, 11, 7, "tnth5@naver.com", 0, 0, 0, 1, 7000, "닉넴뭐하지5", 0, "", "", 30, 15, "01012168849"),
(now(), 0, 11, 7, "tnth6@naver.com", 0, 0, 0, 1, 7000, "닉넴뭐하지6", 0, "", "", 30, 15, "01012168849"),
(now(), 0, 11, 7, "tnth7@naver.com", 0, 0, 0, 1, 7000, "닉넴뭐하지7", 0, "", "", 30, 15, "01012168849"),
(now(), 0, 11, 7, "tnth8@naver.com", 0, 0, 0, 1, 7000, "닉넴뭐하지8", 0, "", "", 30, 15, "01012168849");

INSERT INTO items
(description, img_path, name, price, for_runner)
VALUES
    ("자신의 위치를 드러내지 않고 가장 가까운 추노꾼의 위치를 확인할 수 있다.", "item/item1.png", "천리안", 1500, 1),
    ("추노꾼이 자신을 잡을 수 있는 범위를 축소한다.", "item/item2.png", "위장", 2000, 1),
    ("진짜 노비문서의 위치를 확인할 수 있다.", "item/item3.png", "확실한\n정보통", 1000, 1),
    ("먹물을 뿌려 내 화면을 가릴 수 있다.", "item/item4.png", "먹물탄", 1300, 1),
    ("30초간 노비의 위치를 지도에 표시할 수 있다.", "item/item5.png", "조명탄", 1500, 0),
    ("자신이 노비를 잡을 수 있는 범위를 확대할 수 있다.", "item/item6.png", "긴\n오랏줄", 2000, 0),
    ("노비 문서의 위치를 셔플할 수 있다.", "item/item7.png", "거짓\n정보통", 1000, 0),
    ("연기를 피워 내 화면을 가릴 수 있다.", "item/item8.png", "연막탄", 1300, 0);


/*- 천리안 : 자신의 위치를 드러내지 않고 가장 가까운 추노꾼의 위치를 확인할 수 있다.
- 위장 : 추노꾼이 자신을 잡을 수 있는 범위를 축소한다.
- 확실한 정보통 : 진짜 노비문서의 위치를 확인할 수 있다.
- 먹물탄 : 먹물을 뿌려 내 화면을 가릴 수 있다.
2. 추노꾼용
    - 조명탄 : n초간 노비의 위치를 지도에 표시할 수 있다.
        - 전체 노비 위치 잠깐 표시
    - 긴 오랏줄 : 자신이 노비를 잡을 수 있는 범위를 확대할 수 있다.
    - 거짓 정보통 : 노비 문서의 위치를 셔플할 수 있다.
    - ~~허수아비 : 노비 화면에 가짜 추노꾼 위치 띄우기~~
    - 연막탄: 연기를 피워 내 화면을 가릴 수 있다.*/

```


&nbsp;

&nbsp;
