package com.israt.hp.chatime.controllers;

import android.app.Activity;
import android.app.Application;
import android.app.Fragment;

import com.israt.hp.chatime.models.Receiver;
import com.israt.hp.chatime.models.Receiverv2;
import com.israt.hp.chatime.models.User;
import com.israt.hp.chatime.models.Userv2;

import io.realm.Realm;
import io.realm.RealmResults;

/**
 * Created by HP on 5/13/2017.
 */

public class RealmController {

    private static RealmController instance;
    private final Realm realm;

    public RealmController(Application application) {
        realm = Realm.getDefaultInstance();
    }

    public static RealmController with(Fragment fragment) {

        if (instance == null) {
            instance = new RealmController(fragment.getActivity().getApplication());
        }
        return instance;
    }

    public static RealmController with(Activity activity) {

        if (instance == null) {
            instance = new RealmController(activity.getApplication());
        }
        return instance;
    }


    public static RealmController with(Application application) {

        if (instance == null) {
            instance = new RealmController(application);
        }
        return instance;
    }

    public static RealmController getInstance() {

        return instance;
    }

    public Realm getRealm() {

        return realm;
    }

    //Refresh the realm istance
    public void refresh() {

        realm.refresh();
    }

    //clear all objects from Book.class
    public void clearAll() {

        realm.beginTransaction();
        realm.clear(Receiverv2.class);//class name
        realm.commitTransaction();
    }
    public void clearAlluser() {

        realm.beginTransaction();
        realm.clear(Userv2.class);//class name
        realm.commitTransaction();
    }
    //find all objects in the Book.class
    public RealmResults<Receiverv2> getAll() {

        return realm.where(Receiverv2.class).findAll();
    }

   /*query a single item with the given id
    public TaskStatusv2 getOneStatus(String Taskid) {

        return realm.where(TaskStatusv2.class).equalTo("TaskId", Taskid).findFirst();
    }

    //check if Book.class is empty
    public boolean hasStatus() {

        return !realm.allObjects(TaskStatusv2.class).isEmpty();
    }
  public void Updaterow(String id) {
        TaskStatusv2 taskStatus = realm.where(TaskStatusv2.class).equalTo("TaskId", id).findFirst();
        realm.beginTransaction();
        taskStatus.setState(false);
        realm.commitTransaction();



    }
    public boolean checkIfExists(String id){

        RealmResults<TaskStatusv2> query = realm.where(TaskStatusv2.class)
                .equalTo("TaskId", id).findAll();

        return query.size() !=0 ;
    }*/
    //query example*/
    public RealmResults<Userv2> queryedPhoto(String uid) {

        return realm.where(Userv2.class)
                .contains("uid", uid)
                .findAll();

    }

}