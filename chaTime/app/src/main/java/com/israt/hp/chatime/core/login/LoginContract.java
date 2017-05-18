package com.israt.hp.chatime.core.login;

import android.app.Activity;

import com.facebook.AccessToken;
import com.facebook.login.LoginResult;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.FirebaseUser;



public interface LoginContract {
    interface View {
        void onLoginSuccess(FirebaseUser firebaseUser);

        void onLoginFailure(String message);
    }

    interface Presenter {
        void login(Activity activity, String email, String password);
        void login(Activity activity, GoogleSignInAccount acct);
    }

    interface Interactor {
        void performFirebaseLogin(Activity activity, String email, String password);
        void firebaseAuthWithGoogle(Activity activity, GoogleSignInAccount acct);
        void firebaseAuthWithFacebook( final Activity activity, final AccessToken acct);
    }


    interface OnLoginListener {
        void onSuccess(FirebaseUser firebaseUser);

        void onFailure(String message);
    }
}
