package com.sevfruit.model;

import java.util.List;
import java.util.LinkedList;

/**
 * Отчёт о поставках видов продукции с итогами по количеству и стоимости
 * @author dm
 *
 */
public class SupplierProductsReport {
	private final Supplier supplier;
	private final List<ProductTotal> products = new LinkedList<>();
	
	public SupplierProductsReport(Supplier supplier) {
		super();
		this.supplier = supplier;
	}
	
	public void addProduct(ProductTotal productTotal)
	{
		products.add(productTotal);
	}

	public Supplier getSupplier() {
		return supplier;
	}

	public List<ProductTotal> getProducts() {
		return products;
	}
	
	
}
