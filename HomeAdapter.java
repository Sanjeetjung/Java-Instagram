package com.atwebpages.awaillixa.merofirebase;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.util.List;



    public class HomeAdapter extends RecyclerView.Adapter<HomeAdapter.ViewHolder> {

        List<DAO> HomeList;
        OnFeedClickHandleListener FeedClickHandleListener;


        public HomeAdapter(List<DAO> HomeList, OnFeedClickHandleListener FeedClickHandleListener) {
            this.HomeList = HomeList;
            this.FeedClickHandleListener=FeedClickHandleListener;
        }

        @NonNull
        @Override
        public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_feed,parent, false);
            return new ViewHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
            holder.bindView(HomeList.get(position));
        }

        @Override
        public int getItemCount() {
            return HomeList.size();
        }

        class ViewHolder extends RecyclerView.ViewHolder {
            TextView title, description;
            ImageView image;


            public ViewHolder(View itemView) {
                super(itemView);

                title = itemView.findViewById(R.id.title1);
                description= itemView.findViewById(R.id.description1);
                image = itemView.findViewById(R.id.images);


            }

            public void bindView(final DAO post) {
                Log.d("create","::"+post.getDescription());
                title.setText(post.getCaption());
                description.setText(post.getDescription());
                Log.d("create",post.getImageUrl());
                if(!TextUtils.isEmpty(post.getImageUrl())) {
                    Glide.with(image.getContext()).load(post.getImageUrl()).into(image);
                }else {
                    image.setVisibility(View.GONE);
                }

                itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        FeedClickHandleListener.onFeedClicked(post);
                    }
                });

            }



        }
        interface OnFeedClickHandleListener{
            void onFeedClicked(DAO post);
        }

    }

