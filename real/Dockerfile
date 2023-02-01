# 최신 Node.js LTS 버전
FROM node:16.19.0

WORKDIR /app

# WORKDIR은 RUN, CMD, ENTRYPOINT의 명령이 실행될 디렉터리를 설정합니다. < 컨테이너 위치
# WORKDIR 뒤에 오는 모든 RUN, CMD, ENTRYPOINT에 적용되며, 중간에 다른 디렉터리를 설정하여 실행 디렉터리를 바꿀 수 있습니다.
 
# 복사할 파일 경로 : 이미지에서 파일이 위치할 경로
COPY . .
RUN npm install
RUN npm run build
 
# 가장 기본적인 커맨드이다. 어떤 이미지를 기반으로 새로운 이미지를 생성할 것인지를 나타낸다
FROM nginx:stable-alpine as production-stage
 
# RUN 커맨드는 정말 간단하게 생각해서, bash 쉘에서 입력하는것과 동일하다고 생각하면 된다.
RUN rm /etc/nginx/conf.d/default.conf
COPY ./homepage.conf /etc/nginx/conf.d/homepage.conf
COPY --from=build-stage ./homepage/dist /usr/share/nginx/html/homepage
# COPY --from=builder를 통해 전 단계 스테이지 빌드에서 생성된 특정 결과물만 새로운 BASE 이미지로 복사해서 이미지를 생성했다.
# '이 도커 이미지는 3000번 포트를 외부에 공개할 예정이다'라고 명시할
#EXPOSE 구문으로 명시한 포트는 'docker run -P' 명령을 이용할 때 호스트 운영체제로 오픈
EXPOSE 3000
CMD ["nginx", "-g", "daemon off;"]
