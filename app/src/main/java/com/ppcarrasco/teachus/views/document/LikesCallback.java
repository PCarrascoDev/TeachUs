package com.ppcarrasco.teachus.views.document;

/**
 * Created by pedro on 10-12-2017.
 */

public interface LikesCallback {
    void liked();
    void disliked();
    void setLikes(Integer likes);
    void setDislikes(Integer dislikes);
}
