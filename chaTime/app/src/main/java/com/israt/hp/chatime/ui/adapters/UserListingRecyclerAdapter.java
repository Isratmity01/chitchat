package com.israt.hp.chatime.ui.adapters;

import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;


import com.bumptech.glide.Glide;
import com.israt.hp.chatime.R;
import com.israt.hp.chatime.controllers.RealmController;
import com.israt.hp.chatime.models.User;
import com.israt.hp.chatime.models.Userv2;
import com.israt.hp.chatime.ui.fragments.ChatFragment;
import com.israt.hp.chatime.ui.fragments.UsersFragment;
import com.israt.hp.chatime.utils.RoundTransform;

import java.util.ArrayList;
import java.util.List;

import io.realm.Realm;
import io.realm.RealmResults;


public class UserListingRecyclerAdapter extends RecyclerView.Adapter<UserListingRecyclerAdapter.ViewHolder> {
    private List<User> mUsers;
    private Realm realm;
    RealmController realmController;
    ArrayList<Userv2>userv2ArrayList=new ArrayList<>();
    Fragment fragment;
    public UserListingRecyclerAdapter(UsersFragment fragment, List<User> users) {
        this.fragment=fragment;
        this.mUsers = users;
    }

    public void add(User user) {
        mUsers.add(user);
        notifyItemInserted(mUsers.size() - 1);
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.new_chat_users, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        User user = mUsers.get(position);

        this.realm =
                RealmController.with(fragment.getActivity()).getRealm();
        RealmResults<Userv2>realmResults=realm.where(Userv2.class).findAll();
      for(int i=0;i<realmResults.size();i++)
      {
          if(realmResults.get(i).getUid().equals(user.uid))
          {
              if (realmResults.get(i).getPhoto() == null) {
                  holder.image.setImageDrawable(ContextCompat.getDrawable(fragment.getContext(),
                          R.drawable.cup));
              } else {
                  Glide.with(holder.itemView.getContext())
                          .load(realmResults.get(i).getPhoto())
                          .transform(new RoundTransform(fragment.getContext()))
                          .into(holder.image);

              }
          }
          else continue;

      }


        holder.txtUsername.setText(user.DisplayName);


    }

    @Override
    public int getItemCount() {
        if (mUsers != null) {
            return mUsers.size();
        }
        return 0;
    }

    public User getUser(int position) {
        return mUsers.get(position);
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        private TextView  txtUsername;

        private ImageView image;
        ViewHolder(View itemView) {
            super(itemView);
            image = (ImageView) itemView.findViewById(R.id.text_view_user_alphabet);
            txtUsername = (TextView) itemView.findViewById(R.id.person_name);

        }
    }
}
