/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */
package com.konan.gspmk.resources.webapps.validate;

import com.konan.gspmk.resources.hibernate.DAO.UsersDAO;
import com.konan.gspmk.resources.javabeans.Users;
import java.io.IOException;
import java.io.PrintWriter;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

/**
 *
 * @author vdang
 */
@WebServlet(name = "ChangePassword", urlPatterns = {"/validate/ChangePassword"})
public class ChangePassword extends HttpServlet {

	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		// Lấy dữ liệu từ yêu cầu
		String username = request.getParameter("username");
		String newPassword = request.getParameter("newPassword");

		// Kiểm tra dữ liệu đầu vào
		if (username == null || newPassword == null || username.isEmpty() || newPassword.isEmpty()) {
			response.setContentType("text/plain");
			response.getWriter().write("error"); // Lỗi nếu dữ liệu thiếu
			return;
		}

		// Thực hiện thay đổi mật khẩu
		Users updatedUser = UsersDAO.getInstance().changePassWordByName(username, newPassword);
		response.setContentType("text/plain");
		if (updatedUser != null) {
			response.getWriter().write("success"); // Thay đổi thành công
		} else {
			response.getWriter().write("error"); // Thay đổi không thành công
		}
	}
}


