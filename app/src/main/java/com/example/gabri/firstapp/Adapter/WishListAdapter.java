
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
import com.example.gabri.firstapp.FragmentGameDetail;
import com.example.gabri.firstapp.FragmentNewsDetail;
import com.example.gabri.firstapp.GameEntity;
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
        garbage.setTag(position);
        gameTitle.setTag(position);
        setClickOnGarbage();
        setOnclicOnText();

        return convertView;
    }

    private void setClickOnGarbage() {
        garbage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int pos =(int)view.getTag();
                Game tmp=getItem(pos);
                Toast.makeText(mContex,"Games removed from your wish list", Toast.LENGTH_SHORT).show();
                DatabaseReference databaseWishGame= FirebaseDatabase.getInstance().getReference("game");
                databaseWishGame.child(Data.getIdUserForRemoteDb()).child(Integer.toString(tmp.getId())).removeValue();
                remove(tmp);
                notifyDataSetChanged();

            }
        });


    }

    private void setOnclicOnText(){
        this.gameTitle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int pos=(int)view.getTag();
                Game tmp=getItem(pos);
                FragmentGameDetail fragmentGameDetail=new FragmentGameDetail();
                Bundle bundle=new Bundle();
                bundle.putInt("GAME ID",tmp.getId());
                fragmentGameDetail.setArguments(bundle);
                boolean TWOPANELS = Data.getData().getHomePageActivity().getResources().getBoolean(R.bool.has_two_panes);

                    //FINAL SOLUTION
                    Fragment fragmentById = Data.getData().getHomePageActivity().getSupportFragmentManager().findFragmentById(R.id.mainframeLayout);
                    FragmentTransaction transaction = Data.getData().getHomePageActivity().getSupportFragmentManager().beginTransaction().replace(R.id.mainframeLayout, fragmentGameDetail, "GameDetail");
                    transaction.addToBackStack("TABLAYOUT");
                    transaction.commit();
            }
        });
    }

}
