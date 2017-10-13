package com.example.rynel.amazonchallenge;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;

import com.example.rynel.amazonchallenge.data.RemoteDataSource;
import com.example.rynel.amazonchallenge.model.Book;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;


public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity";

    //declaring objects as per RecyclerView (layout/item animator), DatabaseHelper, and adapter
    RecyclerView recyclerView;
    List<Book> bookList = new ArrayList<>();
    private RecyclerView.LayoutManager layoutManager;
    private RecyclerView.ItemAnimator itemAnimator;
    private BookListAdapter bookListAdapter;
    private DatabaseHelper dbh;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //initialize the views
        recyclerView = findViewById(R.id.rvBookList);
        layoutManager = new LinearLayoutManager(this);
        itemAnimator = new DefaultItemAnimator();

        //checks last call
        dbh = new DatabaseHelper(this);
//        dbh.getLastCallTime(); <-- caused time delay in loading list

        //populating screen by calling api
        RemoteDataSource.getBooks()
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(new Observer<List<Book>>() {
                    @Override
                    public void onSubscribe(@NonNull Disposable d) {
                        Log.d(TAG, "onSubscribe: ");
                    }

                    @Override
                    public void onNext(@NonNull List<Book> books) {
                        Log.d(TAG, "onNext: " + books.size());
                        bookList = books;
                    }

                    @Override
                    public void onError(@NonNull Throwable e) {
                        Log.d(TAG, "onError: " + e.toString());
                    }

                    @Override
                    public void onComplete() {
                        Log.d(TAG, "onComplete: ");

                        bookListAdapter = new BookListAdapter(bookList);
                        recyclerView.setLayoutManager(layoutManager);
                        recyclerView.setItemAnimator(itemAnimator);
                        recyclerView.setAdapter(bookListAdapter);

                        //store data in database
                        // TODO: 10/13/2017 wasn't calling api to store in database:  FIXED
                        long rowID = dbh.saveBookList(bookList);
                        Log.d(TAG, "onComplete: Added " + rowID + " books.");
                    }
                });
    }
}
