const routes = [
    {
        path: "/waitingRoom/:roomId",
        name: "waitingRoom",
        component: () => import("@/views/WaitingRoomView.vue")
    }
]

export default routes