package com.alexeyosadchy.giphy.presenter;

import com.alexeyosadchy.giphy.model.api.response.Datum;
import com.alexeyosadchy.giphy.model.api.response.FixedHeight;
import com.alexeyosadchy.giphy.model.api.response.Response;
import com.alexeyosadchy.giphy.view.GifView;

import java.util.ArrayList;
import java.util.List;

public class Mapper {

    public static List<GifView> transform(Response response){
        List<GifView> gifs = new ArrayList<>();
        for(Datum datum: response.getData()){
            FixedHeight fixedHeight = datum.getImages().getFixedHeight();
            GifView gif = new GifView();
            gif.setHeight(Integer.valueOf(fixedHeight.getHeight()));
            gif.setWidth(Integer.valueOf(fixedHeight.getWidth()));
            gif.setUri(fixedHeight.getUrl());
            gif.setTitle(datum.getTitle());
            gifs.add(gif);
        }
        return gifs;
    }
}
