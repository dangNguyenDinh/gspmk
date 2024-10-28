/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */
package com.konan.gspmk.resources.webapps.utils;

import com.konan.gspmk.resources.listener.NotificationInitListener;
import jakarta.servlet.ServletContext;
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
@WebServlet(name = "RenderNotification", urlPatterns = {"/utils/RenderNotification"})
public class RenderNotification extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/event-stream");
        response.setCharacterEncoding("UTF-8");

        ServletContext context = getServletContext();
        List<Map.Entry<String, String>> invitations = (List<Map.Entry<String, String>>) context.getAttribute("invitations");
        if (invitations == null) {
            invitations = new ArrayList<>();
            context.setAttribute("invitations", invitations); // Đảm bảo rằng invitations được lưu trữ
        }

        String currentUser = request.getParameter("currentUser");
        try (PrintWriter out = response.getWriter()) {
            while (true) {
                // Tạo chuỗi JSON để chứa các lời mời
                StringBuilder json = new StringBuilder("[");
                
                // Kiểm tra lời mời dành cho người dùng hiện tại
                boolean first = true; // Biến kiểm tra có phải là phần tử đầu tiên không
                for (Map.Entry<String, String> invitation : invitations) {
                    if (invitation.getValue().equals(currentUser)) {
                        if (!first) {
                            json.append(", "); // Thêm dấu phẩy giữa các phần tử
                        }
                        json.append("{")
                            .append("\"from\": \"").append(invitation.getKey()).append("\"")
                            .append("}");
                        first = false; // Đánh dấu phần tử đầu tiên đã được thêm
                    }
                }

                json.append("]"); // Kết thúc mảng JSON
                // Gửi dữ liệu JSON qua SSE
                out.write("data: " + json.toString() + "\n\n");
                out.flush();


                // Ngủ một lúc trước khi kiểm tra tiếp
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                    // Nếu bị gián đoạn, thoát vòng lặp
                    break;
                }
            }
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        // Xử lý yêu cầu POST nếu cần
    }
}
