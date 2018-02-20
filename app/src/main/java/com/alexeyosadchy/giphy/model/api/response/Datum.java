
package com.alexeyosadchy.giphy.model.api.response;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public final class Datum {

    @SerializedName("images")
    @Expose
    private Images images;
    @SerializedName("id")
    @Expose
    private String id;

    public Images getImages() {
        return images;
    }

    public String getId() {
        return id;
    }
}
