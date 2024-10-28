/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */
package com.konan.gspmk.resources.landing;

import com.konan.gspmk.resources.hibernate.DAO.BuyingSessionsDAO;
import com.konan.gspmk.resources.hibernate.DAO.BuyingSessionsHasProductsDAO;
import com.konan.gspmk.resources.hibernate.DAO.ProductsDAO;
import com.konan.gspmk.resources.hibernate.DAO.UsersDAO;
import com.konan.gspmk.resources.hibernate.DAO.UsersInBuyingSessionsDAO;
import com.konan.gspmk.resources.hibernate.HibernateUtils;
import com.konan.gspmk.resources.javabeans.*;
import jakarta.persistence.Query;
import jakarta.servlet.ServletContext;
import java.io.IOException;
import java.io.PrintWriter;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import org.hibernate.Session;
import org.hibernate.Transaction;

/**
 *
 * @author vdang servlet để test trên sản phẩm
 */
@WebServlet(name = "test", urlPatterns = {"/test"})
public class test extends HttpServlet {

	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

//		StringBuilder outputTest = new StringBuilder();
//
//        // Gọi hàm getInstance() để lấy đối tượng ProductsDAO
//        ProductsDAO dao = ProductsDAO.getInstance();
//
//        // Gọi hàm getAll() để lấy danh sách sản phẩm
//        List<Products> productsList = dao.getAll();
//
//        // In kết quả ra, sử dụng outputTest.append()
//        for (Products p : productsList) {
//            outputTest.append("<h3>")
//                      .append(p.getId())
//                      .append(", ")
//                      .append(p.getName())  // hoặc p.getUsername() nếu có trường username
//                      .append("</h3>\n");
//        }
//		int buyingSessionId = UsersDAO.getInstance().getUserByUsername("deidara").getBuyingSessionId();
//		List<Users> lu = UsersDAO.getInstance().getUsersByBuyingSessionId(buyingSessionId);
//		lu.forEach((user) -> {
//			UsersDAO.getInstance().changeBuyingSessionOfUserByUsername(user.getUsername(), null);
//		});

//            String name = "Gạo";
//            double price = 12;
//            String type = "Thực phẩm";
//            String description = "Đây là gạo";
//            boolean readyState = false;
//            String imageName = "gao_thom.png";
//			String sha = "sha";
//            
//			Products prd =  new Products(name, price, type, description, imageName, sha, readyState);

//			BuyingSessions buyingSession = BuyingSessionsDAO.getInstance(getServletContext()).getBuyingSessionsById(1);
//			Products product = ProductsDAO.getInstance().getProductsById(1);
//			BuyingSessionsHasProducts bsshp = new BuyingSessionsHasProducts(buyingSession, product);
//			BuyingSessionsHasProductsDAO.getInstance().add(bsshp);
		//List<Products> p = BuyingSessionsHasProductsDAO.getInstance().getProductsByBuyingSessionId(1);
//		ServletContext context = getServletContext();
//        List<Map.Entry<String, String>> invitations = (List<Map.Entry<String, String>>) context.getAttribute("invitations");
//        PrintWriter out = response.getWriter();
//		if (invitations == null) {
//            invitations = new ArrayList<>();
//            context.setAttribute("invitations", invitations); // Đảm bảo rằng invitations được lưu trữ
//        }
//		// Tạo chuỗi JSON để chứa các lời mời
//                StringBuilder json = new StringBuilder("[");
//                
//                // Kiểm tra lời mời dành cho người dùng hiện tại
//                boolean first = true; // Biến kiểm tra có phải là phần tử đầu tiên không
//                for (Map.Entry<String, String> invitation : invitations) {
//                    if (invitation.getValue().equals("itachi")) {
//                        if (!first) {
//                            json.append(", "); // Thêm dấu phẩy giữa các phần tử
//                        }
//                        json.append("{")
//                            .append("\"from\": \"").append(invitation.getKey()).append("\"")
//                            .append("}");
//                        first = false; // Đánh dấu phần tử đầu tiên đã được thêm
//                    }
//                }
//
//                json.append("]"); // Kết thúc mảng JSON
//                // Gửi dữ liệu JSON qua SSE
//                out.write("data: " + json.toString() + "\n\n");
//                out.flush();
		
		 Map<String, Integer> resultMap = BuyingSessionsHasProductsDAO.getInstance().getStatic(10);
		System.out.println("Danh sách sản phẩm và số lượng mua:");
        for (Map.Entry<String, Integer> entry : resultMap.entrySet()) {
            String productName = entry.getKey();
            Integer quantity = entry.getValue();
            System.out.println("Sản phẩm: " + productName + ", Số lượng: " + quantity);
        }
	}

}
