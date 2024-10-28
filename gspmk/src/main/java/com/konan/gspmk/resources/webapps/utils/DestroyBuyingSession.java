/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */
package com.konan.gspmk.resources.webapps.utils;

import com.konan.gspmk.resources.hibernate.DAO.BuyingSessionsDAO;
import com.konan.gspmk.resources.hibernate.DAO.UsersDAO;
import com.konan.gspmk.resources.javabeans.BuyingSessions;
import com.konan.gspmk.resources.javabeans.Users;
import jakarta.servlet.ServletContext;
import java.io.IOException;
import java.io.PrintWriter;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 *
 * @author vdang
 */
@WebServlet(name = "DestroyBuyingSession", urlPatterns = {"/utils/DestroyBuyingSession"})
public class DestroyBuyingSession extends HttpServlet {

	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String currentUser = request.getParameter("currentUser");
		Users currentUserObj = UsersDAO.getInstance().getUserByUsername(currentUser);
		if (currentUserObj != null) {
			Integer buyingSessionId = UsersDAO.getInstance().getUserByUsername(currentUser).getBuyingSessionId();
			if (buyingSessionId == null) {
				//ket thuc phien gspmk
				//cap nhat session, context
				//cap nhat session (moi nguoi deu cap nhat, ai cap nhat cua nguoi do)
				request.getSession().setAttribute("user", currentUserObj);
				//cap nhat context
				ServletContext sc = getServletContext();
				Map<String, Users> onlineMap = (Map<String, Users>) sc.getAttribute("OnlineMap");
				if (onlineMap == null) {
					onlineMap = new HashMap<>();
				}

				// Lưu thông tin người dùng vào OnlineMap
				onlineMap.put(currentUser, currentUserObj);
				sc.setAttribute("OnlineMap", onlineMap);
				//xóa tin nhắn phiên đó
				// Lấy ra Map lưu trữ tin nhắn từ ServletContext
				Map<Integer, List<Map.Entry<String, String>>> messages
						= (Map<Integer, List<Map.Entry<String, String>>>) getServletContext().getAttribute("messages");

				// Kiểm tra và khởi tạo nếu messages chưa tồn tại
				if (messages == null) {
					messages = new HashMap<>();
					getServletContext().setAttribute("messages", messages);
				}
				messages.remove(buyingSessionId);
				getServletContext().setAttribute("messages", messages);
			} else {
				//con phien
				BuyingSessions bss = BuyingSessionsDAO.getInstance(getServletContext()).getBuyingSessionsById(buyingSessionId);
				String leader = bss.getOrganizerId();
				if (currentUser.equals(leader)) {
					response.getWriter().write("true");
				} else {
					response.getWriter().write("false");
				}
			}
		}
	}

	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		String currentUser = request.getParameter("currentUser");
		LocalDateTime currentDateTime = LocalDateTime.now();

		Users u = UsersDAO.getInstance().getUserByUsername(currentUser);
		if (u != null) {
			int buyingSessionId = u.getBuyingSessionId();
			//nếu là leader thì thực hiện xóa tất cả cho database và xóa session, context cho mình.
			//nếu ko phải thì xóa session, context cho mình.
			BuyingSessions bss = BuyingSessionsDAO.getInstance(getServletContext()).getBuyingSessionsById(buyingSessionId);
			BuyingSessionsDAO.getInstance(getServletContext()).updateEndTimeBuyingSessionsById(buyingSessionId, currentDateTime);
			String leader = bss.getOrganizerId();
			if (currentUser.equals(leader)) {
				//leader
				List<Users> lu = UsersDAO.getInstance().getUsersByBuyingSessionId(buyingSessionId);
				//cap nhat database
				lu.forEach((user) -> {
					UsersDAO.getInstance().changeBuyingSessionOfUserByUsername(user.getUsername(), null);
				});
			}
			//cap nhat session (moi nguoi deu cap nhat, ai cap nhat cua nguoi do)
			Users currentUserObj = UsersDAO.getInstance().getUserByUsername(currentUser);
			request.getSession().setAttribute("user", currentUserObj);
			//cap nhat context
			ServletContext sc = getServletContext();
			Map<String, Users> onlineMap = (Map<String, Users>) sc.getAttribute("OnlineMap");
			if (onlineMap == null) {
				onlineMap = new HashMap<>();
			}

			// Lưu thông tin người dùng vào OnlineMap
			onlineMap.put(currentUser, currentUserObj);
			sc.setAttribute("OnlineMap", onlineMap);
		}
	}

}
