package com.example.project_2;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.project_2.Models.plantGalleryModel;

import java.util.List;

public class plantGalleryAdapter  extends RecyclerView.Adapter<plantGalleryAdapter.plantGalleryViewHolder> {
    Context context;
    List<plantGalleryModel> modelList;
    private onGalleryListener monGalleryListener;

    public plantGalleryAdapter(Context context, List<plantGalleryModel> modelList , onGalleryListener onGalleryListener) {
        this.context = context;
        this.modelList = modelList;
        this.monGalleryListener=onGalleryListener;
    }

    @NonNull
    @Override
    public plantGalleryViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.plant_gallery, parent, false);

        return new plantGalleryViewHolder(view , monGalleryListener);     }

    @Override
    public void onBindViewHolder(@NonNull plantGalleryViewHolder holder, int position) {
        holder.galleryName.setText(modelList.get(position).getName());
        holder.galleryIcon.setBackgroundResource(modelList.get(position).getIconURL());
        holder.galleryBackground.setBackgroundResource(modelList.get(position).getBackgroundURL());

    }

    @Override
    public int getItemCount() {
        return modelList.size();
    }

    public class plantGalleryViewHolder extends RecyclerView.ViewHolder implements  View.OnClickListener{
        TextView galleryName;
        ImageView galleryBackground;
        ImageView galleryIcon;
        onGalleryListener onGalleryListener;
        public plantGalleryViewHolder(@NonNull View itemView , onGalleryListener onGalleryListener) {
            super(itemView);
            galleryName=itemView.findViewById(R.id.galleryName);
            galleryBackground=itemView.findViewById(R.id.galleryBackground);
            galleryIcon=itemView.findViewById(R.id.galleryIcon);
            this.onGalleryListener=onGalleryListener;
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            onGalleryListener.onGalleryClick(getAdapterPosition());
        }
    }
    public interface onGalleryListener{
        void onGalleryClick(int position);
    }
}

