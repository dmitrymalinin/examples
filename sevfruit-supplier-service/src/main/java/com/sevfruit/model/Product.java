package com.sevfruit.model;

import static com.sevfruit.service.SupplierServiceApplication.DB_SCHEMA_NAME;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

/**
 * Вид продукции
 * @author dm
 *
 */
@Entity
@Table(name = "PRODUCT", schema = DB_SCHEMA_NAME)
public class Product {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id")
	private Integer id;
	
	@Column(name = "name", length = 50, nullable = false, unique = true)
	private String name;

	public Product() {
		super();
	}

	public Product(String name) {
		super();
		this.name = name;
	}
	
	/**
	 * ctor для десериализации
	 * @param id
	 */
	public Product(Integer id) {
		super();
		this.id = id;
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
		return "Product [id=" + id + ", name=" + name + "]";
	}
	
	
}
