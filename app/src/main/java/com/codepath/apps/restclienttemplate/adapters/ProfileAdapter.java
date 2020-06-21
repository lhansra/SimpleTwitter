package com.codepath.apps.restclienttemplate.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.codepath.apps.restclienttemplate.R;
import com.codepath.apps.restclienttemplate.models.User;

import java.util.List;

public class ProfileAdapter extends RecyclerView.Adapter<ProfileAdapter.ViewHolder> {

        List<User> profiles;
        Context context;

    public ProfileAdapter(List<User> profiles, Context context) {
        this.profiles = profiles;
        this.context = context;
    }

    @NonNull
        @Override
        public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View profileView = LayoutInflater.from(context).inflate(R.layout.item_profile, parent, false);
            return new ViewHolder(profileView);
        }

        @Override
        public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
            holder.bind(profiles.get(position));
        }

        @Override
        public int getItemCount() {
            return profiles.size();
        }

        class ViewHolder extends RecyclerView.ViewHolder{

            ImageView profilePic;
            TextView displayName;
            TextView name;

            public ViewHolder(@NonNull View itemView) {
                super(itemView);
                profilePic = itemView.findViewById(R.id.imageView);
                displayName = itemView.findViewById(R.id.tvScreenName);
                name = itemView.findViewById(R.id.tvName);
            }

            public void bind(User profile){
                displayName.setText(profile.screenName);
                name.setText(profile.name);
                String profileImageUrl = profile.publicImageUrl;
                Glide.with(context).load(profileImageUrl).circleCrop().into(profilePic);
            }
        }
}
