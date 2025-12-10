package hei.school;

import java.time.Instant;

public class product {
    private int id;
    private String name;
    private double price;
    private Instant creationDateTime;
    private String categoryName;  // exactement comme dans le diagramme

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public double getPrice() { return price; }
    public void setPrice(double price) { this.price = price; }
    public Instant getCreationDateTime() { return creationDateTime; }
    public void setCreationDateTime(Instant creationDateTime) { this.creationDateTime = creationDateTime; }

    public String getCategoryName() {
        return categoryName != null ? categoryName : "";
    }
    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }

    @Override
    public String toString() {
        return String.format("Product{id=%d, name='%s', price=%.2f, creation=%s, category='%s'}",
                id, name, price, creationDateTime, getCategoryName());
    }
}
