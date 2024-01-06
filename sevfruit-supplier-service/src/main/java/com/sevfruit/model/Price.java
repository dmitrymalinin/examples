package com.sevfruit.model;

import java.util.List;

import jakarta.persistence.CascadeType;
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
 * Прайс лист
 * @author dm
 *
 */
@NamedEntityGraph(
		name = "price-entity-graph",		
		attributeNodes = {
				@NamedAttributeNode("supplier"),
				@NamedAttributeNode("period"),
				@NamedAttributeNode(value = "products", subgraph = "products-subgraph")
		},
		subgraphs = {
				@NamedSubgraph(
						name = "products-subgraph", 
						attributeNodes = { 
								@NamedAttributeNode("product"),
								@NamedAttributeNode("value")
						}
				)
		}
)
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
	@OneToMany(mappedBy = "price", fetch = FetchType.LAZY, cascade = CascadeType.PERSIST)
	@OrderBy("id")
	private List<PriceProduct> products;

	public Price() {
		super();
	}

	public Price(Supplier supplier, Period period) {
		super();
		this.supplier = supplier;
		this.period = period;
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
	
	public void setSupplier_id(int supplier_id) {
		this.supplier = new Supplier(supplier_id);
	}

	public Period getPeriod() {
		return period;
	}

	public void setPeriod(Period period) {
		this.period = period;
	}
	
	public void setPeriod_id(int period_id) {
		this.period = new Period(period_id);
	}

	public List<PriceProduct> getProducts() {
		return products;
	}

	public void setProducts(List<PriceProduct> products) {
		this.products = products;
	}

	@Override
	public String toString() {
		return "Price [id=" + id + ", supplier=" + supplier + ", period=" + period + ", products=" + products + "]";
	}
	
}
