package com.example.gabri.firstapp.Adapter;

/**
 * Created by simon on 09/11/2017.
 */

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.graphics.drawable.RoundedBitmapDrawable;
import android.support.v4.graphics.drawable.RoundedBitmapDrawableFactory;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.target.Target;
import com.bumptech.glide.request.transition.Transition;
import com.example.gabri.firstapp.Controller.GlideApp;
import com.example.gabri.firstapp.DBQuery;
import com.example.gabri.firstapp.FragmentGameDetail;
import com.example.gabri.firstapp.FragmentProfile;
import com.example.gabri.firstapp.Model.Data;
import com.example.gabri.firstapp.Model.Game;
import com.example.gabri.firstapp.Model.RowGame;
import com.example.gabri.firstapp.Model.User;
import com.example.gabri.firstapp.R;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import jp.wasabeef.recyclerview.animators.SlideInUpAnimator;


public class RecyclerSearch extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    //Defining type of elements
    private final int ELEMENT_GAME=0;
    private final int ELEMENT_USER=1;

    private Context mContext;
    private List<Object> listObject;


    public class GameHolder extends RecyclerView.ViewHolder {
        private View view;
        public ImageView wishGameCover;
        public TextView gameTitle;
        public TextView consoleGame;
        public TextView dateGame;
        public ImageView facebookImage;

        public GameHolder(View view) {
            super(view);
            wishGameCover=(ImageView)view.findViewById(R.id.wish_game_cover);
            gameTitle=(TextView)view.findViewById(R.id.game_row_title);
            consoleGame=(TextView)view.findViewById(R.id.console_game_row);
            dateGame=(TextView)view.findViewById(R.id.pub_date_game_row);
            facebookImage=(ImageView) view.findViewById(R.id.facebook_share);
            this.view=view;
        }
    }

    public class UserHolder extends RecyclerView.ViewHolder {
        private View view;
        public ImageView userCover;
        public TextView textUsername;
        public TextView descriptionUser;
        public TextView dateGame;
        public ImageView facebookImage;

        public UserHolder(View view) {
            super(view);
            userCover=(ImageView)view.findViewById(R.id.wish_game_cover);
            textUsername=(TextView)view.findViewById(R.id.game_row_title);
            descriptionUser=(TextView)view.findViewById(R.id.console_game_row);
            dateGame=(TextView)view.findViewById(R.id.pub_date_game_row);
            facebookImage=(ImageView) view.findViewById(R.id.facebook_share);
            facebookImage.setVisibility(View.GONE);
            dateGame.setVisibility(View.GONE);
            this.view=view;

        }
    }

    //Constructor
    public RecyclerSearch(Context mContext, List<Object> listObject) {
        this.mContext = mContext;
        this.listObject = listObject;
    }
    @Override
    public int getItemViewType(int position) {
       if (listObject.get(position) instanceof Game){
            return ELEMENT_GAME;
        }else if (listObject.get(position) instanceof User){
            return ELEMENT_USER;
        }
        return -1;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        RecyclerView.ViewHolder viewHolder;
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());

        switch (viewType) {
            case ELEMENT_GAME:
                View gameView = LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.wish_list_row, parent, false);
                viewHolder = new GameHolder(gameView);
                break;
            case ELEMENT_USER:
                View userView = LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.wish_list_row, parent, false);
                viewHolder = new UserHolder(userView);
                break;
            default:
                viewHolder = null;
                break;
        }
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder viewHolder, final int position) {
        switch (viewHolder.getItemViewType()) {
            case ELEMENT_GAME:
                GameHolder gameHolder = (GameHolder) viewHolder;
                configureGameHolder(gameHolder,position);
                break;
            case ELEMENT_USER:
                UserHolder userHolder=(UserHolder)viewHolder;
                configureUserHolder(userHolder,position);
                break;
        }

    }


    private void configureGameHolder(GameHolder gameHolder, int position) {
        DBQuery dbQuery= new DBQuery();
        final Game game = (Game) listObject.get(position);

        Glide.with(mContext).load(R.drawable.gamecontroller).apply(new RequestOptions().circleCrop()).into(gameHolder.wishGameCover);

        Pattern pattern=null;
        Matcher matcher=null;

        //String regex = "([0-9].*\\.jpg?)";
        String regex = "([0-9].*(\\.(jpg)|(png))?)";
        String string = dbQuery.getBoxArtFromGame(game).getThumb();
        final String thumb = dbQuery.getBoxArtFromGame(game).getThumb();
        if (string!=null) {
            pattern = Pattern.compile(regex);
            matcher = pattern.matcher(string);
            if (matcher.find()){
                StorageReference storageReference = FirebaseStorage.getInstance().getReference();
                StorageReference child = storageReference.child("thumbs/" + matcher.group(1));
                GlideApp.with(mContext).load(child).apply(RequestOptions.circleCropTransform()).into(gameHolder.wishGameCover);
            }
        }

        gameHolder.consoleGame.setText(game.getPlatform());
        gameHolder.gameTitle.setText(game.getGameTitle());
        gameHolder.dateGame.setText(game.getReleaseDate());
        gameHolder.facebookImage.setVisibility(View.GONE);
        gameHolder.view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Bundle bundle = new Bundle();
                bundle.putString("IDFIREBASE", game.getIdForFirebase());
                bundle.putSerializable("REALGAMEOBJECT", game);
                FragmentGameDetail fragmentGameDetail = new FragmentGameDetail();
                fragmentGameDetail.setArguments(bundle);
                //FINAL SOLUTION
                Fragment fragmentById = Data.getData().getHomePageActivity().getSupportFragmentManager().findFragmentById(R.id.mainframeLayout);
                FragmentTransaction transaction = Data.getData().getHomePageActivity().getSupportFragmentManager().beginTransaction().replace(R.id.mainframeLayout, fragmentGameDetail, "GameDetail");
                transaction.addToBackStack("Searchlist");
                transaction.commit();

            }
        });
    }

    private void configureUserHolder(final UserHolder userHolder, int position) {
        StorageReference storageReference = FirebaseStorage.getInstance().getReference();
        final User user= (User) listObject.get(position);
        userHolder.textUsername.setText(user.getUsername());
        userHolder.descriptionUser.setText(user.getDescription());
        storageReference.child("images/"+ user.getId()).getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                Glide.with(mContext).asBitmap().load(uri).apply(RequestOptions.circleCropTransform()).into(userHolder.userCover);
            }
        });

        userHolder.view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FragmentProfile fragmentProfile= new FragmentProfile();
                fragmentProfile.setProfile(user);
                FragmentTransaction transaction = Data.getData().getHomePageActivity().getSupportFragmentManager().beginTransaction().replace(R.id.mainframeLayout, fragmentProfile, "GameDetail");
                transaction.addToBackStack("Searchlist");
                transaction.commit();
            }
        });
    }



    @Override
    public int getItemCount() {
        return listObject.size();
    }

}