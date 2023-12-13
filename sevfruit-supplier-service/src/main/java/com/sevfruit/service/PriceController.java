package com.sevfruit.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import com.sevfruit.model.Price;
import com.sevfruit.model.PriceProduct;
import com.sevfruit.repo.PriceProductRepository;
import com.sevfruit.repo.PriceRepository;

/**
 * Операции с прайс листами
 * @author dm
 *
 */
@RestController
@RequestMapping(path = "/price")
public class PriceController {
	@Autowired
	private PriceRepository priceRepository;
	@Autowired
	private PriceProductRepository priceProductRepository;
	
	/**
	 * Список всех прайс листов <br/>
	 * {@code  curl http://localhost:8080/price | jq}
	 * @return
	 */
	@GetMapping("")
	public List<Price> getAll()
	{
		return priceRepository.findAll();
	}
	
	/**
	 * Добавить новый прайс лист <br/>
	 * {@code curl -w '\n' -D - -X POST -H "Content-type: application/json" -d '{"supplier": 3, "period": 4, "products": [{"product": 1, "value":111000}]}' http://localhost:8080/price}
	 * @param price
	 * @return
	 */
	@PostMapping("")
	@Transactional
	public Price add(@RequestBody Price price)
	{
		try
		{
			final Price newPrice = priceRepository.save(price);
			if (price.getProducts() != null)
			{
				price.getProducts().forEach(pp -> {
					if (pp.getProduct() != null)
						priceProductRepository.save(new PriceProduct(newPrice.getId(), pp.getProduct().getId(), pp.getValue()));
				});
			}
				
			return newPrice;
		} catch (Exception e)
		{
			throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage());
		}
	}
}
