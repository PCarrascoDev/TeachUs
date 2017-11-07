package com.ppcarrasco.teachus.adapters;

import android.graphics.Bitmap;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.github.lzyzsd.circleprogress.DonutProgress;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.ppcarrasco.teachus.R;
import com.ppcarrasco.teachus.data.Document;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.util.List;

/**
 * Created by Pedro on 17-10-2017.
 */

public class UploadAdapter extends RecyclerView.Adapter<UploadAdapter.ViewHolder>{
    private List<Document> list;
    private StorageReference storageRef;


    public UploadAdapter(List<Document> list) {
        this.list = list;
        storageRef = FirebaseStorage.getInstance().getReference();
        //documentsRef = new Nodes().getDocuments();
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_document, parent, false);
        ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Document document = list.get(position);
        holder.thumbnail.setImageBitmap(document.getThumbnail());
        holder.name.setText(document.getName());
        //holder.name.setText(String.valueOf(document.getProgress()));
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

    public void addItem(Document document, File file)
    {
        list.add(document);
        notifyDataSetChanged();
        uploadThumbnail(storageRef.child("thumbnails"), document.getThumbnail(), file.getName());
        uploadDocument(storageRef.child("documents"), file, list.size()-1);
    }

    private void uploadThumbnail(StorageReference reference, Bitmap bitmap, String name)
    {
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);
        UploadTask uploadTask = reference.child(name.split("\\.")[0] + "png").putBytes(stream.toByteArray());
    }

    private void uploadDocument(StorageReference reference, File file, final int position)
    {
        UploadTask uploadTask = reference.child(file.getName()).putFile(Uri.fromFile(file));
        //new MyProgress(position).execute(uploadTask);

        uploadTask.addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
                double progress = (100.0 * taskSnapshot.getBytesTransferred()) / taskSnapshot.getTotalByteCount();
                list.get(position).setProgress(progress);
                notifyDataSetChanged();
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

            name = (TextView) itemView.findViewById(R.id.documentTv);
            author = (TextView) itemView.findViewById(R.id.authorTv);
            thumbnail = (ImageView) itemView.findViewById(R.id.documentIv);
            progressBar = (DonutProgress) itemView.findViewById(R.id.uploadPb);
            check = (ImageView) itemView.findViewById(R.id.checkIv);
        }
    }

}
