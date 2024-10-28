/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */

package com.konan.gspmk.resources.webapps.admin.utils;

import com.konan.gspmk.resources.hibernate.DAO.ProductsDAO;
import com.konan.gspmk.resources.javabeans.Products;
import java.io.IOException;
import java.io.PrintWriter;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.util.List;

/**
 *
 * @author vdang
 */
@WebServlet(name="RenderProductsViewAdmin", urlPatterns={"/admin/RenderProductsView"})
public class RenderProductsView extends HttpServlet {
	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		response.setContentType("application/json");
		PrintWriter out = response.getWriter();

		ProductsDAO dao = ProductsDAO.getInstance();

		// Gọi hàm getAll() để lấy danh sách sản phẩm
		List<Products> productsList = dao.getAll();

		// Tạo chuỗi JSON thủ công
		StringBuilder json = new StringBuilder("[");
		for (int i = 0; i < productsList.size(); i++) {
			Products product = productsList.get(i);
			json.append("{")
					.append("\"id\":").append(product.getId()).append(",")
					.append("\"name\":\"").append(product.getName()).append("\",")
					.append("\"price\":").append(product.getPrice()).append(",")
					.append("\"type\":\"").append(product.getType()).append("\",")
					.append("\"sha\":\"").append(product.getSha()).append("\",")
					.append("\"description\":\"").append(product.getDescription()).append("\",")
					.append("\"imageName\":\"").append(product.getImageName()).append("\",")
					.append("\"readyState\":").append(product.isReadyState())
					.append("}");
			if (i < productsList.size() - 1) {
				json.append(",");
			}
		}
		json.append("]");
		
		out.print(json.toString());
		out.flush();

	}
}
