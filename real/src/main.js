import { createApp } from 'vue'
import App from './App.vue'
import router from './router'
import store from './store'
import VueGoogleMaps from "@fawmi/vue-google-maps";

createApp(App)
  .use(VueGoogleMaps, {
    load: {
      key: "AIzaSyDNtaRROIJFfEw-LoB-VotVU6je4YQLE5Y"
    }
  })
  .use(store)
  .use(router)
  .mount('#app')