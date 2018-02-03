package com.alexeyosadchy.giphy.view;

import java.util.List;

public interface ITrendGifListActivity {

    void updateList();

    void prepareView(List<GifView> gifs);
}
