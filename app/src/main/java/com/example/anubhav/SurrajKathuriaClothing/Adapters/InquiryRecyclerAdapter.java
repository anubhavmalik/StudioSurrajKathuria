package com.example.anubhav.SurrajKathuriaClothing.Adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.anubhav.SurrajKathuriaClothing.Models.InquiryItem;
import com.example.anubhav.SurrajKathuriaClothing.Models.UserItem;
import com.example.anubhav.modern.R;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;

/**
 * Created by Anubhav on 14-10-2017.
 */

public class InquiryRecyclerAdapter extends RecyclerView.Adapter<InquiryRecyclerAdapter.InquiryViewHolder> {
    int width;
    FirebaseFirestore db;
    UserItem userItem;
    private Context mContext;
    private ArrayList<InquiryItem> arrayList;
    private InquiryRecyclerAdapter.InquiryClickListener mClickListener;


    public InquiryRecyclerAdapter(Context mContext, ArrayList<InquiryItem> arrayList, InquiryRecyclerAdapter.InquiryClickListener mClickListener) {
        this.mContext = mContext;
        this.arrayList = arrayList;
        this.mClickListener = mClickListener;
        DisplayMetrics displayMetrics = new DisplayMetrics();
        width = displayMetrics.widthPixels;
        db = FirebaseFirestore.getInstance();
    }


    @Override
    public InquiryRecyclerAdapter.InquiryViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(mContext).inflate(R.layout.inquiry_card_layout, parent, false);
        return new InquiryRecyclerAdapter.InquiryViewHolder(itemView, mClickListener);
    }

    @Override
    public void onBindViewHolder(final InquiryRecyclerAdapter.InquiryViewHolder holder, int position) {
        InquiryItem inquiryItem = arrayList.get(position);
        holder.inquiryDetailTextView.setText(inquiryItem.getDetails());
        holder.inquiryTimeTextView.setText(inquiryItem.getTime());
        holder.inquiryDateTextView.setText(inquiryItem.getDate());
        holder.inquiryUserTextView.setText(inquiryItem.getByuser());
        holder.inquiryByUserNumber.setText(inquiryItem.getUsernumber());


        /**           TO FETCH THE USERNAME FROM DATABASE WHICH WILL BE UPDATED
         *
         * db.collection("users").document(postItem.getBy_userNumber()).get()
         .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
        @Override public void onComplete(@NonNull Task<DocumentSnapshot> task) {
        holder.homeUserTextView.setText(userItem.getName());
        }
        })
         .addOnFailureListener(new OnFailureListener() {

        @Override public void onFailure(@NonNull Exception e) {
        Log.d("PICTUREFETCHADAPTER", "Fetch has failed ");
        }
        });     */

    }


    @Override
    public int getItemCount() {
        return arrayList.size();
    }


    public interface InquiryClickListener {
        void onItemClick(View view, int position);
    }

    public static class InquiryViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView inquiryTimeTextView;
        TextView inquiryDateTextView;
        TextView inquiryByUserNumber;
        TextView inquiryDetailTextView;
        TextView inquiryUserTextView;
        InquiryRecyclerAdapter.InquiryClickListener onClickListener;


        public InquiryViewHolder(View itemView, InquiryRecyclerAdapter.InquiryClickListener inquiryClickListener) {
            super(itemView);
            itemView.setOnClickListener(this);
            inquiryDateTextView = itemView.findViewById(R.id.inquiry_date_textView);
            inquiryDetailTextView = itemView.findViewById(R.id.inquirydetail_textView);
            inquiryTimeTextView = itemView.findViewById(R.id.inquiry_time_textView);
            inquiryUserTextView = itemView.findViewById(R.id.inquiry_user_textView);
            inquiryByUserNumber = itemView.findViewById(R.id.inquiry_user_number_textView);
            onClickListener = inquiryClickListener;
        }

        @Override
        public void onClick(View view) {
            int id = view.getId();
            int position = getAdapterPosition();
            if (position != RecyclerView.NO_POSITION) {
                if (id == R.id.inquiry_cardLayout) {
                    onClickListener.onItemClick(view, position);
                }
            }

        }

    }
}
