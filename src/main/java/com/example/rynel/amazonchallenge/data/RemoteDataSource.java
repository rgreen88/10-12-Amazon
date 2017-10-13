package com.example.rynel.amazonchallenge.data;

import com.example.rynel.amazonchallenge.model.Book;

import java.util.List;

import io.reactivex.Observable;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

public class RemoteDataSource {

    //amazon URL provided
    public static final String BASE_URL = "http://de-coding-test.s3.amazonaws.com/";

    //Retrofit construct
    public static Retrofit create() {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .build();

        return retrofit;
    }

    //creates a line of data from arrayList Book to collect information
    //retrofit object creates a new retrofit each point of collection
    //remoteService adds each created item
    public static Observable<List<Book>> getBooks() {
        Retrofit retrofit = create();
        RemoteService remoteService = retrofit.create(RemoteService.class);

        return remoteService.getBooks();
    }
}