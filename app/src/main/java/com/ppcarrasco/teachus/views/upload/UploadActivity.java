package com.ppcarrasco.teachus.views.upload;

import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import com.github.angads25.filepicker.controller.DialogSelectionListener;
import com.github.angads25.filepicker.model.DialogConfigs;
import com.github.angads25.filepicker.model.DialogProperties;
import com.github.angads25.filepicker.view.FilePickerDialog;
import com.ppcarrasco.teachus.R;
import com.ppcarrasco.teachus.adapters.UploadAdapter;
import com.ppcarrasco.teachus.models.UploadDocument;

import java.io.File;
import java.util.ArrayList;
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
        uploadAdapter = new UploadAdapter(new ArrayList<UploadDocument>());
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
                //Toast.makeText(this, "Add!!", Toast.LENGTH_SHORT).show();
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
        dialog.setTitle("Selecciona archivos");
        dialog.setDialogSelectionListener(new DialogSelectionListener() {
            @Override
            public void onSelectedFilePaths(String[] files) {
                for (int i = 0; i < files.length; i++)
                {
                    File file = new File(files[i]);
                    String fileName[] = file.getName().split("\\.");
                    if (Objects.equals(fileName[fileName.length-1], "pdf"))
                    {
                        uploadAdapter.addItem(new UploadDocument(file, UploadActivity.this));
                    }
                }
            }
        });

        dialog.show();
    }

}
