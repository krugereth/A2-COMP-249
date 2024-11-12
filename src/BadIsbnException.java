public class BadIsbnException extends Exception {
    String ISBN;

    public BadIsbnException(String ISBN) {
        this.ISBN = ISBN;
    }

    public String getISBN() {
        return "Error: Invalid ISBN."+ISBN;
    }
}
