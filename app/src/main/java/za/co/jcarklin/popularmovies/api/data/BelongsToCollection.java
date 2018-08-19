package za.co.jcarklin.popularmovies.api.data;

import com.squareup.moshi.Json;

import java.io.Serializable;

public class BelongsToCollection implements Serializable
{

    @Json(name = "id")
    private Integer id;
    @Json(name = "name")
    private String name;
    @Json(name = "poster_path")
    private String posterPath;
    @Json(name = "backdrop_path")
    private String backdropPath;
    private final static long serialVersionUID = -8860082023204960137L;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public BelongsToCollection withId(Integer id) {
        this.id = id;
        return this;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public BelongsToCollection withName(String name) {
        this.name = name;
        return this;
    }

    public String getPosterPath() {
        return posterPath;
    }

    public void setPosterPath(String posterPath) {
        this.posterPath = posterPath;
    }

    public BelongsToCollection withPosterPath(String posterPath) {
        this.posterPath = posterPath;
        return this;
    }

    public String getBackdropPath() {
        return backdropPath;
    }

    public void setBackdropPath(String backdropPath) {
        this.backdropPath = backdropPath;
    }

    public BelongsToCollection withBackdropPath(String backdropPath) {
        this.backdropPath = backdropPath;
        return this;
    }

}
