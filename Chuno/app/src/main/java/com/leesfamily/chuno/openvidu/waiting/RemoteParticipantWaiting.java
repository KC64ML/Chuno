package com.leesfamily.chuno.openvidu.waiting;

import android.view.View;
import android.widget.TextView;

public class RemoteParticipantWaiting extends ParticipantWaiting {

    private View view;
    private TextView participantUserLevel;
    private Boolean participantUserReady;
    private TextView participantUserName;

    public RemoteParticipantWaiting(String connectionId, String participantName, String participantLevel, Boolean participantReady, SessionWaiting session) {
        super(connectionId, participantName, participantLevel, participantReady, session);
        this.session.addRemoteParticipant(this);
    }

    public View getView() {
        return this.view;
    }

    public void setView(View view) {
        this.view = view;
    }

    public TextView getParticipantUserLevel() {
        return this.participantUserLevel;
    }

    public TextView getParticipantUserName() {
        return this.participantUserName;
    }

    public void setParticipantUserLevel(TextView participantUserLevel) {
        this.participantUserLevel = participantUserLevel;
    }

    public void setParticipantUserName(TextView participantUserName) {
        this.participantUserName = participantUserName;
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
