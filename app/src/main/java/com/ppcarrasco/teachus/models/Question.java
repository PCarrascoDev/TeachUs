package com.ppcarrasco.teachus.models;

import com.ppcarrasco.teachus.data.Nodes;

/**
 * Created by pedro on 03-12-2017.
 */

public class Question {
    private String question;
    private String answer;
    private String studentName;
    private String studentUid;
    private String proffessorUid;
    private String key;
    private String questionKey;

    public Question() {
    }

    public Question(String question, String answer, String studentName, String studentUid, String proffessorUid, String key, String questionKey) {
        this.question = question;
        this.answer = answer;
        this.studentName = studentName;
        this.studentUid = studentUid;
        this.proffessorUid = proffessorUid;
        this.key = key;
        this.questionKey = questionKey;
    }

    public String getQuestion() {
        return question;
    }

    public String getAnswer() {
        return answer;
    }

    public String getStudentUid() {
        return studentUid;
    }

    public String getProffessorUid() {
        return proffessorUid;
    }

    public String getKey() {
        return key;
    }

    public String getQuestionKey() {
        return questionKey;
    }

    public String getStudentName() {
        return studentName;
    }

    public void setAnswer(String answer) {
        this.answer = answer;
    }

    public void publishQuestion(){
        if (questionKey.equals(""))
        {
            questionKey = new Nodes().getStudentQuestions().child(studentUid).push().getKey();
        }
        new Nodes().getStudentQuestions().child(studentUid).child(questionKey).setValue(this);
        new Nodes().getProffessorQuestions().child(proffessorUid).child(questionKey).setValue(this);
        new Nodes().getQuestions().child(key).child(questionKey).setValue(this);

    }
}
