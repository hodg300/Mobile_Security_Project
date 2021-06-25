package com.example.mobile_security_project.adapters;


import android.content.Context;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mobile_security_project.R;
import com.example.mobile_security_project.utils.ListItem;
import com.squareup.picasso.Picasso;

import org.jetbrains.annotations.NotNull;

import java.util.List;

public class MyAdapter  extends RecyclerView.Adapter<MyAdapter.ImageViewHolder> {

    private List<ListItem> listItem;
    private Context context;

    public MyAdapter(List<ListItem> listItem, Context context) {
        this.listItem = listItem;
        this.context = context;
    }


    @NonNull
    @NotNull
    @Override
    public ImageViewHolder onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).
                inflate(R.layout.list_item, parent, false);
        return new ImageViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull @NotNull ImageViewHolder holder, int position) {
        ListItem listItem = this.listItem.get(position);
        holder.textViewType.setText(listItem.getType());
        holder.textViewName.setText(listItem.getName());
        holder.textViewAge.setText(String.valueOf(listItem.getAge()));
        Picasso.get().load(Uri.parse(listItem.getImageUrl())).into(holder.imageView);
    }

    @Override
    public int getItemCount() {
        return this.listItem.size();
    }

    public static class ImageViewHolder extends RecyclerView.ViewHolder{

        ImageView imageView;
        TextView textViewType;
        TextView textViewName;
        TextView textViewAge;

        public ImageViewHolder(@NonNull @NotNull View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.imageView);
            textViewType = itemView.findViewById(R.id.textViewType);
            textViewName = itemView.findViewById(R.id.textViewName);
            textViewAge = itemView.findViewById(R.id.textViewAge);
        }
    }
}



