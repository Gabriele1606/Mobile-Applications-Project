package com.example.gabri.firstapp.Controller;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.constraint.ConstraintLayout;
import android.support.constraint.ConstraintSet;
import android.support.design.widget.FloatingActionButton;
import android.support.transition.TransitionManager;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.gabri.firstapp.Adapter.CoverFlowAdapter;
import com.example.gabri.firstapp.FragmentMap;
import com.example.gabri.firstapp.FragmentProfile;
import com.example.gabri.firstapp.FragmentReadLater;
import com.example.gabri.firstapp.FragmentWishList;
import com.example.gabri.firstapp.GameDetailXML;
import com.example.gabri.firstapp.GameEntity;
import com.example.gabri.firstapp.GameXML;
import com.example.gabri.firstapp.Model.Data;
import com.example.gabri.firstapp.Model.GameCover;
import com.example.gabri.firstapp.Model.TabFragment;
import com.example.gabri.firstapp.R;
import com.firebase.ui.auth.AuthUI;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
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
    private ConstraintSet originalConstraint = new ConstraintSet();
    private ConstraintSet toRightConstraint = new ConstraintSet();

    boolean isTwoPanes;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.home);

        Data.getData().setHomePageActivity(this);


        isTwoPanes = getResources().getBoolean(R.bool.has_two_panes);
        if (!isTwoPanes) {
            FlowingDrawer mDrawer = (FlowingDrawer) findViewById(R.id.flowingdrawer);
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
        }

        FlowManager.init(this);
        //To RESET DATABASE ---- PAY ATTENTION
        //FlowManager.getDatabase(AppDatabase.class).reset(this);


        listObject = Data.getInstance();
        if (savedInstanceState == null) {
            // Display the fragment
            /*getSupportFragmentManager().beginTransaction()
                .add(R.id.mainframeLayout, new TabFragment()).commit();*/
            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.mainframeLayout, new TabFragment(), "TABLAYOUT").commit();

        } else {

           /* FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
            transaction.replace(R.id.mainframeLayout, new TabFragment());
            transaction.addToBackStack(null);
            transaction.commit();*/
        }
        TextView textView = (TextView) findViewById(R.id.text_News);
        textView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                boolean TWOPANELS = Data.getData().getHomePageActivity().getResources().getBoolean(R.bool.has_two_panes);
                Fragment fragment = getSupportFragmentManager().findFragmentByTag("Newslist");
                Fragment gamelist = getSupportFragmentManager().findFragmentByTag("Gamelist");
                Fragment maplist = getSupportFragmentManager().findFragmentByTag("Maplist");
                if (gamelist!=null) {
                    getSupportFragmentManager().beginTransaction().remove(getSupportFragmentManager().findFragmentByTag("Gamelist")).commitNow();
                    getSupportFragmentManager().popBackStackImmediate();
                }
                if (maplist!=null) {
                    getSupportFragmentManager().beginTransaction().remove(getSupportFragmentManager().findFragmentByTag("Maplist")).commitNow();
                    getSupportFragmentManager().popBackStackImmediate();
                }

                HighlightSection("News");


                    FragmentReadLater fragmentReadLater = new FragmentReadLater();
                    FragmentTransaction transaction = Data.getData().getHomePageActivity().getSupportFragmentManager().beginTransaction().replace(R.id.mainframeLayout, fragmentReadLater, "Newslist");
                    transaction.addToBackStack("TABLAYOUT");
                    transaction.commit();
                    if (!TWOPANELS) {
                        FlowingDrawer mDrawer = (FlowingDrawer) findViewById(R.id.flowingdrawer);
                        mDrawer.closeMenu(true);
                    }
            }
        });

        TextView textviewProfile = (TextView) findViewById(R.id.text_Profile);
        textviewProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                boolean TWOPANELS = Data.getData().getHomePageActivity().getResources().getBoolean(R.bool.has_two_panes);

                Fragment profile = getSupportFragmentManager().findFragmentByTag("Profile");
                Fragment gamelist = getSupportFragmentManager().findFragmentByTag("Gamelist");
                Fragment maplist = getSupportFragmentManager().findFragmentByTag("Maplist");
                Fragment newslist = getSupportFragmentManager().findFragmentByTag("Newslist");
                if (gamelist!=null) {
                    getSupportFragmentManager().beginTransaction().remove(getSupportFragmentManager().findFragmentByTag("Gamelist")).commitNow();
                    getSupportFragmentManager().popBackStackImmediate();
                }
                if (maplist!=null) {
                    getSupportFragmentManager().beginTransaction().remove(getSupportFragmentManager().findFragmentByTag("Maplist")).commitNow();
                    getSupportFragmentManager().popBackStackImmediate();
                }
                if (newslist!=null) {
                    getSupportFragmentManager().beginTransaction().remove(getSupportFragmentManager().findFragmentByTag("Newslist")).commitNow();
                    getSupportFragmentManager().popBackStackImmediate();
                }

                HighlightSection("Profile");

                FragmentProfile fragmentProfile= new FragmentProfile();
                FragmentTransaction transaction = Data.getData().getHomePageActivity().getSupportFragmentManager().beginTransaction().replace(R.id.mainframeLayout, fragmentProfile, "ProfileLayout");
                transaction.addToBackStack("TABLAYOUT");
                transaction.commit();
                if (!TWOPANELS) {
                    FlowingDrawer mDrawer = (FlowingDrawer) findViewById(R.id.flowingdrawer);
                    mDrawer.closeMenu(true);
                }
            }
        });


        TextView textViewGames = (TextView) findViewById(R.id.text_Games);
        textViewGames.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                boolean TWOPANELS = Data.getData().getHomePageActivity().getResources().getBoolean(R.bool.has_two_panes);
                Fragment newslist = getSupportFragmentManager().findFragmentByTag("Newslist");
                Fragment maplist = getSupportFragmentManager().findFragmentByTag("Maplist");
                Fragment fragment = getSupportFragmentManager().findFragmentByTag("Gamelist");
                if (newslist!=null) {
                    getSupportFragmentManager().beginTransaction().remove(getSupportFragmentManager().findFragmentByTag("Newslist")).commitNow();
                    getSupportFragmentManager().popBackStackImmediate();
                }

                if (maplist!=null) {
                    getSupportFragmentManager().beginTransaction().remove(getSupportFragmentManager().findFragmentByTag("Maplist")).commitNow();
                    getSupportFragmentManager().popBackStackImmediate();
                }

                HighlightSection("Game");

                    FragmentWishList fragmentWishList = new FragmentWishList();
                    FragmentTransaction transaction = Data.getData().getHomePageActivity().getSupportFragmentManager().beginTransaction().replace(R.id.mainframeLayout, fragmentWishList, "Gamelist");
                    transaction.addToBackStack("TABLAYOUT");
                    transaction.commit();
                    if (!TWOPANELS) {
                        FlowingDrawer mDrawer = (FlowingDrawer) findViewById(R.id.flowingdrawer);
                        mDrawer.closeMenu(true);
                    }

            }
        });


        TextView textViewMap = (TextView) findViewById(R.id.text_Map);
        textViewMap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                boolean TWOPANELS = Data.getData().getHomePageActivity().getResources().getBoolean(R.bool.has_two_panes);
                Fragment fragment = getSupportFragmentManager().findFragmentByTag("Maplist");
                Fragment newslist = getSupportFragmentManager().findFragmentByTag("Newslist");
                Fragment gamelist = getSupportFragmentManager().findFragmentByTag("Gamelist");
                if (newslist!=null) {
                    getSupportFragmentManager().beginTransaction().remove(getSupportFragmentManager().findFragmentByTag("Newslist")).commitNow();
                    getSupportFragmentManager().popBackStackImmediate();
                }
                if (gamelist!=null)
                    getSupportFragmentManager().beginTransaction().remove(getSupportFragmentManager().findFragmentByTag("Gamelist")).commitNow();
                    getSupportFragmentManager().popBackStackImmediate();

                HighlightSection("Map");

                    FragmentMap fragmentMap = new FragmentMap();
                    FragmentTransaction transaction = Data.getData().getHomePageActivity().getSupportFragmentManager().beginTransaction().replace(R.id.mainframeLayout, fragmentMap, "Maplist");
                    transaction.addToBackStack("TABLAYOUT");
                    transaction.commit();
                    if (!TWOPANELS) {
                        FlowingDrawer mDrawer = (FlowingDrawer) findViewById(R.id.flowingdrawer);
                        mDrawer.closeMenu(true);
                    }
            }
        });

        final HomePage homePage = this;

        Button logoutButton = (Button) findViewById(R.id.logout_button);
        logoutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AuthUI.getInstance()
                        .signOut(homePage)
                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                            public void onComplete(@NonNull Task<Void> task) {
                                //DA COMPLETARE
                                homePage.logout();
                            }
                        });
            }
        });


        LocationManager locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        Criteria criteria = new Criteria();
        criteria.setAccuracy(Criteria.ACCURACY_COARSE);
        criteria.setAltitudeRequired(false);
        criteria.setBearingRequired(false);
        criteria.setCostAllowed(true);
        criteria.setPowerRequirement(Criteria.POWER_LOW);
        String bestProvider = locationManager.getBestProvider(criteria, true);
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        Data.getData().setLocation(locationManager.getLastKnownLocation(bestProvider));
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0, 0, new LocationListener() {
            @Override
            public void onLocationChanged(Location location) {
               System.out.println("NETWORK INFO Changed"+ "Current location: "+location.getLatitude()+ " "+location.getLongitude());
                Data.getData().setLocation(location);
            }

            @Override
            public void onStatusChanged(String s, int i, Bundle bundle) {

            }

            @Override
            public void onProviderEnabled(String s) {

            }

            @Override
            public void onProviderDisabled(String s) {

            }
        });


        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 5000, 10, new LocationListener() {
            @Override
            public void onLocationChanged(Location location) {
                    System.out.println("GPS INFO Changed"+ "Current location: "+location.getLatitude()+ " "+location.getLongitude());
                Data.getData().setLocation(location);
            }

            @Override
            public void onStatusChanged(String s, int i, Bundle bundle) {

            }

            @Override
            public void onProviderEnabled(String s) {

            }

            @Override
            public void onProviderDisabled(String s) {

            }
        });
        Data.getData().setLocationManager(locationManager);


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

    public void HighlightSection(String name) {
        Fragment fragmentNews = getSupportFragmentManager().findFragmentByTag("Newslist");
        Fragment fragmentGame = getSupportFragmentManager().findFragmentByTag("Gamelist");
        Fragment fragmentMap = getSupportFragmentManager().findFragmentByTag("Maplist");
        Fragment fragmentProfile = getSupportFragmentManager().findFragmentByTag("Profile");
        TextView textNews = findViewById(R.id.text_News);
        TextView textGames = findViewById(R.id.text_Games);
        TextView textMap = findViewById(R.id.text_Map);
        TextView textProfile = findViewById(R.id.text_Profile);
        int highlight = getResources().getColor(R.color.definitiveText);
        int dark = getResources().getColor(R.color.definitiveBackground_transparent);

        if (name=="Profile")
            textProfile.setBackgroundColor(highlight);
        else
            textProfile.setBackgroundColor(dark);

        if (name=="News")
            textNews.setBackgroundColor(highlight);
        else
            textNews.setBackgroundColor(dark);
        if (name=="Game")
            textGames.setBackgroundColor(highlight);
        else
            textGames.setBackgroundColor(dark);
        if (name=="Map")
            textMap.setBackgroundColor(highlight);
        else
            textMap.setBackgroundColor(dark);

    }

    public void logout(){
        Intent login= new Intent(this,Login.class);
        startActivity(login);
        finish();
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

}
