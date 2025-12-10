package hei.school;

import java.time.Instant;

public class Main {
    public static void main(String[] args) {
        DataRetriever dr = new DataRetriever();

        System.out.println("=== 7.a getAllCategories ===");
        dr.getAllCategories().forEach(System.out::println);

        System.out.println("\n=== 7.b getProductList ===");
        int[][] pages = {{1,10}, {1,5}, {1,3}, {2,2}};
        for (int[] p : pages) {
            System.out.println("\nPage " + p[0] + " | Size " + p[1]);
            dr.getProductList(p[0], p[1]).forEach(System.out::println);
        }

        System.out.println("\n=== 7.c getProductsByCriteria (sans pagination ===");

        dr.getProductsByCriteria("Dell", null, null, null).forEach(System.out::println);
        dr.getProductsByCriteria(null, "info", null, null).forEach(System.out::println);

        System.out.println("\n=== 7.d getProductsByCriteria avec pagination ===");
        dr.getProductsByCriteria(null, null, null, null, 1, 10).forEach(System.out::println);
        dr.getProductsByCriteria("Dell", null, null, null, 1, 5).forEach(System.out::println);
        dr.getProductsByCriteria(null, "informatique", null, null, 1, 10).forEach(System.out::println);
    }
}