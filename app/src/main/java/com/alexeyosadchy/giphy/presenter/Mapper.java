package com.alexeyosadchy.giphy.presenter;

import com.alexeyosadchy.giphy.model.api.response.FixedHeight;
import com.alexeyosadchy.giphy.model.api.response.Images;
import com.alexeyosadchy.giphy.view.GifView;

import java.util.ArrayList;
import java.util.List;

public class Mapper {

    public static List<GifView> transform(List<Images> imageList) {
        List<GifView> gifs = new ArrayList<>();
        for (Images image : imageList) {
            FixedHeight fixedHeight = image.getFixedHeight();
            GifView gif = new GifView();
            gif.setHeight(Integer.valueOf(fixedHeight.getHeight()));
            gif.setWidth(Integer.valueOf(fixedHeight.getWidth()));
            gif.setUri(fixedHeight.getUrl());
            gifs.add(gif);
        }
        return gifs;
    }
}
