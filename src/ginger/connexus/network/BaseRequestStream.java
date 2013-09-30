package ginger.connexus.network;

import ginger.connexus.model.ImageWrapper;

import com.octo.android.robospice.request.retrofit.RetrofitSpiceRequest;

public abstract class BaseRequestStream extends RetrofitSpiceRequest<ImageWrapper.List, ConnexusApi> {

    public BaseRequestStream() {
        super(ImageWrapper.List.class, ConnexusApi.class);
    }

}
