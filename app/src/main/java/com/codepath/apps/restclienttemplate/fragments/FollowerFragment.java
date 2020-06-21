package com.codepath.apps.restclienttemplate.fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.codepath.apps.restclienttemplate.R;
import com.codepath.apps.restclienttemplate.TwitterApp;
import com.codepath.apps.restclienttemplate.TwitterClient;
import com.codepath.apps.restclienttemplate.adapters.ProfileAdapter;
import com.codepath.apps.restclienttemplate.models.User;
import com.codepath.asynchttpclient.callback.JsonHttpResponseHandler;

import org.json.JSONException;

import java.util.ArrayList;
import java.util.List;

import okhttp3.Headers;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link FollowerFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class FollowerFragment extends Fragment {

    private Long userId;
    private String mParam2;
    TwitterClient client;
    List<User> profiles;
    View view;

    public FollowerFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment FollowerFragment.
     */
    public static FollowerFragment newInstance(String param1, String param2) {
        FollowerFragment fragment = new FollowerFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            userId = getArguments().getLong("userId");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        profiles = new ArrayList<>();
        view = inflater.inflate(R.layout.fragment_following, container, false);
        client = TwitterApp.getRestClient(getContext());
        client.getFollowers(userId, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Headers headers, JSON json) {
                Log.i("Following Fragment", json.toString());
                try {
                    profiles = User.fromJsonArray(json.jsonObject.getJSONArray("users"));
                    RecyclerView rvFollowing = view.findViewById(R.id.rvFollowing);
                    ProfileAdapter adapter = new ProfileAdapter(profiles, getContext());
                    LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
                    rvFollowing.setLayoutManager(layoutManager);
                    rvFollowing.setAdapter(adapter);
                } catch (JSONException e) {
                    Log.e("Following Fragment", "Could not parse following", e);
                }
            }

            @Override
            public void onFailure(int statusCode, Headers headers, String response, Throwable throwable) {
                Log.e("Following Fragment", "Could not retrieve following", throwable);
            }
        });

        return view;
    }
}
