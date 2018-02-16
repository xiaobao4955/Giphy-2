package com.alexeyosadchy.giphy.model.storage;

import android.content.Context;
import android.content.SharedPreferences;
import android.net.Uri;

import com.alexeyosadchy.giphy.di.ApplicationContext;
import com.alexeyosadchy.giphy.utils.FileUtils;
import com.facebook.common.executors.CallerThreadExecutor;
import com.facebook.common.internal.Closeables;
import com.facebook.common.memory.PooledByteBuffer;
import com.facebook.common.memory.PooledByteBufferInputStream;
import com.facebook.common.references.CloseableReference;
import com.facebook.datasource.BaseDataSubscriber;
import com.facebook.datasource.DataSource;
import com.facebook.drawee.backends.pipeline.Fresco;
import com.facebook.imagepipeline.request.ImageRequest;
import com.facebook.imagepipeline.request.ImageRequestBuilder;
import com.google.gson.Gson;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import io.reactivex.Completable;
import io.reactivex.CompletableEmitter;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

public final class GifStorage {

    private final SharedPreferences gifFilePreferences;
    private final SharedPreferences.Editor editor;
    private final Gson gson;
    private final Context context;

    public GifStorage(@ApplicationContext final Context context, final SharedPreferences sharedPreferences) {
        this.context = context;
        gifFilePreferences = sharedPreferences;
        editor = gifFilePreferences.edit();
        gson = new Gson();
    }

    public Completable deleteGif(final GifView gif) {
        deleteGifFromSharedPreferences(gif.getSharedPreferencesKey());
        return Completable
                .fromCallable(() -> new File(gif.getLocalePath()).delete())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    public Completable saveGif(final GifView gif) {
        return Completable.create(e -> {
            final DataSource<CloseableReference<PooledByteBuffer>> dataSource = buildRequest(gif.getUri());
            e.setCancellable(dataSource::close);
            dataSource.subscribe(buildSubscriber(e, gif), CallerThreadExecutor.getInstance());
        })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    private String writeFile(final InputStream is) throws IOException {
        final byte[] buffer = new byte[8 * 1024];
        int bytesRead;
        final String path = FileUtils.getEmptyImageFilePath(context);
        final OutputStream outStream = new FileOutputStream(path);
        while ((bytesRead = is.read(buffer)) != -1) {
            outStream.write(buffer, 0, bytesRead);
        }
        return path;
    }

    private void putGifToSharedPreferences(final GifView gif) {
        gif.setSharedPreferencesKey(gif.getUri());
        final String json = gson.toJson(gif);
        editor.putString(gif.getUri(), json);
        editor.commit();
    }

    private void deleteGifFromSharedPreferences(final String key) {
        editor.remove(key);
        editor.commit();
    }

    public boolean hasContainGif(final GifView gif) {
        return gifFilePreferences.contains(gif.getSharedPreferencesKey());
    }

    public List<GifView> getAllSavedGifs() {
        final List<GifView> gifs = new ArrayList<>();
        for (final Map.Entry<String, ?> pair : gifFilePreferences.getAll().entrySet()) {
            final GifView gif = gson.fromJson((String) pair.getValue(), GifView.class);
            gif.setSharedPreferencesKey(pair.getKey());
            gifs.add(gif);
        }
        return gifs;
    }

    private DataSource<CloseableReference<PooledByteBuffer>> buildRequest(final String uri) {
        final ImageRequest request = ImageRequestBuilder.newBuilderWithSource(Uri.parse(uri)).build();
        return Fresco.getImagePipeline().fetchEncodedImage(request, null);
    }

    private BaseDataSubscriber<CloseableReference<PooledByteBuffer>> buildSubscriber(final CompletableEmitter e, final GifView gif) {
        return new BaseDataSubscriber<CloseableReference<PooledByteBuffer>>() {

            @Override
            protected void onNewResultImpl(final DataSource<CloseableReference<PooledByteBuffer>> dataSource) {
                final CloseableReference<PooledByteBuffer> ref = dataSource.getResult();
                if (ref != null) {
                    final InputStream is = new PooledByteBufferInputStream(ref.get());
                    try {
                        gif.setLocalePath(writeFile(is));
                        putGifToSharedPreferences(gif);
                        e.onComplete();
                    } catch (Throwable throwable) {
                        e.onError(throwable);
                    } finally {
                        Closeables.closeQuietly(is);
                        CloseableReference.closeSafely(ref);
                    }
                }
            }

            @Override
            protected void onFailureImpl(final DataSource<CloseableReference<PooledByteBuffer>> dataSource) {
                e.onError(dataSource.getFailureCause());
            }
        };
    }
}
