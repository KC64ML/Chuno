
<img src="https://user-images.githubusercontent.com/72541544/220032526-a8bd5da5-b825-4746-8a32-b8369135d081.jpg" width="64" height="64"/>

<img src="https://user-images.githubusercontent.com/72541544/220042420-522b82b3-53b2-4df2-a28f-f36faef915ee.png" width="64" height="64"/>


**🗡️ Docker, Jenkins를 이용한 CD 구축 및 SSL 인증서 적용**

 `Docker image` `Jenkins`의 `pipeline`을 이용하여 자동 배포를 구축하였습니다. `Gitlab webhook`을 설정하여 Jenkins에 빌드 트리거를 설정했으며 Gitlab에서 master 브랜치에 push하면 자동으로 배포될 수 있도록 구축하여 개발하는 과정에서 배포로 인한 시간 낭비를 줄였습니다.
 - `frontend`, `Vue js`에서는 `Nginx`와 함께 `docker image`로 빌드한 후 배포
 - `backend`, `springBoot`에서는 `docker image`로 빌드한 후 배포
 - `nginx`와 `letsencrypt`를 통해 `ssl` 인증서를 적용했고 `frontend`에서는 `/`로  `80(http)`, `443(https)` 로 프록시를 분리시켰고, `backend`에서는 `/api`로 `80(http)`, `443(https)` 로 프록시를 분리

했습니다.


> **🤷‍♀️ 프로젝트 docker, jenkins를 통해 배포하는 방법**
> 프로젝트 docker, jenkins를 통해 배포하는 방법은 [여기서 설명](./exec/1. Gitlab 소스 클론 이후 빌드 및 배포할 수 있도록 정리한 문서/빌드 시 사용되는 환경 변수 등의 주요 내용 상세 기재)을 볼 수 있습니다.
