package com.example.aliosama.dramatranslation.model;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by aliosama on 8/15/2017.
 */

public class MovieModel implements Serializable{


    @SerializedName("id")
    int ID;

    @SerializedName("release_date")
    String Date;

    @SerializedName("title")
    title Title;

    @SerializedName("content")
    content Story;

    @SerializedName("youtube_id")
    String YoutubeVideoID;

    @SerializedName("dt_string")
    String dt_String;

    @SerializedName("repeatable_fields")
    ArrayList<repeatable_fields> Videos;

    MovieLink moviesDownloadLink;
    ArrayList<ImageModel> imagesUrls;

    public MovieModel(){

    }


//    Getter & Setter

    public int getID() {
        return ID;
    }

    public MovieModel setID(int ID) {
        this.ID = ID;
        return this;
    }

    public MovieLink getMoviesDownloadLink() {
        return moviesDownloadLink;
    }

    public MovieModel setMoviesDownloadLink(MovieLink moviesDownloadLink) {
        this.moviesDownloadLink = moviesDownloadLink;
        return this;
    }

    public String getDate() {
        return Date;
    }

    public MovieModel setDate(String date) {
        Date = date;
        return this;
    }

    public title getTitle() {
        return Title;
    }

    public MovieModel setTitle(title title) {
        Title = title;
        return this;
    }

    public content getStory() {
        return Story;
    }

    public MovieModel setStory(content story) {
        Story = story;
        return this;
    }

    public ArrayList<repeatable_fields> getVideos() {
        return Videos;
    }

    public MovieModel setVideos(ArrayList<repeatable_fields> videos) {
        Videos = videos;
        return this;
    }

    public ArrayList<ImageModel> getImagesUrls() {
        return imagesUrls;
    }

    public MovieModel setImagesUrls(ArrayList<ImageModel> imagesUrls) {
        this.imagesUrls = imagesUrls;
        return this;
    }

    public String getDt_String() {
        return dt_String;
    }

    public MovieModel setDt_String(String dt_String) {
        this.dt_String = dt_String;
        return this;
    }

    public String getYoutubeVideoID() {
        return YoutubeVideoID;
    }

    public MovieModel setYoutubeVideoID(String youtubeVideoID) {
        YoutubeVideoID = youtubeVideoID;
        return this;
    }


//    Inner Classes
    public class title implements Serializable{
        @SerializedName("rendered")
         String rendered;

        public String getRendered() {
            return rendered;
        }

        public title setRendered(String rendered) {
            this.rendered = rendered;
            return this;
        }
    }

    public class content implements Serializable{
        @SerializedName("rendered")
         String rendered;

        public String getRendered() {
            return rendered;
        }

        public content setRendered(String rendered) {
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

        public repeatable_fields setHost(String host) {
            Host = host;
            return this;
        }

        public String getUrl() {
            return Url;
        }

        public repeatable_fields setUrl(String url) {
            Url = url;
            return this;
        }
    }

    public class MovieLink implements Serializable{
        @SerializedName("links_url")
        String links_url;

        public String getLinks_url() {
            return links_url;
        }

        public MovieLink setLinks_url(String links_url) {
            this.links_url = links_url;
            return this;
        }
    }

    public class ImageModel implements Serializable{
         @SerializedName("source_url")
         String ImageUrl;

         public String getImageUrl() {
             return ImageUrl;
         }

         public ImageModel setImageUrl(String imageUrl) {
             ImageUrl = imageUrl;
             return this;
         }
     }

}
