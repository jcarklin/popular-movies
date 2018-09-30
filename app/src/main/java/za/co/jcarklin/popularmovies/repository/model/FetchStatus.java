package za.co.jcarklin.popularmovies.repository.model;

public class FetchStatus {

    private final int movieStatus;
    private final Integer statusMessageRvalue;

    public FetchStatus(int status, Integer messageRvalue) {
        movieStatus = status;
        statusMessageRvalue = messageRvalue;
    }

    public int getMovieStatus() {
        return movieStatus;
    }

    public Integer getStatusMessage() {
        return statusMessageRvalue;
    }
}
