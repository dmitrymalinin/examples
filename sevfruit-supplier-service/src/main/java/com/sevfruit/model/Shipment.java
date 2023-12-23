package com.sevfruit.model;

import java.util.List;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;

/**
 * Поставка
 * @author dm
 *
 */
@Entity
@Table(name = "SHIPMENT", uniqueConstraints = {@UniqueConstraint(columnNames = {"id", "price_id"})})
public class Shipment {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id")
	private Integer id;
	
	@JoinColumn(name = "price_id",  referencedColumnName = "id", nullable = false)
	@ManyToOne(optional = false)
	private Price price;
	
	/** Список продукции в поставке */
	@OneToMany(mappedBy = "shipment", fetch = FetchType.LAZY)
	private List<ShipmentProduct> products;
	
	public Shipment() {
		super();
	}

	public Shipment(Integer id) {
		super();
		this.id = id;
	}

	public Shipment(Price price) {
		super();
		this.price = price;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}
	
	public Integer getPrice_id() {
		return price!=null?price.getId():null;
	}

	public void setPrice(Price price) {
		this.price = price;
	}

	public List<ShipmentProduct> getProducts() {
		return products;
	}

	public void setProducts(List<ShipmentProduct> products) {
		this.products = products;
	}

	@Override
	public String toString() {
		return "Shipment [id=" + id + ", price_id=" + (price != null?price.getId():null) + "]";
	}
	
}
