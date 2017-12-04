package com.ppcarrasco.teachus.views;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;
import com.ppcarrasco.teachus.R;
import com.ppcarrasco.teachus.adapters.QuestionsAdapter;
import com.ppcarrasco.teachus.adapters.QuestionsListener;
import com.ppcarrasco.teachus.data.CurrentUser;
import com.ppcarrasco.teachus.data.Nodes;
import com.ppcarrasco.teachus.models.Document;
import com.ppcarrasco.teachus.models.Question;

public class QuestionsActivity extends AppCompatActivity implements QuestionsListener{
    private QuestionsAdapter adapter;

    @Override
    protected void onDestroy() {
        super.onDestroy();
        adapter.cleanup();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_questions);
        Toolbar toolbar = (Toolbar) findViewById(R.id.questionsTb);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        if(actionBar != null)
        {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        new Nodes()
                .getUsers()
                .child(new CurrentUser().getUid())
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        if (dataSnapshot.getValue(Boolean.class))
                        {
                            setUpAdapter(new Nodes().getProffessorQuestions().child(new CurrentUser().getUid()));
                        }
                        else
                        {
                            setUpAdapter(new Nodes().getStudentQuestions().child(new CurrentUser().getUid()));
                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });
    }

    private void setUpAdapter(DatabaseReference reference)
    {
        adapter = new QuestionsAdapter(reference, this);
        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.questionsRv);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setHasFixedSize(true);
        recyclerView.setAdapter(adapter);
    }

    @Override
    public void onPressed(final Question question) {
        new Nodes()
                .getUsers()
                .child(new CurrentUser().getUid())
                .addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.getValue(Boolean.class))
                {
                    //Mostrar el dialog para responder pregunta
                }
                else
                {
                    //enviar al estudiante al documento
                    new Nodes()
                            .getDocuments()
                            .child(question.getKey())
                            .addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            if (dataSnapshot.exists())
                            {
                                Document document = dataSnapshot.getValue(Document.class);
                                Intent intent = new Intent(QuestionsActivity.this, DocumentActivity.class);
                                intent.putExtra("DOC", document);
                                startActivity(intent);
                            }
                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {

                        }
                    });
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }
}
