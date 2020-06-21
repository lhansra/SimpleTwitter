package com.codepath.apps.restclienttemplate.models;

import android.text.format.DateUtils;
import android.util.Log;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.parceler.Parcel;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

@Parcel
@Entity (foreignKeys = @ForeignKey(entity=User.class, parentColumns="id", childColumns="userId"))
public class Tweet {
    @ColumnInfo
    @PrimaryKey
    public long id;
    @ColumnInfo
    public String body;
    @ColumnInfo
    public String createdAt;
    @ColumnInfo
    public long userId;
    @Ignore
    public User user;
    @ColumnInfo
    public String mediaUrl;
    @ColumnInfo
    public boolean liked;
    @ColumnInfo
    public boolean retweeted;
    @ColumnInfo
    public String fullText;

    public Tweet() {}

    public static Tweet fromJsonObject(JSONObject jsonObject) throws JSONException {
        Tweet tweet = new Tweet();
        Log.i("Tweet", jsonObject.toString());
        tweet.body = jsonObject.getString("full_text");
        tweet.retweeted = Boolean.parseBoolean(jsonObject.getString("retweeted"));
        tweet.liked = Boolean.parseBoolean(jsonObject.getString("favorited"));
        String timeStamp = jsonObject.getString("created_at");
        tweet.createdAt = getRelativeTimeAgo(timeStamp);
        tweet.user = User.fromJson(jsonObject.getJSONObject("user"));
        tweet.userId = tweet.user.id;
        tweet.id = jsonObject.getLong("id");
        try {
            JSONObject entities = jsonObject.getJSONObject("entities");
            JSONArray mediaArray = entities.getJSONArray("media");
            JSONObject mediaHttps = (JSONObject) mediaArray.get(0);
            tweet.mediaUrl = mediaHttps.getString("media_url_https");
        } catch(JSONException e) {
            Log.i("Tweet", "Media URL does not exist");
        }
        return tweet;
    }

    public static String getRelativeTimeAgo(String rawJsonDate) {
        String twitterFormat = "EEE MMM dd HH:mm:ss ZZZZZ yyyy";
        SimpleDateFormat sf = new SimpleDateFormat(twitterFormat, Locale.ENGLISH);
        sf.setLenient(true);

        String relativeDate = "";
        try {
            long dateMillis = sf.parse(rawJsonDate).getTime();
            relativeDate = DateUtils.getRelativeTimeSpanString(dateMillis,
                    System.currentTimeMillis(), DateUtils.SECOND_IN_MILLIS).toString();
        } catch (ParseException e) {
            e.printStackTrace();
        }

        return relativeDate;
    }

    public static List<Tweet> fromJsonArray(JSONArray jsonArray) throws JSONException {
        List<Tweet> tweets = new ArrayList<>();
        for (int i=0; i < jsonArray.length(); i++){
            tweets.add(fromJsonObject(jsonArray.getJSONObject(i)));
        }
        return tweets;
    }
}
