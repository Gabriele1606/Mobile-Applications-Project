
package com.example.gabri.firstapp.Controller;

import android.animation.Animator;
import android.animation.ValueAnimator;
import android.content.Intent;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.ViewAnimationUtils;
import android.view.Window;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.gabri.firstapp.Model.Data;
import com.example.gabri.firstapp.Model.Game;
import com.example.gabri.firstapp.Model.User;
import com.example.gabri.firstapp.R;
import com.firebase.ui.auth.AuthUI;
import com.firebase.ui.auth.IdpResponse;
import com.firebase.ui.auth.ResultCodes;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Login extends AppCompatActivity {

    DatabaseReference databaseUsers;
    private ImageView fab;
    private ImageView backImage;
    private RelativeLayout layoutMain;
    private RelativeLayout layoutButton;
    private RelativeLayout layoutContent;
    private LinearLayout buttonsContentLayout;
    private TextView counter;
    private boolean isOpen=false;

    private static final int RC_SIGN_IN = 123;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.login);
        Window w= getWindow();
        layoutMain=(RelativeLayout) findViewById(R.id.layoutmain);
        layoutButton=(RelativeLayout) findViewById(R.id.layoutbutton);
        layoutContent=(RelativeLayout) findViewById(R.id.layoutcontent);
        buttonsContentLayout=(LinearLayout)findViewById(R.id.buttons_content_layout);
        counter=(TextView)findViewById(R.id.counter); 
        backImage=(ImageView)findViewById(R.id.back_image);
        Glide.with(this).load(R.drawable.loginback).into(backImage);

        fab=(ImageView)findViewById(R.id.fab);

        clickOnButton();
        setCounter();

        databaseUsers= FirebaseDatabase.getInstance().getReference("users");

        // in Activity's onCreate() for instance
        w.getDecorView().setSystemUiVisibility(
                View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                        | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                        | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                        | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                        | View.SYSTEM_UI_FLAG_FULLSCREEN
                        | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY);

        FirebaseAuth auth = FirebaseAuth.getInstance();
        if (auth.getCurrentUser() != null) {
            login(auth.getCurrentUser());
        } else {
            // not signed in
        }

        ImageView mailImageView=(ImageView)findViewById(R.id.mail_img);
        mailImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                List<AuthUI.IdpConfig> providers = Arrays.asList(
                        new AuthUI.IdpConfig.Builder(AuthUI.EMAIL_PROVIDER).build());
                startActivityForResult(
                        AuthUI.getInstance()
                                .createSignInIntentBuilder()
                                .setAvailableProviders(providers)
                                .setTheme(R.style.LoginTheme)
                                .build(),
                        RC_SIGN_IN);
            }
        });

        ImageView facebookImageView= (ImageView)findViewById(R.id.facebook_img);
        facebookImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                List<AuthUI.IdpConfig> providers = Arrays.asList(
                        new AuthUI.IdpConfig.Builder(AuthUI.FACEBOOK_PROVIDER).build());
                startActivityForResult(
                        AuthUI.getInstance()
                                .createSignInIntentBuilder()
                                .setAvailableProviders(providers)
                                .setTheme(R.style.LoginTheme)
                                .build(),
                        RC_SIGN_IN);
            }
        });



    }

    private void setCounter() {
        final ValueAnimator animator=ValueAnimator.ofInt(0,48954);
        animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator valueAnimator) {
                int currentValue=(Integer)animator.getAnimatedValue();
                counter.setText(Integer.toString(currentValue));
            }
        });
        animator.setDuration(3000);
        animator.start();
    }

    private void clickOnButton() {
        fab.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
            @Override
            public void onClick(View view) {
                viewMenu();
            }
        });
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    private void viewMenu(){

        if(!isOpen){

            int X = layoutMain.getRight();
            int Y = layoutMain.getBottom();
            int startRadius = 0;
            int endRadius = (int) Math.hypot(layoutMain.getWidth(),layoutMain.getHeight());

            Animator anim = ViewAnimationUtils.createCircularReveal(layoutButton, X, Y, startRadius, endRadius);
            layoutButton.setVisibility(View.VISIBLE);
            anim.start();

            isOpen=true;


        }else{
            int X = layoutMain.getRight();
            int Y = layoutMain.getBottom();
            int startRadius = Math.max(layoutMain.getWidth(),layoutMain.getHeight());
            int endRadius =0;

            Animator anim = ViewAnimationUtils.createCircularReveal(layoutButton, X, Y, startRadius, endRadius);
            anim.addListener(new Animator.AnimatorListener() {
                @Override
                public void onAnimationStart(Animator animator) {

                }

                @Override
                public void onAnimationEnd(Animator animator) {
                    layoutButton.setVisibility(View.GONE);

                }

                @Override
                public void onAnimationCancel(Animator animator) {

                }

                @Override
                public void onAnimationRepeat(Animator animator) {

                }
            });

            anim.start();
            isOpen=false;

        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == RC_SIGN_IN) {
            IdpResponse response = IdpResponse.fromResultIntent(data);

            if (resultCode == ResultCodes.OK) {
                // Successfully signed in
                FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                if(user!=null)
                    login(user);
            } else {
                // Sign in failed, check response for error code
                // ...
            }
        }
    }


    public void login(FirebaseUser firebaseUser){

        //Fingiamo che il login avvenga con successo e che mi faggio restituire tutti i dati dal database

        List<String> temp=new ArrayList<String>();
        temp.add("a");
        temp.add("b");

        String id=firebaseUser.getUid();
        Data.setIdUserForRemoteDb(id);
       /* Map<String, Object> map = new HashMap<>();
        map.put(id+"/nameId1", "John");
        map.put(id+"/nameId2", "Tim");
        map.put(id+"/nameId3", "Sam");*/


        User user= new User("123a","Gabbo94","gabri.bressan@gmail.com","provaPassword","http://myimage.it");
        user= new User(firebaseUser.getUid(),firebaseUser.getDisplayName(),firebaseUser.getEmail(),"","");
        Data.setUser(user);

        databaseUsers.child(id).setValue(user);
        //databaseUsers.updateChildren(map);
        //questa sotto è un alternativa forse non funzionale
        //databaseUsers= FirebaseDatabase.getInstance().getReference("users/"+id+"/game");
        //databaseUsers.setValue(temp);



        DatabaseReference databaseWishGame= FirebaseDatabase.getInstance().getReference("game").child(id);

        Intent homePage=new Intent(this, HomePage.class);

        startActivity(homePage);
        finish();

    }
}
