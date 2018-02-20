package com.alexeyosadchy.giphy.view.base;

import com.alexeyosadchy.giphy.model.storage.GifView;

public interface BaseFavoriteButton {

    void action(GifView gif, int position);

    int getImageResources(GifView gif);

}
