package za.co.jcarklin.popularmovies.repository.api;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import za.co.jcarklin.popularmovies.repository.model.MovieDetails;
import za.co.jcarklin.popularmovies.repository.model.MovieListing;

public class JsonUtils {

    private static final JsonUtils instance = new JsonUtils();

    private JsonUtils(){}

    public static JsonUtils getInstance() {
        return instance;
    }

    public List<MovieListing> processMovieListingResults(String json) {
        List<MovieListing> movies = null;
        try {
            JSONObject root = new JSONObject(json);
            JSONArray results = root.optJSONArray("results");

            if (results != null) {
                JSONObject aResult;
                MovieListing movie;
                movies = new ArrayList<>(results.length());
                for (int i = 0; i<results.length();i++) {
                    aResult = results.getJSONObject(i);
                    movie = new MovieListing();
                    movie.setId(aResult.optInt("id",0));
                    movie.setPosterPath(aResult.optString("poster_path",""));
                    movies.add(movie);
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return movies;
    }

    public MovieDetails processMovieDetails(String jsonResponse) {
        MovieDetails movie = new MovieDetails();
        try {
            JSONArray jsonArray;
            JSONObject jsonObject;
            JSONObject root = new JSONObject(jsonResponse);
            movie.setId(root.optInt("id",0));
            movie.setBackdropPath(root.optString("backdrop_path",""));
            try {
                jsonArray = root.getJSONArray("genres");
                for (int i=0; i<jsonArray.length();i++) {
                    jsonObject = jsonArray.getJSONObject(i);
                    movie.addGenre(jsonObject.optString("name"));
                }
            } catch (JSONException j) {
                //skip it
                j.printStackTrace();
            }
            movie.setHomepage(root.optString("homepage",""));
            movie.setOriginalTitle(root.optString("original_title",""));
            movie.setOriginalLanguage(root.optString("original_language",""));
            movie.setPopularity(root.optDouble("popularity",0d));
            movie.setOverview(root.optString("overview",""));
            movie.setPosterPath(root.optString("poster_path",""));
            movie.setReleaseDate(root.optString("release_date",""));
            movie.setRuntime(root.optInt("runtime",0));
            try {
                jsonArray = root.getJSONArray("spoken_languages");
                for (int i=0; i<jsonArray.length();i++) {
                    jsonObject = jsonArray.getJSONObject(i);
                    movie.addSpokenLanguage(jsonObject.optString("name"));
                }
            } catch (JSONException j) {
                //skip it
                j.printStackTrace();
            }
            movie.setStatus(root.optString("status",""));
            movie.setTagline(root.optString("tagline",""));
            movie.setTitle(root.optString("title",""));
            movie.setVoteAverage(root.optDouble("vote_average",0d));
            movie.setVoteCount(root.optInt("vote_count",0));
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return movie;
    }
}
