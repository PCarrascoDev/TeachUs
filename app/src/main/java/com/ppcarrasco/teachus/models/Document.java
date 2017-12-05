package com.ppcarrasco.teachus.models;

import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;
import com.ppcarrasco.teachus.data.Nodes;

import java.io.Serializable;

/**
 * Created by pedro on 29-11-2017.
 */

public class Document implements Serializable {
    private String author;
    private String authorUid;
    private String name;
    private String downloadUrl;
    private String thumbnailUrl;
    private String key;

    public Document() {
    }

    public Document(String author, String authorUid, String name) {
        this.author = author;
        this.authorUid = authorUid;
        this.name = name;
    }

    public Document(String author, String authorUid, String name, String downloadUrl, String thumbnailUrl, String key) {
        this.author = author;
        this.authorUid = authorUid;
        this.name = name;
        this.downloadUrl = downloadUrl;
        this.thumbnailUrl = thumbnailUrl;
        this.key = key;
    }

    public String getAuthor() {
        return author;
    }

    public String getAuthorUid() {
        return authorUid;
    }

    public String getName() {
        return name;
    }

    public String getKey() {
        return key;
    }

    public String getDownloadUrl() {
        return downloadUrl;
    }

    public String getThumbnailUrl() {
        return thumbnailUrl;
    }

    public void getLikes(final TextView textView){
        new Nodes().getLikes().child(key).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists())
                {
                    Integer val = dataSnapshot.getValue(Integer.class);
                    textView.setText("Likes: " + String.valueOf(val));
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    public void getDislikes(final TextView textView){
        new Nodes().getDislikes().child(key).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists())
                {
                    Integer val = dataSnapshot.getValue(Integer.class);
                    textView.setText("Dislikes: " + String.valueOf(val));
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }
}
