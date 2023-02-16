


# 📚 1. 처음으로 nginx 설치 후 setting 할 때

**✔️ nginx를 다운 받는다.**

```shell
# 설치
sudo apt-get install nginx

# 설치 확인 및 버전 확인
nginx -v
```


&nbsp;


**✔️ letsencrypt 설치를 위해 다음과 같은 순서로 명령어를 입력**

```shell
sudo apt-get install letsencrypt

sudo systemctl stop nginx

sudo letsencrypt certonly --standalone -d www제외한 도메인 이름
```


이렇게 한 후, "Congratulations!"로 시작하는 문구가 보이면, 인증서 발급이 완료된 것이다.


이후

`/etc/nginx/sites-available`로 이동한 후, 적절한 이름의 파일을 생성하여 다음과 같이 작성한다.

`sudo vi proxy-setting.conf`

```shell
server {
        listen 80 default_server;
        listen [::]:80 default_server;

        server_name i8d208.p.ssafy.io;

        location /{
                proxy_pass http://localhost:3000;
                proxy_set_header Upgrade $http_upgrade;
                proxy_set_header Connection "upgrade";
        }

        location /api {
                proxy_pass http://localhost:8000/api;
        }

}
server {
        listen 443 ssl;
        listen [::]:443 ssl;

        server_name i8d208.p.ssafy.io;

        location /{
                proxy_pass http://localhost:3000;
                proxy_set_header Upgrade $http_upgrade;
                proxy_set_header Connection "upgrade";
        }

        location /api {
                proxy_pass http://localhost:8000/api;
        }

        ssl_certificate /etc/letsencrypt/live/i8d208.p.ssafy.io/fullchain.pem; # managed by Certbot
        ssl_certificate_key /etc/letsencrypt/live/i8d208.p.ssafy.io/privkey.pem; # managed by Certbot
        # include /etc/letsencrypt/options-ssl-nginx.conf; # managed by Certbot
        # ssl_dhparam /etc/letsencrypt/ssl-dhparams.pem; # managed by Certbot
}
```



&nbsp;


> **✏️ 여기서 문제점**
> 
> 기존 nginx가 80번 port로 실행된다.
> 
> <img width="1198" alt="스크린샷 2023-02-04 오전 9 50 40" src="https://user-images.githubusercontent.com/72541544/216736266-d317d61e-0bb2-4960-a160-c5c5eef9f465.png">
> **나는 3.36.87.75를 입력했을 때, 우리 main화면이 나왔으면 좋겠다!**
> 
> `/etc/nginx/sites-enabled` 로 이동한다.
> 
> <img width="1138" alt="스크린샷 2023-02-04 오전 9 55 52" src="https://user-images.githubusercontent.com/72541544/216736433-933e3388-3163-4de8-9028-dc9a226b85dc.png">
> `default`, `proxy-setting`이 있는데 (`proxy-setting`은 이후에 실행하면 만들어지는 `.conf` 파일이다.)
> 
> `default 편집기`를 열어서 `port`를 변경해주고 재실행하면 된다. (재실행은 2번 참고) - default 파일을 편집하지 말고 삭제해도 되는 것 같다. (80 -> 180)
> 
> <img width="397" alt="스크린샷 2023-02-04 오전 9 58 41" src="https://user-images.githubusercontent.com/72541544/216736751-999380d0-18b8-4e85-b0e4-762cb17055d7.png">



&nbsp;

**✔️ ln -s 명령어 실행한다.**

<img width="1134" alt="스크린샷 2023-02-04 오전 10 05 03" src="https://user-images.githubusercontent.com/72541544/216736945-fe330631-b9d6-4c3c-bbe4-98625ae9b946.png">


```shell
sudo ln -s /etc/nginx/sites-available/proxy-setting /etc/nginx/sites-enabled/proxy-setting
```



&nbsp;



**✔️ 성공 여부 확인**


<img width="579" alt="스크린샷 2023-02-04 오전 10 06 09" src="https://user-images.githubusercontent.com/72541544/216737010-f7e2473e-197d-40a6-96d2-3986c6be53f5.png">


```shell
sudo nginx -t
```

`nginx test`가 성공했다는 것을 알 수 있다.


&nbsp;


**✔️ nginx 재시작**

```shell
sudo systemctl restart nginx
```



이렇게 실행하면, http로 3000포트 접근시, 443 포트(https)로 리다이렉트 된다. 그리고 백엔드 url을 /api/**로 분기처리할 수 있다. 

- `https://도메인주소` 로 접근하면 배포한 웹 페이지에 접속할 수 있게된다.
- `http://ip주소`로 접근하면 배포한 웹 페이지에 접속할 수 있다.

&nbsp;


&nbsp;


# 📚 2. conf 파일 수정할 때

현재 이전 `proxy-setting`을 수정하고 싶을 때가 있다.

수정하고 싶을 때는 먼저

**✔️ nginx 종료 후, 설정해야 한다.**

`sudo systemctl stop nginx`을 입력하여 nginx을 종료한다.

`sudo letsencrypt certonly --standalone -d www제외한 도메인 이름` 을 입력하여 설정해준다.


<img width="1048" alt="스크린샷 2023-02-04 오전 10 18 33" src="https://user-images.githubusercontent.com/72541544/216737698-463b4af8-94f4-4ffd-bc5a-954c1fa65360.png">

&nbsp;


**✔️ /etc/nginx/sites-available**

`/etc/nginx/sites-available` 로 이동하여 proxy-setting 편집기를 열어 이와 같이 수정한다.



```shell
server {
        listen 80 default_server;
        listen [::]:80 default_server;

        server_name i8d208.p.ssafy.io;

        location /{
                proxy_pass http://localhost:3000;
                proxy_set_header Upgrade $http_upgrade;
                proxy_set_header Connection "upgrade";
        }

        location /api {
                proxy_pass http://localhost:8000/api;
        }

}
```




<img width="520" alt="스크린샷 2023-02-04 오전 10 12 41" src="https://user-images.githubusercontent.com/72541544/216737363-9f853420-9e27-4621-afdb-1b17aa830e57.png">





&nbsp;

**✔️ sites-enabled에서 이전에 만들었던 proxy-setting을 삭제한다.**


```shell
sudo rm proxy-setting
```
<img width="1024" alt="스크린샷 2023-02-04 오전 10 14 53" src="https://user-images.githubusercontent.com/72541544/216737499-c0deb904-5536-472a-be8e-dbeea8b601dd.png">

&nbsp;

**✔️ ln -s 명령어 실행한다. (다시 sites-enabled에 proxy-setting을 추가)**

<img width="1134" alt="스크린샷 2023-02-04 오전 10 05 03" src="https://user-images.githubusercontent.com/72541544/216736945-fe330631-b9d6-4c3c-bbe4-98625ae9b946.png">


```shell
sudo ln -s /etc/nginx/sites-available/proxy-setting /etc/nginx/sites-enabled/proxy-setting
```



&nbsp;



**✔️ 성공 여부 확인**


<img width="579" alt="스크린샷 2023-02-04 오전 10 06 09" src="https://user-images.githubusercontent.com/72541544/216737010-f7e2473e-197d-40a6-96d2-3986c6be53f5.png">


```shell
sudo nginx -t
```

`nginx test`가 성공했다는 것을 알 수 있다.


&nbsp;


**✔️ nginx 재시작**

```shell
sudo systemctl restart nginx
```



이렇게 실행하면, http로 3000포트 접근시, 443 포트(https)로 리다이렉트 된다. 그리고 백엔드 url을 /api/**로 분기처리할 수 있다. 


&nbsp;


**✔️ 결과**

`client http` : `http://3.36.87.75/`
`server http` : `http://3.36.87.75/api/`

<img width="442" alt="스크린샷 2023-02-04 오전 10 28 01" src="https://user-images.githubusercontent.com/72541544/216738778-0a0d215f-6a0c-4fe3-8f8c-857ca789a570.png">



<img width="663" alt="스크린샷 2023-02-04 오전 10 27 57" src="https://user-images.githubusercontent.com/72541544/216738764-e2e7e4c5-9fc4-4d35-8c30-ec62ca5e1b86.png">


&nbsp;


`client https` : `https://i8d208.p.ssafy.io/`
`server https` : `https://i8d208.p.ssafy.io/api/`

<img width="567" alt="스크린샷 2023-02-04 오전 10 28 25" src="https://user-images.githubusercontent.com/72541544/216738790-87690f12-f34e-47e2-956f-e26ed076c72e.png">



<img width="477" alt="스크린샷 2023-02-04 오전 10 28 12" src="https://user-images.githubusercontent.com/72541544/216738785-6fb39d01-a5c4-45f8-94f6-09260c963044.png">



&nbsp;

&nbsp;

