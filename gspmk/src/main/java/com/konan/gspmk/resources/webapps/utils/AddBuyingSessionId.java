/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */

package com.konan.gspmk.resources.webapps.utils;

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
import java.util.HashMap;
import java.util.Map;

/**
 *
 * @author vdang
 */
@WebServlet(name="AddBuyingSessionId", urlPatterns={"/utils/AddBuyingSessionId"})
public class AddBuyingSessionId extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
    throws ServletException, IOException {
        String currentUser = request.getParameter("currentUser");
		String user = request.getParameter("user");
		
		//bây giờ phải đổi buyingSessionId của currentUser theo user, rồi cập nhật lên database, session và context
		Users currentuser_from_database = UsersDAO.getInstance().getUserByUsername(user);
		Users user_from_database = UsersDAO.getInstance().changeBuyingSessionOfUserByUsername(currentUser, currentuser_from_database.getBuyingSessionId());
		//cập nhật lên session
		request.getSession().setAttribute("user", user_from_database);
		//cập nhật lên servletcontext
		ServletContext sc = getServletContext();
		Map<String, Users> onlineMap = (Map<String, Users>) sc.getAttribute("OnlineMap");
		if (onlineMap == null) {
			onlineMap = new HashMap<>();
		} 
		
		// Lưu thông tin người dùng vào OnlineMap
		onlineMap.put(currentUser, currentuser_from_database);
		sc.setAttribute("OnlineMap", onlineMap);
		
		//thông báo là đã vào phiên với người mời
		response.getWriter().println("You are preparing to gspmk with " + user + ".");
    }



}
