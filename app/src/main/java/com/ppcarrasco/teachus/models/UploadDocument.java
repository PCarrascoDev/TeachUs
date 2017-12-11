package com.ppcarrasco.teachus.models;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.ParcelFileDescriptor;

import com.ppcarrasco.teachus.R;
import com.ppcarrasco.teachus.data.CurrentUser;
import com.shockwave.pdfium.PdfDocument;
import com.shockwave.pdfium.PdfiumCore;

import java.io.File;

/**
 * Created by pedro on 29-11-2017.
 */

public class UploadDocument extends Document {

    private Bitmap thumbnail;
    private Uri uri;
    private double progress;

    public UploadDocument(File file, Context context) {
        super(new CurrentUser().getName(), new CurrentUser().getUid(), file.getName());
        uri = Uri.fromFile(file);
        thumbnail = processThumbnail(context);
        progress = 0;
    }

    public void setProgress(double progress) {
        this.progress = progress;
    }

    private Bitmap processThumbnail(Context context){
        int pageNumber = 0;
        PdfiumCore pdfiumCore = new PdfiumCore(context);
        Bitmap bmp = BitmapFactory.decodeResource(context.getResources(), R.drawable.ic_action_emo_err);
        try {
            ParcelFileDescriptor fd = context.getContentResolver().openFileDescriptor(uri, "r");
            PdfDocument pdfDocument = pdfiumCore.newDocument(fd);
            pdfiumCore.openPage(pdfDocument, pageNumber);
            int width = pdfiumCore.getPageWidthPoint(pdfDocument, pageNumber);
            int height = pdfiumCore.getPageHeightPoint(pdfDocument, pageNumber);
            bmp = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
            pdfiumCore.renderPageBitmap(pdfDocument, bmp, pageNumber, 0, 0, width, height);
            pdfiumCore.closeDocument(pdfDocument); // important!
        } catch(Exception e) {
        }

        return bmp;
    }

    public Document makeDocument(String key, String downloadUrl, String thumbnailUrl){
        return new Document(getAuthor(), getAuthorUid(), getName(), downloadUrl, thumbnailUrl, key);
    }

    public Bitmap getThumbnail() {
        return thumbnail;
    }

    public Uri getUri() {
        return uri;
    }

    public double getProgress() {
        return progress;
    }
}
