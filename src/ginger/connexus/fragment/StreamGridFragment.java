package ginger.connexus.fragment;

import ginger.connexus.BuildConfig;
import ginger.connexus.activity.ImageGridActivity;
import ginger.connexus.model.ConnexusStream;
import ginger.connexus.network.ConnexusApi;
import ginger.connexus.network.RequestAllStreams;
import ginger.connexus.network.RequestNearbyStreams;
import ginger.connexus.network.RequestSubscribedStreams;

import java.util.ArrayList;

import android.content.Intent;
import android.location.Location;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Toast;

import com.octo.android.robospice.persistence.exception.SpiceException;
import com.octo.android.robospice.request.listener.RequestListener;
import com.octo.android.robospice.request.retrofit.RetrofitSpiceRequest;

public class StreamGridFragment extends GridFragment {

    private static final String TAG = StreamGridFragment.class.toString();

    public static final String REQUEST = "request";
    public static final String LOCATION = "location";
    public static final int REQUEST_ALL = 0;
    public static final int REQUEST_SUBSCRIBED = 1;
    public static final int REQUEST_NEARBY = 2;
    public static final int REQUEST_SEARCH = 100;

    private RetrofitSpiceRequest<ConnexusStream.List, ConnexusApi> mStreamRequest;
    private ConnexusStream.List mStreams;

    /**
     * Empty constructor as per the Fragment documentation
     */
    public StreamGridFragment() {
    }

    public static StreamGridFragment newInstance(final Intent intent, Bundle arguments) {
        StreamGridFragment fragment = new StreamGridFragment();
        arguments.putParcelable(FORWARD_INTENT, intent);
        fragment.setArguments(arguments);
        return fragment;
    }

    public void setStreams(ConnexusStream.List streams) {
        this.mStreams = streams;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        reloadFromArguments(getArguments());
    }

    private void reloadFromArguments(Bundle arguments) {
        mIntent = (Intent) arguments.getParcelable(FORWARD_INTENT);
        final int request = arguments.getInt(REQUEST, -1);
        switch (request) {
            case REQUEST_ALL:
                mStreamRequest = new RequestAllStreams();
                break;
            case REQUEST_SUBSCRIBED:
                mStreamRequest = new RequestSubscribedStreams();
                break;
            case REQUEST_NEARBY:
                Location location = (Location) arguments.getParcelable(LOCATION);
                double lat = location.getLatitude();
                double lon = location.getLongitude();
                if (BuildConfig.DEBUG) {
                    Toast.makeText(getActivity(), "Latitude " + lat + " Longitude " + lon, Toast.LENGTH_LONG).show();
                }
                mStreamRequest = new RequestNearbyStreams(lat, lon);
                break;
            case REQUEST_SEARCH:
                ArrayList<String> imageUrls = new ArrayList<String>(mStreams.size());
                for (ConnexusStream stream : mStreams) {
                    imageUrls.add(stream.getCoverUrl());
                }
                reloadImages(imageUrls);
                return;
            default:
                throw new UnsupportedOperationException(TAG);
        }
        getSpiceManager().execute(mStreamRequest, new ConnexusStreamRequestListener());
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View v, int position, long id) {
        ConnexusStream stream = mStreams.get(position);
        mIntent.putExtra(ImageGridActivity.EXTRA_STREAM, stream.getId());
        mIntent.putExtra(ImageGridActivity.EXTRA_STREAM_NAME, stream.getName());
        startActivity(mIntent);
    }

    private final class ConnexusStreamRequestListener implements RequestListener<ConnexusStream.List> {

        @Override
        public void onRequestFailure(SpiceException spiceException) {
            Log.w(TAG, "onRequestFailure: " + spiceException.toString());
            Toast.makeText(getActivity(), "Error occured:" + spiceException.toString(), Toast.LENGTH_SHORT).show();
        }

        @Override
        public void onRequestSuccess(final ConnexusStream.List result) {
            mStreams = result;
            ArrayList<String> imageUrls = new ArrayList<String>(result.size());
            for (ConnexusStream stream : result) {
                imageUrls.add(stream.getCoverUrl());
            }
            StreamGridFragment.this.reloadImages(imageUrls);
        }
    }

}
