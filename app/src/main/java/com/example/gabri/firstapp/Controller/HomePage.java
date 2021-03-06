package com.example.gabri.firstapp.Controller;

import android.Manifest;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.constraint.ConstraintLayout;
import android.support.constraint.ConstraintSet;
import android.support.design.widget.FloatingActionButton;
import android.support.transition.TransitionManager;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.support.v4.graphics.drawable.RoundedBitmapDrawable;
import android.support.v4.graphics.drawable.RoundedBitmapDrawableFactory;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.Registry;
import com.bumptech.glide.annotation.GlideModule;
import com.bumptech.glide.module.AppGlideModule;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.BitmapImageViewTarget;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.transition.Transition;
import com.example.gabri.firstapp.Adapter.CoverFlowAdapter;
import com.example.gabri.firstapp.FragmentMap;
import com.example.gabri.firstapp.FragmentPageGames;
import com.example.gabri.firstapp.FragmentProfile;
import com.example.gabri.firstapp.FragmentReadLater;
import com.example.gabri.firstapp.FragmentSearch;
import com.example.gabri.firstapp.FragmentWishList;
import com.example.gabri.firstapp.GameDetailXML;
import com.example.gabri.firstapp.GameEntity;
import com.example.gabri.firstapp.GameXML;
import com.example.gabri.firstapp.Model.Data;
import com.example.gabri.firstapp.Model.GameCover;
import com.example.gabri.firstapp.Model.TabFragment;
import com.example.gabri.firstapp.R;
import com.firebase.ui.auth.AuthUI;
import com.firebase.ui.storage.images.FirebaseImageLoader;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.mxn.soul.flowingdrawer_core.ElasticDrawer;
import com.mxn.soul.flowingdrawer_core.FlowingDrawer;
import com.raizlabs.android.dbflow.config.FlowManager;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;


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
    StorageReference storageReference;
    boolean isTwoPanes;
    private final int PICK_IMAGE_REQUEST = 71;
    private Uri filePath;
    FirebaseStorage storage;

    public static final String GAME_LIST = "Game";
    public static final String PROFILE_LIST = "Profile";
    public static final String NEWS_LIST = "News";
    public static final String MAP_LIST = "Map";
    public static final String SEARCH_LIST = "Search";
    public static final String HOME_LIST = "Home";


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

        //Take user image from FireBase
        final ImageView userPhoto = (ImageView) findViewById(R.id.user_image_3);
        StorageReference storageReference = FirebaseStorage.getInstance().getReference();
        StorageReference child = storageReference.child("images/" + Data.getUser().getId());
        final Context context=this;
        storageReference.child("images/" + Data.getUser().getId()).getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                System.out.println("URI :" + uri.toString());
                if (context!=null) {
                    Glide.with(context).asBitmap().load(uri).apply(RequestOptions.circleCropTransform()).into(new SimpleTarget<Bitmap>() {
                        @Override
                        public void onResourceReady(@NonNull Bitmap resource, @Nullable Transition<? super Bitmap> transition) {
                            if (context != null) {
                                RoundedBitmapDrawable circularBitmapDrawable = RoundedBitmapDrawableFactory.create(getResources(), addBorder(resource, context));
                                circularBitmapDrawable.setCircular(true);
                                userPhoto.setImageDrawable(circularBitmapDrawable);
                            }
                        }
                    });
                }
            }
        });


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
                selectedMenu(NEWS_LIST);
            }

        });

        TextView textHome = (TextView) findViewById(R.id.text_Home);
        textHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                selectedMenu(HOME_LIST);
            }
        });

        TextView textviewProfile = (TextView) findViewById(R.id.text_Profile);
        textviewProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                selectedMenu(PROFILE_LIST);
            }
        });


        TextView textViewGames = (TextView) findViewById(R.id.text_Games);
        textViewGames.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                selectedMenu(GAME_LIST);
            }
        });


        TextView textViewMap = (TextView) findViewById(R.id.text_Map);
        textViewMap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                selectedMenu(MAP_LIST);
            }


        });


        TextView textViewSearch = (TextView) findViewById(R.id.text_Search);
        textViewSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                selectedMenu(SEARCH_LIST);
            }
        });

        final HomePage homePage = this;

        TextView logoutButton = (TextView) findViewById(R.id.logout_button);
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
            return;
        }
        Data.getData().setLocation(locationManager.getLastKnownLocation(bestProvider));
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0, 0, new LocationListener() {
            @Override
            public void onLocationChanged(Location location) {
                System.out.println("NETWORK INFO Changed" + "Current location: " + location.getLatitude() + " " + location.getLongitude());
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
                System.out.println("GPS INFO Changed" + "Current location: " + location.getLatitude() + " " + location.getLongitude());
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

    private void selectedMenu(String selectedMenu) {
        removeOthersFragments(selectedMenu);
        boolean TWOPANELS = Data.getData().getHomePageActivity().getResources().getBoolean(R.bool.has_two_panes);
        Fragment fragment=null;
        switch (selectedMenu){
            case NEWS_LIST:
                fragment=getSupportFragmentManager().findFragmentByTag("Newslist");
                break;
            case GAME_LIST:
                fragment=getSupportFragmentManager().findFragmentByTag("Gamelist");
                break;
            case PROFILE_LIST:
                fragment=getSupportFragmentManager().findFragmentByTag("Profilelayout");
                break;
            case MAP_LIST:
                fragment=getSupportFragmentManager().findFragmentByTag("Maplist");
                break;
            case SEARCH_LIST:
                fragment=getSupportFragmentManager().findFragmentByTag("Searchlist");
                break;
            default:

        }
        if (fragment == null) {
            HighlightSection(selectedMenu);
            loadFragment(selectedMenu);
            if (!TWOPANELS) {
                FlowingDrawer mDrawer = (FlowingDrawer) findViewById(R.id.flowingdrawer);
                mDrawer.closeMenu(true);
            }
        }
    }

    private void loadFragment(String selectedMenu) {
        FragmentTransaction transaction=null;
        switch (selectedMenu){
            case NEWS_LIST:
                FragmentReadLater fragmentReadLater = new FragmentReadLater();
                transaction = Data.getData().getHomePageActivity().getSupportFragmentManager().beginTransaction().replace(R.id.mainframeLayout, fragmentReadLater, selectedMenu);
                break;
            case GAME_LIST:
                FragmentWishList fragmentWishList= new FragmentWishList();
                transaction = Data.getData().getHomePageActivity().getSupportFragmentManager().beginTransaction().replace(R.id.mainframeLayout, fragmentWishList, selectedMenu);
                break;
            case PROFILE_LIST:
                FragmentProfile fragmentProfile= new FragmentProfile();
                transaction = Data.getData().getHomePageActivity().getSupportFragmentManager().beginTransaction().replace(R.id.mainframeLayout, fragmentProfile, selectedMenu);
                break;
            case MAP_LIST:
                FragmentMap fragmentMap= new FragmentMap();
                transaction = Data.getData().getHomePageActivity().getSupportFragmentManager().beginTransaction().replace(R.id.mainframeLayout, fragmentMap, selectedMenu);
                break;
            case SEARCH_LIST:
                FragmentSearch fragmentSearch= new FragmentSearch();
                transaction = Data.getData().getHomePageActivity().getSupportFragmentManager().beginTransaction().replace(R.id.mainframeLayout, fragmentSearch, selectedMenu);
                break;
            default:
        }
        if (transaction != null) {
            transaction.addToBackStack("TABLAYOUT");
            transaction.commit();
        }
    }


    private void removeOthersFragments(String selectedMenu) {
        Fragment fragmentNews = getSupportFragmentManager().findFragmentByTag(NEWS_LIST);
        Fragment fragmentSearch = getSupportFragmentManager().findFragmentByTag(SEARCH_LIST);
        Fragment fragmentGame = getSupportFragmentManager().findFragmentByTag(SEARCH_LIST);
        Fragment fragmentMap = getSupportFragmentManager().findFragmentByTag(MAP_LIST);
        Fragment fragmentProfile = getSupportFragmentManager().findFragmentByTag(PROFILE_LIST);
        if (selectedMenu != SEARCH_LIST && fragmentSearch != null) {
            getSupportFragmentManager().beginTransaction().remove(fragmentSearch).commit();
            getSupportFragmentManager().popBackStack();
        }
        if (selectedMenu != PROFILE_LIST && fragmentProfile != null) {
            getSupportFragmentManager().beginTransaction().remove(fragmentProfile).commit();
            getSupportFragmentManager().popBackStack();
        }

        if (selectedMenu != NEWS_LIST && fragmentNews != null) {
            getSupportFragmentManager().beginTransaction().remove(fragmentNews).commit();
            getSupportFragmentManager().popBackStack();
        }
        if (selectedMenu != GAME_LIST && fragmentGame != null) {
            getSupportFragmentManager().beginTransaction().remove(fragmentGame).commit();
            getSupportFragmentManager().popBackStack();
        }
        if (selectedMenu != MAP_LIST && fragmentMap != null) {
            getSupportFragmentManager().beginTransaction().remove(fragmentMap).commit();
            getSupportFragmentManager().popBackStack();
        }
        if (selectedMenu==HOME_LIST){
            getSupportFragmentManager().popBackStack("TABLAYOUT",FragmentManager.POP_BACK_STACK_INCLUSIVE);
        }
    }



    public void HighlightSection(String name) {
        Fragment fragmentNews = getSupportFragmentManager().findFragmentByTag("Newslist");
        Fragment fragmentSearch = getSupportFragmentManager().findFragmentByTag("Searchlist");
        Fragment fragmentGame = getSupportFragmentManager().findFragmentByTag("Gamelist");
        Fragment fragmentMap = getSupportFragmentManager().findFragmentByTag("Maplist");
        Fragment fragmentProfile = getSupportFragmentManager().findFragmentByTag("Profile");
        TextView textNews = findViewById(R.id.text_News);
        TextView textGames = findViewById(R.id.text_Games);
        TextView textMap = findViewById(R.id.text_Map);
        TextView textProfile = findViewById(R.id.text_Profile);
        TextView textHome= findViewById(R.id.text_Home);
        TextView textSearch= findViewById(R.id.text_Search);

        int highlight = getResources().getColor(R.color.definitiveText);
        int dark = getResources().getColor(R.color.transparent);

        if (name==SEARCH_LIST)
            textSearch.setBackgroundColor(highlight);
        else
            textSearch.setBackgroundColor(dark);
        if (name==HOME_LIST)
            textHome.setBackgroundColor(highlight);
        else
            textHome.setBackgroundColor(dark);
        if (name==PROFILE_LIST)
            textProfile.setBackgroundColor(highlight);
        else
            textProfile.setBackgroundColor(dark);

        if (name==NEWS_LIST)
            textNews.setBackgroundColor(highlight);
        else
            textNews.setBackgroundColor(dark);
        if (name==GAME_LIST)
            textGames.setBackgroundColor(highlight);
        else
            textGames.setBackgroundColor(dark);
        if (name==MAP_LIST)
            textMap.setBackgroundColor(highlight);
        else
            textMap.setBackgroundColor(dark);

    }

    public void logout(){
        Intent login= new Intent(this,Login.class);
        startActivity(login);
        finish();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        super.onActivityResult(requestCode, resultCode, data);

        System.out.println("REQUEST CODE: "+requestCode);
        if(requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK
                && data != null && data.getData() != null )
        {
            filePath = data.getData();
            try {
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), filePath);
                final ImageView imageProfile=findViewById(R.id.user_profile_photo);
                ByteArrayOutputStream stream = new ByteArrayOutputStream();
                bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);
                if (imageProfile!=null) {
                    Glide.with(this).asBitmap().load(stream.toByteArray()).apply(RequestOptions.circleCropTransform()).into(new SimpleTarget<Bitmap>() {
                        @Override
                        public void onResourceReady(@NonNull Bitmap resource, @Nullable Transition<? super Bitmap> transition) {
                            RoundedBitmapDrawable circularBitmapDrawable = RoundedBitmapDrawableFactory.create(imageProfile.getContext().getResources(),  addBorder(resource, imageProfile.getContext()));
                            circularBitmapDrawable.setCircular(true);
                            imageProfile.setImageDrawable(circularBitmapDrawable);
                        }
                    });
                    imageProfile.setBackgroundColor(getResources().getColor(R.color.transparent));
                    storage = FirebaseStorage.getInstance();
                    storageReference = storage.getReference();
                    uploadImage();
                }
            }
            catch (IOException e)
            {
                e.printStackTrace();
            }

            //Take user image from FireBase
            Bitmap bitmap = null;
            try {
                bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), filePath);
                final ImageView imageProfile=findViewById(R.id.user_image_3);
                ByteArrayOutputStream stream = new ByteArrayOutputStream();
                bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);
                if (imageProfile!=null) {
                    Glide.with(this).asBitmap().load(stream.toByteArray()).apply(RequestOptions.circleCropTransform()).into(new SimpleTarget<Bitmap>() {
                        @Override
                        public void onResourceReady(@NonNull Bitmap resource, @Nullable Transition<? super Bitmap> transition) {
                            RoundedBitmapDrawable circularBitmapDrawable = RoundedBitmapDrawableFactory.create(imageProfile.getContext().getResources(), addBorder(resource, imageProfile.getContext()));
                            circularBitmapDrawable.setCircular(true);
                            imageProfile.setImageDrawable(circularBitmapDrawable);
                        }
                    });
                    imageProfile.setBackgroundColor(getResources().getColor(R.color.transparent));
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private static Bitmap addBorder(Bitmap resource, Context context) {
        int w = resource.getWidth();
        int h = resource.getHeight();
        int radius = Math.min(h / 2, w / 2);
        Bitmap output = Bitmap.createBitmap(w + 8, h + 8, Bitmap.Config.ARGB_8888);
        Paint p = new Paint();
        p.setAntiAlias(true);
        Canvas c = new Canvas(output);
        c.drawARGB(0, 0, 0, 0);
        p.setStyle(Paint.Style.FILL);
        c.drawCircle((w / 2) + 4, (h / 2) + 4, radius, p);
        p.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));
        c.drawBitmap(resource, 4, 4, p);
        p.setXfermode(null);
        p.setStyle(Paint.Style.STROKE);
        p.setColor(ContextCompat.getColor(context, R.color.white));
        p.setStrokeWidth(15);
        c.drawCircle((w / 2) + 4, (h / 2) + 4, radius, p);
        return output;
    }

    private void uploadImage() {

        if(filePath != null)
        {
            final ProgressDialog progressDialog = new ProgressDialog(this);
            progressDialog.setTitle("Uploading...");
            progressDialog.show();

            StorageReference ref = storageReference.child("images/"+ Data.getUser().getId());
            ref.putFile(filePath)
                    .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            progressDialog.dismiss();
                            Toast.makeText(HomePage.this, "Uploaded", Toast.LENGTH_SHORT).show();
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            progressDialog.dismiss();
                            Toast.makeText(HomePage.this, "Failed "+e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    })
                    .addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
                            double progress = (100.0*taskSnapshot.getBytesTransferred()/taskSnapshot
                                    .getTotalByteCount());
                            progressDialog.setMessage("Uploaded "+(int)progress+"%");
                        }
                    });
        }
    }

    public void loadImage(){
        final Context context=this;
        final ImageView imageProfile=findViewById(R.id.user_profile_photo);
        StorageReference storageReference = FirebaseStorage.getInstance().getReference();
        storageReference.child("images/"+ Data.getUser().getId()).getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
            System.out.println("URI :" +uri.toString());
            Glide.with(context).asBitmap().load(uri).apply(RequestOptions.circleCropTransform()).into(new SimpleTarget<Bitmap>() {
                    @Override
                    public void onResourceReady(@NonNull Bitmap resource, @Nullable Transition<? super Bitmap> transition) {
                        RoundedBitmapDrawable circularBitmapDrawable = RoundedBitmapDrawableFactory.create(getResources(),  addBorder(resource, context));
                        circularBitmapDrawable.setCircular(true);
                        imageProfile.setImageDrawable(circularBitmapDrawable);
                    }
                });
            }
        });




        /*Glide.with(this).using(new FirebaseImageLoader())
                .load(storageReference)
                .into(imageView);*/
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

    private int convertDpToPx(int dp){
        return Math.round(dp * (getResources().getDisplayMetrics().xdpi / DisplayMetrics.DENSITY_DEFAULT));
    }

}
