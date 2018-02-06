package com.example.gabri.firstapp.Adapter;

/**
 * Created by simon on 09/11/2017.
 */

import android.content.Context;
import android.graphics.Bitmap;
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
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.transition.Transition;
import com.example.gabri.firstapp.DBQuery;
import com.example.gabri.firstapp.FragmentGameDetail;
import com.example.gabri.firstapp.FragmentProfile;
import com.example.gabri.firstapp.Model.Data;
import com.example.gabri.firstapp.Model.Game;
import com.example.gabri.firstapp.Model.RowGame;
import com.example.gabri.firstapp.Model.User;
import com.example.gabri.firstapp.R;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.List;

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


    public class RssFeedHolder extends RecyclerView.ViewHolder{
        public TextView rssText;
        public TextView rssTitle;
        public TextView rssPubDate;
        public ImageView imageView;
        public ImageView readLaterButton;
        public Boolean isFavorite;
        public View view;
        public RssFeedHolder(View itemView) {
            super(itemView);
            view=itemView;
            //rssText = (TextView) itemView.findViewById(R.id.text_news);
            //rssText = (TextView) itemView.findViewById(R.id.text_news);
            rssTitle=(TextView) itemView.findViewById(R.id.title_news);
            rssPubDate=(TextView) itemView.findViewById(R.id.pubdate);
            imageView=(ImageView)itemView.findViewById(R.id.image_rss);
            //readLaterButton=(ImageView) itemView.findViewById(R.id.read_later);

        }
    }


    public class TitleHolder extends RecyclerView.ViewHolder{
        TextView textView;
        public TitleHolder(View itemView) {
            super(itemView);
            this.textView= (TextView) itemView.findViewById(R.id.title_section);
        }
        public void setTitle(String title){
            this.textView.setText(title);
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
        Glide.with(mContext).load("http://thegamesdb.net/banners/"+dbQuery.getBoxArtFromGame(game).getThumb()).apply(new RequestOptions().circleCrop()).into(gameHolder.wishGameCover);
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


    private void configureGridHolder(RecyclerAdapter.GridHolder gridHolder, int position) {
        boolean TWOPANELS = Data.getData().getHomePageActivity().getResources().getBoolean(R.bool.has_two_panes);
        RowGame rowGame=(RowGame)listObject.get(position);
        gridHolder.adapter=new GridAdapter(mContext,rowGame.getList());
        if (TWOPANELS)
            gridHolder.SPANCOUNT=5;
        gridHolder.recelement.setAdapter(gridHolder.adapter);
        gridHolder.recelement.setNestedScrollingEnabled(false);
        gridHolder.recelement.setItemAnimator(new SlideInUpAnimator());
    }

    @Override
    public int getItemCount() {
        return listObject.size();
    }

}