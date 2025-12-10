package hei.school;

import java.sql.*;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

public class DataRetriever {

    // 1. Toutes les catégories
    public List<Category> getAllCategories() {
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
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return categories;
    }

//    2. Liste paginée simple (sans filtre)
    public List<Product> getProductList(int page, int size) {
        return getProductsByCriteria(null, null, null, null, page, size);
    }

//    3. Recherche avec filtres SANS pagination
    public List<Product> getProductsByCriteria(String productName,
                                               String categoryName,
                                               Instant creationMin,
                                               Instant creationMax) {
        return getProductsByCriteria(productName, categoryName, creationMin, creationMax, 0, 0);
    }

//    4. Méthode principale : filtres + pagination (la plus importante)
    public List<Product> getProductsByCriteria(String productName,
                                               String categoryName,
                                               Instant creationMin,
                                               Instant creationMax,
                                               int page,
                                               int size) {

        List<Product> products = new ArrayList<>();
        List<Object> parameters = new ArrayList<>();

        StringBuilder sql = new StringBuilder();
        sql.append("SELECT p.id, p.name, p.price, p.creation_datetime, ")
                .append("   string_agg(pc.name, ', ') AS category_names ")
                .append("FROM product p ")
                .append("LEFT JOIN product_category pc ON p.id = pc.product_id ");

        StringBuilder where = new StringBuilder();
        boolean firstCondition = true;

        if (productName != null && !productName.trim().isEmpty()) {
            where.append(firstCondition ? "WHERE " : "AND ").append("p.name ILIKE ? ");
            parameters.add("%" + productName.trim() + "%");
            firstCondition = false;
        }

        if (categoryName != null && !categoryName.trim().isEmpty()) {
            where.append(firstCondition ? "WHERE " : "AND ").append("pc.name ILIKE ? ");
            parameters.add("%" + categoryName.trim() + "%");
            firstCondition = false;
        }

        if (creationMin != null) {
            where.append(firstCondition ? "WHERE " : "AND ").append("p.creation_datetime >= ? ");
            parameters.add(Timestamp.from(creationMin));
            firstCondition = false;
        }

        if (creationMax != null) {
            where.append(firstCondition ? "WHERE " : "AND ").append("p.creation_datetime <= ? ");
            parameters.add(Timestamp.from(creationMax));
            firstCondition = false;
        }

        sql.append(where);
        sql.append(" GROUP BY p.id, p.name, p.price, p.creation_datetime ");
        sql.append(" ORDER BY p.id ASC");

        if (page > 0 && size > 0) {
            sql.append(" LIMIT ? OFFSET ?");
            parameters.add(size);
            parameters.add((page - 1) * size);
        }

        try (Connection conn = DBConnection.getDBConnection();
             PreparedStatement ps = conn.prepareStatement(sql.toString())) {

            for (int i = 0; i < parameters.size(); i++) {
                ps.setObject(i + 1, parameters.get(i));
            }

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    Product p = new Product();
                    p.setId(rs.getInt("id"));
                    p.setName(rs.getString("name"));
                    p.setPrice(rs.getDouble("price"));
                    p.setCreationDateTime(rs.getTimestamp("creation_datetime").toInstant());

                    String catString = rs.getString("category_names");
                    if (catString != null) {
                        p.setCategoryNames(catString.split(", "));
                    } else {
                        p.setCategoryNames(new String[0]);
                    }
                    products.add(p);
                }
            }
        } catch (SQLException e) {
            System.err.println("Erreur SQL : " + e.getMessage());
            e.printStackTrace();
        }

        return products;
    }
}