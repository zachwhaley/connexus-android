package ginger.connexus.network;

import ginger.connexus.model.ConnexusImage;

import com.octo.android.robospice.request.retrofit.RetrofitSpiceRequest;

public class RequestStreamImages extends RetrofitSpiceRequest<ConnexusImage.List, ConnexusApi> {

    public RequestStreamImages() {
        super(ConnexusImage.List.class, ConnexusApi.class);
    }

    @Override
    public ConnexusImage.List loadDataFromNetwork() throws Exception {
        return getService().streamImages();
    }

}
