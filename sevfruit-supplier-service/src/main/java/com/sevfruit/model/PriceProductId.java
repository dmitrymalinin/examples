package com.sevfruit.model;

import java.io.Serializable;
import java.util.Objects;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;

/**
 * Составной ID для {@linkplain PriceProduct}
 * @author dm
 *
 */
@Embeddable
public class PriceProductId implements Serializable {
	private static final long serialVersionUID = 1L;

	@Column(name = "price_id")
	private int price_id;
	
	@Column(name = "product_id")
	private int product_id;

		
	public PriceProductId() {
		super();
	}

	public PriceProductId(int price_id, int product_id) {
		super();
		this.price_id = price_id;
		this.product_id = product_id;
	}

	
	public int getPrice_id() {
		return price_id;
	}

	public void setPrice_id(int price_id) {
		this.price_id = price_id;
	}

	public int getProduct_id() {
		return product_id;
	}

	public void setProduct_id(int product_id) {
		this.product_id = product_id;
	}

	@Override
	public int hashCode() {
		return Objects.hash(price_id, product_id);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		PriceProductId other = (PriceProductId) obj;
		return Objects.equals(price_id, other.price_id) && Objects.equals(product_id, other.product_id);
	}
	
	
}
