# 최신 Node.js LTS 버전
FROM node:16.19.0 as build-stage

WORKDIR /real
ADD . .
RUN npm install
RUN npm run build


FROM nginx:stable-alpine as production-stage
COPY  ./nginx/chuno.conf /etc/nginx/conf.d/default.conf
COPY --from=build-stage /real/dist /usr/share/nginx/html

EXPOSE 3000

CMD ["nginx", "-g", "daemon off;"]
