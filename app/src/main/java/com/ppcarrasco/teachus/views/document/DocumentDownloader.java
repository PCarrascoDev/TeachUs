package com.ppcarrasco.teachus.views.document;

import android.app.DownloadManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.Uri;
import android.os.Environment;

import com.ppcarrasco.teachus.models.Document;

import static android.content.Context.DOWNLOAD_SERVICE;

/**
 * Created by pedro on 10-12-2017.
 */

public class DocumentDownloader {
    private Context context;
    private Document document;
    private DownloadCallback callback;
    private BroadcastReceiver receiver;

    public DocumentDownloader(Context context, Document document, DownloadCallback callback) {
        this.context = context;
        this.document = document;
        this.callback = callback;
    }

    public void startDownload(){
        final long referenceId = downloadData(Uri.parse(document.getDownloadUrl()), document.getName());
        receiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                long id = intent.getLongExtra(DownloadManager.EXTRA_DOWNLOAD_ID, -1);
                if (referenceId == id)
                {
                    callback.viewDocument(document.getName());
                }
            }
        };

        IntentFilter filter = new IntentFilter(DownloadManager.ACTION_DOWNLOAD_COMPLETE);
        context.registerReceiver(receiver, filter);
    }

    public void cleanUp(){
        if (receiver != null)
        {
            context.unregisterReceiver(receiver);
        }
    }


    private long downloadData(Uri uri, String name)
    {
        long referenceId = 0;
        DownloadManager manager = (DownloadManager) context.getSystemService(DOWNLOAD_SERVICE);
        DownloadManager.Request request = new DownloadManager.Request(uri);

        request.setTitle(name);

        request.setDestinationInExternalFilesDir(context, Environment.DIRECTORY_DOWNLOADS, name);

        if (manager != null) {
            referenceId = manager.enqueue(request);
        }
        return referenceId;
    }
}
