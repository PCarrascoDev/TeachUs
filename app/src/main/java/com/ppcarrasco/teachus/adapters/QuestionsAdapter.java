package com.ppcarrasco.teachus.adapters;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.database.DatabaseReference;
import com.ppcarrasco.teachus.R;
import com.ppcarrasco.teachus.models.Question;
import com.ppcarrasco.teachus.views.questions.QuestionsLogic;

/**
 * Created by pedro on 03-12-2017.
 */

public class QuestionsAdapter extends FirebaseRecyclerAdapter<Question, QuestionsAdapter.QuestionHolder>{

    private QuestionsLogic questionsLogic;

    public QuestionsAdapter(DatabaseReference ref, QuestionsLogic questionsLogic) {
        super(Question.class, R.layout.list_item_question, QuestionHolder.class, ref);
        this.questionsLogic = questionsLogic;
    }

    public QuestionsAdapter(DatabaseReference ref) {
        super(Question.class, R.layout.list_item_question, QuestionHolder.class, ref);
    }

    @Override
    protected void populateViewHolder(QuestionHolder viewHolder, final Question model, int position) {
        viewHolder.student.setText(model.getStudentName());
        viewHolder.question.setText(model.getQuestion());
        viewHolder.answer.setText(model.getAnswer());
        if (questionsLogic != null)
        {
            viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    questionsLogic.processQuestion(model);
                }
            });
        }

    }

    public static class QuestionHolder extends RecyclerView.ViewHolder {
        private TextView student;
        private TextView question;
        private TextView answer;

        public QuestionHolder(View itemView) {
            super(itemView);

            student = (TextView) itemView.findViewById(R.id.studentTv);
            question = (TextView) itemView.findViewById(R.id.questionTv);
            answer = (TextView) itemView.findViewById(R.id.answerTv);
        }
    }
}
