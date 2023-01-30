import { createRouter, createWebHistory } from 'vue-router'

const routes = [
    {
        path: "/",
        name: 'home',
        component: () => import("@/views/HomeView.vue")
    },
    {
        path: "/login",
        name: "login",
        component: () => import("@/views/LoginView.vue")
    },
    {
        path: "/oauth",
        name: "oauth",
        component: () => import("@/views/OauthView.vue")
    },
    {
        path: "/roomSearch",
        name: "roomSearch",
        component: () => import("@/views/RoomSearchView.vue")
    },
    {
        path: "/roomList",
        name: "roomList",
        component: () => import("@/views/RoomListView.vue")
    }
]

const router = createRouter({
  history: createWebHistory(),
  routes
})

export default router
