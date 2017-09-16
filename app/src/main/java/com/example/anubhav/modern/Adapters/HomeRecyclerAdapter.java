package com.example.anubhav.modern.Adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.anubhav.modern.Models.PostItem;
import com.example.anubhav.modern.R;

import java.util.ArrayList;

/**
 * Created by Anubhav on 26-08-2017.
 */

public class HomeRecyclerAdapter extends RecyclerView.Adapter<HomeRecyclerAdapter.HomeViewHolder> {
    Context mContext;
    ImageView imageView;
    TextView detailTextView;
    TextView timeTextView;
    TextView dateTextView;
    ArrayList<PostItem> arrayList;
    HomeClickListener mClickListener;

    public HomeRecyclerAdapter(Context mContext, ImageView imageView, TextView detailTextView, TextView timeTextView, TextView dateTextView, ArrayList<PostItem> arrayList,HomeClickListener mClickListener) {
        this.mContext = mContext;
        this.imageView = imageView;
        this.detailTextView = detailTextView;
        this.timeTextView = timeTextView;
        this.dateTextView = dateTextView;
        this.arrayList = arrayList;
        this.mClickListener=mClickListener;
    }


    @Override
    public HomeViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView= LayoutInflater.from(mContext).inflate(R.layout.catalogfragmentlayout,parent,false);
        return  new HomeViewHolder(itemView,mClickListener);
    }

    @Override
    public void onBindViewHolder(HomeViewHolder holder, int position) {
        PostItem toDoItem = arrayList.get(position);
        holder.homeDetailTextView.setText(Po);


    }


    @Override
    public int getItemCount() {
        return 0;
    }


    public interface HomeClickListener {
        void onItemClick(View view, int position);
    }

    public static class HomeViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        ImageView imageView;
        TextView homeTimeTextView;
        TextView homeDateTextView;
        TextView homeDetailTextView;
        HomeClickListener onClickListener;


        public HomeViewHolder(View itemView, HomeClickListener onClickListener) {
            super(itemView);
            imageView = itemView.findViewById(R.id.home_imageView);
            homeDateTextView = itemView.findViewById(R.id.homedate_textView);
            homeDetailTextView = itemView.findViewById(R.id.homedetail_textView);
            homeTimeTextView = itemView.findViewById(R.id.hometime_textView);
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
