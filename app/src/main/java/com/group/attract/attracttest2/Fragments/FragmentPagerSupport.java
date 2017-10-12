package com.group.attract.attracttest2.Fragments;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.group.attract.attracttest2.R;
import com.group.attract.attracttest2.SuperheroProfile;
import com.squareup.picasso.Picasso;

import java.text.SimpleDateFormat;
import java.util.ArrayList;

/**
 * Created by paul on 11.10.17.
 */

public class FragmentPagerSupport extends FragmentActivity {
    private static int NUM_ITEMS;

    private MyAdapter mAdapter;
    private ViewPager mPager;

    private ArrayList<SuperheroProfile> profiles;


    public int currPosition;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_pager);

        // Recieving a serialized info bundle to fill in profiles page
        Intent heroInfo = getIntent();
        Bundle bundle = heroInfo.getExtras();
        profiles = (ArrayList<SuperheroProfile>) bundle.getSerializable("profile");

        //For the filtered array
        NUM_ITEMS = profiles.size();
        currPosition = bundle.getInt("currentPosition");

        mAdapter = new MyAdapter(getSupportFragmentManager(), profiles);

        //Settin profile from my list click
        mPager = findViewById(R.id.pager);
        mPager.setAdapter(mAdapter);
        mPager.setCurrentItem(currPosition);

    }

    public static class MyAdapter extends FragmentPagerAdapter {
        ArrayList<SuperheroProfile> profiles;

        public MyAdapter(FragmentManager fm, ArrayList<SuperheroProfile> profiles) {
            super(fm);
            this.profiles = profiles;

        }

        @Override
        public int getCount() {
            return NUM_ITEMS;
        }

        @Override
        public Fragment getItem(int position) {

            return SuperheroListFragment.newInstance(position, profiles.get(position));
        }
    }

    public static class SuperheroListFragment extends ListFragment {
        int mNum;
        SuperheroProfile profile;

        /**
         * Create a new instance of CountingFragment, providing "num"
         * as an argument.
         */
        static SuperheroListFragment newInstance(int num, SuperheroProfile superheroProfile) {
            SuperheroListFragment f = new SuperheroListFragment();

            // Supply num input as an argument.
            Bundle args = new Bundle();
            args.putInt("num", num);
            args.putSerializable("profile", superheroProfile);
            f.setArguments(args);

            return f;
        }

        /**
         * When creating, retrieve this instance's number from its arguments.
         */
        @Override
        public void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            mNum = getArguments() != null ? getArguments().getInt("num") : 1;
            profile = (SuperheroProfile) getArguments().getSerializable("profile");
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            View v = inflater.inflate(R.layout.fragment_pager_list, container, false);

            View tvName = v.findViewById(R.id.name);
            ((TextView) tvName).setText(profile.getName());

            View tvDesc = v.findViewById(R.id.desc);
            ((TextView) tvDesc).setText(profile.getDescription());

            //Date conversion
            long timeStamp = Long.parseLong(profile.getTime());
            java.util.Date time=new java.util.Date(timeStamp * 1000);
            SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("dd-MM-yyyy");
            String temp = DATE_FORMAT.format(time);

            View tvDate = v.findViewById(R.id.datetime);
            ((TextView) tvDate).setText(temp);

            View ivAvatar = v.findViewById(R.id.avatar);
            Picasso.with(getContext()).load(profile.getImage()).centerCrop().fit().placeholder(R.mipmap.ic_launcher).into((ImageView) ivAvatar);

            return v;
        }
    }
}
