package com.ppcarrasco.teachus.views.document;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;
import com.ppcarrasco.teachus.data.CurrentUser;
import com.ppcarrasco.teachus.data.Nodes;

/**
 * Created by pedro on 10-12-2017.
 */

public class GetTeacherState {


    public GetTeacherState(final TeacherCallback callback)
    {
        new Nodes()
                .getUsers()
                .child(new CurrentUser().getUid())
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        if (dataSnapshot.getValue(Boolean.class))
                        {
                            callback.isTeacher();
                        }
                        else
                        {
                            callback.isStudent();
                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });
    }
}
