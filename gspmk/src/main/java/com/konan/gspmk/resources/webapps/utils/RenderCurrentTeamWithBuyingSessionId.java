/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */
package com.konan.gspmk.resources.webapps.utils;

import com.konan.gspmk.resources.hibernate.DAO.BuyingSessionsDAO;
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
@WebServlet(name = "RenderCurrentTeamWithBuyingSessionId", urlPatterns = {"/utils/RenderCurrentTeamWithBuyingSessionId"})
public class RenderCurrentTeamWithBuyingSessionId extends HttpServlet {

	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		String currentUser = request.getParameter("currentUser");
		StringBuilder result = new StringBuilder("Your team: ");
		Users cru = UsersDAO.getInstance().getUserByUsername(currentUser);
		int currentBuyingSessionId = -1;
		if (cru != null) {
			if (cru.getBuyingSessionId() != null) {
				currentBuyingSessionId = cru.getBuyingSessionId();
			}
		}

		List<Users> currentTeam = UsersDAO.getInstance().getUsersByBuyingSessionId(currentBuyingSessionId);

		for (Users u : currentTeam) {
			result.append(u.getUsername()).append(", ");
		}
		if (cru != null) {
			result.deleteCharAt(result.length() - 2);
		}
		response.getWriter().write(result.toString());
	}

}
