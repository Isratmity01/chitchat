package com.israt.hp.chatime.ui.fragments;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.FileProvider;
import android.text.TextUtils;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.israt.hp.chatime.BuildConfig;
import com.israt.hp.chatime.R;
import com.israt.hp.chatime.controllers.RealmController;
import com.israt.hp.chatime.core.login.LoginContract;
import com.israt.hp.chatime.core.login.LoginPresenter;
import com.israt.hp.chatime.core.users.add.AddUserContract;
import com.israt.hp.chatime.core.users.add.AddUserPresenter;
import com.israt.hp.chatime.models.User;
import com.israt.hp.chatime.models.Userv2;
import com.israt.hp.chatime.ui.activities.UserListingActivity;
import com.israt.hp.chatime.utils.NetworkConnectionUtil;

import java.io.File;
import java.util.ArrayList;
import java.util.Date;

import io.realm.Realm;


public class LoginFragment extends Fragment implements View.OnClickListener, LoginContract.View,GoogleApiClient.OnConnectionFailedListener,AddUserContract.View {
    private LoginPresenter mLoginPresenter;
    private AddUserPresenter mAddUserPresenter;
    private FirebaseAuth mAuth;
    CallbackManager callbackManager;
    LoginButton fbbutton;
    ImageButton custombutton,signinbtn;
    private FirebaseAuth.AuthStateListener mAuthListener;
    private static final String TAG = "LoginFragment";
    private static final int REQUEST_CODE_GOOGLE_SIGN_IN = 1;

    GoogleApiClient mGoogleApiClient;

    private Realm realm;
    RealmController realmController;
    private SignInButton mSignInButton;
    private ArrayList<String> permissions = new ArrayList<>();
    private ProgressDialog mProgressDialog;
LoginResult fblogin;

    public LoginResult getFblogin() {
        return fblogin;
    }

    public void setFblogin(LoginResult fblogin) {
        this.fblogin = fblogin;
    }

    public static LoginFragment newInstance() {
        Bundle args = new Bundle();
        LoginFragment fragment = new LoginFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View fragmentView = inflater.inflate(R.layout.fragment_login, container, false);
        mAuth=FirebaseAuth.getInstance();
        bindViews(fragmentView);
        return fragmentView;
    }

    private void bindViews(View view) {

        mSignInButton=(SignInButton)view.findViewById(R.id.sign_in_button);
         custombutton =(ImageButton)view.findViewById(R.id.chotofb);

        signinbtn =(ImageButton)view.findViewById(R.id.chotogoogle);
        fbbutton=(LoginButton)view.findViewById(R.id.login_facebook_login_button);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        callbackManager = CallbackManager.Factory.create();

        fbbutton.setReadPermissions("email","public_profile");
        fbbutton.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
               // Toast.makeText(LoginFragment.this.getContext(), "Successful", Toast.LENGTH_SHORT).show();
                //SharedPreferences sharedPref = getSharedPreferences(Constants.SHAREDPREFERENCE_USER_PROFILE,MODE_PRIVATE);
              //  SharedPreferences.Editor editor = sharedPref.edit();
               // editor.putString(Constants.USERLOGINTYPE,"facebook");
               // editor.apply();
                onRegisterFacebook(loginResult.getAccessToken());
            }

            @Override
            public void onCancel() {
               // Toast.makeText(LoginFragment.this.getContext(), "error", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onError(FacebookException error) {
              //  Toast.makeText(LoginFragment.this.getContext(), error.getMessage().toString(), Toast.LENGTH_SHORT).show();
            }
        });


        init();  // Configure Google Sign In
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();

        mGoogleApiClient = new GoogleApiClient.Builder(getContext())
                .enableAutoManage(LoginFragment.this.getActivity() /* FragmentActivity */, this /* OnConnectionFailedListener */)
                .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
                .build();
        this.realm = RealmController.with(LoginFragment.this.getActivity()).getRealm();
        realmController=RealmController.with(LoginFragment.this.getActivity());
    }

    private void init() {
        mLoginPresenter = new LoginPresenter(this);
        mAddUserPresenter = new AddUserPresenter(this);
        mProgressDialog = new ProgressDialog(getActivity());
        mProgressDialog.setTitle(getString(R.string.loading));
        mProgressDialog.setMessage(getString(R.string.please_wait));
        mProgressDialog.setIndeterminate(true);
        custombutton.setOnClickListener(this);
        signinbtn.setOnClickListener(this);
        fbbutton.setFragment(this);

        mSignInButton.setOnClickListener(this);


    }



    @Override
    public void onClick(View view) {
        int viewId = view.getId();

        switch (viewId) {

            case R.id.chotogoogle:
      if(NetworkConnectionUtil.isConnectedToInternet(LoginFragment.this.getContext())) onRegisterGoogle();
              else Toast.makeText(LoginFragment.this.getContext(),"Please activate your internet",Toast.LENGTH_SHORT).show();
                break;

            case R.id.chotofb:
        Toast.makeText(LoginFragment.this.getContext(),"Not supported in this version. Please use google",Toast.LENGTH_SHORT).show();
                break;
        }
    }
private void onRegisterFacebook(AccessToken accessToken) {
    mProgressDialog.show();

    Log.d(TAG, "handleFacebookAccessToken:" + accessToken);
    mLoginPresenter.login(LoginFragment.this.getActivity(), accessToken);
}
    private void onLoginGoogle(GoogleSignInAccount account) {
        mLoginPresenter.login(getActivity(), account);
        mProgressDialog.show();
    }



    private void onRegisterGoogle() {
        Intent signInIntent = Auth.GoogleSignInApi.getSignInIntent(mGoogleApiClient);
        startActivityForResult(signInIntent, REQUEST_CODE_GOOGLE_SIGN_IN);
    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        callbackManager.onActivityResult(requestCode, resultCode, data);// required for facebook

       // mTwitterLoginButton.onActivityResult(requestCode, resultCode, data);

        if (requestCode == REQUEST_CODE_GOOGLE_SIGN_IN) {
            GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
            if (result.isSuccess()) {
                // Google Sign In was successful, authenticate with Firebase
                Log.d("google sign in", "successful");
                GoogleSignInAccount account = result.getSignInAccount();
                onLoginGoogle(account);
            } else {
                Log.e("google sign in", "failed");
                // Google Sign In failed, update UI appropriately
                // ...
            }
        }
        else {
            //onRegisterFacebook();
        }
    }
    @Override
    public void onLoginSuccess(FirebaseUser firebaseUser) {
        mProgressDialog.setMessage(getString(R.string.adding_user_to_db));
       // Toast.makeText(getActivity(), "Registration Successful!", Toast.LENGTH_SHORT).show();



        mAddUserPresenter.addUser(getActivity().getApplicationContext(), firebaseUser);


    }


    @Override
    public void onLoginFailure(String message) {
        mProgressDialog.dismiss();
       // Toast.makeText(getActivity(), "Error: " + message, Toast.LENGTH_SHORT).show();
    }


    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }

    @Override
    public void onAddUserSuccess(String message) {
        mProgressDialog.dismiss();
       // Toast.makeText(getActivity(), message, Toast.LENGTH_SHORT).show();
        UserListingActivity.startActivity(getActivity(),
                Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
    }

    @Override
    public void onAddUserFailure(String message) {

    }
}
