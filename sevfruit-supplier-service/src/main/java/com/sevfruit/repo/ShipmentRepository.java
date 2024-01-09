package com.sevfruit.repo;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

import com.sevfruit.model.Shipment;

public interface ShipmentRepository extends JpaRepository<Shipment, Integer> , ShipmentSave<Shipment> {

	@Override
	@EntityGraph("shipment-entity-graph")
	Optional<Shipment> findById(Integer id);

	@Override
	@EntityGraph("shipment-entity-graph")
	List<Shipment> findAll(Sort sort);

	@Override
	@EntityGraph("shipment-entity-graph")
	List<Shipment> findAll();

}
