package com.example.gabri.firstapp.Adapter;

/**
 * Created by simon on 09/11/2017.
 */

import android.content.Context;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.PopupMenu;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.gabri.firstapp.Model.Game;
import com.example.gabri.firstapp.Model.News;
import com.example.gabri.firstapp.Model.RowGame;
import com.example.gabri.firstapp.R;

import java.util.List;

import jp.wasabeef.recyclerview.animators.SlideInUpAnimator;


public class RecyclerAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private final int NEWS = 1;
    private final int ROWGAME = 0;
    private Context mContext;
    private List<Object> listObject;

    public class RecHolder extends RecyclerView.ViewHolder {
        public RecyclerView recelement;
       public HorizontalAdapter adapter;

        public RecHolder(View view) {
            super(view);
            RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(mContext, LinearLayoutManager.HORIZONTAL,false);
            recelement=(RecyclerView) view.findViewById(R.id.rec_element_id);
            recelement.setLayoutManager(mLayoutManager);
            recelement.setItemAnimator(new DefaultItemAnimator());
            //System.out.println(this.adapter);
        }
    }
    public class NewsHolder extends RecyclerView.ViewHolder{
        public TextView testonews;
        public NewsHolder(View itemView) {
            super(itemView);
            testonews = (TextView) itemView.findViewById(R.id.testo_news);

        }
    }

    public RecyclerAdapter(Context mContext, List<Object> listObject) {
        this.mContext = mContext;
        this.listObject = listObject;
    }
    @Override
    public int getItemViewType(int position) {
        if (listObject.get(position) instanceof RowGame) {
            return ROWGAME;
        }else if (listObject.get(position) instanceof News){
            return NEWS;
        }/*else if (items.get(position) instanceof String) {
                return IMAGE;
            }*/
        return -1;
    }
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        RecyclerView.ViewHolder viewHolder;
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());

        switch (viewType) {
            case ROWGAME:
                View itemView = LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.horizontal_recycler, parent, false);
                viewHolder = new RecHolder(itemView);
                break;

            case NEWS:
                View newsView = LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.news_element, parent, false);
                viewHolder = new NewsHolder(newsView);
                break;
            /*case IMAGE:
                View v2 = inflater.inflate(R.layout.layout_viewholder2, viewGroup, false);
                viewHolder = new ViewHolder2(v2);
                break;*/
            default:
                View v = LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.horizontal_recycler, parent, false);
                viewHolder = new RecHolder(v);
                break;
        }

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder viewHolder, int position) {
        switch (viewHolder.getItemViewType()) {
            case ROWGAME:
                RecHolder vh1 = (RecHolder) viewHolder;
                configureRecHolder(vh1, position);
                break;
            case NEWS:
                NewsHolder newsHolder= (NewsHolder) viewHolder;
                newsHolder.testonews.setText("PROVA NEWS");
            /*case IMAGE:
                ViewHolder2 vh2 = (ViewHolder2) viewHolder;
                configureViewHolder2(vh2, position);
                break;
            default:
                RecyclerViewSimpleTextViewHolder vh = (RecyclerViewSimpleTextViewHolder) viewHolder;
                configureDefaultViewHolder(vh, position);
                break;*/
        }

    }

    private void configureRecHolder(RecHolder holder, int position) {

        RecyclerView recyclerElem;

        RowGame rowGame = (RowGame) listObject.get(position);
        holder.adapter= new HorizontalAdapter(mContext,rowGame.getList());
        holder.recelement.setAdapter(holder.adapter);
        holder.recelement.setNestedScrollingEnabled(false);
        //prova
        holder.recelement.setItemAnimator(new SlideInUpAnimator());
        //holder.recelement.setHasFixedSize(true);
    }


    /**
     * Click listener for popup menu items
     */
    class MyMenuItemClickListener implements PopupMenu.OnMenuItemClickListener {

        public MyMenuItemClickListener() {
        }

        @Override
        public boolean onMenuItemClick(MenuItem menuItem) {
            switch (menuItem.getItemId()) {
            }
            return false;
        }
    }

    @Override
    public int getItemCount() {
        return listObject.size();
    }
}