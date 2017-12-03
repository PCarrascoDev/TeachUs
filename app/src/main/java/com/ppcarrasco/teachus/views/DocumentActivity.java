package com.ppcarrasco.teachus.views;

import android.app.DownloadManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.res.AssetManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.ppcarrasco.teachus.R;
import com.ppcarrasco.teachus.models.Document;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

public class DocumentActivity extends AppCompatActivity {
    private ImageView preview;
    int downloadedSize = 0, totalsize;
    float per = 0;
    private Document document;
    private BroadcastReceiver receiver;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_document);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);

        document = (Document) getIntent().getSerializableExtra("DOC");
        //toolbar.setTitle(document.getName());
        setSupportActionBar(toolbar);
        Log.d("URL", document.getDownloadUrl());
        preview = (ImageView) findViewById(R.id.collapsingIv);

        Glide.with(this)
                .load(document.getThumbnailUrl())
                .into(preview);

        TextView textView = (TextView) findViewById(R.id.nestedTv);
        textView.setText(document.getName());

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //downloadAndOpenPDF(new Nodes().getStorageDocuments().child(document.getKey()));
                /*Intent intent = new Intent(Intent.ACTION_VIEW);
                intent.setDataAndType(Uri.parse( "http://docs.google.com/viewer?url=" + document.getDownloadUrl()), "text/html");
                startActivity(intent);*/

                final long referenceId = downloadData(Uri.parse(document.getDownloadUrl()), document.getName());
                receiver = new BroadcastReceiver() {
                    @Override
                    public void onReceive(Context context, Intent intent) {
                        long id = intent.getLongExtra(DownloadManager.EXTRA_DOWNLOAD_ID, -1);
                        if (referenceId == id)
                        {
                            //Toast.makeText(context, "DOWNLOADED!", Toast.LENGTH_SHORT).show();
                            viewDocument(document.getName());
                        }
                    }
                };

                IntentFilter filter = new IntentFilter(DownloadManager.ACTION_DOWNLOAD_COMPLETE);
                registerReceiver(receiver, filter);
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(receiver);
    }

    private long downloadData(Uri uri, String name)
    {
        long referenceId = 0;
        DownloadManager manager = (DownloadManager) getSystemService(DOWNLOAD_SERVICE);
        DownloadManager.Request request = new DownloadManager.Request(uri);

        request.setTitle(name);
        //request.setDescription("Android Data download using DownloadManager.");

        request.setDestinationInExternalFilesDir(DocumentActivity.this, Environment.DIRECTORY_DOWNLOADS, name);

        if (manager != null) {
            referenceId = manager.enqueue(request);
        }
        return referenceId;
    }

    static private void copy(InputStream in, File dst) throws IOException {
        FileOutputStream out=new FileOutputStream(dst);
        byte[] buf=new byte[1024];
        int len;

        while ((len=in.read(buf)) > 0) {
            out.write(buf, 0, len);
        }

        in.close();
        out.close();
    }

    private void viewDocument(String name){
        //Environment.getExternalStorageDirectory().getAbsolutePath() + "/" + name
        /*File file = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS).getAbsolutePath() + "/" + name);
        Uri fileUri = GenericFileProvider.getUriForFile(DocumentActivity.this, getApplicationContext().getPackageName() + ".com.ppcarrasco.teachus.GenericFileProvider", file);
        Intent target = new Intent(Intent.ACTION_VIEW);
        target.setDataAndType(fileUri,"application/pdf");
        //target.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        target.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        Intent intent = Intent.createChooser(target, "Open File");
        //intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        //intent.setFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
        startActivity(intent);*/

        File file =new File(getExternalFilesDir(Environment.DIRECTORY_DOWNLOADS), name);
        if (!file.exists()) {
            AssetManager assets=getAssets();
            try {
                copy(assets.open(name), file);
            }
            catch (IOException e) {
                Log.e("FileProvider", "Exception copying from assets!", e);
            }
        }

        Log.d("AUT", getApplicationContext().getPackageName());
        Intent intent = new Intent(Intent.ACTION_VIEW, FileProvider.getUriForFile(this, getApplicationContext().getPackageName() + ".com.ppcarrasco.teachus.GenericFileProvider", file));

        intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);

        startActivity(intent);
    }

}
