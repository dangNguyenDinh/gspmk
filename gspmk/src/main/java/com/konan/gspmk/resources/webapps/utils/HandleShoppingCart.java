/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */
package com.konan.gspmk.resources.webapps.utils;

import com.konan.gspmk.resources.hibernate.DAO.BuyingSessionsDAO;
import com.konan.gspmk.resources.hibernate.DAO.BuyingSessionsHasProductsDAO;
import com.konan.gspmk.resources.hibernate.DAO.UsersDAO;
import com.konan.gspmk.resources.javabeans.BuyingSessions;
import com.konan.gspmk.resources.javabeans.BuyingSessionsHasProducts;
import com.konan.gspmk.resources.javabeans.Products;
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
@WebServlet(name = "HandleShoppingCart", urlPatterns = {"/utils/HandleShoppingCart"})
public class HandleShoppingCart extends HttpServlet {

	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		String currentUser = request.getParameter("currentUser");
		Integer buyingSessionId = UsersDAO.getInstance().getUserByUsername(currentUser).getBuyingSessionId();
		if (buyingSessionId != null) {
			//xoa shoppingcart hien tai, day data vao database
			//lay ra tat 
			Map<Integer, List<Products>> BuyingCart = (Map<Integer, List<Products>>) getServletContext().getAttribute("BuyingCart");
			if (BuyingCart == null) {
				BuyingCart = new HashMap<>();
			}

			List<Products> arp = BuyingCart.get(buyingSessionId);
			BuyingSessions buyingSession = BuyingSessionsDAO.getInstance(getServletContext()).getBuyingSessionsById(buyingSessionId);
			if (arp != null) {
				arp.forEach((Products product) -> {
					BuyingSessionsHasProducts bsshp = new BuyingSessionsHasProducts(buyingSession, product);
					BuyingSessionsHasProductsDAO.getInstance().add(bsshp);
				});
				//xóa cũ
				BuyingCart.remove(buyingSessionId);
				getServletContext().setAttribute("BuyingCart", BuyingCart);

				response.getWriter().write(arp.size());
			}

		}

	}

}
