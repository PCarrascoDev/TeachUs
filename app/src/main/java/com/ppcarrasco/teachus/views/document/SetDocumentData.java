package com.ppcarrasco.teachus.views.document;

import com.ppcarrasco.teachus.models.Document;

/**
 * Created by pedro on 10-12-2017.
 */

public class SetDocumentData {

    public SetDocumentData(Document document, DocumentDataCallback callback){
        if (document.getThumbnailUrl() != null)
        {
            callback.setPicture(document.getThumbnailUrl());
        }

        if (document.getName() != null)
        {
            callback.setInfo(document.getName().split("\\.")[0], document.getAuthor());
        }
    }
}
