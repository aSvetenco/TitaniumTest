package com.sa.testtask.api;

import retrofit2.http.GET;
import rx.Observable;

public interface ImagesApi {


    @GET("aSvetenco/0625925e2261c8149868d5236458eede/raw/308d51da2239780d7927e479de04898571b6c778/gistfile1.txt")
    Observable<Response> getImages();
}
