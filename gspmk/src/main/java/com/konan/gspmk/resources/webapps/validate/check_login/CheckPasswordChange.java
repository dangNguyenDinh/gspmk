/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */

package com.konan.gspmk.resources.webapps.validate.check_login;

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
@WebServlet(name="CheckPasswordChange", urlPatterns={"/validate/CheckPasswordChange"})
public class CheckPasswordChange extends HttpServlet {
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
    throws ServletException, IOException {
        
		//data from user
		String username = request.getParameter("username");
		String password = request.getParameter("password");
		// xử lý
		Users u = UsersDAO.getInstance().getUserByUsername(username);
		response.setContentType("text/plain");
        if (u != null && u.getPassword().equals(password)) {
            response.getWriter().write("true"); // Mật khẩu đúng
        } else {
            response.getWriter().write("false"); // Mật khẩu sai
        }
		
    }



}
