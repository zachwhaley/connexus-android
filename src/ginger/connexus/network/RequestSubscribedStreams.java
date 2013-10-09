package ginger.connexus.network;

import ginger.connexus.model.ConnexusStream;

import com.octo.android.robospice.request.retrofit.RetrofitSpiceRequest;

public class RequestSubscribedStreams extends RetrofitSpiceRequest<ConnexusStream.List, ConnexusApi> {

    private final String email;

    public RequestSubscribedStreams(String email) {
        super(ConnexusStream.List.class, ConnexusApi.class);
        this.email = email;
    }

    @Override
    public ConnexusStream.List loadDataFromNetwork() throws Exception {
        return getService().subscribedStreams(email);
    }

}
