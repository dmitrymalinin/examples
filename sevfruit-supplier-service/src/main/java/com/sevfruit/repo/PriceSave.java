package com.sevfruit.repo;

public interface PriceSave<T> {
	<S extends T> S save(S entity);
}
