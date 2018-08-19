package za.co.jcarklin.popularmovies.api.data;

import com.squareup.moshi.Json;

import java.io.Serializable;
import java.util.List;

public class Configuration implements Serializable
{

    @Json(name = "images")
    private Images images;
    @Json(name = "change_keys")
    private List<String> changeKeys = null;
    private final static long serialVersionUID = 8527466831447747436L;

    public Images getImages() {
        return images;
    }

    public void setImages(Images images) {
        this.images = images;
    }

    public Configuration withImages(Images images) {
        this.images = images;
        return this;
    }

    public List<String> getChangeKeys() {
        return changeKeys;
    }

    public void setChangeKeys(List<String> changeKeys) {
        this.changeKeys = changeKeys;
    }

    public Configuration withChangeKeys(List<String> changeKeys) {
        this.changeKeys = changeKeys;
        return this;
    }

}
