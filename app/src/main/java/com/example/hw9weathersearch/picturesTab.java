package com.example.hw9weathersearch;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import java.util.ArrayList;

public class picturesTab  extends Fragment {

    private ImageView imageView;
    RecyclerView recyclerView;
    GridLayoutManager gridLayoutManager;

    @Override
    public View onCreateView(
            @NonNull LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.pictures_tab, container, false);
        imageView = (ImageView) root.findViewById(R.id.picimg);
        recyclerView = (RecyclerView) root.findViewById(R.id.recyclerView);
        gridLayoutManager = new GridLayoutManager(getActivity().getApplicationContext(), 1);
        recyclerView.setLayoutManager(gridLayoutManager);

        ArrayList imageUrlList = prepareData();
        imgDataAdapter dataAdapter = new imgDataAdapter(getActivity().getApplicationContext(), imageUrlList);
        recyclerView.setAdapter(dataAdapter);
        System.out.println("Pictures are found....");
        return root;
    }

    private ArrayList prepareData() {

        detailactivity activity = (detailactivity) getActivity();
        String imageUrls[] = activity.getImageURLS();

        ArrayList imageUrlList = new ArrayList<>();
        for (int i = 0; i < imageUrls.length; i++) {
            ImageUrl imageUrl = new ImageUrl();
            imageUrl.setImageUrl(imageUrls[i]);
            imageUrlList.add(imageUrl);
        }
        return imageUrlList;
    }

}

