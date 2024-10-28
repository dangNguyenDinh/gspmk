/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.konan.gspmk.resources.listener;

import com.konan.gspmk.resources.hibernate.HibernateUtils;
import jakarta.servlet.ServletContext;
import jakarta.servlet.ServletContextEvent;
import jakarta.servlet.ServletContextListener;
import jakarta.servlet.annotation.WebListener;

/**
 *
 * @author vdang
 */

@WebListener
public class MySQLCleanUpListener implements ServletContextListener {
	@Override
	public void contextDestroyed(ServletContextEvent sce) {
		// Dừng AbandonedConnectionCleanupThread
		com.mysql.cj.jdbc.AbandonedConnectionCleanupThread.checkedShutdown();
	}
}