package com.sa.testtask.api;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class Response {

    //if name of field in same as in serialized string, it can be omitted
    @SerializedName("images")
    private List<Image> images = null;

    public List<Image> getImages() {
        return images;
    }

    public void setImages(List<Image> images) {
        this.images = images;
    }

    public static class Image {

        @SerializedName("name")
        private String name;
        @SerializedName("image")
        private String image;

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getImage() {
            return image;
        }

        public void setImage(String image) {
            this.image = image;
        }

    }
}
