/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */

package com.konan.gspmk.resources.webapps.admin.utils;

import com.konan.gspmk.resources.hibernate.DAO.UsersDAO;
import com.konan.gspmk.resources.javabeans.Users;
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
@WebServlet(name="RenderAllUser", urlPatterns={"/admin/utils/RenderAllUser"})
public class RenderAllUser extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        // Lấy danh sách users từ DAO
        UsersDAO usersDAO = UsersDAO.getInstance();
        List<Users> usersList = usersDAO.getAll();

        // Tạo JSON từ danh sách users
        StringBuilder jsonBuilder = new StringBuilder();
        jsonBuilder.append("[");
        for (int i = 0; i < usersList.size(); i++) {
            Users user = usersList.get(i);
            jsonBuilder.append("{");
            jsonBuilder.append("\"id\":").append(user.getId()).append(",");
            jsonBuilder.append("\"username\":\"").append(user.getUsername()).append("\",");
            jsonBuilder.append("\"password\":\"").append(user.getPassword()).append("\",");
            jsonBuilder.append("\"buyingSessionId\":").append(user.getBuyingSessionId() != null ? user.getBuyingSessionId() : "null").append(",");
            jsonBuilder.append("\"isAdmin\":").append(user.getIsAdmin());
            jsonBuilder.append("}");
            if (i < usersList.size() - 1) {
                jsonBuilder.append(",");
            }
        }
        jsonBuilder.append("]");

        // Thiết lập kiểu nội dung và trả về chuỗi JSON
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        response.getWriter().write(jsonBuilder.toString());
    }




}
