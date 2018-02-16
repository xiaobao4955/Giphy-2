package com.alexeyosadchy.giphy.model.storage;

import android.content.Context;
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

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import io.reactivex.Completable;
import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

public final class GifStorage {

    private final Context context;

    public GifStorage(@ApplicationContext final Context context) {
        this.context = context;
    }

    public Completable deleteGif(final String path) {
        return Completable
                .fromCallable(() -> new File(path).delete())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    public Observable<String> saveGif(final String uri) {
        return Observable.create(e -> {
            final DataSource<CloseableReference<PooledByteBuffer>> dataSource = buildRequest(uri);
            e.setCancellable(dataSource::close);
            dataSource.subscribe(buildSubscriber(e), CallerThreadExecutor.getInstance());
        })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .cast(String.class);
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

    private DataSource<CloseableReference<PooledByteBuffer>> buildRequest(final String uri) {
        final ImageRequest request = ImageRequestBuilder.newBuilderWithSource(Uri.parse(uri)).build();
        return Fresco.getImagePipeline().fetchEncodedImage(request, null);
    }

    private BaseDataSubscriber<CloseableReference<PooledByteBuffer>> buildSubscriber(final ObservableEmitter<Object> e) {
        return new BaseDataSubscriber<CloseableReference<PooledByteBuffer>>() {

            @Override
            protected void onNewResultImpl(final DataSource<CloseableReference<PooledByteBuffer>> dataSource) {
                final CloseableReference<PooledByteBuffer> ref = dataSource.getResult();
                if (ref != null) {
                    final InputStream is = new PooledByteBufferInputStream(ref.get());
                    try {
                        e.onNext(writeFile(is));
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
