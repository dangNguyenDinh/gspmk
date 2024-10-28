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

/**
 *
 * @author vdang
 */
@WebServlet(name = "AddProducts", urlPatterns = {"/admin/utils/AddProducts"})
public class AddProducts extends HttpServlet {

	@Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        // Thiết lập bộ mã hóa UTF-8
        request.setCharacterEncoding("UTF-8");
        
        // Lấy dữ liệu từ form
        String name = request.getParameter("name");
        String type = request.getParameter("type");
        String description = request.getParameter("description");
		Double price = Double.valueOf(request.getParameter("price"));
        boolean readyState = Boolean.parseBoolean(request.getParameter("readyState"));
        String imageName = request.getParameter("imageName");
        String sha = request.getParameter("sha");
		

        // Tạo đối tượng sản phẩm mới
        Products prd = new Products(name, price, type, description, imageName, sha, readyState);

        // Lưu sản phẩm vào cơ sở dữ liệu
        ProductsDAO.getInstance().add(prd);

        // Phản hồi lại cho client (nếu cần)
        response.setContentType("text/plain");
        response.setCharacterEncoding("UTF-8");
        response.getWriter().write("Sản phẩm đã được thêm thành công.");
    }

}
