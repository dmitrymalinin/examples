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

import com.sevfruit.model.Product;
import com.sevfruit.repo.ProductRepository;

/**
 * Операции с видами продукции
 * @author dm
 *
 */
@RestController
@RequestMapping(path = "/product")
public class ProductController {
	@Autowired
	private ProductRepository productRepository;
	
	/**
	 * Список всей возможной продукции<br/>
	 * {@code  curl -H "Api-Key: 12345" http://localhost:8080/product | jq}
	 * @return
	 */
	@GetMapping("")
	public List<Product> getAll()
	{
		return productRepository.findAll();
	}
	
	/**
	 * Добавить новый продукт<br/>
	 * {@code curl -w '\n' -D - -X POST -H "Api-Key: 12345" -H "Content-type: application/json" -d '{"name": "Яблоки белые"}' http://localhost:8080/product}
	 * @param product
	 * @return
	 */
	@PostMapping("")
	public Product add(@RequestBody Product product)
	{
		try
		{
			return productRepository.save(product);
		} catch (Exception e)
		{
			throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage());
		}
	}
}
