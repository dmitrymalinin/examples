package com.sevfruit.service;

import java.net.URI;
import java.util.List;
import java.util.Locale;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.data.domain.Sort;
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

import com.sevfruit.model.Shipment;
import com.sevfruit.repo.ShipmentRepository;

/**
 * Операции с поставками
 * @author dm
 *
 */
@RestController
@RequestMapping(path = "/shipment")
public class ShipmentController {
	private static final Logger logger = LoggerFactory.getLogger(ShipmentController.class);
	
	private static final Sort sortById = Sort.by("id");
	
	@Autowired
	private MessageSource msgSrc;

	@Autowired
	private ShipmentRepository shipmentRepository;
	
	/**
	 * Список всех поставок <br/>
	 * {@code  curl -H "Api-Key: 12345" http://localhost:8080/shipment | jq}
	 * @return
	 */
	@GetMapping("")
	public List<Shipment> getAll()
	{
		return shipmentRepository.findAll(sortById);
	}
	
	/**
	 * Заданная поставка <br/>
	 * {@code curl H "Accept-Language: ru" -H "Api-Key: 12345" http://localhost:8080/shipment/1 | jq}
	 * @param shipment_id
	 * @param locale
	 * @return
	 */
	@GetMapping("{shipment_id}")
	public Shipment get(@PathVariable int shipment_id, Locale locale)
	{
		final Optional<Shipment> shipment = shipmentRepository.findById(shipment_id);
		if (shipment.isPresent())
		{
			return shipment.get();			
		} else
		{
			logger.error("ShipmentController.get(): shipment {} not found", shipment_id);
			throw new ResponseStatusException(HttpStatus.NOT_FOUND, msgSrc.getMessage(ServiceMessages.ERROR_SHIPMENT_NOT_FOUND, new Integer[]{shipment_id}, locale));
		}
	}
	
	/**
	 * Добавить новую поставку <br/>
	 * {@code curl -w '\n' -D - -X POST -H "Api-Key: 12345" -H "Content-type: application/json" -d '{"price_id":3, "products": [{"priceProduct":{"product_id":4},"quantity":2.0}, {"priceProduct":{"product_id":1},"quantity":12.0}]}' http://localhost:8080/shipment }
	 * @param shipment
	 * @return
	 */
	@PostMapping("")
	public ResponseEntity<Shipment> add(@RequestBody Shipment shipment)
	{
		try
		{
			final Shipment newShipment = shipmentRepository.save(shipment);
			
			final URI location = ServletUriComponentsBuilder.fromCurrentRequestUri()
					.pathSegment("{shipment_id}").buildAndExpand(newShipment.getId()).toUri();
			
			return ResponseEntity.created(location).body(newShipment);
		} catch (Exception e)
		{
			logger.error("ShipmentController.add() failed: ", e);
			throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage());
		}
	}
}
