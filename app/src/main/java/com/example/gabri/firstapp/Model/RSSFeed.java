package com.example.gabri.firstapp.Model;

import android.support.annotation.AttrRes;

import com.example.gabri.firstapp.Controller.Filter;

import org.simpleframework.xml.Attribute;
import org.simpleframework.xml.Element;
import org.simpleframework.xml.Path;
import org.simpleframework.xml.Root;

import java.io.Serializable;

/**
 * Created by Gabri on 12/11/17.
 */
@Root(name="item", strict=false)
public class RSSFeed implements Serializable {
    @Element(name = "title", required = false)
    private String title;

    @Element(name = "link", required = false)
    private String link;

    @Element(name = "description", required = false)
    private String description;

    @Element(name = "pubDate", required = false)
    private String pubdate;

    @Element(name = "guid", required = false)
    private String guid;

    @Element(name = "creator", required = false)
    private String creator;

    @Attribute(name = "url", required = false)
    @Path("content")
    private String imageLink;

    private String idForFirebase;

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
        return this.pubdate;
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

    public String getIdForFirebase() {
        return idForFirebase;
    }

    public void setIdForFirebase(String idForFirebase) {
        this.idForFirebase = idForFirebase;
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }

    public String getCreator() {
        return creator;
    }

    public void setCreator(String creator) {
        this.creator = creator;
    }

}
