package com.sevfruit.repo;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.sevfruit.model.Period;

public interface PeriodRepository extends JpaRepository<Period, Integer>{//, CustomizedSave<Period> {
	Optional<Period> findByName(String name);
}
