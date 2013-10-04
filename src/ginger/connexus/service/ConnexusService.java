package ginger.connexus.service;

import ginger.connexus.network.ConnexusApi;

import com.octo.android.robospice.retrofit.RetrofitGsonSpiceService;

public class ConnexusService extends RetrofitGsonSpiceService {

	// TODO Get Connexus API url
	private final static String BASE_URL = "https://connexus.com/api";

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
