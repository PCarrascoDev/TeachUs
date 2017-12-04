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
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;
import com.ppcarrasco.teachus.R;
import com.ppcarrasco.teachus.data.CurrentUser;
import com.ppcarrasco.teachus.data.Nodes;
import com.ppcarrasco.teachus.models.Document;
import com.ppcarrasco.teachus.models.Question;

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
    private DatabaseReference likes;
    private DatabaseReference dislikes;
    private DatabaseReference likedBy;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_document);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);

        document = (Document) getIntent().getSerializableExtra("DOC");

        likes = new Nodes().getLikes().child(document.getKey());
        dislikes = new Nodes().getDislikes().child(document.getKey());
        likedBy = new Nodes().getLikedBy().child(new CurrentUser().getUid()).child(document.getKey());


        //Enabling back button
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        if(actionBar != null)
        {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }


        preview = (ImageView) findViewById(R.id.collapsingIv);

        Glide.with(this)
                .load(document.getThumbnailUrl())
                .into(preview);

        TextView title = (TextView) findViewById(R.id.nestedTv);
        title.setText(document.getName());

        TextView author = (TextView) findViewById(R.id.nestedAuthorTv);
        author.setText(document.getAuthor());

        final TextView likeTv = (TextView) findViewById(R.id.likeTv);
        likes.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (!dataSnapshot.exists())
                {
                    likeTv.setText(String.valueOf(0));
                }
                else
                {
                    likeTv.setText(String.valueOf(dataSnapshot.getValue(Integer.class)));
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        final TextView dislikeTv = (TextView) findViewById(R.id.dislikeTv);
        dislikes.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (!dataSnapshot.exists())
                {
                    dislikeTv.setText(String.valueOf(0));
                }
                else
                {
                    dislikeTv.setText(String.valueOf(dataSnapshot.getValue(Integer.class)));
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        final Button likeBtn = (Button) findViewById(R.id.likeBtn);
        likeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //null = no like or dislike / true = liked / false = disliked
                likedBy.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        if (dataSnapshot.exists())
                        {
                            Boolean value = dataSnapshot.getValue(Boolean.class);
                            if (!value)
                            {
                                dislikes.addListenerForSingleValueEvent(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(DataSnapshot dataSnapshot) {
                                        if (dataSnapshot.exists())
                                        {
                                            Integer dislikeCount = dataSnapshot.getValue(Integer.class);
                                            if (dislikeCount > 0)
                                            {
                                                dislikes.setValue(dislikeCount - 1);
                                            }
                                        }
                                        else
                                        {
                                            dislikes.setValue(0);
                                        }
                                    }

                                    @Override
                                    public void onCancelled(DatabaseError databaseError) {

                                    }
                                });

                                likedBy.setValue(true);
                                like();
                            }

                        }
                        else
                        {
                            likedBy.setValue(true);
                            like();
                        }

                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });
            }
        });

        final Button dislikeBtn = (Button) findViewById(R.id.dislikeBtn);
        dislikeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                likedBy.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        if (dataSnapshot.exists())
                        {
                            Boolean value = dataSnapshot.getValue(Boolean.class);
                            if (value)
                            {
                                likes.addListenerForSingleValueEvent(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(DataSnapshot dataSnapshot) {
                                        if (dataSnapshot.exists())
                                        {
                                            Integer value = dataSnapshot.getValue(Integer.class);
                                            if (value > 0)
                                            {
                                                likes.setValue(value - 1);
                                            }
                                            else
                                            {
                                                likes.setValue(0);
                                            }
                                        }
                                        else
                                        {
                                            likes.setValue(0);
                                        }
                                    }

                                    @Override
                                    public void onCancelled(DatabaseError databaseError) {

                                    }
                                });

                                likedBy.setValue(false);
                                dislike();
                            }
                        }
                        else
                        {
                            likedBy.setValue(false);
                            dislike();
                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });
            }
        });

        //Button colors listener
        likedBy.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists())
                {
                    Boolean value = dataSnapshot.getValue(Boolean.class);
                    if (value)
                    {
                        likeBtn.setBackground(getResources().getDrawable(R.drawable.ic_thumb_up_blue));
                        dislikeBtn.setBackground(getResources().getDrawable(R.drawable.ic_thumb_down_gray));
                    }
                    else
                    {
                        likeBtn.setBackground(getResources().getDrawable(R.drawable.ic_thumb_up_gray));
                        dislikeBtn.setBackground(getResources().getDrawable(R.drawable.ic_thumb_down_red));
                    }
                }
                else
                {
                    likeBtn.setBackground(getResources().getDrawable(R.drawable.ic_thumb_up_gray));
                    dislikeBtn.setBackground(getResources().getDrawable(R.drawable.ic_thumb_down_gray));
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });


        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

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

        new Nodes()
                .getUsers()
                .child(new CurrentUser().getUid())
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        if (dataSnapshot.getValue(Boolean.class))
                        {
                            //es professor
                            EditText questionEt = (EditText) findViewById(R.id.questionEt);
                            questionEt.setVisibility(View.GONE);
                            Button questionBtn = (Button) findViewById(R.id.questionBtn);
                            questionBtn.setVisibility(View.GONE);
                            View divider = findViewById(R.id.divider2);
                            divider.setVisibility(View.GONE);
                        }
                        else
                        {
                            //es student
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
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });


    }
    void like()
    {
        likes.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists())
                {
                    Integer likeCount =  dataSnapshot.getValue(Integer.class);
                    likes.setValue(likeCount + 1);
                }
                else
                {
                    likes.setValue(1);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    void dislike(){
        dislikes.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists())
                {
                    Integer dislikeCount = dataSnapshot.getValue(Integer.class);
                    dislikes.setValue(dislikeCount + 1);
                }
                else

                {
                    dislikes.setValue(1);
                }

                /*likes.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        if (dataSnapshot.exists())
                        {
                            Integer likeCount = dataSnapshot.getValue(Integer.class);
                            if (likeCount > 0)
                            {
                                likes.setValue(likeCount - 1);
                            }
                        }
                        else
                        {
                            likes.setValue(0);
                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });*/
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }



    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (receiver != null)
        {
            unregisterReceiver(receiver);
        }
    }

    private long downloadData(Uri uri, String name)
    {
        long referenceId = 0;
        DownloadManager manager = (DownloadManager) getSystemService(DOWNLOAD_SERVICE);
        DownloadManager.Request request = new DownloadManager.Request(uri);

        request.setTitle(name);

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
