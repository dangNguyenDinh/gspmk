/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.konan.gspmk.resources.hibernate.DAO;

import static com.konan.gspmk.resources.hibernate.DAO.DAO.sessionFactory;
import com.konan.gspmk.resources.javabeans.UsersInBuyingSessions;
import java.util.List;
import org.hibernate.Session;
import org.hibernate.Transaction;

/**
 *
 * @author vdang
 */
public class UsersInBuyingSessionsDAO implements DAO<UsersInBuyingSessions>{

	@Override
	public List<UsersInBuyingSessions> getAll() {
		throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
	}

	@Override
	public int add(UsersInBuyingSessions uibs) {
		int res = 0;
		//tạo Session
		try (Session session = sessionFactory.openSession()) {
			// Bắt đầu transaction
			Transaction t = session.beginTransaction();
			try {
				// Thực thi truy vấn trong transaction
				session.persist(uibs);
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
		throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
	}
	
}
