package com.sevfruit.model;

/**
 * Вид продукции с итогами по количеству и стоимости
 * 
 * @author dm
 *
 */
public class ProductTotal {
	private final Product product;
	private final Float totalQuantity;
	private final Float totalValue;

	public static ProductTotal fromReportLine(SupplierProductReportLine line) {
		return new ProductTotal(line.getProduct(), line.getTotalQuantity(), line.getTotalValue());
	}

	private ProductTotal(Product product, Float totalQuantity, Float totalValue) {
		super();
		this.product = product;
		this.totalQuantity = totalQuantity;
		this.totalValue = totalValue;
	}

	public Product getProduct() {
		return product;
	}

	public Float getTotalQuantity() {
		return totalQuantity;
	}

	public Float getTotalValue() {
		return totalValue;
	}
}
