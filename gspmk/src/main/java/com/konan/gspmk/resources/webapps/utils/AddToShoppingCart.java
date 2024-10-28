/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */
package com.konan.gspmk.resources.webapps.utils;

import com.konan.gspmk.resources.hibernate.DAO.UsersDAO;
import com.konan.gspmk.resources.javabeans.Products;
import java.io.IOException;
import java.io.PrintWriter;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.util.AbstractMap;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 *
 * @author vdang
 */
@WebServlet(name = "AddToShoppingCart", urlPatterns = {"/utils/AddToShoppingCart"})
public class AddToShoppingCart extends HttpServlet {

	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		//them mot lua chon vao shoppingcart, voi id xac dinh, them vao context
		int buyingSessionId = Integer.parseInt(request.getParameter("currentBuyingSessionId")) ;
		Products p = new Products();

		int id = Integer.parseInt(request.getParameter("id"));
		String name = request.getParameter("name");
		double price = Double.parseDouble(request.getParameter("price"));
		String type = request.getParameter("type");
		String description = request.getParameter("description");
		String imageName = request.getParameter("imageName");
		String sha = request.getParameter("sha");
		boolean readyState = Boolean.parseBoolean(request.getParameter("readyState"));
		
		p.setId(id); // Gán id
		p.setName(name); // Gán tên sản phẩm
		p.setPrice(price); // Gán giá
		p.setType(type); // Gán loại sản phẩm
		p.setDescription(description); // Gán mô tả
		p.setImageName(imageName); // Gán tên hình ảnh
		p.setSha(sha); // Gán mã SHA
		p.setReadyState(readyState); // Gán trạng thái sẵn sàng

		//lấy context và cập nhật
		Map<Integer, List<Products>> BuyingCart = (Map<Integer, List<Products>>) getServletContext().getAttribute("BuyingCart");
		if(BuyingCart == null){
			BuyingCart = new HashMap<>();
		}
		List<Products> arp = BuyingCart.get(buyingSessionId);
		arp.add(p);
		BuyingCart.put(buyingSessionId, arp);
		getServletContext().setAttribute("BuyingCart", BuyingCart);
		
		//tin nhắn trong session
		Map<Integer, List<Map.Entry<String, String>>> messages = 
            (Map<Integer, List<Map.Entry<String, String>>>) getServletContext().getAttribute("messages");
		// Kiểm tra và khởi tạo nếu messages chưa tồn tại
        if (messages == null) {
            messages = new HashMap<>();
            getServletContext().setAttribute("messages", messages);
        }
		
		// Thêm tin nhắn vào danh sách theo buyingSessionId
        synchronized (messages) {  // Đảm bảo tính đồng bộ khi thêm tin nhắn
            messages.putIfAbsent(buyingSessionId, new ArrayList<>()); // Khởi tạo danh sách nếu chưa tồn tại
			//sau đó sẽ thêm tin nhắn vào đây
		}
		
		response.getWriter().println(name);
	}

}
