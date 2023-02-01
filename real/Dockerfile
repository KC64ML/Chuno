# 최신 Node.js LTS 버전
FROM node:16.19.0

# 생성된 Port 번호
EXPOSE 3000

COPY package.json .

ADD . .
RUN npm install

CMD ["npm", "run", "serve"]
