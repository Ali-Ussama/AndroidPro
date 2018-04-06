package com.example.aliosama.dramatranslation.model;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by aliosama on 8/14/2017.
 */

public class EpisodeModel implements Serializable {

    @SerializedName("id")
    int ID;
    @SerializedName("slug")
    String Slug;

    @SerializedName("title")
    title Title;

    @SerializedName("episodio")
    String EpisodeNumber;

    @SerializedName("serie")
    String Serie;

    @SerializedName("episode_name")
    String EpisodeName;
    @SerializedName("dt_string")
    String dtString;

    @SerializedName("repeatable_fields")
    ArrayList<EpisodeModel.repeatable_fields> Videos;

    ArrayList<DownloadModel> download;

    public int getID() {
        return ID;
    }

    public EpisodeModel setID(int ID) {
        this.ID = ID;
        return this;
    }

    public String getSlug() {
        return Slug;
    }

    public EpisodeModel setSlug(String slug) {
        Slug = slug;
        return this;
    }

    public title getTitle() {
        return Title;
    }

    public EpisodeModel setTitle(title title) {
        Title = title;
        return this;
    }

    public String getEpisodeNumber() {
        return EpisodeNumber;
    }

    public EpisodeModel setEpisodeNumber(String episodeNumber) {
        EpisodeNumber = episodeNumber;
        return this;
    }

    public String getSerie() {
        return Serie;
    }

    public EpisodeModel setSerie(String serie) {
        Serie = serie;
        return this;
    }

    public String getEpisodeName() {
        return EpisodeName;
    }

    public EpisodeModel setEpisodeName(String episodeName) {
        EpisodeName = episodeName;
        return this;
    }

    public ArrayList<EpisodeModel.repeatable_fields> getVideos() {
        return Videos;
    }

    public EpisodeModel setVideos(ArrayList<EpisodeModel.repeatable_fields> videos) {
        Videos = videos;
        return this;
    }

    public String getDtString() {
        return dtString;
    }

    public EpisodeModel setDtString(String dtString) {
        this.dtString = dtString;
        return this;
    }

    public ArrayList<DownloadModel> getDownload() {
        return download;
    }

    public EpisodeModel setDownload(ArrayList<DownloadModel> download) {
        this.download = download;
        return this;
    }

    public class title implements Serializable{
        @SerializedName("rendered")
        String rendered;

        public String getRendered() {
            return rendered;
        }

        public EpisodeModel.title setRendered(String rendered) {
            this.rendered = rendered;
            return this;
        }
    }
    public class repeatable_fields implements Serializable{

        @SerializedName("name")
        String Host;
        @SerializedName("url")
        String Url;

        public String getHost() {
            return Host;
        }

        public EpisodeModel.repeatable_fields setHost(String host) {
            Host = host;
            return this;
        }

        public String getUrl() {
            return Url;
        }

        public EpisodeModel.repeatable_fields setUrl(String url) {
            Url = url;
            return this;
        }
    }

}
