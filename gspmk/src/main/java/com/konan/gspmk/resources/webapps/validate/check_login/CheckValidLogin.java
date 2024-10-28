/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */
package com.konan.gspmk.resources.webapps.validate.check_login;

import com.konan.gspmk.resources.hibernate.DAO.UsersDAO;
import com.konan.gspmk.resources.javabeans.Users;
import jakarta.servlet.ServletContext;
import java.io.IOException;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.util.*;

/**
 *
 * @author vdang
 */
@WebServlet(name = "CheckValidLogin", urlPatterns = {"/validate/CheckValidLogin"})
public class CheckValidLogin extends HttpServlet {

	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		//data from user
		String username = request.getParameter("username");
		String password = request.getParameter("password");
		// xử lý
		Users u = UsersDAO.getInstance().getUserByUsername(username);
		if (u.getPassword().equals(password)) {
			//them ten nguoi dung vao session (luu pham vi session)
			HttpSession session = request.getSession();
			session.setAttribute("user", u);
			session.setMaxInactiveInterval(60 * 2);

			//them nguoi dung vao danh sach online
			ServletContext sc = getServletContext();
			Map<String, Users> onlineMap = (Map<String, Users>) sc.getAttribute("OnlineMap");
			if (onlineMap == null) {
				onlineMap = new HashMap<>();
			}

// Lưu thông tin người dùng vào OnlineMap
			onlineMap.put(username, u); // u là đối tượng Users chứa thông tin của người dùng
			sc.setAttribute("OnlineMap", onlineMap);

			//chuyen huong sang home
			if (u.getIsAdmin()) {
				response.sendRedirect(request.getContextPath() + "/admin/home");
			} else {
				response.sendRedirect(request.getContextPath() + "/user/home");
			}

		} else {
			response.sendRedirect(request.getContextPath() +"/login?error=invalid");
		}
	}
}
