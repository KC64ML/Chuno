FROM node:16.19.0 as build-stage
VOLUME /home/ubuntu/volume:/home/ubuntu/volume

WORKDIR /real

COPY package*.json ./
RUN npm install
COPY . .
RUN npm run build


FROM nginx:stable-alpine as production-stage
COPY  ./nginx/chuno.conf /etc/nginx/conf.d/default.conf
COPY --from=build-stage /real/dist /usr/share/nginx/html

EXPOSE 3000

CMD ["nginx", "-g", "daemon off;"]
