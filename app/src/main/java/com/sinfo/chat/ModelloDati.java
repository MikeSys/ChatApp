package com.sinfo.chat;

import android.arch.lifecycle.ViewModel;
import android.graphics.Bitmap;
import android.net.Uri;

import java.util.ArrayList;

public class ModelloDati extends ViewModel {
    public int TYPE;
    public String testo;
    public Bitmap immagini;
    public Uri uri;


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


    public void setTYPE(int TYPE) {
        this.TYPE = TYPE;
    }

    public void setTesto(String testo) {
        this.testo = testo;
    }

    public void setImmagini(Bitmap immagini) {
        this.immagini = immagini;
    }

    public void setUri(Uri uri) {
        this.uri = uri;
    }
}
