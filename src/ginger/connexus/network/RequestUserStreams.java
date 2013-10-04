package ginger.connexus.network;

import ginger.connexus.model.ConnexusStream;

import com.octo.android.robospice.request.retrofit.RetrofitSpiceRequest;

public class RequestUserStreams extends RetrofitSpiceRequest<ConnexusStream.List, ConnexusApi> {

    public RequestUserStreams() {
        super(ConnexusStream.List.class, ConnexusApi.class);
    }

    @Override
    public ConnexusStream.List loadDataFromNetwork() throws Exception {
        return getService().userStreams();
    }

}
