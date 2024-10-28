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
@Table(name = "BuyingSessionsHasProducts")
public class BuyingSessionsHasProducts {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @ManyToOne
    @JoinColumn(name = "SessionID", nullable = false)
    private BuyingSessions buyingSession;

    @ManyToOne
    @JoinColumn(name = "ProductID", nullable = false)
    private Products product;

    // Getters and Setters
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public BuyingSessions getBuyingSession() {
        return buyingSession;
    }

    public void setBuyingSession(BuyingSessions buyingSession) {
        this.buyingSession = buyingSession;
    }

    public Products getProduct() {
        return product;
    }

    public void setProduct(Products product) {
        this.product = product;
    }

	public BuyingSessionsHasProducts(BuyingSessions buyingSession, Products product) {
		this.buyingSession = buyingSession;
		this.product = product;
	}
	
	
	
}

