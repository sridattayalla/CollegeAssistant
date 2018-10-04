package com.stepCone.sridatta.CollegeAssistant;

import android.graphics.Bitmap;

/**
 * Created by HP-PC on 24-01-2018.
 */

public class linkContainer {
    Bitmap bitmap;
    String url;
    String content;
    String facultyName;
    String date;

    public String getDate() {
        return date;
    }

    public linkContainer(Bitmap bitmap, String url, String content, String facultyName, String date) {
        this.bitmap = bitmap;
        this.url = url;
        this.content = content;
        this.facultyName=facultyName;
        this.date=date;

    }

    public Bitmap getBitmap() {
        return bitmap;
    }

    public String getUrl() {
        return url;
    }

    public String getContent() {
        return content;
    }

    public String getFacultyName() {
        return facultyName;
    }
}
