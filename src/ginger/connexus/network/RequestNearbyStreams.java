package ginger.connexus.network;

import ginger.connexus.model.ConnexusStream;

import com.octo.android.robospice.request.retrofit.RetrofitSpiceRequest;

public class RequestNearbyStreams extends RetrofitSpiceRequest<ConnexusStream.List, ConnexusApi> {

    private final float latitude;
    private final float longitude;

    public RequestNearbyStreams(float lat, float lng) {
        super(ConnexusStream.List.class, ConnexusApi.class);
        this.latitude = lat;
        this.longitude = lng;
    }

    @Override
    public ConnexusStream.List loadDataFromNetwork() throws Exception {
        return getService().nearbyStreams(latitude, longitude);
    }

}
