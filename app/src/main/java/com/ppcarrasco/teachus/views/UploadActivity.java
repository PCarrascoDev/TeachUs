package com.ppcarrasco.teachus.views;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.ColorFilter;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.ParcelFileDescriptor;
import android.support.annotation.IntRange;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.github.angads25.filepicker.controller.DialogSelectionListener;
import com.github.angads25.filepicker.model.DialogConfigs;
import com.github.angads25.filepicker.model.DialogProperties;
import com.github.angads25.filepicker.view.FilePickerDialog;
import com.ppcarrasco.teachus.R;
import com.ppcarrasco.teachus.adapters.UploadAdapter;
import com.ppcarrasco.teachus.data.CurrentUser;
import com.ppcarrasco.teachus.data.Document;
import com.shockwave.pdfium.PdfDocument;
import com.shockwave.pdfium.PdfiumCore;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class UploadActivity extends AppCompatActivity {
    private UploadAdapter uploadAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_upload);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        if(actionBar != null)
        {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.uploadRv);
        LinearLayoutManager manager = new LinearLayoutManager(UploadActivity.this);
        recyclerView.setLayoutManager(manager);
        uploadAdapter = new UploadAdapter(new ArrayList<Document>());
        recyclerView.setAdapter(uploadAdapter);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);

        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.navigation, menu);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId())
        {
            case R.id.navigation_add:
                addItem();
                Toast.makeText(this, "Add!!", Toast.LENGTH_SHORT).show();
                return true;
            case android.R.id.home:
                onBackPressed();
                return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void addItem(){

        DialogProperties properties = new DialogProperties();
        properties.selection_mode = DialogConfigs.MULTI_MODE;
        properties.selection_type = DialogConfigs.FILE_SELECT;
        properties.root = new File(DialogConfigs.DEFAULT_DIR);
        properties.error_dir = new File(DialogConfigs.DEFAULT_DIR);
        properties.offset = new File(DialogConfigs.DEFAULT_DIR);
        properties.extensions = null;

        FilePickerDialog dialog = new FilePickerDialog(UploadActivity.this, properties);
        dialog.setTitle("Select a file");
        dialog.setDialogSelectionListener(new DialogSelectionListener() {
            @Override
            public void onSelectedFilePaths(String[] files) {
                for (int i = 0; i < files.length; i++)
                {
                    File file = new File(files[i]);
                    String fileName[] = file.getName().split("\\.");
                    if (Objects.equals(fileName[fileName.length-1], "pdf"))
                    {
                        uploadAdapter.addItem(makeDocument(file, fileName[0]), file);
                    }
                }
            }
        });

        dialog.show();
    }


    private void uploadDocument(File file){
        TextView documentTv = (TextView) findViewById(R.id.documentTv);
        ImageView documentIv = (ImageView) findViewById(R.id.documentIv);
        String name[] = file.getName().split("\\.");
        String extension = name[name.length-1];

        if (Objects.equals(extension, "pdf"))
        {
            documentTv.setText(name[0]);
            documentIv.setImageBitmap(generateImageFromPdf(Uri.fromFile(file)));
        }
    }

    private Document makeDocument(File file, String documentName)
    {
        String author = new CurrentUser().getName();
        Bitmap thumbnail = generateImageFromPdf(Uri.fromFile(file));
        return new Document(documentName, author, thumbnail);
    }

    private Bitmap generateImageFromPdf(Uri pdfUri) {
        int pageNumber = 0;
        PdfiumCore pdfiumCore = new PdfiumCore(this);
        Bitmap bmp = BitmapFactory.decodeResource(getResources(), R.drawable.ic_action_emo_err);
        try {
            //http://www.programcreek.com/java-api-examples/index.php?api=android.os.ParcelFileDescriptor
            ParcelFileDescriptor fd = getContentResolver().openFileDescriptor(pdfUri, "r");
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
}
