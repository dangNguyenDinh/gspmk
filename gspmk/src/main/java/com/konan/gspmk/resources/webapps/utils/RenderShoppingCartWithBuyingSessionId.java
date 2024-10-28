/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */
package com.konan.gspmk.resources.webapps.utils;

import com.konan.gspmk.resources.javabeans.Products;
import com.konan.gspmk.resources.javabeans.Users;
import java.io.IOException;
import java.io.PrintWriter;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 *
 * @author vdang
 */
@WebServlet(name = "RenderShoppingCartWithBuyingSessionId", urlPatterns = {"/utils/RenderShoppingCartWithBuyingSessionId"})
public class RenderShoppingCartWithBuyingSessionId extends HttpServlet {

	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		response.setContentType("text/event-stream");
		response.setCharacterEncoding("UTF-8");

		PrintWriter out = response.getWriter();

		while (true) {
			int buyingSessionId = Integer.parseInt(request.getParameter("buyingsessionid"));

			// Lấy BuyingCart từ ServletContext
			Map<Integer, List<Products>> BuyingCart = (Map<Integer, List<Products>>) getServletContext().getAttribute("BuyingCart");
			if (BuyingCart == null) {
				BuyingCart = new HashMap<>();
			}

			// Tạo chuỗi JSON để xuất ra
			StringBuilder json = new StringBuilder("[");
			List<Products> productsList = BuyingCart.get(buyingSessionId);

			if (productsList != null) {
				for (Products product : productsList) {
					json.append("{")
							.append("\"id\": ").append(product.getId()).append(", ")
							.append("\"name\": \"").append(product.getName()).append("\", ")
							.append("\"price\": ").append(product.getPrice()).append(", ")
							.append("\"type\": \"").append(product.getType()).append("\", ")
							.append("\"description\": \"").append(product.getDescription()).append("\", ")
							.append("\"imageName\": \"").append(product.getImageName()).append("\"") // Đảm bảo không có dấu phẩy ở cuối
							.append("}, "); // Không thêm dấu phẩy nếu là sản phẩm cuối cùng
				}

				// Loại bỏ dấu phẩy cuối cùng nếu có
				if (json.length() > 1) {
					json.delete(json.length() - 2, json.length()); // Xóa dấu phẩy và khoảng trắng cuối cùng
				}
			}

			json.append("]");

			// Gửi chuỗi JSON qua response
			out.write("data: " + json.toString() + "\n\n");
			out.flush();
			// Nghỉ 1 giây trước khi gửi tiếp
			try {
				Thread.sleep(1000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}

}
