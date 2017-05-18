package com.israt.hp.chatime.ui.fragments;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.FileProvider;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.israt.hp.chatime.BuildConfig;
import com.israt.hp.chatime.R;
import com.israt.hp.chatime.controllers.RealmController;
import com.israt.hp.chatime.core.chat.ChatContract;
import com.israt.hp.chatime.core.chat.ChatPresenter;
import com.israt.hp.chatime.events.PushNotificationEvent;
import com.israt.hp.chatime.models.Chat;
import com.israt.hp.chatime.models.Receiver;
import com.israt.hp.chatime.models.Receiverv2;
import com.israt.hp.chatime.ui.adapters.ChatRecyclerAdapter;
import com.israt.hp.chatime.utils.Constants;
import com.israt.hp.chatime.utils.NetworkConnectionUtil;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.io.File;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import hani.momanii.supernova_emoji_library.Actions.EmojIconActions;
import hani.momanii.supernova_emoji_library.Helper.EmojiconEditText;
import io.realm.Realm;
import io.realm.RealmResults;

import static android.app.Activity.RESULT_CANCELED;
import static android.app.Activity.RESULT_OK;
import static com.facebook.FacebookSdk.getApplicationContext;


public class ChatFragment extends Fragment implements ChatContract.View,View.OnClickListener {
    private RecyclerView mRecyclerViewChat;
    private EditText mETxtMessage;
    private LinearLayoutManager mLinearLayoutManager;
    private ProgressDialog mProgressDialog;

    private ChatRecyclerAdapter mChatRecyclerAdapter;
    private EmojiconEditText edMessage;
    private View contentRoot;
    private EmojIconActions emojIcon;
    private ImageView btEmoji, btSendMessage, imagesendbutton;
    private ChatPresenter mChatPresenter;
    private File filePathImageCamera;
    Uri photoURI;
    int Chatbefore=0;

    public int getChatbefore() {
        return Chatbefore;
    }

    public void setChatbefore(int chatbefore) {
        Chatbefore = chatbefore;
    }

    Chat chat;
    RealmController realmController;
    private Realm realm;
    FirebaseStorage storage;
    private static final int IMAGE_GALLERY_REQUEST = 1;
    private static final int IMAGE_CAMERA_REQUEST = 222;
    // Storage Permissions
    private static final int REQUEST_EXTERNAL_STORAGE = 1;
    private static String[] PERMISSIONS_STORAGE = {
            android.Manifest.permission.READ_EXTERNAL_STORAGE,
            android.Manifest.permission.WRITE_EXTERNAL_STORAGE
    };

    public static ChatFragment newInstance(String receiver,
                                           String receiverUid,
                                           String firebaseToken) {
        Bundle args = new Bundle();
        args.putString(Constants.ARG_RECEIVER, receiver);
        args.putString(Constants.ARG_RECEIVER_UID, receiverUid);
        args.putString(Constants.ARG_FIREBASE_TOKEN, firebaseToken);
        ChatFragment fragment = new ChatFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onStart() {
        super.onStart();
        EventBus.getDefault().register(this);
    }

    @Override
    public void onStop() {
        super.onStop();
        EventBus.getDefault().unregister(this);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View fragmentView = inflater.inflate(R.layout.content_chat_room, container, false);
        bindViews(fragmentView);

        this.realm = RealmController.with(ChatFragment.this.getActivity()).getRealm();
        realmController=RealmController.with(ChatFragment.this.getActivity());
        return fragmentView;
    }

    private void bindViews(View view) {
        mRecyclerViewChat = (RecyclerView) view.findViewById(R.id.recycler_view_chat);
        edMessage = (EmojiconEditText) view.findViewById(R.id.editTextMessage);
        btEmoji = (ImageView) view.findViewById(R.id.buttonEmoji);
        btSendMessage = (ImageView) view.findViewById(R.id.buttonMessage);
        imagesendbutton = (ImageView) view.findViewById(R.id.buttonImage);
        contentRoot = view.findViewById(R.id.root);
        emojIcon = new EmojIconActions(ChatFragment.this.getContext(), contentRoot, edMessage, btEmoji);
        emojIcon.ShowEmojIcon();
        mLinearLayoutManager = new LinearLayoutManager(ChatFragment.this.getContext());
        mLinearLayoutManager.setStackFromEnd(true);


    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        storage = FirebaseStorage.getInstance();
        init();
    }

    private void init() {
        mProgressDialog = new ProgressDialog(getActivity());
        mProgressDialog.setTitle(getString(R.string.loading));
        mProgressDialog.setMessage(getString(R.string.please_wait));
        mProgressDialog.setIndeterminate(true);
        btSendMessage.setOnClickListener(this);
        imagesendbutton.setOnClickListener(this);

        realmController.clearAll();

        realm.beginTransaction();
       Receiverv2 receiverv=realm.createObject(Receiverv2.class);
        receiverv.setReceivername(getArguments().getString(Constants.ARG_RECEIVER));
        receiverv.setReceiverid(getArguments().getString(Constants.ARG_RECEIVER_UID));
        receiverv.setReceivertoken(getArguments().getString(Constants.ARG_FIREBASE_TOKEN));
        realm.commitTransaction();
        mChatPresenter = new ChatPresenter(this);
        mChatPresenter.getMessage(FirebaseAuth.getInstance().getCurrentUser().getUid(),
                getArguments().getString(Constants.ARG_RECEIVER_UID));
        mProgressDialog.show();
    }


    private void sendMessage() {
        String message = edMessage.getText().toString();
        String receiver = getArguments().getString(Constants.ARG_RECEIVER);
        String receiverUid = getArguments().getString(Constants.ARG_RECEIVER_UID);

        String sender = FirebaseAuth.getInstance().getCurrentUser().getDisplayName();
        String senderUid = FirebaseAuth.getInstance().getCurrentUser().getUid();
        String uri = FirebaseAuth.getInstance().getCurrentUser().getPhotoUrl().toString();
        String receiverFirebaseToken = getArguments().getString(Constants.ARG_FIREBASE_TOKEN);
        chat = new Chat(sender,
                receiver,
                senderUid,
                receiverUid,
                message, uri,
                System.currentTimeMillis());
        edMessage.setText(null);


       if(getChatbefore()==1)
       {
           mChatRecyclerAdapter.add(chat);
           mRecyclerViewChat.smoothScrollToPosition(mChatRecyclerAdapter.getItemCount() - 1);
       }



        mChatPresenter.sendMessage(getActivity().getApplicationContext(),
                chat,
                receiverFirebaseToken,getChatbefore());

       // if(NetworkConnectionUtil.isConnectedToInternet(getActivity().getApplicationContext())==true)

     //   else Toast.makeText(getActivity().getApplicationContext(),"Please activate your internet",Toast.LENGTH_SHORT).show();


    }

    @Override
    public void onSendMessageSuccess() {

      //  Toast.makeText(getActivity(), "Message sent", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onSendMessageFailure(String message) {
     //   Toast.makeText(getActivity(), message, Toast.LENGTH_SHORT).show();
    }
    @Override
    public void onSendMessageSuccessFile() {
        mProgressDialog.dismiss();
       // Toast.makeText(getActivity(), "done", Toast.LENGTH_SHORT).show();
    }
    @Override
    public void onGetNoMsg() {
        mProgressDialog.dismiss();

       Toast.makeText(getActivity(), "Start Chatting!", Toast.LENGTH_SHORT).show();
    }
    @Override
    public void onGetMessagesSuccess(Chat chat) {
            setChatbefore(1);
        //impp
        if (mChatRecyclerAdapter == null) {
            mChatRecyclerAdapter = new ChatRecyclerAdapter(ChatFragment.this.getContext(),new ArrayList<Chat>());
            mRecyclerViewChat.setAdapter(mChatRecyclerAdapter);
        }
            // mRecyclerViewChat.scrollToPosition(mChatRecyclerAdapter.getItemCount()-1);
        /*    mChatRecyclerAdapter.registerAdapterDataObserver(new RecyclerView.AdapterDataObserver() {
                @Override
                public void onItemRangeInserted(int positionStart, int itemCount) {
                    super.onItemRangeInserted(positionStart, itemCount);
                    int friendlyMessageCount = mChatRecyclerAdapter.getItemCount();
                    int lastVisiblePosition = mLinearLayoutManager.findLastCompletelyVisibleItemPosition();
                    // If the recycler view is initially being loaded or the
                    // user is at the bottom of the list, scroll to the bottom
                    // of the list to show the newly added message.
                    if (lastVisiblePosition == -1 ||
                            (positionStart >= (friendlyMessageCount - 1) &&
                                    lastVisiblePosition == (positionStart - 1))) {
                        mRecyclerViewChat.scrollToPosition(positionStart);
                    }
                }
            });
        }
*/
       mRecyclerViewChat.setLayoutManager(mLinearLayoutManager);
       mChatRecyclerAdapter.add(chat);

       mRecyclerViewChat.smoothScrollToPosition(mChatRecyclerAdapter.getItemCount() - 1);
        mProgressDialog.dismiss();



    }

    @Override
    public void onGetMessagesFailure(String message) {
       // Toast.makeText(getActivity(), message, Toast.LENGTH_SHORT).show();
    }

    @Subscribe
    public void onPushNotificationEvent(PushNotificationEvent pushNotificationEvent) {
        if (mChatRecyclerAdapter == null || mChatRecyclerAdapter.getItemCount() == 0) {
            mChatPresenter.getMessage(FirebaseAuth.getInstance().getCurrentUser().getUid(),
                    pushNotificationEvent.getUid());
        }
    }

    @Override
    public void onClick(View v) {
        int viewId = v.getId();

        switch (viewId) {
            case R.id.buttonMessage:
                if (!edMessage.getText().toString().isEmpty()) {
                    sendMessage();
                    break;
                } else Toast.makeText(getContext(), "Enter something", Toast.LENGTH_LONG).show();
                break;
            case R.id.buttonImage:
                verifyStoragePermissions();
                break;

        }
    }
    private void photoGalleryIntent(){
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, getString(R.string.select_picture_title)), IMAGE_GALLERY_REQUEST);
    }
    public void verifyStoragePermissions() {
        // Check if we have write permission
        int permission = ActivityCompat.checkSelfPermission(ChatFragment.this.getContext(), android.Manifest.permission.WRITE_EXTERNAL_STORAGE);

        if (permission != PackageManager.PERMISSION_GRANTED) {
            // We don't have permission so prompt the user
            ActivityCompat.requestPermissions(
                    ChatFragment.this.getActivity(),
                    PERMISSIONS_STORAGE,
                    REQUEST_EXTERNAL_STORAGE
            );
        } else {
            // we already have permission, lets go ahead and call camera intent
            photoGalleryIntent();
        }
    }



    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {

        StorageReference storageRef = storage.getReferenceFromUrl(Constants.URL_STORAGE_REFERENCE).child(Constants.FOLDER_STORAGE_IMG);
        if (requestCode == IMAGE_GALLERY_REQUEST){
            if (resultCode == RESULT_OK){
                Uri selectedImageUri = data.getData();
                if (selectedImageUri != null){
                    RealmResults<Receiverv2> results = realm.where(Receiverv2.class).findAll();
                    String rid=results.get(0).getReceiverid();
                    String rname=results.get(0).getReceivername();
                    String rtoken=results.get(0).getReceivertoken();

                        mChatPresenter.sendFileFirebase(ChatFragment.this.getContext(),rid,rname,rtoken,storageRef,selectedImageUri);
                        mProgressDialog.show();


                }else{
                    //URI IS NULL
                }
            }
        } else {

        }
       /* if (requestCode == IMAGE_CAMERA_REQUEST){
            if (resultCode == RESULT_OK){
                Uri selectedImageUri = data.getData();
                if (selectedImageUri != null){
                    mChatPresenter.sendFileFirebase(chat,storageRef,selectedImageUri);
                    mProgressDialog.show();
                }else{
                    //URI IS NULL
                }
            }
        }*/

    }

}
