/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */
package com.konan.gspmk.resources.webapps.user;
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
@WebServlet(name = "RenderNameProducts", urlPatterns = {"/user/RenderNameProducts"})
public class RenderNameProducts extends HttpServlet {

	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		response.setContentType("application/json");
		PrintWriter out = response.getWriter();

		ProductsDAO dao = ProductsDAO.getInstance();

		// Lấy danh sách sản phẩm
		List<Products> productsList = dao.getAll();

		// Tạo chuỗi JSON chỉ chứa tên sản phẩm
		StringBuilder json = new StringBuilder("[");
		for (int i = 0; i < productsList.size(); i++) {
			Products product = productsList.get(i);
			if (product.isReadyState()) {
				json.append("{")
						.append("\"name\":\"").append(product.getName()).append("\"")
						.append("},");
			}
		}

		// Loại bỏ dấu phẩy cuối cùng và đóng mảng JSON
		if (json.length() > 1) {
			json = new StringBuilder(json.substring(0, json.length() - 1));
		}
		json.append("]");

		// Gửi chuỗi JSON đến client
		out.print(json.toString());
		out.flush();
	}

}
