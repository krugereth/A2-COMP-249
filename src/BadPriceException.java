public class BadPriceException extends Exception {
    String price;
    public BadPriceException(String price) {
        this.price = price;
    }
    public String getPrice() {
        return "Error invalid price" +price;
    }
}
