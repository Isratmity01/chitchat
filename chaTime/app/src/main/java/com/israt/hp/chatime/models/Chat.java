package com.israt.hp.chatime.models;


import com.google.firebase.database.IgnoreExtraProperties;

import java.io.File;


@IgnoreExtraProperties
public class Chat {
    public String sender;
    public String receiver;
    public String senderUid;
    public String receiverUid;
    public String message;
    private String uri;
    public long timestamp;
 private FileModel file;
    public Chat() {
    }

    public Chat(String sender, String receiver, String senderUid, String receiverUid, String message, String uri, long timestamp) {
        this.sender = sender;
        this.receiver = receiver;
        this.senderUid = senderUid;
        this.receiverUid = receiverUid;
        this.message = message;
        this.uri = uri;
        this.timestamp = timestamp;
    }
    public Chat(String sender, String receiver, String senderUid, String receiverUid, String message, String uri, long timestamp,FileModel file) {
        this.sender = sender;
        this.receiver = receiver;
        this.senderUid = senderUid;
        this.receiverUid = receiverUid;
        this.message = message;
        this.uri = uri;
        this.timestamp = timestamp;
        this.file=file;
    }
    public String getUri() {
        return uri;
    }

    public FileModel getFile() {
        return file;
    }

    public void setFile(FileModel file) {
        this.file = file;
    }

    public void setUri(String uri) {
        this.uri = uri;
    }
    public boolean hasfile()
    {
        try {
            String gotvalue= file.toString();
        }catch (Exception e)
        {
            return false;
        }
        return true;

    }
}
