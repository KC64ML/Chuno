

> **👨🏻‍🏭 title**
> 
> [🏃🏻 1. Project 소개 및 구현 화면](#🏃🏻 1. Project 소개 및 구현 화면)
> 
> [🏃🏻 2. 개발 환경](#🏃🏻 2. 개발 환경)
> 
> [🏃🏻 3. 협업 툴 ](#🏃🏻 3. 협업 툴 )
> 
> [🏃🏻 4. 프로젝트 회고](#🏃🏻 4. 프로젝트 회고)


&nbsp;


<img src="https://user-images.githubusercontent.com/72541544/220042420-522b82b3-53b2-4df2-a28f-f36faef915ee.png" width="128" height="128"/>


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

[추노 게임 화면 (클릭시 페이지 이동)](./README.assets/구현화면.md)
  

&nbsp;

&nbsp;

  
  

### 💰 서비스 주요 기능
  
- **서비스 설명** : **추노게임** 노비는 도망가며 노비문서를 찾으러 가다, 추노꾼은 노비를 잡으러 가다
- **주요 기능**
  - **`WebRTC`를 활용하여 화상정보를 실시간 공유하여 노비와 추노꾼에게 각자의 정보를 전송하여 서로의 위치를 추적할 수 있습니다.**
  - `google maps api`와 `websocket`기술을 사용하여 실시간으로 서로의 위치정보를 공유하여 노비문서와 노비들의 위치에 대한 정보를 교환할 수 있습니다.
  - 자이로 센서를 활용해 영상 단서 미제공에 대한 패널티 부여합니다.
  - 실시간 위치 정보 기반 미션을 수행합니다.
  - 다양한 아이템을 제공하며 게임의 역동성을 향상합니다.
  - 사용자의 게임 참여 실적에 따른 보상을 제공합니다.



&nbsp;

&nbsp;


  

# 🏃🏻 2. 개발 환경


> **💡 docker container 조회**
> 
> <img width="1413" alt="스크린샷 2023-02-17 오전 12 10 02" src="https://user-images.githubusercontent.com/72541544/219406596-f8d8ae3b-75b6-4957-a41a-a6c0d390c3a4.png">



&nbsp;

  

### 💰 사용된 기술


**🗡️ Android**

- `Android Studio`
- `Glide`
- `Retrofit2`
- `OkHttp3`
- `Jetpack`



&nbsp;



**🗡️ Frontend**


  - `vue3-geolocation`
  - `vuex`
  - `vue-router`
  - `@fawmi/vue-google-maps`
  - `scss`
  - `vue-cli`
  - `Google-Maps API`
  - `kakao-auth`
  - `websocket`


&nbsp;

  

**🗡️ Backend + DevOps(CI/CD)**

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


**🗡 WebSocket 기술 및 geo-location library를 활용**

- `websocket기술`을 활용하여 각 컴포넌트 별로 다양한 이벤트 들을 트리거하여 클라이언트간의 동시적인 정보교환을 가능하게 했습니다.
- `geo-location library`를 활용하여 자신의 위치정보를 획득 및 배포하여 서로간의 위치를 실시간으로 확인할 수 있도록 했습니다.


&nbsp;

**🗡 Web RTC, OpenVidu 라이브러리를 사용**

`OpenVidu`로만 할 수 있는 기능 뿐만이 아니라 백엔드를 함께 이용한 개발로 여러 기능을 구현했습니다. 각 방마다 인원수가 최대 10명까지만 들어갈 수 있게 구현했습니다. 그리고 private 방을 만들 수 있게 하여 비밀번호를 아는 사용자 외에는 들어오지 못하게 구현했습니다.
또한, `OpenVidu`라이브러리를 사용하여 `kurento-midea-server`를 거쳐 서로 영상정보를 송수신할 수 있는 시스템을 구축 및 적용했습니다.


&nbsp;


**🗡️ Docker, Jenkins를 이용한 CD 구축 및 SSL 인증서 적용**

`Docker image` `Jenkins`의 `pipeline`을 이용하여 자동 배포를 구축했습니다. `Gitlab webhook`을 설정하여 Jenkins에 빌드 트리거를 설정했으며 Gitlab에서 master 브랜치에 push하면 자동으로 배포될 수 있도록 구축하여 개발하는 과정에서 배포로 인한 시간 낭비를 줄였습니다.

- `frontend`, `Vue js`에서는 `Nginx`와 함께 `docker image`로 빌드한 후 배포
- `backend`, `springBoot`에서는 `docker image`로 빌드한 후 배포
- `nginx`와 `letsencrypt`를 통해 `ssl` 인증서를 적용했고 `frontend`에서는 `/`로 `80(http)`, `443(https)` 로 프록시를 분리시켰고, `backend`에서는 `/api`로 `80(http)`, `443(https)` 로 프록시를 분리


했습니다.


  

> **🤷‍♀️ 프로젝트 docker, jenkins를 통해 배포하는 방법**
>
> 프로젝트 docker, jenkins를 통해 배포하는 방법은 [여기서 설명](./exec/1. Gitlab 소스 클론 이후 빌드 및 배포할 수 있도록 정리한 문서/빌드 시 사용되는 환경 변수 등의 주요 내용 상세 기재)을 볼 수 있습니다.

  
  

&nbsp;

  

&nbsp;

  

# 🏃🏻 3. 협업 툴

  

> **🥁 추노꾼 협업 툴**
> 
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

  

[프로젝트 개발 단계 (클릭시 페이지 이동)](./README.assets/프로젝트 개발 단계.md)

  

&nbsp;


&nbsp;

  


  

# 🏃🏻 4. 프로젝트 회고



![공통 Team](https://user-images.githubusercontent.com/72541544/220129191-8e1cd5f0-2975-45fe-af8f-77c6f82fe5cc.png)




**🦉 이동하**

```text

(1) 컴포넌트 별의 모듈화가 제대로 이뤄지지 않았다 그 이유는 기획기간 동안 프론트 단에서의 컨벤션의 부재 때문이라고 생각한다. 이로인해서 깃에서의 많은 conflict가 발생하였고 이를 해결하는 것에서 많은 비효율성이 발생하였다. 이를 해결하기 위해 앞으로 기획 및 설계단계를 더 꼼꼼히 해야겠다는 생각을 했다.

(2) 사용하지않는 컴포넌트와 약속과는 다른 변수스타일 때문에 다른사람이 소스를 읽고 잘못된 코드를 읽고 이해하느라 비효율이 크게 증가하였다. 이를 해결하기 위해 다음부터는 팀원들간의 컨벤션에 더 주의를 기울이고 사용하지 않는 컴포넌트는 따로 잘 관리해야 겠다고 생각을 했다.

```


&nbsp;


**🐥 이아름**

```text

(1) git 충돌시 대처 사항을 메모하는 습관이 필요하다. git을 이용한 협업에서 의외로 같은 충돌이 많이 났지만 그때마다 배운 대처법이 기억나지 않아서 팀원을 번거롭게 하였다. 이를 보완하기 위해 충돌시에 대처사항을 간략하게나마 메모를 해야겠다고 생각했다.

(2) 코드에 주석을 제대로 달아야한다. 이번 프로젝트 진행으로 다른 사람의 코드를 읽는 것은 쉬운 일이 아니라는 것을 깨달았다. 이를 이해하기 위해 도움이 되었던 것이 주석이었기 때문에 코드를 작성할 때 주석이 꼭 필요하다고 생각했다.

```


&nbsp;



**🐭 이경창**


```text

크게 2가지를 배웠다!

(1) DevOps CI/CD에서 Jenkins Docker등 어떻게 동작하는지 알게 됐다.

(2) 협업할 때는 대면 시간이 중요하다. 프로젝트 개발 6주차 중, 5 ~ 6차에는 팀원끼리 수업 이후 카페에서 24시까지 같이 개발했었는데 이때 개발 속도가 상당히 빨랐다.

```



&nbsp;



**🐰 이채은**

```text

(1) 팀 프로젝트에 대한 이해도 향상
각종 컨벤션의 부재 때문에 같은 내용에 대한 반복적인 논의가 필요했다. 이 과정에서 많은 시간과 자원이 낭비되는 것을 느꼈는데, 작은 규모라도 협업에 용이한 시스템을 구축하는 것이 중요하다는 것을 배웠다.

(2) 개발 우선순위의 중요성
개발 과정의 중반까지 메인 기능인 게임보다 부가적인 기능 구현에 집중하게되어 주객이 전도되는 경험을 했다. 사전에 구현 우선순위를 포함한 기능명세서를 작성하고 개발 계획과 과정을 문서화 한다면 시간을 더 효율적으로 활용하고 프로젝트의 완성도도 높일 수 있을 것이다.

```


&nbsp;



**🐶 이주찬**

```text

(1) 초기 컨벤션의 정의가 되지 않아 통일성이 부족했다.

(2) ORM에 대한 이해가 부족한 상태에서 JPA를 사용하여 DB Connection에 비효율적인 부분이 많았다.

(3) 초기 기술 이해에 너무 많은 시간을 투자하여 실 서비스 개발에 투자할 시간이 충분치 않았다.

```


&nbsp;



&nbsp;

