/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */

package com.konan.gspmk.resources.webapps.utils;

import com.konan.gspmk.resources.listener.NotificationInitListener;
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
@WebServlet(name="SendInvitation", urlPatterns={"/utils/SendInvitation"})
public class SendInvitation extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
    throws ServletException, IOException {
        String currentUser = request.getParameter("currentUser");
		String user = request.getParameter("user");
		NotificationInitListener.addInvitation(getServletContext(), currentUser, user);
		System.out.println("user " + user + " cur "+currentUser );
		response.getWriter().println("Sent invitation to " + user + " successfully!");
    }



}
