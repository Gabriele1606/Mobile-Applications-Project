package com.example.gabri.firstapp;

import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.location.Location;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.constraint.ConstraintLayout;
import android.support.constraint.ConstraintSet;
import android.support.constraint.Guideline;
import android.support.transition.TransitionManager;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;

import com.example.gabri.firstapp.Controller.HomePage;
import com.example.gabri.firstapp.Model.Data;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MapStyleOptions;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import jp.wasabeef.blurry.Blurry;

import static android.content.Context.LOCATION_SERVICE;

/**
 * Created by simon on 31/01/2018.
 */

public class FragmentMap extends Fragment implements OnMapReadyCallback, LocationListener,
        GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener {
    private static final long LOCATION_REFRESH_TIME = 1;
    private static final float LOCATION_REFRESH_DISTANCE = 1;
    //ThingsAdapter adapter;
    LocationRequest mLocationRequest;
    FragmentActivity listener;
    private int PROXIMITY_RADIUS = 10000;
    Location mLastLocation;
    Marker mCurrLocationMarker;
    double latitude;
    double longitude;
    private GoogleMap mMap;
    GoogleApiClient mGoogleApiClient;
    GetNearbyPlacesData getNearbyPlacesData;
    FragmentMap fragmentMap;
    SupportMapFragment mapFragment;
    private View view;
    private Context context;

    // This event fires 1st, before creation of fragment or any views
    // The onAttach method is called when the Fragment instance is associated with an Activity.
    // This does not mean the Activity is fully initialized.
    @Override
    public void onAttach(Context context) {
        this.context=context;
        super.onAttach(context);
        fragmentMap = this;
        if (context instanceof Activity) {
            this.listener = (FragmentActivity) context;
        }
    }

    // This event fires 2nd, before views are created for the fragment
    // The onCreate method is called when the Fragment instance is being created, or re-created.
    // Use onCreate for any standard setup that does not require the activity to be fully created
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    // The onCreateView method is called when Fragment should create its View object hierarchy,
    // either dynamically or via XML layout inflation.
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup parent, Bundle savedInstanceState) {
        View view;
        boolean TWOPANELS = getActivity().getResources().getBoolean(R.bool.has_two_panes);
        if (TWOPANELS)
            view = inflater.inflate(R.layout.fragment_map_extended, parent, false);
        else
            view = inflater.inflate(R.layout.fragment_map, parent, false);

        mapFragment = (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.el_fragment_Map);
        mapFragment.getMapAsync(this);

        return view;
    }

    // This event is triggered soon after onCreateView().
    // onViewCreated() is only called if the view returned from onCreateView() is non-null.
    // Any view setup should occur here.  E.g., view lookups and attaching view listeners.
    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        this.view=view;
        //lv.setAdapter(adapter);
    }

    // This method is called when the fragment is no longer connected to the Activity
    // Any references saved in onAttach should be nulled out here to prevent memory leaks.
    @Override
    public void onDetach() {
        super.onDetach();
        this.listener = null;
    }

    // This method is called after the parent Activity's onCreate() method has completed.
    // Accessing the view hierarchy of the parent activity must be done in the onActivityCreated.
    // At this point, it is safe to search for activity View objects by their ID, for example.
    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        googleMap.setOnMapLoadedCallback(new GoogleMap.OnMapLoadedCallback() {
            @Override
            public void onMapLoaded() {
                fragmentMap.Blur();
                fragmentMap.moveGuideline(0.61f);
            }
        });
        try {
            boolean success = mMap.setMapStyle(
                    MapStyleOptions.loadRawResourceStyle(
                            getActivity(), R.raw.maps_style));

            if (!success) {
                Log.e("FRAGMENT MAP", "Style parsing failed.");
            }
        } catch (Resources.NotFoundException e) {
            Log.e("FRAGMENT MAP", "Can't find style. Error: ", e);
        }
        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (ContextCompat.checkSelfPermission(getActivity(),
                    android.Manifest.permission.ACCESS_FINE_LOCATION)
                    == PackageManager.PERMISSION_GRANTED) {
                buildGoogleApiClient();
                mMap.setMyLocationEnabled(true);
                if (Data.getData().getLocation() != null)
                    System.out.println("CURRENT POSITION: " + Data.getData().getLocation().getLatitude() + "," + Data.getData().getLocation().getLongitude());
            }
        } else {
            buildGoogleApiClient();
            mMap.setMyLocationEnabled(true);
        }
        mMap.clear();
        String url;
        if (Data.getData().getLocation() != null) {
            url = getUrl(Data.getData().getLocation().getLatitude(), Data.getData().getLocation().getLongitude(), "negozio%20di%20videogiochi");
        } else {
            url = getUrl(45.464233f, 9.190061f, "negozio%20di%20videogiochi");
        }
        Object[] DataTransfer = new Object[3];
        DataTransfer[0] = mMap;
        DataTransfer[1] = url;
        DataTransfer[2] = this;
        Log.d("onClick", url);
        getNearbyPlacesData = new GetNearbyPlacesData();
        getNearbyPlacesData.setListener(this);
        getNearbyPlacesData.execute(DataTransfer);


        Toast.makeText(getActivity(), "Nearby Game Shops", Toast.LENGTH_LONG).show();

    }

    // The onCreateView method is called when Fragment should create its View object hierarchy,
    // either dynamically or via XML layout inflation.
    protected synchronized void buildGoogleApiClient() {
        mGoogleApiClient = new GoogleApiClient.Builder(getActivity())
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build();
        mGoogleApiClient.connect();
    }

    @Override
    public void onStop() {
        if (getNearbyPlacesData != null)
            getNearbyPlacesData.cancel(true);
        System.out.println("STOPPED FRAGMENT MAP");
        super.onStop();
    }

    private String getUrl(double latitude, double longitude, String keyword) {

        StringBuilder googlePlacesUrl = new StringBuilder("https://maps.googleapis.com/maps/api/place/nearbysearch/json?");
        googlePlacesUrl.append("location=" + latitude + "," + longitude);
        googlePlacesUrl.append("&radius=" + PROXIMITY_RADIUS);
        googlePlacesUrl.append("&keyword=" + keyword);
        googlePlacesUrl.append("&sensor=true");
        googlePlacesUrl.append("&key=" + "AIzaSyCcK1biS4ls781wEfWHB7Xw6_QlVoqgX54");
        Log.d("getUrl", googlePlacesUrl.toString());
        return (googlePlacesUrl.toString());
    }


    @Override
    public void onLocationChanged(Location location) {
        Log.d("onLocationChanged", "entered");

        mLastLocation = location;
        if (mCurrLocationMarker != null) {
            mCurrLocationMarker.remove();
        }

        //Place current location marker
        latitude = location.getLatitude();
        longitude = location.getLongitude();
        LatLng latLng = new LatLng(location.getLatitude(), location.getLongitude());
        MarkerOptions markerOptions = new MarkerOptions();
        markerOptions.position(latLng);
        markerOptions.title("Current Position");
        markerOptions.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_MAGENTA));
        mCurrLocationMarker = mMap.addMarker(markerOptions);

        //move map camera
        mMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));
        mMap.animateCamera(CameraUpdateFactory.zoomTo(11));
        Toast.makeText(getActivity(), "Your Current Location", Toast.LENGTH_LONG).show();

        Log.d("onLocationChanged", String.format("latitude:%.3f longitude:%.3f", latitude, longitude));

        //stop location updates
        if (mGoogleApiClient != null) {
            LocationServices.FusedLocationApi.removeLocationUpdates(mGoogleApiClient, this);
            Log.d("onLocationChanged", "Removing Location Updates");
        }
        Log.d("onLocationChanged", "Exit");
    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {
        mLocationRequest = new LocationRequest();
        mLocationRequest.setInterval(1000);
        mLocationRequest.setFastestInterval(1000);
        mLocationRequest.setPriority(LocationRequest.PRIORITY_BALANCED_POWER_ACCURACY);
        if (ContextCompat.checkSelfPermission(getActivity(),
                android.Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {
            //  LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient, mLocationRequest, this);
        }
    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }

    public void load(final List<HashMap<String, String>> nearbyPlacesList, final ArrayList<Marker> markers) {
        MapListAdapter mapListAdapter = new MapListAdapter(getActivity(), R.id.listMap, nearbyPlacesList);
        ListView listView = (ListView) getActivity().findViewById(R.id.listMap);


        if (mMap != null & listView != null)
            listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                    HashMap<String, String> object = nearbyPlacesList.get(i);
                    double lat = Double.parseDouble(object.get("lat"));
                    double lng = Double.parseDouble(object.get("lng"));
                    LatLng latLng = new LatLng(lat, lng);
                    mMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));
                    mMap.animateCamera(CameraUpdateFactory.zoomTo(11));
                    for (Marker m :
                            markers) {
                        if (m.getPosition().equals(latLng)) {

                            mMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
                                @Override
                                public boolean onMarkerClick(Marker marker) {
                                    return false;
                                }
                            });
                            m.showInfoWindow();
                            mMap.getUiSettings().setMapToolbarEnabled(true);
                        }
                    }

                }
            });
        if (listView != null)
            listView.setAdapter(mapListAdapter);
        //Blur();
    }


    @Override
    public void onResume() {
        if (getActivity() instanceof HomePage) {
            HomePage activity = (HomePage) getActivity();
            activity.HighlightSection("Map");
        }
        super.onResume();
    }

    public void Blur() {
        final ImageView viewById = (ImageView) view.findViewById(R.id.backList);
        mMap.snapshot(new GoogleMap.SnapshotReadyCallback() {
            @Override
            public void onSnapshotReady(Bitmap bitmap) {
                Blurry.with(context)
                        .radius(15)
                        .sampling(8)
                        .color(Color.argb(150, 23, 127, 165))
                        .async()
                        .animate(500).from(bitmap).into(viewById);
            }
        });
    }


    public void moveGuideline(float percent) {
        boolean isTwoPanes = getResources().getBoolean(R.bool.has_two_panes);
        if (!isTwoPanes) {
            ConstraintLayout constraintLayout = getActivity().findViewById(R.id.constraintMap);
            ConstraintSet constraintset = new ConstraintSet();
            constraintset.clone(constraintLayout);
            constraintset.setGuidelinePercent(R.id.guideline_map, percent);
            TransitionManager.beginDelayedTransition(constraintLayout);
            constraintset.applyTo(constraintLayout);
        }
    }

    @Override
    public void onDestroy() {
            if (!(ActivityCompat.checkSelfPermission(getContext(), android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getContext(), android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED)&&mMap != null) {
                mMap.setMyLocationEnabled(false);
            }
        mGoogleApiClient.disconnect();
        try{
            getChildFragmentManager().beginTransaction().remove(mapFragment);

        }catch (Error e){
            System.out.println("DESTROY: Fragment");
        }

        try{
            getNearbyPlacesData.cancel(true);
        }catch (Error e){
            System.out.println("CANCEL: AsyncTask");
        }
        super.onDestroy();
    }
}