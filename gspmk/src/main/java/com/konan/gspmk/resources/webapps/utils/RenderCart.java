/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */
package com.konan.gspmk.resources.webapps.utils;

import com.konan.gspmk.resources.hibernate.DAO.BuyingSessionsHasProductsDAO;
import com.konan.gspmk.resources.hibernate.DAO.UsersDAO;
import com.konan.gspmk.resources.javabeans.Products;
import java.io.IOException;
import java.io.PrintWriter;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.util.List;
import java.util.*;

/**
 *
 * @author vdang
 */
@WebServlet(name = "RenderCart", urlPatterns = {"/utils/RenderCart"})
public class RenderCart extends HttpServlet {

	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		//lấy từ db lên

		String currentUser = request.getParameter("currentUser");
		Integer buyingSessionId = UsersDAO.getInstance().getUserByUsername(currentUser).getBuyingSessionId();
		if (buyingSessionId != null) {
			// Lấy danh sách sản phẩm từ session hoặc cơ sở dữ liệu
			List<Products> listProducts = BuyingSessionsHasProductsDAO.getInstance().getProductsByBuyingSessionId(buyingSessionId);
			System.out.println("listProducts" + listProducts.size());
			// Map để chứa dữ liệu tạm thời cho các sản phẩm lặp lại
			Map<Products, Integer> productCountMap = new HashMap<>();

			for (Products product : listProducts) {
				productCountMap.put(product, productCountMap.getOrDefault(product, 0) + 1);
			}

			// Tạo chuỗi kết quả để gửi về phía client
			StringBuilder result = new StringBuilder();
			for (Map.Entry<Products, Integer> entry : productCountMap.entrySet()) {
				Products product = entry.getKey();
				int quantity = entry.getValue();
				result.append(product.getName()).append(",")
						.append(product.getImageName()).append(",")
						.append(quantity).append(",")
						.append(product.getPrice()).append(";");
			}

			// Gửi chuỗi về phía client
			response.setContentType("text/plain");
			PrintWriter out = response.getWriter();
			out.print(result.toString());
			out.flush();
		}

	}
}