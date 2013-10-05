package ginger.connexus.network;

import ginger.connexus.model.ConnexusStream;

import com.octo.android.robospice.request.retrofit.RetrofitSpiceRequest;

public class RequestSubscribedStreams extends RetrofitSpiceRequest<ConnexusStream.List, ConnexusApi> {

	public RequestSubscribedStreams() {
		super(ConnexusStream.List.class, ConnexusApi.class);
	}

	@Override
	public ConnexusStream.List loadDataFromNetwork() throws Exception {
		return getService().subscribedStreams();
	}

}
