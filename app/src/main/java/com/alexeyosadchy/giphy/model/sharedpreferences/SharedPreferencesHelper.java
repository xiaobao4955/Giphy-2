package com.alexeyosadchy.giphy.model.sharedpreferences;

import android.content.SharedPreferences;

import com.alexeyosadchy.giphy.view.GifView;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;

public final class SharedPreferencesHelper {

    private final SharedPreferences gifFilePreferences;
    private final SharedPreferences.Editor editor;
    private final Gson gson;

    @Inject
    public SharedPreferencesHelper(SharedPreferences sharedPreferences) {
        gifFilePreferences = sharedPreferences;
        editor = gifFilePreferences.edit();
        gson = new Gson();
    }

    public void put(GifView gif) {
        String json = gson.toJson(gif);
        editor.putString(gif.getUri(), json);
        editor.commit();
    }

    public void delete(String key) {
        editor.remove(key);
        editor.commit();
    }

    public String getFilePath(String key) {
        return gson.fromJson(gifFilePreferences.getString(key, null), GifView.class).getLocalePath();
    }

    public boolean hasContainKey(String key) {
        return gifFilePreferences.contains(key);
    }

    public List<GifView> getAllFilePath() {
        List<GifView> gifs = new ArrayList<>();
        for (Map.Entry<String, ?> pair : gifFilePreferences.getAll().entrySet()) {
            GifView gif = gson.fromJson((String) pair.getValue(), GifView.class);
            gif.setSharedPreferencesKey(pair.getKey());
            gifs.add(gif);
        }
        return gifs;
    }
}
