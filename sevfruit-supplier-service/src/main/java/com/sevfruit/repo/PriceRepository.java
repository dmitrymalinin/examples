package com.sevfruit.repo;

import org.springframework.data.jpa.repository.JpaRepository;

import com.sevfruit.model.Price;

public interface PriceRepository extends JpaRepository<Price, Integer> {

}
