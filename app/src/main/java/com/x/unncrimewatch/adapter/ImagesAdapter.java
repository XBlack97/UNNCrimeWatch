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
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;

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

    private ArrayList<Image> mImages;
    private ArrayList<Uri> mImagesUri = new ArrayList<>();
    private Image mimage;
    private Uri uris;

    public ImagesAdapter(ArrayList<Image> images) {
        mImages = images;
    }

    public void setImages(ArrayList<Image> images) {
        mImages = images;
        notifyDataSetChanged();
    }

    @Override
    public ImageViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        return new ImageViewHolder(LayoutInflater.from(parent.getContext())
                .inflate(R.layout.list_pic_update, parent, false));

    }

    @Override
    public void onBindViewHolder(ImageViewHolder holder, final int position) {
        holder.bind(mImages.get(position));
        holder.cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int rpos = holder.getAdatpterpos();
                holder.removeAt(rpos, mImages.get(position).getSource());
            }
        });
    }

    @Override
    public int getItemCount() {
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

        }

        public int getAdatpterpos() {
            return getAdapterPosition();
        }

        public void removeAt(int position, Uri path) {

            try {
                String mPath = path.toString();
                File filepath = new File(mPath);
                File mfiepath = new File(filepath.getAbsolutePath());
                mfiepath.delete();
            } catch(Exception e){
                System.out.println("Not delete !!!!!!!!!!!");

                e.printStackTrace();
            }

            mImages.remove(position);
            notifyItemRemoved(position);
            notifyItemRangeChanged(position, mImages.size());

        }
    }


    public ArrayList<Uri> getImageUris() {

        if (mImages != null) {
            for (Image imageuri : mImages) {
                uris = imageuri.getSource();

                mImagesUri.add(uris);
            }


            return mImagesUri;
        }
        return null;
    }

}
