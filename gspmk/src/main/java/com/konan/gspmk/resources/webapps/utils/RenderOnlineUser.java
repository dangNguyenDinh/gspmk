/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */
package com.konan.gspmk.resources.webapps.utils;

import com.konan.gspmk.resources.javabeans.Users;
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
@WebServlet(name = "RenderOnlineUser", urlPatterns = {"/utils/RenderOnlineUser"})
public class RenderOnlineUser extends HttpServlet {

	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		response.setContentType("text/event-stream");
		response.setCharacterEncoding("UTF-8");

		PrintWriter out = response.getWriter();

		while (true) {
			Map<String, Users> onlineMap = (Map<String, Users>) getServletContext().getAttribute("OnlineMap");
			if (onlineMap == null) {
				onlineMap = new HashMap<>();
			}

			// Tạo chuỗi JSON thủ công
			StringBuilder json = new StringBuilder("{");
			for (Map.Entry<String, Users> entry : onlineMap.entrySet()) {
				Users user = entry.getValue();
				json.append("\"").append(entry.getKey()).append("\": {")
						.append("\"isAdmin\": ").append(user.getIsAdmin()).append(", ")
						.append("\"buyingSessionId\": ").append(user.getBuyingSessionId()).append("}, ");
			}

			// Loại bỏ dấu phẩy cuối cùng nếu có
			if (json.length() > 1) {
				json.delete(json.length() - 2, json.length()); // Xóa dấu phẩy và khoảng trắng cuối cùng
			}
			json.append("}");

			// Gửi chuỗi JSON qua SSE
			out.write("data: " + json.toString() + "\n\n");

			out.flush();

			// Nghỉ 1 giây trước khi gửi tiếp
			try {
				Thread.sleep(2000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}

}
