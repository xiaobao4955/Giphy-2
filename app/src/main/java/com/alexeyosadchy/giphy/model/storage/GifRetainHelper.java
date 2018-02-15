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

public final class GifRetainHelper {

    private final Context context;

    public GifRetainHelper(@ApplicationContext Context context) {
        this.context = context;
    }

    public Completable deleteGif(String path) {

        return Completable.fromCallable(() -> new File(path).delete());
    }

    public Observable<String> saveGif(String uri) {
        return Observable.create(e -> {
            ImageRequest request = ImageRequestBuilder.newBuilderWithSource(Uri.parse(uri)).build();
            DataSource<CloseableReference<PooledByteBuffer>> dataSource =
                    Fresco.getImagePipeline().fetchEncodedImage(request, null);
            e.setCancellable(dataSource::close);
            BaseDataSubscriber<CloseableReference<PooledByteBuffer>> dataSubscriber =
                    new BaseDataSubscriber<CloseableReference<PooledByteBuffer>>() {

                        @Override
                        protected void onNewResultImpl(DataSource<CloseableReference<PooledByteBuffer>> dataSource) {
                            CloseableReference<PooledByteBuffer> ref = dataSource.getResult();
                            if (ref != null) {
                                InputStream is = new PooledByteBufferInputStream(ref.get());
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
                        protected void onFailureImpl(DataSource<CloseableReference<PooledByteBuffer>> dataSource) {
                            e.onError(dataSource.getFailureCause());
                        }
                    };
            dataSource.subscribe(dataSubscriber, CallerThreadExecutor.getInstance());
        });
    }

    private String writeFile(InputStream is) throws IOException {
        byte[] buffer = new byte[8 * 1024];
        int bytesRead;
        String path = FileUtils.getEmptyImageFilePath(context);
        OutputStream outStream = new FileOutputStream(path);
        while ((bytesRead = is.read(buffer)) != -1) {
            outStream.write(buffer, 0, bytesRead);
        }
        return path;
    }
}
