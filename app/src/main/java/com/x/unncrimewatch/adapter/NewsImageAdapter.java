package com.x.unncrimewatch.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.recyclerview.widget.RecyclerView;

import com.x.unncrimewatch.R;
import com.x.unncrimewatch.model.Image;

import java.io.File;
import java.util.ArrayList;

public class NewsImageAdapter extends RecyclerView.Adapter<NewsImageAdapter.NewsImageViewHolder> {

    private ArrayList<Image> mImages;
    private Image mimage;
    File file;

    public NewsImageAdapter(ArrayList<Image> images) {
        mImages = images;
    }

    public void setImages(ArrayList<Image> images) {
        mImages = images;
        notifyDataSetChanged();
    }

    @Override
    public NewsImageViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        return new NewsImageViewHolder(LayoutInflater.from(parent.getContext())
                .inflate(R.layout.news_pic, parent, false));

    }

    @Override
    public void onBindViewHolder(NewsImageViewHolder holder, final int position) {
        holder.bind(mImages.get(position));
    }

    @Override
    public int getItemCount() {
        return mImages.size();


    }

    public class NewsImageViewHolder extends RecyclerView.ViewHolder {

        protected ImageView imageView;

        public NewsImageViewHolder(View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.news_pic_details);
        }

        public void bind(Image image) {

            imageView.setImageBitmap(image.getImage());
            imageView.setImageURI(image.getSource());

        }

    }


}
