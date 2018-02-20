
package com.alexeyosadchy.giphy.model.api.response;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

public final class Response {

    @SerializedName("data")
    @Expose
    private final List<Datum> data;

    Response(final List<Datum> data) {
        this.data = new ArrayList<>(data);
    }

    public List<Datum> getData() {
        return new ArrayList<>(data);
    }
}
