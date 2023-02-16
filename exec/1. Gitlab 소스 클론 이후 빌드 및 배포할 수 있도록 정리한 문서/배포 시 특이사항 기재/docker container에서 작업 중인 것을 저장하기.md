

시작하기 전에, 먼저 docker container에 접속해야 한다. (`docker exec -it images이름 /bin/bash`)

&nbsp;

**✔ 해야할 것**

(1) docker container 안으로 들어가기 : `docker exec -it images이름 /bin/bash`

(2) docker container 현 상태를 images로 만들기 : `docker commit -m "ubuntu jenkins save" -a "lkc263@naver.com" 41163cf987a0 d208_jenkins_test:1.0`

(3) docker login 하기 : `docker login`

(4) docker tag 넣기 : `docker tag 46ae lkc263/docker_jenkins_test:1.0`

(5) docker push 하기 : `docker push lkc263/docker_jenkins_test:1.0`



&nbsp;

### 📖 A. 현재 container에서 작업한 것을 image로 만들기


`docker commit -m "ubuntu jenkins save" -a "lkc263@naver.com" 41163cf987a0 d208_jenkins_test:1.0`


![1저장](https://user-images.githubusercontent.com/72541544/216548183-12ee5315-372c-4110-a0e1-0101bfae2b39.png)


&nbsp;


### 📖 B. docker tag 넣기

image의 ID를 확인한 후, tag 넣기

 `docker tag 46ae lkc263/docker_jenkins_test:1.0`


![2저장](https://user-images.githubusercontent.com/72541544/216548185-d4ce86d6-f0c6-48b3-835a-0352a4c2e0f6.png)



&nbsp;


### 📖 C. docker push 하기

`docker push lkc263/docker_jenkins_test:1.0`


![3 저장](https://user-images.githubusercontent.com/72541544/216548187-9d99f310-c11e-4f14-9c59-4e3972f70d6e.png)


&nbsp;



### 📖 D. 성공

![4저장](https://user-images.githubusercontent.com/72541544/216548641-c3e15fa0-4170-4d0b-b455-e2a24b1e0475.png)


&nbsp;

&nbsp;
