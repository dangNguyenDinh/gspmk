/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.konan.gspmk.resources.hibernate;

import java.util.Properties;
import org.hibernate.SessionFactory;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.cfg.Configuration;
import org.hibernate.cfg.Environment;
import org.hibernate.service.ServiceRegistry;
import com.konan.gspmk.resources.javabeans.Users;
import com.konan.gspmk.resources.javabeans.BuyingSessions;
import com.konan.gspmk.resources.javabeans.BuyingSessionsHasProducts;
import com.konan.gspmk.resources.javabeans.Products;
import com.konan.gspmk.resources.javabeans.UsersInBuyingSessions;




/**
 *
 * @author vdang
 */
public class HibernateUtils {
	//init the Factory
	private final static SessionFactory FACTORY;
	
	//init one time
	static{
		//always one time run when program start
		Configuration conf = new Configuration();
		Properties pros = new Properties();
		pros.put(Environment.DIALECT, "org.hibernate.dialect.MySQLDialect");
		pros.put(Environment.JAKARTA_JDBC_DRIVER, "com.mysql.cj.jdbc.Driver");
		pros.put(Environment.JAKARTA_JDBC_URL, "jdbc:mysql://localhost:3306/GSPMKManager?useUnicode=true&characterEncoding=UTF-8");
		pros.put(Environment.JAKARTA_JDBC_USER, "dangvudinh");
		pros.put(Environment.JAKARTA_JDBC_PASSWORD, "himawari");
		
		conf.addAnnotatedClass(Users.class);
		conf.addAnnotatedClass(Products.class);
		conf.addAnnotatedClass(BuyingSessions.class);
		conf.addAnnotatedClass(UsersInBuyingSessions.class);
		conf.addAnnotatedClass(BuyingSessionsHasProducts.class);
		ServiceRegistry serviceRegistry = new StandardServiceRegistryBuilder().applySettings(pros).build();
		FACTORY = conf.buildSessionFactory(serviceRegistry);
	}
	
	public static SessionFactory getFactory(){
		return FACTORY;
	}

}
