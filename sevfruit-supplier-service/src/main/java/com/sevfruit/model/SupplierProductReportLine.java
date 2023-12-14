package com.sevfruit.model;

/**
 * Строка отчёта поступление продукции по поставщикам
 * @author dm
 *
 */
public interface SupplierProductReportLine {
	Supplier getSupplier();
	Product getProduct();
	Float getTotalQuantity();
	Float getTotalValue();
}
