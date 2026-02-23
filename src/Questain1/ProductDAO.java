package Questain1;

import org.hibernate.Session;
import org.hibernate.Transaction;
import java.util.List;

public class ProductDAO {

	public void addProduct(Product p) {
		Session session = HibernateUtil.getSessionFactory().openSession();
		Transaction tx = session.beginTransaction();
		session.persist(p);
		tx.commit();
		session.close();
	}

	public void fetchAll() {
		Session session = HibernateUtil.getSessionFactory().openSession();
		List<Product> list = session.createQuery("from Product", Product.class).list();
		list.forEach(System.out::println);
		session.close();
	}

	public Product fetchById(int id) {
		Session session = HibernateUtil.getSessionFactory().openSession();
		Product p = session.get(Product.class, id);
		session.close();
		return p;
	}

	public List<Product> fetchByName(String name) {
		Session session = HibernateUtil.getSessionFactory().openSession();
		List<Product> list = session.createQuery("from Product where name = :name", Product.class)
				.setParameter("name", name).list();
		session.close();
		return list;
	}

	public void updateProduct(int id, double newPrice) {
		Session session = HibernateUtil.getSessionFactory().openSession();
		Transaction tx = session.beginTransaction();
		Product p = session.get(Product.class, id);
		if (p != null) {
			p.setPrice(newPrice);
		}
		tx.commit();
		session.close();
	}

	public void deleteProduct(int id) {
		Session session = HibernateUtil.getSessionFactory().openSession();
		Transaction tx = session.beginTransaction();
		Product p = session.get(Product.class, id);
		if (p != null) {
			session.remove(p);
		}
		tx.commit();
		session.close();
	}
}