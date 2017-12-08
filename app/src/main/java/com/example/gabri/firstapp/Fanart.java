package com.example.gabri.firstapp;

import org.simpleframework.xml.Element;
import org.simpleframework.xml.Root;

/**
 * Created by Gabri on 07/12/17.
 */
@Root(name="fanart", strict=false)
public class Fanart {

    @Element(name="original",required = false)
    private String originalFanart;

    @Element(name="thumb",required = false)
    private String thumb;

    public String getOriginalFanart() {
        return originalFanart;
    }

    public void setOriginalFanart(String originalFanart) {
        this.originalFanart = originalFanart;
    }

    public String getThumb() {
        return thumb;
    }

    public void setThumb(String thumb) {
        this.thumb = thumb;
    }
}
