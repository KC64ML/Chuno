

**SSAFY에서 제공되는 ec2를 받기 전, aws ec2 free 메모리 1GB을 이용**하다 발생한 문제들을 해결한 후 설명한 글입니다.


&nbsp;

# 📚 1. 현재 나의 상황

**✔️ EC2 Ubuntu 20.04 LTS**
- 스토리지 30
- port 여러개
- 메모리 1GB (free 요금으로 ec2를 이용할 경우 메모리 크기가 1GB이다.)

&nbsp;


local에서 만든 프로젝트를 가상 서버에 배포하기 위해 ec2 환경을 생성했다.
다만, 매번 배포 파일을 만들기 위해 xxx.jar 파일을 만들어야 했다.

=> 이를 위해 CI/CD Jenkins 환경을 구축하게 되었다.


**Freestyle project, Pipeline** 등 아이템을 여러 번 새로 만들어 봤지만 빌드 실행할 때 공통적으로 문제가 발생한 곳이 있다. (build 실행을 70번 정도 한 것 같다.)

<img width="281" alt="Screenshot 2023-01-12 at 11 16 58 PM" src="https://user-images.githubusercontent.com/72541544/212090051-3585f095-325e-4210-b74a-bbc6c8eec893.png">


`./gradlew build` or `./gradlew clean bootJar`

`./gradlew` : 새로운 환경에서 프로젝트를 설정할 때 java나 gradle을 설치하지 않고 바로 빌드할 수 있게 해주는 역할을 한다.


이 명령어가 실행되는 순간 서버가 터지는 (멈추는 상황이 발생했다.) 😅

아니 도대체 무엇이 문제인거지?



&nbsp;


&nbsp;

# 📚 2. 해결책

혹시 aws ec2 프리티어 이기 때문에 무엇인가 문제가 있는 걸까? 라고 구글링을 하던 순간

# AWS EC2 프리티어 쓰시는분들 참고하세요!
를 읽게 되었다. (https://okky.kr/articles/884329)

나와 같이, github 웹 훅과 Jenkins를 이용하여 CI/CD 구축을 성공적으로 마치고 jar를 실행하면 EC2 서버가 먹통이 되어버린 상황이였다.
그리고.... **_(키보드 입력도 안먹히고 외부접속도 안된다. 강제종료 - 재시작밖에 할 수 없는 상황이었다.)_**
라고 하시는데 나도 몇 일동안 계속 이런 상황을 만났었다. 😱

&nbsp;


### 📖 A. 현재 문제점

Jenkins Build에서 `java -jar` 명령어 입력시 EC2가 정지되어 배포가 되지 않는 문제!!!!

AWS EC2 재부팅하면 EC2는 정상화되지만, jar 재실행시 계속 멈추는 상황

**Jenkins에서 배포 자동화를 시도할 때만 EC2가 멈추는 문제가 발생!
직접 ubuntu 터미널에 접속해서 쉘 스크립트를 동작시키면 정상 배포가 완료된다.**


&nbsp;

**✔️ 현재 새로운 환경 jenkins에서 jar 재실행하다 발생한 오류 화면**

<img width="864" alt="스크린샷 2023-01-15 오전 10 35 18" src="https://user-images.githubusercontent.com/72541544/212505351-8bd56684-0d42-4b45-babb-141bd0fe40c3.png">

<img width="860" alt="스크린샷 2023-01-15 오전 10 35 10" src="https://user-images.githubusercontent.com/72541544/212505342-cd52d75f-4f97-4c92-8e6a-071cdd7e0d6f.png">

&nbsp;


왜 안될까? 아니 구글링해보니 프리 티어 메모리가 1GB였다...

리눅스에서 메모리 상태 확인하는 명령어 : `free -h`

<img width="652" alt="스크린샷 2023-01-15 오전 10 45 09" src="https://user-images.githubusercontent.com/72541544/212508566-eff48885-2923-4ebc-99e5-eb719b45c5d0.png">


아 그렇구나, 이 상태에서 Jenkins 환경에서 스프링 부트를 띄우니 ec2가 멈춘 것이다.


메모리를 늘리거나, 젠킨스와 스프링 부트를 분리시켜야 한다. (Docker를 이용하면 될 것 같다.)

또 다른 방법으로, 리눅스는 하드디스크를 가상 메모리로 전환시켜 사용할 수 있다고 한다. (스와핑, Swapping)

[공식 AWS Swapping 설명 자료]([aws.amazon.com/ko/premiumsupport/knowledge-center/ec2-memory-swap-file/](https://aws.amazon.com/ko/premiumsupport/knowledge-center/ec2-memory-swap-file/))

&nbsp;


**✔️ AWS에서 메모리의 양에 따라 스왑 메모리 크기를 권장한다.**

|물리적 RAM의 양|권장 스왑 공간|
|-|-|
|RAM 2GB 이하|RAM 용량의 2배(최소 32MB)|
|RAM 2GB 초과, 32GB 미만|4GB + (RAM - 2GB)|
|RAM 32GB 이상|RAM 용량의 1배|

- 스왑 공간은 절대로 32MB 미만이 되어서는 안된다.


&nbsp;


### 📖 B. 해결책

<img width="667" alt="스크린샷 2023-01-15 오전 9 59 58" src="https://user-images.githubusercontent.com/72541544/212511644-7d5358d2-46aa-4758-9ee0-d656c1f50c60.png">


```bash
$ sudo dd if=/dev/zero of=/swapfile bs=128M count=16
```

- dd 명령을 사용하여 루트 파일 시스템에 스왑 파일을 생성한다.
- bs : 블록 크기, count : 블록의 수
- 지정한 블록 크기는 인스턴스에서 사용 가능한 메모리보다 작아야 한다. (그렇지 않을 시, memory exhausted 오류가 발생한다.)


&nbsp;

권장사항에 의하면 스왑 공간 크기는 RAM 용량의 2배를 권장했으므로
2GB 증설시켜야 한다. (128MB x 16 = 2.048MB)


&nbsp;

```bash
$ sudo chmod 600 /swapfile
```

- 스왑 파일에 대한 읽기 및 쓰기 권한을 업데이트

&nbsp;


```bash
$ sudo mkswap /swapfile
```

- Linux 스왑 영역을 설정한다.

&nbsp;


<img width="653" alt="스크린샷 2023-01-15 오전 10 00 39" src="https://user-images.githubusercontent.com/72541544/212511655-798f53af-ff80-4468-96a5-bfea8f99f62a.png">



```bash
$ sudo swapon /swapfile
```

- 스왑 공간에 스왑 파일을 추가하여 스왑 파일을 즉시 사용할 수 있도록 만든다

&nbsp;

```bash
$ sudo swapon -s
```

- 절차가 성공했는지 확인

&nbsp;


```bash
$ sudo vi /etc/fstab
```

- `/etc/fstab` 파일을 편집하여 부팅 시 스왑 파일을 활성화

&nbsp;


<img width="656" alt="스크린샷 2023-01-15 오전 10 01 16" src="https://user-images.githubusercontent.com/72541544/212511661-6ec693c3-fe9a-4517-9018-cbc550788cb2.png">

```bash
/swapfile swap swap defaults 0 0
```

- vi 편집기 파일을 연 후, 파일 끝에 이것을 추가한 후, 저장한 다음 종료


&nbsp;


```bash
$ free -h
```


<img width="665" alt="스크린샷 2023-01-15 오전 11 11 43" src="https://user-images.githubusercontent.com/72541544/212518598-75ffbf4b-33ad-4f71-a7c4-543ac3adfad3.png">


- Memory 2GB 추가되었다.


&nbsp;

&nbsp;


# 📚 3. 빌드 실행 결과

이전 memory 1gb일 때는 3시간 정도 기다렸는데, 오류가 발생함
이제는 2분안에 성공한 결과를 볼 수 있다.
<img width="856" alt="스크린샷 2023-01-15 오전 10 10 16" src="https://user-images.githubusercontent.com/72541544/212519294-d62b761c-cbf3-4c5c-8c66-f81ccf6599fa.png">

<img width="1717" alt="스크린샷 2023-01-15 오전 10 09 20" src="https://user-images.githubusercontent.com/72541544/212519277-bafb92de-1638-480a-bd9e-bd64bf95cdf6.png">
<img width="849" alt="스크린샷 2023-01-15 오전 10 09 30" src="https://user-images.githubusercontent.com/72541544/212519290-24f52bdd-0552-43b9-a21f-3ba962b534a4.png">






&nbsp;


&nbsp;

-----
참고
- https://okky.kr/articles/884329


