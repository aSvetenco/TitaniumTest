package com.sa.testtask.api;


import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;


public class Api {

    public static final String BASE_URL = "https://gist.githubusercontent.com/";

    public static Retrofit getInstance() {

            return new Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                    .build();
    }

    public static ImagesApi getImagesApi() {
        return getInstance().create(ImagesApi.class);
    }
}
