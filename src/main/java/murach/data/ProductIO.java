package murach.data;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;

import murach.business.Product;

public class ProductIO {

    public static List<Product> getProducts(String path) {
        List<Product> products = new ArrayList<>();
        try (BufferedReader in = new BufferedReader(new InputStreamReader(
                new FileInputStream(path), StandardCharsets.UTF_8))) {
            String line = in.readLine();
            while (line != null) {
                if (!line.trim().isEmpty()) {
                    StringTokenizer t = new StringTokenizer(line, "|");
                    Product p = new Product();
                    p.setCode(t.nextToken());
                    p.setDescription(t.nextToken());
                    p.setPrice(Double.parseDouble(t.nextToken()));
                    products.add(p);
                }
                line = in.readLine();
            }
        } catch (IOException e) {
            System.out.println(e);
        }
        return products;
    }

    public static Product getProduct(String code, String path) {
        for (Product p : getProducts(path)) {
            if (p.getCode().equals(code)) {
                return p;
            }
        }
        return null;
    }
}
