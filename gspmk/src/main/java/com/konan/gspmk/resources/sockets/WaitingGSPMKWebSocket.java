/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.konan.gspmk.resources.sockets;

import jakarta.websocket.server.*;
import jakarta.websocket.OnClose;
import jakarta.websocket.OnMessage;
import jakarta.websocket.OnOpen;
import jakarta.websocket.Session;
import jakarta.websocket.server.ServerEndpoint;

import java.io.IOException;
import java.util.HashSet;
import java.util.Set;

/**
 *
 * @author vdang
 */

@ServerEndpoint("/sockets/socket")
public class WaitingGSPMKWebSocket {

    private static Set<Session> clients = new HashSet<>();

    @OnOpen
    public void onOpen(Session session) {
        clients.add(session);
        System.out.println("Client đã kết nối: " + session.getId());
    }

    @OnMessage
    public void onMessage(String message, Session session) throws IOException {
        if ("start_gspmk".equals(message)) {
            // Khi nhận được yêu cầu từ một client, thông báo cho tất cả client
            for (Session client : clients) {
                client.getBasicRemote().sendText("redirect");
            }
        }
    }

    @OnClose
    public void onClose(Session session) {
        clients.remove(session);
        System.out.println("Client đã ngắt kết nối: " + session.getId());
    }
}
