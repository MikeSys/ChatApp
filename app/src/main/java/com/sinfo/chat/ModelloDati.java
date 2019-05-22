package com.sinfo.chat;

import android.graphics.Bitmap;
import android.net.Uri;

import java.util.ArrayList;

public class ModelloDati {
    public int TYPE;
    private String testo;
    private Bitmap immagini;
    private Uri uri;


    public ModelloDati(int TYPE, Bitmap immagini) {
        this.TYPE = TYPE;
        this.immagini = immagini;
    }
    public ModelloDati(int TYPE, String testo) {
        this.TYPE = TYPE;
        this.testo = testo;
    }
    public ModelloDati(int TYPE, Uri uri) {
        this.TYPE = TYPE;
        this.uri = uri;
    }

    public int getTYPE() {
        return TYPE;
    }
    public String getTesto() {
        return testo;
    }
    public Bitmap getImmagini() {
        return immagini;
    }
    public Uri getUri() {
        return uri;
    }
}
