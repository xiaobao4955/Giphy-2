package com.alexeyosadchy.giphy.presenter;

import com.alexeyosadchy.giphy.model.api.response.Datum;
import com.alexeyosadchy.giphy.model.api.response.FixedHeight;
import com.alexeyosadchy.giphy.model.storage.GifView;

import java.util.ArrayList;
import java.util.List;

final class Mapper {

    static List<GifView> transform(final List<Datum> datumList) {
        final List<GifView> gifs = new ArrayList<>();
        for (final Datum datum : datumList) {
            final FixedHeight fixedHeight = datum.getImages().getFixedHeight();
            gifs.add(new GifView(fixedHeight.getUrl(), datum.getId(), Integer.valueOf(fixedHeight.getWidth()),
                    Integer.valueOf(fixedHeight.getHeight())));
        }
        return gifs;
    }
}
