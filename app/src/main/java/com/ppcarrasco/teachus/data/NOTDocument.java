/*package com.ppcarrasco.teachus.data;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.ParcelFileDescriptor;

import com.ppcarrasco.teachus.R;
import com.shockwave.pdfium.PdfDocument;
import com.shockwave.pdfium.PdfiumCore;

import java.io.File;*/

/**
 * Created by Pedro on 17-10-2017.
 */
/*
public class Document {
    private String name;
    private String author;
    private Bitmap thumbnail;
    private Uri uri;
    private double progress;

    //Constructor for upload
    public Document(File file, Context context) {
        uri = Uri.fromFile(file);
        name = processName(file.getName());
        author = new CurrentUser().getName();
        thumbnail = processThumbnail(context);
        progress = 0;
    }

    private String processName(String fileName)
    {
        return fileName.split("\\.")[0];
    }

    private Bitmap processThumbnail(Context context){
        int pageNumber = 0;
        PdfiumCore pdfiumCore = new PdfiumCore(context);
        Bitmap bmp = BitmapFactory.decodeResource(context.getResources(), R.drawable.ic_action_emo_err);
        try {
            //http://www.programcreek.com/java-api-examples/index.php?api=android.os.ParcelFileDescriptor
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

    public String getName() {
        return name;
    }

    public String getAuthor() {
        return author;
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

    public void setProgress(double progress) {
        this.progress = progress;
    }
}
*/