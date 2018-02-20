package com.alexeyosadchy.giphy.model.storage;

import com.google.gson.annotations.Expose;

public final class GifView {

    @Expose
    private final String uri;
    @Expose
    private final String id;
    @Expose
    private final float width;
    @Expose
    private final float height;

    public GifView(final String uri, final String id, final float width, final float height) {
        this.uri = uri;
        this.id = id;
        this.width = width;
        this.height = height;
    }

    public String getUri() {
        return uri;
    }

    public float getWidth() {
        return width;
    }

    public float getHeight() {
        return height;
    }

    String getId() {
        return id;
    }

}
