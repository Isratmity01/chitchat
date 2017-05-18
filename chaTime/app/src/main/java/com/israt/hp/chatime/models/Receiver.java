package com.israt.hp.chatime.models;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;
import io.realm.annotations.Required;

/**
 * Created by HP on 5/13/2017.
 */

public class Receiver extends RealmObject{
    @Required
    private String receivername;
    @Required
    private String receiverid;


    public Receiver(String receivername, String receiverid) {
        this.receivername = receivername;
        this.receiverid = receiverid;
    }

    public Receiver() {

    }

    public String getReceivername() {
        return receivername;
    }

    public void setReceivername(String receivername) {
        this.receivername = receivername;
    }

    public String getReceiverid() {
        return receiverid;
    }

    public void setReceiverid(String receiverid) {
        this.receiverid = receiverid;
    }
}
