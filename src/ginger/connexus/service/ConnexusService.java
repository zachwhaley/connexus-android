package ginger.connexus.service;

import ginger.connexus.network.ConnexusApi;

import com.octo.android.robospice.retrofit.RetrofitGsonSpiceService;

public class ConnexusService extends RetrofitGsonSpiceService {

    private final static String BASE_URL = "http://connexus-api.appspot.com";

    @Override
    protected String getServerUrl() {
        return BASE_URL;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        addRetrofitInterface(ConnexusApi.class);
    }

}
