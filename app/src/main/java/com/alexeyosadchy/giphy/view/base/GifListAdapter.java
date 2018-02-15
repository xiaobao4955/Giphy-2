package com.alexeyosadchy.giphy.view.base;

import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;

import com.alexeyosadchy.giphy.R;
import com.alexeyosadchy.giphy.utils.AdapterUtils;
import com.alexeyosadchy.giphy.view.GifView;
import com.facebook.drawee.backends.pipeline.Fresco;
import com.facebook.drawee.drawable.ProgressBarDrawable;
import com.facebook.drawee.interfaces.DraweeController;
import com.facebook.drawee.view.SimpleDraweeView;

import java.util.List;

public final class GifListAdapter extends RecyclerView.Adapter<GifListAdapter.GifHolder> {

    private static final float DEFAULT_SCALE_FACTOR = 1f;

    private final List<GifView> mGifs;
    private final RecyclerView.LayoutManager mLayoutManager;
    private final BaseFavoriteButton mButton;

    public GifListAdapter(List<GifView> gifs, RecyclerView.LayoutManager layoutManager, BaseFavoriteButton button) {
        mGifs = gifs;
        mLayoutManager = layoutManager;
        mButton = button;
    }

    @Override
    public GifHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_view_gif, parent, false);
        return new GifHolder(view);
    }

    @Override
    public void onBindViewHolder(GifHolder holder, int position) {
        float scaleFactor = DEFAULT_SCALE_FACTOR;
        DraweeController controller = Fresco.newDraweeControllerBuilder()
                .setUri(mGifs.get(position).getUri())
                .setAutoPlayAnimations(true)
                .build();
        holder.mSimpleDraweeView.setController(controller);
        holder.mSimpleDraweeView.getHierarchy().setProgressBarImage(new ProgressBarDrawable());
        if (mLayoutManager instanceof LinearLayoutManager) {
            scaleFactor = (float) mLayoutManager.getWidth() / mGifs.get(position).getWidth();
        } else if (mLayoutManager instanceof StaggeredGridLayoutManager) {
            scaleFactor = (float) mLayoutManager.getWidth() /
                    ((StaggeredGridLayoutManager) mLayoutManager).getSpanCount() /
                    mGifs.get(position).getHeight();
        }
        holder.mCardView.getLayoutParams().height = (int) ((float) mGifs.get(position).getHeight() * scaleFactor);
        holder.mSimpleDraweeView.getHierarchy().setPlaceholderImage(AdapterUtils.getRandomColor());
        holder.mFavoriteButton.setImageResource(mButton.getImageResources(position));
    }

    @Override
    public int getItemCount() {
        return mGifs.size();
    }

    final class GifHolder extends RecyclerView.ViewHolder {

        private final CardView mCardView;
        private final SimpleDraweeView mSimpleDraweeView;
        private final ImageButton mFavoriteButton;

        GifHolder(View itemView) {
            super(itemView);
            mCardView = itemView.findViewById(R.id.card_view_gif);
            mSimpleDraweeView = itemView.findViewById(R.id.my_image_view);
            mFavoriteButton = itemView.findViewById(R.id.button_favorite);
            mFavoriteButton.setOnClickListener(view -> mButton.action(getAdapterPosition()));
        }
    }
}
