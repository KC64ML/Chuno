import { createApp } from 'vue'
import App from './App.vue'
import router from './router'
import store from './store'
import VueGoogleMaps from "@fawmi/vue-google-maps";
import Vue3Geolocation from 'vue3-geolocation';
import axios from 'axios'

const app = createApp(App)
  .use(VueGoogleMaps, {
    load: {
      key: "AIzaSyDNtaRROIJFfEw-LoB-VotVU6je4YQLE5Y"
    }
  })
  .use(Vue3Geolocation)
  .use(store)
  .use(router)

app.config.globalProperties.axios = axios;
app.config.globalProperties.conn = new WebSocket(process.env.VUE_APP_SOCKET);
app.config.globalProperties.conn.onopen = () => {
  console.log("websocket connected");
}
app.config.globalProperties.conn.onclose = () => {
  console.log("websocket disconnected");
  console.log("reconnection initiated");
  WebSocketConnect();
}
function WebSocketConnect() {
  app.config.globalProperties.conn = new WebSocket(process.env.VUE_APP_SOCKET);
  app.config.globalProperties.conn.onopen = () => {
    console.log("websocket connected");
  }
  app.config.globalProperties.conn.onclose = () => {
    console.log("websocket disconnected")
    console.log("reconnection initiated");
    WebSocketConnect();
  }
}


app.config.globalProperties.sendData  = function(data)
{
  if (app.config.globalProperties.conn.readyState) {
    app.config.globalProperties.conn.send(JSON.stringify(data));
  } else {
    setTimeout(app.config.globalProperties.sendData, 500);
  }
}

app.mount('#app')