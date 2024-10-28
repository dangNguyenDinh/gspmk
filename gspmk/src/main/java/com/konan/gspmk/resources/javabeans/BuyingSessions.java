/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.konan.gspmk.resources.javabeans;

import jakarta.persistence.*;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 *
 * @author vdang
 */


@Entity
@Table(name = "BuyingSessions")
public class BuyingSessions {

    @Id
    private Integer id;

    @Column(name = "organizerId", nullable = false, length = 50)
    private String organizerId;
	
	@Column(name = "onGoing", nullable = false)
    private boolean onGoing = true;

    @Column(name = "startTime", nullable = false)
    private LocalDateTime startTime; // Sử dụng LocalDateTime

    @Column(name = "endTime")
    private LocalDateTime endTime; // Có thể NULL khi phiên đang diễn ra

    // Liên kết với Users thông qua bảng UsersInBuyingSessions
    @ManyToMany(mappedBy = "buyingSessions")
    private Set<Users> users = new HashSet<>();
	
	@ManyToMany
    @JoinTable(
        name = "BuyingSessionsHasProducts",
        joinColumns = @JoinColumn(name = "SessionID"),
        inverseJoinColumns = @JoinColumn(name = "ProductID")
    )
    private Set<Products> products = new HashSet<>();

    // Phương thức getter/setter
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getOrganizerId() {
        return organizerId;
    }

    public void setOrganizerId(String organizerId) {
        this.organizerId = organizerId;
    }

    public LocalDateTime getStartTime() {
        return startTime;
    }

    public void setStartTime(LocalDateTime startTime) {
        this.startTime = startTime;
    }

    public LocalDateTime getEndTime() {
        return endTime;
    }

    public void setEndTime(LocalDateTime endTime) {
        this.endTime = endTime;
    }

    public Set<Users> getUsers() {
        return users;
    }

    public void setUsers(Set<Users> users) {
        this.users = users;
    }

	public BuyingSessions(Integer id, String organizerId, LocalDateTime startTime, LocalDateTime endTime) {
		this.id = id;
		this.organizerId = organizerId;
		this.startTime = startTime;
		this.endTime = endTime;
		this.onGoing = false;
	}

	public BuyingSessions() {
	}

	public boolean isOnGoing() {
		return onGoing;
	}

	public Set<Products> getProducts() {
		return products;
	}

	public void setOnGoing(boolean onGoing) {
		this.onGoing = onGoing;
	}

	public void setProducts(Set<Products> products) {
		this.products = products;
	}
	
	
}