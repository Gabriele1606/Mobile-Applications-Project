
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
import com.example.gabri.firstapp.R;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.List;

/**
 * Created by Gabri
 */

public class WishListAdapter extends ArrayAdapter<Game> {

    private ImageView coverImage;
    private ImageView garbage;
    private TextView gameTitle;
    private Game game;
    private Context mContex;

    public WishListAdapter(@NonNull Context context, int resource, @NonNull List<Game> objects) {
        super(context, resource, objects);
        this.mContex=context;

    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) getContext()
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        convertView = inflater.inflate(R.layout.wish_list_row, null);

        coverImage=(ImageView) convertView.findViewById(R.id.wish_game_cover);
        garbage=(ImageView) convertView.findViewById(R.id.garbage);
        gameTitle = (TextView)convertView.findViewById(R.id.username);

        game = getItem(position);
        gameTitle.setText(game.getGameTitle());
        DBQuery dbQuery=new DBQuery();
        Boxart boxart=dbQuery.getBoxArtFromGame(game);
        Glide.with(getContext()).load("http://thegamesdb.net/banners/"+boxart.getThumb()).into(coverImage);

        setClickOnGarbage();

        return convertView;
    }

    private void setClickOnGarbage() {
        garbage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                System.out.println("cliccato");
                DatabaseReference databaseWishGame= FirebaseDatabase.getInstance().getReference("game");
                databaseWishGame.child(Data.getIdUserForRemoteDb()).child(Integer.toString(game.getId())).removeValue();
                remove(game);
                notifyDataSetChanged();

            }
        });


    }

}
