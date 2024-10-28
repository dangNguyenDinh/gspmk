/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */

package com.konan.gspmk.resources.webapps.utils;

import com.konan.gspmk.resources.hibernate.DAO.BuyingSessionsDAO;
import com.konan.gspmk.resources.hibernate.DAO.UsersDAO;
import com.konan.gspmk.resources.javabeans.BuyingSessions;
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
@WebServlet(name="ActiveOnGoingInBuyingSession", urlPatterns={"/utils/ActiveOnGoingInBuyingSession"})
public class ActiveOnGoingInBuyingSession extends HttpServlet {
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
    throws ServletException, IOException {
        String currentUser = request.getParameter("currentUser");
		int buyingSessionId = UsersDAO.getInstance().getUserByUsername(currentUser).getBuyingSessionId();
		//đổi ongoing thành true
		BuyingSessionsDAO.getInstance(getServletContext()).activeOnGoingInBuyingSessionsById(buyingSessionId);
    }



}
