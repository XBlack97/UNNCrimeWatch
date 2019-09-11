/*
 * Professional Android, 4th Edition
 * Reto Meier and Ian Lake
 * Copyright 2018 John Wiley Wiley & Sons, Inc.
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

import android.content.Context;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.AsyncTask;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.x.unncrimewatch.R;
import com.x.unncrimewatch.model.Image;
import com.x.unncrimewatch.roomDB.CW;

import java.util.ArrayList;
import java.util.List;

public class CWRecyclerViewAdapter extends
        RecyclerView.Adapter<CWRecyclerViewAdapter.ViewHolder> {

    private final List<CW> Updates;


    private ArrayList<Image> mImages = new ArrayList<>();
    private ArrayList<String> dbList = new ArrayList<>();

    private NewsImageAdapter mNewsImageAdapter = new NewsImageAdapter(mImages);

    public CWRecyclerViewAdapter(List<CW> updates) {
        Updates = updates;
    }

    private Bitmap image;
    private Uri stringToUri;

    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.list_item_update, parent, false);


        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, int position) {
        Context ctx = holder.ctx;
        holder.cw = Updates.get(position);
        holder.detailsView.setText(Updates.get(position).toString());
        dbList = Updates.get(position).getImageUris();

        if (dbList != null) {
            //holder.imageDetailsView.setVisibility(View.VISIBLE);
            AsyncTask.execute(new Runnable() {
                @Override
                public void run() {
                    for (String uri : dbList) {
                        stringToUri = Uri.parse(uri);
                        try {
                            image = MediaStore.Images.Media.getBitmap(ctx.getContentResolver(), stringToUri);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        mImages.add(new Image(stringToUri, image));
                    }
                }
            });
            //mNewsImageAdapter.setImages(mImages);
        }
        //else {holder.imageDetailsView.setVisibility(View.GONE);}

    }


    @Override
    public int getItemCount() {
        return Updates.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public final View parentView;
        public final TextView detailsView;
        public final RecyclerView imageDetailsView;
        public CW cw;
        public Context ctx;

        public ViewHolder(View view) {
            super(view);
            ctx = view.getContext();
            parentView = view;
            detailsView = view.findViewById(R.id.list_item_update_details);
            imageDetailsView = view.findViewById(R.id.crime_pics_view);

            imageDetailsView.setLayoutManager(new LinearLayoutManager(ctx, LinearLayoutManager.HORIZONTAL,false));
            imageDetailsView.setAdapter(mNewsImageAdapter);


        }

        @Override
        public String toString() {
            return super.toString() + " '" + detailsView.getText() + "'";
        }
    }


}
