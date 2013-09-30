package ginger.connexus.network;

import ginger.connexus.model.ImageWrapper;

public class RequestAllStreams extends BaseRequestStream {

    @Override
    public ImageWrapper.List loadDataFromNetwork() throws Exception {
        return getService().allStreams();
    }

}
