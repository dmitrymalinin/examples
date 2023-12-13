package com.sevfruit.repo;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.sevfruit.model.Supplier;

public interface SupplierRepository extends JpaRepository<Supplier, Integer> {
	Optional<Supplier> findByName(String name);
}
