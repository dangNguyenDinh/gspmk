/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */
package com.konan.gspmk.resources.webapps.utils;

import com.konan.gspmk.resources.hibernate.DAO.BuyingSessionsDAO;
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
import java.io.BufferedReader;
import java.util.HashMap;
import java.util.Map;

/**
 *
 * @author vdang
 */
@WebServlet(name = "ActiveBuyingSessionId", urlPatterns = {"/utils/ActiveBuyingSessionId"})
public class ActiveBuyingSessionId extends HttpServlet {

	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		// Đọc chuỗi từ body của request
		StringBuilder sb = new StringBuilder();
		BufferedReader reader = request.getReader();
		String line;
		while ((line = reader.readLine()) != null) {
			sb.append(line);
		}
		String receivedString = sb.toString();
		String username = receivedString;
		//thêm một BuyingSession mới
		int buyingSessionId = BuyingSessionsDAO.getInstance(getServletContext()).add(username);
		//chỉnh sửa thuộc tính buyingSession của người mời
		Users userToChange = UsersDAO.getInstance().changeBuyingSessionOfUserByUsername(username, buyingSessionId);
		//xong phải cập nhật lên Session sau thay đổi.
		/*
		nếu thắc mắc tại sao lại chỉ cập nhật với session của người hiện tại trong Session thì:
		khi Client gốc vào BuyingSession -> cập nhật -> ok
		nếu Client gốc mời Client khách -> vẫn chưa cập nhật. Đến khi được chấp nhận thì Client khách 
		tự ấn nút confirm -> lúc đó mới vào phiên -> request vẫn xuât phát từ người cần cập nhật.
		*/
		request.getSession().setAttribute("user", userToChange);
		//cập nhật lên servletcontext
		ServletContext sc = getServletContext();
		Map<String, Users> onlineMap = (Map<String, Users>) sc.getAttribute("OnlineMap");
		if (onlineMap == null) {
			onlineMap = new HashMap<>();
		}
		
		// Lưu thông tin người dùng vào OnlineMap
		onlineMap.put(username, userToChange);
		sc.setAttribute("OnlineMap", onlineMap);
		
		System.out.println("đcmmm");
		
		response.getWriter().println(buyingSessionId);
	}
}
