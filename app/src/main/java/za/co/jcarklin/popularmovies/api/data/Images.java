package za.co.jcarklin.popularmovies.api.data;

import com.squareup.moshi.Json;

import java.io.Serializable;
import java.util.List;

public class Images implements Serializable
{

    @Json(name = "base_url")
    private String baseUrl;
    @Json(name = "secure_base_url")
    private String secureBaseUrl;
    @Json(name = "backdrop_sizes")
    private List<String> backdropSizes = null;
    @Json(name = "logo_sizes")
    private List<String> logoSizes = null;
    @Json(name = "poster_sizes")
    private List<String> posterSizes = null;
    @Json(name = "profile_sizes")
    private List<String> profileSizes = null;
    @Json(name = "still_sizes")
    private List<String> stillSizes = null;
    private final static long serialVersionUID = 5000039423519031235L;

    public String getBaseUrl() {
        return baseUrl;
    }

    public void setBaseUrl(String baseUrl) {
        this.baseUrl = baseUrl;
    }

    public Images withBaseUrl(String baseUrl) {
        this.baseUrl = baseUrl;
        return this;
    }

    public String getSecureBaseUrl() {
        return secureBaseUrl;
    }

    public void setSecureBaseUrl(String secureBaseUrl) {
        this.secureBaseUrl = secureBaseUrl;
    }

    public Images withSecureBaseUrl(String secureBaseUrl) {
        this.secureBaseUrl = secureBaseUrl;
        return this;
    }

    public List<String> getBackdropSizes() {
        return backdropSizes;
    }

    public void setBackdropSizes(List<String> backdropSizes) {
        this.backdropSizes = backdropSizes;
    }

    public Images withBackdropSizes(List<String> backdropSizes) {
        this.backdropSizes = backdropSizes;
        return this;
    }

    public List<String> getLogoSizes() {
        return logoSizes;
    }

    public void setLogoSizes(List<String> logoSizes) {
        this.logoSizes = logoSizes;
    }

    public Images withLogoSizes(List<String> logoSizes) {
        this.logoSizes = logoSizes;
        return this;
    }

    public List<String> getPosterSizes() {
        return posterSizes;
    }

    public void setPosterSizes(List<String> posterSizes) {
        this.posterSizes = posterSizes;
    }

    public Images withPosterSizes(List<String> posterSizes) {
        this.posterSizes = posterSizes;
        return this;
    }

    public List<String> getProfileSizes() {
        return profileSizes;
    }

    public void setProfileSizes(List<String> profileSizes) {
        this.profileSizes = profileSizes;
    }

    public Images withProfileSizes(List<String> profileSizes) {
        this.profileSizes = profileSizes;
        return this;
    }

    public List<String> getStillSizes() {
        return stillSizes;
    }

    public void setStillSizes(List<String> stillSizes) {
        this.stillSizes = stillSizes;
    }

    public Images withStillSizes(List<String> stillSizes) {
        this.stillSizes = stillSizes;
        return this;
    }

}
