package com.sevfruit.service;

import java.util.List;
import java.util.Locale;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
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
	private static final Logger logger = LoggerFactory.getLogger(SupplierController.class);
	
	@Autowired
	private MessageSource msgSrc;

	@Autowired
	private SupplierRepository supplierRepository;
	
	/**
	 * Список всех поставщиков <br/>
	 * {@code curl -H "Api-Key: 12345" http://localhost:8080/supplier | jq}
	 * @return
	 */
	@GetMapping("")
	public List<Supplier> getAll()
	{
		return supplierRepository.findAll();
	}
	
	/**
	 * Заданный поставщик <br/>
	 * {@code curl -H "Accept-Language: ru" -H "Api-Key: 12345" http://localhost:8080/supplier/1 | jq}
	 * @param supplier_id
	 * @param locale
	 * @return
	 */
	@GetMapping("{supplier_id}")
	public Supplier get(@PathVariable int supplier_id, Locale locale)
	{
		final Optional<Supplier> supplier = supplierRepository.findById(supplier_id);
		if (supplier.isPresent())
		{
			return supplier.get();			
		} else
		{
			logger.error("SupplierController.get(): product {} not found", supplier_id);
			throw new ResponseStatusException(HttpStatus.NOT_FOUND, msgSrc.getMessage(ServiceMessages.ERROR_SUPPLIER_NOT_FOUND, new Integer[]{supplier_id}, locale));
		}
	}
	
	/**
	 * Добавить нового поставщика<br/>
	 * {@code curl -w '\n' -D - -X POST -H "Api-Key: 12345" -H "Content-type: application/json" -d '{"name": "S4"}' http://localhost:8080/supplier}
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
