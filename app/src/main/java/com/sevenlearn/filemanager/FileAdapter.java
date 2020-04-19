package com.sevenlearn.filemanager;

import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.PopupMenu;
import androidx.recyclerview.widget.RecyclerView;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class FileAdapter extends RecyclerView.Adapter<FileAdapter.FileViewHolder> {
    private List<File> files;
    private List<File> filteredFiles;
    private FileItemEventListener fileItemEventListener;
    private ViewType viewType = ViewType.ROW;

    public FileAdapter(List<File> files, FileItemEventListener fileItemEventListener) {
        this.files = new ArrayList<>(files);
        this.filteredFiles = this.files;
        this.fileItemEventListener = fileItemEventListener;
    }

    @NonNull
    @Override
    public FileViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new FileViewHolder(LayoutInflater.from(parent.getContext()).inflate(
                viewType == ViewType.ROW.getValue() ? R.layout.item_file : R.layout.item_file_grid, parent, false
        ));
    }

    @Override
    public void onBindViewHolder(@NonNull FileViewHolder holder, int position) {
        holder.bindFile(filteredFiles.get(position));
    }

    @Override
    public int getItemCount() {
        return filteredFiles.size();
    }

    public class FileViewHolder extends RecyclerView.ViewHolder {
        private TextView fileNameTv;
        private ImageView fileIconIv;
        private View moreIv;

        public FileViewHolder(@NonNull View itemView) {
            super(itemView);
            fileNameTv = itemView.findViewById(R.id.tv_file_name);
            fileIconIv = itemView.findViewById(R.id.iv_file);
            moreIv = itemView.findViewById(R.id.iv_file_more);
        }

        public void bindFile(final File file) {
            if (file.isDirectory()) {
                fileIconIv.setImageResource(R.drawable.ic_folder_black_32dp);
            } else
                fileIconIv.setImageResource(R.drawable.ic_file_black_32dp);

            fileNameTv.setText(file.getName());
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    fileItemEventListener.onFileItemClick(file);
                }
            });

            moreIv.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    PopupMenu popupMenu = new PopupMenu(v.getContext(), v);
                    popupMenu.getMenuInflater().inflate(R.menu.menu_file_item, popupMenu.getMenu());
                    popupMenu.show();
                    popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                        @Override
                        public boolean onMenuItemClick(MenuItem item) {
                            switch (item.getItemId()) {
                                case R.id.menuItem_delete:
                                    fileItemEventListener.onDeleteFileItemClick(file);
                                    break;
                                case R.id.menuItem_copy:
                                    fileItemEventListener.onCopyFileItemClick(file);
                                    break;
                                case R.id.menuItem_move:
                                    fileItemEventListener.onMoveFileItemClick(file);
                                    break;
                            }
                            return true;
                        }
                    });
                }
            });
        }
    }

    public void deleteFile(File file) {
        int index = files.indexOf(file);
        if (index > -1) {
            files.remove(index);
            notifyItemRemoved(index);
        }
    }

    public interface FileItemEventListener {
        void onFileItemClick(File file);

        void onDeleteFileItemClick(File file);

        void onCopyFileItemClick(File file);

        void onMoveFileItemClick(File file);
    }

    public void addFile(File file) {
        files.add(0, file);
        notifyItemInserted(0);
    }

    public void search(String query) {
        if (query.length() > 0) {
            List<File> result = new ArrayList<>();
            for (File file :
                    this.files) {
                if (file.getName().toLowerCase().contains(query.toLowerCase())) {
                    result.add(file);
                }
            }

            this.filteredFiles = result;
            notifyDataSetChanged();
        } else {
            this.filteredFiles = this.files;
            notifyDataSetChanged();
        }
    }

    @Override
    public int getItemViewType(int position) {
        return viewType.getValue();
    }

    public void setViewType(ViewType viewType) {
        this.viewType = viewType;
        notifyDataSetChanged();
    }
}
