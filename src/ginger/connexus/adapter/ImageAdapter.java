package ginger.connexus.adapter;

import ginger.connexus.ui.RecyclingImageView;
import ginger.connexus.util.ImageFetcher;

import java.util.List;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.ArrayAdapter;
import android.widget.GridView;
import android.widget.ImageView;

/**
 * The main adapter that backs the GridView. This is fairly standard except the
 * number of columns in the GridView is used to create a fake top row of empty
 * views as we use a transparent ActionBar and don't want the real top row of
 * images to start off covered by it.
 */
public class ImageAdapter extends ArrayAdapter<String> {

    private final Context mContext;
    private final ImageFetcher mImageFetcher;
    private int mItemHeight = 0;
    private int mNumColumns = 0;
    private GridView.LayoutParams mImageViewLayoutParams;

    public ImageAdapter(Context context, List<String> imageUrls, ImageFetcher imageFetcher) {
        super(context, -1, -1, imageUrls);
        mContext = context;
        mImageFetcher = imageFetcher;

        mImageViewLayoutParams = new GridView.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup container) {
        // ImageView thumbnails
        ImageView imageView;
        if (convertView == null) {
            // if it's not recycled, instantiate and initialize
            imageView = new RecyclingImageView(mContext);
            imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
            imageView.setLayoutParams(mImageViewLayoutParams);
        } else {
            // Otherwise re-use the converted view
            imageView = (ImageView) convertView;
        }

        // Check the height matches our calculated column width
        if (imageView.getLayoutParams().height != mItemHeight) {
            imageView.setLayoutParams(mImageViewLayoutParams);
        }

        // Finally load the image asynchronously into the ImageView, this also
        // takes care of setting a placeholder image while the background thread
        // runs
        mImageFetcher.loadImage(getItem(position), imageView);
        return imageView;
    }

    /**
     * Sets the item height. Useful for when we know the column width so the
     * height can be set to match.
     *
     * @param height
     */
    public void setItemHeight(int height) {
        if (height == mItemHeight) {
            return;
        }
        mItemHeight = height;
        mImageViewLayoutParams = new GridView.LayoutParams(LayoutParams.MATCH_PARENT, mItemHeight);
        mImageFetcher.setImageSize(height);
        notifyDataSetChanged();
    }

    public void setNumColumns(int numColumns) {
        mNumColumns = numColumns;
    }

    public int getNumColumns() {
        return mNumColumns;
    }
}
