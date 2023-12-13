package com.sevfruit.repo;

import org.springframework.data.jpa.repository.JpaRepository;

import com.sevfruit.model.Shipment;

public interface ShipmentRepository extends JpaRepository<Shipment, Integer> {

}
