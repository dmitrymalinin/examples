package com.sevfruit.service;

import java.util.List;
import java.util.Locale;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.http.HttpStatus;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
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
	private static final Logger logger = LoggerFactory.getLogger(PriceController.class);
	
	@Autowired
	private MessageSource msgSrc;
	
	@Autowired
	private PriceRepository priceRepository;
	
	@Autowired
	private PriceProductRepository priceProductRepository;
	
	/**
	 * Список всех прайс листов <br/>
	 * {@code curl -H "Api-Key: 12345" http://localhost:8080/price | jq}
	 * @return
	 */
	@GetMapping("")
	public List<Price> getAll()
	{
		return priceRepository.findAll();
	}
	
	/**
	 * Заданный прайс лист <br/>
	 * {@code curl H "Accept-Language: ru" -H "Api-Key: 12345" http://localhost:8080/price/1 | jq}
	 * @param price_id
	 * @param locale
	 * @return
	 */
	@GetMapping("{price_id}")
	public Price get(@PathVariable int price_id, Locale locale)
	{
		final Optional<Price> price = priceRepository.findById(price_id);
		if (price.isPresent())
		{
			return price.get();			
		} else
		{
			logger.error("PriceController.get(): price {} not found", price_id);
			throw new ResponseStatusException(HttpStatus.NOT_FOUND, msgSrc.getMessage(ServiceMessages.ERROR_PRICE_NOT_FOUND, new Integer[]{price_id}, locale));
		}
	}
	
	/**
	 * Добавить новый прайс лист <br/>
	 * {@code curl -w '\n' -D - -X POST -H "Api-Key: 12345" -H "Content-type: application/json" -d '{"supplier": 3, "period": 4, "products": [{"product": 1, "value":111000}]}' http://localhost:8080/price}
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
