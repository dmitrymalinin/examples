package com.sevfruit.model;

import java.util.List;

import org.springframework.lang.NonNull;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.fasterxml.jackson.annotation.JsonSetter;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.NamedAttributeNode;
import jakarta.persistence.NamedEntityGraph;
import jakarta.persistence.NamedSubgraph;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OrderBy;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;

/**
 * Поставка
 * @author dm
 *
 */
@NamedEntityGraph(
		name = "shipment-entity-graph",		
		attributeNodes = {
				@NamedAttributeNode("price"),
				@NamedAttributeNode(value = "products", subgraph = "products-subgraph")
		},
		subgraphs = {
				@NamedSubgraph(
						name = "products-subgraph", 
						attributeNodes = { 
								@NamedAttributeNode(value = "priceProduct", subgraph = "priceProduct-subgraph"),
								@NamedAttributeNode("quantity")
						}
				),
				@NamedSubgraph(
						name = "priceProduct-subgraph", 
						attributeNodes = { 
								@NamedAttributeNode("product"),
								@NamedAttributeNode("value")
						}
				)
		}
)

@Entity
@Table(name = "SHIPMENT", uniqueConstraints = {@UniqueConstraint(columnNames = {"id", "price_id"})})
@JsonPropertyOrder({"id", "price_id", "products"})
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
	@OrderBy("id")
	private List<ShipmentProduct> products;
	
	public Shipment() {
		super();
	}

	public Shipment(@NonNull Price price) {
		super();
		this.price = price;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}
	
	@JsonIgnore
	public Price getPrice() {
		return price;
	}

	@JsonSetter("price_id")
	public void setPrice(Price price) {
		this.price = price;
	}

	@JsonProperty
	public Integer getPrice_id() {
		return price!=null?price.getId():null;
	}
	
	public List<ShipmentProduct> getProducts() {
		return products;
	}

	public void setProducts(List<ShipmentProduct> products) {
		this.products = products;
	}

	@Override
	public String toString() {
		return "Shipment [id=" + id
				+ ", price_id=" + (price != null?price.getId():null)
				+ ", products=" + products
				+ "]";
	}
	
}
