/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */

package com.konan.gspmk.resources.webapps.user;

import com.konan.gspmk.resources.hibernate.DAO.UsersDAO;
import com.konan.gspmk.resources.javabeans.Users;
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
@WebServlet(name="PrepareGSPMK", urlPatterns={"/user/prepareGSPMK"})
public class PrepareGSPMK extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
    throws ServletException, IOException {
		//cập nhật lại phần sessionScope.user.buyingSessionId.
		String username = ((Users)request.getSession().getAttribute("user")).getUsername();
		Users userToChange = UsersDAO.getInstance().getUserByUsername(username);
		request.getSession().setAttribute("user", userToChange);
        request.getRequestDispatcher("/WEB-INF/webviews/user/preparegspmk.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
    throws ServletException, IOException {
        
    }



}



