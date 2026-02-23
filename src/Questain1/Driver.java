package Questain1;

import java.util.List;

public class Driver {

	public static void main(String[] args) {

		ProductDAO dao = new ProductDAO();

		Product p1 = new Product();
		p1.setName("Laptop");
		p1.setPrice(50000);
		p1.setStock(10);
		dao.addProduct(p1);

		Product p2 = new Product();
		p2.setName("Mobile");
		p2.setPrice(20000);
		p2.setStock(25);
		dao.addProduct(p2);

		System.out.println("Products Added Successfully\n");

		System.out.println("All Products:");
		dao.fetchAll();

		System.out.println("\nFetch By ID (1):");
		Product product = dao.fetchById(1);
		System.out.println(product);

		System.out.println("\nFetch By Name (Mobile):");
		List<Product> list = dao.fetchByName("Mobile");
		list.forEach(System.out::println);

		dao.updateProduct(1, 55000);
		System.out.println("\nProduct Updated Successfully");
		System.out.println(dao.fetchById(1));

		dao.deleteProduct(2);
		System.out.println("\nProduct Deleted Successfully");

		System.out.println("\nFinal Product List:");
		dao.fetchAll();

		HibernateUtil.getSessionFactory().close();
	}
}