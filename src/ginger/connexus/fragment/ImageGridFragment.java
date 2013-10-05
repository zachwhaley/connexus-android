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

import ginger.connexus.model.ConnexusImage;
import ginger.connexus.network.RequestStreamImages;
import ginger.connexus.util.Images;

import java.util.ArrayList;
import java.util.Arrays;

import android.content.Intent;
import android.os.Bundle;

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

    private RequestStreamImages mImageRequest;

    /**
     * Empty constructor as per the Fragment documentation
     */
    public ImageGridFragment() {
    }

    public static ImageGridFragment newInstance(final Intent intent, Bundle arguments) {
        ImageGridFragment fragment = new ImageGridFragment();
        arguments.putParcelable(FORWARD_INTENT, intent);
        fragment.setArguments(arguments);
        return fragment;
    }

    @Override
    protected void reloadFromArguments(Bundle arguments) {
        mIntent = (Intent) arguments.getParcelable(FORWARD_INTENT);

        mImageRequest = new RequestStreamImages();

        // TODO temporary
        reloadImages(Arrays.asList(Images.imageThumbUrls));

        // getSpiceManager().execute(mImageRequest, new
        // ConnexusImageRequestListener());
    }

    public final class ConnexusImageRequestListener implements RequestListener<ConnexusImage.List> {

        @Override
        public void onRequestFailure(SpiceException spiceException) {
            // TODO failure
        }

        @Override
        public void onRequestSuccess(final ConnexusImage.List result) {
            ArrayList<String> imageUrls = new ArrayList<String>(result.size());
            for (ConnexusImage image : result) {
                imageUrls.add(image.link);
            }
            reloadImages(imageUrls);
        }
    }

}
