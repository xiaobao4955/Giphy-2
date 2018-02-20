
package com.alexeyosadchy.giphy.model.api.response;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public final class Images {

    @SerializedName("fixed_height")
    @Expose
    private FixedHeight fixedHeight;

    public FixedHeight getFixedHeight() {
        return fixedHeight;
    }
}
