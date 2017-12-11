package com.ppcarrasco.teachus.views.login;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;

import com.ppcarrasco.teachus.R;
import com.ppcarrasco.teachus.data.CurrentUser;
import com.ppcarrasco.teachus.data.Nodes;
import com.ppcarrasco.teachus.views.main.MainActivity;

public class FormActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_form);

        final CheckBox proffessorCb = (CheckBox) findViewById(R.id.proffesorCb);
        Button acceptBtn = (Button) findViewById(R.id.acceptBtn);

        proffessorCb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (proffessorCb.isChecked())
                {
                    proffessorCb.setHintTextColor(ContextCompat.getColor(FormActivity.this , R.color.colorAccent));
                }
                else
                {
                    proffessorCb.setHintTextColor(ContextCompat.getColor(FormActivity.this , R.color.tw__composer_red));
                }
            }
        });

        acceptBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new Nodes().getUsers().child(new CurrentUser().getUid()).setValue(proffessorCb.isChecked());
                startActivity(new Intent(FormActivity.this, MainActivity.class));

            }
        });

    }
}
