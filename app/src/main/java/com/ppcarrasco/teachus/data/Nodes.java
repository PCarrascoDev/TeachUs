package com.ppcarrasco.teachus.data;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

/**
 * Created by Pedro on 28-09-2017.
 */

public class Nodes {
    private DatabaseReference root;
    private StorageReference storage;

    public Nodes()
    {
        root = FirebaseDatabase.getInstance().getReference();
        storage = FirebaseStorage.getInstance().getReference();
    }

    public DatabaseReference getUsers()
    {
        return root.child("users");
    }

    public DatabaseReference getProffessors()
    {
        return root.child("proffessors");
    }

    public DatabaseReference getDocuments()
    {
        return root.child("documents");
    }


    public DatabaseReference getDocIndex()
    {
        return root.child("documentsIndex");
    }

    public StorageReference getStorageDocuments(){
        return storage.child("documents");
    }
    public StorageReference getStorageThumbnails(){
        return storage.child("thumbnails");
    }
}
