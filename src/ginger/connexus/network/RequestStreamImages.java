package ginger.connexus.network;

import ginger.connexus.model.ConnexusImage;

import com.octo.android.robospice.request.retrofit.RetrofitSpiceRequest;

public class RequestStreamImages extends RetrofitSpiceRequest<ConnexusImage.List, ConnexusApi> {

    private final long streamId;

    public RequestStreamImages(long streamId) {
        super(ConnexusImage.List.class, ConnexusApi.class);
        this.streamId = streamId;
    }

    @Override
    public ConnexusImage.List loadDataFromNetwork() throws Exception {
        return getService().streamImages(streamId);
    }

}
