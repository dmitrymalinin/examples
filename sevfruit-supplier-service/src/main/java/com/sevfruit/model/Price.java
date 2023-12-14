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
 * Прайс лист
 * @author dm
 *
 */
@Entity
@Table(name = "PRICE", uniqueConstraints = {@UniqueConstraint(columnNames = {"supplier_id", "period_id"})})
public class Price {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id")
	private Integer id;
	
	@JoinColumn(name = "supplier_id", referencedColumnName = "id", nullable = false)
	@ManyToOne(optional = false)
	private Supplier supplier;
	
	@JoinColumn(name = "period_id", referencedColumnName = "id", nullable = false)
	@ManyToOne(optional = false)
	private Period period;

	/** Список продукции с ценами */
	@OneToMany(mappedBy = "price", fetch = FetchType.LAZY)
	private List<PriceProduct> products;

	public Price() {
		super();
	}

	public Price(Supplier supplier, Period period) {
		super();
		this.supplier = supplier;
		this.period = period;
	}
	
	public Price(int supplier_id, int period_id) {
		super();
		this.supplier = new Supplier(supplier_id);
		this.period = new Period(period_id);
	}
	
	public Price(int id) {
		super();
		this.id = id;
	}
		
	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Supplier getSupplier() {
		return supplier;
	}

	public void setSupplier(Supplier supplier) {
		this.supplier = supplier;
	}

	public Period getPeriod() {
		return period;
	}

	public void setPeriod(Period period) {
		this.period = period;
	}

	public List<PriceProduct> getProducts() {
		return products;
	}

	public void setProducts(List<PriceProduct> products) {
		this.products = products;
	}
	
	
}
