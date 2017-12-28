package com.example.gabri.firstapp;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;

import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.youtube.player.YouTubeInitializationResult;
import com.google.android.youtube.player.YouTubePlayer;
import com.google.android.youtube.player.YouTubePlayer.OnInitializedListener;
import com.google.android.youtube.player.YouTubePlayer.Provider;
import com.google.android.youtube.player.YouTubePlayerSupportFragment;

import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class YoutubePlayerFragment extends YouTubePlayerSupportFragment {
    private FragmentActivity myContext;

    private YouTubePlayer YPlayer;
    private static final String YOUTUBE_KEY = "AIzaSyDyRscHFl1-ISHhSaVsL6ve3iDyrnsh7R0";
    private static final int RECOVERY_DIALOG_REQUEST = 1;
    private String url;
    private static  String idVideo;


    public static YoutubePlayerFragment newInstance(String url){
        YoutubePlayerFragment player=new YoutubePlayerFragment();
        Bundle bundle =new Bundle();
        bundle.putString("URL",url);

        player.setArguments(bundle);
        player.init();

        return player;
    }

    private void init() {
        initialize(YOUTUBE_KEY, new OnInitializedListener() {

            @Override
            public void onInitializationFailure(Provider arg0, YouTubeInitializationResult arg1) {
            }

            @Override
            public void onInitializationSuccess(YouTubePlayer.Provider provider, YouTubePlayer player, boolean wasRestored) {
                YPlayer = player;
                YPlayer.setPlayerStyle(YouTubePlayer.PlayerStyle.DEFAULT);
                if (!wasRestored) {
                    YPlayer.loadVideo(getIdFromLink(getArguments().getString("URL")), 0);


                }
            }
        });
    }



   /* @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        Activity a;

        if (context instanceof Activity){
            a=(Activity) context;
        }

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            this.url = getArguments().getString("LINK");
            this.idVideo=getIdFromLink(this.url);
        }
        View rootView = inflater.inflate(R.layout.fragment_game_detail, container, false);


        //YouTubePlayerSupportFragment youTubePlayerFragment = (YouTubePlayerSupportFragment) getFragmentManager().findFragmentById(R.id.youtube_view);
        YouTubePlayerSupportFragment youTubePlayerFragment = YouTubePlayerSupportFragment.newInstance();
        FragmentTransaction transaction = getFragmentManager().beginTransaction();
        transaction.add(R.id.youtube_view, youTubePlayerFragment).commit();

        youTubePlayerFragment.initialize(YOUTUBE_KEY, new OnInitializedListener() {

            @Override
            public void onInitializationSuccess(Provider arg0, YouTubePlayer youTubePlayer, boolean b) {
                if (!b) {
                    YPlayer = youTubePlayer;
                    YPlayer.setFullscreen(false);
                    YPlayer.loadVideo(idVideo);

                }

            }

            @Override
            public void onInitializationFailure(Provider arg0, YouTubeInitializationResult arg1) {
                // TODO Auto-generated method stub


            }
        });

        return rootView;

    }
*/
   private String getIdFromLink(String url) {

        String pattern = "(?<=watch\\?v=|/videos/|embed\\/)[^#\\&\\?]*";
        Pattern compiledPattern = Pattern.compile(pattern);
        Matcher matcher = compiledPattern.matcher(url);

        if(matcher.find()){
            return matcher.group();
        }

        return "null";

    }
    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }
}