package com.sa.testtask.screens;


import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;

import com.sa.testtask.R;
import com.sa.testtask.Storage;
import com.sa.testtask.api.Response;
import com.sa.testtask.screens.list.ImagesRVAdapter;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import rx.Observable;
import rx.Subscription;
import rx.subscriptions.CompositeSubscription;

import static com.sa.testtask.screens.MainActivity.IMAGE_POSITION_KEY;

public class ListActivity extends AppCompatActivity {


    private static final String TAG = ListActivity.class.getSimpleName();
    private CompositeSubscription subscription = new CompositeSubscription();
    @BindView(R.id.recycle_view)
    RecyclerView recyclerView;
    @BindView(R.id.toolbar)
    Toolbar toolbar;

    private ImagesRVAdapter adapter;
    private List<Response.Image> imageList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list);
        ButterKnife.bind(this);
        setSupportActionBar(toolbar);
        imageList = new ArrayList<>();
        imageList.addAll(Storage.getInstance().getImages());
        adapter = new ImagesRVAdapter();
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);
        showList(imageList);

    }

    @Override
    protected void onResume() {
        super.onResume();
        Subscribe();
    }

    private void Subscribe() {
        subscription.add(onItemClickRegister());
    }

    private Subscription onItemClickRegister() {
        return  adapter.getViewClickedObservable()
                .subscribe(this::startMainActivity,
                        throwable -> Log.d(TAG, throwable.getMessage(), throwable));
    }

    private void startMainActivity(int position) {
        Intent intent = new Intent();
        intent.putExtra(IMAGE_POSITION_KEY, position);
        setResult(RESULT_OK, intent);
        finish();
    }

    private void showList(List<Response.Image> list) {
        adapter.swapData(list);
    }

    @Override
    protected void onStop() {
        subscription.clear();
        super.onStop();
    }


}
