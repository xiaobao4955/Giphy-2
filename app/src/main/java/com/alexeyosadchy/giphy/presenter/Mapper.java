package com.alexeyosadchy.giphy.presenter;

import com.alexeyosadchy.giphy.model.api.response.FixedHeight;
import com.alexeyosadchy.giphy.model.api.response.Images;
import com.alexeyosadchy.giphy.view.GifView;

import java.util.ArrayList;
import java.util.List;

final class Mapper {

    static List<GifView> transform(final List<Images> imageList) {
        final List<GifView> gifs = new ArrayList<>();
        for (final Images image : imageList) {
            final FixedHeight fixedHeight = image.getFixedHeight();
            final GifView gif = new GifView();
            gif.setHeight(Integer.valueOf(fixedHeight.getHeight()));
            gif.setWidth(Integer.valueOf(fixedHeight.getWidth()));
            gif.setUri(fixedHeight.getUrl());
            gifs.add(gif);
        }
        return gifs;
    }
}
