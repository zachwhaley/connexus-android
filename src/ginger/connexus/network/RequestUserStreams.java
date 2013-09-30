package ginger.connexus.network;

import ginger.connexus.model.ImageWrapper;

public class RequestUserStreams extends BaseRequestStream {

    public RequestUserStreams(String user) {
        // TODO Auto-generated constructor stub
    }

    @Override
    public ImageWrapper.List loadDataFromNetwork() throws Exception {
        return getService().userStreams();
    }

}
