package com.israt.hp.chatime.core.login;

import android.app.Activity;

import com.facebook.AccessToken;
import com.facebook.login.LoginResult;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.FirebaseUser;


public class LoginPresenter implements LoginContract.Presenter, LoginContract.OnLoginListener {
    private LoginContract.View mLoginView;
    private LoginInteractor mLoginInteractor;

    public LoginPresenter(LoginContract.View loginView) {
        this.mLoginView = loginView;
        mLoginInteractor = new LoginInteractor(this);
    }

    @Override
    public void login(Activity activity, String email, String password) {
        mLoginInteractor.performFirebaseLogin(activity, email, password);
    }



    @Override
    public void login(Activity activity, GoogleSignInAccount account) {
        mLoginInteractor.firebaseAuthWithGoogle(activity, account);
    }

    public void login(Activity activity, AccessToken loginResult) {
        mLoginInteractor.firebaseAuthWithFacebook(activity, loginResult);
    }
    @Override
    public void onSuccess(FirebaseUser firebaseUser) {
        mLoginView.onLoginSuccess(firebaseUser);
    }


    @Override
    public void onFailure(String message) {
        mLoginView.onLoginFailure(message);
    }
}
