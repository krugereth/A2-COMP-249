public class TooFewFieldsException extends Exception {
    public String record;
    public String fileName;

    public TooFewFieldsException(String record) {
        this.record = record;
        this.fileName = fileName;
    }

    public String getMessage(){
        return "Error: Too many fields in record: \"" + record + "\" in file: " + fileName;
    }
}
