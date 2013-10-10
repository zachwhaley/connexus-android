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

package ginger.connexus.activity;

import ginger.connexus.BuildConfig;
import ginger.connexus.fragment.ImageGridFragment;
import ginger.connexus.fragment.StreamGridFragment;
import ginger.connexus.util.Utils;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;

/**
 * Simple FragmentActivity to hold the main {@link ImageGridFragment} and not
 * much else.
 */
public class ImageGridActivity extends BaseActivity {

    private static final String TAG = ImageGridActivity.class.toString();

    public static final String EXTRA_STREAM = "extra_stream";
    public static final String EXTRA_STREAM_NAME = "extra_stream_name";

    /*
     * Called when the Activity becomes visible.
     */
    @Override
    protected void onStart() {
        super.onStart();
        startLocationClient();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        if (BuildConfig.DEBUG) {
            Utils.enableStrictMode();
        }
        super.onCreate(savedInstanceState);

        if (getSupportFragmentManager().findFragmentByTag(TAG) == null) {
            final FragmentTransaction ft = getSupportFragmentManager().beginTransaction();

            Intent intent = new Intent(this, ImageDetailActivity.class);
            final long streamId = getIntent().getLongExtra(EXTRA_STREAM, -1L);
            final String streamName = getIntent().getStringExtra(EXTRA_STREAM_NAME);

            Bundle arguments = new Bundle();
            arguments.putLong(ImageGridFragment.STREAM_ID, streamId);
            arguments.putString(ImageGridFragment.STREAM_NAME, streamName);
            arguments.putParcelable(StreamGridFragment.LOCATION, getLocation());

            ft.add(android.R.id.content, ImageGridFragment.newInstance(intent, arguments), TAG);
            ft.commit();
        }
    }
}
