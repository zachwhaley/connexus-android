package ginger.connexus.network;

import ginger.connexus.model.ImageWrapper;

public class RequestNearbyStreams extends BaseRequestStream {

    @Override
    public ImageWrapper.List loadDataFromNetwork() throws Exception {
        return getService().nearbyStreams();
    }

}
