<template>
<div v-if="streamManager" :class="className">
    <div class="camera_name">
        {{ clientData }}
    </div>
	<ov-video :stream-manager="streamManager"/>
	<!-- <div><p>{{ clientData }}</p></div> -->
</div>
</template>

<script>
import OvVideo from '@/components/game/OvVideo.vue';

    export default {
        name: 'UserVideo',

        components: {
            OvVideo,
        },

        props: {
            streamManager: Object,
            className: String,
        },

        computed: {
            clientData () {
                console.log("*************")
                const { clientData } = this.getConnectionData();
                return clientData;
            },
        },

        methods: {
            getConnectionData () {
                const { connection } = this.streamManager.stream;
                return JSON.parse(connection.data);
            },
        },

        created() {
            console.log("+++++++++++++", this.streamManager)
        }
    }
</script>

<style lang="scss" scoped>
.my_video {
    width: 100%;
    border: solid blue;
}
.enemy_video{
    width: 100%;
    position: absolute;
    top: 50%;
    transform: translateY(-50%);
}
</style>