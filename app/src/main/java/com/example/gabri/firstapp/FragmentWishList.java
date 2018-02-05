
package com.example.gabri.firstapp;

import android.app.Fragment;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.Toolbar;

import com.example.gabri.firstapp.Adapter.ReadLaterAdapter;
import com.example.gabri.firstapp.Adapter.WishListAdapter;
import com.example.gabri.firstapp.Controller.HomePage;
import com.example.gabri.firstapp.Controller.TimerLoad;
import com.example.gabri.firstapp.Model.Data;
import com.example.gabri.firstapp.Model.Game;
import com.facebook.CallbackManager;
import com.facebook.FacebookSdk;
import com.facebook.share.model.ShareLinkContent;
import com.facebook.share.widget.ShareDialog;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;

import static com.facebook.FacebookSdk.getApplicationContext;

/**
 * Created by Gabri on 24/12/17.
 */

public class FragmentWishList extends android.support.v4.app.Fragment {

    private View view;
    private List<Game> gameList=new ArrayList<Game>();
    private RecyclerView recyclerView;
    private WishListAdapter mAdapter;
    private List<Integer> idGame;
    private DBQuery dbQuery;
    private TextView emtpyListText;
    private CallbackManager callbackManager;
    private ShareDialog shareDialog;



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        view= inflater.inflate(R.layout.fragment_wish_list, container, false);
        this.recyclerView = (RecyclerView) view.findViewById(R.id.recycler_view_wishlist);
        mAdapter = new WishListAdapter(gameList);
        mAdapter.setFragmentWishList(this);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(mAdapter);

        this.emtpyListText=(TextView) view.findViewById(R.id.wish_list_empty);

        this.idGame= new ArrayList<Integer>();
        this.dbQuery=new DBQuery();


        setSwipe();

        DatabaseReference databaseReference= FirebaseDatabase.getInstance().getReference();
        databaseReference.child("game").child(Data.getIdUserForRemoteDb()).addListenerForSingleValueEvent(
                new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        idGame.clear();
                        gameList.clear();
                        for(DataSnapshot idSnapshot: dataSnapshot.getChildren()) {
                            idGame.add((int)(long) idSnapshot.getValue());
                        }
                        for(int i=0;i<idGame.size();i++) {
                            gameList.add(dbQuery.getGameFromId(idGame.get(i)));
                        }
                        if(gameList.size()==0){
                         recyclerView.setVisibility(View.GONE);
                         emtpyListText.setVisibility(View.VISIBLE);
                        }else{
                            emtpyListText.setVisibility(View.GONE);
                            recyclerView.setVisibility(View.VISIBLE);
                        }
                        mAdapter.notifyDataSetChanged();
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                }
        );


        return view;
    }

    public void setFacebookShare(Game game) {
        callbackManager = CallbackManager.Factory.create();
        shareDialog = new ShareDialog(this);

        if (ShareDialog.canShow(ShareLinkContent.class)) {
            ShareLinkContent linkContent = new ShareLinkContent.Builder()
                    .setQuote("I'm looking for this GAME. Can you help Me?")
                    .setContentUrl(Uri.parse("http://thegamesdb.net/game/"+game.getId()))
                    .build();
            shareDialog.show(linkContent);
        }
    }


    private void setSwipe() {
        ItemTouchHelper.SimpleCallback simpleItemTouchCallback = new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT) {

            @Override
            public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(RecyclerView.ViewHolder viewHolder, int swipeDir) {
                int position = viewHolder.getAdapterPosition();
                Game tmp=gameList.get(position);
                Toast.makeText(view.getContext(),"Game removed from your Wish List", Toast.LENGTH_SHORT).show();
                DatabaseReference databaseWishGame= FirebaseDatabase.getInstance().getReference("game");
                databaseWishGame.child(Data.getIdUserForRemoteDb()).child(Integer.toString(tmp.getId())).removeValue();
                gameList.remove(position);
                mAdapter.notifyItemRemoved(position);
                if(gameList.size()==0){
                    recyclerView.setVisibility(View.GONE);
                    emtpyListText.setVisibility(View.VISIBLE);
                }else{
                    emtpyListText.setVisibility(View.GONE);
                    recyclerView.setVisibility(View.VISIBLE);
                }


            }
            public static final float ALPHA_FULL = 1.0f;

            @Override
            public void onChildDraw(Canvas c, RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, float dX, float dY, int actionState, boolean isCurrentlyActive) {
                if (actionState == ItemTouchHelper.ACTION_STATE_SWIPE) {

                    View itemView = viewHolder.itemView;

                    Paint p = new Paint();
                    Bitmap icon;

                    //color : right side (swiping towards left)
                    p.setARGB(210, 255, 20, 20);

                    c.drawRect((float) itemView.getRight() + dX, (float) itemView.getTop(),
                            (float) itemView.getRight(), (float) itemView.getBottom(), p);

                    //icon : left side (swiping towards right)
                    icon = BitmapFactory.decodeResource(getApplicationContext().getResources(), R.drawable.garbage);
                    c.drawBitmap(icon,
                            (float) itemView.getRight() - convertDpToPx(16) - icon.getWidth(),
                            (float) itemView.getTop() + ((float) itemView.getBottom() - (float) itemView.getTop() - icon.getHeight())/2,
                            p);


                    // Fade out the view when it is swiped out of the parent
                    final float alpha = ALPHA_FULL - Math.abs(dX) / (float) viewHolder.itemView.getWidth();
                    viewHolder.itemView.setAlpha(alpha);
                    viewHolder.itemView.setTranslationX(dX);

                } else {
                    super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive);
                }
            }
        };

        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(simpleItemTouchCallback);
        itemTouchHelper.attachToRecyclerView(recyclerView);

    }

    private int convertDpToPx(int dp){
        return Math.round(dp * (getResources().getDisplayMetrics().xdpi / DisplayMetrics.DENSITY_DEFAULT));
    }

    @Override
    public void onResume() {
        if (getActivity()instanceof HomePage) {
            HomePage activity = (HomePage) getActivity();
            activity.HighlightSection("Game");
        }
        super.onResume();
    }
}
