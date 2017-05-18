package com.israt.hp.chatime.models;

import android.net.Uri;

import com.google.firebase.database.IgnoreExtraProperties;

import io.realm.RealmObject;


@IgnoreExtraProperties
public class User {
    public String uid;
    public String DisplayName;
    public String Photo;
    public String firebaseToken;

    public User() {
    }

    public User(String uid, String name, String photo, String firebaseToken) {
        this.uid = uid;
        this.DisplayName = name;
        this.Photo=photo;
        this.firebaseToken = firebaseToken;
    }


}
