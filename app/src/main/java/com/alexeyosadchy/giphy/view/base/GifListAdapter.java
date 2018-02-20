package com.alexeyosadchy.giphy.view.base;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;

import com.alexeyosadchy.giphy.R;
import com.alexeyosadchy.giphy.model.storage.GifView;
import com.alexeyosadchy.giphy.utils.AdapterUtils;
import com.facebook.drawee.backends.pipeline.Fresco;
import com.facebook.drawee.drawable.ProgressBarDrawable;
import com.facebook.drawee.interfaces.DraweeController;
import com.facebook.drawee.view.SimpleDraweeView;

import java.util.ArrayList;
import java.util.List;

public final class GifListAdapter extends RecyclerView.Adapter<GifListAdapter.GifHolder> {

    private final List<GifView> gifs = new ArrayList<>();
    private final BaseFavoriteButton mButton;

    public GifListAdapter(final BaseFavoriteButton button) {
        mButton = button;
    }

    void updateList(final List<GifView> gifs) {
        this.gifs.addAll(gifs);
        notifyDataSetChanged();
    }

    void removeItem(final int position) {
        gifs.remove(position);
        notifyItemRemoved(position);
    }

    void clearList() {
        gifs.clear();
        notifyDataSetChanged();
    }

    int getSizeList() {
        return gifs.size();
    }

    @Override
    public GifHolder onCreateViewHolder(final ViewGroup parent, final int viewType) {
        final View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_view_gif, parent, false);
        return new GifHolder(view);
    }

    @Override
    public void onBindViewHolder(final GifHolder holder, final int position) {
        final GifView gif = gifs.get(position);
        final DraweeController controller = Fresco.newDraweeControllerBuilder()
                .setUri(gif.getUri())
                .setAutoPlayAnimations(true)
                .build();
        holder.mSimpleDraweeView.setAspectRatio(gif.getWidth() / gif.getHeight());
        holder.mSimpleDraweeView.setController(controller);
        holder.mSimpleDraweeView.getHierarchy().setProgressBarImage(new ProgressBarDrawable());
        holder.mSimpleDraweeView.getHierarchy().setPlaceholderImage(AdapterUtils.getRandomColor());
        holder.mFavoriteButton.setImageResource(mButton.getImageResources(gif));
    }

    @Override
    public int getItemCount() {
        return gifs.size();
    }

    final class GifHolder extends RecyclerView.ViewHolder {

        private final SimpleDraweeView mSimpleDraweeView;
        private final ImageButton mFavoriteButton;

        GifHolder(final View itemView) {
            super(itemView);
            mSimpleDraweeView = itemView.findViewById(R.id.my_image_view);
            mFavoriteButton = itemView.findViewById(R.id.button_favorite);
            mFavoriteButton.setOnClickListener(view -> mButton.action(gifs.get(getAdapterPosition()), getAdapterPosition()));
        }
    }
}
