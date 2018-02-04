package com.alexeyosadchy.giphy.model.api.mapper;

import com.alexeyosadchy.giphy.model.api.response.Datum;
import com.alexeyosadchy.giphy.model.api.response.Images;
import com.alexeyosadchy.giphy.model.api.response.Response;

import java.util.ArrayList;
import java.util.List;

public class ResponseDataMapper {

    public static List<Images> transform(Response response) {
        List<Images> images = new ArrayList<>();
        for (Datum datum : response.getData()) {
            images.add(datum.getImages());
        }
        return images;
    }
}
