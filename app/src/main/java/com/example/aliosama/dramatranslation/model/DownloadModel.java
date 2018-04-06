package com.example.aliosama.dramatranslation.model;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * Created by aliosama on 8/21/2017.
 */

public class DownloadModel implements Serializable {
    @SerializedName("id")
    int ID;

    @SerializedName("link")
    String Link;

    @SerializedName("links_url")
    String LinkUrl;
    public int getID() {
        return ID;
    }

    public DownloadModel setID(int ID) {
        this.ID = ID;
        return this;
    }

    public String getLink() {
        return Link;
    }

    public DownloadModel setLink(String link) {
        Link = link;
        return this;
    }

    public String getLinkUrl() {
        return LinkUrl;
    }

    public DownloadModel setLinkUrl(String linkUrl) {
        LinkUrl = linkUrl;
        return this;
    }

}
