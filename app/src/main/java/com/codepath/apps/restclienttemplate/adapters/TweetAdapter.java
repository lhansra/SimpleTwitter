package com.codepath.apps.restclienttemplate.adapters;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.codepath.apps.restclienttemplate.R;
import com.codepath.apps.restclienttemplate.TweetDetailsActivity;
import com.codepath.apps.restclienttemplate.models.Tweet;

import org.parceler.Parcels;

import java.util.List;

public class TweetAdapter extends RecyclerView.Adapter<TweetAdapter.ViewHolder> {

    List<Tweet> tweets;
    Context context;
    private final onClickListener clickListener;

    public interface onClickListener{
        void onReply(int position);
        void onLike(int position);
        void onRetweet(int position);
    }

    public TweetAdapter(List<Tweet> tweets, Context context, onClickListener clickListener) {
        this.tweets = tweets;
        this.context = context;
        this.clickListener = clickListener;
    }

    public void clear() {
        tweets.clear();
        notifyDataSetChanged();
    }

    public void addAll(List<Tweet> list) {
        tweets.addAll(list);
        notifyDataSetChanged();
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

    class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        ImageView profilePic;
        TextView displayName;
        TextView tweetBody;
        TextView timeStamp;
        TextView name;
        ImageView reply;
        ImageView like;
        ImageView retweet;
        ImageView media;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            profilePic = itemView.findViewById(R.id.imageView);
            displayName = itemView.findViewById(R.id.tvScreenName);
            tweetBody = itemView.findViewById(R.id.tvBody);
            timeStamp = itemView.findViewById(R.id.tvTimeStamp);
            name = itemView.findViewById(R.id.tvName);
            reply = itemView.findViewById(R.id.reply);
            like = itemView.findViewById(R.id.like);
            retweet = itemView.findViewById(R.id.retweet);
            media=itemView.findViewById(R.id.ivMedia);
            itemView.setOnClickListener(this);
        }

        public void bind(Tweet tweet){
            displayName.setText(tweet.user.screenName);
            name.setText(tweet.user.name);
            tweetBody.setText(tweet.body);
            timeStamp.setText(tweet.createdAt);
            String profileImageUrl = tweet.user.publicImageUrl;
            Glide.with(context)
                    .load(profileImageUrl)
                    .circleCrop()
                    .into(profilePic);
            if (tweet.liked){
                Glide.with(context).load(R.drawable.ic_vector_heart).into(like);
            }
            if (tweet.retweeted){
                Glide.with(context).load(R.drawable.ic_vector_retweet).into(retweet);
            }
            if(tweet.mediaUrl != null){
                media.setVisibility(View.VISIBLE);
                Glide.with(context).load(tweet.mediaUrl).apply(new RequestOptions().override(700, 700)).into(media);
            } else {
                media.setVisibility(View.INVISIBLE);
            }
            this.reply.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    clickListener.onReply(getAdapterPosition());
                }
            });
            this.like.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    clickListener.onLike(getAdapterPosition());
                }
            });
            this.retweet.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    clickListener.onRetweet(getAdapterPosition());
                }
            });
        }
        @Override
        public void onClick(View v) {
            int position = getAdapterPosition();
            if (position != RecyclerView.NO_POSITION){
                Log.i("TAG", "reached");
                Tweet tweet = tweets.get(position);
                Intent intent = new Intent(context, TweetDetailsActivity.class);
                intent.putExtra(Tweet.class.getSimpleName(), Parcels.wrap(tweet));
                context.startActivity(intent);
            }
        }
    }
}
