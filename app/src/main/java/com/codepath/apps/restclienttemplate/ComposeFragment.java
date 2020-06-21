package com.codepath.apps.restclienttemplate;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import com.codepath.apps.restclienttemplate.models.Tweet;
import com.codepath.asynchttpclient.callback.JsonHttpResponseHandler;

import org.json.JSONException;
import org.parceler.Parcels;
import org.w3c.dom.Text;

import okhttp3.Headers;

import static android.app.Activity.RESULT_OK;

public class ComposeFragment extends DialogFragment {
    private EditText etTweet;
    private Button sendBtn;
    TwitterClient client;
    TextView charCount;
    Button closeBtn;
    TextWatcher counter;
    public static final int MAX_TWEET_LENGTH = 280;

    public ComposeFragment(){ }

    public static ComposeFragment newInstance(String title, String reply) {
        ComposeFragment frag = new ComposeFragment();
        Bundle args = new Bundle();
        args.putString("title", title);
        args.putString("reply", reply);
        frag.setArguments(args);
        return frag;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_compose, container);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        // Get field from view
        etTweet = (EditText) view.findViewById(R.id.etCompose2);
        sendBtn = (Button) view.findViewById(R.id.sendBtn);
        client = TwitterApp.getRestClient(getContext());
        closeBtn = view.findViewById(R.id.cancelBtn);
        charCount = view.findViewById(R.id.tvCharCount);


        // Fetch arguments from bundle and set title
        String title = getArguments().getString("title", "Compose");
        getDialog().setTitle(title);
        etTweet.setText(getArguments().getString("reply"));
        // Show soft keyboard automatically and request focus to field
        etTweet.requestFocus();
        getDialog().getWindow().setSoftInputMode(
                WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE);

        counter = new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                charCount.setText("" + charSequence.length());
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        };

        etTweet.addTextChangedListener(counter);

        closeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getActivity().finish();
            }
        });

        sendBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String tweet = etTweet.getText().toString();
                if (tweet.isEmpty()){
                    Toast.makeText(getContext(), "Your tweet cannot be empty", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (tweet.length() > MAX_TWEET_LENGTH){
                    Toast.makeText(getContext(), "Your tweet is too long", Toast.LENGTH_SHORT).show();
                    return;
                }
                client.postTweet(tweet, new JsonHttpResponseHandler() {
                    @Override
                    public void onSuccess(int statusCode, Headers headers, JSON json) {
                        try {
                            Tweet tweet = Tweet.fromJsonObject(json.jsonObject);
                            Intent intent = new Intent();
                            intent.putExtra("tweet", Parcels.wrap(tweet));
                            getActivity().setResult(RESULT_OK, intent);
                            getActivity().finish();
                        } catch (JSONException e) {
                            Log.e("ComposeFragment", "Could not post tweet", e);
                        }
                        Log.i("ComposeFragment", "Successfully posted tweet");
                    }

                    @Override
                    public void onFailure(int statusCode, Headers headers, String response, Throwable throwable) {
                        Log.e("ComposeFragment", "Could not post tweet", throwable);
                    }
                });
            }
        });
    }

    @Override
    public void onResume() {
        // Get existing layout params for the window
        ViewGroup.LayoutParams params = getDialog().getWindow().getAttributes();
        // Assign window properties to fill the parent
        params.width = WindowManager.LayoutParams.MATCH_PARENT;
        params.height = WindowManager.LayoutParams.MATCH_PARENT;
        getDialog().getWindow().setAttributes((android.view.WindowManager.LayoutParams) params);
        // Call super onResume after sizing
        super.onResume();
    }

}
