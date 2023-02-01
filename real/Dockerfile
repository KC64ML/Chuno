# 최신 Node.js LTS 버전
FROM node:16.19.0

# 생성된 Port 번호
EXPOSE 3000

# Node.js 앱을 위한 app 폴더 생성
RUN mkdir -p /app

# 어플리케이션 폴더를 WORKDIR명령어로 지정 - 서버 가동용
WORKDIR /app

# 가능한 package.json 과 package-lock.json을 모두 복사하기 위해서 와일드 카드 사용
COPY package*.json /app/

# 의존성 설치
RUN npm install

# 앱 소스코드 추가
COPY app.js /app/

# 인자값을 지정하지 않을시 node app.js를 실행
CMD [ "node", "app.js" ]
