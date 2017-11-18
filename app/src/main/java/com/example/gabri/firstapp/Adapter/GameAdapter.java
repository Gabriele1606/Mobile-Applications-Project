package com.example.gabri.firstapp.Adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.gabri.firstapp.Model.GameCover;
import com.example.gabri.firstapp.R;

import java.util.List;

/**
 * Created by Gabri on 11/11/17.
 */

public class GameAdapter extends RecyclerView.Adapter<GameAdapter.ViewHolder> {

    private List<GameCover> listOfCoverGame;
    private Context context;

    public GameAdapter(List<GameCover> listOfCoverGame, Context context) {
        this.listOfCoverGame = listOfCoverGame;
        this.context = context;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.game_cover,parent,false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        GameCover gameCover = listOfCoverGame.get(position);

        holder.image.setImageResource(R.drawable.image_2);
        holder.description.setText(gameCover.getDescription());
    }


    @Override
    public int getItemCount() {
        return listOfCoverGame.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

         ImageView image;
         TextView description;


        public ViewHolder(View itemView) {
            super(itemView);

            image = (ImageView) itemView.findViewById(R.id.imageadapter);
            description=(TextView)itemView.findViewById(R.id.descriptionadapter);
        }
    }
}
