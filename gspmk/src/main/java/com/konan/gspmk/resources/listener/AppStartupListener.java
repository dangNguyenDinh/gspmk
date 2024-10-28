/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.konan.gspmk.resources.listener;

import com.konan.gspmk.resources.hibernate.HibernateUtils;
import jakarta.servlet.*;
import jakarta.servlet.annotation.WebListener;

/**
 *
 * @author vdang
 */
@WebListener
public class AppStartupListener implements ServletContextListener {

	// Biến buyingSessionIndex khởi tạo với giá trị 0
	private int buyingSessionIndex = 0;

	@Override
	public void contextInitialized(ServletContextEvent sce) {
		HibernateUtils.getFactory();
		ServletContext servletContext = sce.getServletContext();
		servletContext.setAttribute("buyingSessionIndex", buyingSessionIndex);
		
	}
	@Override
	public void contextDestroyed(ServletContextEvent sce) {
		// Dừng AbandonedConnectionCleanupThread
		com.mysql.cj.jdbc.AbandonedConnectionCleanupThread.checkedShutdown();
	}
}
