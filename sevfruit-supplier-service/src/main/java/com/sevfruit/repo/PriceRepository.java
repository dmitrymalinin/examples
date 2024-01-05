package com.sevfruit.repo;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

import com.sevfruit.model.Price;

public interface PriceRepository extends JpaRepository<Price, Integer>, PriceSave<Price> {

	@Override
	@EntityGraph("price-entity-graph")
	Optional<Price> findById(Integer id);

	@Override
	@EntityGraph("price-entity-graph")
	List<Price> findAll();

	@Override
	@EntityGraph("price-entity-graph")
	List<Price> findAll(Sort sort);
}
