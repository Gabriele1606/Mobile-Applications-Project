package com.example.gabri.firstapp.Controller;

import android.os.Bundle;
import android.support.constraint.ConstraintLayout;
import android.support.constraint.ConstraintSet;
import android.support.design.widget.FloatingActionButton;
import android.support.transition.TransitionManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;

import com.example.gabri.firstapp.Adapter.CoverFlowAdapter;
import com.example.gabri.firstapp.GameDetailXML;
import com.example.gabri.firstapp.GameEntity;
import com.example.gabri.firstapp.GameXML;
import com.example.gabri.firstapp.Model.Data;
import com.example.gabri.firstapp.Model.GameCover;
import com.example.gabri.firstapp.Model.TabFragment;
import com.example.gabri.firstapp.R;
import com.mxn.soul.flowingdrawer_core.ElasticDrawer;
import com.mxn.soul.flowingdrawer_core.FlowingDrawer;
import com.raizlabs.android.dbflow.config.FlowManager;

import java.util.ArrayList;
import java.util.List;


//import it.moondroid.coverflow.components.ui.containers.FeatureCoverFlow;
import retrofit2.Call;


public class HomePage extends AppCompatActivity {

    //private FeatureCoverFlow mCoverFlow;
    private CoverFlowAdapter mAdapter;
    private ArrayList<GameEntity> mData = new ArrayList<>(0);
    private RecyclerView recyclerView;
    private RecyclerView.Adapter adapter;
    private List<GameCover> gameCoverList;
    private RecyclerView recyclerViewTwo;
    private RecyclerView.Adapter adapterTwo;
    private List<GameCover> gameCoverListTwo;
    List<Object> listObject;
    APIManager apiManager;
    private ConstraintSet originalConstraint= new ConstraintSet();
    private ConstraintSet toRightConstraint= new ConstraintSet();

    boolean isTwoPanes;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.home);

        Data.getData().setHomePageActivity(this);

        isTwoPanes = getResources().getBoolean(R.bool.has_two_panes);

        FlowingDrawer mDrawer = (FlowingDrawer) findViewById(R.id.drawerlayout);
        mDrawer.setTouchMode(ElasticDrawer.TOUCH_MODE_BEZEL);
        mDrawer.setOnDrawerStateChangeListener(new ElasticDrawer.OnDrawerStateChangeListener() {
            @Override
            public void onDrawerStateChange(int oldState, int newState) {
                if (newState == ElasticDrawer.STATE_CLOSED) {
                    Log.i("MainActivity", "Drawer STATE_CLOSED");
                }
            }

            @Override
            public void onDrawerSlide(float openRatio, int offsetPixels) {
                Log.i("MainActivity", "openRatio=" + openRatio + " ,offsetPixels=" + offsetPixels);
            }
        });


        FlowManager.init(this);
        //To RESET DATABASE ---- PAY ATTENTION
        //FlowManager.getDatabase(AppDatabase.class).reset(this);



        listObject= Data.getInstance();
        collapseDetailGame();
        addFloatingButton();
       if (savedInstanceState == null )
        {
            // Display the fragment
            /*getSupportFragmentManager().beginTransaction()
                .add(R.id.mainframeLayout, new TabFragment()).commit();*/
            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.mainframeLayout, new TabFragment(), "TABLAYOUT").commit();

        }else{
           /* FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
            transaction.replace(R.id.mainframeLayout, new TabFragment());
            transaction.addToBackStack(null);
            transaction.commit();*/
        }





/*
        mData.add(new GameEntity(R.drawable.image_1, R.string.title1));
        mData.add(new GameEntity(R.drawable.image_2, R.string.title2));
        mData.add(new GameEntity(R.drawable.image_3, R.string.title3));
        mData.add(new GameEntity(R.drawable.image_4, R.string.title4));
        mData.add(new GameEntity(R.drawable.image_5, R.string.title4));
        mData.add(new GameEntity(R.drawable.image_6, R.string.title4));



        Animation in = AnimationUtils.loadAnimation(this, R.anim.slide_in_top);
        Animation out = AnimationUtils.loadAnimation(this, R.anim.slide_out_bottom);


        mAdapter = new CoverFlowAdapter(this);
        mAdapter.setData(mData);
        mCoverFlow = (FeatureCoverFlow) findViewById(R.id.coverflow);
        mCoverFlow.setAdapter(mAdapter);*/








    }

    private void addFloatingButton() {
        if (isTwoPanes) {
            FloatingActionButton floatingActionButton = (FloatingActionButton) findViewById(R.id.floatingButton);
            floatingActionButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    collapseDetailGame();
                    //Data.getData().getSetSampleFragmentPagerAdapter().notifyDataSetChanged();
                }
            });
        }
    }

/*
    public static int getDrawable(Context context, String name)
    {
        Assert.assertNotNull(context);
        Assert.assertNotNull(name);

        return context.getResources().getIdentifier(name,
                "drawable", context.getPackageName());
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_coverflow_activity, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }


*/


    @Override
    protected void onStop() {
        System.out.println("STOP CALLED");
        for ( APIManager.MyAsyncTask mt:
             Data.getData().getMyAsyncTaskList()) {
            mt.cancel(true);
        }
        for (Call<GameXML> call:
            Data.getData().getCallToGame() ){
            call.cancel();
        }
        for (Call<GameDetailXML> call:
                Data.getData().getCallToGameDetail() ){
            call.cancel();
        }
        super.onStop();
    }

    public void collapseDetailGame(){
        moveGuideline(1.0f);
    }
    public void enlargeDetailGame(){
        moveGuideline(0.6f);
    }

    public void collapseWishList(){
        moveGuideline(1.0f);
    }
    public void enlargeWishList(){
        moveGuideline(0.6f);
    }

    public void moveGuideline(float percent){
        if (isTwoPanes){
            ConstraintLayout constraintLayout= findViewById(R.id.homepage);
            ConstraintSet constraintset= new ConstraintSet();
            constraintset.clone(constraintLayout);
            constraintset.setGuidelinePercent(R.id.guideline,percent);
            TransitionManager.beginDelayedTransition(constraintLayout);
            constraintset.applyTo(constraintLayout);
        }
    }




}
