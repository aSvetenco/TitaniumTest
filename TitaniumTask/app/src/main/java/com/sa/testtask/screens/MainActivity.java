package com.sa.testtask.screens;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import com.jakewharton.rxbinding.view.RxView;
import com.sa.testtask.R;
import com.sa.testtask.Storage;
import com.sa.testtask.api.Response;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.TimeUnit;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnTextChanged;
import rx.Observable;
import rx.Subscription;
import rx.subscriptions.CompositeSubscription;

import static android.view.View.Y;

public class MainActivity extends AppCompatActivity {


    private static final String IMAGE_POSITION_KEY = "position_key";
    private static final String TAG = MainActivity.class.getSimpleName();
    private CompositeSubscription subscription = new CompositeSubscription();
    private int position;
    private List<Response.Image> imageList;
    private AlertDialog dialog;

    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.save)
    Button save;
    @BindView(R.id.image)
    ImageView imageView;
    @BindView(R.id.random)
    Button random;
    @BindView(R.id.select)
    Button select;
    @BindView(R.id.edit_label)
    EditText edit_label;

    @OnTextChanged(R.id.edit_label)
    void onEditTextClick(CharSequence s, int start, int before, int count) {
        if (s.length() != count) save.setVisibility(View.VISIBLE);
    }

    public static void start(Context context, int position) {
        Intent intent = new Intent(context, MainActivity.class);
        intent.putExtra(IMAGE_POSITION_KEY, position);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        setSupportActionBar(toolbar);
        position = getIntent().getIntExtra(IMAGE_POSITION_KEY, 0);
        imageList = new ArrayList<>();
        imageList.clear();
        imageList.addAll(Storage.getInstance().getImages());
        update(position);
    }

    @Override
    protected void onResume() {
        super.onResume();
        Subscribe();
    }

    private void Subscribe() {
        subscription.add(onSaveClick());
        subscription.add(onRandomClick());
        subscription.add(onSelectClick());

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.menu_info) {
            showDialog();
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu_layout, menu);
        return true;
    }


    private void update(int position) {
        Response.Image image = imageList.get(position);
        edit_label.setText(image.getName());
        Picasso.with(this)
                .load(image.getImage())
                .fit()
                .into(imageView);
    }

    private Subscription onSaveClick() {
        return RxView.clicks(save)
                .map(aVoid -> edit_label.getText().toString())
                .doOnNext(string -> imageList.get(position).setName(string))
                .subscribe(aVoid -> {
                    save.setVisibility(View.GONE);
                    hideKeybord();
                }, throwable -> Log.d(TAG, throwable.getMessage(), throwable));
    }

    private Subscription onRandomClick() {
        return RxView.clicks(random)
                .map(aVoid -> getRandomPosition())
                .subscribe(this::update
                        , throwable -> Log.d(TAG, throwable.getMessage(), throwable));
    }

    private Subscription onSelectClick() {
        return RxView.clicks(select)
                .subscribe(aVoid -> startListActivity()
                        , throwable -> Log.d(TAG, throwable.getMessage(), throwable));
    }

    private void startListActivity() {
        ListActivity.start(this);
    }

    private int getRandomPosition() {
        Random random = new Random();
        position = random.nextInt(imageList.size());
        return position;
    }

    private void showDialog() {
        if (dialog != null && dialog.isShowing()) {
            dialog.setMessage(getString(R.string.created_by));
        } else {
            dialog = new AlertDialog.Builder(this)
                    .setMessage(getString(R.string.created_by))
                    .setNegativeButton(R.string.dialog_button_ok, (dialog1, which) -> {
                        dialog1.dismiss();
                        dismissDialog().unsubscribe();
                    })
                    .create();
            dialog.setCancelable(false);
            dialog.show();
            dismissDialog();
        }
    }

    private Subscription dismissDialog() {
       return Observable.timer(10, TimeUnit.SECONDS)
                .doOnNext(aLong -> dialog.dismiss())
                .subscribe();

    }

    private void hideKeybord() {
        View view = this.getCurrentFocus();
        if (view != null) {
            InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }

    @Override
    protected void onStop() {
        subscription.clear();
        super.onStop();
    }


}
