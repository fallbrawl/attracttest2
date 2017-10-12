package com.group.attract.attracttest2.Fragments;

import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import com.group.attract.attracttest2.R;
import com.group.attract.attracttest2.RssAdapter;
import com.group.attract.attracttest2.RssItem;
import com.group.attract.attracttest2.SuperheroProfile;
import com.group.attract.attracttest2.Utils.XMLUtils;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import retrofit2.Response;

/**
 * Created by paul on 11.10.17.
 */

public class RssFragment extends Fragment {
    ArrayList<RssItem> rssItems;
    private Handler handler;
    private RssAdapter rssAdapter;
    private ListView listView;
    private XMLUtils parseXML;

    //Request URL for RSS feed
    final static String rssURL = "http://www.techlearning.com/RSS";

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.e("", "frag1");

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.rss_fragment, null);
        listView = view.findViewById(R.id.list_rss);
        rssItems = new ArrayList<>();
        rssAdapter = new RssAdapter(getContext(), rssItems);
        listView.setAdapter(rssAdapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

            }
        });
        new ParseRss().execute(rssURL);
        return view;
    }

    //Task for retrieving array of {@link RssItem}s
    private class ParseRss extends AsyncTask<String, Void, List<RssItem>> {

        @Override
        protected ArrayList<RssItem> doInBackground(String... params) {

            parseXML = new XMLUtils(params[0]);
            parseXML.fetchXML();

            while (parseXML.parsingComplete);

            return parseXML.getRssItems();
        }

        @Override
        protected void onPostExecute(List<RssItem> contains) {
            //Settin the Cardview with rss's
            rssItems.clear();
            rssItems.addAll(contains);
            rssAdapter.notifyDataSetChanged();
            Log.e("staty","async" + String.valueOf(contains.size()));
        }
    }
}
