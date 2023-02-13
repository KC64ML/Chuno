const routes = [
    {
        path: "/waitingRoom/:roomId",
        name: "waitingRoom",
        component: () => import("@/views/NewWaitingRoomView.vue")
    },
    {
        path: "/test/:namee",
        name: "Test",
        component: () => import("@/views/TestView.vue"),
        props: true,
    },
    {
        path: "/gameResult",
        name: "gameResult",
        component: () => import("@/views/GameResultView.vue")
    }
]

export default routes