package com.sa.testtask.screens;

import android.app.ProgressDialog;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.sa.testtask.R;
import com.sa.testtask.Storage;
import com.sa.testtask.api.Api;
import com.sa.testtask.api.Response;

import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class SplashActivity extends AppCompatActivity {

    private static final String TAG = SplashActivity.class.getSimpleName();
    private ProgressDialog progress;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

    }

    @Override
    protected void onResume() {
        super.onResume();
        request();
    }

    private void request() {
        isNetworkAvailble()
                .filter(aBoolean -> {
                    if (!aBoolean) {
                        showToast(getString(R.string.no_network));
                        return false;
                    } else {
                        return true;
                    }
                })
                .doOnNext(aBoolean -> showProgressDialog())
                .observeOn(Schedulers.io())
                .flatMap(integer -> Api.getImagesApi().getImages())
                .filter(response -> response != null)
                .map(Response::getImages)
                .filter(images -> images != null && images.size() > 0)
                .doOnNext(images -> Storage.getInstance().setImages(images))
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(images -> startMainActivity(),
                        throwable -> Log.d(TAG, throwable.getMessage(), throwable));
    }

    private void startMainActivity() {
        hideProgressDialog();
        MainActivity.start(this, 0);
        finish();
    }

    private void showProgressDialog() {
        if (progress == null) {
            progress = new ProgressDialog(this);
        }
        progress.setMessage(getString(R.string.loading));
        progress.show();
    }

    private void hideProgressDialog() {
        if (progress.isShowing()) {
            progress.dismiss();
        }
    }



    public Observable<Boolean> isNetworkAvailble() {
        ConnectivityManager connectivityManager =
                (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return Observable.defer(() -> Observable.just(activeNetworkInfo != null && activeNetworkInfo.isConnected()));
    }

    private void showToast(String message) {
        Toast.makeText(this, message, Toast.LENGTH_LONG).show();
    }




}

