package com.group.attract.attracttest2;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.text.SimpleDateFormat;
import java.util.ArrayList;

/**
 * Created by nexus on 11.10.2017.
 */
public class RssAdapter extends ArrayAdapter<RssItem> {
    private LayoutInflater mInflater;

    public RssAdapter(Context context, ArrayList<RssItem> objects) {
        super(context, 0, objects);
        mInflater = LayoutInflater.from(context);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;

        if (convertView == null) {
            convertView = mInflater.inflate(R.layout.item_rss, null);

            // Creates a ViewHolder and store references to the children views
            // we want to bind data to.
            holder = new ViewHolder();

            holder.title = convertView.findViewById(R.id.rss_item_title);
            holder.shortDescription = convertView.findViewById(R.id.rss_short_desc);

            convertView.setTag(holder);
        } else {

            // Get the ViewHolder back to get fast access to the TextView
            holder = (ViewHolder) convertView.getTag();
        }

        RssItem rssItem = getItem(position);

        // Bind the data efficiently with the holder.
        holder.title.setText(rssItem.getTitle());
        holder.shortDescription.setText(rssItem.getDescription());

        return convertView;
    }
    static class ViewHolder {

        TextView title;
        TextView shortDescription;

    }
}
