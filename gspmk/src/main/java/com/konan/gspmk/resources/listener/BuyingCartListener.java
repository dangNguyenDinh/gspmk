/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.konan.gspmk.resources.listener;

import com.konan.gspmk.resources.javabeans.Products;
import jakarta.servlet.ServletContextEvent;
import jakarta.servlet.ServletContextListener;
import jakarta.servlet.annotation.WebListener;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;

/**
 *
 * @author vdang
 */
@WebListener
public class BuyingCartListener implements ServletContextListener {
	
	@Override
    public void contextInitialized(ServletContextEvent sce) {
		// Khởi tạo List để lưu lời mời <người mời, người được mời>
        Map<Integer, List<Products>> BuyingCart = new HashMap<>();
        sce.getServletContext().setAttribute("BuyingCart", BuyingCart);
		
		//tin nhắn
		Map<Integer, List<Map.Entry<String, String>>> messages = new HashMap<>();
        sce.getServletContext().setAttribute("messages", messages);
	}
	
}