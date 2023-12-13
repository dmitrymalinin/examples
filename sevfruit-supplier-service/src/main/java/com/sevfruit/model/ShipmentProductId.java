package com.sevfruit.model;

import java.io.Serializable;
import java.util.Objects;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;

/**
 * ID для {@linkplain ShipmentProduct}
 * @author dm
 *
 */
@Embeddable
public class ShipmentProductId implements Serializable {

	private static final long serialVersionUID = 1L;
	
	@Column(name = "shipment_id")
	private int shipment_id;
	
	@Column(name = "price_id")
	private int price_id;
	
	@Column(name = "product_id")
	private int product_id;
	
	public ShipmentProductId() {
		super();
	}

	public ShipmentProductId(int shipment_id, int price_id, int product_id) {
		super();
		this.shipment_id = shipment_id;
		this.price_id = price_id;
		this.product_id = product_id;
	}
	
	

	public int getShipment_id() {
		return shipment_id;
	}

	public void setShipment_id(int shipment_id) {
		this.shipment_id = shipment_id;
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
		return Objects.hash(price_id, product_id, shipment_id);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		ShipmentProductId other = (ShipmentProductId) obj;
		return price_id == other.price_id && product_id == other.product_id && shipment_id == other.shipment_id;
	}
	
	
}
