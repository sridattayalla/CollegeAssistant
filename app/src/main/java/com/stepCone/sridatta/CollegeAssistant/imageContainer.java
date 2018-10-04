package com.stepCone.sridatta.CollegeAssistant;

import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.support.v4.widget.DrawerLayout;

/**
 * Created by HP-PC on 25-01-2018.
 */

public class imageContainer {
    Drawable image;
    String description;
    String topText;

    public imageContainer(Drawable image, String description, String topText) {
        this.image = image;
        this.description = description;
        this.topText = topText;
    }

    public Drawable getImage() {
        return image;
    }

    public String getDescription() {
        return description;
    }

    public String getTopText() {
        return topText;
    }
}
