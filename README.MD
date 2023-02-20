

[🏃🏻 1. Project 소개 및 구현 화면](# 🏃🏻 1. Project 소개 및 구현 화면)

[🏃🏻 2. 개발 환경](# 🏃🏻 2. 개발 환경)

[🏃🏻 3. 협업 툴 ](# 🏃🏻 3. 협업 툴 )

[🏃🏻 4. 프로젝트 회고](# 🏃🏻 4. 프로젝트 회고)


&nbsp;

<img src="https://user-images.githubusercontent.com/72541544/220042420-522b82b3-53b2-4df2-a28f-f36faef915ee.png" width="64" height="64"/>

# 🏃🏻 1. Project 소개 및 구현 화면

> **🧭 프로젝트 기간**
> 
> - `1월 10일` ~ `2월 16일`
 

&nbsp;

### 💰 Project 소개 

**🗡️ 주제 선정 계기**

```text
A : 무한도전 술개잡기 해보고 싶다.
A : 나도 해보고 싶은데 일상에서 어떻게 하지?
A : 일상에서 하려고하니 시스템과 감독 카메라 등이 없다.
A : 게임 판단, 진행, 기록, 아이템 사용 등에 어려움이 있다.
A : 술래잡기 만들어보자
B : 근데, 술래잡기에 컨셉이 너무 없는 것 같아
B : 컨셉 추가된 추노 어때?
B : 도망가는 사람은 노비!
B : 쫓는 사람은 추노꾼!
```


&nbsp;

&nbsp;


### 💰 추노 게임 설명

[추노 게임 화면 page 이동](./README.assets/구현화면.md)


&nbsp;


&nbsp;


### 💰 서비스 주요 기능

![[스크린샷 2023-02-16 오전 4.32.08.png]]

- **서비스 설명** : **추노게임** 노비는 도망가며 노비문서를 찾으러 가다, 추노꾼은 노비를 잡으러 가다
- **주요 기능**
  - 프론트에서 중요한 2가지
  - 백엔드에서 중요한 2가지
  - `git push` 할 시 자동으로 변경된 내용 배포 환경에 적용되다.
  - `webRTC`를 통해 실시간으로 노비는 노비문서를 찾다, 추노꾼은 노비를 잡다.


&nbsp;

&nbsp;


# 🏃🏻 2. 개발 환경



> **💡 docker container 조회**
> 
> <img width="1413" alt="스크린샷 2023-02-17 오전 12 10 02" src="https://user-images.githubusercontent.com/72541544/219406596-f8d8ae3b-75b6-4957-a41a-a6c0d390c3a4.png">


&nbsp;



### 💰 사용된 기술

**🗡️ Android**



&nbsp;

**🗡️ Frontend**

  - `vue3-geolocation` : 1.0.0
  - `vuex` : 4.0.0
  - `vue-router` : 4.0.3
  - `@fawmi/vue-google-maps` : 0.9.79
  - `scss` : 1.57.1

&nbsp;

**🗡️ Backend + DevOps(CI/CD)

- `java 11`
- `spring boot 2.7.8`
- `gradle-7.6-bin`
- `Intellij`
- `mysql`
- `aws ec2`
- `docker`
- `nginx`
- `jenkins`



&nbsp;


**🗡️ Web RTC**

- `openvidu-proxy` : 2.25.0
- `kurento-media-server` : 6.18.0
- `openvidu-coturn` : 2.25.0
- `openvidu-server` : 2.25.0


&nbsp;

&nbsp;


### 💰 기술 특이점

**🗡 frontend**

&nbsp;

**🗡️ Docker, Jenkins를 이용한 CD 구축 및 SSL 인증서 적용**

 `Docker image` `Jenkins`의 `pipeline`을 이용하여 자동 배포를 구축하였습니다. `Gitlab webhook`을 설정하여 Jenkins에 빌드 트리거를 설정했으며 Gitlab에서 master 브랜치에 push하면 자동으로 배포될 수 있도록 구축하여 개발하는 과정에서 배포로 인한 시간 낭비를 줄였습니다.
 
 - `frontend`, `Vue js`에서는 `Nginx`와 함께 `docker image`로 빌드한 후 배포
   
 - `backend`, `springBoot`에서는 `docker image`로 빌드한 후 배포
   
 - `nginx`와 `letsencrypt`를 통해 `ssl` 인증서를 적용했고 `frontend`에서는 `/`로  `80(http)`, `443(https)` 로 프록시를 분리시켰고, `backend`에서는 `/api`로 `80(http)`, `443(https)` 로 프록시를 분리

했습니다.


> **🤷‍♀️ 프로젝트 docker, jenkins를 통해 배포하는 방법**
> 
> 프로젝트 docker, jenkins를 통해 배포하는 방법은 [여기서 설명](./exec/1. Gitlab 소스 클론 이후 빌드 및 배포할 수 있도록 정리한 문서/빌드 시 사용되는 환경 변수 등의 주요 내용 상세 기재)을 볼 수 있습니다.



&nbsp;






&nbsp;

&nbsp;





# 🏃🏻 3. 협업 툴 

> **🥁 추노꾼 협업 툴** 
> [notion](https://www.notion.so/e35d3b316e19451993cdeea158e9bffd) 
> [mattermost](https://meeting.ssafy.com/s08p10d2/channels/d208)
> [Jira](https://ssafy.atlassian.net/jira/your-work)


&nbsp;


### 💰 ec2 port


|PORT|이름|
|-|-|
|`80`|`HTTP`|
|`443`|`HTTPS`|
|`3000`|`Vue js, Nginx Docker Container`|
|`3305`|`MySQL Container`|
|`3478`|`OpenVidu`|
|`7777`|`jenkins`|
|`8000`|`Spring Boot Docker Container`|



&nbsp;


&nbsp;


### 💰 프로젝트 개발 단계

[프로젝트 개발 단계](./README.assets/프로젝트 개발 단계.md)


&nbsp;


&nbsp;




# 🏃🏻 4. 프로젝트 회고

넣을지 고민해보기


😎 이경창

```text
크게 2가지를 배웠다! 
(1) DevOps CI/CD에서 Jenkins Docker등 어떻게 동작하는지 
(2) 협업할 때는 대면 시간이 중요하다. 프로젝트 개발 6주차 중, 5 ~ 6차에는 팀원끼리 수업 이후 카페에서 24시까지 같이 개발했었는데 이때 개발 속도가 상당히 빨랐다.
```


&nbsp;



&nbsp;

