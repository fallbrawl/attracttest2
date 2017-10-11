package com.group.attract.attracttest2;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.text.SimpleDateFormat;
import java.util.ArrayList;

/**
 * Created by paul on 11.10.17.
 */

public class SuperheroAdapter extends ArrayAdapter<SuperheroProfile> implements Filterable {
    private ArrayList<SuperheroProfile> heroArrayList;
    private ArrayList<SuperheroProfile> filteredHeroArrayList;
    private LayoutInflater mInflater;

    public SuperheroAdapter(Context context, ArrayList<SuperheroProfile> objects) {
        super(context, 0, objects);
        mInflater = LayoutInflater.from(context);
        filteredHeroArrayList = objects;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;

        if (convertView == null) {
            convertView = mInflater.inflate(R.layout.list_item, null);

            // Creates a ViewHolder and store references to the children views
            // we want to bind data to.
            holder = new ViewHolder();

            holder.avatar = convertView.findViewById(R.id.list_item_thumbnail);
            holder.name = convertView.findViewById(R.id.list_item_superhero_name);
            holder.date = convertView.findViewById(R.id.list_item_date);

            convertView.setTag(holder);
        } else {

            // Get the ViewHolder back to get fast access to the TextView
            holder = (ViewHolder) convertView.getTag();
        }
        SuperheroProfile superheroProfile = getItem(position);
        long timeStamp = Long.parseLong(superheroProfile.getTime());

        //Date conversion
        java.util.Date time=new java.util.Date(timeStamp *1000);
        SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("dd-MM-yyyy");
        String temp = DATE_FORMAT.format(time);

        // Bind the data efficiently with the holder.
        holder.name.setText(superheroProfile.getName());
        holder.date.setText(temp);
        Picasso.with(getContext()).load(superheroProfile.getImage()).centerCrop().resize(64,64).placeholder(R.mipmap.ic_launcher).into(holder.avatar);

        return convertView;
    }
    static class ViewHolder {
        ImageView avatar;
        TextView name;
        TextView date;
    }

    /**
     * Custom filter for SUPERHERO list
     * Filter content in superhero list according to the search text
     */
    public Filter getFilter() {
        return new Filter() {

            @Override
            protected FilterResults performFiltering(CharSequence constraint) {
                final FilterResults oReturn = new FilterResults();
                final ArrayList<SuperheroProfile> results = new ArrayList<>();
                if (heroArrayList == null){
                    heroArrayList = new ArrayList<>();
                    heroArrayList.addAll(filteredHeroArrayList);
                }
                if (constraint != null) {
                    if (heroArrayList != null && heroArrayList.size() > 0) {
                        for (final SuperheroProfile g : heroArrayList) {
                            if (g.getName().toLowerCase()
                                    .contains(constraint.toString()))
                                results.add(g);
                        }
                    }
                    oReturn.values = results;
                }
                return oReturn;
            }

            @SuppressWarnings("unchecked")
            @Override
            protected void publishResults(CharSequence constraint,
                                          FilterResults results) {

                filteredHeroArrayList.clear();
                filteredHeroArrayList.addAll((ArrayList<SuperheroProfile>) results.values);

                notifyDataSetChanged();
            }
        };
    }

    public void notifyDataSetChanged() {
        super.notifyDataSetChanged();
    }

}
