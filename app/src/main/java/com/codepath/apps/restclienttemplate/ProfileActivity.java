package com.codepath.apps.restclienttemplate;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;

import com.codepath.apps.restclienttemplate.fragments.FollowerFragment;
import com.codepath.apps.restclienttemplate.fragments.FollowingFragment;
import com.codepath.asynchttpclient.callback.JsonHttpResponseHandler;
import com.github.florent37.materialviewpager.MaterialViewPager;
import com.google.android.material.tabs.TabLayout;

import java.util.ArrayList;
import java.util.List;

import okhttp3.Headers;

public class ProfileActivity extends AppCompatActivity {

    TwitterClient client;
    Intent intent;
    Long userId;
    public final String TAG = "ProfileActivity";
    private Toolbar toolbar;
    private ViewPager viewPager;
    private TabLayout tabLayout;
    private FollowerFragment followerFrag;
    private FollowingFragment followingFrag;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        toolbar = findViewById(R.id.toolbar);
        viewPager = findViewById(R.id.viewPager);
        tabLayout = findViewById(R.id.tabLayout);

        intent = getIntent();
        userId = intent.getLongExtra("user_id", 0);


        Bundle bundle = new Bundle();
        bundle.putLong("userId", userId);
        followerFrag = new FollowerFragment();
        followingFrag = new FollowingFragment();
        followerFrag.setArguments(bundle);
        followingFrag.setArguments(bundle);

        tabLayout.setupWithViewPager(viewPager);

        ViewPagerAdapter vadapter = new ViewPagerAdapter(getSupportFragmentManager(), 0);
        vadapter.addFragment(followerFrag, "Followers");
        vadapter.addFragment(followingFrag, "Following");
        viewPager.setAdapter(vadapter);

        client = TwitterApp.getRestClient(this);

        Log.i(TAG, userId.toString());

    }

    private class ViewPagerAdapter extends FragmentPagerAdapter {

        private List<Fragment> fragments = new ArrayList<>();
        private List<String> fragmentTitle = new ArrayList<>();

        public ViewPagerAdapter(@NonNull FragmentManager fm, int behavior) {
            super(fm, behavior);
        }

        public void addFragment(Fragment fragment, String title){
            fragments.add(fragment);
            fragmentTitle.add(title);
        }

        @Nullable
        @Override
        public CharSequence getPageTitle(int position) {
            return fragmentTitle.get(position);
        }

        @NonNull
        @Override
        public Fragment getItem(int position) {
            return fragments.get(position);
        }

        @Override
        public int getCount() {
            return fragments.size();
        }
    }
}
