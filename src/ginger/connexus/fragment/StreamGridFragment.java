package ginger.connexus.fragment;

import ginger.connexus.model.ConnexusStream;
import ginger.connexus.network.ConnexusApi;
import ginger.connexus.network.RequestAllStreams;
import ginger.connexus.network.RequestNearbyStreams;
import ginger.connexus.network.RequestSubscribedStreams;
import ginger.connexus.network.RequestUserStreams;
import ginger.connexus.util.Images;

import java.util.ArrayList;
import java.util.Arrays;

import android.content.Intent;
import android.os.Bundle;

import com.octo.android.robospice.persistence.exception.SpiceException;
import com.octo.android.robospice.request.listener.RequestListener;
import com.octo.android.robospice.request.retrofit.RetrofitSpiceRequest;

public class StreamGridFragment extends GridFragment {

	private static final String TAG = StreamGridFragment.class.toString();

	public static final int REQUEST_ALL = 0;
	public static final int REQUEST_SUBSCRIBED = 1;
	public static final int REQUEST_USER = 2;
	public static final int REQUEST_NEARBY = 3;

	private RetrofitSpiceRequest<ConnexusStream.List, ConnexusApi> mStreamRequest;

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

	@Override
	protected void reloadFromArguments(Bundle arguments) {
		mIntent = (Intent) arguments.getParcelable(FORWARD_INTENT);
		final int request = arguments.getInt(REQUEST, -1);
		switch (request) {
			case REQUEST_ALL:
				mStreamRequest = new RequestAllStreams();
				break;
			case REQUEST_SUBSCRIBED: {
				mStreamRequest = new RequestSubscribedStreams();
				break;
			}
			case REQUEST_USER: {
				mStreamRequest = new RequestUserStreams();
				break;
			}
			case REQUEST_NEARBY:
				mStreamRequest = new RequestNearbyStreams();
				break;
			default:
				throw new UnsupportedOperationException(TAG);
		}

		// TODO temporary
		reloadImages(Arrays.asList(Images.imageThumbUrls));
		// getSpiceManager().execute(mStreamRequest, new
		// ConnexusStreamRequestListener());
	}

	public final class ConnexusStreamRequestListener implements RequestListener<ConnexusStream.List> {

		@Override
		public void onRequestFailure(SpiceException spiceException) {
			// TODO failure
		}

		@Override
		public void onRequestSuccess(final ConnexusStream.List result) {
			ArrayList<String> imageUrls = new ArrayList<String>(result.size());
			for (ConnexusStream stream : result) {
				imageUrls.add(stream.cover);
			}
			reloadImages(imageUrls);
		}
	}

}
