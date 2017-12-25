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

    @Element(name = "pubDate", required = false)
    private String pubdate;

    @Element(name = "guid", required = false)
    private String guid;

    private String imageLink;

    public RSSFeed() {

    }


    public RSSFeed(String title, String description, String pubdate, String guid, String imageLink) {
        this.title = title;
        this.description = description;
        this.pubdate = pubdate;
        this.guid = guid;
        this.imageLink = imageLink;
    }

    public String getGuid() {
        return guid;
    }

    public void setGuid(String guid) {
        this.guid = guid;
    }

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
