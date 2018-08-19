package za.co.jcarklin.popularmovies.api.data;

import com.squareup.moshi.Json;

import java.io.Serializable;

public class ProductionCountry implements Serializable
{

    @Json(name = "iso_3166_1")
    private String iso31661;
    @Json(name = "name")
    private String name;
    private final static long serialVersionUID = 9124152323242328589L;

    public String getIso31661() {
        return iso31661;
    }

    public void setIso31661(String iso31661) {
        this.iso31661 = iso31661;
    }

    public ProductionCountry withIso31661(String iso31661) {
        this.iso31661 = iso31661;
        return this;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public ProductionCountry withName(String name) {
        this.name = name;
        return this;
    }

}
