/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */
package com.konan.gspmk.resources.webapps.utils;

import com.konan.gspmk.resources.hibernate.DAO.UsersDAO;
import jakarta.servlet.ServletContext;
import java.io.IOException;
import java.io.PrintWriter;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.util.*;

/**
 *
 * @author vdang
 */
@WebServlet(name = "SendMessage", urlPatterns = {"/utils/send"})
public class SendMessage extends HttpServlet {

	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
		String username = request.getParameter("username");
		String content = request.getParameter("content");

		int buyingSessionId = UsersDAO.getInstance().getUserByUsername(username).getBuyingSessionId();

		// Lấy ra ServletContext
		ServletContext context = getServletContext();

		// Lấy ra Map lưu trữ tin nhắn từ ServletContext
		Map<Integer, List<Map.Entry<String, String>>> messages
				= (Map<Integer, List<Map.Entry<String, String>>>) context.getAttribute("messages");

		// Kiểm tra và khởi tạo nếu messages chưa tồn tại
		if (messages == null) {
			messages = new HashMap<>();
			context.setAttribute("messages", messages);
		}

		// Thêm tin nhắn vào danh sách theo buyingSessionId
		messages.putIfAbsent(buyingSessionId, new ArrayList<>()); // Khởi tạo danh sách nếu chưa tồn tại
		messages.get(buyingSessionId).add(new AbstractMap.SimpleEntry<>(username, content));
		
		
		// Phản hồi cho client
		response.setStatus(HttpServletResponse.SC_OK);
	}

}
