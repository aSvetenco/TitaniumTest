package com.sa.testtask.screens;


import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;

import com.sa.testtask.R;
import com.sa.testtask.Storage;
import com.sa.testtask.screens.list.ImagesRVAdapter;

import butterknife.BindView;
import butterknife.ButterKnife;
import rx.Subscription;

import static com.sa.testtask.screens.MainActivity.IMAGE_POSITION_KEY;

public class ListActivity extends AppCompatActivity {

    private static final String TAG = ListActivity.class.getSimpleName();
    private Subscription subscription;
    @BindView(R.id.recycle_view)
    RecyclerView recyclerView;
    @BindView(R.id.toolbar)
    Toolbar toolbar;

    private ImagesRVAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list);
        ButterKnife.bind(this);
        setSupportActionBar(toolbar);
        adapter = new ImagesRVAdapter();
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);
        showList();

    }

    @Override
    protected void onResume() {
        super.onResume();
        onItemClickRegister();
    }

    private void onItemClickRegister() {
        subscription = adapter.getViewClickedObservable()
                .subscribe(this::startMainActivity,
                        throwable -> Log.d(TAG, throwable.getMessage(), throwable));
    }

    private void startMainActivity(int position) {
        Intent intent = new Intent();
        intent.putExtra(IMAGE_POSITION_KEY, position);
        setResult(RESULT_OK, intent);
        finish();
    }

    private void showList() {
        adapter.swapData(Storage.getInstance().getImages());
    }

    @Override
    protected void onStop() {
        if (subscription != null) {
            subscription.unsubscribe();
        }
        super.onStop();
    }


}
