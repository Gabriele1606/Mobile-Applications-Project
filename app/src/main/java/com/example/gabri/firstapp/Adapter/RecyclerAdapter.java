package com.example.gabri.firstapp.Adapter;

/**
 * Created by simon on 09/11/2017.
 */

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.PopupMenu;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.daimajia.slider.library.SliderLayout;
import com.daimajia.slider.library.SliderTypes.TextSliderView;
import com.example.gabri.firstapp.Controller.TimerSlider;
import com.example.gabri.firstapp.FragmentProva;
import com.example.gabri.firstapp.Model.Data;
import com.example.gabri.firstapp.FragmentGameDetail;
import com.example.gabri.firstapp.FragmentNewsDetail;
import com.example.gabri.firstapp.Model.ImgSlider;
import com.example.gabri.firstapp.Model.RSSFeed;
import com.example.gabri.firstapp.Model.RowGame;
import com.example.gabri.firstapp.Model.Title;
import com.example.gabri.firstapp.R;

import java.util.List;
import java.util.Timer;

import jp.wasabeef.recyclerview.animators.SlideInUpAnimator;


public class RecyclerAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

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
    public class RssFeedHolder extends RecyclerView.ViewHolder{
        public TextView rssText;
        public TextView rssTitle;
        public ImageView imageView;
        public View view;
        public RssFeedHolder(View itemView) {
            super(itemView);
            view=itemView;
            //rssText = (TextView) itemView.findViewById(R.id.text_news);
            //rssText = (TextView) itemView.findViewById(R.id.text_news);
            rssTitle=(TextView) itemView.findViewById(R.id.title_news);
            imageView=(ImageView)itemView.findViewById(R.id.image_rss);

        }
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
            return ROWGAME;
        }else if (listObject.get(position) instanceof RSSFeed){
            return RSSFEED;
        }else if (listObject.get(position) instanceof Title){
            return TITLE;
        } else if (listObject.get(position) instanceof ImgSlider){
            return IMGSLIDER;
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
    public void onBindViewHolder(RecyclerView.ViewHolder viewHolder, int position) {
        switch (viewHolder.getItemViewType()) {
            case ROWGAME:
                RecHolder vh1 = (RecHolder) viewHolder;
                configureRecHolder(vh1, position);
                break;
            case RSSFEED:
                RssFeedHolder rssFeedHolder = (RssFeedHolder) viewHolder;
                setRssCard(rssFeedHolder,position);
                rssFeedHolder.view.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        boolean TWOPANELS = Data.getData().getHomePageActivity().getResources().getBoolean(R.bool.has_two_panes);
                        if(!TWOPANELS){
                            FragmentNewsDetail fragmentNewsDetail= new FragmentNewsDetail();
                            //FINAL SOLUTION
                            Fragment fragmentById = Data.getData().getHomePageActivity().getSupportFragmentManager().findFragmentById(R.id.mainframeLayout);
                            FragmentTransaction transaction = Data.getData().getHomePageActivity().getSupportFragmentManager().beginTransaction().replace(R.id.mainframeLayout, fragmentNewsDetail, "GameDetail");
                            transaction.addToBackStack("TABLAYOUT");
                            transaction.commit();
                        }else{
                            FragmentNewsDetail fragmentNewsDetail= new FragmentNewsDetail();
                            FragmentTransaction transaction = Data.getData().getHomePageActivity().getSupportFragmentManager().beginTransaction().replace(R.id.framegameDetail,fragmentNewsDetail, "GameDetail");
                            transaction.commit();
                            Data.getData().getHomePageActivity().enlargeDetailGame();
                        }
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
        holder.adapter= new HorizontalAdapter(mContext,rowGame.getList());
        holder.recelement.setAdapter(holder.adapter);
        holder.recelement.setNestedScrollingEnabled(false);
        //prova
        holder.recelement.setItemAnimator(new SlideInUpAnimator());
        //holder.recelement.setHasFixedSize(true);
    }

    private void setRssCard(RssFeedHolder rssFeedHolder, int position){
        //rssFeedHolder.rssText.setText(((RSSFeed)listObject.get(position)).getDescription());
        rssFeedHolder.rssTitle.setText(((RSSFeed)listObject.get(position)).getTitle());
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
}