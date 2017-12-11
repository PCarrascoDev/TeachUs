package com.ppcarrasco.teachus.views.questions;

import com.google.firebase.database.DatabaseReference;
import com.ppcarrasco.teachus.models.Question;

/**
 * Created by pedro on 03-12-2017.
 */

public interface QuestionsCallback {
    void setUpAdapter(DatabaseReference reference);
    void answer(Question question);
    void showDocument(Question question);
}
