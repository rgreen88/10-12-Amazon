package com.example.rynel.amazonchallenge.data;

import com.example.rynel.amazonchallenge.model.Book;

import java.util.List;

import io.reactivex.Observable;
import retrofit2.http.GET;

public interface RemoteService {

    @GET("books.json")
    Observable<List<Book>> getBooks();
}
