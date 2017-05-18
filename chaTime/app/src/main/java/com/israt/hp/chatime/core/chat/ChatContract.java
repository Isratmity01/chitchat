package com.israt.hp.chatime.core.chat;

import android.content.Context;
import android.net.Uri;

import com.google.firebase.storage.StorageReference;
import com.israt.hp.chatime.models.Chat;

import java.io.File;


public interface ChatContract {
    interface View {
        void onSendMessageSuccess();
        void onSendMessageSuccessFile();
        void onSendMessageFailure(String message);

        void onGetMessagesSuccess(Chat chat);
        void onGetNoMsg();


        void onGetMessagesFailure(String message);
    }

    interface Presenter {
        void sendMessage(Context context, Chat chat, String receiverFirebaseToken,int before);
      void   sendFileFirebase(Context context,String rid,String rname,String rtoken, StorageReference storageReference, Uri  uri);
        void getMessage(String senderUid, String receiverUid);
    }

    interface Interactor {
        void sendMessageToFirebaseUser(Context context, Chat chat, String receiverFirebaseToken,int before);

        void getMessageFromFirebaseUser(String senderUid, String receiverUid);
    }

    interface OnSendMessageListener {
        void onSendMessageSuccess();
        void onSendMessageSuccessFile();
        void onSendMessageFailure(String message);
    }

    interface OnGetMessagesListener {
        void onGetMessagesSuccess(Chat chat);
        void onGetNoMsg();
        void onGetMessagesFailure(String message);
    }
}
