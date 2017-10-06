package com.sa.testtask.screens;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
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

import butterknife.BindView;
import butterknife.ButterKnife;
import rx.Observable;

public class MainActivity extends AppCompatActivity {


    private static final String IMAGE_POSITION_KEY = "position_key";
    private int position;
    private List<Response.Image> imageList;

    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.image)
    ImageView imageView;
    @BindView(R.id.random)
    Button random;
    @BindView(R.id.select)
    Button select;
    @BindView(R.id.edit_label)
    EditText edit_label;

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
        position = getIntent().getIntExtra(IMAGE_POSITION_KEY, 0);
        imageList = new ArrayList<>();
        imageList.clear();
        imageList.addAll(Storage.getInstance().getImages());
        update(position);
    }

    private void update(int position) {
        Response.Image image = imageList.get(position);
        edit_label.setText(image.getName());
        Picasso.with(this)
                .load(image.getImage())
                .fit()
                .into(imageView);

    }

    private void onRandomClick() {
        RxView.clicks(random)
                .map(aVoid -> getRandomPosition())
                .subscribe(this::update);
    }

    private void onSelectClick() {
        RxView.clicks(select)
                .subscribe(aVoid -> startListActivity());
    }

    private void startListActivity() {

    }

    private int getRandomPosition() {
        Random random = new Random();
        return random.nextInt(imageList.size());
    }


}
