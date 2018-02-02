package com.example.gabri.firstapp;

import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.content.Context;
import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.text.method.LinkMovementMethod;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.gabri.firstapp.Adapter.CommentAdapter;
import com.example.gabri.firstapp.Model.Comment;
import com.example.gabri.firstapp.Model.Data;
import com.example.gabri.firstapp.Model.Game;
import com.example.gabri.firstapp.Model.RSSFeed;
import com.example.gabri.firstapp.View.LikeButtonView;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;


public class FragmentNewsDetail extends android.support.v4.app.Fragment
        implements AppBarLayout.OnOffsetChangedListener {

    private static final float PERCENTAGE_TO_SHOW_TITLE_AT_TOOLBAR  = 0.9f;
    private static final float PERCENTAGE_TO_HIDE_TITLE_DETAILS     = 0.3f;
    private static final int ALPHA_ANIMATIONS_DURATION              = 200;

    private boolean mIsTheTitleVisible          = false;
    private boolean mIsTheTitleContainerVisible = true;

    private LinearLayout mTitleContainer;
    private TextView mTitle;
    private AppBarLayout mAppBarLayout;
    private Toolbar mToolbar;
    private int gameId;
    private TextView bigTitle;
    private ImageView coverImage;
    private TextView littleTitle;
    private TextView description;
    private TextView underTitle;
    private TextView title;
    private TextView author;
    private TextView pubdate;
    private TextView linkToGamespot;
    private ImageView bigImage;
    private View viewRoot;
    private Context mContext;
    private LayoutInflater inflater;
    private YoutubePlayerFragment youtube;
    private Boolean isFavorite;
    private String idFirebase;
    private ImageView favorite;
    private RSSFeed realRssObject;





    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.viewRoot=inflater.inflate(R.layout.fragment_news_detail,container,false);
        this.inflater=inflater;
        bindActivity();

        mAppBarLayout.addOnOffsetChangedListener(this);

        mToolbar.inflateMenu(R.menu.menu_main);
        startAlphaAnimation(mTitle, 0, View.INVISIBLE);

        this.bigTitle=(TextView)this.viewRoot.findViewById(R.id.big_title);
        this.littleTitle=(TextView)this.viewRoot.findViewById(R.id.main_textview_title);
        this.author=(TextView)this.viewRoot.findViewById(R.id.author);
        this.description=(TextView)this.viewRoot.findViewById(R.id.description);
        this.underTitle=(TextView)this.viewRoot.findViewById(R.id.under_title);
        this.bigImage=(ImageView)this.viewRoot.findViewById(R.id.main_imageview_placeholder);
        this.title=(TextView)this.viewRoot.findViewById(R.id.content_title);
        this.linkToGamespot=(TextView)this.viewRoot.findViewById(R.id.gamespot_link);
        this.pubdate=(TextView)this.viewRoot.findViewById(R.id.pubdate_2);
        this.favorite=(ImageView)this.viewRoot.findViewById(R.id.favorite_star);
        prepareData();
        verifyIfNewsIsPreferred();
        setFavoriteClick();

        return this.viewRoot;

    }

    private void setFavoriteClick() {

        this.favorite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(isFavorite==false){
                    Toast.makeText(viewRoot.getContext(), "News added from your read more list", Toast.LENGTH_SHORT).show();
                    DatabaseReference databaseWishGame = FirebaseDatabase.getInstance().getReference("news").child(Data.getIdUserForRemoteDb());
                    String id = databaseWishGame.push().getKey();
                    realRssObject.setIdForFirebase(id);
                    databaseWishGame.child(id).setValue(realRssObject);
                    favorite.setImageResource(R.drawable.readlateron);
                    isFavorite=true;
                }else{
                    Toast.makeText(viewRoot.getContext(), "News removed from your read more list", Toast.LENGTH_SHORT).show();
                    DatabaseReference databaseWishGame= FirebaseDatabase.getInstance().getReference("news");
                    databaseWishGame.child(Data.getIdUserForRemoteDb()).child((realRssObject).getIdForFirebase()).removeValue();
                    favorite.setImageResource(R.drawable.readlateroff);
                    isFavorite=false;
                }
            }
        });


    }

    private void verifyIfNewsIsPreferred() {
        DatabaseReference databaseReference= FirebaseDatabase.getInstance().getReference();
        databaseReference.addListenerForSingleValueEvent(
                new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        if(dataSnapshot.hasChild("news"+"/"+ Data.getIdUserForRemoteDb()+"/"+idFirebase)){
                            isFavorite=true;
                            favorite.setImageResource(R.drawable.readlateron);
                        }
                        else {
                            isFavorite = false;
                            favorite.setImageResource(R.drawable.readlateroff);
                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                }
        );
    }

    private void setContex(Context mContext){
        this.mContext=mContext;
    }


    private void prepareData() {
        this.realRssObject=((RSSFeed)getArguments().getSerializable("REALRSSOBJECT"));
        String title =realRssObject.getTitle();
        String text =realRssObject.getDescription();
        String link =realRssObject.getImageLink();
        String releaseDate=realRssObject.getPubdate();
        String author=realRssObject.getCreator();
        this.idFirebase=getArguments().getString("IDFIREBASE");
        String gamespotLink="https://www.gamespot.com";

            this.bigTitle.setText("GameSpot Reviews");
            this.littleTitle.setText("GameSpot Reviews");
            this.description.setText(text);
            this.underTitle.setText("Publication Date: "+releaseDate);
            this.author.setText("Creator: "+author);
            this.pubdate.setText(releaseDate);
            this.title.setText(title);
            this.linkToGamespot.setText(Html.fromHtml("<html><a href=\""+gamespotLink+"\">Read More on Gamespot.com</a><html>"));
            this.linkToGamespot.setMovementMethod(LinkMovementMethod.getInstance());


            Glide.with(this).load(link).into(this.bigImage);







    }



    private void bindActivity() {
        mToolbar        = (Toolbar) this.viewRoot.findViewById(R.id.main_toolbar);
        mTitle          = (TextView) this.viewRoot.findViewById(R.id.main_textview_title);
        mTitleContainer = (LinearLayout) this.viewRoot.findViewById(R.id.main_linearlayout_title);
        mAppBarLayout   = (AppBarLayout) this.viewRoot.findViewById(R.id.main_appbar);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_main, menu);
        super.onCreateOptionsMenu(menu,inflater);
    }


    @Override
    public void onDestroyView() {

        super.onDestroyView();
    }

    @Override
    public void onOffsetChanged(AppBarLayout appBarLayout, int offset) {
        int maxScroll = appBarLayout.getTotalScrollRange();
        float percentage = (float) Math.abs(offset) / (float) maxScroll;

        handleAlphaOnTitle(percentage);
        handleToolbarTitleVisibility(percentage);
    }

    private void handleToolbarTitleVisibility(float percentage) {
        if (percentage >= PERCENTAGE_TO_SHOW_TITLE_AT_TOOLBAR) {

            if(!mIsTheTitleVisible) {
                startAlphaAnimation(mTitle, ALPHA_ANIMATIONS_DURATION, View.VISIBLE);
                mIsTheTitleVisible = true;
            }

        } else {

            if (mIsTheTitleVisible) {
                startAlphaAnimation(mTitle, ALPHA_ANIMATIONS_DURATION, View.INVISIBLE);
                mIsTheTitleVisible = false;
            }
        }
    }

    private void handleAlphaOnTitle(float percentage) {
        if (percentage >= PERCENTAGE_TO_HIDE_TITLE_DETAILS) {
            if(mIsTheTitleContainerVisible) {
                startAlphaAnimation(mTitleContainer, ALPHA_ANIMATIONS_DURATION, View.INVISIBLE);
                mIsTheTitleContainerVisible = false;
            }

        } else {

            if (!mIsTheTitleContainerVisible) {
                startAlphaAnimation(mTitleContainer, ALPHA_ANIMATIONS_DURATION, View.VISIBLE);
                mIsTheTitleContainerVisible = true;
            }
        }
    }

    public static void startAlphaAnimation (View v, long duration, int visibility) {
        AlphaAnimation alphaAnimation = (visibility == View.VISIBLE)
                ? new AlphaAnimation(0f, 1f)
                : new AlphaAnimation(1f, 0f);

        alphaAnimation.setDuration(duration);
        alphaAnimation.setFillAfter(true);
        v.startAnimation(alphaAnimation);
    }
}
