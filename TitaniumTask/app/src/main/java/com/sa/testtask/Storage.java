package com.sa.testtask;

import com.sa.testtask.api.Response;

import java.util.ArrayList;
import java.util.List;

public class Storage {

    private static Storage storage;

    public static Storage getInstance() {
        if (storage == null) {
            storage = new Storage();
        }
        return storage;
    }

    private Storage() {

    }

    private List<Response.Image> images = new ArrayList<>();

    public List<Response.Image> getImages() {
        return images;
    }

    public void setImages(List<Response.Image> images) {
        this.images = images;
    }
}
