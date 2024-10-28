/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Filter.java to edit this template
 */
package com.konan.gspmk.resources.filter;

import com.konan.gspmk.resources.javabeans.Users;
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
 * @author vdang không cho phép người dùng truy cập khi chưa đăng nhập, điều
 * hướng lại login
 */
@WebFilter(filterName = "CheckLoginFilter", urlPatterns = {"/user/*", "/admin/*"})
public class CheckLoginFilter implements Filter {

	@Override
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
			throws IOException, ServletException {
		HttpServletRequest req = (HttpServletRequest) request;
		HttpServletResponse res = (HttpServletResponse) response;
		HttpSession session = req.getSession(false);

		// Kiểm tra nếu session không tồn tại hoặc không có thông tin đăng nhập
		if (session == null || session.getAttribute("user") == null) {
			// Chuyển hướng về trang đăng nhập
			res.sendRedirect(req.getContextPath() + "/login");
			return;
		}

		// Nếu đã đăng nhập
		Users user = (Users) session.getAttribute("user");
		boolean isAdmin = user.getIsAdmin();
		String servletPath = req.getServletPath();

		// Nếu là admin và truy cập /user, chuyển hướng tới trang admin
		if (isAdmin && servletPath.startsWith("/user")) {
			res.sendRedirect(req.getContextPath() + "/admin/home");
			return;
		}

		// Nếu không phải admin và truy cập /admin, chuyển hướng tới trang user
		if (!isAdmin && servletPath.startsWith("/admin")) {
			res.sendRedirect(req.getContextPath() + "/user/home");
			return;
		}

		// Cho phép request tiếp tục nếu không vi phạm quy tắc
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
