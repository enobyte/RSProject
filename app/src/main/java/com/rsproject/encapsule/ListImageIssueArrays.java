package com.rsproject.encapsule;

import android.graphics.Bitmap;

/**
 * Created by Eno on 9/19/2016.
 */
public class ListImageIssueArrays {
    private Bitmap imageUri;

    public ListImageIssueArrays(Bitmap imageUri) {
        this.imageUri = imageUri;
    }

    public Bitmap getImage() {
        return imageUri;
    }
}
