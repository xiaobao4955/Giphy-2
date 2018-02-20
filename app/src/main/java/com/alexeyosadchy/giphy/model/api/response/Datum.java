
package com.alexeyosadchy.giphy.model.api.response;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public final class Datum {

    @SerializedName("images")
    @Expose
    private final Images images;
    @SerializedName("id")
    @Expose
    private final String id;

    Datum(final Images images, final String id) {
        this.images = images;
        this.id = id;
    }

    public Images getImages() {
        return images;
    }

    public String getId() {
        return id;
    }
}
