const routes = [
    {
        path: "/waitingRoom/:roomId",
        name: "waitingRoom",
        component: () => import("@/views/WaitingRoomView.vue")
    },
    {
        path: "/test/:namee",
        name: "Test",
        component: () => import("@/views/TestView.vue"),
        props: true,
    }
]

export default routes