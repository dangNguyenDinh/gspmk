/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Filter.java to edit this template
 */
package com.konan.gspmk.resources.filter;

import java.io.IOException;
import java.io.PrintStream;
import java.io.PrintWriter;
import java.io.StringWriter;
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

/**
 *
 * @author vdang không cho phép người dùng truy cập vào validate
 */
@WebFilter(filterName = "PreventAccessServerContent", urlPatterns = {"/validate/*"})
public class PreventAccessServerContent implements Filter {

	@Override
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
			throws IOException, ServletException {
		HttpServletRequest req = (HttpServletRequest) request;
		HttpServletResponse res = (HttpServletResponse) response;

		// Kiểm tra nếu yêu cầu này là một forward hoặc include
		String forwardHeader = req.getHeader("referer");

		// Nếu không có referer hoặc không phải là yêu cầu bên trong ứng dụng, chặn truy cập
		if (forwardHeader == null || !forwardHeader.contains(req.getContextPath())) {
			res.sendRedirect(req.getContextPath() + "/error/403.jsp");
			return;
		}

		// Yêu cầu hợp lệ, cho phép tiếp tục
		chain.doFilter(request, response);
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
