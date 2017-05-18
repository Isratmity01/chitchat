package com.israt.hp.chatime.core.chat;

import android.content.Context;
import android.net.Uri;

import com.google.firebase.storage.StorageReference;
import com.israt.hp.chatime.models.Chat;

import java.io.File;


public class ChatPresenter implements ChatContract.Presenter, ChatContract.OnSendMessageListener,
        ChatContract.OnGetMessagesListener {
    private ChatContract.View mView;
    private ChatInteractor mChatInteractor;

    public ChatPresenter(ChatContract.View view) {
        this.mView = view;
        mChatInteractor = new ChatInteractor(this, this);
    }

    @Override
    public void sendMessage(Context context, Chat chat, String receiverFirebaseToken,int before) {
        mChatInteractor.sendMessageToFirebaseUser(context, chat, receiverFirebaseToken,before);
    }

@Override
public void onSendMessageSuccessFile()
{
    mView.onSendMessageSuccessFile();
}

@Override
    public void sendFileFirebase(Context context,String rid,String rname,String rtoken, StorageReference storageReference, Uri  uri){
        mChatInteractor.sendFileFirebase(context,rid,rname,rtoken,storageReference,uri);
    }

    @Override
    public void getMessage(String senderUid, String receiverUid) {
        mChatInteractor.getMessageFromFirebaseUser(senderUid, receiverUid);
    }

    @Override
    public void onSendMessageSuccess() {
        mView.onSendMessageSuccess();
    }

    @Override
    public void onSendMessageFailure(String message) {
        mView.onSendMessageFailure(message);
    }

    @Override
    public void onGetMessagesSuccess(Chat chat) {
        mView.onGetMessagesSuccess(chat);
    }
    @Override
    public void onGetNoMsg() {
        mView.onGetNoMsg();
    }

    @Override
    public void onGetMessagesFailure(String message) {
        mView.onGetMessagesFailure(message);
    }
}
