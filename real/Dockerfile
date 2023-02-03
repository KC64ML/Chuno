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
