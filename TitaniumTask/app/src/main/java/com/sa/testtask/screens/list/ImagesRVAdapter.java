package com.sa.testtask.screens.list;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.jakewharton.rxbinding.view.RxView;
import com.sa.testtask.R;
import com.sa.testtask.api.Response;

import java.util.ArrayList;
import java.util.List;

import rx.Observable;
import rx.subjects.PublishSubject;


public class ImagesRVAdapter extends RecyclerView.Adapter<ImagesVH> {

    private final PublishSubject<View> publishSubject = PublishSubject.create();

    private List<Response.Image> imageList = new ArrayList<>();

    public ImagesRVAdapter() {
    }

    public void swapData(List<Response.Image> imageList) {
        this.imageList.clear();
        this.imageList.addAll(imageList);
        notifyDataSetChanged();
    }

    public Observable<View> getViewClickedObservable() {
        return publishSubject.asObservable();
    }
    @Override
    public ImagesVH onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.item_image, parent, false);
        RxView.clicks(view).takeUntil(RxView.detaches(parent)).map(aVoid -> view).subscribe(publishSubject);
        return new ImagesVH(view);

    }

    @Override
    public void onBindViewHolder(ImagesVH holder, int position) {
        Response.Image image = imageList.get(position);
        holder.bind(image.getImage(), image.getName(), getPosition(position));

    }

    private String getPosition(int position) {
        return String.valueOf(position + 1);
    }

    @Override
    public int getItemCount() {
        return imageList.size();
    }

    private Response.Image getImage(int position) {
        return imageList.get(position);
    }

}