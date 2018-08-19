package za.co.jcarklin.popularmovies.api.data;

import com.squareup.moshi.Json;

import java.io.Serializable;

public class Genre implements Serializable
{

    @Json(name = "id")
    private Integer id;
    @Json(name = "name")
    private String name;
    private final static long serialVersionUID = 4815647989181225928L;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Genre withId(Integer id) {
        this.id = id;
        return this;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Genre withName(String name) {
        this.name = name;
        return this;
    }

}
