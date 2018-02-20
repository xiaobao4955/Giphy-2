
package com.alexeyosadchy.giphy.model.api.response;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public final class Images {

    @SerializedName("fixed_height")
    @Expose
    private final FixedHeight fixedHeight;

    Images(final FixedHeight fixedHeight) {
        this.fixedHeight = fixedHeight;
    }

    public FixedHeight getFixedHeight() {
        return fixedHeight;
    }
}
