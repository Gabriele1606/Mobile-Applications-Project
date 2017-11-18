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

import com.example.gabri.firstapp.Model.Game;
import com.example.gabri.firstapp.R;

import java.util.List;

import jp.wasabeef.recyclerview.animators.SlideInUpAnimator;

/**
 * Created by Ravi Tamada on 18/05/16.
 */
public class RecyclerAdapter extends RecyclerView.Adapter<RecyclerAdapter.RecHolder> {

    private Context mContext;
    private List<List<Game>> listGameList;

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


    public RecyclerAdapter(Context mContext, List<List<Game>> listGameList) {
        this.mContext = mContext;
        this.listGameList = listGameList;
    }

    @Override
    public RecHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.horizontal_recycler, parent, false);

        return new RecHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final RecHolder holder, int position) {
        RecyclerView recyclerElem;

        List<Game> listAlbum = listGameList.get(position);
        holder.adapter= new HorizontalAdapter(mContext,listAlbum);
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
        return listGameList.size();
    }
}