package com.sevfruit.repo;

public class CustomizedSaveImpl<T> implements CustomizedSave<T> {

	@Override
	public <S extends T> S save(S entity) {
		System.out.println("CustomizedSaveImpl("+entity+")");
		return null;
	}

	
}
