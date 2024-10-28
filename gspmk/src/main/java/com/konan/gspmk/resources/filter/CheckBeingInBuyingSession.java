/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.konan.gspmk.resources.filter;

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
 * nếu thấy đang trong phiên mua bán thì chuyển về trang phiên mua bán /user/prepareGSPMK 
 */
@WebFilter(filterName = "CheckBeingInBuyingSession", urlPatterns = {"/user/home"})

public class CheckBeingInBuyingSession implements Filter {

	@Override
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
			throws IOException, ServletException {
		HttpServletRequest req = (HttpServletRequest) request;
		HttpServletResponse res = (HttpServletResponse) response;
		HttpSession session = req.getSession();

		if (session != null) {
			Users user = (Users) session.getAttribute("user");

			if (user != null && user.getBuyingSessionId() != null) {
				// Chuyển hướng về trang prepareGSPMK
				res.sendRedirect(req.getContextPath() + "/user/prepareGSPMK");
			} else {
				// Nếu không thì cho phép request tiếp tục
				chain.doFilter(request, response);
			}
		} else {
			// Nếu session là null, cho phép request tiếp tục
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
