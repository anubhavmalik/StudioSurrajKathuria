package com.example.anubhav.SurrajKathuriaClothing.Adapters;

import android.content.Context;
import android.graphics.Bitmap;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.example.anubhav.SurrajKathuriaClothing.Models.CatalogItem;
import com.example.anubhav.modern.R;

import java.util.List;

/**
 * Created by Anubhav on 26-08-2017.
 */

public class CatalogRecyclerAdapter extends RecyclerView.Adapter<CatalogRecyclerAdapter.CatalogViewHolder> {
    int width;
    private Context mContext;
    private List<CatalogItem> arrayList;
    private CatalogRecyclerAdapter.CatalogClickListener mClickListener;

    public CatalogRecyclerAdapter(Context mContext, List<CatalogItem> arrayList, CatalogRecyclerAdapter.CatalogClickListener mClickListener) {
        this.mContext = mContext;
        this.arrayList = arrayList;
        this.mClickListener = mClickListener;
        DisplayMetrics displayMetrics = new DisplayMetrics();
        width = displayMetrics.widthPixels;

    }


    @Override
    public CatalogRecyclerAdapter.CatalogViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        android.view.View itemView = LayoutInflater.from(mContext).inflate(R.layout.catalog_card_layout, parent, false);
        return new CatalogRecyclerAdapter.CatalogViewHolder(itemView, mClickListener);
    }


    @Override
    public void onBindViewHolder(final CatalogViewHolder holder, int position) {
        CatalogItem catalogItem = arrayList.get(position);
        holder.catalogDetailTextView.setText(catalogItem.getDetails());
        holder.catalogUserTextView.setText(catalogItem.getTitle());
        Glide.with(mContext.getApplicationContext())
                .load(catalogItem.getCatalogimageURL())
                .asBitmap()
                .centerCrop()
//                .fitCenter()
//                .override(300,holder.imageView.getHeight())
                .diskCacheStrategy(DiskCacheStrategy.ALL)

                .listener(new RequestListener<String, Bitmap>() {
                    @Override
                    public boolean onException(Exception e, String model, Target<Bitmap> target, boolean isFirstResource) {
                        return false;
                    }

                    @Override
                    public boolean onResourceReady(Bitmap resource, String model, Target<Bitmap> target, boolean isFromMemoryCache, boolean isFirstResource) {
                        holder.mProgressBar.setVisibility(View.GONE);
                        return false;
                    }
                })
                .into(holder.imageView);


    }


    @Override
    public int getItemCount() {
        return arrayList.size();
    }


    public interface CatalogClickListener {
        void onItemClick(View view, int position);
    }

    public static class CatalogViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        ImageView imageView;
        TextView catalogDetailTextView;
        TextView catalogUserTextView;
        CatalogRecyclerAdapter.CatalogClickListener onClickListener;
        ProgressBar mProgressBar;


        public CatalogViewHolder(View itemView, CatalogRecyclerAdapter.CatalogClickListener catalogClickListener) {
            super(itemView);
            itemView.setOnClickListener(this);
            imageView = itemView.findViewById(R.id.catalog_imageView);
            catalogDetailTextView = itemView.findViewById(R.id.catalog_detailtextView);
            catalogUserTextView = itemView.findViewById(R.id.catalog_titletextView);
            mProgressBar = itemView.findViewById(R.id.catalog_imageprogress);
            onClickListener = catalogClickListener;
        }

        @Override
        public void onClick(View view) {
            int id = view.getId();
            int position = getAdapterPosition();
            if (position != RecyclerView.NO_POSITION) {
                if (id == R.id.catalog_cardLayout) {
                    onClickListener.onItemClick(view, position);
                }
            }

        }

    }
}
