package com.example.gabri.firstapp.Adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.gabri.firstapp.Boxart;
import com.example.gabri.firstapp.DBQuery;
import com.example.gabri.firstapp.Model.Comment;
import com.example.gabri.firstapp.Model.Data;
import com.example.gabri.firstapp.Model.Game;
import com.example.gabri.firstapp.Model.RSSFeed;
import com.example.gabri.firstapp.R;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.List;

/**
 * Created by Gabri
 */

public class ReadLaterAdapter extends ArrayAdapter<RSSFeed> {

    private ImageView coverImage;
    private ImageView garbage;
    private TextView newsTitle;
    private RSSFeed news;
    private Context mContex;

    public ReadLaterAdapter(@NonNull Context context, int resource, @NonNull List<RSSFeed> objects) {
        super(context, resource, objects);
        this.mContex=context;

    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) getContext()
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        convertView = inflater.inflate(R.layout.read_later_list_row, null);

        coverImage=(ImageView) convertView.findViewById(R.id.wish_news_cover);
        garbage=(ImageView) convertView.findViewById(R.id.garbage);
        newsTitle = (TextView)convertView.findViewById(R.id.news_title);
        System.out.println("Sto stampando questo"+getItem(position).toString());
        news = getItem(position);
        newsTitle.setText(news.getTitle());
        Glide.with(getContext()).load(news.getImageLink()).into(coverImage);

        setClickOnGarbage();

        return convertView;
    }

    private void setClickOnGarbage() {


    }

}

