package com.israt.hp.chatime.core.users.get.all;


import android.app.Activity;

import com.israt.hp.chatime.models.User;

import java.util.List;



public interface GetUsersContract {
    interface View {
        void onGetAllUsersSuccess(List<User> users);

        void onGetAllUsersFailure(String message);

        void onGetChatUsersSuccess(List<User> users);

        void onGetChatUsersFailure(String message);
    }

    interface Presenter {
        void getAllUsers(Activity activity);

        void getChatUsers();
    }

    interface Interactor {
        void getAllUsersFromFirebase(Activity activity);

        void getChatUsersFromFirebase();
    }

    interface OnGetAllUsersListener {
        void onGetAllUsersSuccess(List<User> users);

        void onGetAllUsersFailure(String message);
    }

    interface OnGetChatUsersListener {
        void onGetChatUsersSuccess(List<User> users);

        void onGetChatUsersFailure(String message);
    }
}
