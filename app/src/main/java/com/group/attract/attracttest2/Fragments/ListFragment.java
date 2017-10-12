package com.group.attract.attracttest2.Fragments;

import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.SearchView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import com.group.attract.attracttest2.R;
import com.group.attract.attracttest2.SuperheroAdapter;
import com.group.attract.attracttest2.SuperheroProfile;
import com.group.attract.attracttest2.Utils.RetrofitManager;
import com.group.attract.attracttest2.Utils.SuperheroService;

import java.io.IOException;
import java.util.ArrayList;

import retrofit2.Response;

/**
 * Created by paul on 11.10.17.
 */


public class ListFragment extends Fragment implements SearchView.OnQueryTextListener{
    private ListView listView;
    private RetrofitManager retrofitManager;
    private SuperheroService superheroService;
    private SuperheroAdapter superheroAdapter;
    private ArrayList<SuperheroProfile> superheroProfiles;
    private Handler handler;
    SearchView searchView;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.list_fragment, null);
        listView = view.findViewById(R.id.list_superheroes);
        retrofitManager = new RetrofitManager();

        superheroService = retrofitManager.getSuperheroService();

        superheroProfiles = new ArrayList<>();
        superheroAdapter = new SuperheroAdapter(getContext(), superheroProfiles);
        listView.setAdapter(superheroAdapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent intent = new Intent(getContext(), FragmentPagerSupport.class);
                Bundle bundle = new Bundle();

                bundle.putSerializable("profile", superheroProfiles);
                bundle.putInt("currentPosition", i);
                intent.putExtras(bundle);

                startActivity(intent);
            }
        });
        getResults();

        return view;
    }

    private void getResults() {

        final Thread tr = new Thread(new Runnable() {

            @Override
            public void run() {

                try {

                    Response<ArrayList<SuperheroProfile>> response = superheroService.getListSuperheroes().execute();

                    if (response.isSuccessful()) {

                        ArrayList<SuperheroProfile> temp = response.body();

                        Message msg;
                        msg = handler.obtainMessage(0, 0, 0, temp);

                        handler.sendMessage(msg);
                    }

                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });
        tr.start();

        handler = new Handler() {

            @Override
            public void handleMessage(Message msg) {
                ArrayList<SuperheroProfile> wow = (ArrayList<SuperheroProfile>) msg.obj;

                superheroProfiles.clear();
                superheroProfiles.addAll(wow);
                superheroAdapter.notifyDataSetChanged();

            }
        };
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {

        inflater.inflate(R.menu.searchbar, menu);

        // Associate searchable configuration with the SearchView
        SearchManager searchManager =
                (SearchManager) getActivity().getSystemService(Context.SEARCH_SERVICE);
        searchView =
                (SearchView) menu.findItem(R.id.search).getActionView();
        searchView.setSearchableInfo(
                searchManager.getSearchableInfo(getActivity().getComponentName()));
        searchView.setOnQueryTextListener(this);
        searchView.setQueryHint("Search here!");

        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onQueryTextSubmit(String query) {
        return false;
    }

    @Override
    public boolean onQueryTextChange(String newText) {
       superheroAdapter.getFilter().filter(newText);

        return true;
    }
}


