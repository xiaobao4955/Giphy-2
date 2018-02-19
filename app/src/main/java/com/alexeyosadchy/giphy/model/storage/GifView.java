package com.alexeyosadchy.giphy.model.storage;

import com.google.gson.annotations.Expose;

public final class GifView {

    @Expose
    private String uri;
    @Expose
    private final String id;
    @Expose
    private final int width;
    @Expose
    private final int height;

    public GifView(final String uri, final String id, final int width, final int height) {
        this.uri = uri;
        this.id = id;
        this.width = width;
        this.height = height;
    }

    public String getUri() {
        return uri;
    }

    void setUri(final String uri) {
        this.uri = uri;
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

    String getId() {
        return id;
    }

}
