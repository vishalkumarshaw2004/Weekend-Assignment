package Questain2;

import java.sql.*;

public class OrderPlacement {

	public static void main(String[] args) {

		int prodId = 1;
		int quantity = 2;

		String url = "jdbc:postgresql://localhost:5432/training";
		String user = "postgres";
		String pass = "1234";

		try (Connection con = DriverManager.getConnection(url, user, pass)) {

			con.setAutoCommit(false);

			String checkSql = "SELECT stock FROM product WHERE prod_id = ?";
			PreparedStatement psCheck = con.prepareStatement(checkSql);
			psCheck.setInt(1, prodId);

			ResultSet rs = psCheck.executeQuery();

			if (!rs.next()) {
				System.out.println("Product does not exist.");
				con.rollback();
				return;
			}

			int currentStock = rs.getInt("stock");

			if (currentStock < quantity) {
				System.out.println("Insufficient stock.");
				con.rollback();
				return;
			}

			String updateStock = "UPDATE product SET stock = stock - ? WHERE prod_id = ?";
			PreparedStatement psUpdate = con.prepareStatement(updateStock);
			psUpdate.setInt(1, quantity);
			psUpdate.setInt(2, prodId);
			psUpdate.executeUpdate();

			String insertOrder = "INSERT INTO orders(prod_id, quantity) VALUES(?, ?)";
			PreparedStatement psInsert = con.prepareStatement(insertOrder);
			psInsert.setInt(1, prodId);
			psInsert.setInt(2, quantity);
			psInsert.executeUpdate();

			con.commit();
			System.out.println("Order placed successfully!");

		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}