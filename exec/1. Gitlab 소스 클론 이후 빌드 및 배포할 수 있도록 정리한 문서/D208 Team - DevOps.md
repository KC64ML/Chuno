  

# 📚 1. 프로젝트 구조

  

![%E1%84%8B%E1%85%AE%E1%84%85%E1%85%B5%E1%84%90%E1%85%B5%E1%86%B7_%E1%84%80%E1%85%AE%E1%84%8C%E1%85%A9](https://user-images.githubusercontent.com/72541544/219372479-5f779be4-6f74-4500-bc44-2c50245bd640.jpeg)


- docker, jenkins, nginx

&nbsp;


ec2 정보 조회 : `cat /etc/**release**`

<img width="699" alt="Screenshot_2023-01-31_at_9 07 12_PM" src="https://user-images.githubusercontent.com/72541544/219372030-1adc35c6-d324-4fca-a6b3-b227b91f45d3.png">

&nbsp;

&nbsp;

  

# 📚 2. port 번호, 경로 구조

  

**✔️ port 번호**


```text
`Frontend` : 3000
`backend` : 8000
`Openvidu` : 4443
`kurento-media-server` : 8888
`database` : 3306
`jenkins` : 7777
`mysql` : 3305
```
  

&nbsp;


&nbsp;


# 📚 3. Jenkins pipeline

  

**✔️ `frontend pipeline`**

```bash
pipeline {
    agent any

    tools {nodejs "node16_19"}

    environment {
        imagename = "lkc263/d208_fe"
        registryCredential = 'DockerHubD208Fe'
        dockerImage = ''
    }

    stages {
        stage('git clone') {
            steps {
                checkout scmGit(branches: [[name: '*/FE']], extensions: [submodule(parentCredentials: true, reference: '', trackingSubmodules: true)], userRemoteConfigs: [[credentialsId: 'D208-UserCredentials', url: 'https://lab.ssafy.com/s08-webmobile1-sub2/S08P12D208']])
            }
        }
        
        stage('build') {
            steps {
                dir('real') {
                    echo "build run"
                    sh "npm i @vue/cli-service"
                    sh "npm run build"
                }
            }
        }
        
        stage('docker-build') {
            steps {
                dir('real'){
                    script {
                        
                        sh """
                        if ! test docker; then
                        curl -fsSL https://get.docker.com -o get-docker.sh
                        get-docker.sh
                        fi
                        """
                        
                        dockerImage = docker.build imagename
                    }
                }

            }
        }
        
        
        stage('docker-push') {
            steps {
                
                sshagent(credentials: ['EC2_D208']) {
                    sh '''
                    if test "`docker ps -aq --filter ancestor=lkc263/d208_fe:1.0`"; then
					ssh -o StrictHostKeyChecking=no ubuntu@3.36.87.75 "docker stop $(docker ps -aq --filter ancestor=lkc263/d208_fe:1.0)"
                    ssh -o StrictHostKeyChecking=no ubuntu@3.36.87.75 "docker rm -f $(docker ps -aq --filter ancestor=lkc263/d208_fe:1.0)"
                    ssh -o StrictHostKeyChecking=no ubuntu@3.36.87.75 "docker rmi lkc263/d208_fe:1.0"
                    
                    fi
                    '''
                }
                
                
                script {
                    docker.withRegistry('', registryCredential){
                        dockerImage.push("1.0")
                    }
                }
            }
        }
        
        
        stage('SSH-Server-EC2'){
            steps {
                echo 'SSH - Server EC2'
                
                sshagent(credentials: ['EC2_D208']) {
                    
                    sh 'ssh -o StrictHostKeyChecking=no ubuntu@3.36.87.75 "docker pull lkc263/d208_fe:1.0"'
                    sh 'ssh -o StrictHostKeyChecking=no ubuntu@3.36.87.75 "docker run -i -d -p 3000:3000 --name d208_frontend lkc263/d208_fe:1.0"'
                }
                
                sh "docker system prune -f" // Do not prompt for confirmation
            }
        }
    }
}
```

  

- 입력이 3000번으로 들어왔을 때, 3000번 port를 호출


&nbsp;


  

**✔️ `backend pipeline`**

  

```bash
pipeline {
    agent any

    environment {
        imagename = "lkc263/d208_be"
        registryCredential = 'DockerHubD208Be'
        dockerImage = ''
    }

    stages {
        stage('gitlab clone') {
            steps {
                checkout scmGit(branches: [[name: '*/BE']], extensions: [submodule(parentCredentials: true, reference: '', trackingSubmodules: true)], userRemoteConfigs: [[credentialsId: 'D208-UserCredentials', url: 'https://lab.ssafy.com/s08-webmobile1-sub2/S08P12D208']])
            }
        }
        
        stage('build') {
            
            steps {
                dir('chuno') {
                    sh "chmod +x gradlew"
                    sh "./gradlew clean bootJar"
                }
            }
        }
        
        stage('docker-build') {
            steps {
                echo 'Build Docker'
                dir('chuno') {
                    script {
                        sh "ls -al"
                        sh "pwd"
                        
                        sh """
                        if ! test docker; then
                        curl -fsSL https://get.docker.com -o get-docker.sh
                        get-docker.sh
                        fi
                        """
                        
                        dockerImage = docker.build imagename
                    }
                }
            }
        }
        
        stage('docker push') {
            steps {
                echo 'Push Docker and delete image'
                
                sshagent(credentials: ['EC2_D208']) {
                    
                    sh '''
                    if test "`docker ps -aq --filter ancestor=lkc263/d208_be:1.0`"; then
                    
					ssh -o StrictHostKeyChecking=no ubuntu@3.36.87.75 "docker stop $(docker ps -aq --filter ancestor=lkc263/d208_be:1.0)"
                    ssh -o StrictHostKeyChecking=no ubuntu@3.36.87.75 "docker rm -f $(docker ps -aq --filter ancestor=lkc263/d208_be:1.0)"
                    ssh -o StrictHostKeyChecking=no ubuntu@3.36.87.75 "docker rmi lkc263/d208_be:1.0"
                    
                    fi
                    '''
                }
                
                
                script {
                    docker.withRegistry('', registryCredential){
                        dockerImage.push("1.0")
                    }
                }
            }
        }
        
        stage('SSH-Server-EC2'){
            steps {
                echo 'SSH - Server EC2'
                
                sshagent(credentials: ['EC2_D208']) {
                    sh 'ssh -o StrictHostKeyChecking=no ubuntu@3.36.87.75 "docker pull lkc263/d208_be:1.0"'
                    sh 'ssh -o StrictHostKeyChecking=no ubuntu@3.36.87.75 "docker run -i -p 8000:8080 -d lkc263/d208_be:1.0"'
                }
            }
        }
        
    }
}

```


&nbsp;


&nbsp;
  

# 📚 4. docker

  

### 📖 A. frontend

  

**✔️ 이전 frontend/dockerfile**

  

```bash

FROM node:16.19.0

VOLUME /home/ubuntu/volume:/home/ubuntu/volume

WORKDIR /real

# WORKDIR은 RUN, CMD, ENTRYPOINT의 명령이 실행될 디렉터리를 설정합니다. < 컨테이너 위치

# WORKDIR 뒤에 오는 모든 RUN, CMD, ENTRYPOINT에 적용되며, 중간에 다른 디렉터리를 설정하여 실행 디렉터리를 바꿀 수 있습니다.

# 복사할 파일 경로 : 이미지에서 파일이 위치할 경로

  

# 감자 테스트

COPY package.json .

ADD . .

RUN npm install

EXPOSE 3000

CMD ["npm", "run", "serve"]

```


&nbsp;


**✔️ 현재 frontend/dockerfile**


```bash

FROM node:lts-alpine as builder

VOLUME /tmp

  

WORKDIR /real

  

COPY package*.json ./

RUN npm install

COPY . .

RUN npm run build

  

FROM nginx:stable-alpine

RUN rm -rf /etc/nginx/conf.d/default.conf

COPY ./nginx/chuno.conf /etc/nginx/conf.d/default.conf

  

RUN rm -rf /usr/share/nginx/html/*

COPY --from=builder /real/dist /usr/share/nginx/html

  

EXPOSE 3000

  

CMD ["nginx", "-g", "daemon off;"]

```

&nbsp;
  

**✔️ 현재 chuno.conf**

  

```bash
server {
    listen 3000;

    location / {
		root /usr/share/nginx/html;
		index index.html index.htm;
		try_files $uri $uri/ /index.html;
	}
}


```


&nbsp;

&nbsp;
  

### 📖 B. backend


**✔️ dockerfile**


```bash

# open jdk java11 버전 환경

FROM openjdk:11-jdk

  

VOLUME /tmp

  

# 생성된 이미지에서 열어줄

EXPOSE 8000

  

# JAR_FILE 변수 정의

ARG JAR_FILE=./build/libs/chuno-0.0.1-SNAPSHOT.jar

  

# JAR 파일 메인 디렉터리에 복사

COPY ${JAR_FILE} app.jar

  

# 시스템 진입점 정의

ENTRYPOINT ["java", "-jar", "/app.jar"]

```


&nbsp;

&nbsp;


# 📚 5. 결과

  

**✔️ http**


<img width="442" alt="%E1%84%89%E1%85%B3%E1%84%8F%E1%85%B3%E1%84%85%E1%85%B5%E1%86%AB%E1%84%89%E1%85%A3%E1%86%BA_2023-02-04_%E1%84%8B%E1%85%A9%E1%84%8C%E1%85%A5%E1%86%AB_10 28 01" src="https://user-images.githubusercontent.com/72541544/219372018-ebe7254b-d649-4138-9c60-48ec42985cc4.png">


<img width="663" alt="%E1%84%89%E1%85%B3%E1%84%8F%E1%85%B3%E1%84%85%E1%85%B5%E1%86%AB%E1%84%89%E1%85%A3%E1%86%BA_2023-02-04_%E1%84%8B%E1%85%A9%E1%84%8C%E1%85%A5%E1%86%AB_10 27 57" src="https://user-images.githubusercontent.com/72541544/219372017-47b31165-2e43-454c-aa25-ea4a92b5ec0a.png">



&nbsp;
  

**✔️ https**

  <img width="567" alt="%E1%84%89%E1%85%B3%E1%84%8F%E1%85%B3%E1%84%85%E1%85%B5%E1%86%AB%E1%84%89%E1%85%A3%E1%86%BA_2023-02-04_%E1%84%8B%E1%85%A9%E1%84%8C%E1%85%A5%E1%86%AB_10 28 25" src="https://user-images.githubusercontent.com/72541544/219372024-975a4d50-29bd-47c9-bc83-ae26e2a7e32d.png">


<img width="477" alt="%E1%84%89%E1%85%B3%E1%84%8F%E1%85%B3%E1%84%85%E1%85%B5%E1%86%AB%E1%84%89%E1%85%A3%E1%86%BA_2023-02-04_%E1%84%8B%E1%85%A9%E1%84%8C%E1%85%A5%E1%86%AB_10 28 12" src="https://user-images.githubusercontent.com/72541544/219372020-4e7fc879-2f00-46af-943e-5a7b6476ef72.png">



&nbsp;

**✔️ 이외 nginx → `proxy.setting` 파일**

`/etc/nginx/sites-available`


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

  

`frontend` : `80 → 3000 → 3000`

`backend` : `80/api → 8000`


&nbsp;
  

**✔ volume 설정**

  

docker run 할 때 volume 설정 추가 (`-v backend-volume:/var/backend-volume`)

  

![backend-volume](https://user-images.githubusercontent.com/72541544/219372028-c724d9df-9d87-4fec-b603-6c1389b7bbdb.png)



이와 같이 할 경우, **docker backend image container**에 보면,

`/var/backend-volume` 에 저장된다. (test 파일 저장)

또한 ec2에

`/var/lib/docker/volumes/backend-volume/_data` 에 test 파일이 저장된다.


![test_%ED%8F%B4%EB%8D%94](https://user-images.githubusercontent.com/72541544/219372033-276c3246-50fe-4ff5-b4d2-c52f99d14a52.png)



&nbsp;
  

**✔ docker images에서 nane 관련 파일 모두 삭제**

  

`docker image prune`


&nbsp;


**✔ nginx의 default.conf를 삭제한다.**

  

`RUN rm /etc/nginx/conf.d/default.conf`


&nbsp;
  

**✔ docker image frontend로 접속하기**

  

`sudo docker exec -it 8111d52684b5 /bin/sh`


&nbsp;

**✔ Docker 이미지, 컨테이너 세부 정보 출력하기**

  

```bash

$ docker image inspect <image>

$ docker container inspect <container>

```


&nbsp;


&nbsp;
