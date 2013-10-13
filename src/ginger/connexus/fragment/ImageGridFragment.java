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

import ginger.connexus.R;
import ginger.connexus.activity.BaseActivity;
import ginger.connexus.activity.ImageDetailActivity;
import ginger.connexus.model.ConnexusImage;
import ginger.connexus.network.RequestStreamImages;
import ginger.connexus.network.RequestUploadURL;
import ginger.connexus.network.UploadImage;
import ginger.connexus.util.Utils;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;
import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.Activity;
import android.app.ActivityOptions;
import android.content.Intent;
import android.location.Location;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
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
    private static final int CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE = 100;

    public static final String STREAM_ID = "stream_id";
    public static final String STREAM_NAME = "stream_name";
    public static final int MEDIA_TYPE_IMAGE = 1;
    public static final int MEDIA_TYPE_VIDEO = 2;
    public float lat;
    public float lng;

    private RequestStreamImages mImageRequest;
    private Uri fileUri;

    /**
     * Empty constructor as per the Fragment documentation
     */
    public ImageGridFragment() {
    }

    public static ImageGridFragment newInstance(final Intent intent,
            Bundle arguments) {
        ImageGridFragment fragment = new ImageGridFragment();
        arguments.putParcelable(FORWARD_INTENT, intent);
        fragment.setArguments(arguments);
        return fragment;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.camera_menu, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
        case R.id.action_takepic:
            // create Intent to take a picture and return control to the
            // calling application
            Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

            // fileUri is public. Use this handle to access the saved image
            // from other classes.
            // create a file to save the image
            fileUri = Uri.fromFile(getOutputMediaFile(MEDIA_TYPE_IMAGE));
            // set the image file name
            intent.putExtra(MediaStore.EXTRA_OUTPUT, fileUri);

            // start the image capture Intent
            startActivityForResult(intent, CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE);
            
            // capture current location
            Location loc = ((BaseActivity) getActivity()).getLocation();
            lat = (float) loc.getLatitude();
            lng = (float) loc.getLongitude();
            
            return true;
        default:
            return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        reloadFromArguments(getArguments());
    }

    private void reloadFromArguments(Bundle arguments) {
        mIntent = (Intent) arguments.getParcelable(FORWARD_INTENT);
        long streamId = arguments.getLong(STREAM_ID);
        String streamName = arguments.getString(STREAM_NAME);
        getActivity().getActionBar().setTitle(streamName);

        mImageRequest = new RequestStreamImages(streamId);
        getSpiceManager().execute(mImageRequest,
                new ConnexusImageRequestListener());
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
            ActivityOptions options = ActivityOptions.makeScaleUpAnimation(v,
                    0, 0, v.getWidth(), v.getHeight());
            getActivity().startActivity(mIntent, options.toBundle());
        } else {
            startActivity(mIntent);
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        Log.i(TAG, "onActivityResult");
        if (requestCode == CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE) {
            if (resultCode == Activity.RESULT_OK) {
                // Image captured and saved to fileUri specified in the Intent
                String uploadUrl = RequestUploadURL.getUploadURL();
                Log.i(TAG, "Upload URL: " + uploadUrl);
                String[] split = uploadUrl.split("upload/");
                uploadUrl = split[1];
                Log.i(TAG, "Stripped URL: " + uploadUrl);

                long streamId = getArguments().getLong(STREAM_ID);
                File file = new File(fileUri.getPath());
                UploadCallback cb = new UploadCallback();
                new UploadImage(uploadUrl, streamId, lat, lng, file, cb)
                        .post();
            } else if (resultCode == Activity.RESULT_CANCELED) {
                // User cancelled the image capture
                Toast.makeText(getActivity(), "Canceled", Toast.LENGTH_LONG)
                        .show();
            } else {
                // Image capture failed, advise user
                Toast.makeText(getActivity(), "Image capture failed",
                        Toast.LENGTH_LONG).show();
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    private final class UploadCallback implements Callback<ConnexusImage> {

        @Override
        public void failure(RetrofitError e) {
            Log.e(TAG, e.getMessage() == null ? "NULL MESSAGE" : e.getMessage());
            Log.e(TAG, e.getUrl() == null ? "NULL URL" : e.getUrl());
            Log.e(TAG, e.getResponse().getReason());
            /*
             * e.getCause().getMessage()); Log.e(TAG,
             * e.getStackTrace().toString());
             */
            Toast.makeText(getActivity(), "Error occured:" + e.toString(),
                    Toast.LENGTH_LONG).show();
        }

        @Override
        public void success(ConnexusImage image, Response resp) {
            Toast.makeText(getActivity(), "Upload worked:", Toast.LENGTH_LONG)
                    .show();
        }
    }

    /** Create a File for saving an image or video */
    @SuppressLint("SimpleDateFormat")
    private static File getOutputMediaFile(int type) {
        // To be safe, you should check that the SDCard is mounted
        // using Environment.getExternalStorageState() before doing this.

        File mediaStorageDir = new File(
                Environment
                        .getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES),
                "Connexus");
        // This location works best if you want the created images to be shared
        // between applications and persist after your app has been uninstalled.
        // Create the storage directory if it does not exist
        if (!mediaStorageDir.exists()) {
            if (!mediaStorageDir.mkdirs()) {
                Log.d("Connexus", "failed to create directory");
                return null;
            }
        }

        // Create a media file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss")
                .format(new Date());
        File mediaFile;
        if (type == MEDIA_TYPE_IMAGE) {
            mediaFile = new File(mediaStorageDir.getPath() + File.separator
                    + "IMG_" + timeStamp + ".jpg");
        } else {
            return null;
        }

        return mediaFile;
    }

    private final class ConnexusImageRequestListener implements
            RequestListener<ConnexusImage.List> {

        @Override
        public void onRequestFailure(SpiceException spiceException) {
            Log.w(TAG, "onRequestFailure: " + spiceException.toString());
            Toast.makeText(getActivity(),
                    "Error occured:" + spiceException.toString(),
                    Toast.LENGTH_SHORT).show();
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

    private final class ConnexusImageUploadListener implements
            RequestListener<String> {

        @Override
        public void onRequestFailure(SpiceException spiceException) {
            Log.w(TAG, "onRequestFailure: " + spiceException.toString());
            Toast.makeText(getActivity(),
                    "Error occured:" + spiceException.toString(),
                    Toast.LENGTH_SHORT).show();
        }

        @Override
        public void onRequestSuccess(final String result) {
            Log.i(TAG, "Upload worked");
        }
    }

}
