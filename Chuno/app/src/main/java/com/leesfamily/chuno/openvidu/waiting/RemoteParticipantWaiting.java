package com.leesfamily.chuno.openvidu.waiting;

import android.view.View;
import android.widget.TextView;

import com.leesfamily.chuno.network.data.Player;

public class RemoteParticipantWaiting extends ParticipantWaiting {

    private Player player;
    private TextView participantUserLevel;
    private TextView participantUserName;
    private Boolean participantUserReady;

    public RemoteParticipantWaiting(String connectionId, String participantName, String participantLevel, Boolean participantReady, SessionWaiting session) {
        super(connectionId, participantName, participantLevel, participantReady, session);
        this.session.addRemoteParticipant(this);
    }

    public Player getPlayer() {
        return this.player;
    }

    public void setPlayer() {
        this.player = new Player(participantName,participantLevel,participantReady);
    }

    public TextView getParticipantUserName() {
        return this.participantUserName;
    }

    public void setParticipantUserName(TextView participantUserName) {
        this.participantUserName = participantUserName;
    }


    public void setParticipantUserLevel(TextView participantUserLevel) {
        this.participantUserLevel = participantUserLevel;
    }

    public TextView getParticipantUserLevel() {
        return this.participantUserLevel;
    }


    public Boolean getParticipantUserReady() {
        return this.participantUserReady;
    }

    public void participantUserReady(Boolean participantUserReady) {
        this.participantUserReady = participantUserReady;
    }

    @Override
    public void dispose() {
        super.dispose();
    }
}
