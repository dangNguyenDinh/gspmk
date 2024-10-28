/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.konan.gspmk.resources.hibernate.DAO;

import com.konan.gspmk.resources.javabeans.Users;
import org.hibernate.query.Query;
import java.util.ArrayList;
import java.util.List;
import org.hibernate.*;

/**
 *
 * @author vdang
 */
public class UsersDAO implements DAO<Users> {

	public static UsersDAO getInstance() {
		return new UsersDAO();
	}

	//lấy tất cả
	@Override
	public List<Users> getAll() {
		List<Users> listUser = new ArrayList<>();
		//tạo Session
		try (Session session = sessionFactory.openSession()) {
			// Bắt đầu transaction
			Transaction t = session.beginTransaction();
			try {
				// Thực thi truy vấn trong transaction
				String hql = "from Users";
				Query<Users> qsl = session.createQuery(hql, Users.class);
				listUser = qsl.list();
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
		return listUser;
	}

	public List<Users> getUsersByBuyingSessionId(int buyingSessionId) {
		List<Users> listUser = new ArrayList<>();
		//tạo Session
		try (Session session = sessionFactory.openSession()) {
			// Bắt đầu transaction
			Transaction t = session.beginTransaction();
			try {
				// Thực thi truy vấn trong transaction
				String hql = "from Users where buyingSessionId =:id";
				Query<Users> qsl = session.createQuery(hql, Users.class);
				qsl.setParameter("id", buyingSessionId);
				listUser = qsl.list();
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
		return listUser;
	}

	//lấy theo tên
	public Users getUserByUsername(String username) {
		Users res = new Users();
		//tạo Session
		try (Session session = sessionFactory.openSession()) {
			// Bắt đầu transaction
			Transaction t = session.beginTransaction();
			try {
				// Thực thi truy vấn trong transaction
				String hql = "from Users where username = :name";
				Query<Users> qsl = session.createQuery(hql, Users.class);
				qsl.setParameter("name", username);
				res = qsl.uniqueResult();
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

	// Đổi trạng thái đang trong Session của người dùng theo username
	public Users changeBuyingSessionOfUserByUsername(String username, Integer buyingSessionId) {
		Users user = null;

		// Tạo Session
		try (Session session = sessionFactory.openSession()) {
			// Bắt đầu transaction
			Transaction transaction = session.beginTransaction();
			try {
				// Truy vấn tìm user theo username
				String hql = "from Users where username = :name";
				Query<Users> query = session.createQuery(hql, Users.class);
				query.setParameter("name", username);
				user = query.uniqueResult();

				// Nếu user tồn tại, cập nhật buyingSessionId
				if (user != null) {
					user.setBuyingSessionId(buyingSessionId);
					session.persist(user);  // Cập nhật user
				}

				// Commit transaction
				transaction.commit();
			} catch (Exception e) {
				// Rollback nếu có lỗi
				if (transaction != null) {
					transaction.rollback();
				}
				System.out.println("Error: " + e);
			}
		} catch (Exception e) {
			System.out.println("Error: " + e);
		}

		return user;
	}

	@Override
	public int add(Users user) {
		int res = 0;
		//tạo Session
		try (Session session = sessionFactory.openSession()) {
			// Bắt đầu transaction
			Transaction t = session.beginTransaction();
			try {
				// Thực thi truy vấn trong transaction
				session.persist(user);
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

	@Override
	public int delete(String username) {
		int res = 0;
		// Tạo Session
		try (Session session = sessionFactory.openSession()) {
			// Bắt đầu transaction
			Transaction t = session.beginTransaction();

			try {
				// Tìm kiếm user theo username
				String hql = "from Users where username = :uname";
				Query<Users> query = session.createQuery(hql, Users.class);
				query.setParameter("uname", username);

				Users user = query.uniqueResult();

				if (user != null) {
					// Nếu tìm thấy user, xóa bằng session.remove()
					session.remove(user);
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

	public Users changePassWordByName(String username, String newPassword) {
		Users res = null;
		try (Session session = sessionFactory.openSession()) {
			// Bắt đầu transaction
			Transaction t = session.beginTransaction();
			try {
				// Thực thi truy vấn HQL để lấy người dùng theo username
				String hql = "FROM Users u WHERE u.username = :username";
				res = session.createQuery(hql, Users.class)
						.setParameter("username", username)
						.uniqueResult();

				// Kiểm tra xem người dùng có tồn tại không
				if (res != null) {
					// Cập nhật mật khẩu
					res.setPassword(newPassword);
					session.update(res);
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
