package ginger.connexus.model;

import java.util.ArrayList;
import java.util.Date;

public class ConnexusImage {

    long id;
    long streamId;
    String comments;
    String bkUrl;
    Date createDate;
    double latitude;
    double longitude;

    public String getUrl() {
        return bkUrl;
    }

    @SuppressWarnings("serial")
    public static class List extends ArrayList<ConnexusImage> {
    }

}
