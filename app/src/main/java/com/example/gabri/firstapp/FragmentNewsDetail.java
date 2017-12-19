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
import android.view.animation.AlphaAnimation;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.gabri.firstapp.Adapter.CommentAdapter;
import com.example.gabri.firstapp.Model.Comment;
import com.example.gabri.firstapp.Model.Game;

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
    private TextView linkToMultiplayer;
    private ImageView bigImage;
    private ImageView contentImage;
    private View viewRoot;
    private Context mContext;
    private LayoutInflater inflater;
    private YoutubePlayerFragment youtube;




    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.viewRoot=inflater.inflate(R.layout.fragment_news_detail,container,false);
        this.mContext=container.getContext();
        this.inflater=inflater;
        bindActivity();

        mAppBarLayout.addOnOffsetChangedListener(this);

        mToolbar.inflateMenu(R.menu.menu_main);
        startAlphaAnimation(mTitle, 0, View.INVISIBLE);

        this.bigTitle=(TextView)this.viewRoot.findViewById(R.id.big_title);
        this.littleTitle=(TextView)this.viewRoot.findViewById(R.id.main_textview_title);
        this.description=(TextView)this.viewRoot.findViewById(R.id.description);
        this.underTitle=(TextView)this.viewRoot.findViewById(R.id.under_title);
        this.bigImage=(ImageView)this.viewRoot.findViewById(R.id.main_imageview_placeholder);
        this.title=(TextView)this.viewRoot.findViewById(R.id.content_title);
        this.contentImage=(ImageView)this.viewRoot.findViewById(R.id.content_image);
        this.linkToMultiplayer=(TextView)this.viewRoot.findViewById(R.id.multiplayer_link);

        prepareData();

        return this.viewRoot;

    }

    private void setContex(Context mContext){
        this.mContext=mContext;
    }


    private void prepareData() {
        String title =getArguments().getString("TITLE");
        String text =getArguments().getString("TEXT");
        String link =getArguments().getString("IMAGE");
        String multiplayerLink=getArguments().getString("MULTIPLAYERLINK");
        String releaseDate=getArguments().getString("DATE");


            this.bigTitle.setText("Multiplayer.it");
            this.littleTitle.setText("Multiplayer.it");
            this.description.setText(text);
            this.underTitle.setText("Publication Date: "+releaseDate);
            this.title.setText(title);
            this.linkToMultiplayer.setText(Html.fromHtml("<html><a href=\""+multiplayerLink+"\">Read More on Multiplayer.it</a><html>"));
            this.linkToMultiplayer.setMovementMethod(LinkMovementMethod.getInstance());


            Glide.with(this).load(link).into(this.contentImage);







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
