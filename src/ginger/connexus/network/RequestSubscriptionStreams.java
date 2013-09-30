package ginger.connexus.network;

import ginger.connexus.model.ImageWrapper;

public class RequestSubscriptionStreams extends BaseRequestStream {

    public RequestSubscriptionStreams(String user) {
        // TODO Auto-generated constructor stub
    }

    @Override
    public ImageWrapper.List loadDataFromNetwork() throws Exception {
        return getService().subscriptionStreams();
    }

}
