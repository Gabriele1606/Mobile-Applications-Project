package com.example.gabri.firstapp;

import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.gabri.firstapp.Model.Game;

import java.util.List;


public class FragmentGameDetail extends AppCompatActivity
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
    private ImageView bigImage;
    private TextView underTitle;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_game_detail);

        bindActivity();

        mAppBarLayout.addOnOffsetChangedListener(this);

        mToolbar.inflateMenu(R.menu.menu_main);
        startAlphaAnimation(mTitle, 0, View.INVISIBLE);

        this.bigTitle=(TextView)findViewById(R.id.big_title);
        this.coverImage=(ImageView)findViewById(R.id.cover_image);
        this.littleTitle=(TextView)findViewById(R.id.main_textview_title);
        this.description=(TextView)findViewById(R.id.description);
        this.bigImage=(ImageView)findViewById(R.id.main_imageview_placeholder);
        this.underTitle=(TextView)findViewById(R.id.under_title);

        prepareData();

    }

    private void prepareData() {
        Bundle extras=getIntent().getExtras();
        if(extras!=null){
            this.gameId=extras.getInt("GAME ID");
            DBQuery dbQuery=new DBQuery();
            Game game;
            GameDetail gameDetail;
            Boxart boxart;
            List<Fanart> fanart;
            game=dbQuery.getGameFromId(this.gameId);
            gameDetail=dbQuery.getGameDetailFromId(this.gameId);
            boxart=dbQuery.getBoxArtFromGame(game);
            fanart=dbQuery.getFanartFromGame(game);

            this.bigTitle.setText(game.getGameTitle());
            this.littleTitle.setText(game.getGameTitle());
            this.description.setText(gameDetail.getOverView());
            this.underTitle.setText(gameDetail.getPlatform());

            Glide.with(this).load("http://thegamesdb.net/banners/"+boxart.getThumb()).into(this.coverImage);
            if(fanart.size()>0)
            Glide.with(this).load("http://thegamesdb.net/banners/"+fanart.get(0).getOriginalFanart().toString()).into(this.bigImage);





            System.out.println("cliccato su --------->"+this.gameId);
        }else
            System.out.println("NULLAAAAAAAAAA");
    }




    private void bindActivity() {
        mToolbar        = (Toolbar) findViewById(R.id.main_toolbar);
        mTitle          = (TextView) findViewById(R.id.main_textview_title);
        mTitleContainer = (LinearLayout) findViewById(R.id.main_linearlayout_title);
        mAppBarLayout   = (AppBarLayout) findViewById(R.id.main_appbar);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
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
