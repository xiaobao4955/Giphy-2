package com.alexeyosadchy.giphy.view.base;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.alexeyosadchy.giphy.App;
import com.alexeyosadchy.giphy.R;
import com.alexeyosadchy.giphy.di.component.ActivityComponent;
import com.alexeyosadchy.giphy.di.component.DaggerActivityComponent;
import com.alexeyosadchy.giphy.di.module.ActivityModule;
import com.alexeyosadchy.giphy.model.storage.GifView;
import com.alexeyosadchy.giphy.view.screens.trends.ITrendGifListActivity;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

public abstract class BaseListGifActivity extends AppCompatActivity implements BaseListGifView {

    protected ActivityComponent mActivityComponent;
    protected GifListAdapter mGifListAdapter;
    protected final LinearLayoutManager layoutManager = new LinearLayoutManager(this);

    private Unbinder mUnBinder;

    @BindView(R.id.progressBar)
    protected ProgressBar mProgressBar;

    @BindView(R.id.rv_gifs)
    protected RecyclerView mRecyclerView;

    @Override
    protected void onCreate(@Nullable final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_trend_gif_list);
        mUnBinder = ButterKnife.bind(this);
        mActivityComponent = DaggerActivityComponent.builder()
                .applicationComponent(((App) getApplication()).getApplicationComponent())
                .activityModule(new ActivityModule(this))
                .build();
    }

    @Override
    public void configurateAdapter() {
        mRecyclerView.setLayoutManager(layoutManager);
        mRecyclerView.clearOnScrollListeners();
    }

    @Override
    public void addItemsToList(final List<GifView> gifs) {
        mGifListAdapter.updateList(gifs);
    }

    @Override
    public void removeItem(final int position) {
        mGifListAdapter.removeItem(position);
    }

    @Override
    public void clearList() {
        mGifListAdapter.clearList();
    }

    @Override
    public int getSizeList() {
        return mGifListAdapter.getSizeList();
    }

    @Override
    public void updateList(int position) {
        mGifListAdapter.notifyItemChanged(position);
    }

    @Override
    public void showLoading() {
        mProgressBar.setVisibility(View.VISIBLE);
    }

    @Override
    public void hideLoading() {
        mProgressBar.setVisibility(View.GONE);
    }

    @Override
    protected void onDestroy() {
        mUnBinder.unbind();
        super.onDestroy();
    }

    @Override
    public void closeApplication() {
        super.onBackPressed();
    }

    @Override
    public void showError(final String message, final ITrendGifListActivity.Callback callback) {
        if (message != null) {
            showSnackBar(message, callback);
        } else {
            showSnackBar(getString(R.string.error_some), callback);
        }
    }

    @Override
    public void showMessage(final String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }

    private void showSnackBar(final String message, final ITrendGifListActivity.Callback callback) {
        final Snackbar snackbar = Snackbar.make(findViewById(android.R.id.content),
                message, Snackbar.LENGTH_INDEFINITE).setAction(R.string.text_btn_snackbar, view -> callback.call());
        final View sbView = snackbar.getView();
        final TextView textView = sbView.findViewById(android.support.design.R.id.snackbar_text);
        textView.setTextColor(ContextCompat.getColor(this, R.color.text_color_inverse));
        snackbar.show();
    }
}
