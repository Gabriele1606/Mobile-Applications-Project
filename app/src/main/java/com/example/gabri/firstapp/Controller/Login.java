package com.example.gabri.firstapp.Controller;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.Window;

import com.example.gabri.firstapp.Model.Data;
import com.example.gabri.firstapp.Model.Game;
import com.example.gabri.firstapp.Model.User;
import com.example.gabri.firstapp.R;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Login extends AppCompatActivity {

    DatabaseReference databaseUsers;

    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login);
            Window w= getWindow();
            // in Activity's onCreate() for instance
            w.getDecorView().setSystemUiVisibility(
                    View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                            | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                            | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                            | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                            | View.SYSTEM_UI_FLAG_FULLSCREEN
                            | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY);


    databaseUsers= FirebaseDatabase.getInstance().getReference("users");

    }

    public void registrationForm(View view){
        Intent registrationActivity=new Intent(this, RegistrationForm.class);
        startActivity(registrationActivity);

    }

    public void login(View view){

        //Fingiamo che il login avvenga con successo e che mi faggio restituire tutti i dati dal database

        List<String> temp=new ArrayList<String>();
        temp.add("a");
        temp.add("b");

        String id=databaseUsers.push().getKey();
        Data.setIdUserForRemoteDb(id);
       /* Map<String, Object> map = new HashMap<>();
        map.put(id+"/nameId1", "John");
        map.put(id+"/nameId2", "Tim");
        map.put(id+"/nameId3", "Sam");*/


        User user= new User("123a","Gabbo94","gabri.bressan@gmail.com","provaPassword","http://myimage.it");
        Data.setUser(user);

       databaseUsers.child(id).setValue(user);
        //databaseUsers.updateChildren(map);
        //questa sotto è un alternativa forse non funzionale
        //databaseUsers= FirebaseDatabase.getInstance().getReference("users/"+id+"/game");
        //databaseUsers.setValue(temp);



        DatabaseReference databaseWishGame= FirebaseDatabase.getInstance().getReference("game").child(id);

        Intent homePage=new Intent(this, HomePage.class);
        startActivity(homePage);


    }
}
