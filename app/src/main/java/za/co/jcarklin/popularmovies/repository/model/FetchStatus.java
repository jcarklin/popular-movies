package za.co.jcarklin.popularmovies.repository.model;

public class FetchStatus {

    private int movieStatus;
    private Integer statusMessageRvalue;

    public FetchStatus(int status, Integer messageRvalue) {
        movieStatus = status;
        statusMessageRvalue = messageRvalue;
    }

    public int getMovieStatus() {
        return movieStatus;
    }

    public void setMovieStatus(int movieStatus) {
        this.movieStatus = movieStatus;
    }

    public Integer getStatusMessage() {
        return statusMessageRvalue;
    }

    public void setStatusMessage(Integer statusMessage) {
        this.statusMessageRvalue = statusMessage;
    }
}
