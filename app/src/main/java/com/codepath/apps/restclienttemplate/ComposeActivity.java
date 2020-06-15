package com.codepath.apps.restclienttemplate;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.codepath.apps.restclienttemplate.models.Tweet;
import com.codepath.asynchttpclient.callback.JsonHttpResponseHandler;

import org.json.JSONException;
import org.parceler.Parcel;
import org.parceler.Parcels;

import okhttp3.Headers;

public class ComposeActivity extends AppCompatActivity {

    public static final int MAX_TWEET_LENGTH = 140;

    EditText etCompose;
    Button sendTweet;

    TwitterClient client;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_compose);

        client = TwitterApp.getRestClient(this);

        etCompose = findViewById(R.id.etCompose);
        sendTweet = findViewById(R.id.sendTweetBtn);

        sendTweet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String tweet = etCompose.getText().toString();
                if (tweet.isEmpty()){
                    Toast.makeText(ComposeActivity.this, "Your tweet cannot be empty", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (tweet.length() > MAX_TWEET_LENGTH){
                    Toast.makeText(ComposeActivity.this, "Your tweet is too long", Toast.LENGTH_SHORT).show();
                    return;
                }
                client.postTweet(tweet, new JsonHttpResponseHandler() {
                    @Override
                    public void onSuccess(int statusCode, Headers headers, JSON json) {
                        try {
                            Tweet tweet = Tweet.fromJsonObject(json.jsonObject);
                            Intent intent = new Intent();
                            intent.putExtra("tweet", Parcels.wrap(tweet));
                            setResult(RESULT_OK, intent);
                            finish();
                        } catch (JSONException e) {
                            Log.e("ComposeActivity", "Could not post tweet", e);
                        }
                        Log.i("ComposeActivity", "Successfully posted tweet");
                    }

                    @Override
                    public void onFailure(int statusCode, Headers headers, String response, Throwable throwable) {
                        Log.e("ComposeActivity", "Could not post tweet", throwable);
                    }
                });
            }
        });
    }
}
