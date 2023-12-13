package com.sevfruit.repo;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.sevfruit.model.Product;

public interface ProductRepository extends JpaRepository<Product, Integer> {
	Optional<Product> findByName(String name);
}
