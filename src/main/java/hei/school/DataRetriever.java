package hei.school;

import java.sql.ResultSet;

public class DataRetriever {
    public List<Category> getAllCategories() throws SQLException {
        List<Category> categories = new ArrayList<>();
        String sql = "SELECT id, name FROM product_category ORDER BY id";
        try (Connection conn = DBConnection.getDBConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                Category c = new Category();
                c.setId(rs.getInt("id"));
                c.setName(rs.getString("name"));
                categories.add(c);
            }
        }
        return categories;
    }

    public List<Product> getProductList(int page, int size) throws SQLException {
        return getProductsByCriteria(null, null, null, null, page, size);
    }

    public List<Product> getProductsByCriteria(String productName, String categoryName,
                                               Instant creationMin, Instant creationMax) throws SQLException {
        return getProductsByCriteria(productName, categoryName, creationMin, creationMax, 0, 0);
    }

    public List<Product> getProductsByCriteria(String productName, String categoryName,
                                               Instant creationMin, Instant creationMax,
                                               int page, int size) throws SQLException {
        List<Product> products = new ArrayList<>();
        StringBuilder sql = new StringBuilder(
                "SELECT p.id, p.name, p.price, p.creation_datetime, pc.name AS category_name " +
                        "FROM product p " +
                        "LEFT JOIN product_category pc ON p.id = pc.product_id " +
                        "WHERE TRUE"
        );

        List<Object> params = new ArrayList<>();

        if (productName != null && !productName.isBlank()) {
            sql.append(" AND p.name ILIKE ?");
            params.add("%" + productName + "%");
        }
        if (categoryName != null && !categoryName.isBlank()) {
            sql.append(" AND pc.name ILIKE ?");
            params.add("%" + categoryName + "%");
        }
        if (creationMin != null) {
            sql.append(" AND p.creation_datetime >= ?");
            params.add(Timestamp.from(creationMin));
        }
        if (creationMax != null) {
            sql.append(" AND p.creation_datetime <= ?");
            params.add(Timestamp.from(creationMax));
        }

        sql.append(" ORDER BY p.id");

        if (page > 0 && size > 0) {
            sql.append(" LIMIT ? OFFSET ?");
            params.add(size);
            params.add((page - 1) * size);
        }

        try (Connection conn = DBConnection.getDBConnection();
             PreparedStatement ps = conn.prepareStatement(sql.toString())) {

            for (int i = 0; i < params.size(); i++) {
                ps.setObject(i + 1, params.get(i));
            }

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    Product p = new Product();
                    p.setId(rs.getInt("id"));
                    p.setName(rs.getString("name"));
                    p.setPrice(rs.getDouble("price"));
                    p.setCreationDateTime(rs.getTimestamp("creation_datetime").toInstant());
                    p.setCategoryName(rs.getString("category_name"));
                    products.add(p);
                }
            }
        }
        return products;
    }
}
