package com.group.attract.attracttest2.Fragments;

import android.annotation.TargetApi;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TextInputEditText;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AlertDialog;
import android.text.InputType;
import android.util.Log;
import android.util.Patterns;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.group.attract.attracttest2.Activities.MainActivity;
import com.group.attract.attracttest2.R;
import com.group.attract.attracttest2.RssAdapter;
import com.group.attract.attracttest2.RssItem;
import com.group.attract.attracttest2.SuperheroProfile;
import com.group.attract.attracttest2.Utils.XMLUtils;

import org.w3c.dom.Text;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import retrofit2.Response;

/**
 * Created by paul on 11.10.17.
 */

public class RssListFragment extends Fragment {
    ArrayList<RssItem> rssItems;
    private RssAdapter rssAdapter;
    private ListView listView;
    private FloatingActionButton myFabPager;
    private RssItemFragment fragment;
    private FragmentManager fragmentManager;
    private Menu menu;
    private static int itemId = 5;

    //Request URL for RSS feed
    private static String customURL;
    private XMLUtils parseXML;


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.e("", "frag1");

    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        menu = ((MainActivity)this.getActivity()).getNav().getMenu();

    }

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN_MR1)
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.rss_fragment, null);
        listView = view.findViewById(R.id.list_rss);
        myFabPager = (FloatingActionButton) view.findViewById(R.id.fabAddRss);
        rssItems = new ArrayList<>();
        rssAdapter = new RssAdapter(getContext(), rssItems);
        listView.setAdapter(rssAdapter);
        fragmentManager = getFragmentManager();
        fragment = new RssItemFragment();

        final AlertDialog.Builder alertDialog = new AlertDialog.Builder(getContext());
        alertDialog.setTitle("Add RSS");
        alertDialog.setMessage("Enter URL:");
        final EditText addRssEdit;
        final TextInputEditText textInputEditText;

        //как изменить свойство xml типа app:
        //например добавить ошибку к текстинпутлэйаут

        textInputEditText = new TextInputEditText(getContext());
        textInputEditText.setMaxLines(1);
        textInputEditText.setInputType(InputType.TYPE_CLASS_TEXT);

        // TextInputLayout
        TextInputLayout textInputLayout = new TextInputLayout(getContext());
        textInputLayout.setId(View.generateViewId());
        RelativeLayout.LayoutParams textInputLayoutParams = new RelativeLayout.LayoutParams(
                RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
        textInputLayout.setLayoutParams(textInputLayoutParams);

        // Then you add editText into a textInputLayout
                LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.MATCH_PARENT);
        textInputLayout.addView(textInputEditText, lp);

//
//        addRssEdit = new EditText(getContext());
//        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
//                LinearLayout.LayoutParams.MATCH_PARENT,
//                LinearLayout.LayoutParams.MATCH_PARENT);
//        addRssEdit.setLayoutParams(lp);
        alertDialog.setView(textInputLayout);
        alertDialog.setIcon(R.drawable.ic_healing_black_24dp);
        alertDialog.setPositiveButton("ADD",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {

                    }
                });
        alertDialog.setNegativeButton("NO",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });

        //Builder "build" needs to be finished with "create"

        final AlertDialog dialog = alertDialog.create();

        myFabPager.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                dialog.show();

                dialog.getButton(AlertDialog.BUTTON_POSITIVE).setOnClickListener(new View.OnClickListener()
                {
                    @Override
                    public void onClick(View v)
                    {
                        boolean wntToClose = false;
                        //Do stuff, possibly set wantToCloseDialog to true then...
                        customURL = textInputEditText.getText().toString();
                        if (customURL.length() != 0) {
                            if (Patterns.WEB_URL.matcher(customURL).matches()){
                                Intent intent = new Intent();
                                intent.putExtra("url", customURL);

                                menu.add(R.id.rss_items_group, itemId++, 1, customURL).setCheckable(true).setIntent(intent);
                                wntToClose = true;
                            } else {textInputEditText.setError("Enter a valid URL!");}


                        } else {
                            textInputEditText.setError("ADD SOME TEXT HERE!");
                        }
                        //else dialog stays open.
                        //Make sure you have an obvious way to close the dialog especially if you set cancellable to false.
                        if (wntToClose) dialog.dismiss();
                    }
                });
            }
        });


        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

                Bundle bundle = new Bundle();
                bundle.putString("title", rssItems.get(i).getTitle());
                bundle.putString("desc", rssItems.get(i).getDescription());
                fragment.setArguments(bundle);
                fragmentManager.beginTransaction().replace(R.id.flContent, fragment).addToBackStack(null).commit();
            }
        });
        String wow = getArguments().getString("url");
        new ParseRss().execute(wow);
        return view;
    }

    // Task for retrieving array of {@link RssItem}s
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
