/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.konan.gspmk.resources.hibernate.DAO;

import static com.konan.gspmk.resources.hibernate.DAO.DAO.sessionFactory;
import com.konan.gspmk.resources.javabeans.Products;
import com.konan.gspmk.resources.javabeans.Users;
import java.util.ArrayList;
import java.util.List;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.query.Query;

/**
 *
 * @author vdang
 */
public class ProductsDAO implements DAO<Products> {

	public static ProductsDAO getInstance() {
		return new ProductsDAO();
	}

	@Override
	public List<Products> getAll() {
		List<Products> listProducts = new ArrayList<>();
		//tạo Session
		try (Session session = sessionFactory.openSession()) {
			// Bắt đầu transaction
			Transaction t = session.beginTransaction();
			try {
				// Thực thi truy vấn trong transaction
				String hql = "from Products";
				Query<Products> qsl = session.createQuery(hql, Products.class);
				listProducts = qsl.list();
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

	public Products getProductsById(int id) {
		Products p = new Products();
		//tạo Session
		try (Session session = sessionFactory.openSession()) {
			// Bắt đầu transaction
			Transaction t = session.beginTransaction();
			try {
				String hql = "from Products where id = :id";
				Query<Products> query = session.createQuery(hql, Products.class);
				query.setParameter("id", id);

				p = query.uniqueResult();
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

		return p;
	}

	
	@Override
	public int add(Products prd) {
		int res = 0;
		try (Session session = sessionFactory.openSession()) {
			// Bắt đầu transaction
			Transaction t = session.beginTransaction();
			try {
				// Thực thi truy vấn trong transaction
				Products p = new Products();
				p.setName(prd.getName());
				p.setPrice(prd.getPrice());
				p.setType(prd.getType());
				p.setDescription(prd.getDescription());
				p.setReadyState(prd.isReadyState());
				p.setImageName(prd.getImageName());
				p.setSha(prd.getSha());
				session.persist(p);
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

	public void updateWithoutImage(int id, Products prd) {
		//tạo Session
		try (Session session = sessionFactory.openSession()) {
			// Bắt đầu transaction
			Transaction t = session.beginTransaction();
			try {
				// Thực thi truy vấn trong transaction
				//lấy products có id cần sửa
				String hql = "from Products where id = :id";
				Query<Products> qsl = session.createQuery(hql, Products.class);
				qsl.setParameter("id", id);
				Products needToFix = qsl.uniqueResult();
				//sửa Product đó
				needToFix.setName(prd.getName());
				needToFix.setPrice(prd.getPrice());
				needToFix.setType(prd.getType());
				needToFix.setDescription(prd.getDescription());
				needToFix.setReadyState(prd.isReadyState());
				session.persist(needToFix);
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

	}

	public void updateImageName(int id, String imageName, String sha) {
		//tạo Session
		try (Session session = sessionFactory.openSession()) {
			// Bắt đầu transaction
			Transaction t = session.beginTransaction();
			try {
				// Thực thi truy vấn trong transaction
				//lấy products có id cần sửa
				String hql = "from Products where id = :id";
				Query<Products> qsl = session.createQuery(hql, Products.class);
				qsl.setParameter("id", id);
				Products needToFix = qsl.uniqueResult();
				//sửa Product đó
				needToFix.setImageName(imageName);
				needToFix.setSha(sha);
				session.persist(needToFix);
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

	}

	public int delete(int id) {
		int res = 0;
		// Tạo Session
		try (Session session = sessionFactory.openSession()) {
			// Bắt đầu transaction
			Transaction t = session.beginTransaction();

			try {
				// Tìm kiếm user theo username
				String hql = "from Products where id = :id";
				Query<Products> query = session.createQuery(hql, Products.class);
				query.setParameter("id", id);

				Products p = query.uniqueResult();

				if (p != null) {
					// Nếu tìm thấy user, xóa bằng session.remove()
					session.remove(p);
					res = 1; // Xóa thành công 1 bản ghi
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

	@Override
	public int delete(String username) {
		throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
	}

	
	
}
