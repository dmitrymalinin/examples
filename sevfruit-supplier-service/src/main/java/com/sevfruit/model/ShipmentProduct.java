package com.sevfruit.model;

import org.springframework.lang.NonNull;

import jakarta.persistence.Column;
import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinColumns;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

/**
 * Позиция в поставке
 * @author dm
 *
 */
@Entity
@Table(name = "SHIPMENT_PRODUCT")
public class ShipmentProduct {
	@EmbeddedId
	private ShipmentProductId id;
	
	@JoinColumns({
		@JoinColumn(name = "shipment_id", referencedColumnName = "id", nullable = false, insertable = false, updatable = false),
		@JoinColumn(name = "price_id", referencedColumnName = "price_id", nullable = false, insertable = false, updatable = false)})
	@ManyToOne(optional = false)
	private Shipment shipment;
	
	@JoinColumns({
		@JoinColumn(name = "price_id", referencedColumnName = "price_id", nullable = false, insertable = false, updatable = false),
		@JoinColumn(name = "product_id", referencedColumnName = "product_id", nullable = false, insertable = false, updatable = false)})
	@ManyToOne(optional = false)
	private PriceProduct priceProduct;
	
	/** кол-во в тоннах */
	@Column(name = "quantity", nullable = false)
	private Float quantity;

	public ShipmentProduct() {
		super();
	}

	public ShipmentProduct(@NonNull Shipment shipment, @NonNull PriceProduct priceProduct, @NonNull Float quantity) {
		super();
		this.id = new ShipmentProductId(shipment.getId(), priceProduct.getId().getPrice_id(), priceProduct.getId().getProduct_id());
		this.priceProduct = priceProduct;
		this.quantity = quantity;
	}
	
	
	public ShipmentProductId getId() {
		return id;
	}

	public void setId(ShipmentProductId id) {
		this.id = id;
	}

	public PriceProduct getPriceProduct() {
		return priceProduct;
	}

	public void setPriceProduct(PriceProduct priceProduct) {
		this.priceProduct = priceProduct;
	}

	public Float getQuantity() {
		return quantity;
	}

	public void setQuantity(Float quantity) {
		this.quantity = quantity;
	}
	
	
}

