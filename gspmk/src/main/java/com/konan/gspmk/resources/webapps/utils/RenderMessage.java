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
@WebServlet(name = "RenderMessage", urlPatterns = {"/utils/render"})
public class RenderMessage extends HttpServlet {

	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		response.setContentType("text/event-stream");
		response.setCharacterEncoding("UTF-8");

		String username = request.getParameter("currentUser");
		int buyingSessionId = UsersDAO.getInstance().getUserByUsername(username).getBuyingSessionId();

		ServletContext context = getServletContext();
		Map<Integer, List<Map.Entry<String, String>>> messages = (Map<Integer, List<Map.Entry<String, String>>>) context.getAttribute("messages");

		if (messages == null) {
			messages = new HashMap<>();
			context.setAttribute("messages", messages);
		}
		try (PrintWriter out = response.getWriter()) {
			while (true) {
				// Tạo chuỗi JSON để chứa các tin nhắn
				StringBuilder json = new StringBuilder("[");

				List<Map.Entry<String, String>> sessionMessages = messages.get(buyingSessionId);
				if (sessionMessages != null) {
					boolean first = true; // Biến kiểm tra có phải là phần tử đầu tiên không
					for (Map.Entry<String, String> message : sessionMessages) {
						if (!first) {
							json.append(", "); // Thêm dấu phẩy giữa các phần tử
						}
						json.append("{")
								.append("\"from\": \"").append(message.getKey()).append("\", ")
								.append("\"content\": \"").append(message.getValue()).append("\"")
								.append("}");
						first = false; // Đánh dấu phần tử đầu tiên đã được thêm
					}
				}

				json.append("]"); // Kết thúc mảng JSON

				// Gửi dữ liệu JSON qua SSE
				out.write("data: " + json.toString() + "\n\n");
				out.flush();

				// Ngủ một lúc trước khi kiểm tra tiếp
				try {
					Thread.sleep(1000);
				} catch (InterruptedException e) {
					e.printStackTrace();
					// Nếu bị gián đoạn, thoát vòng lặp
					break;
				}
			}
		}
	}

}
