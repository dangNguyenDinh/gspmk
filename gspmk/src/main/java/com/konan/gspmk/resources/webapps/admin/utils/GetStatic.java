/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */

package com.konan.gspmk.resources.webapps.admin.utils;

import com.konan.gspmk.resources.hibernate.DAO.BuyingSessionsHasProductsDAO;
import java.io.IOException;
import java.io.PrintWriter;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.util.Map;

/**
 *
 * @author vdang
 */
@WebServlet(name="GetStatic", urlPatterns={"/admin/utils/GetStatic"})
public class GetStatic extends HttpServlet {
     @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        int so_lieu = Integer.parseInt(request.getParameter("so_lieu"));
		// Lấy dữ liệu từ DAO
        Map<String, Integer> resultMap = BuyingSessionsHasProductsDAO.getInstance().getStatic(so_lieu);

        // Tạo chuỗi JSON thủ công
        StringBuilder jsonBuilder = new StringBuilder();
        jsonBuilder.append("{");

        int count = 0;
        for (Map.Entry<String, Integer> entry : resultMap.entrySet()) {
            jsonBuilder.append("\"").append(entry.getKey()).append("\": ").append(entry.getValue());
            count++;
            if (count < resultMap.size()) {
                jsonBuilder.append(", ");
            }
        }

        jsonBuilder.append("}");

        // Cấu hình response
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");

        // Ghi JSON vào response
        PrintWriter out = response.getWriter();
        out.print(jsonBuilder.toString());
        out.flush();
    }



}
