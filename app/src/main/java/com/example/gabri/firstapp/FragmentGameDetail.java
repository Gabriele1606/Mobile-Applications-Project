
package com.example.gabri.firstapp;

import android.content.Context;
import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AlphaAnimation;
import android.widget.Button;
import android.widget.FrameLayout;
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
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

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
    private TextView console;
    private TextView pubdate;
    private View viewRoot;
    private Context mContext;
    private LayoutInflater inflater;
    private YoutubePlayerFragment youtube;
    private  Boolean isFavorite=false;
    private ImageView heart;
    private Game game;
    private int numberOfStarComment;
    private CommentAdapter commentAdapter;




    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {


        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);

        this.viewRoot=inflater.inflate(R.layout.fragment_game_detail,container,false);
        this.mContext=container.getContext();
        this.inflater=inflater;
        this.numberOfStarComment=0;
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
        this.pubdate=(TextView)this.viewRoot.findViewById(R.id.pubdate_3);
        this.console=(TextView)this.viewRoot.findViewById(R.id.console);
        this.heart=(ImageView)this.viewRoot.findViewById(R.id.favorite_game);

        prepareData();
        setGameRate();
        takeCommentFromFirebase();
        verifyIfFavorite();
        setFavoriteClick();
        setNestedScroll();
        setClickOnCommentStars();
        setCommentSection();
        return this.viewRoot;

    }

    private void setGameRate(){

        final int[] numberOfEvaluation = {0};
        final int[] totalRate = {0};

        DatabaseReference databaseComment= FirebaseDatabase.getInstance().getReference();
        databaseComment.child("comments").child(String.valueOf(gameId)).addListenerForSingleValueEvent(
                new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        for(DataSnapshot idSnapshot: dataSnapshot.getChildren()) {
                            totalRate[0] +=(int)(long)idSnapshot.child("rate").getValue();
                            numberOfEvaluation[0]++;
                        }
                    if(numberOfEvaluation[0]==0)
                        turnOnRateStar(0);
                        else
                        turnOnRateStar((int)(totalRate[0]/numberOfEvaluation[0]));
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                }
        );

    }
     private void turnOnRateStar(int numberOfStar){
         ImageView starOne=(ImageView)this.viewRoot.findViewById(R.id.star_one);
         ImageView starTwo=(ImageView)this.viewRoot.findViewById(R.id.star_two);
         ImageView starThree=(ImageView)this.viewRoot.findViewById(R.id.star_three);
         ImageView starFour=(ImageView)this.viewRoot.findViewById(R.id.star_four);
         ImageView starFive=(ImageView)this.viewRoot.findViewById(R.id.star_five);

         switch (numberOfStar){
             case 0:
                 starOne.setImageResource(R.drawable.staroff);
                 starTwo.setImageResource(R.drawable.staroff);
                 starThree.setImageResource(R.drawable.staroff);
                 starFour.setImageResource(R.drawable.staroff);
                 starFive.setImageResource(R.drawable.staroff);
                 break;
             case 1:
                 starOne.setImageResource(R.drawable.staron);
                 starTwo.setImageResource(R.drawable.staroff);
                 starThree.setImageResource(R.drawable.staroff);
                 starFour.setImageResource(R.drawable.staroff);
                 starFive.setImageResource(R.drawable.staroff);
                 break;
             case 2:
                 starOne.setImageResource(R.drawable.staron);
                 starTwo.setImageResource(R.drawable.staron);
                 starThree.setImageResource(R.drawable.staroff);
                 starFour.setImageResource(R.drawable.staroff);
                 starFive.setImageResource(R.drawable.staroff);
                 break;
             case 3:
                 starOne.setImageResource(R.drawable.staron);
                 starTwo.setImageResource(R.drawable.staron);
                 starThree.setImageResource(R.drawable.staron);
                 starFour.setImageResource(R.drawable.staroff);
                 starFive.setImageResource(R.drawable.staroff);
                 break;
             case 4:
                 starOne.setImageResource(R.drawable.staron);
                 starTwo.setImageResource(R.drawable.staron);
                 starThree.setImageResource(R.drawable.staron);
                 starFour.setImageResource(R.drawable.staron);
                 starFive.setImageResource(R.drawable.staroff);
                 break;
             case 5:
                 starOne.setImageResource(R.drawable.staron);
                 starTwo.setImageResource(R.drawable.staron);
                 starThree.setImageResource(R.drawable.staron);
                 starFour.setImageResource(R.drawable.staron);
                 starFive.setImageResource(R.drawable.staron);
                 break;
         }

     }

    private void takeCommentFromFirebase() {
        //final ListView listView= (ListView) this.viewRoot.findViewById(R.id.comment_section);
        final LinearLayout listView= (LinearLayout) this.viewRoot.findViewById(R.id.comment_section);
        final ScrollView commentSectionScroll=(ScrollView)this.viewRoot.findViewById(R.id.comment_section_scroll);
        final ImageView imageNoComment=(ImageView) this.viewRoot.findViewById(R.id.image_no_comments);
        listView.removeAllViews();

        DatabaseReference databaseComment= FirebaseDatabase.getInstance().getReference();
        final List<Comment> comments=new ArrayList<Comment>();
        databaseComment.child("comments").child(String.valueOf(gameId)).addListenerForSingleValueEvent(
                new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        comments.clear();
                        for(DataSnapshot idSnapshot: dataSnapshot.getChildren()) {
                            Comment tmp=new Comment();
                            tmp.setUsername((String)idSnapshot.child("username").getValue());
                            tmp.setRate((int)(long)idSnapshot.child("rate").getValue());
                            tmp.setReview((String)idSnapshot.child("review").getValue());
                            comments.add(tmp);
                        }

                       commentAdapter=new CommentAdapter(mContext,R.layout.wish_list_row,comments);

                        if(commentAdapter.getCount()==0){
                            imageNoComment.setVisibility(View.VISIBLE);
                            commentSectionScroll.setVisibility(View.GONE);
                            imageNoComment.setImageResource(R.drawable.nocommentsavailable);
                        }else {

                            for (int i = 0; i < commentAdapter.getCount(); i++) {
                                View item = commentAdapter.getView(i, null, null);
                                listView.addView(item);
                            }
                            imageNoComment.setVisibility(View.GONE);
                            commentSectionScroll.setVisibility(View.VISIBLE);
                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                }
        );




        CommentAdapter commentAdapter= new CommentAdapter(this.mContext, R.layout.comment_row,comments);

        for(int i=0;i<commentAdapter.getCount();i++){
            View item= commentAdapter.getView(i,null,null);
            listView.addView(item);
        }


    }

    private void setCommentSection() {

        Button sendComment=(Button)this.viewRoot.findViewById(R.id.send_comment);
        sendComment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String review;
                TextView commentView=(TextView)viewRoot.findViewById(R.id.insert_comment);
                review=commentView.getText().toString();

                Comment comment=new Comment(Data.getUser().getUsername(),review,numberOfStarComment);
                if(!review.equals("")) {
                    DatabaseReference databaseComment = FirebaseDatabase.getInstance().getReference("comments").child(String.valueOf(gameId));
                    String id = databaseComment.push().getKey();
                    databaseComment.child(id).setValue(comment);
                    Toast.makeText(getActivity(), "Your comment has been inserted!", Toast.LENGTH_SHORT).show();
                    resetCommentSection();
                    takeCommentFromFirebase();
                    setGameRate();
                }
                else{
                    Toast.makeText(getActivity(),"ATTENTION! Comment box cannot be empty", Toast.LENGTH_SHORT).show();
                }
            }
        });

    }

    private void resetCommentSection(){

        ImageView commentStarOne=(ImageView)this.viewRoot.findViewById(R.id.comment_star_one);
        ImageView commentStarTwo=(ImageView)this.viewRoot.findViewById(R.id.comment_star_two);
        ImageView commentStarThree=(ImageView)this.viewRoot.findViewById(R.id.comment_star_three);
        ImageView commentStarFour=(ImageView)this.viewRoot.findViewById(R.id.comment_star_four);
        ImageView commentStarFive=(ImageView)this.viewRoot.findViewById(R.id.comment_star_five);
        commentStarOne.setImageResource(R.drawable.staroff);
        commentStarTwo.setImageResource(R.drawable.staroff);
        commentStarThree.setImageResource(R.drawable.staroff);
        commentStarFour.setImageResource(R.drawable.staroff);
        commentStarFive.setImageResource(R.drawable.staroff);

        TextView commentView=(TextView)viewRoot.findViewById(R.id.insert_comment);
        commentView.setText("");
        commentView.setHint("Insert your comment...");

    }

    private void setClickOnCommentStars() {
        final ImageView commentStarOne=(ImageView)this.viewRoot.findViewById(R.id.comment_star_one);
        final ImageView commentStarTwo=(ImageView)this.viewRoot.findViewById(R.id.comment_star_two);
        final ImageView commentStarThree=(ImageView)this.viewRoot.findViewById(R.id.comment_star_three);
        final ImageView commentStarFour=(ImageView)this.viewRoot.findViewById(R.id.comment_star_four);
        final ImageView commentStarFive=(ImageView)this.viewRoot.findViewById(R.id.comment_star_five);

        commentStarOne.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                commentStarOne.setImageResource(R.drawable.staron);
                commentStarTwo.setImageResource(R.drawable.staroff);
                commentStarThree.setImageResource(R.drawable.staroff);
                commentStarFour.setImageResource(R.drawable.staroff);
                commentStarFive.setImageResource(R.drawable.staroff);
                numberOfStarComment=1;
            }
        });

        commentStarTwo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                commentStarOne.setImageResource(R.drawable.staron);
                commentStarTwo.setImageResource(R.drawable.staron);
                commentStarThree.setImageResource(R.drawable.staroff);
                commentStarFour.setImageResource(R.drawable.staroff);
                commentStarFive.setImageResource(R.drawable.staroff);
                numberOfStarComment=2;
            }
        });

        commentStarThree.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                commentStarOne.setImageResource(R.drawable.staron);
                commentStarTwo.setImageResource(R.drawable.staron);
                commentStarThree.setImageResource(R.drawable.staron);
                commentStarFour.setImageResource(R.drawable.staroff);
                commentStarFive.setImageResource(R.drawable.staroff);
                numberOfStarComment=3;
            }
        });

        commentStarFour.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                commentStarOne.setImageResource(R.drawable.staron);
                commentStarTwo.setImageResource(R.drawable.staron);
                commentStarThree.setImageResource(R.drawable.staron);
                commentStarFour.setImageResource(R.drawable.staron);
                commentStarFive.setImageResource(R.drawable.staroff);
                numberOfStarComment=4;
            }
        });

        commentStarFive.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                commentStarOne.setImageResource(R.drawable.staron);
                commentStarTwo.setImageResource(R.drawable.staron);
                commentStarThree.setImageResource(R.drawable.staron);
                commentStarFour.setImageResource(R.drawable.staron);
                commentStarFive.setImageResource(R.drawable.staron);
                numberOfStarComment=5;
            }
        });

    }


    private void verifyIfFavorite() {
        DatabaseReference databaseReference= FirebaseDatabase.getInstance().getReference();
        databaseReference.addListenerForSingleValueEvent(
                new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        if(dataSnapshot.hasChild("game"+"/"+Data.getIdUserForRemoteDb()+"/"+String.valueOf(gameId))){
                            isFavorite=true;
                            heart.setImageResource(R.drawable.hearton);
                        }
                        else {
                            isFavorite = false;
                            heart.setImageResource(R.drawable.heartoff);
                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                }
        );



    }

    private void setFavoriteClick() {
        this.heart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(isFavorite==false){
                    heart.setImageResource(R.drawable.hearton);
                    addGameToWishList(gameId);
                    isFavorite=true;
                    Toast.makeText(getActivity(),"Game added to your wish list", Toast.LENGTH_SHORT).show();
                }else{
                    heart.setImageResource(R.drawable.heartoff);
                    removeGameToWishList(gameId);
                    isFavorite=false;
                    Toast.makeText(getActivity(),"Game removed from your wish list", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void setContex(Context mContext){
        this.mContext=mContext;
    }

    private void setNestedScroll() {
        NestedScrollView parentScroll= (NestedScrollView) this.viewRoot.findViewById(R.id.parent_scroll);
        ScrollView childScroll=(ScrollView)this.viewRoot.findViewById(R.id.comment_section_scroll);
        parentScroll.setOnTouchListener(new View.OnTouchListener() {

            public boolean onTouch(View v, MotionEvent event) {
                v.findViewById(R.id.comment_section_scroll).getParent().requestDisallowInterceptTouchEvent(false);
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
        Game game = (Game)getArguments().getSerializable("REALGAMEOBJECT");
        if(game!=null) {
            int paramPassed = game.getId();
            if (paramPassed != 0) {
                this.gameId = paramPassed;
                DBQuery dbQuery = new DBQuery();
                GameDetail gameDetail;
                Boxart boxart;
                List<Fanart> fanart;
                gameDetail = dbQuery.getGameDetailFromId(this.gameId);
                boxart = dbQuery.getBoxArtFromGame(game);
                fanart = dbQuery.getFanartFromGame(game);

                this.bigTitle.setText(game.getGameTitle());
                this.littleTitle.setText(game.getGameTitle());
                if (gameDetail != null) {
                    this.description.setText(gameDetail.getOverView());
                    this.underTitle.setText(gameDetail.getPlatform());


                    Glide.with(this).load("http://thegamesdb.net/banners/" + boxart.getThumb()).into(this.coverImage);
                    if (fanart.size() > 0) {
                        Glide.with(this).load("http://thegamesdb.net/banners/" + fanart.get(0).getOriginalFanart().toString()).into(this.bigImage);
                    } else {
                        Glide.with(this).load(R.drawable.defaultdbgame).into(this.bigImage);
                    }

                    //Details section

                    if (game.getGameTitle() != null) {
                        this.titleInDetail.setText(game.getGameTitle());
                    }
                    if (game.getPlatform() != null) {
                        this.platformInDetail.setText(game.getPlatform());
                    }
                    if (gameDetail.getCoop() != null) {
                        this.coopInDetail.setText(gameDetail.getCoop());
                    }
                    if (gameDetail.getPlayers() != null) {
                        this.playerInDetail.setText(gameDetail.getPlayers());
                    }
                    if (gameDetail.getPublisher() != null) {
                        this.publisherInDetail.setText(gameDetail.getPublisher());
                    }
                    if (gameDetail.getDeveloper() != null) {
                        this.developerInDetail.setText(gameDetail.getDeveloper());
                    }
                    if (!game.getReleaseDate().equals("null")) {
                        this.pubdate.setText("PubDate: " + game.getReleaseDate());
                    }
                    if (!game.getPlatform().equals("null")) {
                        this.console.setText("Console: " + game.getPlatform());
                    }
                    if (gameDetail.getYoutubeLink() == null) {
                        gameDetail.setYoutubeLink("null");
                    }


                    YoutubePlayerFragment myFragment = YoutubePlayerFragment.newInstance(gameDetail.getYoutubeLink());
                    getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.youtube_view, myFragment).commit();


                }
            }
        }
    }




    private void bindActivity() {
        mToolbar        = (Toolbar) this.viewRoot.findViewById(R.id.main_toolbar);
        mTitle          = (TextView) this.viewRoot.findViewById(R.id.main_textview_title);
        mTitleContainer = (LinearLayout) this.viewRoot.findViewById(R.id.main_linearlayout_title);
        mAppBarLayout   = (AppBarLayout) this.viewRoot.findViewById(R.id.main_appbar);
    }







    private void addGameToWishList(int gameId) {
        DatabaseReference databaseWishGame= FirebaseDatabase.getInstance().getReference("game").child(Data.getIdUserForRemoteDb());
        String id = databaseWishGame.push().getKey();
        databaseWishGame.child(Integer.toString(gameId)).setValue(gameId);
    }

    private void removeGameToWishList(int gameId) {
        DatabaseReference databaseWishGame= FirebaseDatabase.getInstance().getReference("game");
        databaseWishGame.child(Data.getIdUserForRemoteDb()).child(Integer.toString(gameId)).removeValue();

    }


    @Override
    public void onDestroyView() {
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
