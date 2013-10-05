/*
 * Copyright (C) 2012 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package ginger.connexus.fragment;

import ginger.connexus.activity.ImageDetailActivity;
import ginger.connexus.model.ConnexusImage;
import ginger.connexus.network.RequestStreamImages;
import ginger.connexus.util.Utils;

import java.util.ArrayList;

import android.annotation.TargetApi;
import android.app.ActivityOptions;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Toast;

import com.octo.android.robospice.persistence.exception.SpiceException;
import com.octo.android.robospice.request.listener.RequestListener;

/**
 * The main fragment that powers the ImageGridActivity screen. Fairly straight
 * forward GridView implementation with the key addition being the ImageWorker
 * class w/ImageCache to load children asynchronously, keeping the UI nice and
 * smooth and caching thumbnails for quick retrieval. The cache is retained over
 * configuration changes like orientation change so the images are populated
 * quickly if, for example, the user rotates the device.
 */
public class ImageGridFragment extends GridFragment {

    private static final String TAG = ImageGridFragment.class.toString();

    public static final String STREAM_ID = "stream_id";

    private RequestStreamImages mImageRequest;

    /**
     * Empty constructor as per the Fragment documentation
     */
    public ImageGridFragment() {
    }

    public static ImageGridFragment newInstance(final Intent intent, long streamId) {
        ImageGridFragment fragment = new ImageGridFragment();
        Bundle arguments = new Bundle();
        arguments.putParcelable(FORWARD_INTENT, intent);
        arguments.putLong(STREAM_ID, streamId);
        fragment.setArguments(arguments);
        return fragment;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        reloadFromArguments(getArguments());
    }

    private void reloadFromArguments(Bundle arguments) {
        mIntent = (Intent) arguments.getParcelable(FORWARD_INTENT);
        long streamId = arguments.getLong(STREAM_ID);

        mImageRequest = new RequestStreamImages(streamId);
        getSpiceManager().execute(mImageRequest, new ConnexusImageRequestListener());
    }

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    @Override
    public void onItemClick(AdapterView<?> parent, View v, int position, long id) {
        mIntent.putExtra(ImageDetailActivity.EXTRA_IMAGE, (int) id);
        String[] imageUrls = new String[mAdapter.getCount()];
        for (int i = 0; i < imageUrls.length; ++i) {
            imageUrls[i] = mAdapter.getItem(i);
        }
        mIntent.putExtra(ImageDetailActivity.EXTRA_IMAGE_URLS, imageUrls);
        if (Utils.hasJellyBean()) {
            // makeThumbnailScaleUpAnimation() looks kind of ugly here as the
            // loading spinner may show plus the thumbnail image in GridView is
            // cropped. so using makeScaleUpAnimation() instead.
            ActivityOptions options = ActivityOptions.makeScaleUpAnimation(v, 0, 0, v.getWidth(), v.getHeight());
            getActivity().startActivity(mIntent, options.toBundle());
        } else {
            startActivity(mIntent);
        }
    }

    private final class ConnexusImageRequestListener implements RequestListener<ConnexusImage.List> {

        @Override
        public void onRequestFailure(SpiceException spiceException) {
            Log.w(TAG, "onRequestFailure: " + spiceException.toString());
            Toast.makeText(getActivity(), "Error occured:" + spiceException.toString(), Toast.LENGTH_SHORT).show();
        }

        @Override
        public void onRequestSuccess(final ConnexusImage.List result) {
            ArrayList<String> imageUrls = new ArrayList<String>(result.size());
            for (ConnexusImage image : result) {
                imageUrls.add(image.getUrl());
            }
            ImageGridFragment.this.reloadImages(imageUrls);
        }
    }

}
