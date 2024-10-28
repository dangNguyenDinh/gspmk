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
@WebFilter(filterName = "PrepareGSPMKListener", urlPatterns = {"/user/prepareGSPMK"})
public class PrepareGSPMKListener implements Filter {

	@Override
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
			throws IOException, ServletException {
		HttpServletRequest req = (HttpServletRequest) request;
		HttpServletResponse res = (HttpServletResponse) response;
		HttpSession session = req.getSession(false);

		//lam sao de lay ra duoc buyingsession hien tai?
		Users u = (Users) req.getSession().getAttribute("user");
		int buyingSessionId = u.getBuyingSessionId();
		BuyingSessions bss = BuyingSessionsDAO.getInstance(request.getServletContext()).getBuyingSessionsById(buyingSessionId);
		
		if (bss != null) {
			boolean onGoing = bss.isOnGoing();
			if (onGoing) {
				res.sendRedirect(req.getContextPath() + "/user/WaitingForGSPMK");
			} else {
				chain.doFilter(request, response);
			}
		} else {
			chain.doFilter(request, response);
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
