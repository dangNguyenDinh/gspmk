/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */
package com.konan.gspmk.resources.webapps.validate.check_login;

import com.konan.gspmk.resources.hibernate.DAO.UsersDAO;
import com.konan.gspmk.resources.javabeans.Users;
import jakarta.servlet.ServletContext;
import java.io.IOException;
import java.io.PrintWriter;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 *
 * @author vdang
 */
@WebServlet(name = "CheckValidAndSignup", urlPatterns = {"/validate/CheckValidAndSignup"})
public class CheckValidAndSignup extends HttpServlet {

	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		//data from user
		String username = request.getParameter("username-signup");
		String password = request.getParameter("password-signup");

		Users u = new Users(username, password, null, false);
		UsersDAO.getInstance().add(u);

		//như là đã đăng nhập rồi
		//them ten nguoi dung vao session (luu pham vi session)
		HttpSession session = request.getSession();
		session.setAttribute("user", u);
		session.setMaxInactiveInterval(60 * 60);
		//them nguoi dung vao danh sach online

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
	}

}
