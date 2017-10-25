package com.ppcarrasco.teachus.data;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

/**
 * Created by Pedro on 28-09-2017.
 */

public class Nodes {
    private DatabaseReference root;

    public Nodes()
    {
        root = FirebaseDatabase.getInstance().getReference();
    }

    public DatabaseReference getUsers()
    {
        return root.child("Users");
    }

    public DatabaseReference getProffessors()
    {
        return root.child("Proffessors");
    }

    public DatabaseReference getDocuments()
    {
        return root.child("Documents");
    }

    public DatabaseReference getDocIndex()
    {
        return root.child("DocumentsIndex");
    }
}
