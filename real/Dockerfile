FROM node:lts-alpine as builder
VOLUME /tmp

WORKDIR /real

COPY package*.json ./
RUN npm install
COPY . .
RUN npm run build


FROM nginx:stable-alpine
RUN rm -rf /etc/nginx/conf.d/default.conf
COPY  ./nginx/chuno.conf /etc/nginx/conf.d/default.conf


RUN rm -rf /usr/share/nginx/html/*
COPY --from=builder /real/dist /usr/share/nginx/html

EXPOSE 3000


CMD ["nginx", "-g", "daemon off;"]
