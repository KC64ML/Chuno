# 최신 Node.js LTS 버전
FROM node:16.19.0

WORKDIR /real

# WORKDIR은 RUN, CMD, ENTRYPOINT의 명령이 실행될 디렉터리를 설정합니다. < 컨테이너 위치
# WORKDIR 뒤에 오는 모든 RUN, CMD, ENTRYPOINT에 적용되며, 중간에 다른 디렉터리를 설정하여 실행 디렉터리를 바꿀 수 있습니다.
 
# 복사할 파일 경로 : 이미지에서 파일이 위치할 경로
COPY package*.json ./

RUN npm install

COPY . .

RUN npm run build

# nginx config
FROM nginx:stable-alpine as production-stage
RUN rm /etc/nginx/conf.d/default.conf
COPY ./nginx/chuno.conf /etc/nginx/conf.d/chuno.conf

# COPY --from=build-stage ./real/dist /usr/share/nginx/html

EXPOSE 3000
# nginx config

CMD ["nginx", "-g", "daemon off;"]
