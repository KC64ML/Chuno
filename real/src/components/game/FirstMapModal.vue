<template>
    <div id="first_map_modal">
        <GMapMap :center="me" :zoom="18" :options="{
            zoomControl: true,
            mapTypeControl: false,
            streetViewControl: false,
            fullscreenControl: false,
            minZoom: 11,
            maxZoom: 18,
        }" style="width: 100vw; height: 25rem">
            <GMapCircle :radius="radius" :center="player" :options="gameCircle" />
        </GMapMap>
    </div>
</template>

<script>
export default {
    props: {
        location_list: {},
    },
    data() {
        return {
            info: {},
            teamchuno: [],
            teamslave: [],
            gameCircle: {
                strokeColor: "#0000FF",
                strokeOpacity: 0.3,
                strokeWeight: 2,
                fillColor: "#0000FF",
                fillOpacity: 0.15,
            },
        }
    },
    methods: {
    },
    created() {
        this.info = sessionStorage.getItem("info");
        console.log(this.info)
        // console.log({roomlat: this.info.roomlat, roomlng: this.info.roomlng});
        for (var i of this.location_list) {
            if (i.role == "chuno") this.teamchuno.push(i);
            else if (i.role == "slave") this.teamslave.push(i);
        }
    }
}
</script>

<style lang="scss" scoped>
#first_map_modal {
    position: absolute;
    z-index: 10000;
    width: 100vw;
    height: 100%;
    background-image: url("@/assets/main_back.png");
    background-size: cover;
}
</style>