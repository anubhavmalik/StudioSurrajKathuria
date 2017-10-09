package com.example.anubhav.modern.Adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.example.anubhav.modern.Models.PostItem;
import com.example.anubhav.modern.R;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by Anubhav on 26-08-2017.
 */

public class HomeRecyclerAdapter extends RecyclerView.Adapter<HomeRecyclerAdapter.HomeViewHolder> {
    int width;
    private Context mContext;
    private ArrayList<PostItem> arrayList;
    private HomeClickListener mClickListener;


    public HomeRecyclerAdapter(Context mContext, ArrayList<PostItem> arrayList, HomeClickListener mClickListener) {
        this.mContext = mContext;
        this.arrayList = arrayList;
        this.mClickListener=mClickListener;
        DisplayMetrics displayMetrics = new DisplayMetrics();
        width = displayMetrics.widthPixels;
    }


    @Override
    public HomeViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(mContext).inflate(R.layout.homecardlayout, parent, false);
        return  new HomeViewHolder(itemView,mClickListener);
    }

    @Override
    public void onBindViewHolder(HomeViewHolder holder, int position) {
        PostItem postItem = arrayList.get(position);
        holder.homeDetailTextView.setText(postItem.getDetails());
        holder.homeTimeTextView.setText(postItem.getTime());
        holder.homeDateTextView.setText(postItem.getDate());
        holder.homeUserTextView.setText(postItem.getBy_user());
        Glide.with(mContext.getApplicationContext())
                .load(postItem.getPostImageURL())
                .asBitmap()
                .centerCrop()
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .into(holder.postImageView);

        Glide.with(mContext.getApplicationContext())
                .load(postItem.getUserImageURL())
                .asBitmap()
                .centerCrop()
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .into(holder.homeCircularImageView);
    }


    @Override
    public int getItemCount() {
        return arrayList.size();
    }


    public interface HomeClickListener {
        void onItemClick(View view, int position);
    }

    public static class HomeViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        ImageView postImageView;
        TextView homeTimeTextView;
        TextView homeDateTextView;
        TextView homeDetailTextView;
        CircleImageView homeCircularImageView;
        TextView homeUserTextView;
        HomeClickListener onClickListener;


        public HomeViewHolder(View itemView, HomeClickListener homeClickListener) {
            super(itemView);
            itemView.setOnClickListener(this);
            postImageView = itemView.findViewById(R.id.home_imageView);
            homeDateTextView = itemView.findViewById(R.id.homedate_textView);
            homeDetailTextView = itemView.findViewById(R.id.homedetail_textView);
            homeTimeTextView = itemView.findViewById(R.id.hometime_textView);
            homeCircularImageView = itemView.findViewById(R.id.circular_imageView);
            homeUserTextView = itemView.findViewById(R.id.user_textView);
            onClickListener = homeClickListener;
        }

        @Override
        public void onClick(View view) {
            int id = view.getId();
            int position = getAdapterPosition();
            if (position != RecyclerView.NO_POSITION) {
                if (id == R.id.home_cardLayout) {
                    onClickListener.onItemClick(view, position);
                }
            }

        }

    }
}
