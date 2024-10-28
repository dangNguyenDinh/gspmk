/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.konan.gspmk.resources.hibernate.DAO;

import static com.konan.gspmk.resources.hibernate.DAO.DAO.sessionFactory;
import com.konan.gspmk.resources.javabeans.BuyingSessions;
import jakarta.servlet.ServletContext;
import java.time.LocalDateTime;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.query.Query;

/**
 *
 * @author vdang
 */
public class BuyingSessionsDAO{
	ServletContext servletContext;
	
	public static BuyingSessionsDAO getInstance(ServletContext sc) {
		return new BuyingSessionsDAO(sc);
	}
	
	public BuyingSessionsDAO(ServletContext sc){
		this.servletContext = sc;
	}
	
	
	public int add(String organizerId) {
		//gan gia tri cho buying session
		int id = (int) this.servletContext.getAttribute("buyingSessionIndex");
		LocalDateTime currentDateTime = LocalDateTime.now();
		BuyingSessions bss = new BuyingSessions(id+1, organizerId, currentDateTime, null);
		//tạo Session
		try (Session session = sessionFactory.openSession()) {
			// Bắt đầu transaction
			Transaction t = session.beginTransaction();
			try {
				// Thực thi truy vấn trong transaction
				session.persist(bss);
				// Kết thúc transaction nếu thành công
				t.commit();
				//lưu lại giá trị (đã tăng)
				this.servletContext.setAttribute("buyingSessionIndex", id+1);
				System.out.println("BuyingSessions: " + (id+1));
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
		
		return id+1;
	}
	
	public BuyingSessions getBuyingSessionsById(int id){
		BuyingSessions res = new BuyingSessions();
		//tạo Session
		try (Session session = sessionFactory.openSession()) {
			// Bắt đầu transaction
			Transaction t = session.beginTransaction();
			try {
				// Thực thi truy vấn trong transaction
				String hql = "from BuyingSessions where id = :id";
				Query<BuyingSessions> qsl = session.createQuery(hql, BuyingSessions.class);
				qsl.setParameter("id", id);
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
	
	public BuyingSessions updateEndTimeBuyingSessionsById(int id, LocalDateTime time){
		BuyingSessions res = new BuyingSessions();
		//tạo Session
		try (Session session = sessionFactory.openSession()) {
			// Bắt đầu transaction
			Transaction t = session.beginTransaction();
			try {
				// Thực thi truy vấn trong transaction
				String hql = "from BuyingSessions where id = :id";
				Query<BuyingSessions> qsl = session.createQuery(hql, BuyingSessions.class);
				qsl.setParameter("id", id);
				res = qsl.uniqueResult();
				res.setEndTime(time);
				session.persist(res);
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

	public BuyingSessions activeOnGoingInBuyingSessionsById(int id){
		BuyingSessions res = new BuyingSessions();
		//tạo Session
		try (Session session = sessionFactory.openSession()) {
			// Bắt đầu transaction
			Transaction t = session.beginTransaction();
			try {
				// Thực thi truy vấn trong transaction
				String hql = "from BuyingSessions where id = :id";
				Query<BuyingSessions> qsl = session.createQuery(hql, BuyingSessions.class);
				qsl.setParameter("id", id);
				res = qsl.uniqueResult();
				res.setOnGoing(true);
				session.persist(res);
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
