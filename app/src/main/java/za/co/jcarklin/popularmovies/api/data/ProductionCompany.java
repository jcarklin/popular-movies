package za.co.jcarklin.popularmovies.api.data;

import com.squareup.moshi.Json;

import java.io.Serializable;

public class ProductionCompany implements Serializable
{

    @Json(name = "id")
    private Integer id;
    @Json(name = "logo_path")
    private String logoPath;
    @Json(name = "name")
    private String name;
    @Json(name = "origin_country")
    private String originCountry;
    private final static long serialVersionUID = 866000291288035014L;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public ProductionCompany withId(Integer id) {
        this.id = id;
        return this;
    }

    public String getLogoPath() {
        return logoPath;
    }

    public void setLogoPath(String logoPath) {
        this.logoPath = logoPath;
    }

    public ProductionCompany withLogoPath(String logoPath) {
        this.logoPath = logoPath;
        return this;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public ProductionCompany withName(String name) {
        this.name = name;
        return this;
    }

    public String getOriginCountry() {
        return originCountry;
    }

    public void setOriginCountry(String originCountry) {
        this.originCountry = originCountry;
    }

    public ProductionCompany withOriginCountry(String originCountry) {
        this.originCountry = originCountry;
        return this;
    }

}
