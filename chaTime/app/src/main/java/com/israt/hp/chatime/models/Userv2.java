package com.israt.hp.chatime.models;

import com.google.firebase.database.IgnoreExtraProperties;

import io.realm.RealmObject;
import io.realm.annotations.Required;



public class Userv2 extends RealmObject{
    @Required
    private String uid;
    @Required
    private String DisplayName;

    private String Photo;


    public Userv2() {
    }

    public Userv2(String uid, String name, String photo) {
        this.uid = uid;
        this.DisplayName = name;
        this.Photo=photo;

    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getDisplayName() {
        return DisplayName;
    }

    public void setDisplayName(String displayName) {
        DisplayName = displayName;
    }

    public String getPhoto() {
        return Photo;
    }

    public void setPhoto(String photo) {
        Photo = photo;
    }


}
