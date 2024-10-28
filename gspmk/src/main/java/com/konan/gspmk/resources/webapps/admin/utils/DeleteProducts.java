/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */

package com.konan.gspmk.resources.webapps.admin.utils;

import com.konan.gspmk.resources.hibernate.DAO.ProductsDAO;
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
@WebServlet(name="DeleteProducts", urlPatterns={"/admin/utils/DeleteProducts"})
public class DeleteProducts extends HttpServlet {
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
    throws ServletException, IOException {
        int id = Integer.parseInt(request.getParameter("id"));
		System.out.println(id);
		String sha = ProductsDAO.getInstance().getProductsById(id).getSha();
		int res = ProductsDAO.getInstance().delete(id);
		
		//gửi sha lên cho js xóa trong github
		response.getWriter().write(sha);
    }



}
