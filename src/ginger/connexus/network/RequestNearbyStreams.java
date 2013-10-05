package ginger.connexus.network;

import ginger.connexus.model.ConnexusStream;

import com.octo.android.robospice.request.retrofit.RetrofitSpiceRequest;

public class RequestNearbyStreams extends RetrofitSpiceRequest<ConnexusStream.List, ConnexusApi> {

    private final double latitude;
    private final double longitude;

    public RequestNearbyStreams(double lat, double lon) {
        super(ConnexusStream.List.class, ConnexusApi.class);
        this.latitude = lat;
        this.longitude = lon;
    }

    @Override
    public ConnexusStream.List loadDataFromNetwork() throws Exception {
        return getService().nearbyStreams(latitude, longitude);
    }

}
