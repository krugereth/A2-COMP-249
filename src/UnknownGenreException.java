public class UnknownGenreException extends Exception {
    private final String genre;


    public UnknownGenreException(String genre) {
        this.genre = genre;
    }

    public String getMessage() {
        return "Error: Unknown genre \"" + genre;
    }
}