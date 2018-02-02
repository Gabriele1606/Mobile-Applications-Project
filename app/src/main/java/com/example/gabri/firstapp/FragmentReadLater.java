
package com.example.gabri.firstapp;

import android.annotation.SuppressLint;
import android.app.Fragment;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.drawable.Drawable;
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
import android.widget.ListView;
import android.widget.Toast;
import android.widget.Toolbar;

import com.example.gabri.firstapp.Adapter.ReadLaterAdapter;
import com.example.gabri.firstapp.Adapter.WishListAdapter;
import com.example.gabri.firstapp.Controller.TimerLoad;
import com.example.gabri.firstapp.Model.Data;
import com.example.gabri.firstapp.Model.Game;
import com.example.gabri.firstapp.Model.RSSFeed;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Timer;

import static com.facebook.FacebookSdk.getApplicationContext;

/**
 * Created by Gabri on 24/12/17.
 */

public class FragmentReadLater extends android.support.v4.app.Fragment  {

    private View view;
    private List<RSSFeed> newsList=new ArrayList<RSSFeed>();
    private RecyclerView recyclerView;
    private ReadLaterAdapter mAdapter;
    private DBQuery dbQuery;
    private ListView listOfNewsToRead;
    private Context mContext;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        view= inflater.inflate(R.layout.fragment_read_later, container, false);

        recyclerView = (RecyclerView) view.findViewById(R.id.recycler_view);
        mAdapter = new ReadLaterAdapter(newsList);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(mAdapter);

        setSwipe();

        DatabaseReference databaseReference= FirebaseDatabase.getInstance().getReference();
        databaseReference.child("news").child(Data.getIdUserForRemoteDb()).addListenerForSingleValueEvent(
                new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        newsList.clear();
                        for(DataSnapshot idSnapshot: dataSnapshot.getChildren()) {
                            RSSFeed tmp =new RSSFeed();
                            tmp.setDescription((String) idSnapshot.child("description").getValue());
                            tmp.setGuid((String) idSnapshot.child("guid").getValue());
                            tmp.setImageLink((String) idSnapshot.child("imageLink").getValue());
                            tmp.setPubdate((String) idSnapshot.child("pubdate").getValue());
                            tmp.setTitle((String) idSnapshot.child("title").getValue());
                            tmp.setIdForFirebase((String)idSnapshot.child("idForFirebase").getValue());

                            newsList.add(tmp);

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


    public void setSwipe(){

        ItemTouchHelper.SimpleCallback simpleItemTouchCallback = new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT) {

            @Override
            public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(RecyclerView.ViewHolder viewHolder, int swipeDir) {
                int position = viewHolder.getAdapterPosition();
                RSSFeed tmp=newsList.get(position);
                Toast.makeText(view.getContext(),"News removed from your read more list", Toast.LENGTH_SHORT).show();
                DatabaseReference databaseWishGame= FirebaseDatabase.getInstance().getReference("news");
                databaseWishGame.child(Data.getIdUserForRemoteDb()).child(tmp.getIdForFirebase()).removeValue();
                newsList.remove(position);
                mAdapter.notifyItemRemoved(position);


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



}

