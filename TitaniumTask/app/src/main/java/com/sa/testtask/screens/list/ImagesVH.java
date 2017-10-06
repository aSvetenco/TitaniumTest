package com.sa.testtask.screens.list;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.sa.testtask.R;
import com.squareup.picasso.Picasso;

import butterknife.BindView;
import butterknife.ButterKnife;



public class ImagesVH extends RecyclerView.ViewHolder {

    @BindView(R.id.image)
    ImageView imageView;
    @BindView(R.id.title)
    TextView title;
    @BindView(R.id.number)
    TextView number;

    ImagesVH(View view) {
        super(view);
        ButterKnife.bind(this, view);
    }

    public void bind(String image, String title, String position) {
        this.title.setText(title);
        this.number.setText(position);
        Picasso.with(itemView.getContext())
                .load(image)
                .fit()
                .into(imageView);
    }
}