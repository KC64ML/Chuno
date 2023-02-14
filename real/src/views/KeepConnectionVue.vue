<template>
    <div>

    </div>
</template>

<script>
export default {
    created() {
        this.enrollEvent();
    },
    methods: {
        enrollEvent() {
            new Promise((resolve) => {
                this.conn.addEventListener('message', (e) => {
                    var content = JSON.parse(e.data);
                    if (content.type == "keepconnect") {
                        console.log(content.info)
                    }
                })
                resolve();
            }).then(() => {
                setInterval(() => {
                    console.log("소켓연결을 유지해요");
                    this.sendData({
                        "event": "keepconnect"
                    })
                }, 30000)
            })
        },

    }
}
</script>

<style lang="scss" scoped>

</style>