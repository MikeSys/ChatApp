package com.sinfo.chat;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.ViewModel;
import android.net.Uri;
import android.view.Display;

import java.util.ArrayList;
import java.util.List;

public class MyViewModel extends ViewModel {


    private ArrayList<ModelloDati> dati;
    private Uri UriVideo;

    public MyViewModel() {}

    public MyViewModel(ArrayList<ModelloDati> dati, Uri uriVideo) {
        this.dati = dati;
        UriVideo = uriVideo;
    }
}

