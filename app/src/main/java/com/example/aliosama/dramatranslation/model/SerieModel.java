package com.example.aliosama.dramatranslation.model;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by aliosama on 8/14/2017.
 */

public class SerieModel implements Serializable {

    @SerializedName("id")
    int ID;

    @SerializedName("slug")
    String Slug;

    @SerializedName("first_air_date")
    String Date;

    @SerializedName("original_name")
    String OrignalName;

    @SerializedName("title")
    SerieModel.title Title;

    @SerializedName("content")
    SerieModel.content Story;

    @SerializedName("number_of_episodes")
    String NumberOfEpisodes;

    @SerializedName("number_of_seasons")
    String NumberOfSeasons;

    ArrayList<SerieModel.ImageModel> imagesUrls;

    public SerieModel(){

    }

    public int getID() {
        return ID;
    }

    public SerieModel setID(int ID) {
        this.ID = ID;
        return this;
    }

    public String getSlug() {
        return Slug;
    }

    public SerieModel setSlug(String slug) {
        Slug = slug;
        return this;
    }

    public String getOrignalName() {
        return OrignalName;
    }

    public SerieModel setOrignalName(String orignalName) {
        OrignalName = orignalName;
        return this;
    }

    public String getDate() {
        return Date;
    }

    public SerieModel setDate(String date) {
        Date = date;
        return this;
    }

    public SerieModel.title getTitle() {
        return Title;
    }

    public SerieModel setTitle(SerieModel.title title) {
        Title = title;
        return this;
    }

    public SerieModel.content getStory() {
        return Story;
    }

    public SerieModel setStory(SerieModel.content story) {
        Story = story;
        return this;
    }

    public ArrayList<SerieModel.ImageModel> getImagesUrls() {
        return imagesUrls;
    }

    public SerieModel setImagesUrls(ArrayList<SerieModel.ImageModel> imagesUrls) {
        this.imagesUrls = imagesUrls;
        return this;
    }

    public String getNumberOfEpisodes() {
        return NumberOfEpisodes;
    }

    public SerieModel setNumberOfEpisodes(String numberOfEpisodes) {
        NumberOfEpisodes = numberOfEpisodes;
        return this;
    }

    public String getNumberOfSeasons() {
        return NumberOfSeasons;
    }

    public SerieModel setNumberOfSeasons(String numberOfSeasons) {
        NumberOfSeasons = numberOfSeasons;
        return this;
    }


    public class title implements Serializable{
        @SerializedName("rendered")
        String rendered;

        public String getRendered() {
            return rendered;
        }

        public SerieModel.title setRendered(String rendered) {
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

        public SerieModel.content setRendered(String rendered) {
            this.rendered = rendered;
            return this;
        }
    }
    public class ImageModel implements Serializable{
        @SerializedName("source_url")
        String FullImageUrl;
        @SerializedName("media_details")
        mediaDetails MediaDetails;

        public mediaDetails getMediaDetails() {
            return MediaDetails;
        }

        public ImageModel setMediaDetails(mediaDetails mediaDetails) {
            MediaDetails = mediaDetails;
            return this;
        }

        public String getImageUrl() {
            return FullImageUrl;
        }

        public SerieModel.ImageModel setImageUrl(String imageUrl) {
            FullImageUrl = imageUrl;
            return this;
        }

        public class mediaDetails implements Serializable{
            @SerializedName("sizes")
            sizes Sizes;

            public sizes getSizes() {
                return Sizes;
            }

            public mediaDetails setSizes(sizes sizes) {
                Sizes = sizes;
                return this;
            }

            public class sizes implements Serializable{

                @SerializedName("medium")
                thumbnail Thumbnail;

                public thumbnail getThumbnail() {
                    return Thumbnail;
                }

                public sizes setThumbnail(thumbnail thumbnail) {
                    Thumbnail = thumbnail;
                    return this;
                }

                public class thumbnail implements Serializable{

                    @SerializedName("source_url")
                    String ImageUrl;

                    public String getImageUrl() {
                        return ImageUrl;
                    }

                    public thumbnail setImageUrl(String imageUrl) {
                        ImageUrl = imageUrl;
                        return this;
                    }
                }

            }
        }
    }

}
