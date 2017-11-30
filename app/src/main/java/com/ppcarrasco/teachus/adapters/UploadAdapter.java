package com.ppcarrasco.teachus.adapters;

import android.graphics.Bitmap;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.github.lzyzsd.circleprogress.DonutProgress;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.ppcarrasco.teachus.R;
import com.ppcarrasco.teachus.data.Nodes;
import com.ppcarrasco.teachus.models.UploadDocument;

import java.io.ByteArrayOutputStream;
import java.util.List;

/**
 * Created by Pedro on 17-10-2017.
 */

public class UploadAdapter extends RecyclerView.Adapter<UploadAdapter.ViewHolder>{
    private List<UploadDocument> list;
    private StorageReference storageRef;


    public UploadAdapter(List<UploadDocument> list) {
        this.list = list;
        storageRef = FirebaseStorage.getInstance().getReference();
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_document, parent, false);
        ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        UploadDocument document = list.get(position);
        holder.thumbnail.setImageBitmap(document.getThumbnail());
        holder.name.setText(document.getName());
        if ((int) document.getProgress() < 100)
        {
            holder.progressBar.setProgress((int) document.getProgress());
        }
        else
        {
            holder.progressBar.setVisibility(View.GONE);
            holder.check.setVisibility(View.VISIBLE);
        }
        holder.author.setText(document.getAuthor());
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public void addItem(UploadDocument document)
    {
        list.add(document);
        notifyDataSetChanged();
        DatabaseReference documentsReference = new Nodes().getDocuments();
        String key = documentsReference.push().getKey();
        uploadThumbnail(document, key);
    }

    private void uploadToDB(UploadDocument document, String downloadUrl, String thumbnailUrl){
        DatabaseReference documentsReference = new Nodes().getDocuments();
        String key = documentsReference.push().getKey();
        documentsReference.child(key).setValue(document.makeDocument(key, downloadUrl, thumbnailUrl));
    }

    private void uploadThumbnail(final UploadDocument document, final String key)
    {
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        document.getThumbnail().compress(Bitmap.CompressFormat.PNG, 50, stream);
        UploadTask uploadTask = new Nodes().getStorageThumbnails().child(key).putBytes(stream.toByteArray());
        uploadTask.addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                uploadDocument(document, key, String.valueOf(taskSnapshot.getDownloadUrl()), list.size()-1);
            }
        });
    }

    private void uploadDocument(final UploadDocument document, String key, final String thumbnailUrl, final int position)
    {
        UploadTask uploadTask = new Nodes().getStorageDocuments().child(key).putFile(document.getUri());

        uploadTask.addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
                double progress = (100.0 * taskSnapshot.getBytesTransferred()) / taskSnapshot.getTotalByteCount();
                list.get(position).setProgress(progress);
                notifyDataSetChanged();
            }
        });

        uploadTask.addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                uploadToDB(document, String.valueOf(taskSnapshot.getDownloadUrl()), thumbnailUrl);
            }
        });
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        private TextView name;
        private TextView author;
        private ImageView thumbnail;
        private DonutProgress progressBar;
        private ImageView check;

        public ViewHolder(View itemView) {
            super(itemView);
            itemView.findViewById(R.id.uploadPb).setVisibility(View.VISIBLE);
            name = (TextView) itemView.findViewById(R.id.documentTv);
            author = (TextView) itemView.findViewById(R.id.authorTv);
            thumbnail = (ImageView) itemView.findViewById(R.id.documentIv);
            progressBar = (DonutProgress) itemView.findViewById(R.id.uploadPb);
            check = (ImageView) itemView.findViewById(R.id.checkIv);
        }
    }

}
