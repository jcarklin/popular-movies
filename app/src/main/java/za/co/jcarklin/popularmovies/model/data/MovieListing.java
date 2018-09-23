package za.co.jcarklin.popularmovies.model.data;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.Ignore;
import android.arch.persistence.room.PrimaryKey;
import android.os.Parcel;
import android.os.Parcelable;

import java.util.List;
import java.util.Locale;

@Entity(tableName = "fav_movies")
public class MovieListing implements Parcelable {

    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "fav_movie_id")
    private Integer faveMovieId;
    @ColumnInfo(name = "themoviedb_id")
    private Integer id;
    @ColumnInfo(name = "poster_path")
    private String posterPath;

    @Ignore
    private Integer voteCount;
    @Ignore
    private Boolean video = false;
    @Ignore
    private Float voteAverage;
    @Ignore
    private String title;
    @Ignore
    private Double popularity;
    @Ignore
    private String originalLanguage;
    @Ignore
    private String originalTitle;
    @Ignore
    private List<Integer> genreIds = null;
    @Ignore
    private String backdropPath;
    @Ignore
    private Boolean adult = false;
    @Ignore
    private String overview;
    @Ignore
    private String releaseDate;

    @Ignore
    public MovieListing() {

    }

    public MovieListing(int faveMovieId, int id, String posterPath) {
        this.faveMovieId = faveMovieId;
        this.id = id;
        this.posterPath = posterPath;
    }

    private MovieListing(Parcel in) {
        int fvm = (Integer) in.readValue(Integer.class.getClassLoader());
        this.faveMovieId = (fvm<0?null:fvm);
        this.id = in.readInt();
        this.posterPath = in.readString();
        this.voteCount = in.readInt();
        this.video = (in.readInt()==1);
        this.voteAverage = in.readFloat();
        this.title = in.readString();
        this.popularity = in.readDouble();
        this.originalLanguage = in.readString();
        this.originalTitle = in.readString();
        this.backdropPath = in.readString();
        this.adult = (in.readInt()==1);
        this.overview = in.readString();
        this.releaseDate = in.readString();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeValue(faveMovieId==null?-1:faveMovieId);
        dest.writeInt(id);
        dest.writeString(posterPath);
        dest.writeInt(voteCount);
        dest.writeInt(video?1:0);
        dest.writeFloat(voteAverage);
        dest.writeString(title);
        dest.writeDouble(popularity);
        dest.writeString(originalLanguage);
        dest.writeString(originalTitle);
        dest.writeString(backdropPath);
        dest.writeInt(adult?1:0);
        dest.writeString(overview);
        dest.writeString(releaseDate);
    }

    public Integer getFaveMovieId() {
        return faveMovieId;
    }

    public void setFaveMovieId(Integer faveMovieId) {
        this.faveMovieId = faveMovieId;
    }

    public Integer getVoteCount() {
        return voteCount;
    }

    public void setVoteCount(int voteCount) {
        this.voteCount = voteCount;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Boolean getVideo() {
        return video;
    }

    public void setVideo(Boolean video) {
        this.video = video;
    }

    public Float getVoteAverage() {
        return voteAverage;
    }

    public void setVoteAverage(Float voteAverage) {
        this.voteAverage = voteAverage;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Double getPopularity() {
        return popularity;
    }

    public void setPopularity(Double popularity) {
        this.popularity = popularity;
    }

    public String getPosterPath() {
        return posterPath;
    }

    public void setPosterPath(String posterPath) {
        this.posterPath = posterPath;
    }

    public String getOriginalLanguage() {
        return originalLanguage;
    }

    public void setOriginalLanguage(String originalLanguage) {
        this.originalLanguage = originalLanguage;
    }

    public String getOriginalLanguageName() {
        if (!originalLanguage.isEmpty()) {
            Locale loc = new Locale(getOriginalLanguage());
            return loc.getDisplayName();
        }
        return "";
    }

    public String getOriginalTitle() {
        return originalTitle;
    }

    public void setOriginalTitle(String originalTitle) {
        this.originalTitle = originalTitle;
    }

    public List<Integer> getGenreIds() {
        return genreIds;
    }

    public void setGenreIds(List<Integer> genreIds) {
        this.genreIds = genreIds;
    }

    public String getBackdropPath() {
        return backdropPath;
    }

    public void setBackdropPath(String backdropPath) {
        this.backdropPath = backdropPath;
    }

    public Boolean getAdult() {
        return adult;
    }

    public void setAdult(Boolean adult) {
        this.adult = adult;
    }

    public String getOverview() {
        return overview;
    }

    public void setOverview(String overview) {
        this.overview = overview;
    }

    public String getReleaseDate() {
        return releaseDate;
    }

    public void setReleaseDate(String releaseDate) {
        this.releaseDate = releaseDate;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    public static final Parcelable.Creator<MovieListing> CREATOR = new Parcelable.Creator<MovieListing>() {

        public MovieListing createFromParcel(Parcel in) {
            return new MovieListing(in);
        }

        public MovieListing[] newArray(int size) {
            return new MovieListing[size];
        }
    };


}
