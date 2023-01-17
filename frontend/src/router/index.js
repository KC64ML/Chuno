import { createRouter, createWebHistory } from 'vue-router'
import HomeView from '../views/HomeView.vue'
import StartView from '@/views/accounts/StartView.vue'
import LoginView from '@/views/accounts/LoginView.vue'
import SearchView from '@/views/SearchView.vue'
import ProfileView from '@/views/ProfileView.vue'
import ShopView from '@/views/ShopView.vue'
import RankView from '@/views/RankView.vue'
import InputInfoView from '@/views/accounts/InputInfoView.vue'

const routes = [
  {
    path: '/',
    name: 'Home',
    component: HomeView
  },
  {
    path: '/start/',
    name: 'Start',
    component: StartView
  },
  {
    path: '/login/',
    name: 'Login',
    component: LoginView
  },
  {
    path: '/search/',
    name: 'Search',
    component:SearchView
  },
  {
    path: '/profile/',
    name: 'Profile',
    component: ProfileView
  },
  {
    path: '/shop/',
    name: 'Shop',
    component: ShopView
  },
  {
    path: '/rank/',
    name: 'Rank',
    component: RankView
  },
  {
    path: '/info/',
    name: 'Info',
    component: InputInfoView
  },
  
]

const router = createRouter({
  history: createWebHistory(process.env.BASE_URL),
  routes
})

export default router
