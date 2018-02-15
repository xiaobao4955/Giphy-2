package com.alexeyosadchy.giphy.view.base;

import android.content.res.Configuration;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.alexeyosadchy.giphy.App;
import com.alexeyosadchy.giphy.R;
import com.alexeyosadchy.giphy.di.component.ActivityComponent;
import com.alexeyosadchy.giphy.di.component.DaggerActivityComponent;
import com.alexeyosadchy.giphy.di.module.ActivityModule;
import com.alexeyosadchy.giphy.view.GifView;
import com.alexeyosadchy.giphy.view.screens.trends.ITrendGifListActivity;

import java.util.List;

import butterknife.BindView;
import butterknife.Unbinder;

public abstract class BaseListGifActivity extends AppCompatActivity implements BaseListGifView {

    protected ActivityComponent mActivityComponent;
    protected GifListAdapter mGifListAdapter;
    protected RecyclerView.LayoutManager layoutManager;

    private Unbinder mUnBinder;

    @BindView(R.id.progressBar)
    protected ProgressBar mProgressBar;

    @BindView(R.id.rv_gifs)
    protected RecyclerView mRecyclerView;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mActivityComponent = DaggerActivityComponent.builder()
                .applicationComponent(((App) getApplication()).getApplicationComponent())
                .activityModule(new ActivityModule(this))
                .build();
    }

    @Override
    public void prepareView(List<GifView> gifs, int position) {
        if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT) {
            layoutManager = new LinearLayoutManager(this);
        } else if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE) {
            layoutManager = new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL);
        }
        mRecyclerView.setLayoutManager(layoutManager);
        mRecyclerView.clearOnScrollListeners();
        mRecyclerView.scrollToPosition(position);
    }

    @Override
    public void updateList() {
        mGifListAdapter.notifyDataSetChanged();
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
    public void showError(String message, ITrendGifListActivity.Callback callback) {
        if (message != null) {
            showSnackBar(message, callback);
        } else {
            showSnackBar(getString(R.string.error_some), callback);
        }
    }

    @Override
    public void showMessage(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }

    private void showSnackBar(String message, ITrendGifListActivity.Callback callback) {
        Snackbar snackbar = Snackbar.make(findViewById(android.R.id.content),
                message, Snackbar.LENGTH_INDEFINITE).setAction(R.string.text_btn_snackbar, view -> callback.call());
        View sbView = snackbar.getView();
        TextView textView = sbView.findViewById(android.support.design.R.id.snackbar_text);
        textView.setTextColor(ContextCompat.getColor(this, R.color.text_color_inverse));
        snackbar.show();
    }

    protected void setUnBinder(Unbinder unBinder) {
        mUnBinder = unBinder;
    }
}
