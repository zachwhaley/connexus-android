/*
 * Copyright (C) 2013 Zach Whaley, Trevor Latson
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

import ginger.connexus.fragment.StreamGridFragment;
import ginger.connexus.model.ConnexusStream;
import ginger.connexus.network.RequestAllStreams;
import ginger.connexus.service.ConnexusService;
import android.app.SearchManager;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;

import com.octo.android.robospice.SpiceManager;
import com.octo.android.robospice.persistence.exception.SpiceException;
import com.octo.android.robospice.request.listener.RequestListener;

public class SearchableActivity extends FragmentActivity {

    private static final String TAG = SearchableActivity.class.toString();

    private final SpiceManager mSpiceManager = new SpiceManager(ConnexusService.class);
    private RequestAllStreams mRequest;
    private String mQuery;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mRequest = new RequestAllStreams();
        // Get the intent, verify the action and get the query
        Intent intent = getIntent();
        if (Intent.ACTION_SEARCH.equals(intent.getAction())) {
            mQuery = intent.getStringExtra(SearchManager.QUERY);
        }
    }

    @Override
    public void onStart() {
        super.onStart();
        mSpiceManager.start(this);
        mSpiceManager.execute(mRequest, new ConnexusStreamRequestListener());
    }

    @Override
    public void onStop() {
        // Please review https://github.com/octo-online/robospice/issues/96 for
        // the reason of that ugly if statement.
        if (mSpiceManager.isStarted()) {
            mSpiceManager.shouldStop();
        }
        super.onStop();
    }

    private void showResults(ConnexusStream.List streams) {
        if (getSupportFragmentManager().findFragmentByTag(TAG) == null) {
            final FragmentTransaction ft = getSupportFragmentManager().beginTransaction();

            Intent intent = new Intent(this, ImageGridActivity.class);

            Bundle arguments = new Bundle();
            arguments.putInt(StreamGridFragment.REQUEST, StreamGridFragment.REQUEST_SEARCH);
            StreamGridFragment gridFragment = StreamGridFragment.newInstance(intent, arguments);
            gridFragment.setStreams(streams);
            ft.add(android.R.id.content, gridFragment, TAG);
            ft.commit();
        }
    }

    private final class ConnexusStreamRequestListener implements RequestListener<ConnexusStream.List> {

        @Override
        public void onRequestFailure(SpiceException spiceException) {
            Log.w(TAG, "onRequestFailure: " + spiceException.toString());
        }

        @Override
        public void onRequestSuccess(final ConnexusStream.List result) {
            ConnexusStream.List streams = new ConnexusStream.List();
            for (ConnexusStream stream : result) {
                Log.i(TAG, "Stream " + stream.getName());
                if (stream.isMatch(mQuery)) {
                    streams.add(stream);
                }
            }
            SearchableActivity.this.showResults(streams);
        }
    }
}
