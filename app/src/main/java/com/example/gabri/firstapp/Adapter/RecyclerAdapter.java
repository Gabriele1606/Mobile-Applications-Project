package com.example.gabri.firstapp.Adapter;

/**
 * Created by simon on 09/11/2017.
 */

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v4.graphics.drawable.RoundedBitmapDrawable;
import android.support.v4.graphics.drawable.RoundedBitmapDrawableFactory;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.PopupMenu;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.transition.Transition;
import com.daimajia.slider.library.SliderLayout;
import com.daimajia.slider.library.SliderTypes.TextSliderView;
import com.example.gabri.firstapp.Controller.TimerSlider;
import com.example.gabri.firstapp.FragmentReadLater;
import com.example.gabri.firstapp.FragmentWishList;
import com.example.gabri.firstapp.Model.Data;
import com.example.gabri.firstapp.FragmentNewsDetail;
import com.example.gabri.firstapp.Model.ImgSlider;
import com.example.gabri.firstapp.Model.RSSFeed;
import com.example.gabri.firstapp.Model.RowGame;
import com.example.gabri.firstapp.Model.Title;
import com.example.gabri.firstapp.MyTask;
import com.example.gabri.firstapp.R;
import com.example.gabri.firstapp.UserInfo;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.twitter.sdk.android.core.models.Image;
import com.wajahatkarim3.easyflipview.EasyFlipView;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;

import jp.wasabeef.recyclerview.animators.SlideInUpAnimator;


public class RecyclerAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private final int GRIDGAME=6;
    private final int USERINFO=5;
    private final int IMGSLIDER = 4;
    private final int TITLE = 3;
    private final int SLIDER = 2;
    private final int RSSFEED = 1;
    private final int ROWGAME = 0;
    private Context mContext;
    private List<Object> listObject;
    boolean timerStarted =false;
    public class RecHolder extends RecyclerView.ViewHolder {
        public RecyclerView recelement;
       public HorizontalAdapter adapter;

        public RecHolder(View view) {
            super(view);
            RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(mContext, LinearLayoutManager.HORIZONTAL,false);
            recelement=(RecyclerView) view.findViewById(R.id.rec_element_id);
            recelement.setLayoutManager(mLayoutManager);
            recelement.setItemAnimator(new DefaultItemAnimator());
            //System.out.println(this.adapter);
        }
    }

    public class GridHolder extends RecyclerView.ViewHolder {
        public int SPANCOUNT = 3;
        public RecyclerView recelement;
        public GridAdapter adapter;

        public GridHolder(View view) {
            super(view);
            RecyclerView.LayoutManager mLayoutManager = new GridLayoutManager(mContext,SPANCOUNT);
            recelement=(RecyclerView) view.findViewById(R.id.rec_element_id);
            recelement.setLayoutManager(mLayoutManager);
            recelement.setItemAnimator(new DefaultItemAnimator());
            //System.out.println(this.adapter);
        }
    }

    public class RssFeedHolder extends RecyclerView.ViewHolder{
        public TextView rssText;
        public TextView rssTitle;
        public TextView rssPubDate;
        public ImageView imageView;
        public ImageView readLaterButton;
        public Boolean isFavorite;
        public View view;
        public RssFeedHolder(View itemView) {
            super(itemView);
            view=itemView;
            //rssText = (TextView) itemView.findViewById(R.id.text_news);
            //rssText = (TextView) itemView.findViewById(R.id.text_news);
            rssTitle=(TextView) itemView.findViewById(R.id.title_news);
            rssPubDate=(TextView) itemView.findViewById(R.id.pubdate);
            imageView=(ImageView)itemView.findViewById(R.id.image_rss);
            //readLaterButton=(ImageView) itemView.findViewById(R.id.read_later);

        }
    }

    public class UserInfoHolder extends RecyclerView.ViewHolder{
            TextView welcomeMessage;
            ImageView userPhoto;
            TextView description;
            ImageView wishList;
            ImageView readLater;
            ImageView backgroundImageFront;
            ImageView backgroundImageBack;
            TextView notificationNumberNews;
            TextView notificationNumberGame;
            EasyFlipView flipView;

        public UserInfoHolder(View itemView) {
            super(itemView);
            this.welcomeMessage= (TextView) itemView.findViewById(R.id.welcome_message);
            this.description= (TextView) itemView.findViewById(R.id.user_description);
            this.userPhoto=(ImageView) itemView.findViewById(R.id.user_photo);
            this.wishList=(ImageView) itemView.findViewById(R.id.wish_list_button);
            this.readLater=(ImageView) itemView.findViewById(R.id.notification_button);
            this.backgroundImageFront=(ImageView)itemView.findViewById(R.id.background_image_front);
            this.backgroundImageBack=(ImageView)itemView.findViewById(R.id.background_image_back);
            this.notificationNumberNews=(TextView) itemView.findViewById(R.id.number_of_favorite_news);
            this.notificationNumberGame=(TextView) itemView.findViewById(R.id.number_of_wishlist);
            this.flipView=(EasyFlipView)itemView.findViewById(R.id.flip_view);

        }
        public void setWelcomeMessage(String message){this.welcomeMessage.setText(message);}
    }

        public void getNumberFavoriteNews(final UserInfoHolder userInfoHolder){
            final List<Object> tmp=new ArrayList<Object>();
            DatabaseReference databaseReference= FirebaseDatabase.getInstance().getReference();
            databaseReference.child("news").child(Data.getIdUserForRemoteDb()).addListenerForSingleValueEvent(
                    new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            if(dataSnapshot.getChildrenCount()>0){
                                userInfoHolder.notificationNumberNews.setVisibility(View.VISIBLE);
                                userInfoHolder.notificationNumberNews.setText(Long.toString(dataSnapshot.getChildrenCount()));
                            }
                            else{
                                userInfoHolder.notificationNumberNews.setVisibility(View.GONE);
                            }

                        }
                        @Override
                        public void onCancelled(DatabaseError databaseError) {

                        }
                    }
            );

        }

        public void getNumberWishlist(final UserInfoHolder userInfoHolder){
            final List<Object> tmp=new ArrayList<Object>();
            DatabaseReference databaseReference= FirebaseDatabase.getInstance().getReference();
            databaseReference.child("game").child(Data.getIdUserForRemoteDb()).addListenerForSingleValueEvent(
                    new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            if(dataSnapshot.getChildrenCount()>0){
                                userInfoHolder.notificationNumberGame.setVisibility(View.VISIBLE);
                                userInfoHolder.notificationNumberGame.setText(Long.toString(dataSnapshot.getChildrenCount()));
                            }
                            else{
                                userInfoHolder.notificationNumberGame.setVisibility(View.GONE);
                            }

                        }
                        @Override
                        public void onCancelled(DatabaseError databaseError) {

                        }
                    }
            );

        }

    public class SliderHolder extends RecyclerView.ViewHolder{
        SliderLayout sliderShow;
        TextSliderView textSliderView;
        public SliderHolder(View itemView) {
            super(itemView);
            this.sliderShow = (SliderLayout) itemView.findViewById(R.id.main_slider);
            //this.textSliderView = new TextSliderView(itemView.getContext());
        }
        public void addImage(String url){
           /* textSliderView
                    .description("Game of Thrones")
                    .image(url);
            sliderShow.addSlider(textSliderView);*/

        }
    }
    public class TitleHolder extends RecyclerView.ViewHolder{
        TextView textView;
        public TitleHolder(View itemView) {
            super(itemView);
            this.textView= (TextView) itemView.findViewById(R.id.title_section);
        }
        public void setTitle(String title){
            this.textView.setText(title);
        }
    }
    public class ImgSliderHolder extends RecyclerView.ViewHolder{
        ViewPager viewPager;
        Context context;
        public ImgSliderHolder(View itemView) {
            super(itemView);
            this.viewPager= (ViewPager) itemView.findViewById(R.id.img_slider_viewpager);
             context = itemView.getContext();
        }
        public ViewPager getViewPager(){
            return viewPager;
        }
        public Context getContext(){
            return context;
        }
    }
    public RecyclerAdapter(Context mContext, List<Object> listObject) {
        this.mContext = mContext;
        this.listObject = listObject;
    }
    @Override
    public int getItemViewType(int position) {
        if (listObject.get(position) instanceof RowGame) {
            //if (((RowGame) listObject.get(position)).isSlider())
                //return SLIDER;
            if (((RowGame) listObject.get(position)).isGrid())
                return GRIDGAME;
            return ROWGAME;
        }else if (listObject.get(position) instanceof RSSFeed){
            return RSSFEED;
        }else if (listObject.get(position) instanceof Title){
            return TITLE;
        } else if (listObject.get(position) instanceof ImgSlider){
            return IMGSLIDER;
        }else if(listObject.get(position) instanceof UserInfo) {
            return USERINFO;
        }/*else if (items.get(position) instanceof String) {
                return IMAGE;
            }*/
        return -1;
    }
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        RecyclerView.ViewHolder viewHolder;
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());

        switch (viewType) {
            case ROWGAME:
                View itemView = LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.horizontal_recycler, parent, false);
                viewHolder = new RecHolder(itemView);
                break;
            case GRIDGAME:
                View gridView = LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.horizontal_recycler, parent, false);
                viewHolder = new GridHolder(gridView);
                break;

            case RSSFEED:
                View newsView = LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.news_element, parent, false);
                viewHolder = new RssFeedHolder(newsView);
                break;

            case SLIDER:
                View sliderView = LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.slider_layout, parent, false);
                viewHolder = new SliderHolder(sliderView);
                break;
            case TITLE:
                View titleView = LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.section_title_layout, parent, false);
                viewHolder= new TitleHolder(titleView);
                break;

            case IMGSLIDER:
                View imgsliderView = LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.image_slider, parent, false);
                viewHolder = new ImgSliderHolder(imgsliderView);
                break;

            case USERINFO:
                View userInfoView = LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.user_info, parent, false);
                viewHolder = new UserInfoHolder(userInfoView);
                break;
            /*case IMAGE:
                View v2 = inflater.inflate(R.layout.layout_viewholder2, viewGroup, false);
                viewHolder = new ViewHolder2(v2);
                break;*/
            default:
                View v = LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.horizontal_recycler, parent, false);
                viewHolder = new RecHolder(v);
                break;
        }

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder viewHolder, final int position) {
        switch (viewHolder.getItemViewType()) {
            case ROWGAME:
                RecHolder vh1 = (RecHolder) viewHolder;
                configureRecHolder(vh1, position);
                break;
            case GRIDGAME:
                GridHolder gridHolder = (GridHolder) viewHolder;
                configureGridHolder(gridHolder, position);
                break;
            case RSSFEED:
                final RssFeedHolder rssFeedHolder = (RssFeedHolder) viewHolder;
                setRssCard(rssFeedHolder,position);

                DatabaseReference databaseReference= FirebaseDatabase.getInstance().getReference();
                databaseReference.addListenerForSingleValueEvent(
                        new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {
                                if (listObject.size() > position) {
                                    if (dataSnapshot.hasChild("news" + "/" + Data.getIdUserForRemoteDb() + "/" + String.valueOf(((RSSFeed) listObject.get(position)).getIdForFirebase()))) {
                                        rssFeedHolder.isFavorite = true;
                                        //rssFeedHolder.readLaterButton.setImageResource(R.drawable.readlateron);
                                    } else {
                                        rssFeedHolder.isFavorite = false;
                                        //rssFeedHolder.readLaterButton.setImageResource(R.drawable.readlateroff);
                                    }
                                }
                            }

                            @Override
                            public void onCancelled(DatabaseError databaseError) {

                            }
                        }
                );

                rssFeedHolder.view.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        boolean TWOPANELS = Data.getData().getHomePageActivity().getResources().getBoolean(R.bool.has_two_panes);

                        Bundle bundle=new Bundle();
                        bundle.putString("IDFIREBASE",String.valueOf(((RSSFeed)listObject.get(position)).getIdForFirebase()));
                        bundle.putSerializable("REALRSSOBJECT",((RSSFeed)listObject.get(position)));

                            FragmentNewsDetail fragmentNewsDetail= new FragmentNewsDetail();
                            fragmentNewsDetail.setArguments(bundle);
                            //FINAL SOLUTION
                            Fragment fragmentById = Data.getData().getHomePageActivity().getSupportFragmentManager().findFragmentById(R.id.mainframeLayout);
                            FragmentTransaction transaction = Data.getData().getHomePageActivity().getSupportFragmentManager().beginTransaction().replace(R.id.mainframeLayout, fragmentNewsDetail, "GameDetail");
                            transaction.addToBackStack("TABLAYOUT");
                            transaction.commit();

                    }
                });
                break;
            case SLIDER:
                SliderHolder sliderHolder= (SliderHolder) viewHolder;
                //sliderHolder.addImage("http://thegamesdb.net/banners/fanart/original/17097-1.jpg");
                break;
            case TITLE:
                TitleHolder titleHolder= (TitleHolder) viewHolder;
                titleHolder.setTitle(((Title)listObject.get(position)).getTitle());
                break;
            case IMGSLIDER:
                ImgSliderHolder imgSliderHolder=(ImgSliderHolder) viewHolder;
                startImgSlider(imgSliderHolder,position);
                break;
            case USERINFO:
                final UserInfoHolder userInfoHolder=(UserInfoHolder) viewHolder;
                Glide.with(mContext).load(R.drawable.joypad).into(userInfoHolder.backgroundImageFront);
                Glide.with(mContext).load(R.drawable.joypad).into(userInfoHolder.backgroundImageBack);
                getNumberFavoriteNews(userInfoHolder);
                getNumberWishlist(userInfoHolder);

                //Load profile photo of user
                StorageReference storageReference = FirebaseStorage.getInstance().getReference();
                Glide.with(mContext).load(R.drawable.avatar).into(userInfoHolder.userPhoto);

                storageReference.child("images/"+ Data.getUser().getId()).getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                    @Override
                    public void onSuccess(Uri uri) {
                        //Glide.with(mContext).load(uri).into(userInfoHolder.userPhoto);
                        Glide.with(mContext).asBitmap().load(uri).apply(RequestOptions.circleCropTransform()).into(new SimpleTarget<Bitmap>() {
                            @Override
                            public void onResourceReady(@NonNull Bitmap resource, @Nullable Transition<? super Bitmap> transition) {
                                Context context = mContext;
                                if (context!=null) {
                                    RoundedBitmapDrawable circularBitmapDrawable = RoundedBitmapDrawableFactory.create(mContext.getResources(), addBorder(resource, context));
                                    circularBitmapDrawable.setCircular(true);
                                    userInfoHolder.userPhoto.setImageDrawable(circularBitmapDrawable);
                                }
                            }
                        });
                    }
                });

                //Load Description from Firebase
                DatabaseReference databaseReferences= FirebaseDatabase.getInstance().getReference();
                databaseReferences.child("users").child(Data.getIdUserForRemoteDb()).child("description").addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        String description = (String) dataSnapshot.getValue();
                        userInfoHolder.description.setText(description);

                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });


                //Load Username from firebase
                databaseReferences.child("users").child(Data.getIdUserForRemoteDb()).child("username").addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        String username = (String) dataSnapshot.getValue();
                        userInfoHolder.welcomeMessage.setText("Welcome "+username);

                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });

                userInfoHolder.setWelcomeMessage("Welcome "+Data.getUser().getUsername());

                MyTask task=new MyTask(userInfoHolder.flipView);
                task.execute();
                userInfoHolder.readLater.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        boolean TWOPANELS = Data.getData().getHomePageActivity().getResources().getBoolean(R.bool.has_two_panes);

                            FragmentReadLater fragmentReadLater= new FragmentReadLater();
                            FragmentTransaction transaction = Data.getData().getHomePageActivity().getSupportFragmentManager().beginTransaction().replace(R.id.mainframeLayout, fragmentReadLater, "Newslist");
                            transaction.addToBackStack("TABLAYOUT");
                            transaction.commit();


                    }
                });

                userInfoHolder.wishList.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        boolean TWOPANELS = Data.getData().getHomePageActivity().getResources().getBoolean(R.bool.has_two_panes);

                            FragmentWishList fragmentWishList= new FragmentWishList();
                            FragmentTransaction transaction = Data.getData().getHomePageActivity().getSupportFragmentManager().beginTransaction().replace(R.id.mainframeLayout, fragmentWishList, "Gamelist");
                            transaction.addToBackStack("TABLAYOUT");
                            transaction.commit();

                    }
                });
                break;
            /*case IMAGE:
                ViewHolder2 vh2 = (ViewHolder2) viewHolder;
                configureViewHolder2(vh2, position);
                break;
            default:
                RecyclerViewSimpleTextViewHolder vh = (RecyclerViewSimpleTextViewHolder) viewHolder;
                configureDefaultViewHolder(vh, position);
                break;*/
        }

    }

    private void configureGridHolder(GridHolder gridHolder, int position) {
        boolean TWOPANELS = Data.getData().getHomePageActivity().getResources().getBoolean(R.bool.has_two_panes);
        RowGame rowGame=(RowGame)listObject.get(position);
        gridHolder.adapter=new GridAdapter(mContext,rowGame.getList());
        if (TWOPANELS)
            gridHolder.SPANCOUNT=5;
        gridHolder.recelement.setAdapter(gridHolder.adapter);
        gridHolder.recelement.setNestedScrollingEnabled(false);
        gridHolder.recelement.setItemAnimator(new SlideInUpAnimator());
    }

    private  void startImgSlider(ImgSliderHolder holder,int position ){

        ViewPagerImgSliderAdapter viewPagerImgSliderAdapter= new ViewPagerImgSliderAdapter(mContext);
        viewPagerImgSliderAdapter.setUrlImages(((ImgSlider)listObject.get(position)).getUrlImages());
        holder.getViewPager().setAdapter(viewPagerImgSliderAdapter);
        if(!timerStarted) {
            Timer timer = new Timer();
            timer.scheduleAtFixedRate(new TimerSlider(mContext, holder.getViewPager(), viewPagerImgSliderAdapter.getUrlImages()), 2000, 3000);
            timerStarted =true;
        }
    }

    private void configureRecHolder(RecHolder holder, int position) {

        RecyclerView recyclerElem;

        RowGame rowGame = (RowGame) listObject.get(position);
        holder.adapter= new HorizontalAdapter(mContext,rowGame.getList(),holder.recelement);
        holder.recelement.setAdapter(holder.adapter);

        holder.recelement.setNestedScrollingEnabled(false);
        //prova
        holder.recelement.setItemAnimator(new SlideInUpAnimator());
        //holder.recelement.setHasFixedSize(true);
    }

    private void setRssCard(RssFeedHolder rssFeedHolder, int position){
        //rssFeedHolder.rssText.setText(((RSSFeed)listObject.get(position)).getDescription());
        rssFeedHolder.rssTitle.setText(((RSSFeed)listObject.get(position)).getTitle());
        rssFeedHolder.rssPubDate.setText(((RSSFeed)listObject.get(position)).getPubdate());
        Glide.with(mContext).load(((RSSFeed)listObject.get(position)).getImageLink()).into(rssFeedHolder.imageView);
    }


    /**
     * Click listener for popup menu items
     */
    class MyMenuItemClickListener implements PopupMenu.OnMenuItemClickListener {

        public MyMenuItemClickListener() {
        }

        @Override
        public boolean onMenuItemClick(MenuItem menuItem) {
            switch (menuItem.getItemId()) {
            }
            return false;
        }
    }

    @Override
    public int getItemCount() {
        return listObject.size();
    }

    public void clear() {
        listObject.clear();
        notifyDataSetChanged();
    }

    public void addAll(List<Object> list) {
        listObject.addAll(list);
        notifyDataSetChanged();
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
}