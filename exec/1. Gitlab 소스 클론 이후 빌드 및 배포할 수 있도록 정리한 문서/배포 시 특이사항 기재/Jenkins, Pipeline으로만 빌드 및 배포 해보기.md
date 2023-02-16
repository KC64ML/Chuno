
> **📌 시작하기전**
> docker 없이 Jenkins, Pipeline으로만 빌드 및 배포해보기


&nbsp;



> **🔔 젠킨스 파이프라인을 활용한 배포 자동화**
> - 파이프라인을 활용하여 배포 자동화

&nbsp;


# 📚 1. Jenkins 어떤 구조로 동작할까?

**✔️ 현재 상황**

프로젝트에 기능을 추가해서 base branch에 merge 되었다. 이제 실제 서버에 배포를 해야하겠다.


&nbsp;

**✔️ 팀프로젝트에서 많이 사용하는 work flow - 현재 매번 실행되어야 한다.**

<img width="786" alt="스크린샷 2023-01-14 오전 12 54 46" src="https://user-images.githubusercontent.com/72541544/212362539-477996c8-6565-485d-a852-b20a1f261cf4.png">
(1) 각 개발자가 기능을 구현한 후 PR

(2) EC2 서버로 접속

(3) github에 merge된 코드를 git pull

(4) gradle을 이용해 bootJar

(5) jar 파일을 java -jar 명령어를 통해 80port에 실행한다.

=> 5가지를 매번 실행해야 한다. 귀찮지 않은가? `Jenkins`를 이용하면 해결할 수 있다.

&nbsp;


> **💡 참고**
> - 서버 배포는 매번 기능이 추가될 때마다 시행되어야 한다.
> - 현재 flow는 새로운 기능마다 바뀌는 것이 아니기 때문에, 자동화 할 수 있는 대상이다.

&nbsp;

**✔️ Continuous Distribution - 지속적 배포**

`flow`를 자동화하여 최신 상태의 코드가 자동으로 배포되는 것


&nbsp;


**✔️ Jenkins가 해주는 것은?**

**`Jenkins`가 기능 구현을 해주지는 않는다.**

Jenkins가 해주는 부분은?

(1) github으로부터 merge 되었다는 연락을 받아서

(2) 코드를 git clone하고

(3) gradle로 프로젝트를 build 하여

(4) build한 목적파일을 어플리케이션이 실행되어야 할 서버로 전달하고, 어플리케이션을 구동하기 위한 명령어를 실행한다.


&nbsp;

**✔️ Jenkins의 설정이 배포를 할때 어떤 부분을 자동화 해주는 걸까?**

<img width="791" alt="스크린샷 2023-01-14 오전 8 31 46" src="https://user-images.githubusercontent.com/72541544/212437241-8a57d8b3-ad7b-43f3-9a36-2c71211c0166.png">


&nbsp;


> **💡 참고**
> 어플리케이션 서버에서 Jenkins를 구동할 경우, 빌드가 진행될 때마다 CPU 자원이 모자라서 서버가 다운되는 일이 있다.
> 
> 이로 인해 별도 EC2로 분리하는 것이 좋다.
> 
> 만약, **서버 자원이 모자라거나 여러 개의 WAS를 띄울 일이 생긴다면, 별도 환경으로 분리하는 것을 추천한다.**



&nbsp;


**✔️ Ubuntu 초기설정(20.04)**

```java
sudo apt-get update
sudo apt-get install apache2
sudo apt-get install openjdk-11-jdk
```

&nbsp;

[Jenkins 설치 참고](https://velog.io/@chang626/Jenkins)


- sudo cat ~/initialAdminPassword :  첫 설치시 필요한 password


&nbsp;

> **💡 참고**
> `sudo apt-get install openjdk-11-jdk` : java 11 jdk 설치

&nbsp;


# 📚 2. credentials 세팅

컴퓨터가 나 대신에 git clone을 하게 하려면 해당 레포지토리에 대한 권한이 필요하다!

&nbsp;


> **💡 참고**
> - 공개 레포지토리에 있는 소스코드를 clone 하는 목적이라면 권한이 필요없기 때문에 따로 credentials를 설정하지 않아도 된다.
> - `PipeLine` 상에서 push 또는 서브모듈 등 권한이 필요한 일이 있다면 `credentials`이 필요하다.


&nbsp;


### 📖 A. access token 발급 및 test

<img width="719" alt="스크린샷 2023-01-14 오전 11 00 02" src="https://user-images.githubusercontent.com/72541544/212445728-bba0ae3b-0910-4807-9cd1-559164e9f3a7.png">

- 시스템 설정


&nbsp;

**✔️ github repository 연결**

<img width="1437" alt="스크린샷 2023-01-14 오전 11 02 04" src="https://user-images.githubusercontent.com/72541544/212445730-1f471176-4975-4afa-b30d-2c23933b348d.png">
- github 탭으로 이동, add  누른다.

<img width="1304" alt="스크린샷 2023-01-14 오전 11 10 47" src="https://user-images.githubusercontent.com/72541544/212446083-dc060a90-89a2-405a-b42b-fc8873a163be.png">
- Secret text 선택, Secret(github token), ID는 아무거나

&nbsp;

<img width="1222" alt="스크린샷 2023-01-14 오전 11 10 53" src="https://user-images.githubusercontent.com/72541544/212446085-98fb5366-a27d-46b6-bd49-ea68636f1b08.png">

- test Connection이 발생할 경우 성공

&nbsp;


**✔️ credentials이 필요하다 (미리 생성하자)**

- 시스템 설정의 github과 연관된 것은 credentials에 Secret text로 등록한 credentials만 인식한다.
  
- 지금은 Username with password 로 생성한 credentials이 필요하다.



&nbsp;


**✔️ 등록 혹은 확인 가능하다.**

`credentials`는 추후 `manage credentials`에서 등록 혹은 확인이 가능하다.

<img width="724" alt="스크린샷 2023-01-14 오전 11 17 45" src="https://user-images.githubusercontent.com/72541544/212446325-6fe8278e-6c1a-434c-bca2-21630e75a7e6.png">

<img width="1225" alt="스크린샷 2023-01-14 오전 11 20 38" src="https://user-images.githubusercontent.com/72541544/212446395-9160da62-39fb-48f8-926c-0c1fa9dcbd53.png">




&nbsp;


&nbsp;


# 📚 2. 파이프라인 생성하기

> **💡 참고**
> - 간단한 Job을 위해선 freestyle도 좋은 선택 (비교적 세팅이 단순)
>   
> - 복잡한 종류의 Job간의 연계나 상세한 세팅, UI (각 job별로 어떻게 진행되고 있는지 보여준다.)를 원한다면 파이프라인을 선택


<img width="1432" alt="스크린샷 2023-01-14 오전 11 25 04" src="https://user-images.githubusercontent.com/72541544/212446578-fbf31d81-edfa-4735-bc3b-a64e8736a84a.png">



&nbsp;


**✔️ pipeline script**

pipeline script은 Groobee 또는 Jenkins에서 정의한 pipeline syntax를 통해 item에서 수행할 Job을 선언하고, 순서를 조정하고, 환경을 설정하는 역할을 한다.

[PipeLine 공식문서](https://www.jenkins.io/doc/book/pipeline/)

&nbsp;


**✔️ pipeline syntax**

플러그인 별로 syntax를 만들어준다.


<img width="1408" alt="스크린샷 2023-01-14 오후 2 21 05" src="https://user-images.githubusercontent.com/72541544/212457276-5503caaf-e9f1-478b-9071-72e1c74aa8fe.png">


(1) `pipe line`은 이 파이프라인 자체를 의미한다.

(2) `agent`는 이 파이프라인 스크립트를 실행할 `executor`를 지정한다. `any`로 둘 시 어떤 `executor`도 실행할 수 있다는 의미가 된다.  

(3) `stages`는 실행할 Job들의 집합이다.  

(4) `stage`는 각각의 Job을 의미한다. Job 내부의 단계를 의미하는 steps를 포함해야한다. 

(5) `steps`에선 실제로 실행할 쉘이나 `syntax`를 입력해주면 된다.


&nbsp;

&nbsp;

# 📚 3. Git Clone 하기

`git clone` 하기 위해서는 `syntax`를 만들어야 한다. (pipeline syntax를 클릭)

`pipeline syntax -> snippet generate` click

<img width="1413" alt="스크린샷 2023-01-14 오후 2 28 56" src="https://user-images.githubusercontent.com/72541544/212457494-6ca99c36-4136-49af-88e0-4b9f38591f52.png">
- git check

&nbsp;


<img width="1417" alt="스크린샷 2023-01-14 오후 2 29 12" src="https://user-images.githubusercontent.com/72541544/212457501-e0197c08-8038-4107-921e-5e9fc48eb42b.png">

<img width="1435" alt="스크린샷 2023-01-14 오후 2 33 17" src="https://user-images.githubusercontent.com/72541544/212457588-d4329965-234a-406b-b01d-facdc4aae95d.png">



- `repository url`을 적어주고, `branch`를 선택하고, `credentials`를 넣어준다. 
  
- 아까 추가하지 않았다면 지금 추가해준다. 
  
- **공용 레포지토리를 clone하는 입장이라면 비워놓아도 된다.**

&nbsp;



<img width="1403" alt="스크린샷 2023-01-14 오후 2 34 19" src="https://user-images.githubusercontent.com/72541544/212457613-fa4e17eb-0f8c-4781-aee2-3888721ee3ea.png">



- generate를 누르면 스니펫이 생성된다. 해당 내용을 복사하자!




&nbsp;


<img width="1414" alt="스크린샷 2023-01-14 오후 2 36 54" src="https://user-images.githubusercontent.com/72541544/212457668-ad632bfa-128b-4709-a79b-f54f078ddea9.png">
- `stage` 이름을 원하는 이름으로 변경하고, `steps`에는 위에서 생성한 `snippet`을 붙여주면 된다.
  
- **읽어보면, git 특정 브랜치에서 특정 `credentials`로 url의 레포지토리에서 소스를 가져오는 `snippet`이다.**


=> 이제 저장을 누르면, **`github`을 `clone` 하는 job을 가진 파이프라인을 생성한 것이다.**

&nbsp;

> **💡 참고**
> - **한 번에 전체 job을 작성하면 trouble shooting이 힘들어지므로 각 단계별로 테스트 하자.** 
> - 대쉬보드에서 생성한 아이템을 클릭하여 들어갑시다.

&nbsp;

**✔️ 이전까지 작업한 내용 테스트**

<img width="1431" alt="스크린샷 2023-01-14 오후 2 45 42" src="https://user-images.githubusercontent.com/72541544/212457898-d7d28f38-d63a-40b7-aece-7cccbeb90062.png">
- 수동적으로 build 할시, job을 유발한다.
  
- 실행되어 Build History에 잘 들어간 것을 볼 수 있다.

&nbsp;


**✔️ 빌드 과정에서 출력되는 콘솔문은 `console output` 탭에서 확인할 수 있다.**

<img width="1383" alt="스크린샷 2023-01-14 오후 2 49 59" src="https://user-images.githubusercontent.com/72541544/212458016-29d3b3b1-c249-43c5-b895-6b9440b70295.png">
<img width="1091" alt="스크린샷 2023-01-14 오후 2 50 06" src="https://user-images.githubusercontent.com/72541544/212458023-1511d5da-860a-4be0-b09b-4df0719d2dcf.png">

- `pipeline`  job의 진행 경과 및 `github clone`을 진행하며 입력된 명령어와 출력문들을 확인할 수 있다.


&nbsp;

&nbsp;


# 📚 4. Build 

> **📣 시작하기전**
> 
> - 빌드는(Build) clone해 온 소스에 포함되어 있는 gradle wrapper를 활용한다!
>   
> - 프로젝트에서 .gradlew 위치를 찾아야 한다.
>   
> - dir 스니펫 : cd
>   
> - `sh ''' ${쉘 명령어} '''` : pipeline syntax에서 쉘 명령어를 실행하도록 설정하는 부분
>   
> - 현재 빌드를 위해 실행해야할 테스트 : `clean`, `bootJar`


&nbsp;


<img width="1728" alt="스크린샷 2023-01-15 오전 12 09 24" src="https://user-images.githubusercontent.com/72541544/212478779-56d5df4f-7a06-4819-b019-41d50beb233e.png">


```pipeline
pipeline {
    agent any

    stages {
        stage('github clone') {
            steps {
                git branch: 'main', credentialsId: 'repo-and-hook-access-token-credentials', url: 'https://github.com/ToDoStudy/study_jenkins'
            }
        }
        
        stage('build'){
            steps {
                dir('jenkins') {
                    sh '''
                        echo 'start bootJar'
                        ./gradlew clean bootJar
                    '''
                }
            }
        }
    }
}

```


&nbsp;

**✔️ 스니펫(snippet)**
스니펫(snippet) : **재사용 가능한 소스 코드, 기계어, 텍스트의 작은 부분**을 일컫는 프로그래밍 용어

- dir : cd
  
- `stages` : 실행할 Job들의 집합
  
- `stage` : 각각의 Job을 의미

&nbsp;


**✔️ build 결과(실패)**

<img width="1707" alt="스크린샷 2023-01-15 오전 12 03 47" src="https://user-images.githubusercontent.com/72541544/212478792-e6ef6cba-525d-42d5-9ea1-cd324e1b6007.png">

<img width="781" alt="스크린샷 2023-01-15 오전 12 00 24" src="https://user-images.githubusercontent.com/72541544/212478790-743ea451-0d5e-4197-8bb4-cee1acfcf010.png">
- 현재 서브 모듈을 init하고 update하지 않는다!


현재 실패한 결과를 얻게 되었다. 이유는?
코드를 빌드할 때 서브모듈이 필요한데, 해당 내용을 고려하지 않았기 때문이다. (만약에, 서브 모듈이 없었다면 성공이다!)

**`git plugin snippet`은 서브 모듈의 `init`과 `update`까지 지원해주지 않는다.**

=> 이렇게 생성한 syntax가 원하는대로 동작하지 않는다면 다른 step을 찾거나, 공식문서를 통해 원하는 설정이 있는지 확인해야 한다.


&nbsp;



**✔️ 서브모듈까지 받아오기 위해 다른 step syntax을 활용해보자**

<img width="1685" alt="스크린샷 2023-01-15 오전 12 23 08" src="https://user-images.githubusercontent.com/72541544/212479697-767be7ee-0afb-497e-833b-533b18b993b0.png">

서브 모듈까지 받아오기 위해서는 다른 step syntax을 활용해보자!
checkout으로 검색하여 해당 simple step을 선택한다.
private repository로 등록한 서브모듈을 받아오기 위한 여정이므로! credentials를 세팅해준다.


&nbsp;


<img width="1706" alt="스크린샷 2023-01-15 오전 12 24 19" src="https://user-images.githubusercontent.com/72541544/212479701-5f7ae833-59d6-44c6-9667-2b6a9d37ae58.png">
- `pipeline syntax`에서 `additional behaviours`의 add 버튼을 클릭한 후, `Advanced sub-modules behaviours`를 눌러 위와 같이 설정해준다.
  
- `Generate Pipeline Script`을 눌러서, 부모 레포지토리의 `credentials`를 그대로 활용한다.


&nbsp;


<img width="1716" alt="스크린샷 2023-01-15 오전 12 27 35" src="https://user-images.githubusercontent.com/72541544/212479703-c5a75d96-b92b-4cd8-b5e6-313352f0d78b.png">
- 생성한 `pipeline script`를 steps 하위에 붙여넣어서 **기존의 `git syntax`를 대체해준다.**
  
- 서브 모듈 관련 명령어가 추가되었다.


&nbsp;


**✔️ 현재까지의 파이프라인**

```pipeline
pipeline {
    agent any

    stages {
        stage('github clone') {
            steps {
                checkout scmGit(branches: [[name: '*/main']], extensions: [submodule(parentCredentials: true, reference: '', trackingSubmodules: true)], userRemoteConfigs: [[credentialsId: 'repo-and-hook-access-token-credentials', url: 'https://github.com/ToDoStudy/study_jenkins']])
            }
        }
        
        stage('build'){
            steps {
                dir('jenkins') {
                    sh '''
                        echo 'start bootJar'
                        ./gradlew clean bootJar
                    '''
                }
            }
        }
    }
}

```



&nbsp;


**✔️ 테스트 결과**

처음에는 3시간 정도 기다려도 빌드에서 오류가 발생했었다. (이는 가상메모리 ec2 메모리 크기가 1GB 이기 때문이다.)


<img width="856" alt="스크린샷 2023-01-15 오전 10 10 16" src="https://user-images.githubusercontent.com/72541544/212519814-446ff96c-d927-4a88-924d-aff817d3ffa6.png">



<img width="1717" alt="스크린샷 2023-01-15 오전 10 09 20" src="https://user-images.githubusercontent.com/72541544/212519277-bafb92de-1638-480a-bd9e-bd64bf95cdf6.png">
<img width="849" alt="스크린샷 2023-01-15 오전 10 09 30" src="https://user-images.githubusercontent.com/72541544/212519290-24f52bdd-0552-43b9-a21f-3ba962b534a4.png">


&nbsp;


&nbsp;




# 📚 5. ssh를 이용하여 서버로 jar 파일 전달하기

이제 배포용 Jenkins에서 WAS용 EC2로 Build 결과 파일을 전달해야 한다.
이를 위해서는 Publish over ssh 플러그인을 활용해주면 된다.


**✔️ 플러그인 추가**

<img width="869" alt="스크린샷 2023-01-15 오후 3 09 15" src="https://user-images.githubusercontent.com/72541544/212528116-7642a2aa-9a62-4c6f-b7ea-865f99b88699.png">
<img width="1714" alt="스크린샷 2023-01-15 오후 3 09 59" src="https://user-images.githubusercontent.com/72541544/212528119-c870a71a-5b76-4e66-b1d9-526b731e2156.png">

&nbsp;

<img width="864" alt="스크린샷 2023-01-15 오후 3 11 48" src="https://user-images.githubusercontent.com/72541544/212528120-3a752b1c-3726-4b5b-aa77-3b65e1c85b52.png">

- ssh를 통해 파일을 보내기전에, pem키의 정보가 필요하다.
  
- EC2에 접속할 수 있는 pem키의 내용을 얻어온 후, 내용을 복사한 후, EC2 접속에 필요한 pem키의 내용을 사진 key에 붙여넣어 준다.



<img width="818" alt="스크린샷 2023-01-15 오후 4 20 56" src="https://user-images.githubusercontent.com/72541544/212528243-e59ad3c7-73a2-42da-8b60-a86ffe721397.png">


`Name` : syntax에서 참조할 수 있는 이름
`HostName` : 빌드된 파일을 전송할 서버의 private ip (같은 vpc에 속해있어 private ip로 접근할 수 있다.)
`username` : ubuntu(default)
`Remote Directory` : 파일이 도착할 디렉터리를 적어준다. (ec2 가상서버에서 생성한 디렉터리로 해줘야함 아니면,  `jenkins.plugins.publish_over.bappublisherexception` 발생)

<img width="1475" alt="스크린샷 2023-01-15 오후 4 28 24" src="https://user-images.githubusercontent.com/72541544/212528437-1b37d9e3-6f89-4e32-9b02-9b918749fabc.png">
<img width="1552" alt="스크린샷 2023-01-15 오후 4 29 46" src="https://user-images.githubusercontent.com/72541544/212528455-bc7ffd7b-fd43-45cc-89c1-da16666b39b6.png">

현재는 `/home/ubuntu`로 변경


<img width="661" alt="스크린샷 2023-01-15 오후 4 28 55" src="https://user-images.githubusercontent.com/72541544/212528438-22fba431-8ac8-4b01-bcc9-1532ff12ab94.png">


&nbsp;


> **💡 참고**
> (Ubuntu22버전 사용할 경우 jenkins.plugins.publish_over.bappublisherexception: failed to connect and initialize ssh connection. message: failed to connect session for config 와 같은 오류를 만날 수도 있으니 Ubuntu20.04.5 사용하기를 권장한다.) -> 내가 경험한 오류


&nbsp;

&nbsp;


### 📖 A. snippet을 만들어보자! - sshPublisher simple step


<img width="861" alt="스크린샷 2023-01-15 오후 9 40 00" src="https://user-images.githubusercontent.com/72541544/212541197-e931657f-99bc-4bc2-a3a3-7deff384c656.png">

<img width="1721" alt="스크린샷 2023-01-15 오후 9 40 11" src="https://user-images.githubusercontent.com/72541544/212541198-4be8273f-e6b6-4816-8a55-88647b45c997.png">

- ssh Server 설정에서 선언한 name을 넣어준다. (원래 자동으로 들어간다.)
  
- `Source files` : 소스파일의 위치, gradlew wrapper에서 빌드 결과물을 `build/libs/`로 위치하므로, `build/libs/*.jar` 로 작성하였다.
  
- `Remove prefix` : 소스파일에서 원본파일의 디렉터리를 어디까지 포함할 것인지에 대한 설정
  
- `remote directory` : 배포될 경로를 적는다. 배포 서버의 해당 폴더로 목적파일이 도착하게 된다. (디렉터리 미리 만들어야 한다.)
  
- `Exec command` : 전송을 마치고 실행할 shell 문의 디렉터리 및 파일 위치이다.

&nbsp;

**✔️ 프로젝트에서 제공하는 배포 쉘**

```bash
echo "> pid 확인"
CURRENT_PID=$(ps -ef | grep java | grep D208 | grep -v nohup | awk '{print $2}')
echo "$CURRENT_PID"
if [ -z ${CURRENT_PID} ] ;then
	echo "> 현재 구동중인 애플리케이션이 없으므로 종료하지 않습니다."
else
	echo "> sudo kill -9 $CURRENT_PID"
	sudo kill -9 $CURRENT_PID
	sleep 10
fi
echo "> jujeol 배포"
JAR_PATH=$(ls -t /home/ubuntu/D208-Project/deploy/*.jar | head -1)
sudo nohup java -jar ${JAR_PATH} >> /home/ubuntu/D208-Project/logs/D208.log &
```

- `CURRENT_PID=$(ps -ef | grep java | grep D208 | grep -v nohup | awk '{print $2}')` : `CURRENT_PID`에 현재 실행되고 있는 프로젝트의 pid를 받아온다.
  
- 쉘을 작성하기 전 직접 서버를 실행하고 해당 명령어를 입력해서 pid를 잘 잡아오는지 확인한다.
  
  - `ps -ef | grep java | grep D208 | grep -v nohup | awk '{print $2}'`

- `CURRENT_PID`가 있다면 먼저 종료한다. (셧다운 시간이 있기 때문에 sleep을 해준다.)
  
- 종료가 끝나면 `deploy` 폴더에서 마지막 jar을, 80port에 dev 프로필로 실행한다.


&nbsp;


> 📝 리눅스 `2>&1` 그리고 `/dev/null`
> 
> 참고 : https://inpa.tistory.com/entry/%EB%A6%AC%EB%88%85%EC%8A%A4-devnull-%EB%A6%AC%EB%8B%A4%EC%9D%B4%EB%A0%89%EC%85%98-%EA%B8%B0%ED%98%B8-%EC%A2%85%EB%A5%98
> 
> (1) `2>&1`
> <img width="1044" alt="스크린샷 2023-01-15 오후 9 53 40" src="https://user-images.githubusercontent.com/72541544/212541916-8db7e632-ccdc-45c7-b084-9a31c8129b78.png"><img width="898" alt="스크린샷 2023-01-15 오후 9 53 56" src="https://user-images.githubusercontent.com/72541544/212541917-bd804105-8415-44c1-a9a8-cc006c22695a.png">
> 
> (2) `/dev/null`
> 
> - `/dev/null`로 결과를 보낸다는 것은, 데이터를 죄다 말끔히 없애버려서 깔끔하게 화면에 표시하지 않는다는 것을 말한다.
>   
>   <img width="1080" alt="스크린샷 2023-01-15 오후 9 56 44" src="https://user-images.githubusercontent.com/72541544/212542006-b31b8c11-d6d1-4ed1-a10b-a317645536f2.png">
> 


&nbsp;
<img width="1689" alt="스크린샷 2023-01-15 오후 9 40 18" src="https://user-images.githubusercontent.com/72541544/212541199-b1829d62-cdd1-4d64-a463-3995d48b4936.png">
- Generate Pipeline Script클릭을 통해 발급 받는다.
  
- snippet을 stage로 만들어보자.
  
- **sshPublisher 같은 경우 verbose 옵션이 있는데, 해당 옵션을 true로 주면 트러플 슈팅시 유용하다.**  빌드의 console output에 해당 내용이 상세히 찍힌다. (true 줄 시, 디벅)

&nbsp;

현재까지의 파이프라인

```bash
pipeline {
    agent any

    stages {
        stage('git clone') {
            steps {
                checkout scmGit(branches: [[name: '*/main']], extensions: [submodule(parentCredentials: true, reference: '', trackingSubmodules: true)], userRemoteConfigs: [[credentialsId: 'repo-and-hook-access-token-credentials', url: 'https://github.com/ToDoStudy/study_jenkins']])
            }
        }
        
        stage('build'){
            steps {
                dir('jenkins') {
                    sh '''
                        echo 'start bootJar'
                        ./gradlew clean bootJar
                    '''
                }
            }
        }
        
        stage('publish on ssh'){
            steps{
                dir('jenkins'){
                    sshPublisher(publishers: [sshPublisherDesc(configName: 'ECGumD208', transfers: [sshTransfer(cleanRemote: false, excludes: '', execCommand: 'sh /home/ubuntu/D208-Project/script/init_server.sh', execTimeout: 120000, flatten: false, makeEmptyDirs: false, noDefaultExcludes: false, patternSeparator: '[, ]+', remoteDirectory: '/D208-Project/deploy', remoteDirectorySDF: false, removePrefix: 'build/libs', sourceFiles: 'build/libs/*.jar')], usePromotionTimestamp: false, useWorkspaceInPromotion: false, verbose: true)])
                }
            }
        }
    }
}


```


&nbsp;


**✔️ 성공적으로 실행되었는지 파일 확인**


<img width="664" alt="스크린샷 2023-01-15 오후 9 38 42" src="https://user-images.githubusercontent.com/72541544/212541193-62e2aa45-8eee-4fdf-98d5-6684c643715a.png">

<img width="1721" alt="스크린샷 2023-01-15 오후 9 39 21" src="https://user-images.githubusercontent.com/72541544/212541195-3bcca784-eff9-4d43-ba07-67bba0c0f89f.png">

<img width="865" alt="스크린샷 2023-01-15 오후 9 40 53" src="https://user-images.githubusercontent.com/72541544/212541200-5b8aad3c-258e-440f-b1a2-0e77094cfe8a.png">

<img width="860" alt="스크린샷 2023-01-15 오후 9 41 07" src="https://user-images.githubusercontent.com/72541544/212541201-3736699b-ae5d-44ab-b8f5-35eae3de67b9.png">

<img width="1684" alt="스크린샷 2023-01-15 오후 9 41 12" src="https://user-images.githubusercontent.com/72541544/212541202-7a96ed4a-6abe-47ee-9680-1e5b68133367.png">

<img width="1703" alt="스크린샷 2023-01-15 오후 9 41 17" src="https://user-images.githubusercontent.com/72541544/212541204-3bb564f6-5c43-43b0-9e9c-b31d0892bea7.png">



- 성공적으로 실행된 결과다. (EC2 콘솔에서 `ps -ef | grep java` 명령어를 통해 구동이 잘되었는지 확인한다.)
  
- 다만, 실행하다 2분뒤 종료된다. 그전까지는 업로드가 계속 진행중이었다.



=> 이제 Jenkins 웹 콘솔에 접속해 build now를 누르면 프로그램을 자동 배포할 수 있는 발판을 마련했다!

&nbsp;

&nbsp;


# 📚 6. 빌드 유발하기

**✔️ github에서 제공하는 webhook**

- 특정한 이벤트가 발생했을 때, 해당 내용을 등록해놓은 api로 보내주는 기능이다.
  
- 이 기능을 활용하여 github에서 특정한 활동이 일어났을 때 job이 유발되도록 구현할 수 있다.

=> 이를 적용해보기 위해 PR이 merge될 때 자동으로 빌드가 일어나도록 설정해보자!

&nbsp;


> **💡 참고**
> `Github hook trigger for GITScm polling` 옵션은 freestyle에서 build trigger로 많이 사용했었다.
> **하지만, pipeline에서는 제대로 동작시키기 어렵고 디테일한 세팅을 할 수 없다는 단점이 있다.**


&nbsp;

**✔️ 백엔드 PR merge에만 Jenkins가 동작하도록 만들어보자!**

어떻게 하면 될까?

=> Pull Request의 라벨을 바탕으로 백엔드와 프론트엔드 PR을 구분하도록 해보자!

=> 백엔드 라벨이 붙어 있는 PR이 머지 되었을 때만 빌드가 일어나도록 해보자!


&nbsp;

**✔️ Generic Webhook Trigger 플러그인 설치**


<img width="672" alt="스크린샷 2023-01-15 오후 11 09 07" src="https://user-images.githubusercontent.com/72541544/212545615-c18d9ca7-2a62-40d3-9675-c92b96d7ebb9.png">



&nbsp;

파이프라인의 상세내용에서 `build triggers`로 이동하면, `generic webhook trigger`가 추가된 것을 볼 수 있다.


라고 했지만, 일단은 시간이 없어

`Github hook trigger for GITScm polling` 을 사용했다.


<img width="731" alt="스크린샷 2023-01-16 오전 12 06 21" src="https://user-images.githubusercontent.com/72541544/212549099-2938b701-1f2f-4669-b6dd-27aca8279ab7.png">




&nbsp;


&nbsp;



-----
참고
- https://velog.io/@sihyung92/%EC%9A%B0%EC%A0%A0%EA%B5%AC2%ED%8E%B8-%EC%A0%A0%ED%82%A8%EC%8A%A4-%ED%8C%8C%EC%9D%B4%ED%94%84%EB%9D%BC%EC%9D%B8%EC%9D%84-%ED%99%9C%EC%9A%A9%ED%95%9C-%EB%B0%B0%ED%8F%AC-%EC%9E%90%EB%8F%99%ED%99%94
