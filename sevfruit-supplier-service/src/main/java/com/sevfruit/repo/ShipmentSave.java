package com.sevfruit.repo;

public interface ShipmentSave<T> {
	<S extends T> S save(S entity);
}
