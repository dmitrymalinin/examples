package com.sevfruit.repo;

public interface CustomizedSave<T> {
	<S extends T> S save(S entity);
}
