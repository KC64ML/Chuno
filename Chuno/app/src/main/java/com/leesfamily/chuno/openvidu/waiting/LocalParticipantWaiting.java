package com.leesfamily.chuno.openvidu.waiting;

import android.content.Context;

import com.leesfamily.chuno.openvidu.waiting.ParticipantWaiting;
import com.leesfamily.chuno.openvidu.waiting.SessionWaiting;

import org.webrtc.AudioSource;
import org.webrtc.Camera2Enumerator;
import org.webrtc.CameraEnumerator;
import org.webrtc.EglBase;
import org.webrtc.IceCandidate;
import org.webrtc.MediaConstraints;
import org.webrtc.PeerConnectionFactory;
import org.webrtc.SessionDescription;
import org.webrtc.SurfaceTextureHelper;
import org.webrtc.SurfaceViewRenderer;
import org.webrtc.VideoCapturer;
import org.webrtc.VideoSource;

import java.util.ArrayList;
import java.util.Collection;

public class LocalParticipantWaiting extends ParticipantWaiting {

    private Context context;
    //    private SurfaceViewRenderer localVideoView;
    private SurfaceTextureHelper surfaceTextureHelper;
    private VideoCapturer videoCapturer;

    private Collection<IceCandidate> localIceCandidates;
    private SessionDescription localSessionDescription;

    public LocalParticipantWaiting(String participantName, String participantLevel,Boolean participantReady, SessionWaiting session, Context context/*, SurfaceViewRenderer localVideoView*/) {
        super(participantName, participantLevel, participantReady, session);
//        this.localVideoView = localVideoView;
        this.context = context;
        this.localIceCandidates = new ArrayList<>();
        session.setLocalParticipant(this);
    }

    // 사진과 비디오 캡쳐 가능, video track에 저장
    public void startCamera() {

        final EglBase.Context eglBaseContext = EglBase.create().getEglBaseContext();
        PeerConnectionFactory peerConnectionFactory = this.session.getPeerConnectionFactory();

        // Create AudioSource
        AudioSource audioSource = peerConnectionFactory.createAudioSource(new MediaConstraints());
        this.audioTrack = peerConnectionFactory.createAudioTrack("101", audioSource);

        surfaceTextureHelper = SurfaceTextureHelper.create("CaptureThread", eglBaseContext);

        // Create VideoCapturer
        VideoCapturer videoCapturer = createCameraCapturer();
        VideoSource videoSource = peerConnectionFactory.createVideoSource(videoCapturer.isScreencast());
        videoCapturer.initialize(surfaceTextureHelper, context, videoSource.getCapturerObserver());
        videoCapturer.startCapture(480, 640, 30);

        // Create VideoTrack
        this.videoTrack = peerConnectionFactory.createVideoTrack("100", videoSource);

        // Display in localView
//        this.videoTrack.addSink(localVideoView);
    }

    // Camera2 Android API 사용
    private VideoCapturer createCameraCapturer() {
        CameraEnumerator enumerator;
        enumerator = new Camera2Enumerator(this.context);
        final String[] deviceNames = enumerator.getDeviceNames();

        // Try to find front facing camera
        for (String deviceName : deviceNames) {
            if (enumerator.isFrontFacing(deviceName)) {
                videoCapturer = enumerator.createCapturer(deviceName, null);
                if (videoCapturer != null) {
                    return videoCapturer;
                }
            }
        }
        // Front facing camera not found, try something else
        for (String deviceName : deviceNames) {
            if (!enumerator.isFrontFacing(deviceName)) {
                videoCapturer = enumerator.createCapturer(deviceName, null);
                if (videoCapturer != null) {
                    return videoCapturer;
                }
            }
        }
        return null;
    }

    public void storeIceCandidate(IceCandidate iceCandidate) {
        localIceCandidates.add(iceCandidate);
    }

    public Collection<IceCandidate> getLocalIceCandidates() {
        return this.localIceCandidates;
    }

    public void storeLocalSessionDescription(SessionDescription sessionDescription) {
        localSessionDescription = sessionDescription;
    }

    public SessionDescription getLocalSessionDescription() {
        return this.localSessionDescription;
    }

    @Override
    public void dispose() {
        super.dispose();
        if (videoTrack != null) {
//            videoTrack.removeSink(localVideoView);
            videoCapturer.dispose();
            videoCapturer = null;
        }
        if (surfaceTextureHelper != null) {
            surfaceTextureHelper.dispose();
            surfaceTextureHelper = null;
        }
    }
}
