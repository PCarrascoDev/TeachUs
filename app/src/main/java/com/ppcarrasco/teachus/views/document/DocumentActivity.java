package com.ppcarrasco.teachus.views.document;

import android.content.Intent;
import android.content.res.AssetManager;
import android.os.Bundle;
import android.os.Environment;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.content.FileProvider;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.ppcarrasco.teachus.R;
import com.ppcarrasco.teachus.adapters.QuestionsAdapter;
import com.ppcarrasco.teachus.data.CurrentUser;
import com.ppcarrasco.teachus.data.Nodes;
import com.ppcarrasco.teachus.models.Document;
import com.ppcarrasco.teachus.models.Question;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

public class DocumentActivity extends AppCompatActivity implements TeacherCallback, LikesCallback, DocumentDataCallback, DownloadCallback{
    private ImageView preview;
    int downloadedSize = 0, totalsize;
    float per = 0;
    private Document document;
    private QuestionsAdapter adapter;
    private Button likeBtn;
    private Button dislikeBtn;
    private LikesLogic likesLogic;
    private TextView titleTv;
    private TextView authorTv;
    private DocumentDownloader downloader;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_document);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);

        document = (Document) getIntent().getSerializableExtra("DOC");


        //Enabling back button
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        if(actionBar != null)
        {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        adapter = new QuestionsAdapter(new Nodes().getQuestions().child(document.getKey()));
        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.questionRv);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);

        //Finding the views
        likeBtn = (Button) findViewById(R.id.likeBtn);
        dislikeBtn = (Button) findViewById(R.id.dislikeBtn);
        preview = (ImageView) findViewById(R.id.collapsingIv);
        titleTv = (TextView) findViewById(R.id.nestedTv);
        authorTv = (TextView) findViewById(R.id.nestedAuthorTv);

        likesLogic = new LikesLogic(document.getKey(), this);
        new SetDocumentData(document, this);

        likeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                likesLogic.likeDislike(true);
            }
        });

        dislikeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                likesLogic.likeDislike(false);
            }
        });

        downloader = new DocumentDownloader(this, document, this);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(DocumentActivity.this, "Descargando...", Toast.LENGTH_SHORT).show();
                downloader.startDownload();

            }
        });

        new GetTeacherState(this);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        adapter.cleanup();
        downloader.cleanUp();
        likesLogic.cleanUp();
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

    @Override
    public void isTeacher() {
        EditText questionEt = (EditText) findViewById(R.id.questionEt);
        questionEt.setVisibility(View.GONE);
        Button questionBtn = (Button) findViewById(R.id.questionBtn);
        questionBtn.setVisibility(View.GONE);
        View divider = findViewById(R.id.divider2);
        divider.setVisibility(View.GONE);
    }

    @Override
    public void isStudent() {
        View divider = findViewById(R.id.divider2);
        divider.setVisibility(View.VISIBLE);
        final EditText questionEt = (EditText) findViewById(R.id.questionEt);
        questionEt.setVisibility(View.VISIBLE);
        Button questionBtn = (Button) findViewById(R.id.questionBtn);
        questionBtn.setVisibility(View.VISIBLE);
        questionBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!questionEt.getText().toString().matches(""))
                {
                    Question myQuestion = new Question(questionEt.getText().toString(), "", new CurrentUser().getName(), new CurrentUser().getUid(), document.getAuthorUid(), document.getKey(), "");
                    myQuestion.publishQuestion();
                    questionEt.setText("");
                }
            }
        });
    }

    @Override
    public void liked() {

        likeBtn.setBackground(getResources().getDrawable(R.drawable.ic_thumb_up_blue));
        dislikeBtn.setBackground(getResources().getDrawable(R.drawable.ic_thumb_down_gray));
    }

    @Override
    public void disliked() {
        likeBtn.setBackground(getResources().getDrawable(R.drawable.ic_thumb_up_gray));
        dislikeBtn.setBackground(getResources().getDrawable(R.drawable.ic_thumb_down_red));
    }

    @Override
    public void setLikes(Integer likes) {
        TextView likeTv = (TextView) findViewById(R.id.likeTv);
        likeTv.setText(String.valueOf(likes));
    }

    @Override
    public void setDislikes(Integer dislikes) {
        TextView dislikeTv = (TextView) findViewById(R.id.dislikeTv);
        dislikeTv.setText(String.valueOf(dislikes));
    }

    @Override
    public void setPicture(String pictureUrl) {
        Glide.with(this)
                .load(pictureUrl)
                .into(preview);
    }

    @Override
    public void setInfo(String title, String author) {
        titleTv.setText(title);
        authorTv.setText(author);
    }

    @Override
    public void viewDocument(String name) {
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
