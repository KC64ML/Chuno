FROM node:16.19.0 as build-stage
VOLUME /tmp

WORKDIR /real

COPY package*.json ./
RUN npm install
COPY . .
RUN npm run build



