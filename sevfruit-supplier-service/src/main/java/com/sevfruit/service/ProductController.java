package com.sevfruit.service;

import java.net.URI;
import java.util.List;
import java.util.Locale;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

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
	private static final Logger logger = LoggerFactory.getLogger(ProductController.class);
	
	@Autowired
	private MessageSource msgSrc;
	
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
	 * Заданный вид продукции<br/>
	 * {@code  curl -H "Accept-Language: ru" -H "Api-Key: 12345" http://localhost:8080/product/1 | jq}
	 * @param product_id
	 * @param locale
	 * @return
	 */
	@GetMapping("{product_id}")
	public Product get(@PathVariable int product_id, Locale locale)
	{
		final Optional<Product> product = productRepository.findById(product_id);
		if (product.isPresent())
		{
			return product.get();			
		} else
		{
			logger.error("ProductController.get(): product {} not found", product_id);
			throw new ResponseStatusException(HttpStatus.NOT_FOUND, msgSrc.getMessage(ServiceMessages.ERROR_PRODUCT_NOT_FOUND, new Integer[]{product_id}, locale));
		}
	}
	
	/**
	 * Добавить новый продукт<br/>
	 * {@code curl -w '\n' -D - -X POST -H "Api-Key: 12345" -H "Content-type: application/json" -d '{"name": "Яблоки белые"}' http://localhost:8080/product}
	 * @param product
	 * @return
	 */
	@PostMapping("")
	public ResponseEntity<Product> add(@RequestBody Product product)
	{
		try
		{
			final Product newProduct = productRepository.save(product);
			
			final URI location = ServletUriComponentsBuilder.fromCurrentRequestUri()
					.pathSegment("{product_id}").buildAndExpand(newProduct.getId()).toUri();
			
			return ResponseEntity.created(location).body(newProduct);
		} catch (Exception e)
		{
			throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage());
		}
	}
}
