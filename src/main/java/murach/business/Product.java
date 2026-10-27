package murach.business;

import java.io.Serializable;
import java.text.NumberFormat;
import java.util.Locale;

public class Product implements Serializable {

    private String code;
    private String description;
    private double price;
    private String details;
    private boolean hasImage;
    private long imageVersion;

    public Product() {
        code = "";
        description = "";
        price = 0;
        details = "";
        hasImage = false;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getCode() {
        return code;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public double getPrice() {
        return price;
    }

    public void setDetails(String details) {
        this.details = details;
    }

    public String getDetails() {
        return details;
    }

    public void setHasImage(boolean hasImage) {
        this.hasImage = hasImage;
    }

    public boolean isHasImage() {
        return hasImage;
    }

    // đổi mỗi khi ảnh đổi, gắn vào URL để trình duyệt không dùng ảnh cũ
    public void setImageVersion(long imageVersion) {
        this.imageVersion = imageVersion;
    }

    public long getImageVersion() {
        return imageVersion;
    }

    public String getPriceCurrencyFormat() {
        NumberFormat currency = NumberFormat.getCurrencyInstance(
                new Locale("vi", "VN"));
        return currency.format(price);
    }
}
