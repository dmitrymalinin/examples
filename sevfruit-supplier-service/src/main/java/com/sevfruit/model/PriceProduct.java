package com.sevfruit.model;

import org.springframework.lang.NonNull;

import jakarta.persistence.Column;
import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.MapsId;
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
	private PriceProductId id = new PriceProductId();
	
	@JoinColumn(name = "price_id", referencedColumnName = "id", nullable = false, insertable = false, updatable = false)
	@ManyToOne(optional = false)
	@MapsId("price_id")
	private Price price;
	
	@JoinColumn(name = "product_id", referencedColumnName = "id", nullable = false, insertable = false, updatable = false)
	@ManyToOne(optional = false)
	@MapsId("product_id")
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
	
	public PriceProduct(int price_id, int product_id, @NonNull Float value) {
		super();
		this.id = new PriceProductId(price_id, product_id);
		this.value = value;
	}

	public PriceProduct(@NonNull Price price, @NonNull Product product, @NonNull Float value) {
		super();
		this.price = price;
		this.product = product;
		this.value = value;
	}

	public PriceProductId getId() {
		return id;
	}

	public void setId(PriceProductId id) {
		this.id = id;
	}
	
	public void setPrice(Price price) {
		this.price = price;
	}

	public Product getProduct() {
		return product;
	}

	public void setProduct(Product product) {
		this.product = product;
	}
	
	public void setProduct_id(int product_id) {
		this.product = new Product(product_id);
	}

	public Float getValue() {
		return value;
	}

	public void setValue(Float value) {
		this.value = value;
	}

	@Override
	public String toString() {
		return "PriceProduct [id=" + id + ", price_id=" + (price!=null?price.getId():null) + ", product=" + product + ", value=" + value + "]";
	}
	
}
