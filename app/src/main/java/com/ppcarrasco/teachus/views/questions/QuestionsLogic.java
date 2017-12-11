package com.ppcarrasco.teachus.views.questions;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;
import com.ppcarrasco.teachus.data.CurrentUser;
import com.ppcarrasco.teachus.data.Nodes;
import com.ppcarrasco.teachus.models.Question;

/**
 * Created by pedro on 10-12-2017.
 */

public class QuestionsLogic {
    private QuestionsCallback callback;
    public QuestionsLogic(final QuestionsCallback callback){
        this.callback = callback;
        new Nodes()
                .getUsers()
                .child(new CurrentUser().getUid())
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        if (dataSnapshot.getValue(Boolean.class))
                        {

                            callback.setUpAdapter(new Nodes().getProffessorQuestions().child(new CurrentUser().getUid()));
                        }
                        else
                        {

                            callback.setUpAdapter(new Nodes().getStudentQuestions().child(new CurrentUser().getUid()));
                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });
    }

    public void processQuestion(final Question question){
        new Nodes()
                .getUsers()
                .child(new CurrentUser().getUid())
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        if (dataSnapshot.getValue(Boolean.class))
                        {
                            //If it is teacher
                            callback.answer(question);
                        }
                        else
                        {
                            //If it's not
                            callback.showDocument(question);
                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });
    }
}
