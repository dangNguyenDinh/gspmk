/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package com.konan.gspmk.resources.hibernate.DAO;

import com.konan.gspmk.resources.hibernate.HibernateUtils;
import java.util.List;
import org.hibernate.SessionFactory;

/**
 *
 * @author vdang
 * @param <Obj>
 */
public interface DAO<Obj> {
	SessionFactory sessionFactory = HibernateUtils.getFactory();
	public List<Obj> getAll();
	public int add(Obj obj);
	public int delete(String username);
}
