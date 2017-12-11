package com.ppcarrasco.teachus.views.questions;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.LinearLayout;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;
import com.ppcarrasco.teachus.R;
import com.ppcarrasco.teachus.adapters.QuestionsAdapter;
import com.ppcarrasco.teachus.data.Nodes;
import com.ppcarrasco.teachus.models.Document;
import com.ppcarrasco.teachus.models.Question;
import com.ppcarrasco.teachus.views.document.DocumentActivity;

public class QuestionsActivity extends AppCompatActivity implements QuestionsCallback {
    private QuestionsAdapter adapter;
    private QuestionsLogic questionsLogic;
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
        questionsLogic = new QuestionsLogic(this);

    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home)
        {
            onBackPressed();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void setUpAdapter(DatabaseReference reference) {
        RecyclerView questionsRv = (RecyclerView) findViewById(R.id.questionsRv);
        adapter = new QuestionsAdapter(reference, questionsLogic);
        questionsRv.setLayoutManager(new LinearLayoutManager(this));
        questionsRv.setHasFixedSize(true);
        questionsRv.setAdapter(adapter);
    }

    @Override
    public void answer(final Question question) {
        //Mostrar el dialog para responder pregunta
        final AlertDialog.Builder dialog = new AlertDialog.Builder(QuestionsActivity.this);
        final EditText input = new EditText(QuestionsActivity.this);
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.MATCH_PARENT);
        input.setLayoutParams(lp);
        dialog.setView(input);
        dialog.setTitle("Respuesta: ");
        dialog.setCancelable(true);
        dialog.setPositiveButton("Aceptar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                if (!input.getText().toString().equals(""))
                {
                    question.setAnswer("R: " + input.getText().toString());
                    question.publishQuestion();
                    dialogInterface.dismiss();
                }
                dialogInterface.dismiss();
            }
        });
        dialog.setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
            }
        });
        dialog.show();
    }

    @Override
    public void showDocument(Question question) {
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
