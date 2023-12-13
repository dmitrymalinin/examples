package com.sevfruit.model;

import org.springframework.lang.NonNull;

import jakarta.persistence.Column;
import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

/**
 * Позиция в прайс листе
 * @author dm
 *
 */
@Entity
@Table(name = "PRICE_PRODUCT")
public class PriceProduct {
	@EmbeddedId
	private PriceProductId id;
	
	@JoinColumn(name = "product_id", referencedColumnName = "id", nullable = false, insertable = false, updatable = false)
	@ManyToOne(optional = false)	
	private Product product;
	
	/** Цена за тонну */
	@Column(name = "value", nullable = false)
	private Float value;

	public PriceProduct() {
		super();
	}
	
	public PriceProduct(int price_id, int product_id) {
		super();
		this.id = new PriceProductId(price_id, product_id);
	}
	
	public PriceProduct(int product_id) {
		super();
		this.id = new PriceProductId(0, product_id);
	}
	
	public PriceProduct(int price_id, int product_id, @NonNull Float value) {
		super();
		this.id = new PriceProductId(price_id, product_id);
		this.value = value;
	}

	public PriceProduct(@NonNull Price price, @NonNull Product product, @NonNull Float value) {
		super();
		this.id = new PriceProductId(price.getId(), product.getId());
		this.value = value;
	}

	public PriceProductId getId() {
		return id;
	}

	public void setId(PriceProductId id) {
		this.id = id;
	}


	public Product getProduct() {
		return product;
	}

	public void setProduct(Product product) {
		this.product = product;
	}

	public Float getValue() {
		return value;
	}

	public void setValue(Float value) {
		this.value = value;
	}


	@Override
	public String toString() {
		return "PriceProduct [id=" + id + ", product=" + product + ", value=" + value + "]";
	}
	
}
