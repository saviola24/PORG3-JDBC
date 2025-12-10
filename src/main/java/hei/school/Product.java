package hei.school;

import java.time.Instant;
import java.util.Arrays;

public class Product {
    private int id;
    private String name;
    private double price;
    private Instant creationDateTime;
    private String[] categoryNames;


    public int getId() { return id; }
    public void setId(int id) { this.id = id; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public double getPrice() { return price; }
    public void setPrice(double price) { this.price = price; }
    public Instant getCreationDateTime() { return creationDateTime; }
    public void setCreationDateTime(Instant d) { this.creationDateTime = d; }
    public String[] getCategoryNames() { return categoryNames; }
    public void setCategoryNames(String[] a) { this.categoryNames = a; }

    @Override
    public String toString() {
        return "Product{id=" + id + ", name='" + name + "', price=" + price +
                ", creation=" + creationDateTime + ", categories=" + Arrays.toString(categoryNames) + "}";
    }
}
