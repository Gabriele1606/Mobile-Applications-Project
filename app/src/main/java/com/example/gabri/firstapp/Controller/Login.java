
package com.example.gabri.firstapp.Controller;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;

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

    private static final int RC_SIGN_IN = 123;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login);
        Window w= getWindow();

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
                                .build(),
                        RC_SIGN_IN);
            }
        });



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
