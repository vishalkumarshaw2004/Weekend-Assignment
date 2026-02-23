package Questain3;

import java.sql.*;
import java.util.Scanner;

public class OrderPlacement {

    public static void main(String[] args) {

        Scanner sc = new Scanner(System.in);

        System.out.print("Enter Product Id: ");
        int prodId = sc.nextInt();

        System.out.print("Enter Quantity: ");
        int quantity = sc.nextInt();

        String url = "jdbc:postgresql://localhost:5432/demo";
        String user = "postgres";
        String pass = "1234";

        Connection con = null;

        try {
            con = DriverManager.getConnection(url, user, pass);
            con.setAutoCommit(false);   // start transaction

           
            String checkProduct = "SELECT stock FROM product WHERE prod_id = ?";
            PreparedStatement ps1 = con.prepareStatement(checkProduct);
            ps1.setInt(1, prodId);

            ResultSet rs1 = ps1.executeQuery();

            if (!rs1.next()) {
                System.out.println("Product not found.");
                con.rollback();
                return;
            }

            int stock = rs1.getInt("stock");

           
            String checkDuplicate = 
                "SELECT COUNT(*) FROM orders " +
                "WHERE prod_id = ? " +
                "AND order_time >= NOW() - INTERVAL '5 minutes'";

            PreparedStatement ps2 = con.prepareStatement(checkDuplicate);
            ps2.setInt(1, prodId);

            ResultSet rs2 = ps2.executeQuery();
            rs2.next();

            if (rs2.getInt(1) > 0) {
                System.out.println("Duplicate order within 5 minutes. Order rejected.");
                con.rollback();
                return;
            }

           
            if (stock < quantity) {
                System.out.println("Insufficient stock.");
                con.rollback();
                return;
            }

          
            String updateStock = 
                "UPDATE product SET stock = stock - ? WHERE prod_id = ?";

            PreparedStatement ps3 = con.prepareStatement(updateStock);
            ps3.setInt(1, quantity);
            ps3.setInt(2, prodId);
            ps3.executeUpdate();

           
            String insertOrder = 
                "INSERT INTO orders (prod_id, quantity) VALUES (?, ?)";

            PreparedStatement ps4 = con.prepareStatement(insertOrder);
            ps4.setInt(1, prodId);
            ps4.setInt(2, quantity);
            ps4.executeUpdate();

            con.commit();  
            System.out.println("Order placed successfully.");

        } catch (Exception e) {
            try {
                if (con != null) {
                    con.rollback();
                }
            } catch (Exception ex) {
                ex.printStackTrace();
            }
            e.printStackTrace();
        } finally {
            try {
                if (con != null) {
                    con.close();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}