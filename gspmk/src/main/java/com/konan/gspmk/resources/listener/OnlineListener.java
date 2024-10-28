/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.konan.gspmk.resources.listener;

import com.konan.gspmk.resources.javabeans.Users;
import jakarta.servlet.ServletContext;
import jakarta.servlet.annotation.WebListener;
import jakarta.servlet.http.HttpSessionEvent;
import jakarta.servlet.http.HttpSessionListener;
import java.util.*;

/**
 *
 * @author vdang
 */
@WebListener
public class OnlineListener implements HttpSessionListener {

	@Override

	public void sessionDestroyed(HttpSessionEvent se) {
		ServletContext sc = se.getSession().getServletContext();
		Map<String, Users> onlineMap = (Map<String, Users>) sc.getAttribute("OnlineMap");
		if (onlineMap == null) {
			onlineMap = new HashMap<>();
		}

		// Lấy username từ session đã bị xóa rồi
		Users u = (Users) se.getSession().getAttribute("user");
		if (u != null) {
			System.out.println("destroy: " + u.getUsername());
			onlineMap.remove(u.getUsername()); // Xóa username khỏi onlineMap
			sc.setAttribute("OnlineMap", onlineMap);
		}
	}

}
