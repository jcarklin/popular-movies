package za.co.jcarklin.popularmovies.api.data;

import com.squareup.moshi.Json;

import java.io.Serializable;
import java.util.List;

public class MovieResults implements Serializable
{

    @Json(name = "page")
    private Integer page;
    @Json(name = "total_results")
    private Integer totalResults;
    @Json(name = "total_pages")
    private Integer totalPages;
    @Json(name = "results")
    private List<Result> results = null;
    private final static long serialVersionUID = 9196731684141462407L;

    public Integer getPage() {
        return page;
    }

    public void setPage(Integer page) {
        this.page = page;
    }

    public MovieResults withPage(Integer page) {
        this.page = page;
        return this;
    }

    public Integer getTotalResults() {
        return totalResults;
    }

    public void setTotalResults(Integer totalResults) {
        this.totalResults = totalResults;
    }

    public MovieResults withTotalResults(Integer totalResults) {
        this.totalResults = totalResults;
        return this;
    }

    public Integer getTotalPages() {
        return totalPages;
    }

    public void setTotalPages(Integer totalPages) {
        this.totalPages = totalPages;
    }

    public MovieResults withTotalPages(Integer totalPages) {
        this.totalPages = totalPages;
        return this;
    }

    public List<Result> getResults() {
        return results;
    }

    public void setResults(List<Result> results) {
        this.results = results;
    }

    public MovieResults withResults(List<Result> results) {
        this.results = results;
        return this;
    }

}

