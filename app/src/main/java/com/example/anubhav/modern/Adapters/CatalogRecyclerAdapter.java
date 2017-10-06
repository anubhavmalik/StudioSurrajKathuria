package com.example.anubhav.modern.Adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.anubhav.modern.Models.CatalogItem;
import com.example.anubhav.modern.R;

import java.util.List;

/**
 * Created by Anubhav on 26-08-2017.
 */

public class CatalogRecyclerAdapter extends RecyclerView.Adapter<CatalogRecyclerAdapter.CatalogViewHolder> {
    private Context mContext;
    private ImageView imageView;
    private List<CatalogItem> arrayList;
    private CatalogRecyclerAdapter.CatalogClickListener mClickListener;
//    PostItem postItem;

    public CatalogRecyclerAdapter(Context mContext, List<CatalogItem> arrayList, CatalogRecyclerAdapter.CatalogClickListener mClickListener) {
        this.mContext = mContext;
        this.arrayList = arrayList;
        this.mClickListener = mClickListener;
    }


    @Override
    public CatalogRecyclerAdapter.CatalogViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        android.view.View itemView = LayoutInflater.from(mContext).inflate(R.layout.catalogcardlayout, parent, false);
        return new CatalogRecyclerAdapter.CatalogViewHolder(itemView, mClickListener);
    }


    @Override
    public void onBindViewHolder(CatalogViewHolder holder, int position) {
        CatalogItem catalogItem = arrayList.get(position);
        holder.catalogDetailTextView.setText(catalogItem.getDetails());
        holder.catalogUserTextView.setText(catalogItem.getTitle());
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


        public CatalogViewHolder(View itemView, CatalogRecyclerAdapter.CatalogClickListener catalogClickListener) {
            super(itemView);
            itemView.setOnClickListener(this);
            imageView = itemView.findViewById(R.id.catalog_imageView);
            catalogDetailTextView = itemView.findViewById(R.id.catalog_detailtextView);
            catalogUserTextView = itemView.findViewById(R.id.catalog_titletextView);
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
