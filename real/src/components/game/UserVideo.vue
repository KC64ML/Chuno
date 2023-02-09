<template>
<div v-if="streamManager">
    <div class="camera_name">
        {{ clientData.user.nickname }} {{ roleKor }}
    </div>
	<ov-video 
    :stream-manager="streamManager"
    :class-name="className"
    />
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
            roleKor() {
                if (this.clientData.user.role == "runner") {
                    return "노비";
                } else {
                    return "추노꾼";
                }
            }
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

</style>