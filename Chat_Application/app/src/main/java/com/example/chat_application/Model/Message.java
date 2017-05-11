package com.example.chat_application.Model;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

/**
 * Created by BANDINI on 02-05-2017.
 */

public class Message extends RealmObject {

    @PrimaryKey
    private int ID;
    private int To;
    private int From;
    private String Message;
    private String SendOn;
    private String LastReceivedOn;
    private String Signature;
    public static final String UPDATE_MESSAGES = "UPDATE_MESSAGES";

    public int getID() {
        return ID;
    }

    public void setID(int ID) {
        this.ID = ID;
    }

    public String getMessage() {
        return Message;
    }

    public void setMessage(String message) {
        Message = message;
    }

    public String getSendOn() {
        return SendOn;
    }

    public void setSendOn(String sendOn) {
        SendOn = sendOn;
    }

    public String getLastReceivedOn() {
        return LastReceivedOn;
    }

    public void setLastReceivedOn(String lastReceivedOn) {
        LastReceivedOn = lastReceivedOn;
    }

    public int getFrom() {
        return From;
    }

    public void setFrom(int from) {
        From = from;
    }

    public int getTo() {
        return To;
    }

    public void setTo(int to) {
        To = to;
    }

    public String getUserSignature() {
        return Signature;
    }

    public void setUserSignature(String userSignature) {
        Signature = userSignature;
    }
}
