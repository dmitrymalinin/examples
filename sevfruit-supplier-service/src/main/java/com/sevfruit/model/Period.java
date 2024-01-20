package com.sevfruit.model;

import static com.sevfruit.service.SupplierServiceApplication.DB_SCHEMA_NAME;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

/**
 * Период поставок
 * @author dm
 *
 */
@Entity
@Table(name = "PERIOD", schema = DB_SCHEMA_NAME)
public class Period {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id")
	private Integer id;
	
	@Column(name = "name", length = 50, nullable = false, unique = true)
	private String name;

	public Period() {
		super();
	}

	/**
	 * ctor для десериализации
	 * @param id
	 */
	public Period(Integer id) {
		super();
		this.id = id;
	}

	public Period(String name) {
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
		return "Period [id=" + id + ", name=" + name + "]";
	}
	
	
}
