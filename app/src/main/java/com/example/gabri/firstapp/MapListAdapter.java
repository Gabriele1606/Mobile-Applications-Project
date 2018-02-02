package com.example.gabri.firstapp;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.LatLng;
import com.google.firebase.database.ThrowOnExtraProperties;

import java.util.HashMap;
import java.util.List;

/**
 * Created by simon on 01/02/2018.
 */

public class MapListAdapter extends ArrayAdapter<HashMap<String, String>>{


    private final Context context;
    private final List<HashMap<String, String>> objectList;
    private GoogleMap map;

    public MapListAdapter(@NonNull Context context, int resource, @NonNull List<HashMap<String, String>> objects) {
        super(context, resource, objects);
        this.context=context;
        this.objectList=objects;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View rowView=inflater.inflate(R.layout.row_lismap,parent,false);
        TextView firstline= (TextView) rowView.findViewById(R.id.firstLine);
        TextView secondline= (TextView) rowView.findViewById(R.id.secondLine);

        firstline.setText(objectList.get(position).get("place_name"));
        secondline.setText(objectList.get(position).get("vicinity"));

        return rowView;
    }



    public void setMap(GoogleMap map) {
        this.map = map;
    }
}
