FROM node:16.19.0 as build-stage
VOLUME /tmp

WORKDIR /real

COPY package*.json ./
RUN npm install
COPY . .
RUN npm run build

FROM nginx:stable-alpine as production-stage
COPY  ./nginx/chuno.conf /etc/nginx/conf.d/default.conf

EXPOSE 3000

CMD ["nginx", "-g", "daemon off;"]
