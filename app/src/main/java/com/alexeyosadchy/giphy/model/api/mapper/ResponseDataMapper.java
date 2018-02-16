package com.alexeyosadchy.giphy.model.api.mapper;

import com.alexeyosadchy.giphy.model.api.response.Datum;
import com.alexeyosadchy.giphy.model.api.response.Images;
import com.alexeyosadchy.giphy.model.api.response.Response;

import java.util.ArrayList;
import java.util.List;

public final class ResponseDataMapper {

    public static List<Images> transform(final Response response) {
        final List<Images> images = new ArrayList<>();
        for (final Datum datum : response.getData()) {
            images.add(datum.getImages());
        }
        return images;
    }
}
