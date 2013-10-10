package ginger.connexus.network;

import com.octo.android.robospice.request.retrofit.RetrofitSpiceRequest;

public class UploadImage extends RetrofitSpiceRequest<String, ConnexusApi> {

    private final String uploadurl;
    private final float latitude;
    private final float longitude;
    private final long streamId;
    private final String imageUri;

    public UploadImage(String uploadurl, float latitude, float longitude, long streamId, String imageUri) {
        super(String.class, ConnexusApi.class);
        this.uploadurl = uploadurl;
        this.latitude = latitude;
        this.longitude = longitude;
        this.streamId = streamId;
        this.imageUri = imageUri;
    }

    @Override
    public String loadDataFromNetwork() throws Exception {
        return getService().uploadImage(uploadurl, latitude, longitude, streamId, imageUri);
    }

}
