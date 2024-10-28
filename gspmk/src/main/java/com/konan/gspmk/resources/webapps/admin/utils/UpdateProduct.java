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
import jakarta.servlet.annotation.MultipartConfig;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.Part;
import java.io.File;

/**
 *
 * @author vdang
 */

@WebServlet(name="UpdateProduct", urlPatterns={"/admin/utils/UpdateProduct"})
public class UpdateProduct extends HttpServlet {

     @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        // Thiết lập kiểu phản hồi
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        
        // Lấy các tham số từ request
        String id = request.getParameter("id"); // Lấy ID sản phẩm
        String name = request.getParameter("name");
        String price = request.getParameter("price");
        String type = request.getParameter("type");
        String description = request.getParameter("description");
        String readyState = request.getParameter("readyState");
        
		 System.out.println(id + " " + name + " "+ price);
		
		
        // Chuyển đổi price và readyState sang kiểu dữ liệu tương ứng
        double priceValue = Double.parseDouble(price);
        boolean readyStateValue = "1".equals(readyState); // nếu giá trị là 1 thì true, ngược lại false
        
        // Tạo đối tượng Products và cập nhật vào cơ sở dữ liệu
        Products product = new Products(); // Thay đổi đây để có thể khởi tạo với id
        product.setId(Integer.valueOf(id)); // Gán ID từ request
        product.setName(name);
        product.setPrice(priceValue);
        product.setType(type);
        product.setDescription(description);
        product.setReadyState(readyStateValue);
        
        // Cập nhật sản phẩm trong cơ sở dữ liệu
        ProductsDAO.getInstance().updateWithoutImage(product.getId(), product);
        
        // Trả về phản hồi thành công
        response.getWriter().write("{\"message\": \"Cập nhật sản phẩm thành công!\"}");
    }
}
