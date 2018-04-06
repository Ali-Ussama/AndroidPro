package com.example.aliosama.dramatranslation.model;

import java.io.Serializable;

/**
 * Created by aliosama on 8/15/2017.
 */

public class VideoModel implements Serializable {

    String Host;
    String Url;

    public VideoModel(String host, String url) {
        Host = host;
        Url = url;
    }

    public String getHost() {
        return Host;
    }

    public void setHost(String host) {
        Host = host;
    }

    public String getUrl() {
        return Url;
    }

    public void setUrl(String url) {
        Url = url;
    }
}
