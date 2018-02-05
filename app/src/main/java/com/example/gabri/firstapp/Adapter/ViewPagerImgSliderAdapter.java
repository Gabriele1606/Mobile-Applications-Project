package com.example.gabri.firstapp.Adapter;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.support.annotation.Nullable;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.example.gabri.firstapp.R;

import java.util.List;


/**
 * Created by simon on 04/12/2017.
 */

public class ViewPagerImgSliderAdapter extends PagerAdapter {
    private Context context;
    private LayoutInflater layoutInflater;
    private Integer [] images= {R.drawable.album1};
    private List<String> urlImages;


    public ViewPagerImgSliderAdapter(Context context) {
        super();
        this.context = context;
    }

    @Override
    public int getCount() {
        return urlImages.size();
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view==object;
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        layoutInflater=(LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view=layoutInflater.inflate(R.layout.content_image_slider,null);
        ImageView imageView= view.findViewById(R.id.image_slider);
        final ProgressBar progressBar = (ProgressBar) view.findViewById(R.id.progress);
        if(position<urlImages.size()){
            Glide.with(context).load(urlImages.get(position)).listener(new RequestListener<Drawable>() {
                @Override
                public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                    return false;
                }

                @Override
                public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                    progressBar.setVisibility(View.GONE);
                    return false;
                }
            }).into(imageView);


        }
        ViewPager vp= (ViewPager) container;
        vp.addView(view,0);
        return view;
    }

    public List<String> getUrlImages() {
        return urlImages;
    }

    public void setUrlImages(List<String> urlImages) {
        this.urlImages = urlImages;
    }
    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        //super.destroyItem(container, position, object);
        ViewPager vp= (ViewPager) container;
        View view = (View) object;
        vp.removeView(view);

    }
}
