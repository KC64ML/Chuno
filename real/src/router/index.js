import { createRouter, createWebHistory } from 'vue-router'
import ProfileView from '@/views/ProfileView.vue'
import FriendsView from '@/components/profile/FriendsView.vue'
import ShopView from '@/views/ShopView.vue'
import RankView from '@/views/RankView.vue'
import OauthView from '@/views/OauthView.vue'
import GameView from '@/views/GameView.vue'

import module from '@/router/module.js'



const routes = [
  {
    path: "/",
    name: 'start',
    component: () => import("@/views/StartView.vue")
  },
  {
      path: "/login",
      name: "login",
      component: () => import("@/views/LoginView.vue")
  },
  {
    path: "/search",
    name: "search",
    component: () => import("@/views/SearchView.vue")
  },
  {
    path: "/home",
    name: "home",
    component: () => import("@/views/NewHomeView.vue")
  },
  {
    path: '/profile/:uid',
    name: 'Profile',
    component: ProfileView
  },
  {
    path: '/profile/:uid/friend',
    name: 'friends',
    component: FriendsView
  },
  {
    path: '/shop',
    name: 'Shop',
    component: ShopView
  },
  {
    path: '/rank',
    name: 'Rank',
    component: RankView
  },
  {
    path: '/oauth',
    name: 'Oauth',
    component: OauthView
  },
  {
    path: '/register/:email',
    name: 'Register',
    component: () => import('@/views/RegisterView.vue'),
  },
  {
    path: '/game/:roomId',
    name: 'game',
    component: GameView
  },
  {
    path: '/whatthefuck',
    name: 'Openvidutest',
    component: () => import("@/views/OpenViduTestView.vue")
  },
  {
    path: '/door',
    name: 'Door',
    component: () => import('@/components/game/DoorView.vue')
  },

  ...module

]

const router = createRouter({
  history: createWebHistory(process.env.BASE_URL),
  routes
})

router.beforeEach(async (to, from, next) => {
  // 로그인 없이 갈 수 있는 페이지
  if (to.name == 'start' || to.name == 'login' || to.name == 'Oauth' || to.name == 'Register') {
    return next();
  }
  // 나머지는 다 로그인 체크
  if (sessionStorage.token) {
    const token = sessionStorage.token;
    const isLogin = await this.axios.get(process.env.VUE_APP_SPRING + "user/isLogin", { headers: { Authorization: token } }).then(res => res.data.code);
    if (isLogin) {
      return next()
    }
  }
  return next({ name: 'start' });
})

export default router
