package com.sevfruit.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

/**
 * Поставщик
 * @author dm
 *
 */
@Entity
@Table(name = "SUPPLIER")
public class Supplier {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id")
	private Integer id;
	
	@Column(name = "name", length = 50, nullable = false, unique = true)
	private String name;

	public Supplier() {
		super();
	}

	/**
	 * ctor для десериализации
	 * @param id
	 */
	public Supplier(Integer id) {
		super();
		this.id = id;
	}

	public Supplier(String name) {
		super();
		this.name = name;
	}

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
	
	@Override
	public String toString() {
		return "Supplier [id=" + id + ", name=" + name + "]";
	}
}
