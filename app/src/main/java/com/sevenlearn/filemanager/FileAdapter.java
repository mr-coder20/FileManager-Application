package com.sevenlearn.filemanager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class FileAdapter extends RecyclerView.Adapter<FileAdapter.FileViewHolder> {

    @NonNull
    @Override
    public FileViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new FileViewHolder(LayoutInflater.from(parent.getContext()).inflate(
                R.layout.item_file, parent, false
        ));
    }

    @Override
    public void onBindViewHolder(@NonNull FileViewHolder holder, int position) {

    }

    @Override
    public int getItemCount() {
        return 20;
    }

    public static class FileViewHolder extends RecyclerView.ViewHolder {

        public FileViewHolder(@NonNull View itemView) {
            super(itemView);
        }
    }
}
