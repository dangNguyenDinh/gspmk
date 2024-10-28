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
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 *
 * @author vdang
 */
@WebServlet(name="ActiveNewShoppingCart", urlPatterns={"/utils/ActiveNewShoppingCart"})
public class ActiveNewShoppingCart extends HttpServlet {
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
    throws ServletException, IOException {
		int buyingSessionId = Integer.parseInt(request.getParameter("buyingSessionId").trim());
		System.out.println(buyingSessionId);
		//tao moi mot map.entry trong context
		Map<Integer, List<Products>> BuyingCart = (Map<Integer, List<Products>>) getServletContext().getAttribute("BuyingCart");
		if(BuyingCart == null){
			BuyingCart = new HashMap<>();
		}
		BuyingCart.put(buyingSessionId, new ArrayList<>());
		getServletContext().setAttribute("BuyingCart", BuyingCart);
		response.getWriter().write(buyingSessionId);
    }
}
