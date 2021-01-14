package com.src.Utils;

import android.view.View;

import com.jpardogo.android.googleprogressbar.library.GoogleProgressBar;

public class DialogLoading {
    public static void LoadingGoogle(boolean isLoading, GoogleProgressBar progressbar){
        if(isLoading == true){
            progressbar.setVisibility(View.VISIBLE);
        }else{
            progressbar.setVisibility(View.GONE);

        }
    }
}
