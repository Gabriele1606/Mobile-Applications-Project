
package com.example.gabri.firstapp.Adapter;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.RecyclerView;
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

import static com.raizlabs.android.dbflow.config.FlowManager.getContext;

/**
 * Created by Gabri
 */

public class ReadLaterAdapter extends RecyclerView.Adapter<ReadLaterAdapter.ViewHolder> {

    private List<RSSFeed> readLaterRss;
    private ViewGroup parent;

    public static class ViewHolder extends RecyclerView.ViewHolder {
        private ImageView coverImage;
        private ImageView garbage;
        private TextView newsTitle;
        private TextView pubDate;
        private RSSFeed news;
        private View view;

        public ViewHolder(View view) {
            super(view);
            coverImage = (ImageView) view.findViewById(R.id.wish_news_cover);
            newsTitle = (TextView) view.findViewById(R.id.news_title);
            pubDate = (TextView) view.findViewById(R.id.pub_date);
            this.view=view;

        }

    }


    public ReadLaterAdapter(List<RSSFeed> readLaterRss) {
        this.readLaterRss = readLaterRss;
    }


    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.read_later_list_row, parent, false);
        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        final RSSFeed rss = readLaterRss.get(position);
        holder.newsTitle.setText(rss.getTitle());
        holder.pubDate.setText(rss.getPubdate());
        Glide.with(getContext()).load(rss.getImageLink()).into(holder.coverImage);

        holder.view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                boolean TWOPANELS = Data.getData().getHomePageActivity().getResources().getBoolean(R.bool.has_two_panes);
                Bundle bundle=new Bundle();
                bundle.putString("IDFIREBASE",rss.getIdForFirebase());
                bundle.putSerializable("REALRSSOBJECT",rss);


                    FragmentNewsDetail fragmentNewsDetail= new FragmentNewsDetail();
                    fragmentNewsDetail.setArguments(bundle);
                    //FINAL SOLUTION
                    Fragment fragmentById = Data.getData().getHomePageActivity().getSupportFragmentManager().findFragmentById(R.id.mainframeLayout);
                    FragmentTransaction transaction = Data.getData().getHomePageActivity().getSupportFragmentManager().beginTransaction().replace(R.id.mainframeLayout, fragmentNewsDetail, "GameDetail");
                    transaction.addToBackStack("TABLAYOUT");
                    transaction.commit();

            }
        });

    }



    @Override
    public int getItemCount() {
        return readLaterRss.size();
    }

}


   /* private void setOnclicOnText(){
        this.newsTitle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int pos=(int)view.getTag();
                RSSFeed temp=getItem(pos);
                boolean TWOPANELS = Data.getData().getHomePageActivity().getResources().getBoolean(R.bool.has_two_panes);

                Bundle bundle=new Bundle();
                bundle.putString("TITLE",temp.getTitle());
                bundle.putString("TEXT", temp.getDescription());
                bundle.putString("IMAGE", temp.getImageLink());
                bundle.putString("DATE", temp.getPubdate());
                bundle.putString("MULTIPLAYERLINK", temp.getGuid());

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
    }*/



