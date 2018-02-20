
package com.alexeyosadchy.giphy.model.api.response;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public final class FixedHeight {

    @SerializedName("url")
    @Expose
    private final String url;
    @SerializedName("width")
    @Expose
    private final String width;
    @SerializedName("height")
    @Expose
    private final String height;

    FixedHeight(final String url, final String width, final String height) {
        this.url = url;
        this.width = width;
        this.height = height;
    }

    public String getUrl() {
        return url;
    }

    public String getWidth() {
        return width;
    }

    public String getHeight() {
        return height;
    }
}
