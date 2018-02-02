package com.example.gabri.firstapp.Model;

import android.location.Location;
import android.location.LocationManager;
import android.support.v4.view.ViewPager;

import com.example.gabri.firstapp.Adapter.SampleFragmentPagerAdapter;
import com.example.gabri.firstapp.Controller.APIManager;
import com.example.gabri.firstapp.Controller.HomePage;
import com.example.gabri.firstapp.GameDetailXML;
import com.example.gabri.firstapp.GameXML;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import retrofit2.Call;

/**
 * Created by simon on 03/12/2017.
 */

public class Data {
    private static List<Object> instance = new ArrayList<Object>();
    private Map<String,Boolean> initialized = new HashMap<>();
    private static Data data= new Data();
    private static List<Platform> listPlatform= new ArrayList<Platform>();
    private List<Call<GameXML>> callToGame=new ArrayList<Call<GameXML>>();
    private List<Call<GameDetailXML>> callToGameDetail=new ArrayList<Call<GameDetailXML>>();
    private List<APIManager.MyAsyncTask> myAsyncTaskList= new ArrayList<APIManager.MyAsyncTask>();


    private SampleFragmentPagerAdapter setSampleFragmentPagerAdapter;
    private ViewPager homePageViewPager;

    private LocationManager locationManager;

    private HomePage homePageActivity;
    private static User user;
    private static String idUserForRemoteDb;
    private Location location;


    private Data(){
    }
    public static List<Object> getInstance(){
        return instance;
    }
    public static Data getData(){
    return data;
    }

    public boolean isInitialized(String id){
        if (initialized.get(id)!=null){
            return initialized.get(id);
        }
        return false;
    }
    public void setInitialized(String id){
        initialized.put(id,new Boolean(true));
    }

    public synchronized List<Platform> getListPlatform() {
        return listPlatform;
    }

    public List<Call<GameDetailXML>> getCallToGameDetail() {
        return callToGameDetail;
    }
    public List<APIManager.MyAsyncTask> getMyAsyncTaskList() {
        return myAsyncTaskList;
    }
    public List<Call<GameXML>> getCallToGame() {
        return callToGame;
    }
    public void addTask(APIManager.MyAsyncTask task){
        myAsyncTaskList.add(task);
    }

    public void setSampleFragmentPagerAdapter(SampleFragmentPagerAdapter sampleFragmentPagerAdapter){
        this.setSampleFragmentPagerAdapter= sampleFragmentPagerAdapter;
    }
    public SampleFragmentPagerAdapter getSetSampleFragmentPagerAdapter() {
        return setSampleFragmentPagerAdapter;
    }

    public void setHomePageViewPager(ViewPager homePageViewPager) {
        this.homePageViewPager = homePageViewPager;
    }
    public ViewPager getHomePageViewPager() {
        return homePageViewPager;
    }

    public void setHomePageActivity(HomePage homePageActivity) {
        this.homePageActivity = homePageActivity;
    }
    public HomePage getHomePageActivity() {
        return homePageActivity;
    }

    public static User getUser(){return user;}

    public static void setUser(User newUser){user=newUser;}

    public static String getIdUserForRemoteDb(){return idUserForRemoteDb;}

    public static void setIdUserForRemoteDb(String id){idUserForRemoteDb=id;}

    public LocationManager getLocationManager(){
        return locationManager;
    }
    public void setLocationManager(LocationManager locationManager){
        this.locationManager=locationManager;
    }

    public void setLocation(Location location) {
        this.location = location;
    }

    public Location getLocation() {
        return this.location;
    }
}
