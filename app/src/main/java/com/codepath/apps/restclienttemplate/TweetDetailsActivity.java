package com.codepath.apps.restclienttemplate;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.media.Image;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.codepath.apps.restclienttemplate.models.Tweet;
import com.codepath.asynchttpclient.callback.JsonHttpResponseHandler;
import com.github.scribejava.apis.TwitterApi;

import org.parceler.Parcels;
import org.w3c.dom.Text;

import okhttp3.Headers;

public class TweetDetailsActivity extends AppCompatActivity {

    TextView tvName;
    TextView tvScreenName;
    TextView tvTweet;
    ImageView profilePic;
    ImageView media;
    Tweet tweet;
    ImageView like;
    ImageView retweet;
    ImageView reply;
    TwitterClient client;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tweet_details);

        tvName = findViewById(R.id.tvName);
        tvScreenName = findViewById(R.id.tvScreenName);
        tvTweet = findViewById(R.id.tvTweet);
        profilePic = findViewById(R.id.ivProfile);
        like = findViewById(R.id.like);
        retweet = findViewById(R.id.retweet);
        reply = findViewById(R.id.reply);
        media = findViewById(R.id.ivMedia);

        client = TwitterApp.getRestClient(this);

        tweet = (Tweet) Parcels.unwrap(getIntent().getParcelableExtra(Tweet.class.getSimpleName()));
        Log.i("TweetDetails", tweet.toString());

        tvName.setText(tweet.user.name);
        tvScreenName.setText(tweet.user.screenName);
        if (tweet.liked){
            Glide.with(this).load(R.drawable.ic_vector_heart).into(like);
        }
        if (tweet.retweeted){
            Glide.with(this).load(R.drawable.ic_vector_retweet).into(retweet);
        }
        like.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                client.updateLikes(tweet.liked, tweet.id, new JsonHttpResponseHandler() {
                    @Override
                    public void onSuccess(int statusCode, Headers headers, JSON json) {
                        Log.i("TweetDetailsActivity", "Successfully liked");
                    }

                    @Override
                    public void onFailure(int statusCode, Headers headers, String response, Throwable throwable) {
                        Log.i("TweetDetailsActivity", "Could not like");
                    }
                });
                tweet.liked = !tweet.liked;
                if (tweet.liked){
                    Glide.with(getApplicationContext()).load(R.drawable.ic_vector_heart).into(like);
                } else {
                    Glide.with(getApplicationContext()).load(R.drawable.ic_vector_heart_stroke).into(like);
                }
            }
        });
        retweet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                client.updateRetweets(tweet.retweeted, tweet.id, new JsonHttpResponseHandler() {
                    @Override
                    public void onSuccess(int statusCode, Headers headers, JSON json) {
                        Log.i("TweetDetailsActivity", "Successfully retweeted");
                    }

                    @Override
                    public void onFailure(int statusCode, Headers headers, String response, Throwable throwable) {
                        Log.i("TweetDetailsActivity", "Could not retweet");
                    }
                });
                tweet.retweeted = !tweet.retweeted;
                if (tweet.retweeted){
                    Glide.with(getApplicationContext()).load(R.drawable.ic_vector_retweet).into(retweet);
                } else {
                    Glide.with(getApplicationContext()).load(R.drawable.ic_vector_retweet_stroke).into(retweet);
                }
            }
        });
        profilePic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.i("TwitterDetailsActivity", "Clicked");
                Intent intent = new Intent(getApplicationContext(), ProfileActivity.class);
                intent.putExtra("user_id", tweet.user.id);
                Log.i("TweetDetails", String.valueOf(tweet.user.id));
                startActivity(intent);
            }
        });
        Log.i("TweetDetails", tweet.body);
        tvTweet.setText(tweet.body);
        Glide.with(this).load(tweet.user.publicImageUrl).circleCrop().into(profilePic);
        if (tweet.mediaUrl != null){
            media.setVisibility(View.VISIBLE);
            Glide.with(this).load(tweet.mediaUrl).apply(new RequestOptions().override(900, 900)).into(media);
        }
    }
}
