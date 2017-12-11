package com.ppcarrasco.teachus.views.main;

import com.ppcarrasco.teachus.models.Document;

/**
 * Created by pedro on 10-12-2017.
 */

public class OpenDocument {
    private DocumentsListener listener;

    public OpenDocument(Document document, DocumentsListener listener){
        this.listener = listener;

        if (document != null)
        {
            this.listener.success(document);
        }
        else
        {
            this.listener.failed();
        }
    }
}
