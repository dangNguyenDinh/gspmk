/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.konan.gspmk.resources.javabeans;

import jakarta.persistence.*;
import java.util.*;

/**
 *
 * @author vdang
 */
@Entity
@Table(name = "Users")
public class Users {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "username", nullable = false, unique = true, length = 50)
    private String username;

    @Column(name = "password", nullable = false, length = 50)
    private String password;

    @Column(name = "buyingSessionId")
    private Integer buyingSessionId; // Người dùng có thể không tham gia phiên nào (NULL)

    @Column(name = "isAdmin")
    private Boolean isAdmin = false;

    // Liên kết với BuyingSessions thông qua bảng UsersInBuyingSessions
    @ManyToMany
    @JoinTable(
        name = "UsersInBuyingSessions",
        joinColumns = @JoinColumn(name = "UsersID"),
        inverseJoinColumns = @JoinColumn(name = "BuyingSessionsID")
    )
    private Set<BuyingSessions> buyingSessions = new HashSet<>();

    // Phương thức getter/setter
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Integer getBuyingSessionId() {
        return buyingSessionId;
    }

    public void setBuyingSessionId(Integer buyingSessionId) {
        this.buyingSessionId = buyingSessionId;
    }


    public Boolean getIsAdmin() {
        return isAdmin;
    }

    public void setIsAdmin(Boolean isAdmin) {
        this.isAdmin = isAdmin;
    }

    public Set<BuyingSessions> getBuyingSessions() {
        return buyingSessions;
    }

    public void setBuyingSessions(Set<BuyingSessions> buyingSessions) {
        this.buyingSessions = buyingSessions;
    }

	public Users() {
	}

	public Users(String username, String password, Integer buyingSessionId, boolean isAdmin) {
		this.username = username;
		this.password = password;
		this.buyingSessionId = buyingSessionId;
		this.isAdmin = isAdmin;
	}
	
}