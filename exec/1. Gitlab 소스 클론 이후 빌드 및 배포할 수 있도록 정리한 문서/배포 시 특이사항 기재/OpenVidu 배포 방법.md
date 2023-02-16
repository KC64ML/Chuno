

# 📚 1. OpenVidu on-premise 배포


```shell
sudo su
```

- openvidu를 배포하기 : root 권한을 얻어야 한다.

&nbsp;


```shell
cd /opt
```


- openvidu를 설치하기 위해 권장되는 경로인 `/opt`로 이동


&nbsp;

```shell
curl <https://s3-eu-west-1.amazonaws.com/aws.openvidu.io/install_openvidu_latest.sh> | bash
```

- openvidu 설치

&nbsp;


```shell
cd openvidu
```

- 설치 후 openvidu가 설치된 경로로 이동



&nbsp;


```shell
$ nano .env

# OpenVidu configuration
# ----------------------
# 도메인 또는 퍼블릭IP 주소
DOMAIN_OR_PUBLIC_IP=i5a608.p.ssafy.io

# 오픈비두 서버와 통신을 위한 시크릿
OPENVIDU_SECRET=HOMEDONG

# Certificate type
CERTIFICATE_TYPE=letsencrypt

# 인증서 타입이 letsencrypt일 경우 이메일 설정
LETSENCRYPT_EMAIL=user@example.com

# HTTP port
HTTP_PORT=8442

# HTTPS port(해당 포트를 통해 오픈비두 서버와 연결)
HTTPS_PORT=8443
```


- 도메인 또는 public IP와 openvidu와 통신을 위한 환경설정


&nbsp;


```shell
$ ./openvidu start

Creating openvidu-docker-compose_coturn_1          ... done
Creating openvidu-docker-compose_app_1             ... done
Creating openvidu-docker-compose_kms_1             ... done
Creating openvidu-docker-compose_nginx_1           ... done
Creating openvidu-docker-compose_redis_1           ... done
Creating openvidu-docker-compose_openvidu-server_1 ... done

----------------------------------------------------

   OpenVidu Platform is ready!
   ---------------------------

   * OpenVidu Server: https://DOMAIN_OR_PUBLIC_IP/

   * OpenVidu Dashboard: https://DOMAIN_OR_PUBLIC_IP/dashboard/

----------------------------------------------------
```


- 설정 후 오픈비두 서버 실행(`ctrl + c`를 누르면 백그라운드로 실행됨)


&nbsp;


&nbsp;


# 📚 2. openvidu 관련 용어 정리



> **💡 참고**
> 
> `openvidu` : Docker Compose로 관리되는 일련의 Docker 컨테이너로 배포된다.
> - 설치되는 서비스(괄호 안은 openvidu 내 docker-compose로 인해 실행되는 docker container의 name이다.)




&nbsp;

**✏️ 용어**

- `OpenVidu Server(openvidu-server)` : OpenVidu 플랫폼의 두뇌. 시그널링을 담당한다. 
- `Kurento Media Server(kms)` : OpenVidu 플랫폼의 핵심. 미디어 서버를 담당한다. 
- `Coturn(coturn)` : 특정 특수 네트워크에서 브라우저단 간 미디어 통신을 허용하는데 사용되는 서버. 
- `Nginx(nginx)` : SSL 인증서를 구성하고 Openvidu 서버와 응용 프로그램이 모두 표준 https 포트(443)에서 제공되도록 하는 데 사용되는 역방향 프록시를 담당한다. 
- `OpenVidu-Call`: OpenVidu에서 기본으로 제공하는 어플리케이션 프로그램. 우리는 우리 어플리케이션에 적용하여 사용할 것이므로 비활성화 시켰다.



&nbsp;

&nbsp;

