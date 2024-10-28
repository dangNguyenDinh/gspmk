/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */

package com.konan.gspmk.resources.landing;

import jakarta.servlet.ServletContext;
import java.io.IOException;
import java.io.PrintWriter;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.Map;

/**
 *
 * @author vdang
 */
@WebServlet(name="landing", urlPatterns={"/landing"})
public class landing extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
    throws ServletException, IOException {
        response.getWriter().println("<h1>landing page</h1>");
		ServletContext sc = getServletContext();
		Map<String, Integer> OnlineMap = (Map<String, Integer>) sc.getAttribute("OnlineMap");
		if(OnlineMap == null){
			OnlineMap = new HashMap<>();
		}
		sc.setAttribute("OnlineMap", OnlineMap);
		request.getRequestDispatcher("landing.jsp").forward(request, response);
    } 

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
    throws ServletException, IOException {
        
    }



}
