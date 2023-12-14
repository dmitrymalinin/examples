package com.sevfruit.repo;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.sevfruit.model.Period;
import com.sevfruit.model.SupplierProductReportLine;

public interface PeriodRepository extends JpaRepository<Period, Integer>{//, CustomizedSave<Period> {
	Optional<Period> findByName(String name);
	/** Поступление продукции по поставщикам в заданном периоде */
	@Query("SELECT\n"
			+ " S AS supplier, P AS product, sum(SP.quantity) AS totalQuantity, sum(PP.value) AS totalValue\n"
			+ "FROM\n"
			+ " ShipmentProduct SP\n"
			+ " JOIN SP.priceProduct PP\n"
			+ " JOIN PP.price PR\n"
			+ " JOIN PP.product P\n"			
			+ " JOIN PR.supplier S\n"
			+ "WHERE PR.period = ?1\n"
			+ "GROUP BY S.id, P.id\n"
			+ "ORDER BY S.id ")
	List<SupplierProductReportLine> getSupplierProductReport(Period period);
}
