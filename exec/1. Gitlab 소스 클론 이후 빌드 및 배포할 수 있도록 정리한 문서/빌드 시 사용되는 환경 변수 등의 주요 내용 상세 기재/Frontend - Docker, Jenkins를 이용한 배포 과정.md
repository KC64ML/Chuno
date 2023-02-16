

> **📝 파일 소개**
> frontend 환경에서 배포 과정 설명한 문서입니다.



&nbsp;



# 📚 1. Jenkins 설정

**✔ jenkins에서 node js plugin 설치 및 설정 추가**

![nodejs](https://user-images.githubusercontent.com/72541544/215973384-4eabb3e4-3c06-4ad8-9815-6eaefcb4ff6f.png)

- NodeJS Plugin 설치

&nbsp;


![nodejs tool configuratin](https://user-images.githubusercontent.com/72541544/215973375-06997318-13cd-4ea0-acf3-2e2d91e0bc44.png)

- Global Tool Configuration에서 node16.19을 추가한다.
- 이유 : 현재 EC2 ubuntu에서 node16.19을 설치했기 때문이다.



&nbsp;

**✔ ubuntu에서 node16 설치, npm 설치**

```shell
curl -sL https://deb.nodesource.com/setup_16.x -o /tmp/nodesource_setup.sh
nano /tmp/nodesource_setup.sh 
sudo bash /tmp/nodesource_setup.sh
sudo apt install nodejs
sudo apt install npm
```


&nbsp;


**✔ 현재 pipeline**

```shell
pipeline {
    agent any

    tools {nodejs "node16_19"}

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
    }
}

```

- `tool s {nodejs "node16_19"}` : Global Tool Configuration에서 추가한 node (plugin에서 nodejs 를 설치 후, Global Tool Configuration에 추가해야한다.)
- `git clone` : clone
- `build` : Vue js 배포 파일 만들기


![plugin](https://user-images.githubusercontent.com/72541544/216201217-f8adad37-166b-4e9a-90a6-29558128f5ab.png)
![추가](https://user-images.githubusercontent.com/72541544/216201223-aac88ac9-b760-4317-8377-552c3f3e404a.png)



&nbsp;

**✔ 현재 vue-cli-service를 찾지 못한 상태** 


![vue-cli service not found](https://user-images.githubusercontent.com/72541544/215975320-33fa3bd3-5f4b-4bd9-b819-e6bb39ecd392.png)


이유는? 현재 `vue-cli-service`가 설치되지 않았다.

그래서, 설치하면 된다.

```shell
        stage('build') {
            steps {
                dir('real') {
                    echo "build run"
                    sh "npm i @vue/cli-service"
                    sh "npm run build"
                }
            }
        }
```

&nbsp;

![jenkins vue](https://user-images.githubusercontent.com/72541544/215978434-2a48e88e-14fc-43c7-aea3-b39fdcedf1f5.png)

성공!


&nbsp;

&nbsp;

# 📚 2. 이제 docker hub에 push 하자

### 📖 A. docker build

```script
pipeline {
    agent any

    tools {nodejs "node16_19"}

    environment {
        imagename = "lkc263/d208_fe"
        registryCredential = 'DockerHubD208Fe'
        dockerImage = ''
    }

    stages {
        
 
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
    }
}

```

- `imagename` : docker hub의 repository
- `docker.build` : `docker build -t`로 image를 생성한다.


![현재pipeline](https://user-images.githubusercontent.com/72541544/216203601-cc41d455-4acd-4404-a3ff-1f93a96edcd6.png)
![dockerfile](https://user-images.githubusercontent.com/72541544/216203607-f190ffa4-ea25-441a-a44d-b41ba09bc68f.png)


```shell
# 최신 Node.js LTS 버전
FROM node:16.19.0
WORKDIR /app
# WORKDIR은 RUN, CMD, ENTRYPOINT의 명령이 실행될 디렉터리를 설정합니다. < 컨테이너 위치
# WORKDIR 뒤에 오는 모든 RUN, CMD, ENTRYPOINT에 적용되며, 중간에 다른 디렉터리를 설정하여 실행 디렉터리를 바꿀 수 있습니다.
 
# 복사할 파일 경로 : 이미지에서 파일이 위치할 경로
COPY package.json .
ADD . .
RUN npm install
EXPOSE 3000
CMD ["npm", "run", "serve"]
```


&nbsp;

### 📖 B. docker push

```shell        
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
```

- 만약에 가상서버가 실행 중이라면, 종료하기
- `docker.withRegistry`를 통해 docker hub에 push 한다.


![docker hub](https://user-images.githubusercontent.com/72541544/216202213-5a4ea394-46ef-433e-9866-470b05fbc00c.png)



&nbsp;

&nbsp;


# 📚 3. docker hub에 생성된 image를 pull 받아 실행


```shell
        stage('SSH-Server-EC2'){
            steps {
                echo 'SSH - Server EC2'
                
                sshagent(credentials: ['EC2_D208']) {
                    sh 'ssh -o StrictHostKeyChecking=no ubuntu@3.36.87.75 "docker pull lkc263/d208_fe:1.0"'
                    sh 'ssh -o StrictHostKeyChecking=no ubuntu@3.36.87.75 "docker run -i -d -p 3000:8080 lkc263/d208_fe:1.0"'
                }
            }
        }
```

- ec2에 pull 받고 background로 실행



&nbsp;


![성공](https://user-images.githubusercontent.com/72541544/216203087-9d8f829b-6e6f-4c5c-b430-24ef1138de3d.png)


성공!


&nbsp;

```shell
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

&nbsp;

&nbsp;




----

참고
- https://oingdaddy.tistory.com/103





