package com.sinfo.chat;

import android.arch.lifecycle.ViewModel;
import android.graphics.Bitmap;
import android.net.Uri;

import java.io.File;

public class ModelloDati extends ViewModel {
    public int TYPE;
    public String string;
    public Bitmap immagini;
    public Uri uri;


    public ModelloDati(int TYPE, Bitmap immagini) {
        this.TYPE = TYPE;
        this.immagini = immagini;
    }
    public ModelloDati(int TYPE, String testo) {
        this.TYPE = TYPE;
        this.string = testo;
    }
    public ModelloDati(int TYPE, Uri uri) {
        this.TYPE = TYPE;
        this.uri = uri;
    }


    public int getTYPE() {
        return TYPE;
    }
    public String getString() {
        return string;
    }
    public Bitmap getImmagini() {
        return immagini;
    }

    public Uri getUri() {
        return uri;
    }

}
