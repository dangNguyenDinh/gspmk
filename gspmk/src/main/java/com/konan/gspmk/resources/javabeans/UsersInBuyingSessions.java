/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.konan.gspmk.resources.javabeans;

import jakarta.persistence.*;

/**
 *
 * @author vdang
 */
@Entity
@Table(name = "UsersInBuyingSessions")
public class UsersInBuyingSessions {

    @Id
    @ManyToOne
    @JoinColumn(name = "UsersID", nullable = false) // Khóa ngoại trỏ tới bảng Users
    private Users user;

    @Id
    @ManyToOne
    @JoinColumn(name = "BuyingSessionsID", nullable = false) // Khóa ngoại trỏ tới bảng BuyingSessions
    private BuyingSessions buyingSession;

    // Phương thức getter/setter
    public Users getUser() {
        return user;
    }

    public void setUser(Users user) {
        this.user = user;
    }

    public BuyingSessions getBuyingSession() {
        return buyingSession;
    }

    public void setBuyingSession(BuyingSessions buyingSession) {
        this.buyingSession = buyingSession;
    }
}
