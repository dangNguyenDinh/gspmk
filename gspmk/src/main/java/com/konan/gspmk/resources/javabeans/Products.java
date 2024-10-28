/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.konan.gspmk.resources.javabeans;

import jakarta.persistence.*;
import java.util.*;

// Đánh dấu class là một thực thể JPA
@Entity
@Table(name = "Products")
public class Products {

    // Các thuộc tính tương ứng với các cột trong bảng Products

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Integer id;

    @Column(name = "name", nullable = false, length = 100)
    private String name;

    @Column(name = "price", nullable = false)
    private Double price;

    @Column(name = "type", nullable = false, length = 50)
    private String type;

    @Column(name = "description", length = 255)
    private String description;

    @Column(name = "imageName", nullable = false, columnDefinition = "LONGTEXT")
    private String imageName; // Đổi từ base64String thành imageName

	@Column(name = "sha", nullable = false, columnDefinition = "LONGTEXT")
    private String sha; // Đổi từ base64String thành imageName
	
    @Column(name = "readyState", nullable = false)
    private boolean readyState = true;

	@ManyToMany(mappedBy = "products")
    private Set<BuyingSessions> buyingSessions = new HashSet<>();
	
    // Constructor mặc định
    public Products() {
    }

	public Products(String name, Double price, String type, String description, String imageName, String sha, boolean readyState) {
        this.name = name;
        this.price = price;
        this.type = type;
        this.description = description;
        this.imageName = imageName; 
		this.sha = sha;
        this.readyState = readyState;
    }
	
    // Constructor có tham số
    public Products(Integer id, String name, Double price, String type, String description, String imageName, String sha, boolean readyState) {
        this.id = id;
        this.name = name;
        this.price = price;
        this.type = type;
        this.description = description;
        this.imageName = imageName; 
		this.sha = sha;
        this.readyState = readyState;
    }

    // Các phương thức getter và setter
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getImageName() { // Cập nhật tên phương thức cho imageName
        return imageName;
    }

    public void setImageName(String imageName) { // Cập nhật tên phương thức cho imageName
        this.imageName = imageName;
    }

    public boolean isReadyState() {
        return readyState;
    }

    public void setReadyState(boolean readyState) {
        this.readyState = readyState;
    }

	public String getSha() {
		return sha;
	}

	public void setSha(String sha) {
		this.sha = sha;
	}

	
}
