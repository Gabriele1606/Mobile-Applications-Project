
package com.example.gabri.firstapp.Adapter;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.gabri.firstapp.Boxart;
import com.example.gabri.firstapp.DBQuery;
import com.example.gabri.firstapp.FragmentNewsDetail;
import com.example.gabri.firstapp.FragmentReadLater;
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

        setOnclicOnText();
        setClickOnGarbage();

        return convertView;
    }

    private void setClickOnGarbage() {

        garbage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(mContex,"News removed from your read more list", Toast.LENGTH_SHORT).show();
                DatabaseReference databaseWishGame= FirebaseDatabase.getInstance().getReference("news");
                databaseWishGame.child(Data.getIdUserForRemoteDb()).child(news.getIdForFirebase()).removeValue();
                remove(news);
                notifyDataSetChanged();

            }
        });


    }

    private void setOnclicOnText(){
        this.newsTitle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                boolean TWOPANELS = Data.getData().getHomePageActivity().getResources().getBoolean(R.bool.has_two_panes);

                Bundle bundle=new Bundle();
                bundle.putString("TITLE",news.getTitle());
                bundle.putString("TEXT", news.getDescription());
                bundle.putString("IMAGE", news.getImageLink());
                bundle.putString("DATE", news.getPubdate());
                bundle.putString("MULTIPLAYERLINK", news.getGuid());

                if(!TWOPANELS){
                    FragmentNewsDetail fragmentNewsDetail= new FragmentNewsDetail();
                    fragmentNewsDetail.setArguments(bundle);
                    //FINAL SOLUTION
                    Fragment fragmentById = Data.getData().getHomePageActivity().getSupportFragmentManager().findFragmentById(R.id.mainframeLayout);
                    FragmentTransaction transaction = Data.getData().getHomePageActivity().getSupportFragmentManager().beginTransaction().replace(R.id.mainframeLayout, fragmentNewsDetail, "GameDetail");
                    transaction.addToBackStack("TABLAYOUT");
                    transaction.commit();
                }else{
                    FragmentNewsDetail fragmentNewsDetail= new FragmentNewsDetail();
                    fragmentNewsDetail.setArguments(bundle);
                    FragmentTransaction transaction = Data.getData().getHomePageActivity().getSupportFragmentManager().beginTransaction().replace(R.id.framegameDetail,fragmentNewsDetail, "GameDetail");
                    transaction.commit();
                    Data.getData().getHomePageActivity().enlargeDetailGame();
                }
            }
        });
    }

}

