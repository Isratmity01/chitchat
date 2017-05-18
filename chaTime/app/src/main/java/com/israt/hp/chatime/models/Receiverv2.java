package com.israt.hp.chatime.models;

import io.realm.RealmObject;
import io.realm.annotations.Required;

/**
 * Created by HP on 5/13/2017.
 */

public class Receiverv2 extends RealmObject{
    @Required
    private String receivername;
    @Required
    private String receiverid;
    @Required
    private String receivertoken;

    public String getReceivername() {
        return receivername;
    }

    public Receiverv2() {
    }

    public Receiverv2(String receivername, String receiverid, String receivertoken) {
        this.receivername = receivername;
        this.receiverid = receiverid;
        this.receivertoken = receivertoken;
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

    public String getReceivertoken() {
        return receivertoken;
    }

    public void setReceivertoken(String receivertoken) {
        this.receivertoken = receivertoken;
    }
}
