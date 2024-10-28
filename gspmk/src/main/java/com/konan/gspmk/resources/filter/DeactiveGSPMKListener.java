/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.konan.gspmk.resources.filter;

import com.konan.gspmk.resources.hibernate.DAO.BuyingSessionsDAO;
import com.konan.gspmk.resources.javabeans.BuyingSessions;
import com.konan.gspmk.resources.javabeans.Users;
import jakarta.servlet.Filter;
import jakarta.servlet.FilterChain;
import jakarta.servlet.FilterConfig;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.io.IOException;

/**
 *
 * @author vdang
 */
@WebFilter(filterName = "DeactiveGSPMKListener", urlPatterns = {"/user/WaitingForGSPMK"})
public class DeactiveGSPMKListener implements Filter {

	@Override
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
			throws IOException, ServletException {
		HttpServletRequest req = (HttpServletRequest) request;
		HttpServletResponse res = (HttpServletResponse) response;

		//lam sao de lay ra duoc buyingsession hien tai?
		Users u = (Users) req.getSession().getAttribute("user");
		//nếu ko có session nào hiện tại
		if (u.getBuyingSessionId() != null) {
			int buyingSessionId = u.getBuyingSessionId();
			BuyingSessions bss = BuyingSessionsDAO.getInstance(request.getServletContext()).getBuyingSessionsById(buyingSessionId);
			boolean onGoing = bss.isOnGoing();
			if (!onGoing) {
				res.sendRedirect(req.getContextPath() + "/user/home");
			} else {
				chain.doFilter(request, response);
			}
		} else {
			res.sendRedirect(req.getContextPath() + "/user/home");
		}

	}

	@Override
	public void init(FilterConfig filterConfig) throws ServletException {
		// Không cần thiết lập gì ở đây
	}

	@Override
	public void destroy() {
		// Không cần thực hiện hành động khi filter bị huỷ
	}
}
