package com.example.gabri.firstapp.Model;

import org.simpleframework.xml.Element;
import org.simpleframework.xml.Root;

/**
 * Created by Gabri on 12/11/17.
 */
@Root(name="item", strict=false)
public class RSSFeed {
    @Element(name = "title", required = false)
    private String title;

    @Element(name = "description", required = false)
    private String description;

    @Element(name = "pubdate", required = false)
    private String pubdate;

    private String imageLink;


    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }


    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getPubdate() {
        return pubdate;
    }

    public void setPubdate(String pubdate) {
        this.pubdate = pubdate;
    }

    public String getImageLink() {
        return imageLink;
    }

    public void setImageLink(String imageLink) {
        this.imageLink = imageLink;
    }
}