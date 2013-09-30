package ginger.connexus.model;

import java.util.ArrayList;

public class ImageWrapper {

    public ConnexusImage data;

    // TODO Temporary
    public ImageWrapper() {
        data = new ConnexusImage();
    }

    public String getUrl() {
        return data.link;
    }

    @SuppressWarnings("serial")
    public static class List extends ArrayList<ImageWrapper> {
    }
}
