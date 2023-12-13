package com.sevfruit.repo;

import org.springframework.data.jpa.repository.JpaRepository;

import com.sevfruit.model.PriceProduct;
import com.sevfruit.model.PriceProductId;

public interface PriceProductRepository extends JpaRepository<PriceProduct, PriceProductId> {

}
