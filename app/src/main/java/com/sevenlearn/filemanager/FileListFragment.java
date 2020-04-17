package com.sevenlearn.filemanager;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Arrays;

public class FileListFragment extends Fragment implements FileAdapter.FileItemEventListener {
    private String path;
    private FileAdapter fileAdapter;
    private RecyclerView recyclerView;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        path = getArguments().getString("path");
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_files, container, false);
        recyclerView = view.findViewById(R.id.rv_files);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext(), RecyclerView.VERTICAL, false));
        File currentFolder = new File(path);
        File[] files = currentFolder.listFiles();
        fileAdapter = new FileAdapter(Arrays.asList(files), this);
        recyclerView.setAdapter(fileAdapter);
        TextView pathTv = view.findViewById(R.id.tv_files_path);

        pathTv.setText(currentFolder.getName().equalsIgnoreCase("files") ? "External Storage" : currentFolder.getName());

        view.findViewById(R.id.iv_files_back).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().onBackPressed();
            }
        });
        return view;
    }

    @Override
    public void onFileItemClick(File file) {
        if (file.isDirectory()) {
            ((MainActivity) getActivity()).listFiles(file.getPath());
        }
    }

    @Override
    public void onDeleteFileItemClick(File file) {
        if (file.delete()) {
            fileAdapter.deleteFile(file);
        }
    }

    @Override
    public void onCopyFileItemClick(File file) {
        try {
            copy(file, getDestinationFile(file.getName()));
            Toast.makeText(getContext(), "File is copied", Toast.LENGTH_SHORT).show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onMoveFileItemClick(File file) {
        try {
            copy(file, getDestinationFile(file.getName()));
            onDeleteFileItemClick(file);
            Toast.makeText(getContext(), "File is copied", Toast.LENGTH_SHORT).show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private File getDestinationFile(String fileName) {
        return new File(getContext().getExternalFilesDir(null).getPath() + File.separator + "Destination" + File.separator + fileName);
    }

    public void createNewFolder(String folderName) {
        File newFolder = new File(path + File.separator + folderName);
        if (!newFolder.exists()) {
            if (newFolder.mkdir()) {
                fileAdapter.addFile(newFolder);
                recyclerView.smoothScrollToPosition(0);
            }
        }
    }

    private void copy(File source, File destination) throws IOException {
        FileInputStream fileInputStream = new FileInputStream(source);
        FileOutputStream fileOutputStream = new FileOutputStream(destination);
        byte[] buffer = new byte[1024];
        int length;
        while ((length = fileInputStream.read(buffer)) > 0) {
            fileOutputStream.write(buffer, 0, length);
        }

        fileInputStream.close();
        fileOutputStream.close();
    }

}
