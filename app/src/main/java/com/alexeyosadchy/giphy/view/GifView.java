package com.alexeyosadchy.giphy.view;

import com.google.gson.annotations.Expose;

public final class GifView {

    @Expose
    private String uri;
    @Expose
    private String title;
    @Expose
    private String localePath;
    @Expose
    private String sharedPreferencesKey;
    @Expose
    private int width;
    @Expose
    private int height;

    public String getUri() {
        return uri;
    }

    public void setUri(String uri) {
        this.uri = uri;
    }

    public int getWidth() {
        return width;
    }

    public void setWidth(int width) {
        this.width = width;
    }

    public int getHeight() {
        return height;
    }

    public void setHeight(int height) {
        this.height = height;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getLocalePath() {
        return localePath;
    }

    public void setLocalePath(String localePath) {
        this.localePath = localePath;
    }

    public String getSharedPreferencesKey() {
        return sharedPreferencesKey;
    }

    public void setSharedPreferencesKey(String sharedPreferencesKey) {
        this.sharedPreferencesKey = sharedPreferencesKey;
    }
}
