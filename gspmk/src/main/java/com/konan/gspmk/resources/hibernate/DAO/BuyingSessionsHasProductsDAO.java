/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.konan.gspmk.resources.hibernate.DAO;

import static com.konan.gspmk.resources.hibernate.DAO.DAO.sessionFactory;
import com.konan.gspmk.resources.javabeans.BuyingSessionsHasProducts;
import com.konan.gspmk.resources.javabeans.Products;
import com.konan.gspmk.resources.javabeans.Users;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.query.Query;

/**
 *
 * @author vdang
 */
public class BuyingSessionsHasProductsDAO {

	public static BuyingSessionsHasProductsDAO getInstance() {
		return new BuyingSessionsHasProductsDAO();
	}

	public int add(BuyingSessionsHasProducts bsshp) {
		int res = 0;
		//tạo Session
		try (Session session = sessionFactory.openSession()) {
			// Bắt đầu transaction
			Transaction t = session.beginTransaction();
			try {
				// Thực thi truy vấn trong transaction
				session.persist(bsshp);
				// Kết thúc transaction nếu thành công
				t.commit();
				res++;
			} catch (Exception e) {
				// Nếu có lỗi, rollback transaction
				if (t != null) {
					t.rollback();
				}
				System.out.println("Error: " + e);
			}
		} catch (Exception e) {
			System.out.println("Error: " + e);
		}
		return res;
	}

	public List<Products> getProductsByBuyingSessionId(int buyingSessionId) {
		List<Products> listProducts = new ArrayList<>();

		// Tạo Session
		try (Session session = sessionFactory.openSession()) {
			// Bắt đầu transaction
			Transaction t = session.beginTransaction();
			try {
				// Thực hiện HQL truy vấn để lấy id của BuyingSessionsHasProducts và sản phẩm
				String hql = "SELECT bsp.id, bsp.product FROM BuyingSessionsHasProducts bsp "
						+ "WHERE bsp.buyingSession.id = :buyingSessionId";
				Query<Object[]> query = session.createQuery(hql, Object[].class);
				query.setParameter("buyingSessionId", buyingSessionId);
				List<Object[]> resultList = query.getResultList();

				// Xử lý kết quả: duyệt qua danh sách kết quả để lấy sản phẩm
				for (Object[] row : resultList) {
					Integer bspId = (Integer) row[0]; // ID của BuyingSessionsHasProducts (không dùng nhưng có thể lưu lại)
					Products product = (Products) row[1]; // Sản phẩm liên kết với BuyingSessionsHasProducts

					// Thêm sản phẩm vào danh sách (sản phẩm có thể trùng lặp)
					listProducts.add(product);
				}

				// Kết thúc transaction nếu thành công
				t.commit();
			} catch (Exception e) {
				// Nếu có lỗi, rollback transaction
				if (t != null) {
					t.rollback();
				}
				System.out.println("Error: " + e);
			}
		} catch (Exception e) {
			System.out.println("Error: " + e);
		}

		return listProducts;
	}

	public Map<String, Integer> getStatic(int n) {
		Map<String, Integer> res = new HashMap<>();

		// Tạo Session
		try (Session session = sessionFactory.openSession()) {
			// Bắt đầu transaction
			Transaction t = session.beginTransaction();
			try {
				// HQL truy vấn để lấy top n sản phẩm có lượt mua cao nhất
				String hql = "SELECT p.name, COUNT(bsp.product) AS purchaseCount "
						+ "FROM BuyingSessionsHasProducts bsp "
						+ "JOIN bsp.product p "
						+ "GROUP BY p.name "
						+ "ORDER BY purchaseCount DESC";
				Query<Object[]> query = session.createQuery(hql, Object[].class);
				query.setMaxResults(n); // Chỉ lấy n sản phẩm
				List<Object[]> resultList = query.getResultList();

				// Xử lý kết quả: duyệt qua danh sách kết quả để đưa vào Map
				for (Object[] row : resultList) {
					String productName = (String) row[0]; // Tên sản phẩm
					Long purchaseCount = (Long) row[1]; // Số lượng mua (được đếm)

					// Thêm vào Map
					res.put(productName, purchaseCount.intValue());
				}

				// Kết thúc transaction nếu thành công
				t.commit();
			} catch (Exception e) {
				// Nếu có lỗi, rollback transaction
				if (t != null) {
					t.rollback();
				}
				System.out.println("Error: " + e);
			}
		} catch (Exception e) {
			System.out.println("Error: " + e);
		}

		return res;
	}

}
