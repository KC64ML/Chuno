<template>
    <div id="main-container" class="container">
      <div id="join" v-if="!session">
        <div id="img-div">
          <img src="resources/images/openvidu_grey_bg_transp_cropped.png" />
        </div>
        <div id="join-dialog" class="jumbotron vertical-center">
          <h1>Join a video session</h1>
          <div class="form-group">
            <p>
              <label>Participant</label>
              <input
                v-model="myUserName"
                class="form-control"
                type="text"
                required
              />
            </p>
            <p>
              <label>Session</label>
              <input
                v-model="mySessionId"
                class="form-control"
                type="text"
                required
              />
            </p>
            <p class="text-center">
              <button class="btn btn-lg btn-success" @click="joinSession()">
                Join!
              </button>
            </p>
          </div>
        </div>
      </div>
  
      <div id="session" v-if="session">
        <div id="session-header">
          <h1 id="session-title">{{ mySessionId }}</h1>
          <input
            class="btn btn-large btn-danger"
            type="button"
            id="buttonLeaveSession"
            @click="leaveSession"
            value="Leave session"
          />
        </div>
        <div id="main-video" class="col-md-6">
          <user-video :stream-manager="mainStreamManager" />
        </div>
        <div id="video-container" class="col-md-6">
          <user-video
            :stream-manager="publisher"
            @click="updateMainVideoStreamManager(publisher)"
          />
          <user-video
            v-for="sub in subscribers"
            :key="sub.stream.connection.connectionId"
            :stream-manager="sub"
            @click="updateMainVideoStreamManager(sub)"
          />
        </div>
      </div>
      <input type="text" v-model="txtmsg" />
      <button @click="sendTest">sendTest</button>
      <button @click="actionFilter">actionFilter</button>
    </div>
  </template>
  
  <script>
  import { OpenVidu } from "openvidu-browser";
  import axios from "axios";
  import UserVideo from "@/components/openvidutest/UserVideo";
  
  axios.defaults.headers.post["Content-Type"] = "application/json";
  
  const APPLICATION_SERVER_URL = "https://i8d208.p.ssafy.io/api/";
  
  export default {
    name: "OpenViduTestView",
  
    components: {
      UserVideo,
    },
  
    data() {
      return {
        // OpenVidu objects
        OV: undefined,
        session: undefined,
        myVideoStream: null,
        mainStreamManager: undefined,
        publisher: undefined,
        subscribers: [],
  
        txtmsg: "",
  
        // Join form
        mySessionId: "SessionA",
        myUserName: "Participant" + Math.floor(Math.random() * 100),
      };
    },
    created() {
      this.openMediaDevices({
        video: true,
        audio: true,
      }).then((stream) => {
        this.myVideoStream = stream;
      });
    },
  
    methods: {
      joinSession() {
        // --- 1) Get an OpenVidu object ---
        this.OV = new OpenVidu();
        // --- 2) Init a session ---
            this.session = this.OV.initSession();
            console.log("init sesstion");
  
        // --- 3) Specify the actions when events take place in the session ---
  
        // On every new Stream received...
        this.session.on("streamCreated", (res) => {
          const subscriber = this.session.subscribe(res.stream);
          console.log("res : ");
          console.log(res);
          this.subscribers.push(subscriber);
        });
  
        // 메시지 받았을 때
        this.session.on("signal:my-chat", (event) => {
          const fromdata = JSON.parse(event.from.data);
          const fromUser = fromdata.clientData;
          // console.log(event.data); // Message
          // console.log(fromUser); // Connection object of the sender
          // console.log(event.type); // The type of message ("my-chat")
          console.log(`${fromUser} : ${event.data}`);
        });
  
        // On every Stream destroyed...
        this.session.on("streamDestroyed", ({ stream }) => {
          const index = this.subscribers.indexOf(stream.streamManager, 0);
          if (index >= 0) {
            this.subscribers.splice(index, 1);
          }
        });
  
        // On every asynchronous exception...
        this.session.on("exception", ({ exception }) => {
          console.warn(exception);
        });
  
        // --- 4) Connect to the session with a valid user token ---
  
        // Get a token from the OpenVidu deployment
            this.getToken(this.mySessionId + "game").then((token) => {
            console.log(`token : ${token}`);
          // First param is the token. Second param can be retrieved by every user on event
          // 'streamCreated' (property Stream.connection.data), and will be appended to DOM as the user's nickname
            this.session
            .connect(token, { clientData: this.myUserName })
                .then(() => {
                    console.log("session connected");
              // --- 5) Get your own camera stream with the desired properties ---

              // Init a publisher passing undefined as targetElement (we don't want OpenVidu to insert a video
              // element: we will manage it on our own) and with the desired properties
            let publisher = this.OV.initPublisher(undefined, {
                audioSource: undefined, // The source of audio. If undefined default microphone
                videoSource: this.myVideoStream, // The source of video. If undefined default webcam
                publishAudio: true, // Whether you want to start publishing with your audio unmuted or not
                publishVideo: true, // Whether you want to start publishing with your video enabled or not
                resolution: "640x480", // The resolution of your video
                frameRate: 30, // The frame rate of your video
                insertMode: "APPEND", // How the video is inserted in the target element 'video-container'
                mirror: true, // Whether to mirror your local video or not
                // filter: {
                //   type: "GStreamerFilter",
                //   options: { command: "videoflip method=vertical-flip" },
                // },
              });
              // Set the main video in the page to display our webcam and store our Publisher
              this.mainStreamManager = publisher;
              this.publisher = publisher;
              this.publisher.stream.applyFilter("FaceOverlayFilter")
                .then(filter => {
                    filter.execMethod(
                        "setOverlayedImage",
                        {
                            "uri":"https://cdn.pixabay.com/photo/2013/07/12/14/14/derby-148046_960_720.png",
                            "offsetXPercent":"-0.2F",
                            "offsetYPercent":"-0.8F",
                            "widthPercent":"1.3F",
                            "heightPercent":"1.0F"
                        });
                });
  
              // --- 6) Publish your stream ---
  
              this.session.publish(this.publisher);
            })
            .catch((error) => {
              console.log(
                "There was an error connecting to the session:",
                error.code,
                error.message
              );
            });
        });
  
        window.addEventListener("beforeunload", this.leaveSession);
      },
  
      leaveSession() {
        // --- 7) Leave the session by calling 'disconnect' method over the Session object ---
        if (this.session) this.session.disconnect();
  
        // Empty all properties...
        this.session = undefined;
        this.mainStreamManager = undefined;
        this.publisher = undefined;
        this.subscribers = [];
        this.OV = undefined;
  
        // Remove beforeunload listener
        window.removeEventListener("beforeunload", this.leaveSession);
      },
  
      updateMainVideoStreamManager(stream) {
        // if (this.mainStreamManager === stream) return;
        console.log("update main video stream");
        this.mainStreamManager = stream;
      },
  
      /**
       * --------------------------------------------
       * GETTING A TOKEN FROM YOUR APPLICATION SERVER
       * --------------------------------------------
       * The methods below request the creation of a Session and a Token to
       * your application server. This keeps your OpenVidu deployment secure.
       *
       * In this sample code, there is no user control at all. Anybody could
       * access your application server endpoints! In a real production
       * environment, your application server must identify the user to allow
       * access to the endpoints.
       *
       * Visit https://docs.openvidu.io/en/stable/application-server to learn
       * more about the integration of OpenVidu in your application server.
       */
  
      // 매시지 보내기
      sendTest() {
        this.session
          .signal({
            data: this.txtmsg, // Any string (optional)
            to: [], // Array of Connection objects (optional. Broadcast to everyone if empty)
            type: "my-chat", // The type of message (optional)
          })
          .then(() => {
            console.log("Message successfully sent");
          })
          .catch((error) => {
            console.error(error);
          });
      },
  
      actionFilter() {
        // this.publisher.stream.applyFilter("FaceOverlayFilter").then((filter) => {
        //   filter.execMethod("setOverlayedImage", {
        //     uri: "https://cdn.pixabay.com/photo/2013/07/12/14/14/derby-148046_960_720.png",
        //     offsetXPercent: "-0.2F",
        //     offsetYPercent: "-0.8F",
        //     widthPercent: "1.3F",
        //     heightPercent: "1.0F",
        //   });
        // });
        this.publisher.stream
          .applyFilter("GStreamerFilter", {
            command: `timeoverlay valignment=bottom halignment=right font-desc="Sans, 20"`,
          })
          .then(() => {
            console.log("Video rotated!");
          })
          .catch((error) => {
            console.error(error);
          });
      },
  
      async openMediaDevices(constraints) {
        return await navigator.mediaDevices.getUserMedia(constraints);
      },
      async getToken(mySessionId) {
        const sessionId = await this.createSession(mySessionId);
        return await this.createToken(sessionId);
      },
  
      async createSession(sessionId) {
        const response = await axios.post(
          APPLICATION_SERVER_URL + "api/sessions",
          { customSessionId: sessionId },
          {
            headers: { "Content-Type": "application/json" },
          }
        );
        return response.data; // The sessionId
      },
  
      async createToken(sessionId) {
        const response = await axios.post(
          APPLICATION_SERVER_URL + "api/sessions/" + sessionId + "/connections",
          {},
          {
            headers: { "Content-Type": "application/json" },
          }
        );
        return response.data; // The token
      },
    },
  };
  </script>
  
  <style>
  </style>
  