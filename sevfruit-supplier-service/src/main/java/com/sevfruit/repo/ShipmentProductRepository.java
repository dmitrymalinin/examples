package com.sevfruit.repo;

import org.springframework.data.jpa.repository.JpaRepository;

import com.sevfruit.model.ShipmentProduct;
import com.sevfruit.model.ShipmentProductId;

public interface ShipmentProductRepository extends JpaRepository<ShipmentProduct, ShipmentProductId> {

}
