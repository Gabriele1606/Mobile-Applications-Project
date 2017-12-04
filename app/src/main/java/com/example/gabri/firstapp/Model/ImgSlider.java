package com.example.gabri.firstapp.Model;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by simon on 03/12/2017.
 */

public class ImgSlider {
    List<String> urlImages;
    public ImgSlider(){
        urlImages= new ArrayList<String>();
    }

    public void setUrlImages(List<String> urlImages) {
        this.urlImages = urlImages;
    }

    public List<String> getUrlImages() {
        return urlImages;
    }
}
