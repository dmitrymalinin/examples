package com.sevfruit.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import com.sevfruit.model.Supplier;
import com.sevfruit.repo.SupplierRepository;

/**
 * Операции с поставщиками
 * @author dm
 *
 */
@RestController
@RequestMapping(path = "/supplier")
public class SupplierController {
	@Autowired
	private SupplierRepository supplierRepository;
	
	/**
	 * Список всех поставщиков <br/>
	 * {@code  curl http://localhost:8080/supplier | jq}
	 * @return
	 */
	@GetMapping("")
	public List<Supplier> getAll()
	{
		return supplierRepository.findAll();
	}
	
	/**
	 * Добавить нового поставщика<br/>
	 * {@code curl -w '\n' -D - -X POST -H "Content-type: application/json" -d '{"name": "S4"}' http://localhost:8080/supplier}
	 * @param supplier
	 * @return
	 */
	@PostMapping("")
	public Supplier add(@RequestBody Supplier supplier)
	{
		try
		{
			return supplierRepository.save(supplier);
		} catch (Exception e)
		{
			throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage());
		}
	}
}
