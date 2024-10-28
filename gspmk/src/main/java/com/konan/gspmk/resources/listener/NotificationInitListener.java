/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.konan.gspmk.resources.listener;

import jakarta.servlet.ServletContext;
import jakarta.servlet.ServletContextEvent;
import jakarta.servlet.ServletContextListener;
import jakarta.servlet.annotation.WebListener;
import java.util.*;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/**
 *
 * @author vdang
 */
@WebListener
public class NotificationInitListener implements ServletContextListener {
	//tạo trình lập lịch bất đồng bộ để xóa đi thông báo mỗi 1p nó được thêm vào. cái này nên được
	//đặt trong một method static riêng.
	private static final ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);
	
	@Override
    public void contextInitialized(ServletContextEvent sce) {
		// Khởi tạo List để lưu lời mời <người mời, người được mời>
        List<Map.Entry<String, String>> invitations = new ArrayList<>();
        sce.getServletContext().setAttribute("invitations", invitations);
	}
	
	 @Override
    public void contextDestroyed(ServletContextEvent sce) {
        // Tắt scheduler khi ServletContext bị hủy
        if (scheduler != null && !scheduler.isShutdown()) {
            scheduler.shutdownNow();
            System.out.println("Scheduler đã được tắt.");
        }
    }
	
	
	public static void addInvitation(ServletContext context, String inviter, String invitee) {
        List<Map.Entry<String, String>> invitations = (List<Map.Entry<String, String>>) context.getAttribute("invitations");
        
        // Tạo entry mới và thêm vào List
        Map.Entry<String, String> newInvitation = new AbstractMap.SimpleEntry<>(inviter, invitee);
        invitations.add(newInvitation);
		context.setAttribute("invitations", invitations);
		
        // Lập lịch để xóa lời mời sau 60 giây
        scheduler.schedule(() -> {
            invitations.remove(newInvitation);
			context.setAttribute("invitations", invitations);
        }, 30, TimeUnit.SECONDS); //tạm thời cho thông báo 60p
    }
}


