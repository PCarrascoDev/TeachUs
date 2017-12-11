package com.ppcarrasco.teachus.views.main;

import com.ppcarrasco.teachus.models.Document;

/**
 * Created by pedro on 01-12-2017.
 */

public interface DocumentsListener {
    void success(Document document);
    void failed();
}
