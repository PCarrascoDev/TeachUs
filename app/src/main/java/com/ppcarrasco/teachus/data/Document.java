package com.ppcarrasco.teachus.data;

import android.graphics.Bitmap;

/**
 * Created by Pedro on 17-10-2017.
 */

public class Document {
    private String name;
    private String author;
    private double progress;
    private Bitmap thumbnail;

    public Document(String name, String author, Bitmap thumbnail) {
        this.name = name;
        this.author = author;
        this.thumbnail = thumbnail;
        progress = 0;
    }

    public String getName() {
        return name;
    }

    public String getAuthor() {
        return author;
    }

    public Bitmap getThumbnail() {
        return thumbnail;
    }

    public double getProgress() {
        return progress;
    }

    public void setProgress(double progress) {
        this.progress = progress;
    }
}
