package com.example.aliosama.dramatranslation.model;

import java.io.Serializable;

/**
 * Created by aliosama on 8/14/2017.
 */

public class ImageModel implements Serializable {


    String ImageType;
    String ImageUrl;

    public ImageModel(String imageType, String imageUrl) {
        ImageType = imageType;
        ImageUrl = imageUrl;
    }

    public String getImageType() {
        return ImageType;
    }

    public void setImageType(String imageType) {
        ImageType = imageType;
    }

    public String getImageUrl() {
        return ImageUrl;
    }

    public void setImageUrl(String imageUrl) {
        ImageUrl = imageUrl;
    }
}
