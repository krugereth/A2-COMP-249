public class MissingFieldException extends Exception {
    public String record;

    public MissingFieldException(String record) {
        this.record = record;
    }

    public String getMessage() {
        return "Error: Missing field in record: \"" + record;
    }
}
