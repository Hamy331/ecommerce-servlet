package murach.business;

public class ProductImage {

    private byte[] data;
    private String contentType;

    public ProductImage(byte[] data, String contentType) {
        this.data = data;
        this.contentType = contentType;
    }

    public byte[] getData() {
        return data;
    }

    public String getContentType() {
        return contentType;
    }
}
