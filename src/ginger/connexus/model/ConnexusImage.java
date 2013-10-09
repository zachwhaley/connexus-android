package ginger.connexus.model;

import java.util.ArrayList;

public class ConnexusImage {

    long id;
    String image_url;

    public String getUrl() {
        return image_url;
    }

    @SuppressWarnings("serial")
    public static class List extends ArrayList<ConnexusImage> {
    }

}
