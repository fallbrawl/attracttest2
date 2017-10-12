package com.group.attract.attracttest2.Fragments;

import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.group.attract.attracttest2.R;

/**
 * Created by paul on 12.10.17.
 */

public class RssItemFragment extends Fragment {
    TextView title;
    TextView fullDesc;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.item_rss_fragment, null);

        title = view.findViewById(R.id.full_title);
        fullDesc = view.findViewById(R.id.full_desc);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N){

            title.setText(Html.fromHtml(getArguments().getString("title"), Html.FROM_HTML_MODE_COMPACT));
            fullDesc.setText(Html.fromHtml(getArguments().getString("desc"), Html.FROM_HTML_MODE_COMPACT));

        } else {
            fullDesc.setText(Html.fromHtml(getArguments().getString("desc")));
            title.setText(Html.fromHtml(getArguments().getString("title")));

        }


        Log.e("staty", "created" + getArguments().getString("title"));

        return view;
    }
}
