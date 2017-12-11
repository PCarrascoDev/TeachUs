package com.ppcarrasco.teachus.views.document;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;
import com.ppcarrasco.teachus.data.CurrentUser;
import com.ppcarrasco.teachus.data.Nodes;

/**
 * Created by pedro on 10-12-2017.
 */

public class LikesLogic {
    private final ValueEventListener likedByListener;
    private final ValueEventListener likesListener;
    private final ValueEventListener dislikesListener;
    private String key;
    private DatabaseReference likedBy;
    private DatabaseReference likes;
    private DatabaseReference dislikes;

    public LikesLogic(String key, final LikesCallback callback)
    {
        this.key = key;
        //Listens if the user has liked or disliked the document
        likedBy = new Nodes()
                .getLikedBy()
                .child(new CurrentUser().getUid())
                .child(key);

        likedByListener = likedBy.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        if (dataSnapshot.exists())
                        {
                            if (dataSnapshot.getValue(Boolean.class))
                            {
                                callback.liked();
                            }
                            else
                            {
                                callback.disliked();
                            }
                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });

        //Listens the like count
        likes = new Nodes()
                .getLikes()
                .child(key);

        likesListener = likes.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        if (!dataSnapshot.exists())
                        {
                            callback.setLikes(0);
                        }
                        else
                        {
                            callback.setLikes(dataSnapshot.getValue(Integer.class));
                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });

        //Listens the dislike count
        dislikes = new Nodes()
                .getDislikes()
                .child(key);

        dislikesListener = dislikes.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        if (!dataSnapshot.exists())
                        {
                            callback.setDislikes(0);
                        }
                        else
                        {
                            callback.setDislikes(dataSnapshot.getValue(Integer.class));
                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });


    }

    public void likeDislike(final Boolean liked)
    {
        likedBy.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        //If it is null
                        if (!dataSnapshot.exists())
                        {
                            if (liked)
                            {
                                setLike(1);
                            }
                            else
                            {
                                setDislike(1);
                            }
                        }//If it is true
                        else if (dataSnapshot.getValue(Boolean.class))
                        {
                            if (!liked)
                            {
                                setLike(-1);
                                setDislike(1);
                            }
                        }//If it is false
                        else
                        {
                            if (liked)
                            {
                                setLike(1);
                                setDislike(-1);
                            }
                        }

                        likedBy.setValue(liked);
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });


    }

    private void setLike(final Integer step){
        likes.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (!dataSnapshot.exists() && step == 1)
                {
                    likes.setValue(1);
                }
                else if (step == 1)
                {
                    Integer actualValue = dataSnapshot.getValue(Integer.class);
                    likes.setValue(actualValue + 1);
                }
                else if (step == -1 && dataSnapshot.getValue(Integer.class) > 0)
                {
                    Integer actualValue = dataSnapshot.getValue(Integer.class);
                    likes.setValue(actualValue - 1);
                }

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }
    private void setDislike(final Integer step){

        dislikes.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (!dataSnapshot.exists() && step == 1)
                {
                    dislikes.setValue(1);
                }
                else if (step == 1)
                {
                    Integer actualValue = dataSnapshot.getValue(Integer.class);
                    dislikes.setValue(actualValue + 1);
                }
                else if (step == -1 && dataSnapshot.getValue(Integer.class) > 0)
                {
                    Integer actualValue = dataSnapshot.getValue(Integer.class);
                    dislikes.setValue(actualValue - 1);
                }

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }
    public void cleanUp(){
        if (likedByListener != null)
        {
            likedBy.removeEventListener(likesListener);
        }

        if (likesListener != null)
        {
            likes.removeEventListener(likesListener);
        }

        if (dislikesListener !=  null)
        {
            dislikes.removeEventListener(dislikesListener);
        }
    }

}
