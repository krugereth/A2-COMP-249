public class TooManyFieldsException extends Exception {
    public String record;

    public TooManyFieldsException(String record) {
        this.record = record;
    }

    public String getRecord() {
        return "Error: Too many fields in record " + record;
    }
}
