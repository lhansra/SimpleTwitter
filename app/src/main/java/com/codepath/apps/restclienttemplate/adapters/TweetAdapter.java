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
import com.codepath.apps.restclienttemplate.models.Tweet;

import java.util.List;

public class TweetAdapter extends RecyclerView.Adapter<TweetAdapter.ViewHolder> {

    List<Tweet> tweets;
    Context context;

    public TweetAdapter(List<Tweet> tweets, Context context) {
        this.tweets = tweets;
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View tweetView = LayoutInflater.from(context).inflate(R.layout.item_tweet, parent, false);
        return new ViewHolder(tweetView);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.bind(tweets.get(position));
    }

    @Override
    public int getItemCount() {
        return tweets.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder{

        ImageView profilePic;
        TextView displayName;
        TextView tweetBody;
        TextView timeStamp;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            profilePic = itemView.findViewById(R.id.imageView);
            displayName = itemView.findViewById(R.id.tvScreenName);
            tweetBody = itemView.findViewById(R.id.tvBody);
            timeStamp = itemView.findViewById(R.id.tvTimeStamp);
        }

        public void bind(Tweet tweet){
            displayName.setText(tweet.user.screenName);
            tweetBody.setText(tweet.body);
            timeStamp.setText(tweet.createdAt);
            String profileImageUrl = tweet.user.publicImageUrl;
            Glide.with(context).load(profileImageUrl).into(profilePic);
        }
    }
}
