public class BadYearException extends Exception {
    String year;
    public BadYearException(String year) {
        this.year = year;
    }
    public String getYear() {
        return "Error: Invalid year"+year;
    }
}
