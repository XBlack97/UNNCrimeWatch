/*
 * Copyright 2017 Google Inc. All rights reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.x.unncrimewatch.adapter;

import android.net.Uri;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.x.unncrimewatch.R;
import com.x.unncrimewatch.model.Image;

import java.io.File;
import java.util.ArrayList;

/**
 * A RecyclerView adapter that displays {@link Image}s. Includes a callback for when an item has
 * been clicked.
 */
public class ImagesAdapter extends RecyclerView.Adapter<ImagesAdapter.ImageViewHolder> {

    private ArrayList<Image> mImages = new ArrayList<>();
    private ArrayList<Uri> mImagesUri = new ArrayList<>();
    private Uri uris;

    private String mPath;
    private File filepath;

    private static final String TAG = "ImagesAdapter";

    public ImagesAdapter(ArrayList<Image> images) {
        mImages = images;

        if (mImages == null)
            Log.d(TAG, "ImagesAdapter mImages = null");

    }

    @Override
    public ImageViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        return new ImageViewHolder(LayoutInflater.from(parent.getContext())
                .inflate(R.layout.list_pic_update, parent, false));

    }

    @Override
    public void onBindViewHolder(ImageViewHolder holder, final int position) {
        holder.bind(mImages.get(position));
        holder.cancel.setOnClickListener(view -> {
            int rpos = holder.getAdatpterpos();
            removeAt(rpos, mImages.get(position).getSource(), view);

            Log.d(TAG, "imagesAdapter position = " + position);
        });
    }

    @Override
    public int getItemCount() {

        Log.d(TAG, "imagesAdapter getItemCount = " + mImages.size());
        return mImages.size();


    }


    public class ImageViewHolder extends RecyclerView.ViewHolder {

        protected ImageView imageView;
        private ImageButton cancel;

        public ImageViewHolder(View itemView) {
            super(itemView);

            imageView = itemView.findViewById(R.id.news_pic_update_details);
            cancel = itemView.findViewById(R.id.cancel_image);

        }

        public void bind(Image image) {

            imageView.setImageBitmap(image.getImage());
            imageView.setImageURI(image.getSource());

            Log.d(TAG, "imagesAdapter bind successful");

        }

        public int getAdatpterpos() {
            return getAdapterPosition();
        }

    }


    public void removeAt(int position, Uri path, View view) {

        try {
            String mPath = path.toString();
            File filepath = new File(mPath);
            File mfiepath = new File(filepath.getAbsolutePath());
            mfiepath.delete();
        } catch (Exception e) {
            System.out.println("Not delete !!!!!!!!!!!");

            //Log.e(TAG, "No Delete !!" + mPath + mfilepath);
            e.printStackTrace();
        }

        mImages.remove(position);
        notifyItemRemoved(position);
        notifyItemRangeChanged(position, mImages.size());
        Log.d(TAG, "remove size = " + mImages.size());
        notifyDataSetChanged();


    }


    public ArrayList<Uri> getImageUris() {

        if (!mImages.isEmpty()) {

            Log.d(TAG, "getImagesUris != Null \n mImages.size() = " + mImages.size());

            for (Image imageuri : mImages) {
                uris = imageuri.getSource();

                mImagesUri.add(uris);
            }
            Log.d(TAG, "getImagesUris != Null" + mImagesUri);

            return mImagesUri;


        } else

            Log.d(TAG, "getImagesUris = Null \n mImages.size() = " + mImages.size());

        return null;

    }

}
