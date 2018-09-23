package za.co.jcarklin.popularmovies.api;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import za.co.jcarklin.popularmovies.model.data.MovieListing;

public class JsonUtils {

    private static final JsonUtils instance = new JsonUtils();

    private JsonUtils(){}
/*
"vote_count": 7094,
      "id": 299536,
      "video": false,
      "vote_average": 8.3,
      "title": "Avengers: Infinity War",
      "popularity": 259.215,
      "poster_path": "/7WsyChQLEftFiDOVTGkv3hFpyyt.jpg",
      "original_language": "en",
      "original_title": "Avengers: Infinity War",
      "genre_ids": [
        12,
        878,
        14,
        28
      ],
      "backdrop_path": "/bOGkgRGdhrBYJSLpXaxhXVstddV.jpg",
      "adult": false,
      "overview": "As the Avengers and their allies have continued to protect the world from threats too large for any one hero to handle, a new danger has emerged from the cosmic shadows: Thanos. A despot of intergalactic infamy, his goal is to collect all six Infinity Stones, artifacts of unimaginable power, and use them to inflict his twisted will on all of reality. Everything the Avengers have fought for has led up to this moment - the fate of Earth and existence itself has never been more uncertain.",
      "release_date": "2018-04-25"
 */
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
//                    movie.setTitle(aResult.optString("title",""));
//                    movie.setReleaseDate(aResult.optString("release_date",""));
//                    movie.setVoteAverage(BigDecimal.valueOf(aResult.optDouble("vote_average",0d)).floatValue());
//                    movie.setVoteCount(aResult.optInt("vote_count",0));
//                    movie.setPopularity(aResult.optDouble("popularity",0d));
//                    movie.setOriginalTitle(aResult.optString("original_title",""));
//                    movie.setOriginalLanguage(aResult.optString("original_language",""));
//                    movie.setOverview(aResult.optString("overview",""));
                    movies.add(movie);
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return movies;
    }

    public static JsonUtils getInstance() {
        return instance;
    }

    public MovieListing processMovieDetails(String jsonResponse) {
        return null;
    }
}
