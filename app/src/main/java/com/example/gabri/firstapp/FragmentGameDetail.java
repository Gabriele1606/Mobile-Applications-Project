package com.example.gabri.firstapp;

import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.content.Context;
import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.widget.Toolbar;
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


public class FragmentGameDetail extends android.support.v4.app.Fragment
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
    private TextView gameYear;
    private TextView titleInDetail;
    private TextView platformInDetail;
    private TextView coopInDetail;
    private TextView playerInDetail;
    private TextView publisherInDetail;
    private TextView developerInDetail;
    private View viewRoot;
    private Context mContext;
    private LayoutInflater inflater;
    private YoutubePlayerFragment youtube;




    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                            Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.viewRoot=inflater.inflate(R.layout.fragment_game_detail,container,false);
        this.mContext=container.getContext();
        this.inflater=inflater;
        bindActivity();

        mAppBarLayout.addOnOffsetChangedListener(this);

        mToolbar.inflateMenu(R.menu.menu_main);
        startAlphaAnimation(mTitle, 0, View.INVISIBLE);

        this.bigTitle=(TextView)this.viewRoot.findViewById(R.id.big_title);
        this.coverImage=(ImageView)this.viewRoot.findViewById(R.id.cover_image);
        this.littleTitle=(TextView)this.viewRoot.findViewById(R.id.main_textview_title);
        this.description=(TextView)this.viewRoot.findViewById(R.id.description);
        this.bigImage=(ImageView)this.viewRoot.findViewById(R.id.main_imageview_placeholder);
        this.underTitle=(TextView)this.viewRoot.findViewById(R.id.under_title);
        this.titleInDetail=(TextView)this.viewRoot.findViewById(R.id.title_in_detail_from_db);
        this.platformInDetail=(TextView)this.viewRoot.findViewById(R.id.platform_in_detail_from_db);
        this.coopInDetail=(TextView)this.viewRoot.findViewById(R.id.coop_in_detail_from_db);
        this.playerInDetail=(TextView)this.viewRoot.findViewById(R.id.player_in_detail_from_db);
        this.publisherInDetail=(TextView)this.viewRoot.findViewById(R.id.publisher_in_detail_from_db);
        this.developerInDetail=(TextView)this.viewRoot.findViewById(R.id.developer_in_detail_from_db);

        prepareData();

        //to make possible to have multiple scroll nested
        //setNestedScroll();

    return this.viewRoot;

    }

    private void setContex(Context mContext){
        this.mContext=mContext;
    }

    private void setNestedScroll() {
        NestedScrollView parentScroll= (NestedScrollView) this.viewRoot.findViewById(R.id.parent_scroll);
        ScrollView childScroll=(ScrollView)this.viewRoot.findViewById(R.id.child_scroll);
        parentScroll.setOnTouchListener(new View.OnTouchListener() {

            public boolean onTouch(View v, MotionEvent event) {
                v.findViewById(R.id.child_scroll).getParent().requestDisallowInterceptTouchEvent(false);
                return false;
            }
        });
        childScroll.setOnTouchListener(new View.OnTouchListener() {

            public boolean onTouch(View v, MotionEvent event)
            {
                // Disallow the touch request for parent scroll on touch of child view
                v.getParent().requestDisallowInterceptTouchEvent(true);
                return false;
            }
        });
    }


    private void prepareData() {
        int paramPassed=getArguments().getInt("GAME ID");
        if(paramPassed!=0){
            this.gameId=paramPassed;
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
            if(gameDetail!=null) {
                this.description.setText(gameDetail.getOverView());
                this.underTitle.setText(gameDetail.getPlatform());
            }

            Glide.with(this).load("http://thegamesdb.net/banners/"+boxart.getThumb()).into(this.coverImage);
            if(fanart.size()>0)
            Glide.with(this).load("http://thegamesdb.net/banners/"+fanart.get(0).getOriginalFanart().toString()).into(this.bigImage);

            //Details section
            if(game.getGameTitle()!=null) {
                this.titleInDetail.setText(game.getGameTitle());
            }
            if(game.getPlatform()!=null) {
                this.platformInDetail.setText(game.getPlatform());
            }
            if(gameDetail.getCoop()!=null) {
                this.coopInDetail.setText(gameDetail.getCoop());
            }
            if(gameDetail.getPlayers()!=null) {
                this.playerInDetail.setText(gameDetail.getPlayers());
            }
            if(gameDetail.getPublisher()!=null) {
                this.publisherInDetail.setText(gameDetail.getPublisher());
            }
            if(gameDetail.getDeveloper()!=null) {
                this.developerInDetail.setText(gameDetail.getDeveloper());
            }
            if(gameDetail.getYoutubeLink()==null){
                gameDetail.setYoutubeLink("null");
            }





                this.youtube = new YoutubePlayerFragment();
                Bundle bundle=new Bundle();
                bundle.putString("LINK", gameDetail.getYoutubeLink());
                youtube.setArguments(bundle);
               /* Activity activity = (Activity) mContext;
                activity.getFragmentManager().beginTransaction().replace(R.id.homepage,youtube).addToBackStack(null).commit();*/

                android.support.v4.app.FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                fragmentManager.beginTransaction().replace(R.id.youtube_view, youtube).commit();

            LinearLayout listView= (LinearLayout) this.viewRoot.findViewById(R.id.comment_section);
            List<Comment> comments=new ArrayList<Comment>();
            comments.add(new Comment("Gabbo94","Il gioco risulta essere molto figo,potebbe esssere sicuramente migliorato ma comunque mi piace molto",1));
            comments.add(new Comment("SimoDe","yamme ya",3));
            comments.add(new Comment("RikyStream","few feqf ew fqwef  qefefewfq fqewf fqewffqf frefqwfqf143rxf25h h3cth6 2hc ",5));
            comments.add(new Comment("RikyStream","few feqf ew fqwef  qefefewfq fqewf fqewffqf frefqwfqf143rxf25h h3cth6 2hc ",2));
            comments.add(new Comment("Gabbo94","Il gioco risulta essere molto figo,potebbe esssere sicuramente migliorato ma comunque mi piace molto",1));
            comments.add(new Comment("SimoDe","yamme ya",3));
            comments.add(new Comment("RikyStream","few feqf ew fqwef  qefefewfq fqewf fqewffqf frefqwfqf143rxf25h h3cth6 2hc ",5));
            comments.add(new Comment("RikyStream","few feqf ew fqwef  qefefewfq fqewf fqewffqf frefqwfqf143rxf25h h3cth6 2hc ",2));
            CommentAdapter commentAdapter= new CommentAdapter(this.mContext, R.layout.comment_row,comments);

            for(int i=0;i<commentAdapter.getCount();i++){
                View item= commentAdapter.getView(i,null,null);
                listView.addView(item);
            }

        }else
            System.out.println("NULLAAAAAAAAAA");
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
        System.out.println("GAME DETAIL DISTRUTTO -------------");
        if(this.youtube!=null)
            this.youtube.onDestroyView();
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
