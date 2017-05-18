package com.israt.hp.chatime.ui.adapters;

import android.content.Context;
import android.media.Image;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;
import com.israt.hp.chatime.R;
import com.israt.hp.chatime.models.Chat;
import com.israt.hp.chatime.utils.RoundTransform;

import java.util.List;

import hani.momanii.supernova_emoji_library.Helper.EmojiconTextView;
import hani.momanii.supernova_emoji_library.emoji.Emojicon;


public class ChatRecyclerAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private static final int VIEW_TYPE_ME = 1;
    private static final int VIEW_TYPE_OTHER = 2;

    private List<Chat> mChats;
    private List<Chat> mChats2;
    private Context mcontext;

    public ChatRecyclerAdapter(Context context,List<Chat> chats) {
        this.mcontext=context;
        mChats = chats;

    }

    public ChatRecyclerAdapter() {
    }

    public void add(Chat chat) {
        if(mChats.size()==0) {
            mChats.add(chat);
            this.    notifyItemInserted(mChats.size()-1);
        }
        else {


        if(!mChats.get(mChats.size()-1).message.equals(chat.message))
        {
            mChats.add(chat);
            this.    notifyItemInserted(mChats.size()-1);
        }
            else if(mChats.get(mChats.size()-1).message.equals(chat.message)&&chat.hasfile())

            {   if(!mChats.get(mChats.size()-1).getFile().getUrl_file().equals(chat.getFile().getUrl_file()))

                mChats.add(chat);
                this.    notifyItemInserted(mChats.size()-1);
            }
        }

        //(mChats.size() - 1);
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());

        RecyclerView.ViewHolder viewHolder = null;
        switch (viewType) {
            case VIEW_TYPE_ME:
                View viewChatMine = layoutInflater.inflate(R.layout.item_chat_mine, parent, false);
                viewHolder = new MyChatViewHolder(viewChatMine);
                break;
            case VIEW_TYPE_OTHER:
                View viewChatOther = layoutInflater.inflate(R.layout.item_chat_other, parent, false);
                viewHolder = new OtherChatViewHolder(viewChatOther);
                break;
        }
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if (TextUtils.equals(mChats.get(position).senderUid,
                FirebaseAuth.getInstance().getCurrentUser().getUid())) {
            configureMyChatViewHolder((MyChatViewHolder) holder, position);
        } else {
            configureOtherChatViewHolder((OtherChatViewHolder) holder, position);
        }
    }

    private void configureMyChatViewHolder(MyChatViewHolder myChatViewHolder, int position) {
        Chat chat = mChats.get(position);
       if(chat.getFile()!=null)
        {
            myChatViewHolder.txtChatMessage.setVisibility(View.GONE);
            myChatViewHolder.MsgImage.setVisibility(View.VISIBLE);
            myChatViewHolder.setImageMsg(chat.getFile().getUrl_file());
        }
        else {
           myChatViewHolder.MsgImage.setVisibility(View.GONE);
           myChatViewHolder.txtChatMessage.setVisibility(View.VISIBLE);
           myChatViewHolder.txtChatMessage.setText(chat.message);
       }


        if (chat.getUri() == null) {
            myChatViewHolder.icon.setImageDrawable(ContextCompat.getDrawable(mcontext,
                    R.drawable.cup));
        } else {
            Glide.with(mcontext)
                    .load(chat.getUri())
                    .transform(new RoundTransform(mcontext))
                    .into(myChatViewHolder.icon);

        }

    }

    private void configureOtherChatViewHolder(OtherChatViewHolder otherChatViewHolder, int position) {
        Chat chat = mChats.get(position);

      if(chat.getFile()!=null)
        {
            otherChatViewHolder.txtChatMessage.setVisibility(View.GONE);
            otherChatViewHolder.MsgImage.setVisibility(View.VISIBLE);
            otherChatViewHolder.setImageMsg(chat.getFile().getUrl_file());
        }
      else {
          otherChatViewHolder.MsgImage.setVisibility(View.GONE);
          otherChatViewHolder.txtChatMessage.setVisibility(View.VISIBLE);
          otherChatViewHolder.txtChatMessage.setText(chat.message);
      }

        otherChatViewHolder.txtChatMessage.setText(chat.message);
        if (chat.getUri() == null) {
            otherChatViewHolder.icon.setImageDrawable(ContextCompat.getDrawable(mcontext,
                    R.drawable.cup));
        } else {
            Glide.with(mcontext)
                    .load(chat.getUri())
                    .transform(new RoundTransform(mcontext))
                    .into(otherChatViewHolder.icon);

        }
    }

    @Override
    public int getItemCount() {
        if (mChats != null) {
            return mChats.size();
        }
        return 0;
    }

    @Override
    public int getItemViewType(int position) {
        if (TextUtils.equals(mChats.get(position).senderUid,
                FirebaseAuth.getInstance().getCurrentUser().getUid())) {
            return VIEW_TYPE_ME;
        } else {
            return VIEW_TYPE_OTHER;
        }
    }

    private static class MyChatViewHolder extends RecyclerView.ViewHolder {
        private EmojiconTextView txtChatMessage;
        private ImageView icon;
         ImageView MsgImage;

        public MyChatViewHolder(View itemView) {
            super(itemView);
            txtChatMessage = (EmojiconTextView) itemView.findViewById(R.id.text_view_chat_message);
            icon = (ImageView) itemView.findViewById(R.id.imageself);
            MsgImage = (ImageView) itemView.findViewById(R.id.imageView_msg);

        }
        public void setImageMsg(String url){
            if ( MsgImage== null)return;
            Glide.with(MsgImage.getContext()).load(url)
                    .override(100, 100)
                    .fitCenter()
                    .into(MsgImage);
           // ivChatPhoto.setOnClickListener(this);
        }

    }

    private static class OtherChatViewHolder extends RecyclerView.ViewHolder {
        private EmojiconTextView txtChatMessage;
        private ImageView icon;
        private ImageView MsgImage;
        public OtherChatViewHolder(View itemView) {
            super(itemView);
            txtChatMessage = (EmojiconTextView) itemView.findViewById(R.id.text_view_chat_message);
            icon = (ImageView) itemView.findViewById(R.id.imageself);
            MsgImage = (ImageView) itemView.findViewById(R.id.imageView_msg);
        }
        public void setImageMsg(String url){
            if ( MsgImage== null)return;
            Glide.with(MsgImage.getContext()).load(url)
                    .override(100, 100)
                    .fitCenter()
                    .into(MsgImage);
            // ivChatPhoto.setOnClickListener(this);
        }
    }
}
