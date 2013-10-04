package ginger.connexus.fragment;

import ginger.connexus.BuildConfig;
import ginger.connexus.R;
import ginger.connexus.activity.ImageDetailActivity;
import ginger.connexus.adapter.ImageAdapter;
import ginger.connexus.util.ImageCache.ImageCacheParams;
import ginger.connexus.util.ImageFetcher;
import ginger.connexus.util.Utils;

import java.util.ArrayList;
import java.util.List;

import android.annotation.TargetApi;
import android.app.ActivityOptions;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.Toast;

public abstract class GridFragment extends SpiceFragment implements AdapterView.OnItemClickListener {

	private static final String TAG = GridFragment.class.toString();
	private static final String IMAGE_CACHE_DIR = "thumbs";

	public static final String REQUEST = "request";
	public static final String FORWARD_INTENT = "fwd_intent";
	public static final String USER = "user";

	private int mImageThumbSize;
	private int mImageThumbSpacing;
	private ImageFetcher mImageFetcher;

	protected Intent mIntent;
	protected ImageAdapter mAdapter;

	protected void reloadImages(List<String> imageUrls) {
		if (getActivity() != null && mAdapter != null) {
			if (imageUrls != null) {
				mAdapter.clear();
				mAdapter.addAll(imageUrls);
				mAdapter.notifyDataSetChanged();
			}
		}
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setHasOptionsMenu(true);

		mImageThumbSize = getResources().getDimensionPixelSize(R.dimen.image_thumbnail_size);
		mImageThumbSpacing = getResources().getDimensionPixelSize(R.dimen.image_thumbnail_spacing);

		ImageCacheParams cacheParams = new ImageCacheParams(getActivity(), IMAGE_CACHE_DIR);

		// Set memory cache to 25% of app memory
		cacheParams.setMemCacheSizePercent(0.25f);

		// The ImageFetcher takes care of loading images into our ImageView
		// children asynchronously
		mImageFetcher = new ImageFetcher(getActivity(), mImageThumbSize);
		mImageFetcher.setLoadingImage(R.drawable.empty_photo);
		mImageFetcher.addImageCache(getActivity().getSupportFragmentManager(), cacheParams);

		mAdapter = new ImageAdapter(getActivity(), new ArrayList<String>(), mImageFetcher);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

		final View v = inflater.inflate(R.layout.image_grid_fragment, container, false);
		final GridView mGridView = (GridView) v.findViewById(R.id.gridView);
		mGridView.setAdapter(mAdapter);
		mGridView.setOnItemClickListener(this);
		mGridView.setOnScrollListener(new AbsListView.OnScrollListener() {
			@Override
			public void onScrollStateChanged(AbsListView absListView, int scrollState) {
				// Pause fetcher to ensure smoother scrolling when flinging
				if (scrollState == AbsListView.OnScrollListener.SCROLL_STATE_FLING) {
					mImageFetcher.setPauseWork(true);
				} else {
					mImageFetcher.setPauseWork(false);
				}
			}

			@Override
			public void onScroll(AbsListView absListView, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
			}
		});

		// This listener is used to get the final width of the GridView and then
		// calculate the
		// number of columns and the width of each column. The width of each
		// column is variable
		// as the GridView has stretchMode=columnWidth. The column width is used
		// to set the height
		// of each view so we get nice square thumbnails.
		mGridView.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
			@Override
			public void onGlobalLayout() {
				if (mAdapter.getNumColumns() == 0) {
					final int numColumns = (int) Math.floor(mGridView.getWidth()
					        / (mImageThumbSize + mImageThumbSpacing));
					if (numColumns > 0) {
						final int columnWidth = (mGridView.getWidth() / numColumns) - mImageThumbSpacing;
						mAdapter.setNumColumns(numColumns);
						mAdapter.setItemHeight(columnWidth);
						if (BuildConfig.DEBUG) {
							Log.d(TAG, "onCreateView - numColumns set to " + numColumns);
						}
					}
				}
			}
		});

		return v;
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		reloadFromArguments(getArguments());
	}

	protected abstract void reloadFromArguments(Bundle arguments);

	@Override
	public void onResume() {
		super.onResume();
		mImageFetcher.setExitTasksEarly(false);
		mAdapter.notifyDataSetChanged();
	}

	@Override
	public void onPause() {
		super.onPause();
		mImageFetcher.setPauseWork(false);
		mImageFetcher.setExitTasksEarly(true);
		mImageFetcher.flushCache();
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
		mImageFetcher.closeCache();
	}

	@TargetApi(16)
	@Override
	public void onItemClick(AdapterView<?> parent, View v, int position, long id) {
		mIntent.putExtra(ImageDetailActivity.EXTRA_IMAGE, (int) id);
		if (Utils.hasJellyBean()) {
			// makeThumbnailScaleUpAnimation() looks kind of ugly here as the
			// loading spinner may
			// show plus the thumbnail image in GridView is cropped. so using
			// makeScaleUpAnimation() instead.
			ActivityOptions options = ActivityOptions.makeScaleUpAnimation(v, 0, 0, v.getWidth(), v.getHeight());
			getActivity().startActivity(mIntent, options.toBundle());
		} else {
			startActivity(mIntent);
		}
	}

	@Override
	public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
		inflater.inflate(R.menu.main_menu, menu);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
			case R.id.clear_cache:
				mImageFetcher.clearCache();
				Toast.makeText(getActivity(), R.string.clear_cache_complete_toast, Toast.LENGTH_SHORT).show();
				return true;
		}
		return super.onOptionsItemSelected(item);
	}

}
