package za.co.jcarklin.popularmovies.repository.model;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.Ignore;
import android.arch.persistence.room.PrimaryKey;
import android.os.Parcel;
import android.os.Parcelable;

@Entity(tableName = "fav_movies")
public class MovieListing implements Parcelable {

    @PrimaryKey
    private Integer id;
    @ColumnInfo(name = "poster_path")
    private String posterPath;
    private String title;

    @Ignore
    public MovieListing() {

    }

    public MovieListing(int id, String posterPath, String title) {
        this.id = id;
        this.posterPath = posterPath;
        this.title = title;
    }

    private MovieListing(Parcel in) {
        this.id = in.readInt();
        this.posterPath = in.readString();
        this.title = in.readString();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(id);
        dest.writeString(posterPath);
        dest.writeString(title);
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getPosterPath() {
        return posterPath;
    }

    public void setPosterPath(String posterPath) {
        this.posterPath = posterPath;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
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
