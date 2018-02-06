package com.example.gabri.firstapp;

import android.app.ActionBar;
import android.app.Activity;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.gabri.firstapp.Adapter.RecyclerAdapter;
import com.example.gabri.firstapp.Adapter.RecyclerSearch;
import com.example.gabri.firstapp.Adapter.SearchAdapter;
import com.example.gabri.firstapp.Model.Data;
import com.example.gabri.firstapp.Model.Game;
import com.example.gabri.firstapp.Model.RSSFeed;
import com.example.gabri.firstapp.Model.User;
import com.example.gabri.firstapp.View.GameTabFragment;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.zxing.common.StringUtils;

import java.util.ArrayList;
import java.util.List;

import static com.facebook.FacebookSdk.getApplicationContext;

/**
 * Created by Gabri on 04/02/18.
 */

public class FragmentSearch extends Fragment {

    private SearchView searchView;
    private RecyclerView recyclerView;
    private List<Object> listObject;
    RecyclerSearch recyclerSearch;
    private RecyclerView.LayoutManager mLayoutManager;
    //Mandatory Constructor
    public FragmentSearch() {
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        mLayoutManager = new LinearLayoutManager(getApplicationContext());
        View rootView = inflater.inflate(R.layout.fragment_search, container, false);
        searchView = (SearchView) rootView.findViewById(R.id.search);
        setSearchListener();
        recyclerView=(RecyclerView) rootView.findViewById(R.id.recycler_search);
        listObject= new ArrayList<Object>();
        recyclerSearch = new RecyclerSearch(getContext(), listObject);
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(recyclerSearch);

        return rootView;
    }

    private void setSearchListener() {

        this.searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                listObject.clear();
                findGame(query);
                findUserFromFirebase(query);
                searchView.clearFocus();

                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {

                return false;
            }
        });

    }

    public void findGame(String query){
        List<Game>result;
        DBQuery dbQuery=new DBQuery();
        result=dbQuery.getGameListFromName(query);
        for(int i=0;i<result.size();i++)
            System.out.println(result.get(i).getGameTitle());

        listObject.addAll(result);

        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                recyclerSearch.notifyDataSetChanged();
            }
        });
    }


    public void findUserFromFirebase(final String query){
        DatabaseReference databaseReference= FirebaseDatabase.getInstance().getReference();

        databaseReference.child("users").addListenerForSingleValueEvent(
                new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        for(DataSnapshot idSnapshot: dataSnapshot.getChildren()) {

                            String username = (String) idSnapshot.child("username").getValue();
                            System.out.println("USERNAME: "+username);
                            String description;
                            double similarity = similarity(username, query);
                            description = (String) idSnapshot.child("description").getValue();
                            String id = (String) idSnapshot.child("id").getValue();

                            if (similarity>0.3){
                                User user= new User(id,username,"","","");
                                user.setDescription(description);
                                listObject.add(user);
                            }
                        }
                        getActivity().runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                recyclerSearch.notifyDataSetChanged();
                            }
                        });
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                }
        );
    }

    public static double similarity(String s1, String s2) {
        String longer = s1, shorter = s2;
        if (s1.length() < s2.length()) { // longer should always have greater length
            longer = s2; shorter = s1;
        }
        int longerLength = longer.length();
        if (longerLength == 0) { return 1.0; /* both strings are zero length */ }
        return (longerLength - editDistance(longer, shorter)) / (double) longerLength;
    }

    public static int editDistance(String s1, String s2) {
        s1 = s1.toLowerCase();
        s2 = s2.toLowerCase();

        int[] costs = new int[s2.length() + 1];
        for (int i = 0; i <= s1.length(); i++) {
            int lastValue = i;
            for (int j = 0; j <= s2.length(); j++) {
                if (i == 0)
                    costs[j] = j;
                else {
                    if (j > 0) {
                        int newValue = costs[j - 1];
                        if (s1.charAt(i - 1) != s2.charAt(j - 1))
                            newValue = Math.min(Math.min(newValue, lastValue),
                                    costs[j]) + 1;
                        costs[j - 1] = lastValue;
                        lastValue = newValue;
                    }
                }
            }
            if (i > 0)
                costs[s2.length()] = lastValue;
        }
        return costs[s2.length()];
    }

}