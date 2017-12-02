package com.rupiee.android.model;

import android.support.v7.app.AppCompatActivity;;

/**
 * Created by ketan on 7/4/17.
 */

public class HomeEntryVo {

    private int image;
    private String category;
    private String expln;
    private Class targetActivity;

    public HomeEntryVo(int image, String category, String expln, Class targetActivity) {
        this.image = image;
        this.category = category;
        this.expln = expln;
        this.targetActivity = targetActivity;
    }

    public int getImage() {
        return image;
    }

    public void setImage(int image) {
        this.image = image;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getExpln() {
        return expln;
    }

    public void setExpln(String expln) {
        this.expln = expln;
    }

    public Class getTargetActivity() {
        return targetActivity;
    }

    public void setTargetActivity(Class targetActivity) {
        this.targetActivity = targetActivity;
    }
}
