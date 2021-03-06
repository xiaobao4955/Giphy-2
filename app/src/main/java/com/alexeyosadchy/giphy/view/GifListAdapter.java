package com.alexeyosadchy.giphy.view;

import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.alexeyosadchy.giphy.R;
import com.alexeyosadchy.giphy.presenter.ITrendGifListPresenter;
import com.alexeyosadchy.giphy.utils.AdapterUtils;
import com.facebook.drawee.backends.pipeline.Fresco;
import com.facebook.drawee.drawable.ProgressBarDrawable;
import com.facebook.drawee.interfaces.DraweeController;
import com.facebook.drawee.view.SimpleDraweeView;

import java.util.List;

public class GifListAdapter extends RecyclerView.Adapter<GifListAdapter.GifHolder> {

    private List<GifView> mGifs;
    private RecyclerView.LayoutManager mLayoutManager;
    private ITrendGifListPresenter mPresenter;

    public GifListAdapter(List<GifView> gifs, RecyclerView.LayoutManager layoutManager, ITrendGifListPresenter presenter) {
        mGifs = gifs;
        mLayoutManager = layoutManager;
        mPresenter = presenter;
    }

    public void setLayoutManager(RecyclerView.LayoutManager layoutManager) {
        mLayoutManager = layoutManager;
    }

    @Override
    public GifHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_view_gif, parent, false);
        return new GifHolder(view);
    }

    @Override
    public void onBindViewHolder(GifHolder holder, int position) {
        float scaleFactor = 1f;
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
    }

    @Override
    public int getItemCount() {
        return mGifs.size();
    }

    public class GifHolder extends RecyclerView.ViewHolder implements View.OnLongClickListener {

        private CardView mCardView;
        private SimpleDraweeView mSimpleDraweeView;

        public GifHolder(View itemView) {
            super(itemView);
            mCardView = itemView.findViewById(R.id.card_view_gif);
            mSimpleDraweeView = itemView.findViewById(R.id.my_image_view);
            itemView.setOnLongClickListener(this);
        }

        @Override
        public boolean onLongClick(View view) {
            mPresenter.onLongClickItem(getAdapterPosition());
            return false;
        }
    }
}
