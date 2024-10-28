/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */
package com.konan.gspmk.resources.webapps.utils;

import com.konan.gspmk.resources.javabeans.Products;
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
@WebServlet(name = "RemoveFromShoppingCart", urlPatterns = {"/utils/RemoveFromShoppingCart"})
public class RemoveFromShoppingCart extends HttpServlet {

	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		int buyingSessionId = Integer.parseInt(request.getParameter("buyingsessionid"));
		int index = Integer.parseInt(request.getParameter("index"));

		Map<Integer, List<Products>> BuyingCart = (Map<Integer, List<Products>>) getServletContext().getAttribute("BuyingCart");
		if (BuyingCart != null) {
			List<Products> productsList = BuyingCart.get(buyingSessionId);
			if (productsList != null && index >= 0 && index < productsList.size()) {
				productsList.remove(index); // Xóa sản phẩm tại vị trí index
			}
		}
		response.getWriter().write(index);
	}

}
